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
package scaleway.lb.models

import java.time.OffsetDateTime
import com.github.plokhotnyuk.jsoniter_scala.macros.named

  /**
   * Load Balancer object which is attached to the Private Network.
   */
case class ScalewayLbV1PrivateNetworkLb(
  /* Underlying Instance ID. */
  @named("id") id: Option[String] = scala.None,
  /* Load Balancer name. */
  @named("name") name: Option[String] = scala.None,
  /* Load Balancer description. */
  @named("description") description: Option[String] = scala.None,
  /* Load Balancer status. */
  @named("status") status: Option[ScalewayLbV1PrivateNetworkLbEnums.Status] = scala.None,
  /* List of underlying Instances. */
  @named("instances") instances: Option[Seq[Instance]] = scala.None,
  /* Scaleway Organization ID. */
  @named("organization_id") organizationId: Option[String] = scala.None,
  /* Scaleway Project ID. */
  @named("project_id") projectId: Option[String] = scala.None,
  /* List of IP addresses attached to the Load Balancer. */
  @named("ip") ip: Option[Seq[Ip]] = scala.None,
  /* Load Balancer tags. */
  @named("tags") tags: Option[Seq[String]] = scala.None,
  /* Number of frontends the Load Balancer has. */
  @named("frontend_count") frontendCount: Option[Int] = scala.None,
  /* Number of backends the Load Balancer has. */
  @named("backend_count") backendCount: Option[Int] = scala.None,
  /* Load Balancer offer type. */
  @named("type") `type`: Option[String] = scala.None,
  @named("subscriber") subscriber: Option[ScalewayLbV1AclFrontendBackendLbSubscriber] = scala.None,
  /* Determines the minimal SSL version which needs to be supported on client side. */
  @named("ssl_compatibility_level") sslCompatibilityLevel: Option[ScalewayLbV1PrivateNetworkLbEnums.SslCompatibilityLevel] = scala.None,
  /* Date on which the Load Balancer was created. (RFC 3339 format) */
  @named("created_at") createdAt: Option[OffsetDateTime] = scala.None,
  /* Date on which the Load Balancer was last updated. (RFC 3339 format) */
  @named("updated_at") updatedAt: Option[OffsetDateTime] = scala.None,
  /* Number of Private Networks attached to the Load Balancer. */
  @named("private_network_count") privateNetworkCount: Option[Int] = scala.None,
  /* Number of routes configured on the Load Balancer. */
  @named("route_count") routeCount: Option[Int] = scala.None,
  /* The region the Load Balancer is in. */
  @named("region") region: Option[String] = scala.None,
  /* The zone the Load Balancer is in. */
  @named("zone") zone: Option[String] = scala.None
)

object ScalewayLbV1PrivateNetworkLbEnums:
  enum Status:
    case `unknown`
    case `ready`
    case `pending`
    case `stopped`
    case `error`
    case `locked`
    case `migrating`
    case `to_create`
    case `creating`
    case `to_delete`
    case `deleting`

  object Status:
    import com.github.plokhotnyuk.jsoniter_scala.macros.*
    import com.github.plokhotnyuk.jsoniter_scala.core.*
    given statusCodec: JsonValueCodec[Status] = JsonCodecMaker.make {
      CodecMakerConfig
        .withAdtLeafClassNameMapper { x =>
          JsonCodecMaker.simpleClassName(x) match
            case "unknown" => "unknown"
            case "ready" => "ready"
            case "pending" => "pending"
            case "stopped" => "stopped"
            case "error" => "error"
            case "locked" => "locked"
            case "migrating" => "migrating"
            case "to_create" => "to_create"
            case "creating" => "creating"
            case "to_delete" => "to_delete"
            case "deleting" => "deleting"
        }
        .withDiscriminatorFieldName(scala.None)
    }
  enum SslCompatibilityLevel:
    case `ssl_compatibility_level_unknown`
    case `ssl_compatibility_level_intermediate`
    case `ssl_compatibility_level_modern`
    case `ssl_compatibility_level_old`

  object SslCompatibilityLevel:
    import com.github.plokhotnyuk.jsoniter_scala.macros.*
    import com.github.plokhotnyuk.jsoniter_scala.core.*
    given sslCompatibilityLevelCodec: JsonValueCodec[SslCompatibilityLevel] = JsonCodecMaker.make {
      CodecMakerConfig
        .withAdtLeafClassNameMapper { x =>
          JsonCodecMaker.simpleClassName(x) match
            case "ssl_compatibility_level_unknown" => "ssl_compatibility_level_unknown"
            case "ssl_compatibility_level_intermediate" => "ssl_compatibility_level_intermediate"
            case "ssl_compatibility_level_modern" => "ssl_compatibility_level_modern"
            case "ssl_compatibility_level_old" => "ssl_compatibility_level_old"
        }
        .withDiscriminatorFieldName(scala.None)
    }
end ScalewayLbV1PrivateNetworkLbEnums
