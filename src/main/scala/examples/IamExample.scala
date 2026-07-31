package examples

import java.time.OffsetDateTime

import cats.effect.IO
import cats.effect.IOApp
import cats.effect.Resource
import cats.syntax.all.*

import scaleway.iam.api.APIKeysApi
import scaleway.iam.api.ApplicationsApi
import scaleway.iam.api.PermissionSetsApi
import scaleway.iam.api.PoliciesApi
import scaleway.iam.models.APIKey
import scaleway.iam.models.Application
import scaleway.iam.models.CreateAPIKeyRequest
import scaleway.iam.models.CreateApplicationRequest
import scaleway.iam.models.CreatePolicyRequest
import scaleway.iam.models.Policy
import scaleway.iam.models.RuleSpecs

/**
  * IAM: give a workload its own identity.
  *
  * The Scaleway model is that an *application* is a non-human principal, a *policy* grants it
  * permission sets scoped to an Organization or to specific Projects, and an *API key* is the
  * credential it authenticates with. All three are separate resources, so this example creates them
  * as nested `Resource`s -- the API key is released before the application that bears it, which is
  * the order the API requires.
  */
object IamExample extends IOApp.Simple {

  def run: IO[Unit] = Scw.resource
    .use { scw =>
      val credentials    = scw.credentials
      val applications   = ApplicationsApi.withApiKeyAuth(Scw.baseUrl, credentials.secretKey)
      val apiKeys        = APIKeysApi.withApiKeyAuth(Scw.baseUrl, credentials.secretKey)
      val policies       = PoliciesApi.withApiKeyAuth(Scw.baseUrl, credentials.secretKey)
      val permissionSets = PermissionSetsApi.withApiKeyAuth(Scw.baseUrl, credentials.secretKey)

      /**
        * Created up front and deleted on the way out, whatever happens in between.
        */
      def application(name: String): Resource[IO, Application] =
        Resource.make(
          scw.run(
            applications.createApplication(
              CreateApplicationRequest(
                name = name,
                organizationId = Some(credentials.organizationId),
                description = Some("Created by IamExample -- safe to delete"),
                tags = Some(Seq("example", "scaleway-scala"))
              )
            )
          )
        )(created =>
          created.id
            .orFail("application.id")
            .flatMap(id => scw.run(applications.deleteApplication(id)))
        )

      def policy(applicationId: String): Resource[IO, Policy] =
        Resource.make(
          scw.run(
            policies.createPolicy(
              CreatePolicyRequest(
                name = "example-read-only",
                organizationId = Some(credentials.organizationId),
                applicationId = Some(applicationId),
                description = Some("Read-only access to a single Project"),
                // A rule is a set of permission sets plus a scope. Scoping to `projectIds` keeps the grant inside one
                // Project; passing `organizationId` instead would widen it to the whole Organization.
                rules = Some(
                  Seq(
                    RuleSpecs(
                      permissionSetNames = Some(Seq("VPCReadOnly", "InstancesReadOnly")),
                      projectIds = Some(Seq(credentials.projectId))
                    )
                  )
                )
              )
            )
          )
        )(created =>
          created.id.orFail("policy.id").flatMap(id => scw.run(policies.deletePolicy(id)))
        )

      def apiKey(applicationId: String): Resource[IO, APIKey] =
        Resource.make(
          scw.run(
            apiKeys.createAPIKey(
              CreateAPIKeyRequest(
                applicationId = Some(applicationId),
                description = Some("Created by IamExample"),
                // Object Storage requests made with this key are billed to this Project.
                defaultProjectId = Some(credentials.projectId),
                expiresAt = Some(OffsetDateTime.now().plusDays(1))
              )
            )
          )
        )(created =>
          created.accessKey
            .orFail("apiKey.access_key")
            .flatMap(key => scw.run(apiKeys.deleteAPIKey(key)))
        )

      val listExisting =
        for {
          // `applicationIds` has no default: the generator emits array query parameters as plain `Seq`s, so an empty
          // Seq is how you say "no filter".
          page <- scw.run(
                    applications.listApplications(
                      organizationId = Some(credentials.organizationId),
                      pageSize = Some(100),
                      applicationIds = Seq.empty
                    )
                  )
          _ <- IO.println(s"${page.totalCount.getOrElse(0)} application(s) in the Organization:")
          _ <-
            page.applications
              .getOrElse(Seq.empty)
              .traverse_ { app =>
                IO.println(
                  s"  ${app.name.getOrElse("<unnamed>")} (${app.nbApiKeys.getOrElse(0)} API keys)"
                )
              }
        } yield ()

      val listPermissionSets =
        for {
          page <-
            scw.run(
              permissionSets.listPermissionSets(credentials.organizationId, pageSize = Some(5))
            )
          _ <-
            IO.println(s"${page.totalCount.getOrElse(0)} permission set(s) available, first few:")
          _ <- page.permissionSets
                 .getOrElse(Seq.empty)
                 .traverse_ { set =>
                   IO.println(s"  ${set.name.getOrElse("?")} -- ${set.description.getOrElse("")}")
                 }
        } yield ()

      val provision =
        application("example-workload").use { app =>
          for {
            appId <- app.id.orFail("application.id")
            _     <- IO.println(s"created application $appId")
            _     <-
              (policy(appId), apiKey(appId)).tupled
                .use { case (createdPolicy, createdKey) =>
                  for {
                    _ <-
                      IO.println(
                        s"attached policy ${createdPolicy.name.getOrElse("?")} to the application"
                      )
                    // The secret key is returned exactly once, at creation; afterwards the API only ever returns
                    // the access key.
                    _ <- IO.println(s"issued access key ${createdKey.accessKey.getOrElse("?")}")
                    _ <- IO.println("tearing everything back down...")
                  } yield ()
                }
          } yield ()
        }

      listExisting *> listPermissionSets *> provision
    }

}
