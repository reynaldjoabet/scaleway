/** IPAM API IPAM is an **IP** **A**ddress **M**anagement tool for Scaleway's ecosystem. It acts as a single source of
  * truth for the IP addresses of Scaleway resources. You can use the IPAM API to list public IP addresses for products
  * such as Instances, Databases and Load Balancer which are already using [IP
  * mobility](https://www.scaleway.com/en/blog/ip-mobility-removing-nat/). More products will be integrated in the
  * future. Scaleway's IPAM also powers the addressing of resources of all resources in a VPC, simplifying management of
  * IP allocations, conflicts and lifecycle. Private Networks' DHCP server hands out the static private IP addresses
  * assigned by IPAM to resources attached to Private Networks. IPAM allows you to reserve IPs, to ensure they won't be
  * allocated to some other resource. You can use these reserved IPs directly on custom environments (e.g. VMs on an
  * Elastic Metal server). In the future, you will be also be able to specify a reserved IP to use when attaching a
  * resource to a Private Network. In the meantime, when you attach a resource to a Private Network, its IP address is
  * assigned by IPAM and then automatically attached by the product/resource. IPAM gives you a number of guarantees
  * about IPs: - once reserved, the IP will never change - once attached to a resource, no other resource will be able
  * to use the IP, ensuring unicity - once attached to a resource, the IP cannot be released unless detached from the
  * resource ## Concepts ### Resource An IPAM IP can be attached to a resource, which is identified by its type (e.g. an
  * Instance Private NIC) and its UUID. Once attached, most operations on the IP are not available anymore: it must
  * first be detached. Once an IP is attached to a resource, it is not possible anymore to: - release the IP - detach
  * the IP (without detaching the resource from the Private Network first) When an IP is attached to a resource, only
  * the product managing the resource can detach the IP, by detaching the resource from the Private Network. This
  * ensures that IPs cannot be reused and avoid mistakes. ### Source An IP can be reserved in several different sources,
  * which can be understood as distinct pools of available IP. Sources can be either **global** like the `zonal` source
  * which is shared across all users, or **specific**, like the `private_network` source, that is identified by the
  * Private Network ID, and will hand out IPs from a specific Private Network. ## Quickstart **Requirements**: - You
  * have a [Scaleway account](https://console.scaleway.com/) - You have created an [API
  * key](https://www.scaleway.com/en/docs/iam/how-to/create-api-keys/) and that the API key has sufficient [IAM
  * permissions](https://www.scaleway.com/en/docs/iam/reference-content/permission-sets/) to perform the actions
  * described on this page - You have [installed `curl`](https://curl.se/download.html) 1. Configure your environment
  * variables. <Message type=\"note\"> This is an optional step that seeks to simplify your usage of the IPAM API.
  * </Message> ```bash     export SCW_SECRET_KEY=\"<API secret key>\"     export SCW_DEFAULT_REGION=\"<Scaleway default Region>\"     export SCW_PROJECT_ID=\"<Scaleway Project ID>\"     export SCW_PRIVATE_NETWORK_ID=\"<Scaleway Private Network ID>\"     ``` 2.
  * Create an IP: run the following command to create an IP in a specified Private Network. You can customize the tags
  * as you wish. The IP will not be attached to any resource. ```bash     curl -X POST \\       -H \"X-Auth-Token: $SCW_SECRET_KEY\" \\       \"https://api.scaleway.com/ipam/v1/regions/$SCW_DEFAULT_REGION/ips\" \\       --json '{         \"project_id\": \"'\"$SCW_PROJECT_ID\"'\",         \"source\": {\"private_network_id\": \"'\"$SCW_PRIVATE_NETWORK_ID\"'\"},         \"tags\": [\"test\", \"another tag\"]       }'     ``` 3.
  * **Get a list of your IPs**: run the following command to get a list of all the IPs in your account, with their
  * details: ```bash     curl -X GET \\       -H \"X-Auth-Token: $SCW_SECRET_KEY\" \\       \"https://api.scaleway.com/ipam/v1/regions/$SCW_DEFAULT_REGION/ips\"     ``` 4.
  * **Get a list of your IPs in a Private Network**: run the following command to get a list of all the IPs in a Private
  * Network, with their details: ```bash     curl -X GET \\       -H \"X-Auth-Token: $SCW_SECRET_KEY\" \\       \"https://api.scaleway.com/ipam/v1/regions/$SCW_DEFAULT_REGION/ips?private_network_id=$SCW_PRIVATE_NETWORK_ID\"     ``` 5.
  * **Release an IP**: run the following command to delete an IP, if it is not attached to a resource. Ensure that you
  * replace `<IP-ID>` in the URL with the ID of the IP you want to release. ```bash     curl -X DELETE \\       -H \"X-Auth-Token: $SCW_SECRET_KEY\" \\       \"https://api.scaleway.com/ipam/v1/regions/$SCW_DEFAULT_REGION/ips/<IP-ID>\"     ```
  * <Message type=\"requirement\"> - You have a [Scaleway account](https://console.scaleway.com/) - You have created an
  * [API key](https://www.scaleway.com/en/docs/iam/how-to/create-api-keys/) and that the API key has sufficient [IAM
  * permissions](https://www.scaleway.com/en/docs/iam/reference-content/permission-sets/) to perform the actions
  * described on this page - You have [installed `curl`](https://curl.se/download.html) </Message> ## Technical
  * limitations Scaleway's IPAM is a new tool. Not all Scaleway products are currently integrated in terms of public IP
  * addressing, and not all functionalities are currently publicly available for the management of private IPs in a VPC
  * (for example, not all products support specifying a reserved IP when attaching a resource to a Private Network).
  * More features and functionalities will be added in due course. In the meantime, [read our blog
  * post](https://www.scaleway.com/en/blog/ip-mobility-removing-nat/) to find out more about how we are tackling
  * technical debt with IP mobility. ## Going further For more help using Scaleway IPAM and network products, check out
  * the following resources: - Our [VPC documentation](https://www.scaleway.com/en/docs/vpc) - The
  * #virtual-private-cloud channel on our [Slack
  * Community](https://www.scaleway.com/en/docs/tutorials/scaleway-slack-community/) - Our [support ticketing
  * system](https://www.scaleway.com/en/docs/account/how-to/open-a-support-ticket/).
  *
  * The version of the OpenAPI document: v1
  *
  * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
  * https://openapi-generator.tech Do not edit the class manually.
  */
package scaleway.ipam.api

import scaleway.ipam.models.AttachIPRequest
import scaleway.ipam.models.BookIPRequest
import scaleway.ipam.models.IP
import scaleway.ipam.models.ListIPsResponse
import scaleway.ipam.models.ModelType.*
import scaleway.ipam.models.ModelType
import scaleway.ipam.models.MoveIPRequest
import scaleway.ipam.models.UpdateIPRequest
import com.github.plokhotnyuk.jsoniter_scala.circe.JsoniterScalaCodec.*
import scaleway.ipam.JsonSupport.{*, given}
import scaleway.ipam.FormSerializable
import scaleway.ipam.FormStyleFormat
import scaleway.ipam.HeaderSerializable
import scaleway.ipam.ApiKeyLocation
import scaleway.ipam.PathStyleFormat
import scaleway.ipam.PathSerializable
import scaleway.ipam.CookieSerializable
import scaleway.ipam.Helpers.*
import sttp.client4.jsoniter.*
import sttp.client4.*
import sttp.model.Method

object IPs:
  def apply(baseUrl: String = "https://api.scaleway.com"): IPs[scaleway.ipam.Authorization.NoAuthorization.type] =
    IPs(baseUrl, scaleway.ipam.Authorization.NoAuthorization)
  def withBasicAuth(baseUrl: String, username: String, password: String): IPs[scaleway.ipam.Authorization.BasicAuth] =
    IPs(baseUrl, scaleway.ipam.Authorization.BasicAuth(username, password))

  def withApiKeyAuth(baseUrl: String, apiKey: String): IPs[scaleway.ipam.Authorization.ApiKey] =
    IPs(baseUrl, scaleway.ipam.Authorization.ApiKey(apiKey))

  def withBearerTokenAuth(baseUrl: String, token: String): IPs[scaleway.ipam.Authorization.BearerToken] =
    IPs(baseUrl, scaleway.ipam.Authorization.BearerToken(token))

case class IPs[Auth <: scaleway.ipam.Authorization] private (baseUrl: String, authConfig: scaleway.ipam.Authorization):
  def withBasicAuth(username: String, password: String): IPs[scaleway.ipam.Authorization.BasicAuth] =
    copy(authConfig = scaleway.ipam.Authorization.BasicAuth(username, password))

  def withApiKeyAuth(apiKey: String): IPs[scaleway.ipam.Authorization.ApiKey] =
    copy(authConfig = scaleway.ipam.Authorization.ApiKey(apiKey))

  def withNoAuth: IPs[scaleway.ipam.Authorization.NoAuthorization.type] =
    copy(authConfig = scaleway.ipam.Authorization.NoAuthorization)

  def withBearerTokenAuth(token: String): IPs[scaleway.ipam.Authorization.BearerToken] =
    copy(authConfig = scaleway.ipam.Authorization.BearerToken(token))

  /** Attach an existing reserved private IP from a Private Network subnet to a custom, named resource via its MAC
    * address. An example of a custom resource is a virtual machine hosted on an Elastic Metal server. Do not use this
    * method for attaching IP addresses to standard Scaleway resources as it will fail - see the relevant product API
    * for an equivalent method.
    *
    * Expected answers: code 200 : IP ()
    *
    * Available security schemes: scaleway (apiKey)
    *
    * @param region
    *   The region you want to target
    * @param ipId
    *   IP ID. (UUID format)
    * @param attachIPRequest
    */
  def attachIP(region: String, ipId: String, attachIPRequest: AttachIPRequest)(using
      Auth <:< scaleway.ipam.Authorization.ApiKey
  ): sttp.client4.Request[Either[ResponseException[String], IP]] =
    val regionPathParam = PathSerializable.serialize("region", region, PathStyleFormat.SIMPLE, false)
    val ipIdPathParam = PathSerializable.serialize("ip_id", ipId, PathStyleFormat.SIMPLE, false)
    val requestURL =
      uri"$baseUrl/ipam/v1/regions/${regionPathParam}/ips/${ipIdPathParam}/attach"

    basicRequest
      .method(Method.POST, requestURL)
      .contentType("application/json")
      .auth(authConfig, scaleway.ipam.ApiKeyLocation.HEADER, "X-Auth-Token")
      .body(asJson(attachIPRequest))
      .response(asJson[IP])

  /** Reserve a new IP from the specified source. Currently IPs can only be reserved from a Private Network.
    *
    * Expected answers: code 200 : IP ()
    *
    * Available security schemes: scaleway (apiKey)
    *
    * @param region
    *   The region you want to target
    * @param bookIPRequest
    */
  def bookIP(region: String, bookIPRequest: BookIPRequest)(using
      Auth <:< scaleway.ipam.Authorization.ApiKey
  ): sttp.client4.Request[Either[ResponseException[String], IP]] =
    val regionPathParam = PathSerializable.serialize("region", region, PathStyleFormat.SIMPLE, false)
    val requestURL =
      uri"$baseUrl/ipam/v1/regions/${regionPathParam}/ips"

    basicRequest
      .method(Method.POST, requestURL)
      .contentType("application/json")
      .auth(authConfig, scaleway.ipam.ApiKeyLocation.HEADER, "X-Auth-Token")
      .body(asJson(bookIPRequest))
      .response(asJson[IP])

  /** Detach a private IP from a custom resource. An example of a custom resource is a virtual machine hosted on an
    * Elastic Metal server. Do not use this method for detaching IP addresses from standard Scaleway resources (e.g.
    * Instances, Load Balancers) as it will fail - see the relevant product API for an equivalent method.
    *
    * Expected answers: code 200 : IP ()
    *
    * Available security schemes: scaleway (apiKey)
    *
    * @param region
    *   The region you want to target
    * @param ipId
    *   IP ID. (UUID format)
    * @param attachIPRequest
    */
  def detachIP(region: String, ipId: String, attachIPRequest: AttachIPRequest)(using
      Auth <:< scaleway.ipam.Authorization.ApiKey
  ): sttp.client4.Request[Either[ResponseException[String], IP]] =
    val regionPathParam = PathSerializable.serialize("region", region, PathStyleFormat.SIMPLE, false)
    val ipIdPathParam = PathSerializable.serialize("ip_id", ipId, PathStyleFormat.SIMPLE, false)
    val requestURL =
      uri"$baseUrl/ipam/v1/regions/${regionPathParam}/ips/${ipIdPathParam}/detach"

    basicRequest
      .method(Method.POST, requestURL)
      .contentType("application/json")
      .auth(authConfig, scaleway.ipam.ApiKeyLocation.HEADER, "X-Auth-Token")
      .body(asJson(attachIPRequest))
      .response(asJson[IP])

  /** Retrieve details of an existing IP, specified by its IP ID.
    *
    * Expected answers: code 200 : IP ()
    *
    * Available security schemes: scaleway (apiKey)
    *
    * @param region
    *   The region you want to target
    * @param ipId
    *   IP ID. (UUID format)
    */
  def getIP(region: String, ipId: String)(using
      Auth <:< scaleway.ipam.Authorization.ApiKey
  ): sttp.client4.Request[Either[ResponseException[String], IP]] =
    val regionPathParam = PathSerializable.serialize("region", region, PathStyleFormat.SIMPLE, false)
    val ipIdPathParam = PathSerializable.serialize("ip_id", ipId, PathStyleFormat.SIMPLE, false)
    val requestURL =
      uri"$baseUrl/ipam/v1/regions/${regionPathParam}/ips/${ipIdPathParam}"

    basicRequest
      .method(Method.GET, requestURL)
      .contentType("application/json")
      .auth(authConfig, scaleway.ipam.ApiKeyLocation.HEADER, "X-Auth-Token")
      .response(asJson[IP])

  /** List existing IPs in the specified region using various filters. For example, you can filter for IPs within a
    * specified Private Network, or for public IPs within a specified Project. By default, the IPs returned in the list
    * are ordered by creation date in ascending order, though this can be modified via the order_by field.
    *
    * Expected answers: code 200 : ListIPsResponse ()
    *
    * Available security schemes: scaleway (apiKey)
    *
    * @param region
    *   The region you want to target
    * @param orderBy
    *   Sort order of the returned IPs.
    * @param page
    *   Page number to return, from the paginated results.
    * @param pageSize
    *   Maximum number of IPs to return per page.
    * @param projectId
    *   Project ID to filter for. Only IPs belonging to this Project will be returned. (UUID format)
    * @param vpcId
    *   VPC ID to filter for. Only IPs owned by resources in this VPC will be returned.
    * @param attached
    *   Defines whether to filter only for IPs which are attached to a resource.
    * @param resourceName
    *   Attached resource name to filter for, only IPs attached to a resource with this string within their name will be
    *   returned.
    * @param resourceId
    *   Resource ID to filter for. Only IPs attached to this resource will be returned. (UUID format)
    * @param resourceIds
    *   Resource IDs to filter for. Only IPs attached to at least one of these resources will be returned.
    * @param resourceType
    *   Resource type to filter for. Only IPs attached to this type of resource will be returned.
    * @param resourceTypes
    *   Resource types to filter for. Only IPs attached to these types of resources will be returned.
    * @param macAddress
    *   MAC address to filter for. Only IPs attached to a resource with this MAC address will be returned.
    * @param tags
    *   Tags to filter for, only IPs with one or more matching tags will be returned.
    * @param organizationId
    *   Organization ID to filter for. Only IPs belonging to this Organization will be returned. (UUID format)
    * @param isIpv6
    *   Defines whether to filter only for IPv4s or IPv6s.
    * @param ipIds
    *   IP IDs to filter for. Only IPs with these UUIDs will be returned.
    */
  def listIPs(
      region: String,
      orderBy: Option[String] = scala.None,
      page: Option[Int] = scala.None,
      pageSize: Option[Int] = scala.None,
      projectId: Option[String] = scala.None,
      vpcId: Option[String] = scala.None,
      attached: Option[Boolean] = scala.None,
      resourceName: Option[String] = scala.None,
      resourceId: Option[String] = scala.None,
      resourceIds: Seq[String],
      resourceType: Option[String] = scala.None,
      resourceTypes: Seq[ModelType],
      macAddress: Option[String] = scala.None,
      tags: Seq[String],
      organizationId: Option[String] = scala.None,
      isIpv6: Option[Boolean] = scala.None,
      ipIds: Seq[String]
  )(using
      Auth <:< scaleway.ipam.Authorization.ApiKey
  ): sttp.client4.Request[Either[ResponseException[String], ListIPsResponse]] =
    val regionPathParam = PathSerializable.serialize("region", region, PathStyleFormat.SIMPLE, false)
    val requestURL =
      uri"$baseUrl/ipam/v1/regions/${regionPathParam}/ips"
        .addParams(FormSerializable.serialize("order_by", orderBy, FormStyleFormat.FORM, true): _*)
        .addParams(FormSerializable.serialize("page", page, FormStyleFormat.FORM, true): _*)
        .addParams(FormSerializable.serialize("page_size", pageSize, FormStyleFormat.FORM, true): _*)
        .addParams(FormSerializable.serialize("project_id", projectId, FormStyleFormat.FORM, true): _*)
        .addParams(FormSerializable.serialize("vpc_id", vpcId, FormStyleFormat.FORM, true): _*)
        .addParams(FormSerializable.serialize("attached", attached, FormStyleFormat.FORM, true): _*)
        .addParams(FormSerializable.serialize("resource_name", resourceName, FormStyleFormat.FORM, true): _*)
        .addParams(FormSerializable.serialize("resource_id", resourceId, FormStyleFormat.FORM, true): _*)
        .addParams(FormSerializable.serialize("resource_ids", resourceIds, FormStyleFormat.FORM, true): _*)
        .addParams(FormSerializable.serialize("resource_type", resourceType, FormStyleFormat.FORM, true): _*)
        .addParams(FormSerializable.serialize("resource_types", resourceTypes, FormStyleFormat.FORM, true): _*)
        .addParams(FormSerializable.serialize("mac_address", macAddress, FormStyleFormat.FORM, true): _*)
        .addParams(FormSerializable.serialize("tags", tags, FormStyleFormat.FORM, true): _*)
        .addParams(FormSerializable.serialize("organization_id", organizationId, FormStyleFormat.FORM, true): _*)
        .addParams(FormSerializable.serialize("is_ipv6", isIpv6, FormStyleFormat.FORM, true): _*)
        .addParams(FormSerializable.serialize("ip_ids", ipIds, FormStyleFormat.FORM, true): _*)

    basicRequest
      .method(Method.GET, requestURL)
      .contentType("application/json")
      .auth(authConfig, scaleway.ipam.ApiKeyLocation.HEADER, "X-Auth-Token")
      .response(asJson[ListIPsResponse])

  /** Move an existing reserved private IP from one custom resource (e.g. a virtual machine hosted on an Elastic Metal
    * server) to another custom resource. This will detach it from the first resource, and attach it to the second. Do
    * not use this method for moving IP addresses between standard Scaleway resources (e.g. Instances, Load Balancers)
    * as it will fail - see the relevant product API for an equivalent method.
    *
    * Expected answers: code 200 : IP ()
    *
    * Available security schemes: scaleway (apiKey)
    *
    * @param region
    *   The region you want to target
    * @param ipId
    *   IP ID. (UUID format)
    * @param moveIPRequest
    */
  def moveIP(region: String, ipId: String, moveIPRequest: MoveIPRequest)(using
      Auth <:< scaleway.ipam.Authorization.ApiKey
  ): sttp.client4.Request[Either[ResponseException[String], IP]] =
    val regionPathParam = PathSerializable.serialize("region", region, PathStyleFormat.SIMPLE, false)
    val ipIdPathParam = PathSerializable.serialize("ip_id", ipId, PathStyleFormat.SIMPLE, false)
    val requestURL =
      uri"$baseUrl/ipam/v1/regions/${regionPathParam}/ips/${ipIdPathParam}/move"

    basicRequest
      .method(Method.POST, requestURL)
      .contentType("application/json")
      .auth(authConfig, scaleway.ipam.ApiKeyLocation.HEADER, "X-Auth-Token")
      .body(asJson(moveIPRequest))
      .response(asJson[IP])

  /** Release an IP not currently attached to a resource, and returns it to the available IP pool.
    *
    * Expected answers: code 204 : ()
    *
    * Available security schemes: scaleway (apiKey)
    *
    * @param region
    *   The region you want to target
    * @param ipId
    *   IP ID. (UUID format)
    * @param body
    */
  def releaseIP(region: String, ipId: String, body: io.circe.Json)(using
      Auth <:< scaleway.ipam.Authorization.ApiKey
  ): sttp.client4.Request[Either[ResponseException[String], Unit]] =
    val regionPathParam = PathSerializable.serialize("region", region, PathStyleFormat.SIMPLE, false)
    val ipIdPathParam = PathSerializable.serialize("ip_id", ipId, PathStyleFormat.SIMPLE, false)
    val requestURL =
      uri"$baseUrl/ipam/v1/regions/${regionPathParam}/ips/${ipIdPathParam}"

    basicRequest
      .method(Method.DELETE, requestURL)
      .contentType("application/json")
      .auth(authConfig, scaleway.ipam.ApiKeyLocation.HEADER, "X-Auth-Token")
      .body(asJson(body))
      .response(asString.mapWithMetadata(ResponseAs.deserializeRightWithError(_ => Right(()))))

  /** Update parameters including tags of the specified IP.
    *
    * Expected answers: code 200 : IP ()
    *
    * Available security schemes: scaleway (apiKey)
    *
    * @param region
    *   The region you want to target
    * @param ipId
    *   IP ID. (UUID format)
    * @param updateIPRequest
    */
  def updateIP(region: String, ipId: String, updateIPRequest: UpdateIPRequest)(using
      Auth <:< scaleway.ipam.Authorization.ApiKey
  ): sttp.client4.Request[Either[ResponseException[String], IP]] =
    val regionPathParam = PathSerializable.serialize("region", region, PathStyleFormat.SIMPLE, false)
    val ipIdPathParam = PathSerializable.serialize("ip_id", ipId, PathStyleFormat.SIMPLE, false)
    val requestURL =
      uri"$baseUrl/ipam/v1/regions/${regionPathParam}/ips/${ipIdPathParam}"

    basicRequest
      .method(Method.PATCH, requestURL)
      .contentType("application/json")
      .auth(authConfig, scaleway.ipam.ApiKeyLocation.HEADER, "X-Auth-Token")
      .body(asJson(updateIPRequest))
      .response(asJson[IP])

end IPs
