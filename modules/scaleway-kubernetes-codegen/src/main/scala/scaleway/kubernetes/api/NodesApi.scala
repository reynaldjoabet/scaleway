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

import scaleway.kubernetes.models.ListNodesResponse
import scaleway.kubernetes.models.Node
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

object NodesApi:
  def apply(baseUrl: String = "https://api.scaleway.com"): NodesApi[scaleway.kubernetes.Authorization.NoAuthorization.type] = NodesApi(baseUrl, scaleway.kubernetes.Authorization.NoAuthorization)
  def withBasicAuth(baseUrl: String, username: String, password: String): NodesApi[scaleway.kubernetes.Authorization.BasicAuth] =
    NodesApi(baseUrl, scaleway.kubernetes.Authorization.BasicAuth(username, password))

  def withApiKeyAuth(baseUrl: String, apiKey: String): NodesApi[scaleway.kubernetes.Authorization.ApiKey] =
    NodesApi(baseUrl, scaleway.kubernetes.Authorization.ApiKey(apiKey))

  def withBearerTokenAuth(baseUrl: String, token: String): NodesApi[scaleway.kubernetes.Authorization.BearerToken] =
    NodesApi(baseUrl, scaleway.kubernetes.Authorization.BearerToken(token))

case class NodesApi[Auth <: scaleway.kubernetes.Authorization] private (baseUrl: String, authConfig: scaleway.kubernetes.Authorization):
  def withBasicAuth(username: String, password: String): NodesApi[scaleway.kubernetes.Authorization.BasicAuth] =
    copy(authConfig = scaleway.kubernetes.Authorization.BasicAuth(username, password))

  def withApiKeyAuth(apiKey: String): NodesApi[scaleway.kubernetes.Authorization.ApiKey] =
    copy(authConfig = scaleway.kubernetes.Authorization.ApiKey(apiKey))

  def withNoAuth: NodesApi[scaleway.kubernetes.Authorization.NoAuthorization.type] =
    copy(authConfig = scaleway.kubernetes.Authorization.NoAuthorization)

  def withBearerTokenAuth(token: String): NodesApi[scaleway.kubernetes.Authorization.BearerToken] =
    copy(authConfig = scaleway.kubernetes.Authorization.BearerToken(token))

  /**
   * Delete a specific Node. The node will first be drained and pods will be rescheduled onto another node. Note that when there is not enough space to reschedule all the pods (such as in a one-node cluster, or with specific constraints), disruption of your applications may occur.
   * 
   * Expected answers:
   *   code 200 : Node ()
   * 
   * Available security schemes:
   *   scaleway (apiKey)
   * 
   * @param region The region you want to target
   * @param nodeId ID of the node to replace.
   * @param skipDrain Skip draining node from its workload (Note: this parameter is currently inactive).
   */
  def deleteNode(region: String, nodeId: String, skipDrain: Boolean)(using Auth <:< scaleway.kubernetes.Authorization.ApiKey): sttp.client4.Request[Either[ResponseException[String], Node]] =
    val regionPathParam = PathSerializable.serialize("region", region, PathStyleFormat.SIMPLE, false)
    val nodeIdPathParam = PathSerializable.serialize("node_id", nodeId, PathStyleFormat.SIMPLE, false)
    val requestURL =
      uri"$baseUrl/k8s/v1/regions/${regionPathParam}/nodes/${nodeIdPathParam}"
        .addParams(FormSerializable.serialize("skip_drain", skipDrain, FormStyleFormat.FORM, true): _*)

    basicRequest
      .method(Method.DELETE, requestURL)
      .contentType("application/json")
      .auth(authConfig, scaleway.kubernetes.ApiKeyLocation.HEADER, "X-Auth-Token")
      .response(asJson[Node])

  /**
   * Retrieve details about a specific Kubernetes Node.
   * 
   * Expected answers:
   *   code 200 : Node ()
   * 
   * Available security schemes:
   *   scaleway (apiKey)
   * 
   * @param region The region you want to target
   * @param nodeId ID of the requested node.
   */
  def getNode(region: String, nodeId: String)(using Auth <:< scaleway.kubernetes.Authorization.ApiKey): sttp.client4.Request[Either[ResponseException[String], Node]] =
    val regionPathParam = PathSerializable.serialize("region", region, PathStyleFormat.SIMPLE, false)
    val nodeIdPathParam = PathSerializable.serialize("node_id", nodeId, PathStyleFormat.SIMPLE, false)
    val requestURL =
      uri"$baseUrl/k8s/v1/regions/${regionPathParam}/nodes/${nodeIdPathParam}"

    basicRequest
      .method(Method.GET, requestURL)
      .contentType("application/json")
      .auth(authConfig, scaleway.kubernetes.ApiKeyLocation.HEADER, "X-Auth-Token")
      .response(asJson[Node])

  /**
   * List all the existing nodes for a specific Kubernetes cluster.
   * 
   * Expected answers:
   *   code 200 : ListNodesResponse ()
   * 
   * Available security schemes:
   *   scaleway (apiKey)
   * 
   * @param region The region you want to target
   * @param clusterId Cluster ID from which the nodes will be listed from.
   * @param poolId Pool ID on which to filter the returned nodes.
   * @param orderBy Sort order of the returned nodes.
   * @param page Page number for the returned nodes.
   * @param pageSize Maximum number of nodes per page.
   * @param name Name to filter on, only nodes containing this substring in their name will be returned.
   * @param status Status to filter on, only nodes with this status will be returned.
   */
  def listNodes(region: String, clusterId: String, poolId: Option[String] = scala.None, orderBy: Option[String] = scala.None, page: Option[Int] = scala.None, pageSize: Option[Int] = scala.None, name: Option[String] = scala.None, status: Option[String] = scala.None)(using Auth <:< scaleway.kubernetes.Authorization.ApiKey): sttp.client4.Request[Either[ResponseException[String], ListNodesResponse]] =
    val regionPathParam = PathSerializable.serialize("region", region, PathStyleFormat.SIMPLE, false)
    val clusterIdPathParam = PathSerializable.serialize("cluster_id", clusterId, PathStyleFormat.SIMPLE, false)
    val requestURL =
      uri"$baseUrl/k8s/v1/regions/${regionPathParam}/clusters/${clusterIdPathParam}/nodes"
        .addParams(FormSerializable.serialize("pool_id", poolId, FormStyleFormat.FORM, true): _*)
        .addParams(FormSerializable.serialize("order_by", orderBy, FormStyleFormat.FORM, true): _*)
        .addParams(FormSerializable.serialize("page", page, FormStyleFormat.FORM, true): _*)
        .addParams(FormSerializable.serialize("page_size", pageSize, FormStyleFormat.FORM, true): _*)
        .addParams(FormSerializable.serialize("name", name, FormStyleFormat.FORM, true): _*)
        .addParams(FormSerializable.serialize("status", status, FormStyleFormat.FORM, true): _*)

    basicRequest
      .method(Method.GET, requestURL)
      .contentType("application/json")
      .auth(authConfig, scaleway.kubernetes.ApiKeyLocation.HEADER, "X-Auth-Token")
      .response(asJson[ListNodesResponse])

  /**
   * Reboot a specific Node. The node will first be drained and pods will be rescheduled onto another node. Note that when there is not enough space to reschedule all the pods (such as in a one-node cluster, or with specific constraints), disruption of your applications may occur.
   * 
   * Expected answers:
   *   code 200 : Node ()
   * 
   * Available security schemes:
   *   scaleway (apiKey)
   * 
   * @param region The region you want to target
   * @param nodeId ID of the node to reboot.
   * @param body 
   */
  def rebootNode(region: String, nodeId: String, body: io.circe.Json)(using Auth <:< scaleway.kubernetes.Authorization.ApiKey): sttp.client4.Request[Either[ResponseException[String], Node]] =
    val regionPathParam = PathSerializable.serialize("region", region, PathStyleFormat.SIMPLE, false)
    val nodeIdPathParam = PathSerializable.serialize("node_id", nodeId, PathStyleFormat.SIMPLE, false)
    val requestURL =
      uri"$baseUrl/k8s/v1/regions/${regionPathParam}/nodes/${nodeIdPathParam}/reboot"

    basicRequest
      .method(Method.POST, requestURL)
      .contentType("application/json")
      .auth(authConfig, scaleway.kubernetes.ApiKeyLocation.HEADER, "X-Auth-Token")
      .body(asJson(body))
      .response(asJson[Node])

end NodesApi