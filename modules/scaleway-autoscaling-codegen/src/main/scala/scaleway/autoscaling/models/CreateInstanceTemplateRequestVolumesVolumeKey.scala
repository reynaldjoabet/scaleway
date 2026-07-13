/** Instance Scaling Groups API Instance Scaling Groups dynamically adjust their number of Instances based on defined
  * scaling policies. Scaling actions (scale up or down) are triggered when the monitored metric exceeds the configured
  * thresholds from your policies. Instance Scaling Groups rely on the Cockpit product to gather the Instance metrics,
  * such as RAM or bandwidth usage. The Load Balancer of your choice, which handles traffic for the given Instance
  * group, is automatically configured according to the scaling action so that connections are properly distributed
  * among the set of Instances. The purpose of this product is to maintain optimal application performance and cost
  * efficiency by scaling your Instance group up during peak traffic and scaling it down when demand decreases. This
  * ensures that applications have the necessary resources to handle varying loads without manual intervention,
  * providing high availability and fault tolerance. Instance Scaling Groups are particularly useful for dynamic
  * workloads, enabling businesses to optimize resource usage and reduce operational overhead. <Message type=\"note\">
  * This product is currently in [Public Beta](https://www.scaleway.com/en/betas/). </Message> ## Quickstart 1.
  * Configure your environment variables. <Message type=\"note\"> This is an optional step that seeks to simplify your
  * usage of the Instance Scaling Groups API. </Message> ```bash     export SCW_SECRET_KEY=\"<API secret key>\"     export SCW_DEFAULT_ZONE=\"<Scaleway default Availability Zone>\"     export SCW_PROJECT_ID=\"<Scaleway Project ID>\"     ``` 2.
  * **Create an Instance Template**: run the following command to create an Instance template. The created template will
  * be used by the group during scaling to start new Instances, according to the given settings. ```bash     curl -X POST \\       -H \"X-Auth-Token: $SCW_SECRET_KEY\" \\       -H \"Content-Type: application/json\" \\       \"https://api.scaleway.com/autoscaling/v1alpha1/zones/$SCW_DEFAULT_ZONE/instance-templates\" \\       -d '{         \"name\": \"my_template\",         \"commercial_type\": \"PLAY2-NANO\",         \"volumes\": {             \"0\": {                 \"name\": \"boot volume\",                 \"from_snapshot\": {                     \"snapshot_id\": \"<SNAPSHOT_ID>\"                 },                 \"volume_type\": \"sbs\",                 \"boot\": true             }         },         \"private_network_ids\": [             \"<PRIVATE_NETWORK_ID>\"         ],         \"project_id\": \"'\"$SCW_PROJECT_ID\"'\"       }'     ``` 3.
  * **Create an Instance Group**: run the following command to create an Instance group. You must have already
  * configured a Load Balancer, so that the Instance group can update its backend servers list during scaling actions.
  * You can specify the Private Network to be used for traffic between your Load Balancer and the Instances. If
  * specified, the given Private Network must be already attached to the Load Balancer. ```bash     curl -X POST \\       -H \"X-Auth-Token: $SCW_SECRET_KEY\" \\       -H \"Content-Type: application/json\" \\       \"https://api.scaleway.com/autoscaling/v1alpha1/zones/$SCW_DEFAULT_ZONE/instance-groups\" \\       -d '{         \"project_id\": \"'\"$SCW_PROJECT_ID\"'\",         \"name\": \"my_instance_group\",         \"template_id\": \"<AUTOSCALING_TEMPLATE_ID>\",         \"capacity\": {           \"max_replicas\": 5,           \"min_replicas\": 1,           \"cooldown_delay\": \"300s\"         },         \"loadbalancer\":{             \"id\": \"<LOAD_BALANCER_ID>\",             \"backend_ids\": [\"<BACKEND_ID>\"],             \"private_network_id\": \"<PRIVATE_NETWORK_ID>\"         }       }'     ``` 3.
  * **Create Scaling Policies**: run the following command to create scaling policies. Scaling policies are a set of
  * rules which define criteria for scaling actions. Create at least 2 policies for both scaling up and down actions. ```bash     curl -X POST \\       -H \"X-Auth-Token: $SCW_SECRET_KEY\" \\       -H \"Content-Type: application/json\" \\       \"https://api.scaleway.com/autoscaling/v1alpha1/zones/$SCW_DEFAULT_ZONE/instance-policies\" \\       -d '{         \"name\": \"my-policy-scale-up\",         \"metric\": {           \"name\": \"cpu scale up\",           \"managed_metric\": \"managed_metric_instance_cpu\",           \"operator\": \"operator_greater_than\",           \"aggregate\": \"aggregate_average\",           \"sampling_range_min\": 5,           \"threshold\": 70         },         \"action\": \"scale_up\",         \"type\": \"flat_count\",         \"value\": 1,         \"priority\": 1,         \"instance_group_id\": \"<AUTOSCALING_INSTANCE_GROUP_ID>\"       }'      curl -X POST \\       -H \"X-Auth-Token: $SCW_SECRET_KEY\" \\       -H \"Content-Type: application/json\" \\       \"https://api.scaleway.com/autoscaling/v1alpha1/zones/$SCW_DEFAULT_ZONE/instance-policies\" \\       -d '{         \"name\": \"my-policy-scale-down\",         \"metric\": {           \"name\": \"cpu scale down\",           \"managed_metric\": \"managed_metric_instance_cpu\",           \"operator\": \"operator_less_than\",           \"aggregate\": \"aggregate_average\",           \"sampling_range_min\": 5,           \"threshold\": 40         },         \"action\": \"scale_down\",         \"type\": \"flat_count\",         \"value\": 1,         \"priority\": 2,         \"instance_group_id\": \"<AUTOSCALING_INSTANCE_GROUP_ID>\"       }'     ``` 3.
  * **Get a list of scaling events**: run the following command to get a list of all the scaling events which occurred
  * for the given Instance group: ```bash     curl -X GET \\       -H \"Content-Type: application/json\" \\       -H \"X-Auth-Token: $SCW_SECRET_KEY\" \\       \"https://api.scaleway.com/autoscaling/v1alpha1/zones/$SCW_DEFAULT_ZONE/instance-groups/<INSTANCE_GROUP_ID>/events\"     ``` 4.
  * **Delete your Instance Group**: run the following command to delete an Instance group. Ensure that you replace
  * `<INSTANCE_GROUP_ID>` in the URL with the ID of the Instance group you want to delete. ```bash     curl -X DELETE \\       -H \"Content-Type: application/json\" \\       -H \"X-Auth-Token: $SCW_SECRET_KEY\" \\       \"https://api.scaleway.com/autoscaling/v1alpha1/zones/$SCW_DEFAULT_ZONE/instance-groups/<INSTANCE_GROUP_ID>\"     ```
  * <Message type=\"requirement\"> - You have a [Scaleway account](https://console.scaleway.com/) - You have created an
  * [API key](https://www.scaleway.com/en/docs/iam/how-to/create-api-keys/) and that the API key has sufficient [IAM
  * permissions](https://www.scaleway.com/en/docs/iam/reference-content/permission-sets/) to perform the actions
  * described on this page - You have [installed `curl`](https://curl.se/download.html) </Message> ## Technical
  * information Instance Scaling Groups rely on the [Cockpit](https://www.scaleway.com/en/docs/cockpit/) product to
  * gather metrics and take scaling actions based on defined policies. Scaling can thus occur every 5 minutes, which is
  * the time to re-fetch metrics from Cockpit after a previous iteration. ### Availability Zones The Scaleway Instance
  * Scaling Groups API is a **zoned** API, meaning that each call must specify in its path parameters the Availability
  * Zone for the resources concerned by the call. The following Availability Zones are available for Instance Scaling
  * Groups: | Name | API ID | |-----------|----------------------------------| | Paris | `fr-par-1` `fr-par-2` | ##
  * Technical limitations - When you update an Instance template, changes are not replicated to existing Instances. If
  * you need to refresh all existing Instances so that they comply with the new template, you must terminate them so
  * that the group can create new Instances based on the updated template. - Custom metrics are not supported yet. You
  * can only set scaling policies according to pre-defined \"managed metrics\". - Instance Scaling Groups do not handle
  * the creation of Load Balancers and their associated backends. Only backend server updates for the given existing
  * backend are managed by the group. - Instance Scaling Groups rely on the Instance status to determine whether it is
  * healthy or not, not on the Load Balancer health check mechanism. ## Going further For more help using Scaleway
  * Instance Scaling Groups, check out the following resources: - Our [Slack
  * Community](https://www.scaleway.com/en/docs/tutorials/scaleway-slack-community/) - Our [support ticketing
  * system](https://www.scaleway.com/en/docs/account/how-to/open-a-support-ticket/).
  *
  * The version of the OpenAPI document: v1alpha1
  *
  * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
  * https://openapi-generator.tech Do not edit the class manually.
  */
package scaleway.autoscaling.models

import com.github.plokhotnyuk.jsoniter_scala.macros.named

/** Template of Instance volume.
  */
case class CreateInstanceTemplateRequestVolumesVolumeKey(
    /* Name of the volume. */
    @named("name") name: String,
    /* The maximum IO/s expected, according to the different options available in stock (`5000 | 15000`). */
    @named("perf_iops") perfIops: Option[Int] = scala.None,
    @named("from_empty") fromEmpty: Option[CreateInstanceTemplateRequestVolumesVolumeKeyFromEmpty] = scala.None,
    @named("from_snapshot") fromSnapshot: Option[CreateInstanceTemplateRequestVolumesVolumeKeyFromSnapshot] =
      scala.None,
    /* List of tags assigned to the volume. */
    @named("tags") tags: Option[Seq[String]] = scala.None,
    /* Force the Instance to boot on this volume. */
    @named("boot") boot: Option[Boolean] = scala.None,
    /* Type of the volume. */
    @named("volume_type") volumeType: Option[CreateInstanceTemplateRequestVolumesVolumeKeyEnums.VolumeType] = scala.None
)

object CreateInstanceTemplateRequestVolumesVolumeKeyEnums:
  enum VolumeType:
    case `unknown_volume_type`
    case `l_ssd`
    case `sbs`

  object VolumeType:
    import com.github.plokhotnyuk.jsoniter_scala.macros.*
    import com.github.plokhotnyuk.jsoniter_scala.core.*
    given volumeTypeCodec: JsonValueCodec[VolumeType] = JsonCodecMaker.make {
      CodecMakerConfig
        .withAdtLeafClassNameMapper { x =>
          JsonCodecMaker.simpleClassName(x) match
            case "unknown_volume_type" => "unknown_volume_type"
            case "l_ssd"               => "l_ssd"
            case "sbs"                 => "sbs"
        }
        .withDiscriminatorFieldName(scala.None)
    }
end CreateInstanceTemplateRequestVolumesVolumeKeyEnums
