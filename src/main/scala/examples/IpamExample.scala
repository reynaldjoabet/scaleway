package examples

import cats.effect.std.Env
import cats.effect.IO
import cats.effect.IOApp
import cats.syntax.all.*

import io.circe.Json
import scaleway.ipam.api.IPs
import scaleway.ipam.models.BookIPRequest
import scaleway.ipam.models.BookIPRequestSource
import scaleway.ipam.models.IP
import scaleway.ipam.models.ModelType

/**
  * IPAM: the inventory behind every private address Scaleway hands out.
  *
  * Whenever an Instance gets a private NIC, a Load Balancer joins a Private Network or a Kubernetes
  * node comes up, the address it receives is booked in IPAM. That makes IPAM the one place to
  * answer "what is using 172.16.4.37?" without querying each product API in turn -- every IP
  * carries the `resource` that holds it.
  *
  * Booking an IP directly (as this example does at the end) reserves it *before* the resource
  * exists, which is how you pin a database or gateway to a stable private address.
  */
object IpamExample extends IOApp.Simple {

  def run: IO[Unit] = Scw.resource
    .use { scw =>
      val credentials = scw.credentials
      val ips         = IPs.withApiKeyAuth(Scw.baseUrl, credentials.secretKey)

      val region = credentials.region

      def describe(ip: IP): String = {
        val holder = ip.resource match {
          case Some(resource) =>
            val kind = resource.`type`.fold("?")(_.toString)
            val name = resource.name.orElse(resource.id).getOrElse("?")
            s"$kind $name"
          case None => "unattached"
        }
        s"  ${ip.address.getOrElse("?")} -> $holder"
      }

      val inventory =
        for {
          page <- scw.run(
                    ips.listIPs(
                      region,
                      projectId = Some(credentials.projectId),
                      pageSize = Some(100),
                      // Every array filter is a plain `Seq`; empty means "don't filter on this".
                      resourceIds = Seq.empty,
                      resourceTypes = Seq.empty,
                      tags = Seq.empty,
                      ipIds = Seq.empty
                    )
                  )
          allIps = page.ips.getOrElse(Seq.empty)
          _     <- IO.println(s"${page.totalCount.getOrElse(0)} private address(es) booked in $region:")
          _     <- allIps.traverse_(ip => IO.println(describe(ip)))
          _     <- IO.println(s"  (${allIps.count(_.resource.isEmpty)} not attached to anything)")
        } yield ()

      /**
        * The same call, narrowed to the addresses Kubernetes nodes are holding.
        */
      val kubernetesAddresses =
        for {
          page <- scw.run(
                    ips.listIPs(
                      region,
                      projectId = Some(credentials.projectId),
                      attached = Some(true),
                      resourceTypes = Seq(ModelType.`k8s_node`, ModelType.`k8s_cluster`),
                      resourceIds = Seq.empty,
                      tags = Seq.empty,
                      ipIds = Seq.empty
                    )
                  )
          _ <-
            IO.println(s"${page.totalCount.getOrElse(0)} address(es) held by Kubernetes resources")
        } yield ()

      /**
        * Reserving an address up front. The source is what IPAM allocates from -- a Private Network
        * here, so the address comes out of that network's subnet. Passing `address` too would pin
        * one specific host address instead of letting IPAM choose a free one.
        */
      def bookOne(privateNetworkId: String) =
        for {
          booked <- scw.run(
                      ips.bookIP(
                        region,
                        BookIPRequest(
                          projectId = credentials.projectId,
                          source = BookIPRequestSource(privateNetworkId = Some(privateNetworkId)),
                          isIpv6 = Some(false),
                          tags = Some(Seq("example", "reserved-up-front"))
                        )
                      )
                    )
          ipId <- booked.id.orFail("ip.id")
          _    <- IO.println(s"booked ${booked.address.getOrElse("?")} ($ipId)")
          // Endpoints with an empty request body still take one, typed as raw JSON.
          _ <- scw.run(ips.releaseIP(region, ipId, Json.obj())).void
          _ <- IO.println("released it again")
        } yield ()

      val bookExample =
        Env[IO]
          .get("SCW_PRIVATE_NETWORK_ID")
          .map(_.filter(_.nonEmpty))
          .flatMap {
            case Some(id) => bookOne(id)
            case None     =>
              IO.println(
                "set SCW_PRIVATE_NETWORK_ID to also see an address get booked and released"
              )
          }

      inventory *> kubernetesAddresses *> bookExample
    }

}
