package examples

import java.nio.charset.StandardCharsets
import java.util.Base64

import cats.effect.IO
import cats.effect.IOApp
import cats.effect.Resource
import cats.syntax.all.*
import io.circe.Json
import scaleway.keymanager.api.KeysApi
import scaleway.keymanager.models.CreateKeyRequest
import scaleway.keymanager.models.CreateKeyRequestUsage
import scaleway.keymanager.models.CreateKeyRequestUsageEnums
import scaleway.keymanager.models.DecryptRequest
import scaleway.keymanager.models.DecryptRequestAssociatedData
import scaleway.keymanager.models.EncryptRequest
import scaleway.keymanager.models.EncryptRequestAssociatedData
import scaleway.keymanager.models.GenerateDataKeyRequest
import scaleway.keymanager.models.GenerateDataKeyRequestEnums
import scaleway.keymanager.models.Key

/** Key Manager (KMS): the key material never leaves Scaleway.
  *
  * You send plaintext and get ciphertext back -- the key itself is not exportable, so `encrypt`/`decrypt` are round
  * trips to the API and are capped at a few KB. For anything larger the pattern is envelope encryption: ask KMS for a
  * *data key*, encrypt the payload locally with its plaintext, store only the wrapped copy, and throw the plaintext
  * away. This example does both.
  *
  * `associatedData` is authenticated but not encrypted: decryption fails unless the exact same value is supplied, which
  * is how you bind a ciphertext to the context it was created for.
  */
object KeyManagerExample extends IOApp.Simple {

  def run: IO[Unit] = Scw.resource.use { scw =>
    val credentials = scw.credentials
    val keys = KeysApi.withApiKeyAuth(Scw.baseUrl, credentials.secretKey)

    val region = credentials.region

    def encode(value: String): String =
      Base64.getEncoder.encodeToString(value.getBytes(StandardCharsets.UTF_8))

    // The same context, spelled with the request-specific wrapper each endpoint declares.
    val context = encode("tenant=acme")
    val encryptionContext = EncryptRequestAssociatedData(value = Some(context))
    val decryptionContext = DecryptRequestAssociatedData(value = Some(context))

    def decode(value: String): String =
      new String(Base64.getDecoder.decode(value), StandardCharsets.UTF_8)

    val listExisting =
      for {
        page <- scw.run(
          keys.listKeys(
            region,
            scheduledForDeletion = false,
            projectId = Some(credentials.projectId),
            pageSize = Some(50),
            tags = Seq.empty
          )
        )
        _ <- IO.println(s"${page.totalCount.getOrElse(0)} key(s) in $region:")
        _ <- page.keys.getOrElse(Seq.empty).traverse_ { key =>
          IO.println(
            s"  ${key.name.getOrElse("?")} ${key.state.fold("?")(_.toString)} " +
              s"(rotated ${key.rotationCount.getOrElse(0)} time(s))"
          )
        }
      } yield ()

    def key(name: String): Resource[IO, Key] =
      Resource.make(
        scw.run(
          keys.createKey(
            region,
            CreateKeyRequest(
              name = Some(name),
              projectId = Some(credentials.projectId),
              // Usage fixes the algorithm for the key's whole life; a symmetric key cannot later sign.
              usage = Some(
                CreateKeyRequestUsage(
                  symmetricEncryption = Some(CreateKeyRequestUsageEnums.SymmetricEncryption.`aes_256_gcm`)
                )
              ),
              description = Some("Created by KeyManagerExample -- safe to delete"),
              tags = Some(Seq("example", "scaleway-scala")),
              // Keys are protected by default and then refuse deletion; a throwaway key must opt out.
              unprotected = Some(true)
            )
          )
        )
      )(created => created.id.orFail("key.id").flatMap(id => scw.run(keys.deleteKey(region, id)).void))

    val provision =
      key("example-envelope-key").use { created =>
        for {
          keyId <- created.id.orFail("key.id")
          _ <- IO.println(s"created key $keyId")

          // Direct encryption -- fine for short payloads such as a token or a password.
          encrypted <- scw.run(
            keys.encrypt(
              region,
              keyId,
              EncryptRequest(
                plaintext = Some(encode("a short secret")),
                associatedData = Some(encryptionContext)
              )
            )
          )
          ciphertext <- encrypted.ciphertext.orFail("encrypt.ciphertext")
          _ <- IO.println(s"ciphertext is ${ciphertext.length} base64 chars")
          decrypted <- scw.run(
            keys.decrypt(
              region,
              keyId,
              // Same associated data as on the way in, or the API refuses to decrypt.
              DecryptRequest(ciphertext = Some(ciphertext), associatedData = Some(decryptionContext))
            )
          )
          plaintext <- decrypted.plaintext.orFail("decrypt.plaintext")
          _ <- IO.println(s"round-tripped: ${decode(plaintext)}")

          // Envelope encryption -- the data key comes back both in the clear and wrapped by the KMS key. Encrypt
          // locally with the former, persist the latter, and keep neither the plaintext key nor the KMS key material.
          dataKey <- scw.run(
            keys.generateDataKey(
              region,
              keyId,
              GenerateDataKeyRequest(
                algorithm = Some(GenerateDataKeyRequestEnums.Algorithm.`aes_256_gcm`),
                withoutPlaintext = Some(false)
              )
            )
          )
          wrapped <- dataKey.ciphertext.orFail("dataKey.ciphertext")
          _ <- IO.println(s"data key issued; store the ${wrapped.length}-char wrapped copy alongside the payload")

          // Rotation creates new material for future encryptions; existing ciphertexts still decrypt.
          rotated <- scw.run(keys.rotateKey(region, keyId, Json.obj()))
          _ <- IO.println(s"rotation count is now ${rotated.rotationCount.getOrElse(0)}")
          _ <- IO.println("deleting the key...")
        } yield ()
      }

    listExisting *> provision
  }

}
