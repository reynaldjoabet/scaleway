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

import scaleway.kubernetes.models.CreatePoolRequest
import scaleway.kubernetes.models.ListPoolsResponse
import scaleway.kubernetes.models.Pool
import scaleway.kubernetes.models.SetPoolLabelsRequest
import scaleway.kubernetes.models.SetPoolStartupTaintsRequest
import scaleway.kubernetes.models.SetPoolTaintsRequest
import scaleway.kubernetes.models.UpdatePoolRequest
import scaleway.kubernetes.models.UpgradePoolRequest
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

object PoolsApi:
  def apply(baseUrl: String = "https://api.scaleway.com"): PoolsApi[scaleway.kubernetes.Authorization.NoAuthorization.type] = PoolsApi(baseUrl, scaleway.kubernetes.Authorization.NoAuthorization)
  def withBasicAuth(baseUrl: String, username: String, password: String): PoolsApi[scaleway.kubernetes.Authorization.BasicAuth] =
    PoolsApi(baseUrl, scaleway.kubernetes.Authorization.BasicAuth(username, password))

  def withApiKeyAuth(baseUrl: String, apiKey: String): PoolsApi[scaleway.kubernetes.Authorization.ApiKey] =
    PoolsApi(baseUrl, scaleway.kubernetes.Authorization.ApiKey(apiKey))

  def withBearerTokenAuth(baseUrl: String, token: String): PoolsApi[scaleway.kubernetes.Authorization.BearerToken] =
    PoolsApi(baseUrl, scaleway.kubernetes.Authorization.BearerToken(token))

case class PoolsApi[Auth <: scaleway.kubernetes.Authorization] private (baseUrl: String, authConfig: scaleway.kubernetes.Authorization):
  def withBasicAuth(username: String, password: String): PoolsApi[scaleway.kubernetes.Authorization.BasicAuth] =
    copy(authConfig = scaleway.kubernetes.Authorization.BasicAuth(username, password))

  def withApiKeyAuth(apiKey: String): PoolsApi[scaleway.kubernetes.Authorization.ApiKey] =
    copy(authConfig = scaleway.kubernetes.Authorization.ApiKey(apiKey))

  def withNoAuth: PoolsApi[scaleway.kubernetes.Authorization.NoAuthorization.type] =
    copy(authConfig = scaleway.kubernetes.Authorization.NoAuthorization)

  def withBearerTokenAuth(token: String): PoolsApi[scaleway.kubernetes.Authorization.BearerToken] =
    copy(authConfig = scaleway.kubernetes.Authorization.BearerToken(token))

  /**
   * Create a new pool in a specific Kubernetes cluster.
   * 
   * Expected answers:
   *   code 200 : Pool ()
   * 
   * Available security schemes:
   *   scaleway (apiKey)
   * 
   * @param region The region you want to target
   * @param clusterId Cluster ID to which the pool will be attached.
   * @param createPoolRequest 
   */
  def createPool(region: String, clusterId: String, createPoolRequest: CreatePoolRequest)(using Auth <:< scaleway.kubernetes.Authorization.ApiKey): sttp.client4.Request[Either[ResponseException[String], Pool]] =
    val regionPathParam = PathSerializable.serialize("region", region, PathStyleFormat.SIMPLE, false)
    val clusterIdPathParam = PathSerializable.serialize("cluster_id", clusterId, PathStyleFormat.SIMPLE, false)
    val requestURL =
      uri"$baseUrl/k8s/v1/regions/${regionPathParam}/clusters/${clusterIdPathParam}/pools"

    basicRequest
      .method(Method.POST, requestURL)
      .contentType("application/json")
      .auth(authConfig, scaleway.kubernetes.ApiKeyLocation.HEADER, "X-Auth-Token")
      .body(asJson(createPoolRequest))
      .response(asJson[Pool])

  /**
   * Delete a specific pool from a cluster. Note that all the pool's nodes will also be deleted.
   * 
   * Expected answers:
   *   code 200 : Pool ()
   * 
   * Available security schemes:
   *   scaleway (apiKey)
   * 
   * @param region The region you want to target
   * @param poolId ID of the pool to delete.
   */
  def deletePool(region: String, poolId: String)(using Auth <:< scaleway.kubernetes.Authorization.ApiKey): sttp.client4.Request[Either[ResponseException[String], Pool]] =
    val regionPathParam = PathSerializable.serialize("region", region, PathStyleFormat.SIMPLE, false)
    val poolIdPathParam = PathSerializable.serialize("pool_id", poolId, PathStyleFormat.SIMPLE, false)
    val requestURL =
      uri"$baseUrl/k8s/v1/regions/${regionPathParam}/pools/${poolIdPathParam}"

    basicRequest
      .method(Method.DELETE, requestURL)
      .contentType("application/json")
      .auth(authConfig, scaleway.kubernetes.ApiKeyLocation.HEADER, "X-Auth-Token")
      .response(asJson[Pool])

  /**
   * Retrieve details about a specific pool in a Kubernetes cluster.
   * 
   * Expected answers:
   *   code 200 : Pool ()
   * 
   * Available security schemes:
   *   scaleway (apiKey)
   * 
   * @param region The region you want to target
   * @param poolId ID of the requested pool.
   */
  def getPool(region: String, poolId: String)(using Auth <:< scaleway.kubernetes.Authorization.ApiKey): sttp.client4.Request[Either[ResponseException[String], Pool]] =
    val regionPathParam = PathSerializable.serialize("region", region, PathStyleFormat.SIMPLE, false)
    val poolIdPathParam = PathSerializable.serialize("pool_id", poolId, PathStyleFormat.SIMPLE, false)
    val requestURL =
      uri"$baseUrl/k8s/v1/regions/${regionPathParam}/pools/${poolIdPathParam}"

    basicRequest
      .method(Method.GET, requestURL)
      .contentType("application/json")
      .auth(authConfig, scaleway.kubernetes.ApiKeyLocation.HEADER, "X-Auth-Token")
      .response(asJson[Pool])

  /**
   * List all the existing pools for a specific Kubernetes cluster.
   * 
   * Expected answers:
   *   code 200 : ListPoolsResponse ()
   * 
   * Available security schemes:
   *   scaleway (apiKey)
   * 
   * @param region The region you want to target
   * @param clusterId ID of the cluster whose pools will be listed.
   * @param orderBy Sort order of returned pools.
   * @param page Page number for the returned pools.
   * @param pageSize Maximum number of pools per page.
   * @param name Name to filter on, only pools containing this substring in their name will be returned.
   * @param status Status to filter on, only pools with this status will be returned.
   */
  def listPools(region: String, clusterId: String, orderBy: Option[String] = scala.None, page: Option[Int] = scala.None, pageSize: Option[Int] = scala.None, name: Option[String] = scala.None, status: Option[String] = scala.None)(using Auth <:< scaleway.kubernetes.Authorization.ApiKey): sttp.client4.Request[Either[ResponseException[String], ListPoolsResponse]] =
    val regionPathParam = PathSerializable.serialize("region", region, PathStyleFormat.SIMPLE, false)
    val clusterIdPathParam = PathSerializable.serialize("cluster_id", clusterId, PathStyleFormat.SIMPLE, false)
    val requestURL =
      uri"$baseUrl/k8s/v1/regions/${regionPathParam}/clusters/${clusterIdPathParam}/pools"
        .addParams(FormSerializable.serialize("order_by", orderBy, FormStyleFormat.FORM, true): _*)
        .addParams(FormSerializable.serialize("page", page, FormStyleFormat.FORM, true): _*)
        .addParams(FormSerializable.serialize("page_size", pageSize, FormStyleFormat.FORM, true): _*)
        .addParams(FormSerializable.serialize("name", name, FormStyleFormat.FORM, true): _*)
        .addParams(FormSerializable.serialize("status", status, FormStyleFormat.FORM, true): _*)

    basicRequest
      .method(Method.GET, requestURL)
      .contentType("application/json")
      .auth(authConfig, scaleway.kubernetes.ApiKeyLocation.HEADER, "X-Auth-Token")
      .response(asJson[ListPoolsResponse])

  /**
   * Apply a list of taints to all nodes of the pool (only apply to labels which was set through scaleway api).
   * 
   * Expected answers:
   *   code 200 : Pool ()
   * 
   * Available security schemes:
   *   scaleway (apiKey)
   * 
   * @param region The region you want to target
   * @param poolId 
   * @param setPoolLabelsRequest 
   */
  def setPoolLabels(region: String, poolId: String, setPoolLabelsRequest: SetPoolLabelsRequest)(using Auth <:< scaleway.kubernetes.Authorization.ApiKey): sttp.client4.Request[Either[ResponseException[String], Pool]] =
    val regionPathParam = PathSerializable.serialize("region", region, PathStyleFormat.SIMPLE, false)
    val poolIdPathParam = PathSerializable.serialize("pool_id", poolId, PathStyleFormat.SIMPLE, false)
    val requestURL =
      uri"$baseUrl/k8s/v1/regions/${regionPathParam}/pools/${poolIdPathParam}/set-labels"

    basicRequest
      .method(Method.PUT, requestURL)
      .contentType("application/json")
      .auth(authConfig, scaleway.kubernetes.ApiKeyLocation.HEADER, "X-Auth-Token")
      .body(asJson(setPoolLabelsRequest))
      .response(asJson[Pool])

  /**
   * Apply a list of taints to new nodes of the pool which would not be reconciled by scaleway.
   * 
   * Expected answers:
   *   code 200 : Pool ()
   * 
   * Available security schemes:
   *   scaleway (apiKey)
   * 
   * @param region The region you want to target
   * @param poolId ID of the pool to update.
   * @param setPoolStartupTaintsRequest 
   */
  def setPoolStartupTaints(region: String, poolId: String, setPoolStartupTaintsRequest: SetPoolStartupTaintsRequest)(using Auth <:< scaleway.kubernetes.Authorization.ApiKey): sttp.client4.Request[Either[ResponseException[String], Pool]] =
    val regionPathParam = PathSerializable.serialize("region", region, PathStyleFormat.SIMPLE, false)
    val poolIdPathParam = PathSerializable.serialize("pool_id", poolId, PathStyleFormat.SIMPLE, false)
    val requestURL =
      uri"$baseUrl/k8s/v1/regions/${regionPathParam}/pools/${poolIdPathParam}/set-startup-taints"

    basicRequest
      .method(Method.PUT, requestURL)
      .contentType("application/json")
      .auth(authConfig, scaleway.kubernetes.ApiKeyLocation.HEADER, "X-Auth-Token")
      .body(asJson(setPoolStartupTaintsRequest))
      .response(asJson[Pool])

  /**
   * Apply a list of taints to all nodes of the pool which will be periodically reconciled by scaleway.
   * 
   * Expected answers:
   *   code 200 : Pool ()
   * 
   * Available security schemes:
   *   scaleway (apiKey)
   * 
   * @param region The region you want to target
   * @param poolId ID of the pool to update.
   * @param setPoolTaintsRequest 
   */
  def setPoolTaints(region: String, poolId: String, setPoolTaintsRequest: SetPoolTaintsRequest)(using Auth <:< scaleway.kubernetes.Authorization.ApiKey): sttp.client4.Request[Either[ResponseException[String], Pool]] =
    val regionPathParam = PathSerializable.serialize("region", region, PathStyleFormat.SIMPLE, false)
    val poolIdPathParam = PathSerializable.serialize("pool_id", poolId, PathStyleFormat.SIMPLE, false)
    val requestURL =
      uri"$baseUrl/k8s/v1/regions/${regionPathParam}/pools/${poolIdPathParam}/set-taints"

    basicRequest
      .method(Method.PUT, requestURL)
      .contentType("application/json")
      .auth(authConfig, scaleway.kubernetes.ApiKeyLocation.HEADER, "X-Auth-Token")
      .body(asJson(setPoolTaintsRequest))
      .response(asJson[Pool])

  /**
   * Update the attributes of a specific pool, such as its desired size, autoscaling settings, and tags. To upgrade a pool, you will need to use the dedicated endpoint.
   * 
   * Expected answers:
   *   code 200 : Pool ()
   * 
   * Available security schemes:
   *   scaleway (apiKey)
   * 
   * @param region The region you want to target
   * @param poolId ID of the pool to update.
   * @param updatePoolRequest 
   */
  def updatePool(region: String, poolId: String, updatePoolRequest: UpdatePoolRequest)(using Auth <:< scaleway.kubernetes.Authorization.ApiKey): sttp.client4.Request[Either[ResponseException[String], Pool]] =
    val regionPathParam = PathSerializable.serialize("region", region, PathStyleFormat.SIMPLE, false)
    val poolIdPathParam = PathSerializable.serialize("pool_id", poolId, PathStyleFormat.SIMPLE, false)
    val requestURL =
      uri"$baseUrl/k8s/v1/regions/${regionPathParam}/pools/${poolIdPathParam}"

    basicRequest
      .method(Method.PATCH, requestURL)
      .contentType("application/json")
      .auth(authConfig, scaleway.kubernetes.ApiKeyLocation.HEADER, "X-Auth-Token")
      .body(asJson(updatePoolRequest))
      .response(asJson[Pool])

  /**
   * Upgrade the Kubernetes version of a specific pool. Note that it only works if the targeted version matches the cluster's version. This will drain and replace the nodes in that pool.
   * 
   * Expected answers:
   *   code 200 : Pool ()
   * 
   * Available security schemes:
   *   scaleway (apiKey)
   * 
   * @param region The region you want to target
   * @param poolId ID of the pool to upgrade.
   * @param upgradePoolRequest 
   */
  def upgradePool(region: String, poolId: String, upgradePoolRequest: UpgradePoolRequest)(using Auth <:< scaleway.kubernetes.Authorization.ApiKey): sttp.client4.Request[Either[ResponseException[String], Pool]] =
    val regionPathParam = PathSerializable.serialize("region", region, PathStyleFormat.SIMPLE, false)
    val poolIdPathParam = PathSerializable.serialize("pool_id", poolId, PathStyleFormat.SIMPLE, false)
    val requestURL =
      uri"$baseUrl/k8s/v1/regions/${regionPathParam}/pools/${poolIdPathParam}/upgrade"

    basicRequest
      .method(Method.POST, requestURL)
      .contentType("application/json")
      .auth(authConfig, scaleway.kubernetes.ApiKeyLocation.HEADER, "X-Auth-Token")
      .body(asJson(upgradePoolRequest))
      .response(asJson[Pool])

end PoolsApi