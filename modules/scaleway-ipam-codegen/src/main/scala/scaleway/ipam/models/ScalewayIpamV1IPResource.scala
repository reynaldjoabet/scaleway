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
package scaleway.ipam.models

import com.github.plokhotnyuk.jsoniter_scala.macros.named

  /**
   * Resource which the IP is attached to.
   */
case class ScalewayIpamV1IPResource(
  /* Type of resource the IP is attached to. */
  @named("type") `type`: Option[ScalewayIpamV1IPResourceEnums.Type] = scala.None,
  /* ID of the resource the IP is attached to. (UUID format) */
  @named("id") id: Option[String] = scala.None,
  /* MAC of the resource the IP is attached to. */
  @named("mac_address") macAddress: Option[String] = scala.None,
  /* Name of the resource the IP is attached to. When the IP is in a Private Network, then a DNS record is available to resolve the resource name to this IP. */
  @named("name") name: Option[String] = scala.None
)

object ScalewayIpamV1IPResourceEnums:
  enum Type:
    case `unknown_type`
    case `custom`
    case `instance_server`
    case `instance_ip`
    case `instance_private_nic`
    case `lb_server`
    case `fip_ip`
    case `vpc_gateway`
    case `vpc_gateway_network`
    case `k8s_node`
    case `k8s_cluster`
    case `rdb_instance`
    case `redis_cluster`
    case `baremetal_server`
    case `baremetal_private_nic`
    case `llm_deployment`
    case `mgdb_instance`
    case `apple_silicon_server`
    case `apple_silicon_private_nic`
    case `serverless_container`
    case `serverless_function`
    case `vpn_gateway`
    case `ddl_datalab`
    case `kafka_cluster`
    case `bgp_endpoint`
    case `scbl_sedb_cluster`
    case `dtwh_deployment`
    case `sedb_cluster`
    case `msgq_cluster`

  object Type:
    import com.github.plokhotnyuk.jsoniter_scala.macros.*
    import com.github.plokhotnyuk.jsoniter_scala.core.*
    given typeCodec: JsonValueCodec[Type] = JsonCodecMaker.make {
      CodecMakerConfig
        .withAdtLeafClassNameMapper { x =>
          JsonCodecMaker.simpleClassName(x) match
            case "unknown_type" => "unknown_type"
            case "custom" => "custom"
            case "instance_server" => "instance_server"
            case "instance_ip" => "instance_ip"
            case "instance_private_nic" => "instance_private_nic"
            case "lb_server" => "lb_server"
            case "fip_ip" => "fip_ip"
            case "vpc_gateway" => "vpc_gateway"
            case "vpc_gateway_network" => "vpc_gateway_network"
            case "k8s_node" => "k8s_node"
            case "k8s_cluster" => "k8s_cluster"
            case "rdb_instance" => "rdb_instance"
            case "redis_cluster" => "redis_cluster"
            case "baremetal_server" => "baremetal_server"
            case "baremetal_private_nic" => "baremetal_private_nic"
            case "llm_deployment" => "llm_deployment"
            case "mgdb_instance" => "mgdb_instance"
            case "apple_silicon_server" => "apple_silicon_server"
            case "apple_silicon_private_nic" => "apple_silicon_private_nic"
            case "serverless_container" => "serverless_container"
            case "serverless_function" => "serverless_function"
            case "vpn_gateway" => "vpn_gateway"
            case "ddl_datalab" => "ddl_datalab"
            case "kafka_cluster" => "kafka_cluster"
            case "bgp_endpoint" => "bgp_endpoint"
            case "scbl_sedb_cluster" => "scbl_sedb_cluster"
            case "dtwh_deployment" => "dtwh_deployment"
            case "sedb_cluster" => "sedb_cluster"
            case "msgq_cluster" => "msgq_cluster"
        }
        .withDiscriminatorFieldName(scala.None)
    }
end ScalewayIpamV1IPResourceEnums
