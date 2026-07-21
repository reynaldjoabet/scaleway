/**
 * IPAM API
 * IPAM is an **IP** **A**ddress **M**anagement tool for Scaleway's ecosystem.  It acts as a single source of truth for the IP addresses of Scaleway resources. You can use the IPAM API to list public IP addresses for products such as Instances, Databases and Load Balancer which are already using [IP mobility](https://www.scaleway.com/en/blog/ip-mobility-removing-nat/). More products will be integrated in the future.  Scaleway's IPAM also powers the addressing of resources of all resources in a VPC, simplifying management of IP allocations, conflicts and lifecycle. Private Networks' DHCP server hands out the static private IP addresses assigned by IPAM to resources attached to Private Networks.  IPAM allows you to reserve IPs, to ensure they won't be allocated to some other resource. You can use these reserved IPs directly on custom environments (e.g. VMs on an Elastic Metal server). In the future, you will be also be able to specify a reserved IP to use when attaching a resource to a Private Network. In the meantime, when you attach a resource to a Private Network, its IP address is assigned by IPAM and then automatically attached by the product/resource.  IPAM gives you a number of guarantees about IPs:  - once reserved, the IP will never change - once attached to a resource, no other resource will be able to use the IP, ensuring unicity - once attached to a resource, the IP cannot be released unless detached from the resource     ## Concepts  ### Resource  An IPAM IP can be attached to a resource, which is identified by its type (e.g. an Instance Private NIC) and its UUID. Once attached, most operations on the IP are not available anymore: it must first be detached.  Once an IP is attached to a resource, it is not possible anymore to: - release the IP - detach the IP (without detaching the resource from the Private Network first)  When an IP is attached to a resource, only the product managing the resource can detach the IP, by detaching the resource from the Private Network. This ensures that IPs cannot be reused and avoid mistakes.  ### Source  An IP can be reserved in several different sources, which can be understood as distinct pools of available IP.  Sources can be either **global** like the `zonal` source which is shared across all users, or **specific**, like the `private_network` source, that is identified by the Private Network ID, and will hand out IPs from a specific Private Network.  ## Quickstart  **Requirements**: - You have a [Scaleway account](https://console.scaleway.com/) - You have created an [API key](https://www.scaleway.com/en/docs/iam/how-to/create-api-keys/) and that the API key has sufficient [IAM permissions](https://www.scaleway.com/en/docs/iam/reference-content/permission-sets/) to perform the actions described on this page - You have [installed `curl`](https://curl.se/download.html)  1. Configure your environment variables.      <Message type=\"note\">     This is an optional step that seeks to simplify your usage of the IPAM API.     </Message>      ```bash     export SCW_SECRET_KEY=\"<API secret key>\"     export SCW_DEFAULT_REGION=\"<Scaleway default Region>\"     export SCW_PROJECT_ID=\"<Scaleway Project ID>\"     export SCW_PRIVATE_NETWORK_ID=\"<Scaleway Private Network ID>\"     ```  2. Create an IP: run the following command to create an IP in a specified Private Network. You can customize the tags as you wish. The IP will not be attached to any resource.      ```bash     curl -X POST \\       -H \"X-Auth-Token: $SCW_SECRET_KEY\" \\       \"https://api.scaleway.com/ipam/v1/regions/$SCW_DEFAULT_REGION/ips\" \\       --json '{         \"project_id\": \"'\"$SCW_PROJECT_ID\"'\",         \"source\": {\"private_network_id\": \"'\"$SCW_PRIVATE_NETWORK_ID\"'\"},         \"tags\": [\"test\", \"another tag\"]       }'     ```  3. **Get a list of your IPs**: run the following command to get a list of all the IPs in your account, with their details:      ```bash     curl -X GET \\       -H \"X-Auth-Token: $SCW_SECRET_KEY\" \\       \"https://api.scaleway.com/ipam/v1/regions/$SCW_DEFAULT_REGION/ips\"     ```  4. **Get a list of your IPs in a Private Network**: run the following command to get a list of all the IPs in a Private Network, with their details:      ```bash     curl -X GET \\       -H \"X-Auth-Token: $SCW_SECRET_KEY\" \\       \"https://api.scaleway.com/ipam/v1/regions/$SCW_DEFAULT_REGION/ips?private_network_id=$SCW_PRIVATE_NETWORK_ID\"     ```  5. **Release an IP**: run the following command to delete an IP, if it is not attached to a resource. Ensure that you replace `<IP-ID>` in the URL with the ID of the IP you want to release.      ```bash     curl -X DELETE \\       -H \"X-Auth-Token: $SCW_SECRET_KEY\" \\       \"https://api.scaleway.com/ipam/v1/regions/$SCW_DEFAULT_REGION/ips/<IP-ID>\"     ```   <Message type=\"requirement\"> - You have a [Scaleway account](https://console.scaleway.com/) - You have created an [API key](https://www.scaleway.com/en/docs/iam/how-to/create-api-keys/) and that the API key has sufficient [IAM permissions](https://www.scaleway.com/en/docs/iam/reference-content/permission-sets/) to perform the actions described on this page - You have [installed `curl`](https://curl.se/download.html) </Message>   ## Technical limitations  Scaleway's IPAM is a new tool. Not all Scaleway products are currently integrated in terms of public IP addressing, and not all functionalities are currently publicly available for the management of private IPs in a VPC (for example, not all products support specifying a reserved IP when attaching a resource to a Private Network). More features and functionalities will be added in due course. In the meantime, [read our blog post](https://www.scaleway.com/en/blog/ip-mobility-removing-nat/) to find out more about how we are tackling technical debt with IP mobility.  ## Going further  For more help using Scaleway IPAM and network products, check out the following resources:  - Our [VPC documentation](https://www.scaleway.com/en/docs/vpc) - The #virtual-private-cloud channel on our [Slack Community](https://www.scaleway.com/en/docs/tutorials/scaleway-slack-community/) - Our [support ticketing system](https://www.scaleway.com/en/docs/account/how-to/open-a-support-ticket/).
 *
 * The version of the OpenAPI document: v1
 * 
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */
package scaleway.ipam.api

import scaleway.ipam.models.ReleaseIPSetRequest
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

object Default:
  def apply(baseUrl: String = "https://api.scaleway.com"): Default[scaleway.ipam.Authorization.NoAuthorization.type] = Default(baseUrl, scaleway.ipam.Authorization.NoAuthorization)
  def withBasicAuth(baseUrl: String, username: String, password: String): Default[scaleway.ipam.Authorization.BasicAuth] =
    Default(baseUrl, scaleway.ipam.Authorization.BasicAuth(username, password))

  def withApiKeyAuth(baseUrl: String, apiKey: String): Default[scaleway.ipam.Authorization.ApiKey] =
    Default(baseUrl, scaleway.ipam.Authorization.ApiKey(apiKey))

  def withBearerTokenAuth(baseUrl: String, token: String): Default[scaleway.ipam.Authorization.BearerToken] =
    Default(baseUrl, scaleway.ipam.Authorization.BearerToken(token))

case class Default[Auth <: scaleway.ipam.Authorization] private (baseUrl: String, authConfig: scaleway.ipam.Authorization):
  def withBasicAuth(username: String, password: String): Default[scaleway.ipam.Authorization.BasicAuth] =
    copy(authConfig = scaleway.ipam.Authorization.BasicAuth(username, password))

  def withApiKeyAuth(apiKey: String): Default[scaleway.ipam.Authorization.ApiKey] =
    copy(authConfig = scaleway.ipam.Authorization.ApiKey(apiKey))

  def withNoAuth: Default[scaleway.ipam.Authorization.NoAuthorization.type] =
    copy(authConfig = scaleway.ipam.Authorization.NoAuthorization)

  def withBearerTokenAuth(token: String): Default[scaleway.ipam.Authorization.BearerToken] =
    copy(authConfig = scaleway.ipam.Authorization.BearerToken(token))

  /**
   * Expected answers:
   *   code 204 :  ()
   * 
   * Available security schemes:
   *   scaleway (apiKey)
   * 
   * @param region The region you want to target
   * @param releaseIPSetRequest 
   */
  def releaseIPSet(region: String, releaseIPSetRequest: ReleaseIPSetRequest)(using Auth <:< scaleway.ipam.Authorization.ApiKey): sttp.client4.Request[Either[ResponseException[String], Unit]] =
    val regionPathParam = PathSerializable.serialize("region", region, PathStyleFormat.SIMPLE, false)
    val requestURL =
      uri"$baseUrl/ipam/v1/regions/${regionPathParam}/ip-sets/release"

    basicRequest
      .method(Method.POST, requestURL)
      .contentType("application/json")
      .auth(authConfig, scaleway.ipam.ApiKeyLocation.HEADER, "X-Auth-Token")
      .body(asJson(releaseIPSetRequest))
      .response(asString.mapWithMetadata(ResponseAs.deserializeRightWithError(_ => Right(()))))

end Default