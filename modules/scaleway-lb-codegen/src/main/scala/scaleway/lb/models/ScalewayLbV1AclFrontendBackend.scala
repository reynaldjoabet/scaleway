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
   * Backend object the frontend is attached to.
   */
case class ScalewayLbV1AclFrontendBackend(
  /* Backend ID. */
  @named("id") id: Option[String] = scala.None,
  /* Name of the backend. */
  @named("name") name: Option[String] = scala.None,
  /* Protocol used by the backend when forwarding traffic to backend servers. */
  @named("forward_protocol") forwardProtocol: Option[ScalewayLbV1AclFrontendBackendEnums.ForwardProtocol] = scala.None,
  /* Port used by the backend when forwarding traffic to backend servers. */
  @named("forward_port") forwardPort: Option[Int] = scala.None,
  /* Load balancing algorithm to use when determining which backend server to forward new traffic to. */
  @named("forward_port_algorithm") forwardPortAlgorithm: Option[ScalewayLbV1AclFrontendBackendEnums.ForwardPortAlgorithm] = scala.None,
  /* Defines whether sticky sessions (binding a particular session to a particular backend server) are activated and the method to use if so. None disables sticky sessions. Cookie-based uses an HTTP cookie to stick a session to a backend server. Table-based uses the source (client) IP address to stick a session to a backend server. */
  @named("sticky_sessions") stickySessions: Option[ScalewayLbV1AclFrontendBackendEnums.StickySessions] = scala.None,
  /* Cookie name for cookie-based sticky sessions. */
  @named("sticky_sessions_cookie_name") stickySessionsCookieName: Option[String] = scala.None,
  @named("health_check") healthCheck: Option[CreateBackendRequestHealthCheck] = scala.None,
  /* List of IP addresses of backend servers attached to this backend. */
  @named("pool") pool: Option[Seq[String]] = scala.None,
  @named("lb") lb: Option[ScalewayLbV1AclFrontendBackendLb] = scala.None,
  /* Deprecated in favor of proxy_protocol field. */
  @named("send_proxy_v2") sendProxyV2: Option[Boolean] = scala.None,
  /* Maximum allowed time for a backend server to process a request. (in milliseconds) */
  @named("timeout_server") timeoutServer: Option[Double] = scala.None,
  /* Maximum allowed time for establishing a connection to a backend server. (in milliseconds) */
  @named("timeout_connect") timeoutConnect: Option[Double] = scala.None,
  /* Maximum allowed tunnel inactivity time after Websocket is established (takes precedence over client and server timeout). (in milliseconds) */
  @named("timeout_tunnel") timeoutTunnel: Option[Double] = scala.None,
  /* Action to take when a backend server is marked as down. */
  @named("on_marked_down_action") onMarkedDownAction: Option[ScalewayLbV1AclFrontendBackendEnums.OnMarkedDownAction] = scala.None,
  /* Protocol to use between the Load Balancer and backend servers. Allows the backend servers to be informed of the client's real IP address. The PROXY protocol must be supported by the backend servers' software. */
  @named("proxy_protocol") proxyProtocol: Option[ScalewayLbV1AclFrontendBackendEnums.ProxyProtocol] = scala.None,
  /* Date at which the backend was created. (RFC 3339 format) */
  @named("created_at") createdAt: Option[OffsetDateTime] = scala.None,
  /* Date at which the backend was updated. (RFC 3339 format) */
  @named("updated_at") updatedAt: Option[OffsetDateTime] = scala.None,
  /* Scaleway Object Storage bucket website to be served as failover if all backend servers are down, e.g. failover-website.s3-website.fr-par.scw.cloud. */
  @named("failover_host") failoverHost: Option[String] = scala.None,
  /* Defines whether to enable SSL bridging between the Load Balancer and backend servers. */
  @named("ssl_bridging") sslBridging: Option[Boolean] = scala.None,
  /* Defines whether the server certificate verification should be ignored. */
  @named("ignore_ssl_server_verify") ignoreSslServerVerify: Option[Boolean] = scala.None,
  /* Whether to use another backend server on each attempt. */
  @named("redispatch_attempt_count") redispatchAttemptCount: Option[Int] = scala.None,
  /* Number of retries when a backend server connection failed. */
  @named("max_retries") maxRetries: Option[Int] = scala.None,
  /* Maximum number of connections allowed per backend server. */
  @named("max_connections") maxConnections: Option[Int] = scala.None,
  /* Maximum time for a request to be left pending in queue when `max_connections` is reached. (in seconds) */
  @named("timeout_queue") timeoutQueue: Option[String] = scala.None
)

object ScalewayLbV1AclFrontendBackendEnums:
  enum ForwardProtocol:
    case `tcp`
    case `http`

  object ForwardProtocol:
    import com.github.plokhotnyuk.jsoniter_scala.macros.*
    import com.github.plokhotnyuk.jsoniter_scala.core.*
    given forwardProtocolCodec: JsonValueCodec[ForwardProtocol] = JsonCodecMaker.make {
      CodecMakerConfig
        .withAdtLeafClassNameMapper { x =>
          JsonCodecMaker.simpleClassName(x) match
            case "tcp" => "tcp"
            case "http" => "http"
        }
        .withDiscriminatorFieldName(scala.None)
    }
  enum ForwardPortAlgorithm:
    case `roundrobin`
    case `leastconn`
    case `first`

  object ForwardPortAlgorithm:
    import com.github.plokhotnyuk.jsoniter_scala.macros.*
    import com.github.plokhotnyuk.jsoniter_scala.core.*
    given forwardPortAlgorithmCodec: JsonValueCodec[ForwardPortAlgorithm] = JsonCodecMaker.make {
      CodecMakerConfig
        .withAdtLeafClassNameMapper { x =>
          JsonCodecMaker.simpleClassName(x) match
            case "roundrobin" => "roundrobin"
            case "leastconn" => "leastconn"
            case "first" => "first"
        }
        .withDiscriminatorFieldName(scala.None)
    }
  enum StickySessions:
    case `none`
    case `cookie`
    case `table`

  object StickySessions:
    import com.github.plokhotnyuk.jsoniter_scala.macros.*
    import com.github.plokhotnyuk.jsoniter_scala.core.*
    given stickySessionsCodec: JsonValueCodec[StickySessions] = JsonCodecMaker.make {
      CodecMakerConfig
        .withAdtLeafClassNameMapper { x =>
          JsonCodecMaker.simpleClassName(x) match
            case "none" => "none"
            case "cookie" => "cookie"
            case "table" => "table"
        }
        .withDiscriminatorFieldName(scala.None)
    }
  enum OnMarkedDownAction:
    case `on_marked_down_action_none`
    case `shutdown_sessions`

  object OnMarkedDownAction:
    import com.github.plokhotnyuk.jsoniter_scala.macros.*
    import com.github.plokhotnyuk.jsoniter_scala.core.*
    given onMarkedDownActionCodec: JsonValueCodec[OnMarkedDownAction] = JsonCodecMaker.make {
      CodecMakerConfig
        .withAdtLeafClassNameMapper { x =>
          JsonCodecMaker.simpleClassName(x) match
            case "on_marked_down_action_none" => "on_marked_down_action_none"
            case "shutdown_sessions" => "shutdown_sessions"
        }
        .withDiscriminatorFieldName(scala.None)
    }
  enum ProxyProtocol:
    case `proxy_protocol_unknown`
    case `proxy_protocol_none`
    case `proxy_protocol_v1`
    case `proxy_protocol_v2`
    case `proxy_protocol_v2_ssl`
    case `proxy_protocol_v2_ssl_cn`

  object ProxyProtocol:
    import com.github.plokhotnyuk.jsoniter_scala.macros.*
    import com.github.plokhotnyuk.jsoniter_scala.core.*
    given proxyProtocolCodec: JsonValueCodec[ProxyProtocol] = JsonCodecMaker.make {
      CodecMakerConfig
        .withAdtLeafClassNameMapper { x =>
          JsonCodecMaker.simpleClassName(x) match
            case "proxy_protocol_unknown" => "proxy_protocol_unknown"
            case "proxy_protocol_none" => "proxy_protocol_none"
            case "proxy_protocol_v1" => "proxy_protocol_v1"
            case "proxy_protocol_v2" => "proxy_protocol_v2"
            case "proxy_protocol_v2_ssl" => "proxy_protocol_v2_ssl"
            case "proxy_protocol_v2_ssl_cn" => "proxy_protocol_v2_ssl_cn"
        }
        .withDiscriminatorFieldName(scala.None)
    }
end ScalewayLbV1AclFrontendBackendEnums
