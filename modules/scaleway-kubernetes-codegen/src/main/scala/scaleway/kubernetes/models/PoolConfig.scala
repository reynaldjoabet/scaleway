/**
 * Kubernetes API
 * Kubernetes is an open-source platform that enables developers to manage their containerized applications. Scaleway Kubernetes Kapsule and Kosmos are powerful tools to help you manage your containerized workloads and services.  They both provide a managed environment for creating, configuring, and running clusters of pre-configured machines.  The primary difference between Kapsule and Kosmos is that Kapsule clusters are composed solely of Scaleway Instances. In contrast, Kosmos is a managed Multi-Cloud Kubernetes Engine that allows you to connect Instances and virtual or dedicated servers from any cloud provider to a single managed Control-Plane.  ## Concepts  Refer to our [dedicated concepts page](https://www.scaleway.com/en/docs/kubernetes/concepts/) to find definitions of all Kubetnetes-related terminology.  ## Quickstart     1. Configure your environment variables. Note: This is an optional step that seeks to simplify your usage of the Kapsule and Kosmos API.     ```bash     export SCW_ACCESS_KEY=\"<API access key>\"     export SCW_SECRET_KEY=\"<API secret key>\"     export SCW_REGION=\"<Scaleway service region>\"     export SCW_PROJECT_ID=\"<Scaleway Project ID>\"     export SCW_PRIVATE_NETWORK_ID=\"<Scaleway Private Network ID>\"     ```  2. Edit the POST request payload you will use to create your Kubernetes cluster. Replace the parameters in the following example:     ```json     {       \"project_id\": \"$SCW_PROJECT_ID\",       \"private_network_id\": \"$SCW_PRIVATE_NETWORK_ID\",       \"type\": \"string\",       \"name\": \"string\",       \"description\": \"string\",       \"tags\": [         \"string\"       ],       \"version\": \"string\",       \"cni\": \"unknown_cni\",       \"pools\": [         {           \"name\": \"string\",           \"node_type\": \"string\",           \"size\": \"integer\",           \"tags\": [             \"string\"           ],           \"zone\": \"string\",           \"root_volume_type\": \"default_volume_type\",           \"root_volume_size\": \"integer\"         }       ]     }     ```      | Parameter                | Description                                                        |     | :----------------------- | :----------------------------------------------------------------- |     | `project_id`             | The ID of the Project you want to create your Kubernetes cluster in. To find your Project ID you can consult the [Scaleway console](https://console.scaleway.com/project/settings). |     | `type`                   | The type of the cluster (possible values are `kapsule`, `multicloud`, `kapsule-dedicated-X`, `multicloud-dedicated-X` - `X` can be `4`, `8` or `16`). |     | `name`                   | **REQUIRED** Name of the cluster. |     | `description`            | Description of the cluster. |     | `tags`                   | Tags associated with the cluster. |     | `version`                | **REQUIRED** Kubernetes version of the cluster. |     | `cni`                    | **REQUIRED** Container Network Interface (CNI) plugin that will run on the cluster. The default value is `unknown_cni`. (possible values are `cilium`, `cilium_native`, `calico` for `kapsule` and `kilo` for `multicloud`) |     | `pools`                  | Pools to be created along with the cluster. |     | `pools.name`             | **REQUIRED** Name of the pool. |     | `pools.node-type`        | **REQUIRED** The node type of the Scaleway Instance wanted for the pool. |     | `pools.size`             | **REQUIRED** The number of nodes in the pool. |     | `pools.tags`             | Tags associated with the pool. See [managing tags](https://www.scaleway.com/en/docs/kubernetes/api-cli/managing-tags). |     | `pools.zone`             | Availability zone in which the pools will be deployed in. |     | `pools.root_volume_type` | The root volume type. The default value is `default_volume_type`. |     | `pools.root_volume_size` | The system volume disk size in bytes. |     | `private_network_id`     | Private network ID for internal cluster communication. |  3. Create a Kapsule cluster and node pool by running the following command. Make sure you include the payload you edited in the previous step.     ```bash     curl -X POST \\       -H \"X-Auth-Token: $SCW_SECRET_KEY\" \\       \"Content-Type: application/json\" \\       \"https://api.scaleway.com/k8s/v1/regions/$SCW_REGION/clusters\" \\       -d \"{             \\\"project_id\\\": \\\"$SCW_PROJECT_ID\\\",             \\\"private_network_id\\\": \\\"$SCW_PRIVATE_NETWORK_ID\\\",             \\\"type\\\": \\\"kapsule\\\",             \\\"name\\\": \\\"MyFirstKapsuleCluster\\\",             \\\"description\\\": \\\"My first Kapsule Cluster\\\",             \\\"tags\\\": [               \\\"kapsule\\\",               \\\"kubernetes\\\"             ],             \\\"version\\\": \\\"1.31.2\\\",             \\\"cni\\\": \\\"unknown_cni\\\",             \\\"pools\\\": [               {                 \\\"name\\\": \\\"MyFirstKapsulePool\\\",                 \\\"node_type\\\": \\\"PLAY2-MICRO\\\",                 \\\"size\\\": \\\"2\\\",                 \\\"tags\\\": [                   \\\"pool\\\"                 ],                 \\\"zone\\\": \\\"fr-par-1\\\",                 \\\"root_volume_type\\\": \\\"default_volume_type\\\",                 \\\"root_volume_size\\\": \\\"20000000000\\\"               }             ]           }\"     ```  4. List your Kapsule clusters.     ```bash     curl -X GET \\       -H \"Content-Type: application/json\" \\       -H \"X-Auth-Token: $SCW_SECRET_KEY\" \\       https://api.scaleway.com/k8s/v1/regions/$SCW_REGION/clusters     ```      You will see detailed information about your clusters.  5. Download the kubeconfig file for your cluster.     ```bash     curl -X GET \\       -H \"Content-Type: application/json\" \\       -H \"X-Auth-Token: $SCW_SECRET_KEY\" \\       https://api.scaleway.com/k8s/v1/regions/$SCW_REGION/clusters/$SCW_CLUSTER_ID/kubeconfig     ```     <Message type=\"tip\">       Add `?dl=1` at the end of the URL to directly get the `base64`-decoded kubeconfig. If not, the kubeconfig will be `base64`-encoded.     </Message>  6. [Connect to your cluster](https://www.scaleway.com/en/docs/kubernetes/how-to/connect-cluster-kubectl) using kubectl.  7. Delete your cluster.     ```bash     curl -X GET -H \"Content-Type: application/json\" \\       -H \"X-Auth-Token: $SCW_SECRET_KEY\" https://api.scaleway.com/k8s/v1/regions/$SCW_REGION/clusters/$SCW_CLUSTER_ID     ```   <Message type=\"requirement\"> To perform the following steps, you must first ensure that:   - you have an account and are logged into the [Scaleway console](https://console.scaleway.com/organization)   - you have created an [API key](https://www.scaleway.com/en/docs/iam/how-to/create-api-keys/) and that the API key has sufficient [IAM permissions](https://www.scaleway.com/en/docs/iam/reference-content/permission-sets/) to perform the actions described on this page.   - you have [installed `curl`](https://curl.se/download.html) </Message>   ## Technical information  Kubernetes Kapsule provides features such as: - Persistent Volume Claims (PVC) are available through Scaleway Block Volumes. - Pool autoscaling and autohealing is available. - Kubernetes auto upgrades features is available.  ### Regions  Scaleway's infrastructure is spread across different [regions and Availability Zones](https://www.scaleway.com/en/docs/account/reference-content/products-availability/).  Kubernetes Kapsule and Kosmos are available in the Paris, Amsterdam and Warsaw regions, which are represented by the following path parameters:  - `fr-par` - `nl-ams` - `pl-waw`  ### Versions  Kubernetes Kosmos and Kapsule supports at least the latest version of the last 3 major Kubernetes releases.  ## Technical limitations  The following limitations should be taken into account when using the Kubernetes API:  - The maximum number of pods per node is 110, as per the official k8s project recommendation (configurable). - The maximum number of volumes (PVC) per node is 15. - The maximum number of nodes per cluster varies from 150 to up to 500 (depending on the control plane tier). - [VPC Limitations](https://www.scaleway.com/en/docs/vpc/troubleshooting/vpc-limitations/) may restrict the number of nodes and loadbalancers. - Private network must not conflict with the 198.18.0.0/15 subnet. - Users cannot expose port 53 (DNS) to `hostPort` or in `hostNetwork` mode. - Security groups set to drop all inbound must have stateful group enabled. - Security groups must have ports `80` and `443` left open in outbound. - For Kosmos clusters **or** if ACLs are not activated (no network tab in console), ports `8132` and `6443` must also be opened. - Kilo CNI (kosmos nodes) also needs UDP port `51820` to be open.  The following limitations should be acknowledged, while Scaleway is actively working on planned solutions to address them:  - Dual Stack IPv4/IPv6 is not (yet) available. - Read Write Many / Read Only Many are not (yet) available. - Kubernetes control plane network access is managed by a Load Balancer located on `ZONE-1`. In the event of a global failure for this AZ, the Control Plane will be unreachable.  The following limitations should be taken into account when setting custom settings for Pod & Service CIDRs and DNS IP:  - Setting these may reduce the number of nodes and pods per nodes the cluster can handle. - The Pod and Service CIDRs must not conflict between them. - The Pod & Service CIDRs must not conflict with the Private Networks' subnets of the VPC attached to the cluster. - The Pod & Service CIDRs must not conflict with the routes set in the VPC attached to the cluster. - The Pod & Service CIDRs subnets must be part of the RFC 1918 and RFC 6598 (subnet included in: 10.0.0.0/8 or 172.16.0.0/12 or 192.168.0.0/16 or 100.64.0.0/10). - Mask /16 minimum for the Pod CIDR (not above 16) - Mask /24 minimum for the Service CIDR (not above 24) - The Service DNS IP must be included in the Service CIDR  ## Going further  For more help using Kubernetes Kapsule and Kosmos, check out the following resources: - Our [main documentation](https://www.scaleway.com/en/docs/kubernetes/) - The `#k8s` channel on our [Slack Community](https://www.scaleway.com/en/docs/tutorials/scaleway-slack-community/) - Our [support ticketing system](https://www.scaleway.com/en/docs/account/how-to/open-a-support-ticket/).
 *
 * The version of the OpenAPI document: v1
 * 
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */
package scaleway.kubernetes.models

import com.github.plokhotnyuk.jsoniter_scala.macros.named

case class PoolConfig(
  /* Name of the pool. */
  @named("name") name: String,
  /* Node type is the type of Scaleway Instance wanted for the pool. Nodes with insufficient memory are not eligible (DEV1-S, PLAY2-PICO, STARDUST). 'external' is a special node type used to provision instances from other cloud providers in a Kosmos Cluster. */
  @named("node_type") nodeType: String,
  /* Size (number of nodes) of the pool. */
  @named("size") size: Int,
  /* Placement group ID in which all the nodes of the pool will be created, placement groups are limited to 20 instances. */
  @named("placement_group_id") placementGroupId: Option[String] = scala.None,
  /* Defines whether the autoscaling feature is enabled for the pool. */
  @named("autoscaling") autoscaling: Option[Boolean] = scala.None,
  /* Defines the minimum size of the pool. Note that this field is only used when autoscaling is enabled on the pool. */
  @named("min_size") minSize: Option[Int] = scala.None,
  /* Defines the maximum size of the pool. Note that this field is only used when autoscaling is enabled on the pool. */
  @named("max_size") maxSize: Option[Int] = scala.None,
  /* Customization of the container runtime is available for each pool. */
  @named("container_runtime") containerRuntime: Option[PoolConfigEnums.ContainerRuntime] = scala.None,
  /* Defines whether the autohealing feature is enabled for the pool. */
  @named("autohealing") autohealing: Option[Boolean] = scala.None,
  /* Tags associated with the pool, see [managing tags](https://www.scaleway.com/en/docs/kubernetes/api-cli/managing-tags). */
  @named("tags") tags: Option[Seq[String]] = scala.None,
  @named("kubelet_args") kubeletArgs: Option[CreatePoolRequestKubeletArgs] = scala.None,
  @named("upgrade_policy") upgradePolicy: Option[CreatePoolRequestUpgradePolicy] = scala.None,
  /* Zone in which the pool's nodes will be spawned. */
  @named("zone") zone: Option[String] = scala.None,
  /* Defines the system volume disk type. Several types of volume (`volume_type`) are provided:. * `l_ssd` is a local block storage which means your system is stored locally on your node's hypervisor. This type is not available for all node types * `sbs_5k` is a remote block storage which means your system is stored on a centralized and resilient cluster with 5k IOPS limits * `sbs_15k` is a faster remote block storage which means your system is stored on a centralized and resilient cluster with 15k IOPS limits * `b_ssd` is the legacy remote block storage which means your system is stored on a centralized and resilient cluster. Not available for new pools, use `sbs_5k` or `sbs_15k` instead. */
  @named("root_volume_type") rootVolumeType: Option[PoolConfigEnums.RootVolumeType] = scala.None,
  /* System volume disk size. (in bytes) */
  @named("root_volume_size") rootVolumeSize: Option[Int] = scala.None,
  /* Defines if the public IP should be removed from Nodes. To use this feature, your Cluster must have an attached Private Network set up with a Public Gateway. */
  @named("public_ip_disabled") publicIpDisabled: Option[Boolean] = scala.None,
  /* Security group ID in which all the nodes of the pool will be created. If unset, the pool will use default Kapsule security group in current zone. */
  @named("security_group_id") securityGroupId: Option[String] = scala.None,
  @named("labels") labels: Option[CreatePoolRequestLabels] = scala.None,
  /* Kubernetes taints applied and reconciled on the nodes. */
  @named("taints") taints: Option[Seq[CoreV1Taint]] = scala.None,
  /* Kubernetes taints applied at node creation but not reconciled afterwards. */
  @named("startup_taints") startupTaints: Option[Seq[CoreV1Taint]] = scala.None
)

object PoolConfigEnums:
  enum ContainerRuntime:
    case `unknown_runtime`
    case `docker`
    case `containerd`
    case `crio`

  object ContainerRuntime:
    import com.github.plokhotnyuk.jsoniter_scala.macros.*
    import com.github.plokhotnyuk.jsoniter_scala.core.*
    given containerRuntimeCodec: JsonValueCodec[ContainerRuntime] = JsonCodecMaker.make {
      CodecMakerConfig
        .withAdtLeafClassNameMapper { x =>
          JsonCodecMaker.simpleClassName(x) match
            case "unknown_runtime" => "unknown_runtime"
            case "docker" => "docker"
            case "containerd" => "containerd"
            case "crio" => "crio"
        }
        .withDiscriminatorFieldName(scala.None)
    }
  enum RootVolumeType:
    case `default_volume_type`
    case `l_ssd`
    case `b_ssd`
    case `sbs_5k`
    case `sbs_15k`

  object RootVolumeType:
    import com.github.plokhotnyuk.jsoniter_scala.macros.*
    import com.github.plokhotnyuk.jsoniter_scala.core.*
    given rootVolumeTypeCodec: JsonValueCodec[RootVolumeType] = JsonCodecMaker.make {
      CodecMakerConfig
        .withAdtLeafClassNameMapper { x =>
          JsonCodecMaker.simpleClassName(x) match
            case "default_volume_type" => "default_volume_type"
            case "l_ssd" => "l_ssd"
            case "b_ssd" => "b_ssd"
            case "sbs_5k" => "sbs_5k"
            case "sbs_15k" => "sbs_15k"
        }
        .withDiscriminatorFieldName(scala.None)
    }
end PoolConfigEnums
