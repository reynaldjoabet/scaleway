package examples

import scala.concurrent.duration.*

import cats.effect.IO
import cats.effect.IOApp
import cats.effect.Resource
import cats.syntax.all.*
import scaleway.kubernetes.api.ClustersApi
import scaleway.kubernetes.api.NodesApi
import scaleway.kubernetes.api.PoolsApi
import scaleway.kubernetes.api.VersionsApi
import scaleway.kubernetes.models.Cluster
import scaleway.kubernetes.models.ClusterEnums
import scaleway.kubernetes.models.CreateClusterRequest
import scaleway.kubernetes.models.CreateClusterRequestEnums
import scaleway.kubernetes.models.CreatePoolRequest
import scaleway.kubernetes.models.CreatePoolRequestEnums
import scaleway.kubernetes.models.PoolConfig

/** Kapsule: a managed control plane plus the node pools that back it.
  *
  * A cluster is regional, its pools are zonal, and a cluster with no pool sits in `pool_required` rather than `ready`
  * -- so the pool is not optional garnish, it is part of provisioning. This example creates the first pool inline with
  * the cluster (`pools` in the create request) and then adds a second one through the Pools API, which is how you would
  * grow a cluster later.
  *
  * Provisioning takes a few minutes of real time and real money; the polling loop below is bounded accordingly.
  */
object KubernetesExample extends IOApp.Simple {

  def run: IO[Unit] = Scw.resource.use { scw =>
    val credentials = scw.credentials
    val clusters = ClustersApi.withApiKeyAuth(Scw.baseUrl, credentials.secretKey)
    val pools = PoolsApi.withApiKeyAuth(Scw.baseUrl, credentials.secretKey)
    val nodes = NodesApi.withApiKeyAuth(Scw.baseUrl, credentials.secretKey)
    val versions = VersionsApi.withApiKeyAuth(Scw.baseUrl, credentials.secretKey)

    val region = credentials.region

    /** Kapsule only accepts versions it currently offers, so pick from the live list rather than hardcoding one. */
    val latestVersion =
      for {
        response <- scw.run(versions.listVersions(region))
        available = response.versions.getOrElse(Seq.empty).flatMap(_.name)
        _ <- IO.println(s"Kubernetes versions offered in $region: ${available.mkString(", ")}")
        version <- available.lastOption.orFail("at least one available Kubernetes version")
      } yield version

    val listExisting =
      for {
        page <- scw.run(clusters.listClusters(region, projectId = Some(credentials.projectId), pageSize = Some(50)))
        _ <- IO.println(s"${page.totalCount.getOrElse(0)} cluster(s) in $region:")
        _ <- page.clusters.getOrElse(Seq.empty).traverse_ { cluster =>
          IO.println(
            s"  ${cluster.name.getOrElse("?")} v${cluster.version.getOrElse("?")} " +
              s"${cluster.status.fold("?")(_.toString)} " +
              s"cni=${cluster.cni.fold("?")(_.toString)}"
          )
        }
      } yield ()

    def cluster(version: String): Resource[IO, Cluster] =
      Resource.make(
        scw.run(
          clusters.createCluster(
            region,
            CreateClusterRequest(
              name = "example-cluster",
              version = version,
              // Cilium is the Scaleway default and the only CNI with network-policy support on Kapsule.
              cni = CreateClusterRequestEnums.Cni.`cilium`,
              projectId = Some(credentials.projectId),
              description = Some("Created by KubernetesExample -- safe to delete"),
              tags = Some(Seq("example", "scaleway-scala")),
              // Without at least one pool the cluster comes up in `pool_required`, not `ready`.
              pools = Some(
                Seq(
                  PoolConfig(
                    name = "default",
                    nodeType = "DEV1-M",
                    size = 1,
                    autohealing = Some(true),
                    zone = Some(credentials.zone)
                  )
                )
              )
            )
          )
        )
      )(created =>
        created.id
          .orFail("cluster.id")
          // `withAdditionalResources` also deletes the volumes and Load Balancers the cluster created for itself;
          // leaving it false is the usual way to end up paying for orphaned LBs.
          .flatMap(id => scw.run(clusters.deleteCluster(region, id, withAdditionalResources = true)).void)
      )

    def awaitReady(clusterId: String, attemptsLeft: Int): IO[Cluster] =
      scw.run(clusters.getCluster(region, clusterId)).flatMap { current =>
        current.status match {
          case Some(ClusterEnums.Status.`ready`) => IO.pure(current)
          case other if attemptsLeft > 0         =>
            IO.println(s"  status=${other.fold("?")(_.toString)}, waiting...") *>
              IO.sleep(15.seconds) *>
              awaitReady(clusterId, attemptsLeft - 1)
          case other =>
            IO.raiseError(new RuntimeException(s"cluster never became ready (last status: $other)"))
        }
      }

    def addAutoscalingPool(clusterId: String) =
      scw
        .run(
          pools.createPool(
            region,
            clusterId,
            CreatePoolRequest(
              name = "burst",
              nodeType = "DEV1-M",
              size = 0,
              autoscaling = Some(true),
              minSize = Some(0),
              maxSize = Some(3),
              autohealing = Some(true),
              containerRuntime = Some(CreatePoolRequestEnums.ContainerRuntime.`containerd`),
              zone = Some(credentials.zone),
              tags = Some(Seq("example"))
            )
          )
        )
        .flatMap(pool =>
          IO.println(
            s"added pool ${pool.name.getOrElse("?")} (${pool.minSize.getOrElse(0)}-${pool.maxSize.getOrElse(0)} nodes)"
          )
        )

    val provision =
      latestVersion.flatMap { version =>
        cluster(version).use { created =>
          for {
            clusterId <- created.id.orFail("cluster.id")
            _ <- IO.println(s"created cluster $clusterId, waiting for the control plane...")
            ready <- awaitReady(clusterId, attemptsLeft = 40)
            _ <- IO.println(s"cluster ready at ${ready.clusterUrl.getOrElse("?")}")
            _ <- addAutoscalingPool(clusterId)
            poolPage <- scw.run(pools.listPools(region, clusterId))
            _ <- IO.println(s"${poolPage.totalCount.getOrElse(0)} pool(s) on the cluster")
            nodePage <- scw.run(nodes.listNodes(region, clusterId))
            _ <- nodePage.nodes.getOrElse(Seq.empty).traverse_ { node =>
              IO.println(s"  node ${node.name.getOrElse("?")} ${node.status.fold("?")(_.toString)}")
            }
            _ <- IO.println("deleting the cluster...")
          } yield ()
        }
      }

    listExisting *> provision
  }

}
