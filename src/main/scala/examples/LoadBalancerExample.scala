package examples

import scala.concurrent.duration.*

import cats.effect.IO
import cats.effect.IOApp
import cats.effect.Resource
import cats.syntax.all.*

import io.circe.Json
import scaleway.lb.api.BackendsApi
import scaleway.lb.api.FrontendsApi
import scaleway.lb.api.LoadBalancerApi
import scaleway.lb.models.CreateBackendRequest
import scaleway.lb.models.CreateBackendRequestEnums
import scaleway.lb.models.CreateBackendRequestHealthCheck
import scaleway.lb.models.CreateFrontendRequest
import scaleway.lb.models.CreateLbRequest
import scaleway.lb.models.Lb
import scaleway.lb.models.LbEnums

/**
  * Load Balancer: one LB, one backend pool, one listener.
  *
  * The three objects are separate resources and must be created in order -- a frontend has to name
  * an existing backend, and a backend has to name an existing Load Balancer. A frontend is the
  * public side (which port to listen on, which certificate to serve), a backend is the private side
  * (which servers, which algorithm, how to health-check them).
  *
  * Note the health check is not optional in `CreateBackendRequest`: an LB that cannot tell healthy
  * servers from dead ones is not something the API will let you build.
  */
object LoadBalancerExample extends IOApp.Simple {

  def run: IO[Unit] = Scw.resource
    .use { scw =>
      val credentials  = scw.credentials
      val loadBalancer = LoadBalancerApi.withApiKeyAuth(Scw.baseUrl, credentials.secretKey)
      val backends     = BackendsApi.withApiKeyAuth(Scw.baseUrl, credentials.secretKey)
      val frontends    = FrontendsApi.withApiKeyAuth(Scw.baseUrl, credentials.secretKey)

      val zone = credentials.zone

      val listExisting =
        for {
          page <- scw.run(
                    loadBalancer.listLbs(
                      zone,
                      projectId = Some(credentials.projectId),
                      pageSize = Some(50),
                      tags = Seq.empty,
                      lbIds = Seq.empty
                    )
                  )
          _ <- IO.println(s"${page.totalCount.getOrElse(0)} Load Balancer(s) in $zone:")
          _ <- page.lbs
                 .getOrElse(Seq.empty)
                 .traverse_ { lb =>
                   val addresses = lb.ip.getOrElse(Seq.empty).flatMap(_.ipAddress).mkString(", ")
                   IO.println(
                     s"  ${lb.name.getOrElse("?")} [${lb.`type`.getOrElse("?")}] " +
                       s"${lb.status.fold("?")(_.toString)} $addresses " +
                       s"(${lb.frontendCount.getOrElse(0)} frontend(s), ${lb.backendCount
                           .getOrElse(0)} backend(s))"
                   )
                 }
        } yield ()

      def lb(name: String): Resource[IO, Lb] =
        Resource.make(
          scw.run(
            loadBalancer.createLb(
              zone,
              CreateLbRequest(
                name = name,
                projectId = Some(credentials.projectId),
                description = Some("Created by LoadBalancerExample -- safe to delete"),
                `type` = Some("LB-S"),
                // Allocate a flexible public IPv4 with the LB rather than reusing a reserved one.
                assignFlexibleIp = Some(true),
                tags = Some(Seq("example", "scaleway-scala"))
              )
            )
          )
        )(created =>
          created.id
            .orFail("lb.id")
            // `releaseIp = false` would keep the flexible IP reserved (and billed) after the LB is gone.
            .flatMap(id => scw.run(loadBalancer.deleteLb(zone, id, releaseIp = true)))
        )

      def awaitReady(lbId: String, attemptsLeft: Int): IO[Lb] =
        scw
          .run(loadBalancer.getLb(zone, lbId))
          .flatMap { current =>
            current.status match {
              case Some(LbEnums.Status.`ready`) => IO.pure(current)
              case other if attemptsLeft > 0    =>
                IO.println(s"  status=${other.fold("?")(_.toString)}, waiting...") *>
                  IO.sleep(10.seconds) *>
                  awaitReady(lbId, attemptsLeft - 1)
              case other =>
                IO.raiseError(
                  new RuntimeException(s"Load Balancer never became ready (last status: $other)")
                )
            }
          }

      val provision =
        lb("example-lb").use { created =>
          for {
            lbId  <- created.id.orFail("lb.id")
            _     <- IO.println(s"created Load Balancer $lbId, waiting for it to come up...")
            ready <- awaitReady(lbId, attemptsLeft = 30)
            _     <- IO.println(
                   s"ready at ${ready.ip.getOrElse(Seq.empty).flatMap(_.ipAddress).mkString(", ")}"
                 )

            backend <- scw.run(
                         backends.createBackend(
                           zone,
                           lbId,
                           CreateBackendRequest(
                             name = "web",
                             forwardProtocol = CreateBackendRequestEnums.ForwardProtocol.`http`,
                             forwardPort = 8080,
                             forwardPortAlgorithm =
                               CreateBackendRequestEnums.ForwardPortAlgorithm.`roundrobin`,
                             stickySessions = CreateBackendRequestEnums.StickySessions.`none`,
                             healthCheck = CreateBackendRequestHealthCheck(
                               port = Some(8080),
                               checkMaxRetries = Some(3),
                               // Protocol-specific check configs are free-form in the spec, so they surface as raw
                               // JSON. An empty object selects a plain TCP connect check.
                               tcpConfig = Some(Json.obj())
                             ),
                             // Backend servers are addressed by IP -- private addresses if the LB is attached to a
                             // Private Network, public ones otherwise.
                             serverIp = Seq("10.0.0.11", "10.0.0.12"),
                             timeoutServer = Some(30000.0),
                             timeoutConnect = Some(5000.0)
                           )
                         )
                       )
            backendId <- backend.id.orFail("backend.id")
            _         <-
              IO.println(s"created backend $backendId over ${backend.pool.getOrElse(Seq.empty).size} server(s)")

            frontend <- scw.run(
                          frontends.createFrontend(
                            zone,
                            lbId,
                            CreateFrontendRequest(
                              name = "http",
                              inboundPort = 80,
                              backendId = backendId,
                              timeoutClient = Some(30000.0)
                            )
                          )
                        )
            _ <- IO.println(s"listening on port ${frontend.inboundPort.getOrElse(0)}")

            summary <- scw.run(backends.listBackends(zone, lbId))
            _       <- IO.println(s"${summary.totalCount.getOrElse(0)} backend(s) attached")
            _       <- IO.println("deleting the Load Balancer and releasing its IP...")
          } yield ()
        }

      listExisting *> provision
    }

}
