/**
 * VPC API
 * VPC allows you to build your own **V**irtual **P**rivate **C**loud on top of Scaleway’s shared public cloud.   VPC currently comprises the regional Private Networks product. Layer 2 regional Private Networks sit inside the layer 3 VPC. Private Networks allows Scaleway resources (Instances, Load Balancers, Managed Databases etc.) within a single region to be interconnected through a dedicated, private, and flexible [L2 network](https://en.wikipedia.org/wiki/Data_link_layer).  You can add as many resources to your networks as you want, and add up to eight (8) different networks per resource. This allows you to run services isolated from the public internet and expose them to the rest of your infrastructure without worrying about public network filtering.   <Message type=\"note\"> VPC v2 is now in **General Availability**.  </Message>  <Message type=\"tip\"> Check out our [IPAM API](https://www.scaleway.com/en/developers/api/ipam/) to facilitate the management of IP addresses across your different Scaleway resources. </Message>   ## Concepts  Refer to our [dedicated concepts page](https://www.scaleway.com/en/docs/vpc/concepts/) to find definitions of all concepts and terminology related to VPC.     ## Quickstart  1. **Configure your environment variables**      <Message type=\"note\">     This is an optional step that seeks to simplify your usage of the API. See the [Technical information](#technical-information) section below for help choosing an Availability Zone and Region. You can find your Project ID in the [Scaleway console](https://console.scaleway.com/project/settings).     </Message>      ```bash     export SCW_SECRET_KEY=\"<API secret key>\"     export SCW_DEFAULT_REGION=\"<Scaleway region>\"     export SCW_DEFAULT_ZONE=\"<Scaleway Availability Zone>\"     export SCW_PROJECT_ID=\"<Scaleway Project ID>\"     ```  2. **Create a Private Network**. Run the following command to create a Private Network. You can customize the details in the payload (name, tags etc.) to your needs.      ```bash     curl -X POST \\         -H \"X-Auth-Token: $SCW_SECRET_KEY\" \\         -H \"Content-Type: application/json\" \\         \"https://api.scaleway.com/vpc/v2/regions/$SCW_DEFAULT_REGION/private-networks\" \\         -d '{             \"name\": \"My new Private Network\",             \"project_id\": \"'\"$SCW_PROJECT_ID\"'\",              \"tags\": [\"test\", \"dev\"]         }'     ```      <Message type=\"tip\">     Keep the `id` field of the response: it is your Private Network ID, and is useable across all Scaleway products that support Private Networks. It may be useful to you to export the Private Network ID as a new environment variable `export PN_ID=\"<Your Private Network ID>`     </Message>      <Message type=\"tip\">     If you create a Private Network without specifying a VPC to create it in, the behavior depends on when you created your Scaleway Project. [Find out more](https://www.scaleway.com/en/docs/vpc/concepts/#default-vpc)     </Message>  3. **Attach a resource to your Private Network**. Each Scaleway product has its own API to interact with Private Networks. To attach an Instance, Managed Database, Elastic Metal server, Load Balancer or Public Gateway to your Private Network, see instructions in the documentation of the relevant product API. Here, we take the example of an Instance.      Use the following call to attach an Instance to your Private Network. Ensure you replace `<Instance ID>` with the ID of your Instance, and `<Private Network ID>` with the ID of your Private Network. Note that the Instance must be in an Availability Zone that is part of the region of your Private Network.      ```bash     curl -X POST \\         -H \"X-Auth-Token: $SCW_SECRET_KEY\" \\         -H \"Content-Type: application/json\" \\         \"https://api.scaleway.com/instance/v1/zones/$SCW_DEFAULT_ZONE/servers/<Instance ID>/private_nics\" \\         -d '{\"private_network_id\": \"<Private Network ID>\"}'     ```      <Message type=\"tip\">     Keep the `id` field of the response: it is your Private NIC ID. It may be useful to you to export     the Private NIC ID as a new environment variable `export NIC_ID=\"<Your Private NIC ID>`.     </Message>      <Message type=\"tip\">     Keep the `mac_address` field of the response, as it will allow you to identify the Private NIC inside your Instance. If successful, a new network interface will appear inside your Instance, ready to be configured to transmit traffic to other Instances of the same network, with the MAC address returned by the API call.     </Message>  4. **Confirm that the network interface has been plugged in**. To do this, connect to your Instance and run `dmseg`. You should see an output similar to the following:      ```bash     [1579004.592869] pci 0000:00:05.0: [1af4:1000] type 00 class 0x020000     [1579004.594835] pci 0000:00:05.0: reg 0x10: [io  0x0000-0x003f]     [1579004.596715] pci 0000:00:05.0: reg 0x14: [mem 0x00000000-0x00000fff]     [1579004.598732] pci 0000:00:05.0: reg 0x20: [mem 0x00000000-0x00003fff 64bit pref]     [1579004.600765] pci 0000:00:05.0: reg 0x30: [mem 0x00000000-0x0007ffff pref]     [1579004.603819] pci 0000:00:05.0: BAR 6: assigned [mem 0xc0100000-0xc017ffff pref]     [1579004.604582] pci 0000:00:05.0: BAR 4: assigned [mem 0x100000c000-0x100000ffff 64bit pref]     [1579004.605555] pci 0000:00:05.0: BAR 1: assigned [mem 0xc0003000-0xc0003fff]     [1579004.606383] pci 0000:00:05.0: BAR 0: assigned [io  0x1000-0x103f]     [1579004.607212] virtio-pci 0000:00:05.0: enabling device (0000 -> 0003)     [1579004.625149] PCI Interrupt Link [LNKA] enabled at IRQ 11     [1579004.644930] virtio_net virtio3 ens5: renamed from eth0     ```  5. **Confirm the presence of the network interface, and confirm its name if several networks are plugged into your Instance**. To do this, run `ip -br link`. You should see an output similar to the following:      ```bash     lo               UNKNOWN        00:00:00:00:00:00 <LOOPBACK,UP,LOWER_UP>     ens2             UP             de:1c:94:44:d0:04 <BROADCAST,MULTICAST,UP,LOWER_UP>     ens5             DOWN           02:00:00:00:00:31 <BROADCAST,MULTICAST>     ens6             DOWN           02:00:00:00:01:5b <BROADCAST,MULTICAST>     ens7             DOWN           02:00:00:00:01:5e <BROADCAST,MULTICAST>     ```  6. **Configure the Instance's IP address**. DHCP is activated by default on new Private Networks, and automatically assigns IP addresses to resources on the network. If you have an older Private Network, [check whether DHCP is activated](https://www.scaleway.com/en/docs/vpc/reference-content/vpc-migration/) and either activate DHCP for automatic IP configuration, or [manually configure](https://www.scaleway.com/en/docs/instances/reference-content/manual-configuration-private-ips/) the network interface on your Instance if necessary.   7. **Delete your Private NIC**, which equates to unplugging your Instance from the Private Network. Use the following call. Ensure you replace `<Instance ID>` with the ID of your Instance, `<Private Network ID>` with the ID of your Private Network, and `<NIC ID>` with the ID of your Private NIC.      ```bash     curl -X DELETE \\         -H \"X-Auth-Token: $SCW_SECRET_KEY\" \\         -H \"Content-Type: application/json\" \\         \"https://api.scaleway.com/instance/v1/zones/$SCW_DEFAULT_ZONE/servers/<Instance ID>/private_nics/<NIC ID>\"     ```      The network interface disappears from your Instance.  8. **Delete your Private Network**. Use the following call. Ensure you replace `<Private Network ID>` with the ID of your Private Network.      ```bash     curl -X DELETE \\         -H \"X-Auth-Token: $SCW_SECRET_KEY\" \\         -H \"Content-Type: application/json\" \\         \"https://api.scaleway.com/vpc/v2/regions/$SCW_DEFAULT_REGION/private-networks/<Private Network ID>\"     ```      <Message type=\"note\">     Private Networks must be empty to be deleted. Ensure you have detached all resources and deleted all reserved IPs from your network prior to deletion.     </Message>         <Message type=\"requirement\">     - You have a [Scaleway account](https://console.scaleway.com/)     - You have created an [API key](https://www.scaleway.com/en/docs/iam/how-to/create-api-keys/) and that the API key has sufficient [IAM permissions](https://www.scaleway.com/en/docs/iam/reference-content/permission-sets/) to perform the actions described on this page     - You have [installed `curl`](https://curl.se/download.html)     </Message>       ## Technical information  VPC and Private Networks are available in the Paris, Amsterdam and Warsaw regions, which are represented by the following path parameters:  * `fr-par` * `it-mil` * `nl-ams` * `pl-waw`  ## Technical limitations  The following limitations apply to Scaleway VPC:  - Up to 250 resources can be attached to a Private Network. - A resource can be attached to up to 8 Private Networks. - The following resource types can be attached to a Private Network:     - Instances     - Elastic Metal servers     - Apple silicon     - Managed Inference     - Load Balancers     - Public Gateways     - Managed Databases for PostgreSQL and MySQL     - Managed Databases for Redis (only at the time of resource creation)     - Kubernetes Kapsule (only at the time of resource creation) - The MAC address of an Instance in a Private Network cannot be changed. - Broadcast and multicast traffic, while supported, are heavily rate-limited.  ## Going further  For more help using Scaleway VPC and Private Networks, check out the following resources: - Our [main documentation](https://www.scaleway.com/en/docs/vpc/) - The #virtual-private-cloud channel on our [Slack Community](https://www.scaleway.com/en/docs/tutorials/scaleway-slack-community/) - Our [support ticketing system](https://www.scaleway.com/en/docs/account/how-to/open-a-support-ticket/).
 *
 * The version of the OpenAPI document: v2
 * 
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */
package scaleway.vpc.api

import scaleway.vpc.models.CreatePrivateNetworkRequest
import scaleway.vpc.models.ListPrivateNetworksResponse
import scaleway.vpc.models.PrivateNetwork
import scaleway.vpc.models.UpdatePrivateNetworkRequest
import com.github.plokhotnyuk.jsoniter_scala.circe.JsoniterScalaCodec.*
import scaleway.vpc.JsonSupport.{*, given}
import scaleway.vpc.FormSerializable
import scaleway.vpc.FormStyleFormat
import scaleway.vpc.HeaderSerializable
import scaleway.vpc.ApiKeyLocation
import scaleway.vpc.PathStyleFormat
import scaleway.vpc.PathSerializable
import scaleway.vpc.CookieSerializable
import scaleway.vpc.Helpers.*
import sttp.client4.jsoniter.*
import sttp.client4.*
import sttp.model.Method

object PrivateNetworksApi:
  def apply(baseUrl: String = "https://api.scaleway.com"): PrivateNetworksApi[scaleway.vpc.Authorization.NoAuthorization.type] = PrivateNetworksApi(baseUrl, scaleway.vpc.Authorization.NoAuthorization)
  def withBasicAuth(baseUrl: String, username: String, password: String): PrivateNetworksApi[scaleway.vpc.Authorization.BasicAuth] =
    PrivateNetworksApi(baseUrl, scaleway.vpc.Authorization.BasicAuth(username, password))

  def withApiKeyAuth(baseUrl: String, apiKey: String): PrivateNetworksApi[scaleway.vpc.Authorization.ApiKey] =
    PrivateNetworksApi(baseUrl, scaleway.vpc.Authorization.ApiKey(apiKey))

  def withBearerTokenAuth(baseUrl: String, token: String): PrivateNetworksApi[scaleway.vpc.Authorization.BearerToken] =
    PrivateNetworksApi(baseUrl, scaleway.vpc.Authorization.BearerToken(token))

case class PrivateNetworksApi[Auth <: scaleway.vpc.Authorization] private (baseUrl: String, authConfig: scaleway.vpc.Authorization):
  def withBasicAuth(username: String, password: String): PrivateNetworksApi[scaleway.vpc.Authorization.BasicAuth] =
    copy(authConfig = scaleway.vpc.Authorization.BasicAuth(username, password))

  def withApiKeyAuth(apiKey: String): PrivateNetworksApi[scaleway.vpc.Authorization.ApiKey] =
    copy(authConfig = scaleway.vpc.Authorization.ApiKey(apiKey))

  def withNoAuth: PrivateNetworksApi[scaleway.vpc.Authorization.NoAuthorization.type] =
    copy(authConfig = scaleway.vpc.Authorization.NoAuthorization)

  def withBearerTokenAuth(token: String): PrivateNetworksApi[scaleway.vpc.Authorization.BearerToken] =
    copy(authConfig = scaleway.vpc.Authorization.BearerToken(token))

  /**
   * Create a new Private Network. Once created, you can attach Scaleway resources which are in the same region.
   * 
   * Expected answers:
   *   code 200 : PrivateNetwork ()
   * 
   * Available security schemes:
   *   scaleway (apiKey)
   * 
   * @param region The region you want to target
   * @param createPrivateNetworkRequest 
   */
  def createPrivateNetwork(region: String, createPrivateNetworkRequest: CreatePrivateNetworkRequest)(using Auth <:< scaleway.vpc.Authorization.ApiKey): sttp.client4.Request[Either[ResponseException[String], PrivateNetwork]] =
    val regionPathParam = PathSerializable.serialize("region", region, PathStyleFormat.SIMPLE, false)
    val requestURL =
      uri"$baseUrl/vpc/v2/regions/${regionPathParam}/private-networks"

    basicRequest
      .method(Method.POST, requestURL)
      .contentType("application/json")
      .auth(authConfig, scaleway.vpc.ApiKeyLocation.HEADER, "X-Auth-Token")
      .body(asJson(createPrivateNetworkRequest))
      .response(asJson[PrivateNetwork])

  /**
   * Delete an existing Private Network. Note that you must first detach all resources from the network, in order to delete it.
   * 
   * Expected answers:
   *   code 204 :  ()
   * 
   * Available security schemes:
   *   scaleway (apiKey)
   * 
   * @param region The region you want to target
   * @param privateNetworkId Private Network ID. (UUID format)
   */
  def deletePrivateNetwork(region: String, privateNetworkId: String)(using Auth <:< scaleway.vpc.Authorization.ApiKey): sttp.client4.Request[Either[ResponseException[String], Unit]] =
    val regionPathParam = PathSerializable.serialize("region", region, PathStyleFormat.SIMPLE, false)
    val privateNetworkIdPathParam = PathSerializable.serialize("private_network_id", privateNetworkId, PathStyleFormat.SIMPLE, false)
    val requestURL =
      uri"$baseUrl/vpc/v2/regions/${regionPathParam}/private-networks/${privateNetworkIdPathParam}"

    basicRequest
      .method(Method.DELETE, requestURL)
      .contentType("application/json")
      .auth(authConfig, scaleway.vpc.ApiKeyLocation.HEADER, "X-Auth-Token")
      .response(asString.mapWithMetadata(ResponseAs.deserializeRightWithError(_ => Right(()))))

  /**
   * Enable DHCP managed on an existing Private Network. Note that you will not be able to deactivate it afterwards.
   * 
   * Expected answers:
   *   code 200 : PrivateNetwork ()
   * 
   * Available security schemes:
   *   scaleway (apiKey)
   * 
   * @param region The region you want to target
   * @param privateNetworkId Private Network ID. (UUID format)
   * @param body 
   */
  def enableDHCP(region: String, privateNetworkId: String, body: io.circe.Json)(using Auth <:< scaleway.vpc.Authorization.ApiKey): sttp.client4.Request[Either[ResponseException[String], PrivateNetwork]] =
    val regionPathParam = PathSerializable.serialize("region", region, PathStyleFormat.SIMPLE, false)
    val privateNetworkIdPathParam = PathSerializable.serialize("private_network_id", privateNetworkId, PathStyleFormat.SIMPLE, false)
    val requestURL =
      uri"$baseUrl/vpc/v2/regions/${regionPathParam}/private-networks/${privateNetworkIdPathParam}/enable-dhcp"

    basicRequest
      .method(Method.POST, requestURL)
      .contentType("application/json")
      .auth(authConfig, scaleway.vpc.ApiKeyLocation.HEADER, "X-Auth-Token")
      .body(asJson(body))
      .response(asJson[PrivateNetwork])

  /**
   * Retrieve information about an existing Private Network, specified by its Private Network ID. Its full details are returned in the response object.
   * 
   * Expected answers:
   *   code 200 : PrivateNetwork ()
   * 
   * Available security schemes:
   *   scaleway (apiKey)
   * 
   * @param region The region you want to target
   * @param privateNetworkId Private Network ID. (UUID format)
   */
  def getPrivateNetwork(region: String, privateNetworkId: String)(using Auth <:< scaleway.vpc.Authorization.ApiKey): sttp.client4.Request[Either[ResponseException[String], PrivateNetwork]] =
    val regionPathParam = PathSerializable.serialize("region", region, PathStyleFormat.SIMPLE, false)
    val privateNetworkIdPathParam = PathSerializable.serialize("private_network_id", privateNetworkId, PathStyleFormat.SIMPLE, false)
    val requestURL =
      uri"$baseUrl/vpc/v2/regions/${regionPathParam}/private-networks/${privateNetworkIdPathParam}"

    basicRequest
      .method(Method.GET, requestURL)
      .contentType("application/json")
      .auth(authConfig, scaleway.vpc.ApiKeyLocation.HEADER, "X-Auth-Token")
      .response(asJson[PrivateNetwork])

  /**
   * List existing Private Networks in the specified region. By default, the Private Networks returned in the list are ordered by creation date in ascending order, though this can be modified via the order_by field.
   * 
   * Expected answers:
   *   code 200 : ListPrivateNetworksResponse ()
   * 
   * Available security schemes:
   *   scaleway (apiKey)
   * 
   * @param region The region you want to target
   * @param orderBy Sort order of the returned Private Networks.
   * @param page Page number to return, from the paginated results.
   * @param pageSize Maximum number of Private Networks to return per page.
   * @param name Name to filter for. Only Private Networks with names containing this string will be returned.
   * @param tags Tags to filter for. Only Private Networks with one or more matching tags will be returned.
   * @param organizationId Organization ID to filter for. Only Private Networks belonging to this Organization will be returned. (UUID format)
   * @param projectId Project ID to filter for. Only Private Networks belonging to this Project will be returned. (UUID format)
   * @param privateNetworkIds Private Network IDs to filter for. Only Private Networks with one of these IDs will be returned.
   * @param vpcId VPC ID to filter for. Only Private Networks belonging to this VPC will be returned. (UUID format)
   * @param dhcpEnabled DHCP status to filter for. When true, only Private Networks with managed DHCP enabled will be returned.
   */
  def listPrivateNetworks(region: String, orderBy: Option[String] = scala.None, page: Option[Int] = scala.None, pageSize: Option[Int] = scala.None, name: Option[String] = scala.None, tags: Seq[String], organizationId: Option[String] = scala.None, projectId: Option[String] = scala.None, privateNetworkIds: Seq[String], vpcId: Option[String] = scala.None, dhcpEnabled: Option[Boolean] = scala.None)(using Auth <:< scaleway.vpc.Authorization.ApiKey): sttp.client4.Request[Either[ResponseException[String], ListPrivateNetworksResponse]] =
    val regionPathParam = PathSerializable.serialize("region", region, PathStyleFormat.SIMPLE, false)
    val requestURL =
      uri"$baseUrl/vpc/v2/regions/${regionPathParam}/private-networks"
        .addParams(FormSerializable.serialize("order_by", orderBy, FormStyleFormat.FORM, true): _*)
        .addParams(FormSerializable.serialize("page", page, FormStyleFormat.FORM, true): _*)
        .addParams(FormSerializable.serialize("page_size", pageSize, FormStyleFormat.FORM, true): _*)
        .addParams(FormSerializable.serialize("name", name, FormStyleFormat.FORM, true): _*)
        .addParams(FormSerializable.serialize("tags", tags, FormStyleFormat.FORM, true): _*)
        .addParams(FormSerializable.serialize("organization_id", organizationId, FormStyleFormat.FORM, true): _*)
        .addParams(FormSerializable.serialize("project_id", projectId, FormStyleFormat.FORM, true): _*)
        .addParams(FormSerializable.serialize("private_network_ids", privateNetworkIds, FormStyleFormat.FORM, true): _*)
        .addParams(FormSerializable.serialize("vpc_id", vpcId, FormStyleFormat.FORM, true): _*)
        .addParams(FormSerializable.serialize("dhcp_enabled", dhcpEnabled, FormStyleFormat.FORM, true): _*)

    basicRequest
      .method(Method.GET, requestURL)
      .contentType("application/json")
      .auth(authConfig, scaleway.vpc.ApiKeyLocation.HEADER, "X-Auth-Token")
      .response(asJson[ListPrivateNetworksResponse])

  /**
   * Update parameters (such as name or tags) of an existing Private Network, specified by its Private Network ID.
   * 
   * Expected answers:
   *   code 200 : PrivateNetwork ()
   * 
   * Available security schemes:
   *   scaleway (apiKey)
   * 
   * @param region The region you want to target
   * @param privateNetworkId Private Network ID. (UUID format)
   * @param updatePrivateNetworkRequest 
   */
  def updatePrivateNetwork(region: String, privateNetworkId: String, updatePrivateNetworkRequest: UpdatePrivateNetworkRequest)(using Auth <:< scaleway.vpc.Authorization.ApiKey): sttp.client4.Request[Either[ResponseException[String], PrivateNetwork]] =
    val regionPathParam = PathSerializable.serialize("region", region, PathStyleFormat.SIMPLE, false)
    val privateNetworkIdPathParam = PathSerializable.serialize("private_network_id", privateNetworkId, PathStyleFormat.SIMPLE, false)
    val requestURL =
      uri"$baseUrl/vpc/v2/regions/${regionPathParam}/private-networks/${privateNetworkIdPathParam}"

    basicRequest
      .method(Method.PATCH, requestURL)
      .contentType("application/json")
      .auth(authConfig, scaleway.vpc.ApiKeyLocation.HEADER, "X-Auth-Token")
      .body(asJson(updatePrivateNetworkRequest))
      .response(asJson[PrivateNetwork])

end PrivateNetworksApi