/**
 * Instance API
 * Scaleway Instances are virtual machines in the cloud. Different [Instance types](https://www.scaleway.com/en/docs/instances/reference-content/choosing-instance-type/) offer different technical specifications in terms of vCPU, RAM, bandwidth and storage. Once you have created your Instance and installed your image of choice (e.g. an operating system), you can [connect to your Instance via SSH](https://www.scaleway.com/en/docs/instances/how-to/connect-to-instance/) to use it as you wish. When you are done using the Instance, you can delete it from your account.   <Message type=\"tip\"> To retrieve information about the different [images](#path-images) available to install on Scaleway Instances, check out our [Marketplace API](https://www.scaleway.com/en/developers/api/marketplace/). </Message>    ## Concepts  Refer to our [dedicated concepts page](https://www.scaleway.com/en/docs/instances/concepts/) to find definitions of all concepts and terminology related to Instances.     ## Quickstart  1. Configure your environment variables      <Message type=\"note\">     This is an optional step that seeks to simplify your usage of the Instances API. See [Availability Zones](#availability-zones) below for help choosing an Availability Zone. You can find your Project ID in the [Scaleway console](https://console.scaleway.com/project/settings).     </Message>      ```bash     export SCW_SECRET_KEY=\"<API secret key>\"     export SCW_DEFAULT_ZONE=\"<Scaleway Availability Zone>\"     export SCW_PROJECT_ID=\"<Scaleway Project ID>\"     ```  2. **Create an Instance**: Run the following command to create an Instance. You can customize the details in the payload (name, description, type, tags etc) to your needs: use the information below to adjust the payload as necessary.      ```bash     curl -X POST \\       -H \"X-Auth-Token: $SCW_SECRET_KEY\" \\       -H \"Content-Type: application/json\" \\       \"https://api.scaleway.com/instance/v1/zones/$SCW_DEFAULT_ZONE/servers\" \\         -d '{           \"name\": \"my-new-instance\",           \"project\": \"'\"$SCW_PROJECT_ID\"'\",           \"commercial_type\": \"GP1-S\",           \"image\": \"ubuntu_noble\",           \"enable_ipv6\": true,           \"volumes\": {             \"0\":{               \"size\": 300000000000,               \"volume_type\": \"l_ssd\"             }           }         }'     ```     | Parameter         | Description                                                                                                                                                                                                                       | Valid values                                                                                                                                                                                                                                                                                                                                                                                                                                                                                             |    |:------------------|:----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|:---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|    | `name`            | A name of your choice for the Instance (string)                                                                                                                                                                                   | Any string containing only alphanumeric characters, dots, spaces and dashes, e.g. `\"my-new-instance\"`.                                                                                                                                                                                                                                                                                                                                                                                                   |    | `project`         | The Project in which the Instance should be created (string)                                                                                                                                                                      | Any valid Scaleway Project ID (see above), e.g. `\"b4bd99e0-b389-11ed-afa1-0242ac120002\"`                                                                                                                                                                                                                                                                                                                                                                                                                 |    | `commercial-type` | The commercial Instance type to create (string)                                                                                                                                                                                   | Any valid ID of a Scaleway commercial Instance type, e.g. `\"GP1-S\"`, `\"PRO2-M\"`. Use the [List Instance Types](#path-instance-types-list-instance-types) endpoint to get a list of all valid Instance types and their IDs.                                                                                                                                                                                                                                                                               |    | `image`           | The image to install on the Instance, e.g. a particular OS (string)                                                                                                                                                               | Any Scaleway image label, e.g. `\"ubuntu_noble\"`, or any valid Scaleway image ID, e.g. `\"6fc0ade6-d6a3-4fb9-87ab-2444ac71e5c0\"` which is the ID for the `Ubuntu 24.04 Noble Numbat` image. Use the [List Instance Images](#path-images-list-instance-images) endpoint to get a list of all available images with their IDs and labels, or check out the [Scaleway Marketplace API](https://www.scaleway.com/en/developers/api/marketplace/).                                                              |    | `enable_ipv6`     | Whether to enable IPv6 on the Instance (boolean)                                                                                                                                                                                  | `true` or `false`                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        |    | `volumes`         | An object that specifies the storage volumes to attach to the Instance. For more information, see **Creating an Instance: the volumes object** in the [Technical information](#technical-information) section of this quickstart. | A (dictionary) object with a minimum of one key (`\"0\"`) whose value is another object containing the parameters `\"name\"` (a name for the volume), `\"size\"` (the size for the volume, in bytes), and `\"volume_type\"` (`\"l_ssd\"`). Additional keys for additional volumes should increment by 1 each time (the second volume would have a key of `1`.) Further parameters are available, and it is possible to attach existing volumes rather than creating a new one, or create a volume from a snapshot. |  3. **List your Instances**: run the following command to get a list of all the Instances in your account, with their details:      ```bash     curl -X GET \\       -H \"Content-Type: application/json\" \\       -H \"X-Auth-Token: $SCW_SECRET_KEY\" \\       \"https://api.scaleway.com/instance/v1/zones/$SCW_DEFAULT_ZONE/servers/\"     ```  4. **Delete an Instance**: run the following command to delete an Instance, specified by its Instance ID:      ```bash     curl -X DELETE \\       -H \"X-Auth-Token: $SCW_SECRET_KEY\" \\       -H \"Content-Type: application/json\" \\       \"https://api.scaleway.com/instance/v1/zones/$SCW_DEFAULT_ZONE/servers/<Instance-ID>\"     ```      The expected successful response is empty.   <Message type=\"requirement\"> - You have a [Scaleway account](https://console.scaleway.com/) - You have created an [API key](https://www.scaleway.com/en/docs/iam/how-to/create-api-keys/) and that the API key has sufficient [IAM permissions](https://www.scaleway.com/en/docs/iam/reference-content/permission-sets/) to perform the actions described on this page - You have [installed `curl`](https://curl.se/download.html) </Message>   ## Technical information  ### Availability Zones  Instances can be deployed in the following Availability Zones:  | Name      | API ID                | |-----------|-----------------------| | Paris     | `fr-par-1` `fr-par-2` `fr-par-3` | | Amsterdam | `nl-ams-1` `nl-ams-2` `nl-ams-3` | | Warsaw    | `pl-waw-1` `pl-waw-2` `pl-waw-3` |     ### Pagination  Most listing requests receive a paginated response. Requests against paginated endpoints accept two `query` arguments:  - `page`, a positive integer to choose which page to return. - `per_page`, an positive integer lower or equal to 100 to select the number of items to return per page. The default value is `50`.  Paginated endpoints usually also accept filters to search and sort results.These filters are documented along each endpoint documentation.  The `X-Total-Count` header contains the total number of items returned.     ### Creating an Instance: the volumes object  When [creating an Instance](#path-instances-create-an-instance) using the Scaleway API, the `volumes` object is **not strictly required**. However, the defaults vary depending on certain conditions:  1. If an image label is used:    - The default will be an `sbs_volume` volume.    - The size of this volume will be the OS size (typically 10GB in most cases).  2. If an image ID from the marketplace is used:    - If the Instance supports local storage:      - The default will be an `l_ssd` volume.      - The size of this volume will be the instance's maximum local storage capacity.    - Else, the volume created will depend on the marketplace's local_image type:      - SBS volume for instance_sbs type.      - l_ssd volume for instance_local type.  If you want to customize the storage configuration or add additional volumes, you will need to include the volumes object in your API request. This object should contain at least one (dictionary) object with a minimum of one key (`\"0\"`) whose value is another object containing the parameters `\"name\"` (a name for the volume), `\"size\"` (the size for the volume, in bytes), and `\"volume_type\"` (`\"sbs_volume\"` or `\"l_ssd\"`). Additional keys for additional volumes should increment by 1 each time (the second volume would have a key of `\"1\"`.)  Note that volume `size` must respect the volume constraints of the Instance's `commercial_type`: for each type of Instance, a minimum amount of storage is required, and there is also a maximum that cannot be exceeded. All Instance types support Block Storage (`sbs_volume`), some also support local storage (`l_ssd`). Read more about these constraints in the [List Instance types](#path-instance-types-list-instance-types) documentation, specifically the `volume_constraints` parameter for each type listed in the response  You can use the `volumes` object in different ways. The table below shows which parameters are required for each of the following use cases:  | Use case                | Required params       | Optional params     | Notes                                  | |-------------------------|-----------------------|---------------------|----------------------------------------| | Create a volume (`l_ssd`, `sbs_volume`) from a snapshot of an image  |  | `volume_type`, `size`, `boot` | If the `size` parameter is not set, the size of the volume will equal the size of the corresponding snapshot of the image. The image snapshot type should be compatible with the `volume_type`. | | Create a volume (`l_ssd`) from a snapshot     | `base_snapshot`, `name`, `volume_type` | `boot` |  | | Create a volume of type `sbs_volume` from a snapshot     | `base_snapshot`, `name`, `volume_type` | `size`, `boot` |  | | Create an empty volume      | `name`, `volume_type`, `size` | `boot` |  | | Attach an existing volume (`l_ssd`)  | `id` | `boot` |  | | Attach an existing volume of type `sbs_volume`   | `id`, `volume_type` | `boot` |  |   <Message type=\"note\"> This information is designed to help you correctly configure the `volumes` object when using the [Create an Instance](#path-instances-create-an-instance) or [Update an Instance](#path-instances-update-an-instance) methods. </Message>   ## Going further  For more help using Scaleway Instances, check out the following resources: - Our [main documentation](https://www.scaleway.com/en/docs/instances/) - The #instance channel on our [Slack Community](https://www.scaleway.com/en/docs/tutorials/scaleway-slack-community/) - Our [support ticketing system](https://www.scaleway.com/en/docs/account/how-to/open-a-support-ticket/).
 *
 * The version of the OpenAPI document: v1
 * 
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */
package scaleway.instance.api

import scaleway.instance.models.AttachServerFileSystemRequest
import scaleway.instance.models.AttachServerFileSystemResponse
import scaleway.instance.models.AttachServerVolumeRequest
import scaleway.instance.models.AttachServerVolumeResponse
import scaleway.instance.models.CreateServerRequest
import scaleway.instance.models.CreateServerResponse
import scaleway.instance.models.DetachServerFileSystemResponse
import scaleway.instance.models.DetachServerVolumeRequest
import scaleway.instance.models.DetachServerVolumeResponse
import scaleway.instance.models.GetServerResponse
import scaleway.instance.models.ListServerActionsResponse
import scaleway.instance.models.ListServersResponse
import scaleway.instance.models.ServerActionRequest
import scaleway.instance.models.ServerActionResponse
import scaleway.instance.models.ServerCompatibleTypes
import scaleway.instance.models.UpdateServerRequest
import scaleway.instance.models.UpdateServerResponse
import scaleway.instance.JsonSupport.{*, given}
import scaleway.instance.FormSerializable
import scaleway.instance.FormStyleFormat
import scaleway.instance.HeaderSerializable
import scaleway.instance.ApiKeyLocation
import scaleway.instance.PathStyleFormat
import scaleway.instance.PathSerializable
import scaleway.instance.CookieSerializable
import scaleway.instance.Helpers.*
import sttp.client4.jsoniter.*
import sttp.client4.*
import sttp.model.Method

object InstancesApi:
  def apply(baseUrl: String = "https://api.scaleway.com"): InstancesApi[scaleway.instance.Authorization.NoAuthorization.type] = InstancesApi(baseUrl, scaleway.instance.Authorization.NoAuthorization)
  def withBasicAuth(baseUrl: String, username: String, password: String): InstancesApi[scaleway.instance.Authorization.BasicAuth] =
    InstancesApi(baseUrl, scaleway.instance.Authorization.BasicAuth(username, password))

  def withApiKeyAuth(baseUrl: String, apiKey: String): InstancesApi[scaleway.instance.Authorization.ApiKey] =
    InstancesApi(baseUrl, scaleway.instance.Authorization.ApiKey(apiKey))

  def withBearerTokenAuth(baseUrl: String, token: String): InstancesApi[scaleway.instance.Authorization.BearerToken] =
    InstancesApi(baseUrl, scaleway.instance.Authorization.BearerToken(token))

case class InstancesApi[Auth <: scaleway.instance.Authorization] private (baseUrl: String, authConfig: scaleway.instance.Authorization):
  def withBasicAuth(username: String, password: String): InstancesApi[scaleway.instance.Authorization.BasicAuth] =
    copy(authConfig = scaleway.instance.Authorization.BasicAuth(username, password))

  def withApiKeyAuth(apiKey: String): InstancesApi[scaleway.instance.Authorization.ApiKey] =
    copy(authConfig = scaleway.instance.Authorization.ApiKey(apiKey))

  def withNoAuth: InstancesApi[scaleway.instance.Authorization.NoAuthorization.type] =
    copy(authConfig = scaleway.instance.Authorization.NoAuthorization)

  def withBearerTokenAuth(token: String): InstancesApi[scaleway.instance.Authorization.BearerToken] =
    copy(authConfig = scaleway.instance.Authorization.BearerToken(token))

  /**
   * Expected answers:
   *   code 200 : AttachServerFileSystemResponse ()
   * 
   * Available security schemes:
   *   scaleway (apiKey)
   * 
   * @param zone The zone you want to target
   * @param serverId 
   * @param attachServerFileSystemRequest 
   */
  def attachServerFileSystem(zone: String, serverId: String, attachServerFileSystemRequest: AttachServerFileSystemRequest)(using Auth <:< scaleway.instance.Authorization.ApiKey): sttp.client4.Request[Either[ResponseException[String], AttachServerFileSystemResponse]] =
    val zonePathParam = PathSerializable.serialize("zone", zone, PathStyleFormat.SIMPLE, false)
    val serverIdPathParam = PathSerializable.serialize("server_id", serverId, PathStyleFormat.SIMPLE, false)
    val requestURL =
      uri"$baseUrl/instance/v1/zones/${zonePathParam}/servers/${serverIdPathParam}/attach-filesystem"

    basicRequest
      .method(Method.POST, requestURL)
      .contentType("application/json")
      .auth(authConfig, scaleway.instance.ApiKeyLocation.HEADER, "X-Auth-Token")
      .body(asJson(attachServerFileSystemRequest))
      .response(asJson[AttachServerFileSystemResponse])

  /**
   * Expected answers:
   *   code 200 : AttachServerVolumeResponse ()
   * 
   * Available security schemes:
   *   scaleway (apiKey)
   * 
   * @param zone The zone you want to target
   * @param serverId 
   * @param attachServerVolumeRequest 
   */
  def attachServerVolume(zone: String, serverId: String, attachServerVolumeRequest: AttachServerVolumeRequest)(using Auth <:< scaleway.instance.Authorization.ApiKey): sttp.client4.Request[Either[ResponseException[String], AttachServerVolumeResponse]] =
    val zonePathParam = PathSerializable.serialize("zone", zone, PathStyleFormat.SIMPLE, false)
    val serverIdPathParam = PathSerializable.serialize("server_id", serverId, PathStyleFormat.SIMPLE, false)
    val requestURL =
      uri"$baseUrl/instance/v1/zones/${zonePathParam}/servers/${serverIdPathParam}/attach-volume"

    basicRequest
      .method(Method.POST, requestURL)
      .contentType("application/json")
      .auth(authConfig, scaleway.instance.ApiKeyLocation.HEADER, "X-Auth-Token")
      .body(asJson(attachServerVolumeRequest))
      .response(asJson[AttachServerVolumeResponse])

  /**
   * Create a new Instance of the specified commercial type in the specified zone. Pay attention to the volumes parameter, which takes an object which can be used in different ways to achieve different behaviors. Get more information in the [Technical Information](#technical-information) section of the introduction.
   * 
   * Expected answers:
   *   code 201 : CreateServerResponse ()
   * 
   * Available security schemes:
   *   scaleway (apiKey)
   * 
   * @param zone The zone you want to target
   * @param createServerRequest 
   */
  def createServer(zone: String, createServerRequest: CreateServerRequest)(using Auth <:< scaleway.instance.Authorization.ApiKey): sttp.client4.Request[Either[ResponseException[String], CreateServerResponse]] =
    val zonePathParam = PathSerializable.serialize("zone", zone, PathStyleFormat.SIMPLE, false)
    val requestURL =
      uri"$baseUrl/instance/v1/zones/${zonePathParam}/servers"

    basicRequest
      .method(Method.POST, requestURL)
      .contentType("application/json")
      .auth(authConfig, scaleway.instance.ApiKeyLocation.HEADER, "X-Auth-Token")
      .body(asJson(createServerRequest))
      .response(asJson[CreateServerResponse])

  /**
   * Delete the Instance with the specified ID.
   * 
   * Expected answers:
   *   code 204 :  ()
   * 
   * Available security schemes:
   *   scaleway (apiKey)
   * 
   * @param zone The zone you want to target
   * @param serverId 
   */
  def deleteServer(zone: String, serverId: String)(using Auth <:< scaleway.instance.Authorization.ApiKey): sttp.client4.Request[Either[ResponseException[String], Unit]] =
    val zonePathParam = PathSerializable.serialize("zone", zone, PathStyleFormat.SIMPLE, false)
    val serverIdPathParam = PathSerializable.serialize("server_id", serverId, PathStyleFormat.SIMPLE, false)
    val requestURL =
      uri"$baseUrl/instance/v1/zones/${zonePathParam}/servers/${serverIdPathParam}"

    basicRequest
      .method(Method.DELETE, requestURL)
      .contentType("application/json")
      .auth(authConfig, scaleway.instance.ApiKeyLocation.HEADER, "X-Auth-Token")
      .response(asString.mapWithMetadata(ResponseAs.deserializeRightWithError(_ => Right(()))))

  /**
   * Expected answers:
   *   code 200 : DetachServerFileSystemResponse ()
   * 
   * Available security schemes:
   *   scaleway (apiKey)
   * 
   * @param zone The zone you want to target
   * @param serverId 
   * @param attachServerFileSystemRequest 
   */
  def detachServerFileSystem(zone: String, serverId: String, attachServerFileSystemRequest: AttachServerFileSystemRequest)(using Auth <:< scaleway.instance.Authorization.ApiKey): sttp.client4.Request[Either[ResponseException[String], DetachServerFileSystemResponse]] =
    val zonePathParam = PathSerializable.serialize("zone", zone, PathStyleFormat.SIMPLE, false)
    val serverIdPathParam = PathSerializable.serialize("server_id", serverId, PathStyleFormat.SIMPLE, false)
    val requestURL =
      uri"$baseUrl/instance/v1/zones/${zonePathParam}/servers/${serverIdPathParam}/detach-filesystem"

    basicRequest
      .method(Method.POST, requestURL)
      .contentType("application/json")
      .auth(authConfig, scaleway.instance.ApiKeyLocation.HEADER, "X-Auth-Token")
      .body(asJson(attachServerFileSystemRequest))
      .response(asJson[DetachServerFileSystemResponse])

  /**
   * Expected answers:
   *   code 200 : DetachServerVolumeResponse ()
   * 
   * Available security schemes:
   *   scaleway (apiKey)
   * 
   * @param zone The zone you want to target
   * @param serverId 
   * @param detachServerVolumeRequest 
   */
  def detachServerVolume(zone: String, serverId: String, detachServerVolumeRequest: DetachServerVolumeRequest)(using Auth <:< scaleway.instance.Authorization.ApiKey): sttp.client4.Request[Either[ResponseException[String], DetachServerVolumeResponse]] =
    val zonePathParam = PathSerializable.serialize("zone", zone, PathStyleFormat.SIMPLE, false)
    val serverIdPathParam = PathSerializable.serialize("server_id", serverId, PathStyleFormat.SIMPLE, false)
    val requestURL =
      uri"$baseUrl/instance/v1/zones/${zonePathParam}/servers/${serverIdPathParam}/detach-volume"

    basicRequest
      .method(Method.POST, requestURL)
      .contentType("application/json")
      .auth(authConfig, scaleway.instance.ApiKeyLocation.HEADER, "X-Auth-Token")
      .body(asJson(detachServerVolumeRequest))
      .response(asJson[DetachServerVolumeResponse])

  /**
   * Get the details of a specified Instance.
   * 
   * Expected answers:
   *   code 200 : GetServerResponse ()
   * 
   * Available security schemes:
   *   scaleway (apiKey)
   * 
   * @param zone The zone you want to target
   * @param serverId UUID of the Instance you want to get.
   */
  def getServer(zone: String, serverId: String)(using Auth <:< scaleway.instance.Authorization.ApiKey): sttp.client4.Request[Either[ResponseException[String], GetServerResponse]] =
    val zonePathParam = PathSerializable.serialize("zone", zone, PathStyleFormat.SIMPLE, false)
    val serverIdPathParam = PathSerializable.serialize("server_id", serverId, PathStyleFormat.SIMPLE, false)
    val requestURL =
      uri"$baseUrl/instance/v1/zones/${zonePathParam}/servers/${serverIdPathParam}"

    basicRequest
      .method(Method.GET, requestURL)
      .contentType("application/json")
      .auth(authConfig, scaleway.instance.ApiKeyLocation.HEADER, "X-Auth-Token")
      .response(asJson[GetServerResponse])

  /**
   * Get compatible commercial types that can be used to update the Instance. The compatibility of an Instance offer is based on: * the CPU architecture * the OS type * the required l_ssd storage size * the required scratch storage size If the specified Instance offer is flagged as end of service, the best compatible offer is the first returned.
   * 
   * Expected answers:
   *   code 200 : ServerCompatibleTypes ()
   * 
   * Available security schemes:
   *   scaleway (apiKey)
   * 
   * @param zone The zone you want to target
   * @param serverId UUID of the Instance you want to get.
   */
  def getServerCompatibleTypes(zone: String, serverId: String)(using Auth <:< scaleway.instance.Authorization.ApiKey): sttp.client4.Request[Either[ResponseException[String], ServerCompatibleTypes]] =
    val zonePathParam = PathSerializable.serialize("zone", zone, PathStyleFormat.SIMPLE, false)
    val serverIdPathParam = PathSerializable.serialize("server_id", serverId, PathStyleFormat.SIMPLE, false)
    val requestURL =
      uri"$baseUrl/instance/v1/zones/${zonePathParam}/servers/${serverIdPathParam}/compatible-types"

    basicRequest
      .method(Method.GET, requestURL)
      .contentType("application/json")
      .auth(authConfig, scaleway.instance.ApiKeyLocation.HEADER, "X-Auth-Token")
      .response(asJson[ServerCompatibleTypes])

  /**
   * List all actions (e.g. power on, power off, reboot) that can currently be performed on an Instance.
   * 
   * Expected answers:
   *   code 200 : ListServerActionsResponse ()
   * 
   * Available security schemes:
   *   scaleway (apiKey)
   * 
   * @param zone The zone you want to target
   * @param serverId 
   */
  def listServerActions(zone: String, serverId: String)(using Auth <:< scaleway.instance.Authorization.ApiKey): sttp.client4.Request[Either[ResponseException[String], ListServerActionsResponse]] =
    val zonePathParam = PathSerializable.serialize("zone", zone, PathStyleFormat.SIMPLE, false)
    val serverIdPathParam = PathSerializable.serialize("server_id", serverId, PathStyleFormat.SIMPLE, false)
    val requestURL =
      uri"$baseUrl/instance/v1/zones/${zonePathParam}/servers/${serverIdPathParam}/action"

    basicRequest
      .method(Method.GET, requestURL)
      .contentType("application/json")
      .auth(authConfig, scaleway.instance.ApiKeyLocation.HEADER, "X-Auth-Token")
      .response(asJson[ListServerActionsResponse])

  /**
   * List all Instances in a specified Availability Zone, e.g. `fr-par-1`.
   * 
   * Expected answers:
   *   code 200 : ListServersResponse ()
   * 
   * Available security schemes:
   *   scaleway (apiKey)
   * 
   * @param zone The zone you want to target
   * @param perPage A positive integer lower or equal to 100 to select the number of items to return.
   * @param page A positive integer to choose the page to return.
   * @param organization List only Instances of this Organization ID.
   * @param project List only Instances of this Project ID.
   * @param name Filter Instances by name (eg. \"server1\" will return \"server100\" and \"server1\" but not \"foo\").
   * @param privateIp List Instances by private_ip. (IP address)
   * @param withoutIp List Instances that are not attached to a public IP.
   * @param withIp List Instances by IP (both private_ip and public_ip are supported). (IP address)
   * @param commercialType List Instances of this commercial type.
   * @param state List Instances in this state.
   * @param tags List Instances with these exact tags (to filter with several tags, use commas to separate them).
   * @param privateNetwork List Instances in this Private Network.
   * @param order Define the order of the returned servers.
   * @param privateNetworks List Instances from the given Private Networks (use commas to separate them).
   * @param privateNicMacAddress List Instances associated with the given private NIC MAC address.
   * @param servers List Instances from these server ids (use commas to separate them).
   */
  def listServers(zone: String, perPage: Option[Int] = scala.None, page: Option[Int] = scala.None, organization: Option[String] = scala.None, project: Option[String] = scala.None, name: Option[String] = scala.None, privateIp: Option[String] = scala.None, withoutIp: Option[Boolean] = scala.None, withIp: Option[String] = scala.None, commercialType: Option[String] = scala.None, state: Option[String] = scala.None, tags: Option[String] = scala.None, privateNetwork: Option[String] = scala.None, order: Option[String] = scala.None, privateNetworks: Option[String] = scala.None, privateNicMacAddress: Option[String] = scala.None, servers: Option[String] = scala.None)(using Auth <:< scaleway.instance.Authorization.ApiKey): sttp.client4.Request[Either[ResponseException[String], ListServersResponse]] =
    val zonePathParam = PathSerializable.serialize("zone", zone, PathStyleFormat.SIMPLE, false)
    val requestURL =
      uri"$baseUrl/instance/v1/zones/${zonePathParam}/servers"
        .addParams(FormSerializable.serialize("per_page", perPage, FormStyleFormat.FORM, true): _*)
        .addParams(FormSerializable.serialize("page", page, FormStyleFormat.FORM, true): _*)
        .addParams(FormSerializable.serialize("organization", organization, FormStyleFormat.FORM, true): _*)
        .addParams(FormSerializable.serialize("project", project, FormStyleFormat.FORM, true): _*)
        .addParams(FormSerializable.serialize("name", name, FormStyleFormat.FORM, true): _*)
        .addParams(FormSerializable.serialize("private_ip", privateIp, FormStyleFormat.FORM, true): _*)
        .addParams(FormSerializable.serialize("without_ip", withoutIp, FormStyleFormat.FORM, true): _*)
        .addParams(FormSerializable.serialize("with_ip", withIp, FormStyleFormat.FORM, true): _*)
        .addParams(FormSerializable.serialize("commercial_type", commercialType, FormStyleFormat.FORM, true): _*)
        .addParams(FormSerializable.serialize("state", state, FormStyleFormat.FORM, true): _*)
        .addParams(FormSerializable.serialize("tags", tags, FormStyleFormat.FORM, true): _*)
        .addParams(FormSerializable.serialize("private_network", privateNetwork, FormStyleFormat.FORM, true): _*)
        .addParams(FormSerializable.serialize("order", order, FormStyleFormat.FORM, true): _*)
        .addParams(FormSerializable.serialize("private_networks", privateNetworks, FormStyleFormat.FORM, true): _*)
        .addParams(FormSerializable.serialize("private_nic_mac_address", privateNicMacAddress, FormStyleFormat.FORM, true): _*)
        .addParams(FormSerializable.serialize("servers", servers, FormStyleFormat.FORM, true): _*)

    basicRequest
      .method(Method.GET, requestURL)
      .contentType("application/json")
      .auth(authConfig, scaleway.instance.ApiKeyLocation.HEADER, "X-Auth-Token")
      .response(asJson[ListServersResponse])

  /**
   * Perform an action on an Instance. Available actions are: * `poweron`: Start a stopped Instance. * `poweroff`: Fully stop the Instance and release the hypervisor slot. * `stop_in_place`: Stop the Instance, but keep the slot on the hypervisor. * `reboot`: Stop the instance and restart it. * `backup`: Create an image with all the volumes of an Instance. * `terminate`: Delete the Instance along with its attached local volumes. * `enable_routed_ip`: Migrate the Instance to the new network stack.  The `terminate` action will result in the deletion of `l_ssd` and `scratch` volumes types, `sbs_volume` volumes will only be detached. If you want to preserve your `l_ssd` volumes, you should stop your Instance, detach the volumes to be preserved, then delete your Instance.  The `backup` action can be done with: * No `volumes` key in the body: an image is created with snapshots of all the server volumes, except for the `scratch` volumes types. * `volumes` key in the body with a dictionary as value, in this dictionary volumes UUID as keys and empty dictionaries as values : an image is created with the snapshots of the volumes in `volumes` key. `scratch` volumes types can't be shapshotted.
   * 
   * Expected answers:
   *   code 200 : ServerActionResponse ()
   * 
   * Available security schemes:
   *   scaleway (apiKey)
   * 
   * @param zone The zone you want to target
   * @param serverId UUID of the Instance.
   * @param serverActionRequest 
   */
  def serverAction(zone: String, serverId: String, serverActionRequest: ServerActionRequest)(using Auth <:< scaleway.instance.Authorization.ApiKey): sttp.client4.Request[Either[ResponseException[String], ServerActionResponse]] =
    val zonePathParam = PathSerializable.serialize("zone", zone, PathStyleFormat.SIMPLE, false)
    val serverIdPathParam = PathSerializable.serialize("server_id", serverId, PathStyleFormat.SIMPLE, false)
    val requestURL =
      uri"$baseUrl/instance/v1/zones/${zonePathParam}/servers/${serverIdPathParam}/action"

    basicRequest
      .method(Method.POST, requestURL)
      .contentType("application/json")
      .auth(authConfig, scaleway.instance.ApiKeyLocation.HEADER, "X-Auth-Token")
      .body(asJson(serverActionRequest))
      .response(asJson[ServerActionResponse])

  /**
   * Update the Instance information, such as name, boot mode, or tags.
   * 
   * Expected answers:
   *   code 200 : UpdateServerResponse ()
   * 
   * Available security schemes:
   *   scaleway (apiKey)
   * 
   * @param zone The zone you want to target
   * @param serverId UUID of the Instance.
   * @param updateServerRequest 
   */
  def updateServer(zone: String, serverId: String, updateServerRequest: UpdateServerRequest)(using Auth <:< scaleway.instance.Authorization.ApiKey): sttp.client4.Request[Either[ResponseException[String], UpdateServerResponse]] =
    val zonePathParam = PathSerializable.serialize("zone", zone, PathStyleFormat.SIMPLE, false)
    val serverIdPathParam = PathSerializable.serialize("server_id", serverId, PathStyleFormat.SIMPLE, false)
    val requestURL =
      uri"$baseUrl/instance/v1/zones/${zonePathParam}/servers/${serverIdPathParam}"

    basicRequest
      .method(Method.PATCH, requestURL)
      .contentType("application/json")
      .auth(authConfig, scaleway.instance.ApiKeyLocation.HEADER, "X-Auth-Token")
      .body(asJson(updateServerRequest))
      .response(asJson[UpdateServerResponse])

end InstancesApi