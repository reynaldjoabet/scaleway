package examples

import cats.effect.IO
import cats.effect.IOApp
import cats.effect.Resource
import cats.syntax.all.*
import scaleway.vpc.api.PrivateNetworksApi
import scaleway.vpc.api.SubnetsApi
import scaleway.vpc.api.VPCsApi
import scaleway.vpc.models.CreatePrivateNetworkRequest
import scaleway.vpc.models.CreateVPCRequest
import scaleway.vpc.models.PrivateNetwork
import scaleway.vpc.models.VPC

/** VPC: carve a regional address space into Private Networks.
  *
  * A VPC owns a region-wide IPv4 range; each Private Network takes a non-overlapping slice of it and is what resources
  * (Instances, Load Balancers, Kubernetes nodes) actually attach to. Creating a Private Network with `subnets = None`
  * lets Scaleway pick a free /22 from the VPC, which is usually what you want -- this example pins the CIDRs instead so
  * the layout is explicit and reproducible.
  */
object VpcExample extends IOApp.Simple {

  def run: IO[Unit] = Scw.resource.use { scw =>
    val credentials = scw.credentials
    val vpcs = VPCsApi.withApiKeyAuth(Scw.baseUrl, credentials.secretKey)
    val privateNetworks = PrivateNetworksApi.withApiKeyAuth(Scw.baseUrl, credentials.secretKey)
    val subnets = SubnetsApi.withApiKeyAuth(Scw.baseUrl, credentials.secretKey)

    val region = credentials.region

    def vpc(name: String): Resource[IO, VPC] =
      Resource.make(
        scw.run(
          vpcs.createVPC(
            region,
            CreateVPCRequest(
              name = name,
              projectId = credentials.projectId,
              tags = Some(Seq("example", "scaleway-scala")),
              // Without routing, Private Networks in the same VPC cannot reach each other.
              enableRouting = Some(true)
            )
          )
        )
      )(created => created.id.orFail("vpc.id").flatMap(id => scw.run(vpcs.deleteVPC(region, id))))

    def privateNetwork(vpcId: String, name: String, cidr: String): Resource[IO, PrivateNetwork] =
      Resource.make(
        scw.run(
          privateNetworks.createPrivateNetwork(
            region,
            CreatePrivateNetworkRequest(
              name = name,
              projectId = credentials.projectId,
              vpcId = Some(vpcId),
              subnets = Some(Seq(cidr)),
              tags = Some(Seq("example"))
            )
          )
        )
      )(created =>
        created.id
          .orFail("privateNetwork.id")
          .flatMap(id => scw.run(privateNetworks.deletePrivateNetwork(region, id)))
      )

    val listExisting =
      for {
        page <- scw.run(
          vpcs.listVPCs(
            region,
            organizationId = Some(credentials.organizationId),
            pageSize = Some(50),
            tags = Seq.empty
          )
        )
        _ <- IO.println(s"${page.totalCount.getOrElse(0)} VPC(s) in $region:")
        _ <- page.vpcs.getOrElse(Seq.empty).traverse_ { existing =>
          val kind = if (existing.isDefault.getOrElse(false)) "default" else "custom"
          IO.println(
            s"  ${existing.name.getOrElse("?")} [$kind] " +
              s"${existing.privateNetworkCount.getOrElse(0)} Private Network(s), " +
              s"routing=${existing.routingEnabled.getOrElse(false)}"
          )
        }
      } yield ()

    // A three-tier layout inside 172.16.0.0/16 -- one Private Network per tier, all routable to each other.
    val tiers =
      List("example-public" -> "172.16.0.0/22", "example-app" -> "172.16.4.0/22", "example-data" -> "172.16.8.0/22")

    val provision =
      vpc("example-vpc").use { created =>
        for {
          vpcId <- created.id.orFail("vpc.id")
          _ <- IO.println(s"created VPC $vpcId in $region")
          _ <- tiers
            .traverse { case (name, cidr) => privateNetwork(vpcId, name, cidr) }
            .use { networks =>
              for {
                _ <- networks.traverse_ { network =>
                  IO.println(
                    s"  ${network.name.getOrElse("?")} -> " +
                      network.subnets.getOrElse(Seq.empty).flatMap(_.subnet).mkString(", ")
                  )
                }
                // Subnets are listed VPC-wide, which is the quickest way to see the whole address plan.
                page <- scw.run(subnets.listSubnets(region, vpcId = Some(vpcId), subnetIds = Seq.empty))
                _ <- IO.println(s"${page.totalCount.getOrElse(0)} subnet(s) allocated in the VPC")
              } yield ()
            }
        } yield ()
      }

    listExisting *> provision
  }

}
