package examples

import scala.concurrent.duration.*

import cats.effect.IO
import cats.effect.IOApp
import cats.effect.Resource
import cats.syntax.all.*

import io.circe.Json
import scaleway.containers.api.ContainersApi
import scaleway.containers.api.NamespacesApi
import scaleway.containers.models.Container
import scaleway.containers.models.ContainerEnums
import scaleway.containers.models.CreateContainerRequest
import scaleway.containers.models.CreateContainerRequestEnums
import scaleway.containers.models.CreateNamespaceRequest
import scaleway.containers.models.Namespace
import scaleway.containers.models.NamespaceEnums

/**
  * Serverless Containers: a namespace, a container, and an explicit deploy.
  *
  * The namespace is the unit of isolation -- it owns the registry the images are pulled from and
  * the environment variables every container in it inherits. Creating a container only registers
  * its *configuration*; nothing is pulled or started until `deployContainer`, which is why a
  * freshly created container sits in `created` rather than `ready`.
  *
  * `minScale = 0` is the serverless default: no instance runs, and no compute is billed, until a
  * request arrives.
  */
object ContainersExample extends IOApp.Simple {

  def run: IO[Unit] = Scw.resource
    .use { scw =>
      val credentials = scw.credentials
      val namespaces  = NamespacesApi.withApiKeyAuth(Scw.baseUrl, credentials.secretKey)
      val containers  = ContainersApi.withApiKeyAuth(Scw.baseUrl, credentials.secretKey)

      val region = credentials.region

      val listExisting =
        for {
          page <- scw.run(
                    namespaces.listNamespaces(
                      region,
                      projectId = Some(credentials.projectId),
                      pageSize = Some(50)
                    )
                  )
          _ <- IO.println(s"${page.totalCount.getOrElse(0)} namespace(s) in $region:")
          _ <-
            page.namespaces
              .getOrElse(Seq.empty)
              .traverse_ { namespace =>
                IO.println(
                  s"  ${namespace.name.getOrElse("?")} ${namespace.status.fold("?")(_.toString)} " +
                    s"registry=${namespace.registryEndpoint.getOrElse("-")}"
                )
              }
        } yield ()

      def namespace(name: String): Resource[IO, Namespace] =
        Resource.make(
          scw.run(
            namespaces.createNamespace(
              region,
              CreateNamespaceRequest(
                name = Some(name),
                projectId = Some(credentials.projectId),
                description = Some("Created by ContainersExample -- safe to delete"),
                tags = Some(Seq("example", "scaleway-scala"))
              )
            )
          )
        )(created =>
          created.id
            .orFail("namespace.id")
            .flatMap(id => scw.run(namespaces.deleteNamespace(region, id)).void)
        )

      def awaitNamespace(namespaceId: String, attemptsLeft: Int): IO[Namespace] =
        scw
          .run(namespaces.getNamespace(region, namespaceId))
          .flatMap { current =>
            current.status match {
              case Some(NamespaceEnums.Status.`ready`) => IO.pure(current)
              case other if attemptsLeft > 0           =>
                IO.println(s"  namespace status=${other.fold("?")(_.toString)}, waiting...") *>
                  IO.sleep(5.seconds) *>
                  awaitNamespace(namespaceId, attemptsLeft - 1)
              case other =>
                IO.raiseError(
                  new RuntimeException(s"namespace never became ready (last status: $other)")
                )
            }
          }

      def awaitContainer(containerId: String, attemptsLeft: Int): IO[Container] =
        scw
          .run(containers.getContainer(region, containerId))
          .flatMap { current =>
            current.status match {
              case Some(ContainerEnums.Status.`ready`) => IO.pure(current)
              case Some(ContainerEnums.Status.`error`) =>
                IO.raiseError(
                  new RuntimeException(
                    s"deploy failed: ${current.errorMessage.getOrElse("no detail")}"
                  )
                )
              case other if attemptsLeft > 0 =>
                IO.println(s"  container status=${other.fold("?")(_.toString)}, waiting...") *>
                  IO.sleep(5.seconds) *>
                  awaitContainer(containerId, attemptsLeft - 1)
              case other =>
                IO.raiseError(
                  new RuntimeException(s"container never became ready (last status: $other)")
                )
            }
          }

      val provision =
        namespace("example-namespace").use { created =>
          for {
            namespaceId <- created.id.orFail("namespace.id")
            _           <- IO.println(s"created namespace $namespaceId")
            ready       <- awaitNamespace(namespaceId, attemptsLeft = 60)
            _           <-
              IO.println(s"namespace ready, registry at ${ready.registryEndpoint.getOrElse("?")}")

            container <- scw.run(
                           containers.createContainer(
                             region,
                             CreateContainerRequest(
                               namespaceId = Some(namespaceId),
                               name = Some("example-container"),
                               registryImage = Some("docker.io/library/nginx:alpine"),
                               port = Some(80),
                               protocol = Some(CreateContainerRequestEnums.Protocol.`http1`),
                               privacy = Some(CreateContainerRequestEnums.Privacy.`public`),
                               // Scale to zero between requests; the first request after idling pays a cold start.
                               minScale = Some(0),
                               maxScale = Some(3),
                               memoryLimit = Some(256),
                               cpuLimit = Some(140),
                               description = Some("Created by ContainersExample"),
                               tags = Some(Seq("example"))
                             )
                           )
                         )
            containerId <- container.id.orFail("container.id")
            _           <- IO.println(
                   s"registered container $containerId (${container.status.fold("?")(_.toString)})"
                 )

            // Nothing has been pulled yet -- this is the call that actually rolls the image out.
            _        <- scw.run(containers.deployContainer(region, containerId, Json.obj())).void
            deployed <- awaitContainer(containerId, attemptsLeft = 60)
            _        <- IO.println(s"serving at https://${deployed.domainName.getOrElse("?")}")

            page <- scw.run(containers.listContainers(region, namespaceId))
            _    <- IO.println(s"${page.totalCount.getOrElse(0)} container(s) in the namespace")
            // Deleting the namespace would take the containers with it; doing it explicitly keeps the teardown ordered.
            _ <- scw.run(containers.deleteContainer(region, containerId)).void
            _ <- IO.println("deleted the container, dropping the namespace...")
          } yield ()
        }

      listExisting *> provision
    }

}
