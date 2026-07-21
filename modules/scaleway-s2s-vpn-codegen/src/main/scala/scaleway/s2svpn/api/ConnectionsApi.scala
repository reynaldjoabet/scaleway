/**
 * Site-to-Site VPN
 * Scaleway Site-to-Site VPN connects your infrastructure (on-premise, in a private cloud, or in another public cloud) to your Scaleway VPC with IPsec.  Site-to-Site VPN establishes a private & encrypted tunnel over the public internet using IPsec. It allows your remote infrastructure to securely reach resources hosted in your Scaleway Private Networks without requiring dedicated physical links. This managed solution ensures confidentiality and integrity of through traffic, with a deployment that is simple and cost-effective, even across geographically distributed infrastructure.  You can establish one or multiple VPN tunnels depending on your needs, and take advantage of dynamic routing with BGP to ensure high availability and flexibility across your infrastructure.  Once connection is established with your VPC, Site-to-Site VPN allows you to control BGP route propagation: you can fine-tune which IPv4 or IPv6 prefixes are allowed to flow.   <Message type=\"note\"> This product is currently in [Public Beta](https://www.scaleway.com/en/betas/). </Message>   ## Concepts  ### VPN Gateway  A VPN gateway is a managed resource that serves IPsec tunnels between your external infrastructure and your Scaleway VPC. Each connection within the gateway represents an IPsec tunnel established over the public internet. A single VPN gateway can host multiple connections.  ### Customer Gateway  A customer gateway is a logical resource that represents your on-premises network device. It acts as the endpoint of the IPsec tunnel established with the Scaleway VPN gateway. Each customer gateway can be configured with one public IPv4 address, one public IPv6 address, and the ASN of the BGP router used to establish the BGP session over the IPsec tunnel.  This resource is required to define the remote side of the VPN tunnel. Its information will be used during the setup of the connection.  ### Connection  A connection represents the bridge between your VPN gateway and your customer gateway. One connection can support up to two IPsec tunnels: one over IPv4 and one over IPv6. This allows for a highly available tunnel configuration, provided that both the VPN gateway and the customer gateway have configured public IP addresses through Scaleway resources, using the correct IP version on both ends.     ## Quickstart  1. Configure your environment variables.     <Message type=\"note\">    This is an optional step that seeks to simplify your usage of the Site-to-Site VPN API.    </Message>     ```bash    export SCW_SECRET_KEY=\"<API secret key>\"    export SCW_DEFAULT_REGION=\"<Scaleway default region>\"    export SCW_PROJECT_ID=\"<Scaleway Project ID>\"    ```  2. **Choose a VPN gateway type**: VPN gateways come in different shapes, sizes, and pricing. When you create your VPN gateway, you need to specify the required gateway type in the request. Use the following call to get a list of available VPN gateway offer types and their details:      ```bash     curl -X GET \\         -H \"X-Auth-Token: $SCW_SECRET_KEY\" \\         -H \"Content-Type: application/json\" \\         \"https://api.scaleway.com/s2s-vpn/v1alpha1/regions/$SCW_DEFAULT_REGION/vpn-gateway-types\"     ```  3. **Create a VPN gateway**: run the following command to create a VPN Gateway. You can customize the details in the payload to your needs, using the table below to help.     ```bash    curl -X POST \\      -H \"X-Auth-Token: $SCW_SECRET_KEY\" \\      -H \"Content-Type: application/json\" \\      \"https://api.scaleway.com/s2s-vpn/v1alpha1/regions/$SCW_DEFAULT_REGION/vpn-gateways\" \\      -d '{        \"project_id\": \"'\"$SCW_PROJECT_ID\"'\",        \"name\": \"My New Gateway\",        \"tags\": [\"test\", \"another tag\"],        \"gateway_type\": \"VGW-S\",        \"private_network_id\": \"00cfd222-2402-4598-9438-b12b995e9e80\"      }'    ```     | Parameter          | Description                                         | Valid values OR Example                   |    | ------------------ | --------------------------------------------------- | ----------------------------------------- |    | project_id         | ID of project                                       | `any valid project UUID`                  |    | name               | Name for VPN Gateway                                | `desired name for VPN Gateway`            |    | tags               | Tags for VPN Gateway                                | `a list of tags`                          |    | gateway_type       | Offer type for VPN Gateway                          | `any valid offer type string, e.g. VGW-S` |    | private_network_id | ID of a Private Network (cannot be detach later)    | `any valid private network UUID`          |  4. **Get a list of your VPN gateways**: run the following command to get a list of all the VPN gateways in your account, with their details:     ```bash    curl -X GET \\      -H \"Content-Type: application/json\" \\      -H \"X-Auth-Token: $SCW_SECRET_KEY\" \\      \"https://api.scaleway.com/s2s-vpn/v1alpha1/regions/$SCW_DEFAULT_REGION/vpn-gateways\"    ```  5. **Create a customer gateway**: run the following command to create a customer gateway. You can customize the details in the payload to your needs, using the table below to help.     ```bash    curl -X POST \\      -H \"X-Auth-Token: $SCW_SECRET_KEY\" \\      -H \"Content-Type: application/json\" \\      \"https://api.scaleway.com/s2s-vpn/v1alpha1/regions/$SCW_DEFAULT_REGION/customer-gateways\" \\      -d '{        \"project_id\": \"'\"$SCW_PROJECT_ID\"'\",        \"name\": \"My Device\",        \"tags\": [\"test\", \"another tag\"],        \"ipv4_public\": \"51.158.0.1\",        \"asn\": \"11111\"      }'    ```     | Parameter   | Description                     | Valid values OR Example                       |    | ----------- | ------------------------------- | --------------------------------------------- |    | project_id  | ID of project                   | `any valid project UUID`                      |    | name        | Name for Customer Gateway       | `desired name for Connection`                 |    | tags        | Tags for Customer Gateway       | `a list of tags`                              |    | ipv4_public | Public IPv4 of Customer Gateway | `any valid public ipv4 ip`                    |    | asn         | BGP ASN of Customer Gateway     | `any valid asn except reserved scw ASN 12876` |  6. **Create a routing policy**: A routing policy is required for each traffic type (IPv4 and/or IPv6) to be supported over the connection.     ```bash    curl -X POST \\      -H \"X-Auth-Token: $SCW_SECRET_KEY\" \\      -H \"Content-Type: application/json\" \\      \"https://api.scaleway.com/s2s-vpn/v1alpha1/regions/$SCW_DEFAULT_REGION/routing-policies\" \\      -d '{        \"project_id\": \"'\"$SCW_PROJECT_ID\"'\",        \"name\": \"my-rp-v4\",        \"tags\": [\"test\", \"another tag\"],        \"is_ipv6\": false,        \"prefix_filter_in\": [          \"10.0.0.0/8\"        ],        \"prefix_filter_out\": [          \"172.16.12.0/24\"        ]      }'    ```     | Parameter           | Description                                                                               | Valid values OR Example        |    | ------------------- | ----------------------------------------------------------------------------------------- | ------------------------------ |    | project_id          | ID of Project                                                                             | `any valid project UUID`       |    | name                | Name for routing policy                                                                   | `desired name for Connection`  |    | tags                | Tags for routing policy                                                                   | `a list of tags`               |    | is_ipv6             | Whether routing policy should be IPv6 or IPv4                                             | `true or false`                |    | prefix_filter_in    | IP prefixes to accept from the customer gateway (ranges of route announcements to accept) | `a list of IP subnets`         |    | prefix_filter_out   | IP prefixes to advertise to the customer gateway (ranges of routes to advertise)          | `a list of IP subnets`         |  7. **Create a connection between a VPN gateway and a customer gateway**: When your VPN gateway has been provisioned and has **active** status, you must create a connection.     ```bash    curl -X POST \\      -H \"Content-Type: application/json\" \\      -H \"X-Auth-Token: $SCW_SECRET_KEY\" \\      \"https://api.scaleway.com/s2s-vpn/v1alpha1/regions/$SCW_DEFAULT_REGION/connections\" \\      -d '{        \"project_id\": \"'\"$SCW_PROJECT_ID\"'\",        \"name\": \"My New Connection\",        \"tags\": [\"test\", \"another tag\"],        \"vpn_gateway_id\": \"ec5b8281-0c4f-49ea-a8fd-bd37554f5896\",        \"customer_gateway_id\": \"beba6285-b502-4579-82d6-2815fb1ef1f1\",        \"ikev2_ciphers\": [{\"encryption\": \"aes256\", \"integrity\": \"sha256\", \"dh_group\": \"curve25519\"}],        \"esp_ciphers\": [{\"encryption\": \"aes256gcm\", \"dh_group\": \"curve25519\"}],        \"bgp_config_ipv4\": {          \"routing_policy_id\": \"3fe0612d-6d61-41a8-87f4-243900e07616\"        }      }'    ```     | Parameter           | Description                                                              | Valid values OR Example                   |    | ------------------- | ------------------------------------------------------------------------ | ----------------------------------------- |    | project_id          | ID of project                                                            | `any valid project UUID`                  |    | name                | Name for Connection                                                      | `desired name for Connection`             |    | tags                | Tags for Connection                                                      | `a list of tags`                          |    | vpn_gateway_id      | ID of a VPN Gateway                                                      | `any valid VPN Gateway UUID`              |    | customer_gateway_id | ID of a Customer Gateway                                                 | `any valid Customer Gateway UUID`         |    | routing_policy_id   | ID of a Routing Policy (same parameter available for bgp_config_ipv6)    | `any valid Routing Policy UUID`           |     Refer to the [documentation](https://www.scaleway.com/en/docs/site-to-site-vpn/) for help with these steps if necessary.  7. **Configure your customer gateway device**: Configure your real physical or software-based networking device, located on the remote network you want to connect to your Scaleway VPC. It is the physical device represented by your customer gateway. To successfully configure the device, you will need the public IP address(es) of the VPN gateway, the Scaleway ASN (12876) and the pre-shared key of the connection.  8. **Enable route propagation**: Enable route propagation to prompt the two gateways to initiate BGP sessions and share routing information. This is the final step in allowing traffic to flow across the Site-to-Site VPN connection. Ensure that you replace `{connection-id}` in the URL with the ID of the connection on which you want to enable route propagation.     ```bash    curl -X POST \\      -H \"Content-Type: application/json\" \\      -H \"X-Auth-Token: $SCW_SECRET_KEY\" \\      \"https://api.scaleway.com/s2s-vpn/v1alpha1/regions/$SCW_DEFAULT_REGION/connections/{connection-id}/enable-route-propagation\"    ```  9. **Delete your VPN gateway**: run the following command to delete a VPN gateway when you no longer need it. Ensure that you replace `{vpn-gateway-id}` in the URL with the ID of the VPN gateway you want to delete.     ```bash    curl -X DELETE \\      -H \"Content-Type: application/json\" \\      -H \"X-Auth-Token: $SCW_SECRET_KEY\" \\      \"https://api.scaleway.com/s2s-vpn/v1alpha1/regions/$SCW_DEFAULT_REGION/vpn-gateways/{vpn-gateway-id}\"    ```   <Message type=\"requirement\">  - You have a [Scaleway account](https://console.scaleway.com/) - You have created an [API key](https://www.scaleway.com/en/docs/iam/how-to/create-api-keys/) and that the API key has sufficient [IAM permissions](https://www.scaleway.com/en/docs/iam/reference-content/permission-sets/) to perform the actions described on this page - You have [installed `curl`](https://curl.se/download.html)   </Message>     ### Regions  The Scaleway Site-to-Site VPN is a **regional** API, meaning that each call must specify in its path parameters the Region for the resources concerned by the call.  The following Regions are available for Site-to-Site VPN:  | Name      | API ID                | |-----------|-----------------------| | Paris     | `fr-par` | | Milan     | `it-mil` | | Amsterdam | `nl-ams` | | Warsaw    | `pl-waw` |  ## Going further  For more help using Scaleway Site-to-Site VPN, check out the following resources:  - Our [main documentation](https://www.scaleway.com/en/docs/site-to-site-vpn/) - The `#virtual-private-cloud` channel on our [Slack Community](https://www.scaleway.com/en/docs/tutorials/scaleway-slack-community/).
 *
 * The version of the OpenAPI document: v1alpha1
 * 
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */
package scaleway.s2svpn.api

import scaleway.s2svpn.models.ChangeConnectionPskRequest
import scaleway.s2svpn.models.ChangeConnectionPskResponse
import scaleway.s2svpn.models.Connection
import scaleway.s2svpn.models.CreateConnectionRequest
import scaleway.s2svpn.models.CreateConnectionResponse
import scaleway.s2svpn.models.DetachRoutingPolicyRequest
import scaleway.s2svpn.models.ListConnectionsResponse
import scaleway.s2svpn.models.RenewConnectionPskRequest
import scaleway.s2svpn.models.RenewConnectionPskResponse
import scaleway.s2svpn.models.SetRoutingPolicyRequest
import scaleway.s2svpn.models.Status.*
import scaleway.s2svpn.models.Status
import scaleway.s2svpn.models.UpdateConnectionRequest
import com.github.plokhotnyuk.jsoniter_scala.circe.JsoniterScalaCodec.*
import scaleway.s2svpn.JsonSupport.{*, given}
import scaleway.s2svpn.FormSerializable
import scaleway.s2svpn.FormStyleFormat
import scaleway.s2svpn.HeaderSerializable
import scaleway.s2svpn.ApiKeyLocation
import scaleway.s2svpn.PathStyleFormat
import scaleway.s2svpn.PathSerializable
import scaleway.s2svpn.CookieSerializable
import scaleway.s2svpn.Helpers.*
import sttp.client4.jsoniter.*
import sttp.client4.*
import sttp.model.Method

object ConnectionsApi:
  def apply(baseUrl: String = "https://api.scaleway.com"): ConnectionsApi[scaleway.s2svpn.Authorization.NoAuthorization.type] = ConnectionsApi(baseUrl, scaleway.s2svpn.Authorization.NoAuthorization)
  def withBasicAuth(baseUrl: String, username: String, password: String): ConnectionsApi[scaleway.s2svpn.Authorization.BasicAuth] =
    ConnectionsApi(baseUrl, scaleway.s2svpn.Authorization.BasicAuth(username, password))

  def withApiKeyAuth(baseUrl: String, apiKey: String): ConnectionsApi[scaleway.s2svpn.Authorization.ApiKey] =
    ConnectionsApi(baseUrl, scaleway.s2svpn.Authorization.ApiKey(apiKey))

  def withBearerTokenAuth(baseUrl: String, token: String): ConnectionsApi[scaleway.s2svpn.Authorization.BearerToken] =
    ConnectionsApi(baseUrl, scaleway.s2svpn.Authorization.BearerToken(token))

case class ConnectionsApi[Auth <: scaleway.s2svpn.Authorization] private (baseUrl: String, authConfig: scaleway.s2svpn.Authorization):
  def withBasicAuth(username: String, password: String): ConnectionsApi[scaleway.s2svpn.Authorization.BasicAuth] =
    copy(authConfig = scaleway.s2svpn.Authorization.BasicAuth(username, password))

  def withApiKeyAuth(apiKey: String): ConnectionsApi[scaleway.s2svpn.Authorization.ApiKey] =
    copy(authConfig = scaleway.s2svpn.Authorization.ApiKey(apiKey))

  def withNoAuth: ConnectionsApi[scaleway.s2svpn.Authorization.NoAuthorization.type] =
    copy(authConfig = scaleway.s2svpn.Authorization.NoAuthorization)

  def withBearerTokenAuth(token: String): ConnectionsApi[scaleway.s2svpn.Authorization.BearerToken] =
    copy(authConfig = scaleway.s2svpn.Authorization.BearerToken(token))

  /**
   * Change pre-shared key for a given connection.
   * 
   * Expected answers:
   *   code 200 : ChangeConnectionPskResponse ()
   * 
   * Available security schemes:
   *   scaleway (apiKey)
   * 
   * @param region The region you want to target
   * @param connectionId ID of the connection to renew the PSK. (UUID format)
   * @param changeConnectionPskRequest 
   */
  def changeConnectionPsk(region: String, connectionId: String, changeConnectionPskRequest: ChangeConnectionPskRequest)(using Auth <:< scaleway.s2svpn.Authorization.ApiKey): sttp.client4.Request[Either[ResponseException[String], ChangeConnectionPskResponse]] =
    val regionPathParam = PathSerializable.serialize("region", region, PathStyleFormat.SIMPLE, false)
    val connectionIdPathParam = PathSerializable.serialize("connection_id", connectionId, PathStyleFormat.SIMPLE, false)
    val requestURL =
      uri"$baseUrl/s2s-vpn/v1alpha1/regions/${regionPathParam}/connections/${connectionIdPathParam}/change-psk"

    basicRequest
      .method(Method.POST, requestURL)
      .contentType("application/json")
      .auth(authConfig, scaleway.s2svpn.ApiKeyLocation.HEADER, "X-Auth-Token")
      .body(asJson(changeConnectionPskRequest))
      .response(asJson[ChangeConnectionPskResponse])

  /**
   * Expected answers:
   *   code 200 : CreateConnectionResponse ()
   * 
   * Available security schemes:
   *   scaleway (apiKey)
   * 
   * @param region The region you want to target
   * @param createConnectionRequest 
   */
  def createConnection(region: String, createConnectionRequest: CreateConnectionRequest)(using Auth <:< scaleway.s2svpn.Authorization.ApiKey): sttp.client4.Request[Either[ResponseException[String], CreateConnectionResponse]] =
    val regionPathParam = PathSerializable.serialize("region", region, PathStyleFormat.SIMPLE, false)
    val requestURL =
      uri"$baseUrl/s2s-vpn/v1alpha1/regions/${regionPathParam}/connections"

    basicRequest
      .method(Method.POST, requestURL)
      .contentType("application/json")
      .auth(authConfig, scaleway.s2svpn.ApiKeyLocation.HEADER, "X-Auth-Token")
      .body(asJson(createConnectionRequest))
      .response(asJson[CreateConnectionResponse])

  /**
   * Delete an existing connection, specified by its connection ID.
   * 
   * Expected answers:
   *   code 204 :  ()
   * 
   * Available security schemes:
   *   scaleway (apiKey)
   * 
   * @param region The region you want to target
   * @param connectionId ID of the connection to delete. (UUID format)
   */
  def deleteConnection(region: String, connectionId: String)(using Auth <:< scaleway.s2svpn.Authorization.ApiKey): sttp.client4.Request[Either[ResponseException[String], Unit]] =
    val regionPathParam = PathSerializable.serialize("region", region, PathStyleFormat.SIMPLE, false)
    val connectionIdPathParam = PathSerializable.serialize("connection_id", connectionId, PathStyleFormat.SIMPLE, false)
    val requestURL =
      uri"$baseUrl/s2s-vpn/v1alpha1/regions/${regionPathParam}/connections/${connectionIdPathParam}"

    basicRequest
      .method(Method.DELETE, requestURL)
      .contentType("application/json")
      .auth(authConfig, scaleway.s2svpn.ApiKeyLocation.HEADER, "X-Auth-Token")
      .response(asString.mapWithMetadata(ResponseAs.deserializeRightWithError(_ => Right(()))))

  /**
   * Detach an existing routing policy from a connection, specified by its connection ID.
   * 
   * Expected answers:
   *   code 200 : Connection ()
   * 
   * Available security schemes:
   *   scaleway (apiKey)
   * 
   * @param region The region you want to target
   * @param connectionId ID of the connection from which routing policy is being detached. (UUID format)
   * @param detachRoutingPolicyRequest 
   */
  def detachRoutingPolicy(region: String, connectionId: String, detachRoutingPolicyRequest: DetachRoutingPolicyRequest)(using Auth <:< scaleway.s2svpn.Authorization.ApiKey): sttp.client4.Request[Either[ResponseException[String], Connection]] =
    val regionPathParam = PathSerializable.serialize("region", region, PathStyleFormat.SIMPLE, false)
    val connectionIdPathParam = PathSerializable.serialize("connection_id", connectionId, PathStyleFormat.SIMPLE, false)
    val requestURL =
      uri"$baseUrl/s2s-vpn/v1alpha1/regions/${regionPathParam}/connections/${connectionIdPathParam}/detach-routing-policy"

    basicRequest
      .method(Method.POST, requestURL)
      .contentType("application/json")
      .auth(authConfig, scaleway.s2svpn.ApiKeyLocation.HEADER, "X-Auth-Token")
      .body(asJson(detachRoutingPolicyRequest))
      .response(asJson[Connection])

  /**
   * Prevent any prefixes from being announced in the BGP session. Traffic will not be able to flow over the VPN Gateway until route propagation is re-enabled.
   * 
   * Expected answers:
   *   code 200 : Connection ()
   * 
   * Available security schemes:
   *   scaleway (apiKey)
   * 
   * @param region The region you want to target
   * @param connectionId ID of the connection on which to disable route propagation. (UUID format)
   * @param body 
   */
  def disableRoutePropagation(region: String, connectionId: String, body: io.circe.Json)(using Auth <:< scaleway.s2svpn.Authorization.ApiKey): sttp.client4.Request[Either[ResponseException[String], Connection]] =
    val regionPathParam = PathSerializable.serialize("region", region, PathStyleFormat.SIMPLE, false)
    val connectionIdPathParam = PathSerializable.serialize("connection_id", connectionId, PathStyleFormat.SIMPLE, false)
    val requestURL =
      uri"$baseUrl/s2s-vpn/v1alpha1/regions/${regionPathParam}/connections/${connectionIdPathParam}/disable-route-propagation"

    basicRequest
      .method(Method.POST, requestURL)
      .contentType("application/json")
      .auth(authConfig, scaleway.s2svpn.ApiKeyLocation.HEADER, "X-Auth-Token")
      .body(asJson(body))
      .response(asJson[Connection])

  /**
   * Enable all allowed prefixes (defined in a routing policy) to be announced in the BGP session. This allows traffic to flow between the attached VPC and the on-premises infrastructure along the announced routes. Note that by default, even when route propagation is enabled, all routes are blocked. It is essential to attach a routing policy to define the ranges of routes to announce.
   * 
   * Expected answers:
   *   code 200 : Connection ()
   * 
   * Available security schemes:
   *   scaleway (apiKey)
   * 
   * @param region The region you want to target
   * @param connectionId ID of the connection on which to enable route propagation. (UUID format)
   * @param body 
   */
  def enableRoutePropagation(region: String, connectionId: String, body: io.circe.Json)(using Auth <:< scaleway.s2svpn.Authorization.ApiKey): sttp.client4.Request[Either[ResponseException[String], Connection]] =
    val regionPathParam = PathSerializable.serialize("region", region, PathStyleFormat.SIMPLE, false)
    val connectionIdPathParam = PathSerializable.serialize("connection_id", connectionId, PathStyleFormat.SIMPLE, false)
    val requestURL =
      uri"$baseUrl/s2s-vpn/v1alpha1/regions/${regionPathParam}/connections/${connectionIdPathParam}/enable-route-propagation"

    basicRequest
      .method(Method.POST, requestURL)
      .contentType("application/json")
      .auth(authConfig, scaleway.s2svpn.ApiKeyLocation.HEADER, "X-Auth-Token")
      .body(asJson(body))
      .response(asJson[Connection])

  /**
   * Get a connection for the given connection ID. The response object includes information about the connection's various configuration details.
   * 
   * Expected answers:
   *   code 200 : Connection ()
   * 
   * Available security schemes:
   *   scaleway (apiKey)
   * 
   * @param region The region you want to target
   * @param connectionId ID of the requested connection. (UUID format)
   */
  def getConnection(region: String, connectionId: String)(using Auth <:< scaleway.s2svpn.Authorization.ApiKey): sttp.client4.Request[Either[ResponseException[String], Connection]] =
    val regionPathParam = PathSerializable.serialize("region", region, PathStyleFormat.SIMPLE, false)
    val connectionIdPathParam = PathSerializable.serialize("connection_id", connectionId, PathStyleFormat.SIMPLE, false)
    val requestURL =
      uri"$baseUrl/s2s-vpn/v1alpha1/regions/${regionPathParam}/connections/${connectionIdPathParam}"

    basicRequest
      .method(Method.GET, requestURL)
      .contentType("application/json")
      .auth(authConfig, scaleway.s2svpn.ApiKeyLocation.HEADER, "X-Auth-Token")
      .response(asJson[Connection])

  /**
   * List all your connections. A number of filters are available, including Project ID, name, tags and status.
   * 
   * Expected answers:
   *   code 200 : ListConnectionsResponse ()
   * 
   * Available security schemes:
   *   scaleway (apiKey)
   * 
   * @param region The region you want to target
   * @param page Page number to return.
   * @param pageSize Maximum number of connections to return per page.
   * @param orderBy Order in which to return results.
   * @param projectId Project ID to filter for. (UUID format)
   * @param organizationId Organization ID to filter for. (UUID format)
   * @param name Connection name to filter for.
   * @param tags Tags to filter for.
   * @param statuses Connection statuses to filter for.
   * @param isIpv6 Filter connections with IP version of IPSec tunnel.
   * @param routingPolicyIds Filter for connections using these routing policies.
   * @param routePropagationEnabled Filter for connections with route propagation enabled.
   * @param vpnGatewayIds Filter for connections attached to these VPN gateways.
   * @param customerGatewayIds Filter for connections attached to these customer gateways.
   */
  def listConnections(region: String, page: Option[Int] = scala.None, pageSize: Option[Int] = scala.None, orderBy: Option[String] = scala.None, projectId: Option[String] = scala.None, organizationId: Option[String] = scala.None, name: Option[String] = scala.None, tags: Seq[String], statuses: Seq[Status], isIpv6: Option[Boolean] = scala.None, routingPolicyIds: Seq[String], routePropagationEnabled: Option[Boolean] = scala.None, vpnGatewayIds: Seq[String], customerGatewayIds: Seq[String])(using Auth <:< scaleway.s2svpn.Authorization.ApiKey): sttp.client4.Request[Either[ResponseException[String], ListConnectionsResponse]] =
    val regionPathParam = PathSerializable.serialize("region", region, PathStyleFormat.SIMPLE, false)
    val requestURL =
      uri"$baseUrl/s2s-vpn/v1alpha1/regions/${regionPathParam}/connections"
        .addParams(FormSerializable.serialize("page", page, FormStyleFormat.FORM, true): _*)
        .addParams(FormSerializable.serialize("page_size", pageSize, FormStyleFormat.FORM, true): _*)
        .addParams(FormSerializable.serialize("order_by", orderBy, FormStyleFormat.FORM, true): _*)
        .addParams(FormSerializable.serialize("project_id", projectId, FormStyleFormat.FORM, true): _*)
        .addParams(FormSerializable.serialize("organization_id", organizationId, FormStyleFormat.FORM, true): _*)
        .addParams(FormSerializable.serialize("name", name, FormStyleFormat.FORM, true): _*)
        .addParams(FormSerializable.serialize("tags", tags, FormStyleFormat.FORM, true): _*)
        .addParams(FormSerializable.serialize("statuses", statuses, FormStyleFormat.FORM, true): _*)
        .addParams(FormSerializable.serialize("is_ipv6", isIpv6, FormStyleFormat.FORM, true): _*)
        .addParams(FormSerializable.serialize("routing_policy_ids", routingPolicyIds, FormStyleFormat.FORM, true): _*)
        .addParams(FormSerializable.serialize("route_propagation_enabled", routePropagationEnabled, FormStyleFormat.FORM, true): _*)
        .addParams(FormSerializable.serialize("vpn_gateway_ids", vpnGatewayIds, FormStyleFormat.FORM, true): _*)
        .addParams(FormSerializable.serialize("customer_gateway_ids", customerGatewayIds, FormStyleFormat.FORM, true): _*)

    basicRequest
      .method(Method.GET, requestURL)
      .contentType("application/json")
      .auth(authConfig, scaleway.s2svpn.ApiKeyLocation.HEADER, "X-Auth-Token")
      .response(asJson[ListConnectionsResponse])

  /**
   * Renew pre-shared key for a given connection.
   * 
   * Expected answers:
   *   code 200 : RenewConnectionPskResponse ()
   * 
   * Available security schemes:
   *   scaleway (apiKey)
   * 
   * @param region The region you want to target
   * @param connectionId ID of the connection to renew the PSK. (UUID format)
   * @param renewConnectionPskRequest 
   */
  def renewConnectionPsk(region: String, connectionId: String, renewConnectionPskRequest: RenewConnectionPskRequest)(using Auth <:< scaleway.s2svpn.Authorization.ApiKey): sttp.client4.Request[Either[ResponseException[String], RenewConnectionPskResponse]] =
    val regionPathParam = PathSerializable.serialize("region", region, PathStyleFormat.SIMPLE, false)
    val connectionIdPathParam = PathSerializable.serialize("connection_id", connectionId, PathStyleFormat.SIMPLE, false)
    val requestURL =
      uri"$baseUrl/s2s-vpn/v1alpha1/regions/${regionPathParam}/connections/${connectionIdPathParam}/renew-psk"

    basicRequest
      .method(Method.POST, requestURL)
      .contentType("application/json")
      .auth(authConfig, scaleway.s2svpn.ApiKeyLocation.HEADER, "X-Auth-Token")
      .body(asJson(renewConnectionPskRequest))
      .response(asJson[RenewConnectionPskResponse])

  /**
   * Set a new routing policy on a connection, overriding the existing one if present, specified by its connection ID.
   * 
   * Expected answers:
   *   code 200 : Connection ()
   * 
   * Available security schemes:
   *   scaleway (apiKey)
   * 
   * @param region The region you want to target
   * @param connectionId ID of the connection whose routing policy is being updated. (UUID format)
   * @param setRoutingPolicyRequest 
   */
  def setRoutingPolicy(region: String, connectionId: String, setRoutingPolicyRequest: SetRoutingPolicyRequest)(using Auth <:< scaleway.s2svpn.Authorization.ApiKey): sttp.client4.Request[Either[ResponseException[String], Connection]] =
    val regionPathParam = PathSerializable.serialize("region", region, PathStyleFormat.SIMPLE, false)
    val connectionIdPathParam = PathSerializable.serialize("connection_id", connectionId, PathStyleFormat.SIMPLE, false)
    val requestURL =
      uri"$baseUrl/s2s-vpn/v1alpha1/regions/${regionPathParam}/connections/${connectionIdPathParam}/set-routing-policy"

    basicRequest
      .method(Method.POST, requestURL)
      .contentType("application/json")
      .auth(authConfig, scaleway.s2svpn.ApiKeyLocation.HEADER, "X-Auth-Token")
      .body(asJson(setRoutingPolicyRequest))
      .response(asJson[Connection])

  /**
   * Update an existing connection, specified by its connection ID.
   * 
   * Expected answers:
   *   code 200 : Connection ()
   * 
   * Available security schemes:
   *   scaleway (apiKey)
   * 
   * @param region The region you want to target
   * @param connectionId ID of the connection to update. (UUID format)
   * @param updateConnectionRequest 
   */
  def updateConnection(region: String, connectionId: String, updateConnectionRequest: UpdateConnectionRequest)(using Auth <:< scaleway.s2svpn.Authorization.ApiKey): sttp.client4.Request[Either[ResponseException[String], Connection]] =
    val regionPathParam = PathSerializable.serialize("region", region, PathStyleFormat.SIMPLE, false)
    val connectionIdPathParam = PathSerializable.serialize("connection_id", connectionId, PathStyleFormat.SIMPLE, false)
    val requestURL =
      uri"$baseUrl/s2s-vpn/v1alpha1/regions/${regionPathParam}/connections/${connectionIdPathParam}"

    basicRequest
      .method(Method.PATCH, requestURL)
      .contentType("application/json")
      .auth(authConfig, scaleway.s2svpn.ApiKeyLocation.HEADER, "X-Auth-Token")
      .body(asJson(updateConnectionRequest))
      .response(asJson[Connection])

end ConnectionsApi