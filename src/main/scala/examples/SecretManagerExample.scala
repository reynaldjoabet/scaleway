package examples

import java.nio.charset.StandardCharsets
import java.util.Base64

import cats.effect.IO
import cats.effect.IOApp
import cats.effect.Resource
import cats.syntax.all.*
import scaleway.secretmanager.api.SecretVersionsApi
import scaleway.secretmanager.api.SecretsApi
import scaleway.secretmanager.models.CreateSecretRequest
import scaleway.secretmanager.models.CreateSecretRequestEnums
import scaleway.secretmanager.models.CreateSecretVersionRequest
import scaleway.secretmanager.models.Secret

/** Secret Manager: a secret is a named container, versions hold the actual bytes.
  *
  * Rotation is modelled as "add a version", not "overwrite the secret": revisions are numbered from 1, the newest is
  * addressable as `latest`, and old ones stay readable until disabled or deleted. That is what lets a deploy roll
  * forward and back without the secret's identity changing.
  *
  * Payloads are base64 on the wire in both directions -- the API stores opaque bytes and never inspects them.
  */
object SecretManagerExample extends IOApp.Simple {

  def run: IO[Unit] = Scw.resource.use { scw =>
    val credentials = scw.credentials
    val secrets = SecretsApi.withApiKeyAuth(Scw.baseUrl, credentials.secretKey)
    val versions = SecretVersionsApi.withApiKeyAuth(Scw.baseUrl, credentials.secretKey)

    val region = credentials.region

    def encode(value: String): String =
      Base64.getEncoder.encodeToString(value.getBytes(StandardCharsets.UTF_8))

    def decode(value: String): String =
      new String(Base64.getDecoder.decode(value), StandardCharsets.UTF_8)

    val listExisting =
      for {
        page <- scw.run(
          secrets.listSecrets(
            region,
            scheduledForDeletion = false,
            projectId = Some(credentials.projectId),
            pageSize = Some(50),
            tags = Seq.empty
          )
        )
        _ <- IO.println(s"${page.totalCount.getOrElse(0)} secret(s) in $region:")
        _ <- page.secrets.getOrElse(Seq.empty).traverse_ { secret =>
          IO.println(
            s"  ${secret.path.getOrElse("/")}${secret.name.getOrElse("?")} " +
              s"(${secret.versionCount.getOrElse(0)} version(s), ${secret.status.fold("?")(_.toString)})"
          )
        }
      } yield ()

    def secret(name: String): Resource[IO, Secret] =
      Resource.make(
        scw.run(
          secrets.createSecret(
            region,
            CreateSecretRequest(
              name = Some(name),
              projectId = Some(credentials.projectId),
              // Paths are folders in the console; they are part of the secret's identity, not just a label.
              path = Some("/examples"),
              description = Some("Created by SecretManagerExample -- safe to delete"),
              `type` = Some(CreateSecretRequestEnums.Type.`opaque`),
              tags = Some(Seq("example", "scaleway-scala")),
              // `protected` secrets refuse deletion until unprotected -- the opposite of what a throwaway wants.
              `protected` = Some(false)
            )
          )
        )
      )(created => created.id.orFail("secret.id").flatMap(id => scw.run(secrets.deleteSecret(region, id)).void))

    val provision =
      secret("example-database-password").use { created =>
        for {
          secretId <- created.id.orFail("secret.id")
          _ <- IO.println(s"created secret $secretId")
          first <- scw.run(
            versions.createSecretVersion(
              region,
              secretId,
              CreateSecretVersionRequest(
                data = Some(encode("correct-horse-battery-staple")),
                description = Some("initial value")
              )
            )
          )
          _ <- IO.println(s"stored revision ${first.revision.getOrElse(0)}")
          // Rotation: a new revision, with the previous one disabled in the same call.
          rotated <- scw.run(
            versions.createSecretVersion(
              region,
              secretId,
              CreateSecretVersionRequest(
                data = Some(encode("hunter2-but-longer")),
                description = Some("rotated"),
                disablePrevious = Some(true)
              )
            )
          )
          _ <- IO.println(s"rotated to revision ${rotated.revision.getOrElse(0)}, previous one disabled")
          // `latest` is a magic revision selector; a plain number reads that exact revision instead.
          accessed <- scw.run(versions.accessSecretVersion(region, secretId, "latest"))
          payload <- accessed.data.orFail("version.data")
          _ <- IO.println(s"read back revision ${accessed.revision.getOrElse(0)}: ${decode(payload)}")
          history <- scw.run(versions.listSecretVersions(region, secretId, status = Seq.empty))
          _ <- history.versions.getOrElse(Seq.empty).traverse_ { version =>
            IO.println(s"  revision ${version.revision.getOrElse(0)}: ${version.status.fold("?")(_.toString)}")
          }
          _ <- IO.println("deleting the secret and all its versions...")
        } yield ()
      }

    listExisting *> provision
  }

}
