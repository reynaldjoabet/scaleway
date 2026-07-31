package examples

import scala.concurrent.duration.*

import cats.effect.IO
import cats.effect.IOApp
import cats.syntax.all.*

import scaleway.instance.api.ImagesApi
import scaleway.instance.api.InstancesApi
import scaleway.instance.models.CreateServerRequest
import scaleway.instance.models.Server
import scaleway.instance.models.ServerActionRequest
import scaleway.instance.models.ServerActionRequestEnums
import scaleway.instance.models.ServerEnums

/**
  * Instances: boot a VM, wait for it, tear it down.
  *
  * Instance is a *zonal* API -- `fr-par-1` and `fr-par-2` are separate inventories, and an image id
  * from one zone is meaningless in the other, so the zone is the first parameter of every call.
  * Creating a server does not start it: `createServer` allocates it in `stopped` state and a
  * separate `poweron` action boots it, which is why this example has to poll before the instance is
  * usable.
  */
object InstanceExample extends IOApp.Simple {

  def run: IO[Unit] = Scw.resource
    .use { scw =>
      val credentials = scw.credentials
      val instances   = InstancesApi.withApiKeyAuth(Scw.baseUrl, credentials.secretKey)
      val images      = ImagesApi.withApiKeyAuth(Scw.baseUrl, credentials.secretKey)

      val zone = credentials.zone

      val listExisting =
        for {
          page <-
            scw.run(
              instances.listServers(zone, project = Some(credentials.projectId), perPage = Some(50))
            )
          servers = page.servers.getOrElse(Seq.empty)
          _      <- IO.println(s"${servers.size} Instance(s) in $zone:")
          _      <- servers.traverse_ { server =>
                 val address = server.publicIp.flatMap(_.address).getOrElse("no public IP")
                 IO.println(
                   s"  ${server.name.getOrElse("?")} " +
                     s"[${server.commercialType.getOrElse("?")}] " +
                     s"${server.state.fold("?")(_.toString)} $address"
                 )
               }
        } yield ()

      /**
        * Marketplace images are public and zone-local; the newest match wins.
        */
      val ubuntuImage =
        for {
          page <- scw.run(
                    images.listImages(
                      zone,
                      public = Some(true),
                      arch = Some("x86_64"),
                      perPage = Some(100)
                    )
                  )
          image <- page.images
                     .getOrElse(Seq.empty)
                     .filter(_.name.exists(_.startsWith("Ubuntu 24.04")))
                     .maxByOption(_.creationDate.map(_.toInstant.toEpochMilli).getOrElse(0L))
                     .orFail("an Ubuntu 24.04 image")
          id <- image.id.orFail("image.id")
          _  <- IO.println(s"booting from ${image.name.getOrElse("?")} ($id)")
        } yield id

      /**
        * Polls until the Instance reaches `running`. Transitions are not instant and the API has no
        * "wait" endpoint, so every client ends up writing some version of this loop.
        */
      def awaitRunning(serverId: String, attemptsLeft: Int): IO[Server] =
        scw
          .run(instances.getServer(zone, serverId))
          .flatMap { response =>
            val server = response.server
            server.flatMap(_.state) match {
              case Some(ServerEnums.State.`running`) => server.orFail("server")
              case other if attemptsLeft > 0         =>
                IO.println(s"  state=${other.fold("?")(_.toString)}, waiting...") *>
                  IO.sleep(5.seconds) *>
                  awaitRunning(serverId, attemptsLeft - 1)
              case other =>
                IO.raiseError(
                  new RuntimeException(s"Instance never reached `running` (last state: $other)")
                )
            }
          }

      def action(serverId: String, what: ServerActionRequestEnums.Action): IO[Unit] =
        scw
          .run(instances.serverAction(zone, serverId, ServerActionRequest(action = Some(what))))
          .void

      val provision =
        for {
          imageId <- ubuntuImage
          created <- scw.run(
                       instances.createServer(
                         zone,
                         CreateServerRequest(
                           name = "example-instance",
                           commercialType = "DEV1-S",
                           image = Some(imageId),
                           project = Some(credentials.projectId),
                           // A dynamic public IPv4 is attached at boot and released on delete -- fine for a throwaway
                           // instance, but flexible IPs (the IPs API) are what you want for anything long-lived.
                           dynamicIpRequired = Some(true),
                           routedIpEnabled = Some(true),
                           tags = Some(Seq("example", "scaleway-scala"))
                         )
                       )
                     )
          serverId <- created.server.flatMap(_.id).orFail("server.id")
          _        <- IO.println(s"created Instance $serverId (stopped)")
          _        <- action(serverId, ServerActionRequestEnums.Action.`poweron`)
          running  <- awaitRunning(serverId, attemptsLeft = 60)
          _        <- IO.println(
                 s"running at ${running.publicIp.flatMap(_.address).getOrElse("<no public IP>")}"
               )
          // `terminate` powers off, deletes the server *and* releases its volumes and dynamic IP; `deleteServer` would
          // leave the volumes behind, still billed.
          _ <- IO.println("terminating...")
          _ <- action(serverId, ServerActionRequestEnums.Action.`terminate`)
        } yield ()

      listExisting *> provision
    }

}
