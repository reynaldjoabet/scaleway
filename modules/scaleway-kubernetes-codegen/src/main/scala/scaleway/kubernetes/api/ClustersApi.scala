/**
 * Kubernetes API
 * Kubernetes is an open-source platform that enables developers to manage their containerized applications. Scaleway Kubernetes Kapsule and Kosmos are powerful tools to help you manage your containerized workloads and services.  They both provide a managed environment for creating, configuring, and running clusters of pre-configured machines.  The primary difference between Kapsule and Kosmos is that Kapsule clusters are composed solely of Scaleway Instances. In contrast, Kosmos is a managed Multi-Cloud Kubernetes Engine that allows you to connect Instances and virtual or dedicated servers from any cloud provider to a single managed Control-Plane.  ## Concepts  Refer to our [dedicated concepts page](https://www.scaleway.com/en/docs/kubernetes/concepts/) to find definitions of all Kubetnetes-related terminology.  ## Quickstart     1. Configure your environment variables. Note: This is an optional step that seeks to simplify your usage of the Kapsule and Kosmos API.     ```bash     export SCW_ACCESS_KEY=\"<API access key>\"     export SCW_SECRET_KEY=\"<API secret key>\"     export SCW_REGION=\"<Scaleway service region>\"     export SCW_PROJECT_ID=\"<Scaleway Project ID>\"     export SCW_PRIVATE_NETWORK_ID=\"<Scaleway Private Network ID>\"     ```  2. Edit the POST request payload you will use to create your Kubernetes cluster. Replace the parameters in the following example:     ```json     {       \"project_id\": \"$SCW_PROJECT_ID\",       \"private_network_id\": \"$SCW_PRIVATE_NETWORK_ID\",       \"type\": \"string\",       \"name\": \"string\",       \"description\": \"string\",       \"tags\": [         \"string\"       ],       \"version\": \"string\",       \"cni\": \"unknown_cni\",       \"pools\": [         {           \"name\": \"string\",           \"node_type\": \"string\",           \"size\": \"integer\",           \"tags\": [             \"string\"           ],           \"zone\": \"string\",           \"root_volume_type\": \"default_volume_type\",           \"root_volume_size\": \"integer\"         }       ]     }     ```      | Parameter                | Description                                                        |     | :----------------------- | :----------------------------------------------------------------- |     | `project_id`             | The ID of the Project you want to create your Kubernetes cluster in. To find your Project ID you can consult the [Scaleway console](https://console.scaleway.com/project/settings). |     | `type`                   | The type of the cluster (possible values are `kapsule`, `multicloud`, `kapsule-dedicated-X`, `multicloud-dedicated-X` - `X` can be `4`, `8` or `16`). |     | `name`                   | **REQUIRED** Name of the cluster. |     | `description`            | Description of the cluster. |     | `tags`                   | Tags associated with the cluster. |     | `version`                | **REQUIRED** Kubernetes version of the cluster. |     | `cni`                    | **REQUIRED** Container Network Interface (CNI) plugin that will run on the cluster. The default value is `unknown_cni`. (possible values are `cilium`, `cilium_native`, `calico` for `kapsule` and `kilo` for `multicloud`) |     | `pools`                  | Pools to be created along with the cluster. |     | `pools.name`             | **REQUIRED** Name of the pool. |     | `pools.node-type`        | **REQUIRED** The node type of the Scaleway Instance wanted for the pool. |     | `pools.size`             | **REQUIRED** The number of nodes in the pool. |     | `pools.tags`             | Tags associated with the pool. See [managing tags](https://www.scaleway.com/en/docs/kubernetes/api-cli/managing-tags). |     | `pools.zone`             | Availability zone in which the pools will be deployed in. |     | `pools.root_volume_type` | The root volume type. The default value is `default_volume_type`. |     | `pools.root_volume_size` | The system volume disk size in bytes. |     | `private_network_id`     | Private network ID for internal cluster communication. |  3. Create a Kapsule cluster and node pool by running the following command. Make sure you include the payload you edited in the previous step.     ```bash     curl -X POST \\       -H \"X-Auth-Token: $SCW_SECRET_KEY\" \\       \"Content-Type: application/json\" \\       \"https://api.scaleway.com/k8s/v1/regions/$SCW_REGION/clusters\" \\       -d \"{             \\\"project_id\\\": \\\"$SCW_PROJECT_ID\\\",             \\\"private_network_id\\\": \\\"$SCW_PRIVATE_NETWORK_ID\\\",             \\\"type\\\": \\\"kapsule\\\",             \\\"name\\\": \\\"MyFirstKapsuleCluster\\\",             \\\"description\\\": \\\"My first Kapsule Cluster\\\",             \\\"tags\\\": [               \\\"kapsule\\\",               \\\"kubernetes\\\"             ],             \\\"version\\\": \\\"1.31.2\\\",             \\\"cni\\\": \\\"unknown_cni\\\",             \\\"pools\\\": [               {                 \\\"name\\\": \\\"MyFirstKapsulePool\\\",                 \\\"node_type\\\": \\\"PLAY2-MICRO\\\",                 \\\"size\\\": \\\"2\\\",                 \\\"tags\\\": [                   \\\"pool\\\"                 ],                 \\\"zone\\\": \\\"fr-par-1\\\",                 \\\"root_volume_type\\\": \\\"default_volume_type\\\",                 \\\"root_volume_size\\\": \\\"20000000000\\\"               }             ]           }\"     ```  4. List your Kapsule clusters.     ```bash     curl -X GET \\       -H \"Content-Type: application/json\" \\       -H \"X-Auth-Token: $SCW_SECRET_KEY\" \\       https://api.scaleway.com/k8s/v1/regions/$SCW_REGION/clusters     ```      You will see detailed information about your clusters.  5. Download the kubeconfig file for your cluster.     ```bash     curl -X GET \\       -H \"Content-Type: application/json\" \\       -H \"X-Auth-Token: $SCW_SECRET_KEY\" \\       https://api.scaleway.com/k8s/v1/regions/$SCW_REGION/clusters/$SCW_CLUSTER_ID/kubeconfig     ```     <Message type=\"tip\">       Add `?dl=1` at the end of the URL to directly get the `base64`-decoded kubeconfig. If not, the kubeconfig will be `base64`-encoded.     </Message>  6. [Connect to your cluster](https://www.scaleway.com/en/docs/kubernetes/how-to/connect-cluster-kubectl) using kubectl.  7. Delete your cluster.     ```bash     curl -X GET -H \"Content-Type: application/json\" \\       -H \"X-Auth-Token: $SCW_SECRET_KEY\" https://api.scaleway.com/k8s/v1/regions/$SCW_REGION/clusters/$SCW_CLUSTER_ID     ```   <Message type=\"requirement\"> To perform the following steps, you must first ensure that:   - you have an account and are logged into the [Scaleway console](https://console.scaleway.com/organization)   - you have created an [API key](https://www.scaleway.com/en/docs/iam/how-to/create-api-keys/) and that the API key has sufficient [IAM permissions](https://www.scaleway.com/en/docs/iam/reference-content/permission-sets/) to perform the actions described on this page.   - you have [installed `curl`](https://curl.se/download.html) </Message>   ## Technical information  Kubernetes Kapsule provides features such as: - Persistent Volume Claims (PVC) are available through Scaleway Block Volumes. - Pool autoscaling and autohealing is available. - Kubernetes auto upgrades features is available.  ### Regions  Scaleway's infrastructure is spread across different [regions and Availability Zones](https://www.scaleway.com/en/docs/account/reference-content/products-availability/).  Kubernetes Kapsule and Kosmos are available in the Paris, Amsterdam and Warsaw regions, which are represented by the following path parameters:  - `fr-par` - `nl-ams` - `pl-waw`  ### Versions  Kubernetes Kosmos and Kapsule supports at least the latest version of the last 3 major Kubernetes releases.  ## Technical limitations  The following limitations should be taken into account when using the Kubernetes API:  - The maximum number of pods per node is 110, as per the official k8s project recommendation (configurable). - The maximum number of volumes (PVC) per node is 15. - The maximum number of nodes per cluster varies from 150 to up to 500 (depending on the control plane tier). - [VPC Limitations](https://www.scaleway.com/en/docs/vpc/troubleshooting/vpc-limitations/) may restrict the number of nodes and loadbalancers. - Private network must not conflict with the 198.18.0.0/15 subnet. - Users cannot expose port 53 (DNS) to `hostPort` or in `hostNetwork` mode. - Security groups set to drop all inbound must have stateful group enabled. - Security groups must have ports `80` and `443` left open in outbound. - For Kosmos clusters **or** if ACLs are not activated (no network tab in console), ports `8132` and `6443` must also be opened. - Kilo CNI (kosmos nodes) also needs UDP port `51820` to be open.  The following limitations should be acknowledged, while Scaleway is actively working on planned solutions to address them:  - Dual Stack IPv4/IPv6 is not (yet) available. - Read Write Many / Read Only Many are not (yet) available. - Kubernetes control plane network access is managed by a Load Balancer located on `ZONE-1`. In the event of a global failure for this AZ, the Control Plane will be unreachable.  The following limitations should be taken into account when setting custom settings for Pod & Service CIDRs and DNS IP:  - Setting these may reduce the number of nodes and pods per nodes the cluster can handle. - The Pod and Service CIDRs must not conflict between them. - The Pod & Service CIDRs must not conflict with the Private Networks' subnets of the VPC attached to the cluster. - The Pod & Service CIDRs must not conflict with the routes set in the VPC attached to the cluster. - The Pod & Service CIDRs subnets must be part of the RFC 1918 and RFC 6598 (subnet included in: 10.0.0.0/8 or 172.16.0.0/12 or 192.168.0.0/16 or 100.64.0.0/10). - Mask /16 minimum for the Pod CIDR (not above 16) - Mask /24 minimum for the Service CIDR (not above 24) - The Service DNS IP must be included in the Service CIDR  ## Going further  For more help using Kubernetes Kapsule and Kosmos, check out the following resources: - Our [main documentation](https://www.scaleway.com/en/docs/kubernetes/) - The `#k8s` channel on our [Slack Community](https://www.scaleway.com/en/docs/tutorials/scaleway-slack-community/) - Our [support ticketing system](https://www.scaleway.com/en/docs/account/how-to/open-a-support-ticket/).
 *
 * The version of the OpenAPI document: v1
 * 
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */
package scaleway.kubernetes.api

import scaleway.kubernetes.models.Cluster
import scaleway.kubernetes.models.CreateClusterRequest
import java.io.File
import scaleway.kubernetes.models.ListClusterAvailableTypesResponse
import scaleway.kubernetes.models.ListClusterAvailableVersionsResponse
import scaleway.kubernetes.models.ListClustersResponse
import scaleway.kubernetes.models.SetClusterTypeRequest
import scaleway.kubernetes.models.UpdateClusterRequest
import scaleway.kubernetes.models.UpgradeClusterRequest
import com.github.plokhotnyuk.jsoniter_scala.circe.JsoniterScalaCodec.*
import scaleway.kubernetes.JsonSupport.{*, given}
import scaleway.kubernetes.FormSerializable
import scaleway.kubernetes.FormStyleFormat
import scaleway.kubernetes.HeaderSerializable
import scaleway.kubernetes.ApiKeyLocation
import scaleway.kubernetes.PathStyleFormat
import scaleway.kubernetes.PathSerializable
import scaleway.kubernetes.CookieSerializable
import scaleway.kubernetes.Helpers.*
import sttp.client4.jsoniter.*
import sttp.client4.*
import sttp.model.Method

object ClustersApi:
  def apply(baseUrl: String = "https://api.scaleway.com"): ClustersApi[scaleway.kubernetes.Authorization.NoAuthorization.type] = ClustersApi(baseUrl, scaleway.kubernetes.Authorization.NoAuthorization)
  def withBasicAuth(baseUrl: String, username: String, password: String): ClustersApi[scaleway.kubernetes.Authorization.BasicAuth] =
    ClustersApi(baseUrl, scaleway.kubernetes.Authorization.BasicAuth(username, password))

  def withApiKeyAuth(baseUrl: String, apiKey: String): ClustersApi[scaleway.kubernetes.Authorization.ApiKey] =
    ClustersApi(baseUrl, scaleway.kubernetes.Authorization.ApiKey(apiKey))

  def withBearerTokenAuth(baseUrl: String, token: String): ClustersApi[scaleway.kubernetes.Authorization.BearerToken] =
    ClustersApi(baseUrl, scaleway.kubernetes.Authorization.BearerToken(token))

case class ClustersApi[Auth <: scaleway.kubernetes.Authorization] private (baseUrl: String, authConfig: scaleway.kubernetes.Authorization):
  def withBasicAuth(username: String, password: String): ClustersApi[scaleway.kubernetes.Authorization.BasicAuth] =
    copy(authConfig = scaleway.kubernetes.Authorization.BasicAuth(username, password))

  def withApiKeyAuth(apiKey: String): ClustersApi[scaleway.kubernetes.Authorization.ApiKey] =
    copy(authConfig = scaleway.kubernetes.Authorization.ApiKey(apiKey))

  def withNoAuth: ClustersApi[scaleway.kubernetes.Authorization.NoAuthorization.type] =
    copy(authConfig = scaleway.kubernetes.Authorization.NoAuthorization)

  def withBearerTokenAuth(token: String): ClustersApi[scaleway.kubernetes.Authorization.BearerToken] =
    copy(authConfig = scaleway.kubernetes.Authorization.BearerToken(token))

  /**
   * Create a new Kubernetes cluster in a Scaleway region.
   * 
   * Expected answers:
   *   code 200 : Cluster ()
   * 
   * Available security schemes:
   *   scaleway (apiKey)
   * 
   * @param region The region you want to target
   * @param createClusterRequest 
   */
  def createCluster(region: String, createClusterRequest: CreateClusterRequest)(using Auth <:< scaleway.kubernetes.Authorization.ApiKey): sttp.client4.Request[Either[ResponseException[String], Cluster]] =
    val regionPathParam = PathSerializable.serialize("region", region, PathStyleFormat.SIMPLE, false)
    val requestURL =
      uri"$baseUrl/k8s/v1/regions/${regionPathParam}/clusters"

    basicRequest
      .method(Method.POST, requestURL)
      .contentType("application/json")
      .auth(authConfig, scaleway.kubernetes.ApiKeyLocation.HEADER, "X-Auth-Token")
      .body(asJson(createClusterRequest))
      .response(asJson[Cluster])

  /**
   * Delete a specific Kubernetes cluster and all its associated pools and nodes, and possibly its associated Load Balancers or Block Volumes.
   * 
   * Expected answers:
   *   code 200 : Cluster ()
   * 
   * Available security schemes:
   *   scaleway (apiKey)
   * 
   * @param region The region you want to target
   * @param clusterId ID of the cluster to delete.
   * @param withAdditionalResources Defines whether all volumes (including retain volume type), empty Private Networks and Load Balancers with a name starting with the cluster ID will also be deleted.
   */
  def deleteCluster(region: String, clusterId: String, withAdditionalResources: Boolean)(using Auth <:< scaleway.kubernetes.Authorization.ApiKey): sttp.client4.Request[Either[ResponseException[String], Cluster]] =
    val regionPathParam = PathSerializable.serialize("region", region, PathStyleFormat.SIMPLE, false)
    val clusterIdPathParam = PathSerializable.serialize("cluster_id", clusterId, PathStyleFormat.SIMPLE, false)
    val requestURL =
      uri"$baseUrl/k8s/v1/regions/${regionPathParam}/clusters/${clusterIdPathParam}"
        .addParams(FormSerializable.serialize("with_additional_resources", withAdditionalResources, FormStyleFormat.FORM, true): _*)

    basicRequest
      .method(Method.DELETE, requestURL)
      .contentType("application/json")
      .auth(authConfig, scaleway.kubernetes.ApiKeyLocation.HEADER, "X-Auth-Token")
      .response(asJson[Cluster])

  /**
   * Retrieve information about a specific Kubernetes cluster.
   * 
   * Expected answers:
   *   code 200 : Cluster ()
   * 
   * Available security schemes:
   *   scaleway (apiKey)
   * 
   * @param region The region you want to target
   * @param clusterId ID of the requested cluster.
   */
  def getCluster(region: String, clusterId: String)(using Auth <:< scaleway.kubernetes.Authorization.ApiKey): sttp.client4.Request[Either[ResponseException[String], Cluster]] =
    val regionPathParam = PathSerializable.serialize("region", region, PathStyleFormat.SIMPLE, false)
    val clusterIdPathParam = PathSerializable.serialize("cluster_id", clusterId, PathStyleFormat.SIMPLE, false)
    val requestURL =
      uri"$baseUrl/k8s/v1/regions/${regionPathParam}/clusters/${clusterIdPathParam}"

    basicRequest
      .method(Method.GET, requestURL)
      .contentType("application/json")
      .auth(authConfig, scaleway.kubernetes.ApiKeyLocation.HEADER, "X-Auth-Token")
      .response(asJson[Cluster])

  /**
   * Download the Kubernetes cluster config file (also known as `kubeconfig`) for a specific cluster to use it with `kubectl`. Tip: add `?dl=1` at the end of the URL to directly retrieve the base64 decoded kubeconfig. If you choose not to, the kubeconfig will be base64 encoded.
   * 
   * Expected answers:
   *   code 200 : File ()
   * 
   * Available security schemes:
   *   scaleway (apiKey)
   * 
   * @param region The region you want to target
   * @param clusterId Cluster ID for which to download the kubeconfig.
   * @param redacted Hide the legacy token from the kubeconfig.
   */
  def getClusterKubeConfig(region: String, clusterId: String, redacted: Option[Boolean] = scala.None)(using Auth <:< scaleway.kubernetes.Authorization.ApiKey): sttp.client4.Request[Either[ResponseException[String], File]] =
    val regionPathParam = PathSerializable.serialize("region", region, PathStyleFormat.SIMPLE, false)
    val clusterIdPathParam = PathSerializable.serialize("cluster_id", clusterId, PathStyleFormat.SIMPLE, false)
    val requestURL =
      uri"$baseUrl/k8s/v1/regions/${regionPathParam}/clusters/${clusterIdPathParam}/kubeconfig"
        .addParams(FormSerializable.serialize("redacted", redacted, FormStyleFormat.FORM, true): _*)

    basicRequest
      .method(Method.GET, requestURL)
      .contentType("application/json")
      .auth(authConfig, scaleway.kubernetes.ApiKeyLocation.HEADER, "X-Auth-Token")
      .response(asFile(File.createTempFile("download", ".tmp")).mapWithMetadata((result, metadata) => result.left.map(errStr => ResponseException.DeserializationException(errStr, new Exception(errStr), metadata))))

  /**
   * List the cluster types that a specific Kubernetes cluster is allowed to switch to.
   * 
   * Expected answers:
   *   code 200 : ListClusterAvailableTypesResponse ()
   * 
   * Available security schemes:
   *   scaleway (apiKey)
   * 
   * @param region The region you want to target
   * @param clusterId Cluster ID for which the available Kubernetes types will be listed.
   */
  def listClusterAvailableTypes(region: String, clusterId: String)(using Auth <:< scaleway.kubernetes.Authorization.ApiKey): sttp.client4.Request[Either[ResponseException[String], ListClusterAvailableTypesResponse]] =
    val regionPathParam = PathSerializable.serialize("region", region, PathStyleFormat.SIMPLE, false)
    val clusterIdPathParam = PathSerializable.serialize("cluster_id", clusterId, PathStyleFormat.SIMPLE, false)
    val requestURL =
      uri"$baseUrl/k8s/v1/regions/${regionPathParam}/clusters/${clusterIdPathParam}/available-types"

    basicRequest
      .method(Method.GET, requestURL)
      .contentType("application/json")
      .auth(authConfig, scaleway.kubernetes.ApiKeyLocation.HEADER, "X-Auth-Token")
      .response(asJson[ListClusterAvailableTypesResponse])

  /**
   * List the versions that a specific Kubernetes cluster is allowed to upgrade to. Results will include every patch version greater than the current patch, as well as one minor version ahead of the current version. Any upgrade skipping a minor version will not work.
   * 
   * Expected answers:
   *   code 200 : ListClusterAvailableVersionsResponse ()
   * 
   * Available security schemes:
   *   scaleway (apiKey)
   * 
   * @param region The region you want to target
   * @param clusterId Cluster ID for which the available Kubernetes versions will be listed.
   */
  def listClusterAvailableVersions(region: String, clusterId: String)(using Auth <:< scaleway.kubernetes.Authorization.ApiKey): sttp.client4.Request[Either[ResponseException[String], ListClusterAvailableVersionsResponse]] =
    val regionPathParam = PathSerializable.serialize("region", region, PathStyleFormat.SIMPLE, false)
    val clusterIdPathParam = PathSerializable.serialize("cluster_id", clusterId, PathStyleFormat.SIMPLE, false)
    val requestURL =
      uri"$baseUrl/k8s/v1/regions/${regionPathParam}/clusters/${clusterIdPathParam}/available-versions"

    basicRequest
      .method(Method.GET, requestURL)
      .contentType("application/json")
      .auth(authConfig, scaleway.kubernetes.ApiKeyLocation.HEADER, "X-Auth-Token")
      .response(asJson[ListClusterAvailableVersionsResponse])

  /**
   * List all existing Kubernetes clusters in a specific region.
   * 
   * Expected answers:
   *   code 200 : ListClustersResponse ()
   * 
   * Available security schemes:
   *   scaleway (apiKey)
   * 
   * @param region The region you want to target
   * @param organizationId Organization ID on which to filter the returned clusters.
   * @param projectId Project ID on which to filter the returned clusters.
   * @param orderBy Sort order of returned clusters.
   * @param page Page number to return for clusters, from the paginated results.
   * @param pageSize Maximum number of clusters per page.
   * @param name Name to filter on, only clusters containing this substring in their name will be returned.
   * @param status Status to filter on, only clusters with this status will be returned.
   * @param `type` Type to filter on, only clusters with this type will be returned.
   * @param privateNetworkId Private Network ID to filter on, only clusters within this Private Network will be returned.
   * @param version Version to filter on, only cluster matching this prefix version will be returned.
   */
  def listClusters(region: String, organizationId: Option[String] = scala.None, projectId: Option[String] = scala.None, orderBy: Option[String] = scala.None, page: Option[Int] = scala.None, pageSize: Option[Int] = scala.None, name: Option[String] = scala.None, status: Option[String] = scala.None, `type`: Option[String] = scala.None, privateNetworkId: Option[String] = scala.None, version: Option[String] = scala.None)(using Auth <:< scaleway.kubernetes.Authorization.ApiKey): sttp.client4.Request[Either[ResponseException[String], ListClustersResponse]] =
    val regionPathParam = PathSerializable.serialize("region", region, PathStyleFormat.SIMPLE, false)
    val requestURL =
      uri"$baseUrl/k8s/v1/regions/${regionPathParam}/clusters"
        .addParams(FormSerializable.serialize("organization_id", organizationId, FormStyleFormat.FORM, true): _*)
        .addParams(FormSerializable.serialize("project_id", projectId, FormStyleFormat.FORM, true): _*)
        .addParams(FormSerializable.serialize("order_by", orderBy, FormStyleFormat.FORM, true): _*)
        .addParams(FormSerializable.serialize("page", page, FormStyleFormat.FORM, true): _*)
        .addParams(FormSerializable.serialize("page_size", pageSize, FormStyleFormat.FORM, true): _*)
        .addParams(FormSerializable.serialize("name", name, FormStyleFormat.FORM, true): _*)
        .addParams(FormSerializable.serialize("status", status, FormStyleFormat.FORM, true): _*)
        .addParams(FormSerializable.serialize("type", `type`, FormStyleFormat.FORM, true): _*)
        .addParams(FormSerializable.serialize("private_network_id", privateNetworkId, FormStyleFormat.FORM, true): _*)
        .addParams(FormSerializable.serialize("version", version, FormStyleFormat.FORM, true): _*)

    basicRequest
      .method(Method.GET, requestURL)
      .contentType("application/json")
      .auth(authConfig, scaleway.kubernetes.ApiKeyLocation.HEADER, "X-Auth-Token")
      .response(asJson[ListClustersResponse])

  /**
   * Reset the admin token for a specific Kubernetes cluster. This will revoke the old admin token (which will not be usable afterwards) and create a new one. Note that you will need to download the kubeconfig again to keep interacting with the cluster.
   * 
   * Expected answers:
   *   code 204 :  ()
   * 
   * Available security schemes:
   *   scaleway (apiKey)
   * 
   * @param region The region you want to target
   * @param clusterId Cluster ID on which the admin token will be renewed.
   * @param body 
   */
  def resetClusterAdminToken(region: String, clusterId: String, body: io.circe.Json)(using Auth <:< scaleway.kubernetes.Authorization.ApiKey): sttp.client4.Request[Either[ResponseException[String], Unit]] =
    val regionPathParam = PathSerializable.serialize("region", region, PathStyleFormat.SIMPLE, false)
    val clusterIdPathParam = PathSerializable.serialize("cluster_id", clusterId, PathStyleFormat.SIMPLE, false)
    val requestURL =
      uri"$baseUrl/k8s/v1/regions/${regionPathParam}/clusters/${clusterIdPathParam}/reset-admin-token"

    basicRequest
      .method(Method.POST, requestURL)
      .contentType("application/json")
      .auth(authConfig, scaleway.kubernetes.ApiKeyLocation.HEADER, "X-Auth-Token")
      .body(asJson(body))
      .response(asString.mapWithMetadata(ResponseAs.deserializeRightWithError(_ => Right(()))))

  /**
   * Change the type of a specific Kubernetes cluster. To see the possible values you can enter for the `type` field, [list available cluster types](#list-available-cluster-types-for-a-cluster).
   * 
   * Expected answers:
   *   code 200 : Cluster ()
   * 
   * Available security schemes:
   *   scaleway (apiKey)
   * 
   * @param region The region you want to target
   * @param clusterId ID of the cluster to migrate from one type to another.
   * @param setClusterTypeRequest 
   */
  def setClusterType(region: String, clusterId: String, setClusterTypeRequest: SetClusterTypeRequest)(using Auth <:< scaleway.kubernetes.Authorization.ApiKey): sttp.client4.Request[Either[ResponseException[String], Cluster]] =
    val regionPathParam = PathSerializable.serialize("region", region, PathStyleFormat.SIMPLE, false)
    val clusterIdPathParam = PathSerializable.serialize("cluster_id", clusterId, PathStyleFormat.SIMPLE, false)
    val requestURL =
      uri"$baseUrl/k8s/v1/regions/${regionPathParam}/clusters/${clusterIdPathParam}/set-type"

    basicRequest
      .method(Method.POST, requestURL)
      .contentType("application/json")
      .auth(authConfig, scaleway.kubernetes.ApiKeyLocation.HEADER, "X-Auth-Token")
      .body(asJson(setClusterTypeRequest))
      .response(asJson[Cluster])

  /**
   * Update information on a specific Kubernetes cluster. You can update details such as its name, description, tags and configuration. To upgrade a cluster, you will need to use the dedicated endpoint.
   * 
   * Expected answers:
   *   code 200 : Cluster ()
   * 
   * Available security schemes:
   *   scaleway (apiKey)
   * 
   * @param region The region you want to target
   * @param clusterId ID of the cluster to update.
   * @param updateClusterRequest 
   */
  def updateCluster(region: String, clusterId: String, updateClusterRequest: UpdateClusterRequest)(using Auth <:< scaleway.kubernetes.Authorization.ApiKey): sttp.client4.Request[Either[ResponseException[String], Cluster]] =
    val regionPathParam = PathSerializable.serialize("region", region, PathStyleFormat.SIMPLE, false)
    val clusterIdPathParam = PathSerializable.serialize("cluster_id", clusterId, PathStyleFormat.SIMPLE, false)
    val requestURL =
      uri"$baseUrl/k8s/v1/regions/${regionPathParam}/clusters/${clusterIdPathParam}"

    basicRequest
      .method(Method.PATCH, requestURL)
      .contentType("application/json")
      .auth(authConfig, scaleway.kubernetes.ApiKeyLocation.HEADER, "X-Auth-Token")
      .body(asJson(updateClusterRequest))
      .response(asJson[Cluster])

  /**
   * Upgrade a specific Kubernetes cluster and possibly its associated pools to a specific and supported Kubernetes version.
   * 
   * Expected answers:
   *   code 200 : Cluster ()
   * 
   * Available security schemes:
   *   scaleway (apiKey)
   * 
   * @param region The region you want to target
   * @param clusterId ID of the cluster to upgrade.
   * @param upgradeClusterRequest 
   */
  def upgradeCluster(region: String, clusterId: String, upgradeClusterRequest: UpgradeClusterRequest)(using Auth <:< scaleway.kubernetes.Authorization.ApiKey): sttp.client4.Request[Either[ResponseException[String], Cluster]] =
    val regionPathParam = PathSerializable.serialize("region", region, PathStyleFormat.SIMPLE, false)
    val clusterIdPathParam = PathSerializable.serialize("cluster_id", clusterId, PathStyleFormat.SIMPLE, false)
    val requestURL =
      uri"$baseUrl/k8s/v1/regions/${regionPathParam}/clusters/${clusterIdPathParam}/upgrade"

    basicRequest
      .method(Method.POST, requestURL)
      .contentType("application/json")
      .auth(authConfig, scaleway.kubernetes.ApiKeyLocation.HEADER, "X-Auth-Token")
      .body(asJson(upgradeClusterRequest))
      .response(asJson[Cluster])

end ClustersApi