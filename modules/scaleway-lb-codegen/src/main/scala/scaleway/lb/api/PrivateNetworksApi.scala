/**
 * Load Balancer API
 * Load Balancers are highly available and fully managed [Instances](https://www.scaleway.com/en/docs/instances/concepts/#instance) that facilitate the distribution of incoming traffic across multiple servers. Load Balancers allow you to scale your applications while ensuring their continuous availability, even in the event of heavy traffic. They are commonly used to improve the performance and reliability of websites, applications, databases and other services.  A Scaleway Load Balancer has a **public IP address** which is always reachable: in the event of hardware failure it is rerouted to a backup Instance. The Load Balancer receives incoming traffic at this IP address, monitors the health and availability of its **backend servers** via **health checks**, and balances traffic load between all healthy and available backend servers.   You can create as many **frontends** and **backends** for each Load Balancer as you wish, with frontends configured to listen on defined ports and forward traffic to specific backends. You can also add **certificates** to frontends to enable secure, encrypted connections and facilitate SSL bridging or offloading. Backends can be configured to use your **protocol** of choice (TCP or HTTP) to connect to their backend servers. Additional features such as Access Control Lists (ACLs) and routes allow you to further configure the flow of traffic through your Scaleway Load Balancer.     ## Concepts  Refer to our [dedicated concepts page](https://www.scaleway.com/en/docs/load-balancer/concepts/) to find definitions of all Load Balancer-related terminology.     ## Quickstart  **Requirements**: To perform the following steps, you must first ensure that: - You have a [Scaleway account](https://console.scaleway.com/) - You have created an [API key](https://www.scaleway.com/en/docs/iam/how-to/create-api-keys/) and that the API key has sufficient [IAM permissions](https://www.scaleway.com/en/docs/iam/reference-content/permission-sets/) to perform the actions described on this page - You have [installed `curl`](https://curl.se/download.html)  1. Configure your environment variables.      <Message type=\"note\">     This is an optional step that seeks to simplify your usage of the Load Balancer API.     </Message>      ```bash     export SCW_SECRET_KEY=\"<API secret key>\"     export SCW_DEFAULT_ZONE=\"<Scaleway default Availability Zone>\"     export SCW_PROJECT_ID=\"<Scaleway Project ID>\"     ```  2. Create a Load Balancer: run the following command to create a Load Balancer. You can customize the details in the payload (name, description, tags, etc) as you wish.      ```bash     curl -X POST \\       -H \"X-Auth-Token: $SCW_SECRET_KEY\" \\       -H \"Content-Type: application/json\" \\       \"https://api.scaleway.com/lb/v1/zones/$SCW_DEFAULT_ZONE/lbs\" \\       -d '{         \"name\":\"API Test LB\",         \"description\": \"my new Load Balancer\",         \"project_id\":\"'\"$SCW_PROJECT_ID\"'\",         \"tags\":[\"test\",\"another tag\"]       }'     ```  3. **Get a list of your Load Balancers**: run the following command to get a list of all the Load Balancers in your account, with their details:      ```bash     curl -X GET \\       -H \"Content-Type: application/json\" \\       -H \"X-Auth-Token: $SCW_SECRET_KEY\" \\       \"https://api.scaleway.com/lb/v1/zones/$SCW_DEFAULT_ZONE/lbs\"     ```  4. **Create a backend for your Load Balancer**: run the following command to create a backend for a specified Load Balancer. Ensure that you replace `<LOAD-BALANCER-ID>` in the URL with the ID of the Load Balancer you want to create a backend for. You can customize the configuration of the backend according to your needs: use the information below to adjust the payload as necessary.      ```bash     curl -X POST \\       -H \"Content-Type: application/json\" \\       -H \"X-Auth-Token: $SCW_SECRET_KEY\" \\       \"https://api.scaleway.com/lb/v1/zones/$SCW_DEFAULT_ZONE/lbs/<LOAD-BALANCER-ID>/backends\" \\       -d '{         \"name\":\"main backend\",         \"forward_port\": 80,         \"forward_port_algorithm\": \"roundrobin\",         \"forward_protocol\": \"tcp\",         \"health_check\":{             \"check_delay\": 2000,             \"check_max_retries\": 3,             \"check_timeout\": 1000,             \"port\": 80,             \"tcp_config\":{}         },          \"server_ip\": [\"184.224.68.187\", \"139.7.243.56\"]       }'     ```        **Payload values**:        * **name** (string): A name for the backend, e.g. `\"main backend\"`       * **forward_port** (number): The port to use when connecting to a backend server to forward a user session, e.g. `8080`       * **forward_port_algorithm** (string): The forwarding algorithm to use when determining which backend server to forward a user session to. Must be one of the following:           * `\"roundrobin\"`: New sessions are forwarded to each backend server in turn.            * `\"leastconn\"`: New sessions are forwarded to the backend server with the fewest current active sessions.           * `\"first\"`: New sessions are forwarded to the first backend server found .       * **forward_protocol** (string): The protocol to use when connecting to a backend server to forward a user session. Must be one of the following:            * `\"tcp\"`: Transmission Control Protocol           * `\"http\"`: Hypertext Transfer Protocol        * **health_check** (object): The definition of the health check to use to check that backend servers are available and able to receive forwarded user sessions. Must contain the following parameters:           * `\"check_delay\"`: The time between two consecutive health checks (in milliseconds)           * `\"check_max_retries\"`: The number of consecutive unsuccessful health checks, after which the server will be considered dead           * `\"check_timeout:\"` The maximum time a backend server has to reply to the health check (in milliseconds)           * `\"port\"`: The port to use when connecting to a backend server for a health check           * `\"<type>_config\"`: The health check type to use. This parameter name must be one of `\"mysqlconfig\"`, `\"ldap_config\"`, `\"redis_config\"`, `\"tcp_config\"`, `\"pgsql_config\"`, `\"http_config\"` or `\"https_config\"`. The parameter value may be an empty object, or an object containing further parameters, depending on the health check configuration type selected. See the full documentation below on health checks for further details.               * Example of valid `health_check` value `{\"check_delay\":2000,\"check_max_retries\":3,\"check_timeout\":1000,\"port\":80,\"tcp_config\":{}}`       * **server_ip** (array): The list of IPv4 or IPv6 addresses of the backend servers to forward user sessions to, e.g. `[\"184.224.68.187\", \"139.7.243.56\"]`  5. **Create a frontend for your Load Balancer**: run the following command to create a frontend for a specified Load Balancer. Ensure that you replace `<LOAD-BALANCER-ID>` in the URL with the ID of the Load Balancer you want to create a frontend for. You can customize the configuration according to your needs: use the information below to adjust the payload as necessary.      ```bash     curl -X POST \\       -H \"Content-Type: application/json\" \\       -H \"X-Auth-Token: $SCW_SECRET_KEY\" \\       \"https://api.scaleway.com/lb/v1/zones/$SCW_DEFAULT_ZONE/lbs/<LOAD-BALANCER-ID>/frontends\" \\       -d '{         \"name\": \"main frontend\",         \"backend_id\": \"6920166d-8665-426e-85a6-f137e7e71e7f\",         \"inbound_port\": 80,         \"timeout_client\": 5000       }'     ```      **Payload values**:      * **name** (string): A name for the frontend, e.g. `\"main frontend\"`     * **backend_id** (number): The ID of the backend to attach to the frontend, e.g. `6920166d-8665-426e-85a6-f137e7e71e7f`     * **inbound_port** (number): The port that the frontend should listen on for incoming connections, e.g. `80`     * **timeout_client** (number): The maximum amount of inactivity time, in milliseconds, the frontend should allow before closing the connection, e.g. `5000`  6. **Delete your Load Balancer**: run the following command to delete a Load Balancer. Ensure that you replace `<LOAD-BALANCER-ID>` in the URL with the ID of the Load Balancer you want to delete. You can customize the payload to specify whether you want to release (delete) the [Flexible IP](https://www.scaleway.com/en/docs/load-balancer/concepts/#flexible-ip-address) associated with the Load Balancer or not.      ```bash     curl -X DELETE \\       -H \"Content-Type: application/json\" \\       -H \"X-Auth-Token: $SCW_SECRET_KEY\" \\       \"https://api.scaleway.com/lb/v1/zones/$SCW_DEFAULT_ZONE/lbs/<LOAD-BALANCER-ID>\" \\       -d '{         \"release_ip\": false       }'     ```   <Message type=\"requirement\"> - You have a [Scaleway account](https://console.scaleway.com/) - You have created an [API key](https://www.scaleway.com/en/docs/iam/how-to/create-api-keys/) and that the API key has sufficient [IAM permissions](https://www.scaleway.com/en/docs/iam/reference-content/permission-sets/) to perform the actions described on this page - You have [installed `curl`](https://curl.se/download.html) </Message>   ## Technical information   ### Availability Zones  Load Balancers can be deployed in the following Availability Zones:  | Name      | API ID                           | |-----------|----------------------------------| | Paris     | `fr-par-1` `fr-par-2`            | | Amsterdam | `nl-ams-1` `nl-ams-2` `nl-ams-3` | | Warsaw    | `pl-waw-1` `pl-waw-2` `pl-waw-3` |  The Scaleway Load Balancer API is a **zoned** API, meaning that each call must specify in its path parameters the Availability Zone for the resources concerned by the call.  ## Going further  For more help using Scaleway Load Balancers, check out the following resources: - Our [main documentation](https://www.scaleway.com/en/docs/load-balancer/) - The #load-balancer channel on our [Slack Community](https://www.scaleway.com/en/docs/tutorials/scaleway-slack-community/) - Our [support ticketing system](https://www.scaleway.com/en/docs/account/how-to/open-a-support-ticket/).
 *
 * The version of the OpenAPI document: v1
 * 
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */
package scaleway.lb.api

import scaleway.lb.models.AttachPrivateNetworkRequest
import scaleway.lb.models.DetachPrivateNetworkRequest
import scaleway.lb.models.ListLbPrivateNetworksResponse
import scaleway.lb.models.PrivateNetwork
import scaleway.lb.JsonSupport.{*, given}
import scaleway.lb.FormSerializable
import scaleway.lb.FormStyleFormat
import scaleway.lb.HeaderSerializable
import scaleway.lb.ApiKeyLocation
import scaleway.lb.PathStyleFormat
import scaleway.lb.PathSerializable
import scaleway.lb.CookieSerializable
import scaleway.lb.Helpers.*
import sttp.client4.jsoniter.*
import sttp.client4.*
import sttp.model.Method

object PrivateNetworksApi:
  def apply(baseUrl: String = "https://api.scaleway.com"): PrivateNetworksApi[scaleway.lb.Authorization.NoAuthorization.type] = PrivateNetworksApi(baseUrl, scaleway.lb.Authorization.NoAuthorization)
  def withBasicAuth(baseUrl: String, username: String, password: String): PrivateNetworksApi[scaleway.lb.Authorization.BasicAuth] =
    PrivateNetworksApi(baseUrl, scaleway.lb.Authorization.BasicAuth(username, password))

  def withApiKeyAuth(baseUrl: String, apiKey: String): PrivateNetworksApi[scaleway.lb.Authorization.ApiKey] =
    PrivateNetworksApi(baseUrl, scaleway.lb.Authorization.ApiKey(apiKey))

  def withBearerTokenAuth(baseUrl: String, token: String): PrivateNetworksApi[scaleway.lb.Authorization.BearerToken] =
    PrivateNetworksApi(baseUrl, scaleway.lb.Authorization.BearerToken(token))

case class PrivateNetworksApi[Auth <: scaleway.lb.Authorization] private (baseUrl: String, authConfig: scaleway.lb.Authorization):
  def withBasicAuth(username: String, password: String): PrivateNetworksApi[scaleway.lb.Authorization.BasicAuth] =
    copy(authConfig = scaleway.lb.Authorization.BasicAuth(username, password))

  def withApiKeyAuth(apiKey: String): PrivateNetworksApi[scaleway.lb.Authorization.ApiKey] =
    copy(authConfig = scaleway.lb.Authorization.ApiKey(apiKey))

  def withNoAuth: PrivateNetworksApi[scaleway.lb.Authorization.NoAuthorization.type] =
    copy(authConfig = scaleway.lb.Authorization.NoAuthorization)

  def withBearerTokenAuth(token: String): PrivateNetworksApi[scaleway.lb.Authorization.BearerToken] =
    copy(authConfig = scaleway.lb.Authorization.BearerToken(token))

  /**
   * Attach a specified Load Balancer to a specified Private Network, defining a static or DHCP configuration for the Load Balancer on the network.
   * 
   * Expected answers:
   *   code 200 : PrivateNetwork ()
   * 
   * Available security schemes:
   *   scaleway (apiKey)
   * 
   * @param zone The zone you want to target
   * @param lbId Load Balancer ID.
   * @param attachPrivateNetworkRequest 
   */
  def attachPrivateNetwork(zone: String, lbId: String, attachPrivateNetworkRequest: AttachPrivateNetworkRequest)(using Auth <:< scaleway.lb.Authorization.ApiKey): sttp.client4.Request[Either[ResponseException[String], PrivateNetwork]] =
    val zonePathParam = PathSerializable.serialize("zone", zone, PathStyleFormat.SIMPLE, false)
    val lbIdPathParam = PathSerializable.serialize("lb_id", lbId, PathStyleFormat.SIMPLE, false)
    val requestURL =
      uri"$baseUrl/lb/v1/zones/${zonePathParam}/lbs/${lbIdPathParam}/attach-private-network"

    basicRequest
      .method(Method.POST, requestURL)
      .contentType("application/json")
      .auth(authConfig, scaleway.lb.ApiKeyLocation.HEADER, "X-Auth-Token")
      .body(asJson(attachPrivateNetworkRequest))
      .response(asJson[PrivateNetwork])

  /**
   * Detach a specified Load Balancer from a specified Private Network.
   * 
   * Expected answers:
   *   code 204 :  ()
   * 
   * Available security schemes:
   *   scaleway (apiKey)
   * 
   * @param zone The zone you want to target
   * @param lbId Load balancer ID.
   * @param detachPrivateNetworkRequest 
   */
  def detachPrivateNetwork(zone: String, lbId: String, detachPrivateNetworkRequest: DetachPrivateNetworkRequest)(using Auth <:< scaleway.lb.Authorization.ApiKey): sttp.client4.Request[Either[ResponseException[String], Unit]] =
    val zonePathParam = PathSerializable.serialize("zone", zone, PathStyleFormat.SIMPLE, false)
    val lbIdPathParam = PathSerializable.serialize("lb_id", lbId, PathStyleFormat.SIMPLE, false)
    val requestURL =
      uri"$baseUrl/lb/v1/zones/${zonePathParam}/lbs/${lbIdPathParam}/detach-private-network"

    basicRequest
      .method(Method.POST, requestURL)
      .contentType("application/json")
      .auth(authConfig, scaleway.lb.ApiKeyLocation.HEADER, "X-Auth-Token")
      .body(asJson(detachPrivateNetworkRequest))
      .response(asString.mapWithMetadata(ResponseAs.deserializeRightWithError(_ => Right(()))))

  /**
   * List the Private Networks attached to a given Load Balancer, specified by its Load Balancer ID. The response is an array of Private Network objects, giving information including the status, configuration, name and creation date of each Private Network.
   * 
   * Expected answers:
   *   code 200 : ListLbPrivateNetworksResponse ()
   * 
   * Available security schemes:
   *   scaleway (apiKey)
   * 
   * @param zone The zone you want to target
   * @param lbId Load Balancer ID.
   * @param orderBy Sort order of Private Network objects in the response.
   * @param pageSize Number of objects to return.
   * @param page The page number to return, from the paginated results.
   */
  def listLbPrivateNetworks(zone: String, lbId: String, orderBy: Option[String] = scala.None, pageSize: Option[Int] = scala.None, page: Option[Int] = scala.None)(using Auth <:< scaleway.lb.Authorization.ApiKey): sttp.client4.Request[Either[ResponseException[String], ListLbPrivateNetworksResponse]] =
    val zonePathParam = PathSerializable.serialize("zone", zone, PathStyleFormat.SIMPLE, false)
    val lbIdPathParam = PathSerializable.serialize("lb_id", lbId, PathStyleFormat.SIMPLE, false)
    val requestURL =
      uri"$baseUrl/lb/v1/zones/${zonePathParam}/lbs/${lbIdPathParam}/private-networks"
        .addParams(FormSerializable.serialize("order_by", orderBy, FormStyleFormat.FORM, true): _*)
        .addParams(FormSerializable.serialize("page_size", pageSize, FormStyleFormat.FORM, true): _*)
        .addParams(FormSerializable.serialize("page", page, FormStyleFormat.FORM, true): _*)

    basicRequest
      .method(Method.GET, requestURL)
      .contentType("application/json")
      .auth(authConfig, scaleway.lb.ApiKeyLocation.HEADER, "X-Auth-Token")
      .response(asJson[ListLbPrivateNetworksResponse])

end PrivateNetworksApi