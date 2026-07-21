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
package scaleway.instance.models

import java.time.OffsetDateTime
import com.github.plokhotnyuk.jsoniter_scala.macros.named

case class SecurityGroup(
  /* Security group unique ID. */
  @named("id") id: Option[String] = scala.None,
  /* Security group name. */
  @named("name") name: Option[String] = scala.None,
  /* Security group description. */
  @named("description") description: Option[String] = scala.None,
  /* True if SMTP is blocked on IPv4 and IPv6. This feature is read only, please open a support ticket if you need to make it configurable. */
  @named("enable_default_security") enableDefaultSecurity: Option[Boolean] = scala.None,
  /* Default inbound policy. */
  @named("inbound_default_policy") inboundDefaultPolicy: Option[SecurityGroupEnums.InboundDefaultPolicy] = scala.None,
  /* Default outbound policy. */
  @named("outbound_default_policy") outboundDefaultPolicy: Option[SecurityGroupEnums.OutboundDefaultPolicy] = scala.None,
  /* Security group Organization ID. */
  @named("organization") organization: Option[String] = scala.None,
  /* Security group Project ID. */
  @named("project") project: Option[String] = scala.None,
  /* Security group tags. */
  @named("tags") tags: Option[Seq[String]] = scala.None,
  /* True if it is your default security group for this Organization ID. */
  @named("organization_default") organizationDefault: Option[Boolean] = scala.None,
  /* True if it is your default security group for this Project ID. */
  @named("project_default") projectDefault: Option[Boolean] = scala.None,
  /* Security group creation date. (RFC 3339 format) */
  @named("creation_date") creationDate: Option[OffsetDateTime] = scala.None,
  /* Security group modification date. (RFC 3339 format) */
  @named("modification_date") modificationDate: Option[OffsetDateTime] = scala.None,
  /* List of Instances attached to this security group. */
  @named("servers") servers: Option[Seq[ServerSummary]] = scala.None,
  /* Defines whether the security group is stateful. */
  @named("stateful") stateful: Option[Boolean] = scala.None,
  /* Security group state. */
  @named("state") state: Option[SecurityGroupEnums.State] = scala.None,
  /* Zone in which the security group is located. */
  @named("zone") zone: Option[String] = scala.None
)

object SecurityGroupEnums:
  enum InboundDefaultPolicy:
    case `unknown_policy`
    case `accept`
    case `drop`

  object InboundDefaultPolicy:
    import com.github.plokhotnyuk.jsoniter_scala.macros.*
    import com.github.plokhotnyuk.jsoniter_scala.core.*
    given inboundDefaultPolicyCodec: JsonValueCodec[InboundDefaultPolicy] = JsonCodecMaker.make {
      CodecMakerConfig
        .withAdtLeafClassNameMapper { x =>
          JsonCodecMaker.simpleClassName(x) match
            case "unknown_policy" => "unknown_policy"
            case "accept" => "accept"
            case "drop" => "drop"
        }
        .withDiscriminatorFieldName(scala.None)
    }
  enum OutboundDefaultPolicy:
    case `unknown_policy`
    case `accept`
    case `drop`

  object OutboundDefaultPolicy:
    import com.github.plokhotnyuk.jsoniter_scala.macros.*
    import com.github.plokhotnyuk.jsoniter_scala.core.*
    given outboundDefaultPolicyCodec: JsonValueCodec[OutboundDefaultPolicy] = JsonCodecMaker.make {
      CodecMakerConfig
        .withAdtLeafClassNameMapper { x =>
          JsonCodecMaker.simpleClassName(x) match
            case "unknown_policy" => "unknown_policy"
            case "accept" => "accept"
            case "drop" => "drop"
        }
        .withDiscriminatorFieldName(scala.None)
    }
  enum State:
    case `available`
    case `syncing`
    case `syncing_error`

  object State:
    import com.github.plokhotnyuk.jsoniter_scala.macros.*
    import com.github.plokhotnyuk.jsoniter_scala.core.*
    given stateCodec: JsonValueCodec[State] = JsonCodecMaker.make {
      CodecMakerConfig
        .withAdtLeafClassNameMapper { x =>
          JsonCodecMaker.simpleClassName(x) match
            case "available" => "available"
            case "syncing" => "syncing"
            case "syncing_error" => "syncing_error"
        }
        .withDiscriminatorFieldName(scala.None)
    }
end SecurityGroupEnums
