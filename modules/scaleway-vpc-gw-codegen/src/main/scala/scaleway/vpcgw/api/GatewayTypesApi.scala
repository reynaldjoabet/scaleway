/**
 * Public Gateways API
 * Scaleway Public Gateways are building blocks for your infrastructure on Scaleway's public cloud. They sit at the border of Private Networks and provide access to/from other networks or the Internet. As well as this, Public Gateways offer a host of managed features and services to facilitate the management of resources in your Private Network, including NAT to map private IP addresses in the Private Network to the public IP address of the Public Gateway.   <Message type=\"tip\"> To create and manage your Private Networks, check out the [VPC API](https://www.scaleway.com/en/developers/api/vpc). </Message>   ## Concepts  Refer to our [dedicated concepts page](https://www.scaleway.com/en/docs/public-gateways/concepts/) to find definitions of all terminology related to Public Gateways, including NAT, SSH bastion and more.     ## Quickstart  1. Configure your environment variables.      <Message type=\"note\">     This is an optional step that seeks to simplify your usage of the Public Gateways API.     </Message>       ```bash     export SCW_SECRET_KEY=\"<API secret key>\"     export SCW_DEFAULT_ZONE=\"<Scaleway default Availability Zone>\"     export SCW_PROJECT_ID=\"<Scaleway Project ID>\"     ```  2. **Choose a Public Gateway type**: Public Gateways come in different shapes and sizes, with different network capabilities and pricing. When you create your Public Gateway, you need to include the required Public Gateway type in the request. Use the following call to get a list of available Public Gateway offer types and their details:      ```bash     curl -X GET \\         -H \"X-Auth-Token: $SCW_SECRET_KEY\" \\         -H \"Content-Type: application/json\" \\         \"https://api.scaleway.com/vpc-gw/v2/zones/$SCW_DEFAULT_ZONE/gateway-types\"        ```  3. **Create a Public Gateway**: run the following command to create a Public Gateway. You can customize the details in the payload (name, description, tags, etc) to your needs: use the information below to adjust the payload as necessary.      ```bash     curl -X POST \\         -H \"X-Auth-Token: $SCW_SECRET_KEY\" \\         -H \"Content-Type: application/json\" \\         \"https://api.scaleway.com/vpc-gw/v2/zones/$SCW_DEFAULT_ZONE/gateways\" \\         -d '{             \"type\": \"VPC-GW-S\",              \"name\": \"my-new-gateway\",             \"tags\": [\"my-first-tag\", \"my-second-tag\"],              \"project_id\": \"'\"$SCW_PROJECT_ID\"'\"             }'     ```      | Parameter       | Description                                         | Valid values                  |      |-----------------|-----------------------------------------------------|-------------------------------|     | type            | The type of Public Gateway (commercial offer type) to create. Use the Gateway Types endpoint to get a list of offer types. | Any valid offer type string, e.g. `VPC-GW-S` |     | name            | A name of your choice for the Public Gateway        | Any string containing only alphanumeric characters and dashes, e.g. `my-new-gateway`. |     | tags            | A list of tags to describe your Public Gateway. These can help you manage and filter your gateways. | A list of alphanumeric strings, e.g. `[\"my-first-tag`, `my-second-tag` |     | project_id      | The Scaleway Project ID to create the Public Gateway in. | A valid Scaleway Project ID, e.g. `f5fe13a0-b9c7-11ed-afa1-0242ac120002` |      **Note**: Further parameters are available, but for the purposes of this quickstart we have included only the essentials. See the `Create a Public Gateway` endpoint documentation below for full details of all possible parameters.  4. **Get a list of your Public Gateways**: run the following command to get a list of all your Public Gateways.      ```bash     curl -X GET \\         -H \"X-Auth-Token: $SCW_SECRET_KEY\" \\         -H \"Content-Type: application/json\" \\         \"https://api.scaleway.com/vpc-gw/v2/zones/$SCW_DEFAULT_ZONE/gateways\"     ```  5. **Attach a Private Network to a Public Gateway**: run the following command to attach a Private Network to your Public Gateway, and make all the Gateway's services such as NAT available to the Private Network. You can customize the details in the payload to your needs: use the information below to adjust the payload as necessary.     <Message type=\"tip\">    If you haven't created a Private Network yet, see the [Private Networks](https://www/scaleway.com/en/developers/api/vpc/) documentation to learn how to do so. Ensure you retain the ID of the Private Network.    </Message>      ```bash     curl -X POST \\         -H \"X-Auth-Token: $SCW_SECRET_KEY\" \\         -H \"Content-Type: application/json\" \\         \"https://api.scaleway.com/vpc-gw/v2/zones/$SCW_DEFAULT_ZONE/gateway-networks\" \\         -d '{             \"gateway_id\": \"b1b2edda-9364-422d-93f2-ad04e6a054dc\",              \"private_network_id\": \"548dbcc3-8b78-486f-a79a-c3f5a17642f9\",             \"enable_masquerade\": true         }'     ```      This configuration will set up the Public Gateway as a NAT gateway, masquerading traffic sent to it to the     outer internet to provide internet access to resources in the Private Network.      | Parameter       | Description                                         | Valid values                  |      |-----------------|-----------------------------------------------------|-------------------------------|     | gateway_id      | The Public Gateway ID of an existing Public Gateway | Any valid Public Gateway ID, e.g. `b1b2edda-9364-422d-93f2-ad04e6a054dc` |     | private_network_id    | The Private Network ID of an existing Private Network  | Any valid Private Network ID in the same Availability Zone as the Public Gateway, e.g. `548dbcc3-8b78-486f-a79a-c3f5a17642f9` |     | enable_masquerade     | Defines whether the gateway should masquerade traffic for the attached Private Network (i.e. whether to enable dynamic NAT) | A boolean value, e.g. `true` |      <Message type=\"note\">     Further parameters are available, but for the purposes of this quickstart we have included only the essentials. See the [Attach a gateway to a Private Network](#path-gateway-networks-attach-a-public-gateway-to-a-private-network) documentation below for full details of all possible parameters.     </Message>  6. **Delete a Public Gateway**: run the following call to delete your Public Gateway. Ensure that you replace `<PUBLIC-GATEWAY-ID>` in the URL with the ID of the Public Gateway you want to delete.      ```bash     curl -X DELETE \\         -H \"X-Auth-Token: $SCW_SECRET_KEY\" \\         -H \"Content-Type: application/json\" \\         \"https://api.scaleway.com/vpc-gw/v2/zones/$SCW_DEFAULT_ZONE/gateways/<PUBLIC-GATEWAY-ID>\"     ```      The expected successful response is empty.           <Message type=\"requirement\">     - You have a [Scaleway account](https://console.scaleway.com/)     - You have [created an API key](https://www.scaleway.com/en/docs/iam/how-to/create-api-keys/) and that the API key has sufficient [IAM permissions](https://www.scaleway.com/en/docs/iam/reference-content/permission-sets/) to perform the actions described on this page     - You have [installed `curl`](https://curl.se/download.html)     </Message>       ## Technical limitations  The following limitations apply to Public Gateways:  - A maximum of eight (8) Private Networks can be plugged into a single Public Gateway - Note that the Public Gateway takes some time to start up, and actions on it are impossible unless it is in the `running` state. To check the current state of a Public Gateway, use the [Get a Public Gateway](#path-gateways-get-a-public-gateway) endpoint to get information for your gateway: the `status` field of the response will tell you if it is running or in another state. - For further information about Public Gateway limitations see our [dedicated documentation](https://www.scaleway.com/en/docs/public-gateways/troubleshooting/gw-limitations/).   ## Technical information  ### Availability Zones  Public Gateways can be deployed in the following Availability Zones:  | Name      | API ID                | |-----------|-----------------------| | Paris     | `fr-par-1` `fr-par-2` | | Amsterdam | `nl-ams-1` `nl-ams-2` `nl-ams-3` | | Warsaw    | `pl-waw-1` `pl-waw-2` `pl-waw-3` |  The Scaleway Public Gateways API is a **zoned** API, meaning that each call must specify in its path parameters the Availability Zone for the resources concerned by the call.   ## Going further  For more help using Scaleway Public Gateways, check out the following resources: - Our [main documentation](https://www.scaleway.com/en/docs/public-gateways/) - The #public-gateway channel on our [Slack Community](https://www.scaleway.com/en/docs/tutorials/scaleway-slack-community/) - Our [support ticketing system](https://www.scaleway.com/en/docs/account/how-to/open-a-support-ticket/).
 *
 * The version of the OpenAPI document: v2
 * 
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */
package scaleway.vpcgw.api

import scaleway.vpcgw.models.ListGatewayTypesResponse
import scaleway.vpcgw.JsonSupport.{*, given}
import scaleway.vpcgw.FormSerializable
import scaleway.vpcgw.FormStyleFormat
import scaleway.vpcgw.HeaderSerializable
import scaleway.vpcgw.ApiKeyLocation
import scaleway.vpcgw.PathStyleFormat
import scaleway.vpcgw.PathSerializable
import scaleway.vpcgw.CookieSerializable
import scaleway.vpcgw.Helpers.*
import sttp.client4.jsoniter.*
import sttp.client4.*
import sttp.model.Method

object GatewayTypesApi:
  def apply(baseUrl: String = "https://api.scaleway.com"): GatewayTypesApi[scaleway.vpcgw.Authorization.NoAuthorization.type] = GatewayTypesApi(baseUrl, scaleway.vpcgw.Authorization.NoAuthorization)
  def withBasicAuth(baseUrl: String, username: String, password: String): GatewayTypesApi[scaleway.vpcgw.Authorization.BasicAuth] =
    GatewayTypesApi(baseUrl, scaleway.vpcgw.Authorization.BasicAuth(username, password))

  def withApiKeyAuth(baseUrl: String, apiKey: String): GatewayTypesApi[scaleway.vpcgw.Authorization.ApiKey] =
    GatewayTypesApi(baseUrl, scaleway.vpcgw.Authorization.ApiKey(apiKey))

  def withBearerTokenAuth(baseUrl: String, token: String): GatewayTypesApi[scaleway.vpcgw.Authorization.BearerToken] =
    GatewayTypesApi(baseUrl, scaleway.vpcgw.Authorization.BearerToken(token))

case class GatewayTypesApi[Auth <: scaleway.vpcgw.Authorization] private (baseUrl: String, authConfig: scaleway.vpcgw.Authorization):
  def withBasicAuth(username: String, password: String): GatewayTypesApi[scaleway.vpcgw.Authorization.BasicAuth] =
    copy(authConfig = scaleway.vpcgw.Authorization.BasicAuth(username, password))

  def withApiKeyAuth(apiKey: String): GatewayTypesApi[scaleway.vpcgw.Authorization.ApiKey] =
    copy(authConfig = scaleway.vpcgw.Authorization.ApiKey(apiKey))

  def withNoAuth: GatewayTypesApi[scaleway.vpcgw.Authorization.NoAuthorization.type] =
    copy(authConfig = scaleway.vpcgw.Authorization.NoAuthorization)

  def withBearerTokenAuth(token: String): GatewayTypesApi[scaleway.vpcgw.Authorization.BearerToken] =
    copy(authConfig = scaleway.vpcgw.Authorization.BearerToken(token))

  /**
   * List the different Public Gateway commercial offer types available at Scaleway. The response is an array of objects describing the name and technical details of each available gateway type.
   * 
   * Expected answers:
   *   code 200 : ListGatewayTypesResponse ()
   * 
   * Available security schemes:
   *   scaleway (apiKey)
   * 
   * @param zone The zone you want to target
   */
  def listGatewayTypes(zone: String)(using Auth <:< scaleway.vpcgw.Authorization.ApiKey): sttp.client4.Request[Either[ResponseException[String], ListGatewayTypesResponse]] =
    val zonePathParam = PathSerializable.serialize("zone", zone, PathStyleFormat.SIMPLE, false)
    val requestURL =
      uri"$baseUrl/vpc-gw/v2/zones/${zonePathParam}/gateway-types"

    basicRequest
      .method(Method.GET, requestURL)
      .contentType("application/json")
      .auth(authConfig, scaleway.vpcgw.ApiKeyLocation.HEADER, "X-Auth-Token")
      .response(asJson[ListGatewayTypesResponse])

end GatewayTypesApi