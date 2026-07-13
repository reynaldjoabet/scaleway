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
package scaleway.autoscaling

import scala.deriving.*
import scala.compiletime.*
import java.io.File
import java.util.UUID
import java.time.{LocalDate, OffsetDateTime}
import java.time.format.DateTimeFormatter
import com.github.plokhotnyuk.jsoniter_scala.core.{JsonValueCodec, writeToString}

type Primitive = String | Short | Int | Long | Float | Double | BigDecimal | Boolean | UUID | LocalDate | OffsetDateTime

enum Authorization:
  case NoAuthorization
  case BasicAuth(username: String, password: String)
  case ApiKey(apiKey: String)
  case BearerToken(token: String)

enum ApiKeyLocation:
  case HEADER
  case COOKIE
  case QUERY
  case NOAPIKEY

enum FormStyleFormat:
  case FORM
  case SPACEDELIMITED
  case PIPEDELIMITED
  case DEEPOBJECT

enum PathStyleFormat:
  case SIMPLE
  case LABEL
  case MATRIX

inline def allLabels[T <: Tuple]: List[String] =
  constValueTuple[T].toList.asInstanceOf[List[String]]

private inline def checkFields[T <: Tuple]: Unit =
  inline erasedValue[T] match {
    case _: EmptyTuple => ()
    case _: (t *: ts)  =>
      inline erasedValue[t] match
        case _: Primitive         => checkFields[ts]
        case _: Option[Primitive] => checkFields[ts]
        case _                    => error("Cannot derive structure, structure must consist only of primitive fields")
  }

extension (p: Primitive)
  def asString: String = p match
    case v: OffsetDateTime => DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(v)
    case v: LocalDate      => DateTimeFormatter.ISO_LOCAL_DATE.format(v)
    case _                 => p.toString

private val flattenKeyVals: Primitive | Option[Primitive] => Option[Primitive] = {
  case p: Primitive           => Some(p)
  case opt: Option[Primitive] => opt
}

trait FormSerializable[T]:
  inline def serialize(
      name: String,
      obj: T,
      inline format: FormStyleFormat = FormStyleFormat.FORM,
      inline explode: Boolean = true
  ): Seq[(String, String)]

object FormSerializable:
  inline def serialize[T](
      name: String,
      obj: T,
      inline format: FormStyleFormat = FormStyleFormat.FORM,
      inline explode: Boolean = true
  ): Seq[(String, String)] =
    summonFrom {
      case t: FormSerializable[T] => t.serialize(name, obj, format, explode)
      case _                      =>
        inline obj match
          case primitive: Primitive =>
            serializePrimitive(name, primitive, format, explode)
          case array: Seq[Primitive] =>
            serializeArray(name, array, format, explode)
          case optPrimitive: Option[Primitive] =>
            optPrimitive
              .map(value => serializePrimitive(name, value, format, explode))
              .getOrElse(Seq.empty[(String, String)])
          case optArray: Option[Seq[Primitive]] =>
            optArray
              .map(serializeArray(name, _, format, explode))
              .getOrElse(Seq.empty[(String, String)])
          case enumArray: Seq[t] =>
            inline summonInline[Mirror.Of[t]] match
              case mirror: Mirror.SumOf[t] =>
                serializeArray(
                  name,
                  enumArray.map(v =>
                    writeToString(v)(summonInline[JsonValueCodec[mirror.MirroredMonoType]])
                      .stripPrefix("\"")
                      .stripSuffix("\"")
                  ),
                  format,
                  explode
                )
              case _ =>
                error("Arrays of non-primitive types are only supported for enums")
          case optEnumArray: Option[Seq[t]] =>
            inline summonInline[Mirror.Of[t]] match
              case mirror: Mirror.SumOf[t] =>
                optEnumArray
                  .map(seq =>
                    serializeArray(
                      name,
                      seq.map(v =>
                        writeToString(v)(summonInline[JsonValueCodec[mirror.MirroredMonoType]])
                          .stripPrefix("\"")
                          .stripSuffix("\"")
                      ),
                      format,
                      explode
                    )
                  )
                  .getOrElse(Seq.empty[(String, String)])
              case _ =>
                error("Arrays of non-primitive types are only supported for enums")
          case freeObj: Map[String, Primitive] =>
            freeObj.map((key, value) => (key, value.asString)).toSeq
          case optObj: Option[t] =>
            inline summonInline[Mirror.Of[t]] match
              case mirror: Mirror.ProductOf[t] =>
                checkFields[mirror.MirroredElemTypes]
                val labels = allLabels[mirror.MirroredElemLabels]
                optObj
                  .map { obj =>
                    val keyVals = labels
                      .zip(
                        obj
                          .asInstanceOf[Product]
                          .productIterator
                          .toSeq
                          .asInstanceOf[Seq[Primitive | Option[Primitive]]]
                          .map(flattenKeyVals)
                      )
                      .filter((_, v) => v.isDefined)
                      .map((k, v) => (k, v.get))
                    serializeModel(name, keyVals, format, explode)
                  }
                  .getOrElse(Seq.empty[(String, String)])
              case mirror: Mirror.SumOf[t] =>
                optObj.map(v => (name, writeToString(v)(summonInline[JsonValueCodec[mirror.MirroredMonoType]]))).toSeq
          case obj =>
            inline summonInline[Mirror.Of[T]] match
              case _: Mirror.SumOf[T] =>
                Seq((name, writeToString(obj)(summonInline[JsonValueCodec[T]])))
              case mirror: Mirror.ProductOf[T] =>
                checkFields[mirror.MirroredElemTypes] // Stripe ma IDGAF bo używają deepObject np. tak lines[0][tax_amounts][0][amount] - mimo tego że spec na to nie pozwala
                val labels = allLabels[mirror.MirroredElemLabels]
                val keyVals = labels
                  .zip(
                    obj
                      .asInstanceOf[Product]
                      .productIterator
                      .toSeq
                      .asInstanceOf[Seq[Primitive | Option[Primitive]]]
                      .map(flattenKeyVals)
                  )
                  .filter((_, v) => v.isDefined)
                  .map((k, v) => (k, v.get))
                serializeModel(name, keyVals, format, explode)
    }

  private inline def serializePrimitive(
      paramName: String,
      value: Primitive,
      inline format: FormStyleFormat,
      inline explode: Boolean
  ): Seq[(String, String)] = {
    inline format match
      case FormStyleFormat.FORM =>
        Seq(paramName -> value.asString) // for primitve values explode does not change anything
      case FormStyleFormat.SPACEDELIMITED =>
        error("FormStyleFormat.SpaceDelimited does not support primitive values")
      case FormStyleFormat.PIPEDELIMITED =>
        error("FormStyleFormat.PipeDelimited does not support primitive values")
      case FormStyleFormat.DEEPOBJECT =>
        error("FormStyleFormat.DeepObject does not support primitive values")

  }
  private inline def serializeArray(
      paramName: String,
      values: Seq[Primitive],
      inline format: FormStyleFormat,
      inline explode: Boolean
  ): Seq[(String, String)] = {
    inline format match
      case FormStyleFormat.FORM =>
        inline if explode then values.map(s => (paramName, s.asString))
        else Seq(paramName -> values.map(_.asString).mkString(","))
      case FormStyleFormat.SPACEDELIMITED =>
        inline if explode then values.map(s => (paramName, s.asString))
        else
          Seq(
            paramName -> values.map(_.asString).mkString(" ")
          ) // Sttp will encode space as +, from https://swagger.io/docs/specification/v3_0/serialization/#query-parameters it is not clear if it should be + or %20
      case FormStyleFormat.PIPEDELIMITED =>
        inline if explode then values.map(s => (paramName, s.asString))
        else Seq(paramName -> values.map(_.asString).mkString("|"))
      case FormStyleFormat.DEEPOBJECT =>
        error("FormStyleFormat.DeepObject does not support arrays")
  }
  private inline def serializeModel(
      paramName: String,
      keyValPairs: Seq[(String, Primitive)],
      inline format: FormStyleFormat,
      inline explode: Boolean
  ): Seq[(String, String)] = {
    inline format match
      case FormStyleFormat.FORM =>
        inline if explode then keyValPairs.map((key, value) => (key, value.asString))
        else Seq(paramName -> keyValPairs.flatMap((key, value) => Seq(key, value.asString)).mkString(","))
      case FormStyleFormat.SPACEDELIMITED =>
        error("FormStyleFormat.SpaceDelimited does not support objects")
      case FormStyleFormat.PIPEDELIMITED =>
        error("FormStyleFormat.PipeDelimited does not support objects")
      case FormStyleFormat.DEEPOBJECT =>
        inline if explode then keyValPairs.map((key, value) => (s"$paramName[$key]", value.asString))
        else error("FormStyleFormat.DeepObject does not support explode=false")
  }
end FormSerializable

trait HeaderSerializable[T]:
  inline def serialize(
      name: String,
      obj: T,
      inline explode: Boolean = true
  ): Map[String, String]

object HeaderSerializable:
  inline def serialize[T](
      name: String,
      obj: T,
      inline explode: Boolean = true
  ): Map[String, String] =
    summonFrom {
      case t: HeaderSerializable[T] => t.serialize(name, obj, explode)
      case _                        =>
        inline obj match
          case primitive: Primitive            => Map(name -> primitive.asString)
          case optPrimitive: Option[Primitive] =>
            optPrimitive.map(v => Map(name -> v.asString)).getOrElse(Map.empty[String, String])
          case seqPrimitive: Seq[Primitive]            => Map(name -> seqPrimitive.map(_.asString).mkString(","))
          case optSeqPrimitive: Option[Seq[Primitive]] =>
            optSeqPrimitive.map(v => Map(name -> v.map(_.asString).mkString(","))).getOrElse(Map.empty[String, String])
          case enumArray: Seq[t] =>
            inline summonInline[Mirror.Of[t]] match
              case mirror: Mirror.SumOf[t] =>
                Map(
                  name -> enumArray
                    .map(v =>
                      writeToString(v)(summonInline[JsonValueCodec[mirror.MirroredMonoType]])
                        .stripPrefix("\"")
                        .stripSuffix("\"")
                    )
                    .mkString(",")
                )
              case _ => error("Arrays of non-primitive types are only supported for enums")
          case optEnumArray: Option[Seq[t]] =>
            inline summonInline[Mirror.Of[t]] match
              case mirror: Mirror.SumOf[t] =>
                optEnumArray
                  .map(seq =>
                    Map(
                      name -> seq
                        .map(v =>
                          writeToString(v)(summonInline[JsonValueCodec[mirror.MirroredMonoType]])
                            .stripPrefix("\"")
                            .stripSuffix("\"")
                        )
                        .mkString(",")
                    )
                  )
                  .getOrElse(Map.empty[String, String])
              case _ => error("Arrays of non-primitive types are only supported for enums")
          case mapPrimitive: Map[String, Primitive] => mapPrimitive.map((k, v) => (k, v.asString))
          case optObj: Option[t]                    =>
            inline summonInline[Mirror.Of[t]] match
              case mirror: Mirror.ProductOf[t] =>
                checkFields[mirror.MirroredElemTypes]
                val labels = allLabels[mirror.MirroredElemLabels]
                optObj
                  .map { obj =>
                    val keyVals = labels
                      .zip(
                        obj
                          .asInstanceOf[Product]
                          .productIterator
                          .toSeq
                          .asInstanceOf[Seq[Primitive | Option[Primitive]]]
                          .map(flattenKeyVals)
                      )
                      .filter((_, v) => v.isDefined)
                      .map((k, v) => (k, v.get.asString))
                    inline if explode then Map(name -> keyVals.map((k, v) => s"$k=$v").mkString(","))
                    else Map(name -> keyVals.flatMap((k, v) => Seq(k, v)).mkString(","))
                  }
                  .getOrElse(Map.empty[String, String])
              case mirror: Mirror.SumOf[t] =>
                optObj
                  .map(v => Map(name -> writeToString(v)(summonInline[JsonValueCodec[mirror.MirroredMonoType]])))
                  .getOrElse(Map.empty[String, String])
          case obj: T =>
            inline summonInline[Mirror.Of[T]] match
              case mirror: Mirror.ProductOf[T] =>
                checkFields[mirror.MirroredElemTypes]
                val labels = allLabels[mirror.MirroredElemLabels]
                val keyVals = labels
                  .zip(
                    obj
                      .asInstanceOf[Product]
                      .productIterator
                      .toSeq
                      .asInstanceOf[Seq[Primitive | Option[Primitive]]]
                      .map(flattenKeyVals)
                  )
                  .filter((_, v) => v.isDefined)
                  .map((k, v) => (k, v.get.asString))
                inline if explode then Map(name -> keyVals.map((k, v) => s"$k=$v").mkString(","))
                else Map(name -> keyVals.flatMap((k, v) => Seq(k, v)).mkString(","))
              case mirror: Mirror.SumOf[T] =>
                Map(name -> writeToString(obj)(summonInline[JsonValueCodec[mirror.MirroredMonoType]]))
    }
end HeaderSerializable

trait PathSerializer[T]:
  inline def serialize[T](name: String, obj: T, inline style: PathStyleFormat, inline explode: Boolean): String

object PathSerializable:
  inline def serialize[T](name: String, obj: T, inline style: PathStyleFormat, inline explode: Boolean): String =
    summonFrom {
      case t: PathSerializer[T] => t.serialize(name, obj, style, explode)
      case _                    =>
        inline obj match
          case primitive: Primitive =>
            serializePrimitive(name, primitive, style, explode)
          case array: Seq[Primitive] =>
            serializeArray(name, array, style, explode)
          case optPrimitive: Option[Primitive] =>
            optPrimitive
              .map(value => serializePrimitive(name, value, style, explode))
              .getOrElse("")
          case optArray: Option[Seq[Primitive]] =>
            optArray
              .map(serializeArray(name, _, style, explode))
              .getOrElse("")
          case enumArray: Seq[t] =>
            inline summonInline[Mirror.Of[t]] match
              case mirror: Mirror.SumOf[t] =>
                serializeArray(
                  name,
                  enumArray.map(v =>
                    writeToString(v)(summonInline[JsonValueCodec[mirror.MirroredMonoType]])
                      .stripPrefix("\"")
                      .stripSuffix("\"")
                  ),
                  style,
                  explode
                )
              case _ =>
                error("Arrays of non-primitive types are only supported for enums")
          case optEnumArray: Option[Seq[t]] =>
            inline summonInline[Mirror.Of[t]] match
              case mirror: Mirror.SumOf[t] =>
                optEnumArray
                  .map(seq =>
                    serializeArray(
                      name,
                      seq.map(v =>
                        writeToString(v)(summonInline[JsonValueCodec[mirror.MirroredMonoType]])
                          .stripPrefix("\"")
                          .stripSuffix("\"")
                      ),
                      style,
                      explode
                    )
                  )
                  .getOrElse("")
              case _ =>
                error("Arrays of non-primitive types are only supported for enums")
          case freeObj: Map[String, Primitive] =>
            serializeModel(name, freeObj.map((key, value) => (key, value.asString)).toSeq, style, explode)
          case optObj: Option[t] =>
            inline summonInline[Mirror.Of[t]] match
              case mirror: Mirror.ProductOf[t] =>
                checkFields[mirror.MirroredElemTypes]
                val labels = allLabels[mirror.MirroredElemLabels]
                optObj
                  .map { obj =>
                    val keyVals = labels
                      .zip(
                        obj
                          .asInstanceOf[Product]
                          .productIterator
                          .toSeq
                          .asInstanceOf[Seq[Primitive | Option[Primitive]]]
                          .map(flattenKeyVals)
                      )
                      .filter((_, v) => v.isDefined)
                      .map((k, v) => (k, v.get))
                    serializeModel(name, keyVals, style, explode)
                  }
                  .getOrElse("")
              case mirror: Mirror.SumOf[t] =>
                optObj.map(writeToString(_)(summonInline[JsonValueCodec[mirror.MirroredMonoType]])).getOrElse("")
          case obj =>
            inline summonInline[Mirror.Of[T]] match
              case _: Mirror.SumOf[T] =>
                writeToString(obj)(summonInline[JsonValueCodec[T]])
              case mirror: Mirror.ProductOf[T] =>
                checkFields[mirror.MirroredElemTypes]
                val labels = allLabels[mirror.MirroredElemLabels]
                val keyVals = labels
                  .zip(
                    obj
                      .asInstanceOf[Product]
                      .productIterator
                      .toSeq
                      .asInstanceOf[Seq[Primitive | Option[Primitive]]]
                      .map(flattenKeyVals)
                  )
                  .filter((_, v) => v.isDefined)
                  .map((k, v) => (k, v.get))
                serializeModel(name, keyVals, style, explode)
    }

  private inline def serializePrimitive(
      paramName: String,
      value: Primitive,
      inline format: PathStyleFormat,
      inline explode: Boolean
  ): String = inline format match
    case PathStyleFormat.SIMPLE => value.asString
    case PathStyleFormat.LABEL  => s".${value.asString}"
    case PathStyleFormat.MATRIX => s";$paramName=${value.asString}"

  private inline def serializeArray(
      paramName: String,
      values: Seq[Primitive],
      inline format: PathStyleFormat,
      inline explode: Boolean
  ): String = inline format match
    case PathStyleFormat.SIMPLE => values.map(_.asString).mkString(",")
    case PathStyleFormat.LABEL  =>
      inline if explode then values.map(_.asString).mkString(".", ".", "")
      else values.map(_.asString).mkString(".", ",", "")
    case PathStyleFormat.MATRIX =>
      inline if explode then values.map(v => s";$paramName=${v.asString}").mkString
      else s";$paramName=" + values.map(_.asString).mkString(",")

  private inline def serializeModel(
      paramName: String,
      keyValPairs: Seq[(String, Primitive)],
      inline format: PathStyleFormat,
      inline explode: Boolean
  ): String = inline format match
    case PathStyleFormat.SIMPLE =>
      inline if explode then keyValPairs.map((k, v) => s"$k=${v.asString}").mkString(",")
      else keyValPairs.map((k, v) => s"$k,${v.asString}").mkString(",")
    case PathStyleFormat.LABEL =>
      inline if explode then keyValPairs.map((k, v) => s"$k=${v.asString}").mkString(".", ".", "")
      else keyValPairs.map((k, v) => s"$k,${v.asString}").mkString(".", ",", "")
    case PathStyleFormat.MATRIX =>
      inline if explode then keyValPairs.map((k, v) => s";$k=${v.asString}").mkString
      else keyValPairs.map((k, v) => s"$k,${v.asString}").mkString(s";$paramName=", ",", "")
end PathSerializable

trait CookieSerializable[T]:
  inline def serialize(
      name: String,
      obj: T,
      inline explode: Boolean = true
  ): Seq[(String, String)]

object CookieSerializable:
  inline def serialize[T](
      name: String,
      obj: T,
      inline explode: Boolean = true
  ): Seq[(String, String)] =
    summonFrom {
      case t: CookieSerializable[T] => t.serialize(name, obj, explode)
      case _                        =>
        inline obj match
          case primitive: Primitive =>
            serializePrimitive(name, primitive, explode)
          case array: Seq[Primitive] =>
            serializeArray(name, array, explode)
          case optPrimitive: Option[Primitive] =>
            optPrimitive
              .map(value => serializePrimitive(name, value, explode))
              .getOrElse(Seq.empty[(String, String)])
          case optArray: Option[Seq[Primitive]] =>
            optArray
              .map(serializeArray(name, _, explode))
              .getOrElse(Seq.empty[(String, String)])
          case enumArray: Seq[t] =>
            inline summonInline[Mirror.Of[t]] match
              case mirror: Mirror.SumOf[t] =>
                serializeArray(
                  name,
                  enumArray.map(v =>
                    writeToString(v)(summonInline[JsonValueCodec[mirror.MirroredMonoType]])
                      .stripPrefix("\"")
                      .stripSuffix("\"")
                  ),
                  explode
                )
              case _ =>
                error("Arrays of non-primitive types are only supported for enums")
          case optEnumArray: Option[Seq[t]] =>
            inline summonInline[Mirror.Of[t]] match
              case mirror: Mirror.SumOf[t] =>
                optEnumArray
                  .map(seq =>
                    serializeArray(
                      name,
                      seq.map(v =>
                        writeToString(v)(summonInline[JsonValueCodec[mirror.MirroredMonoType]])
                          .stripPrefix("\"")
                          .stripSuffix("\"")
                      ),
                      explode
                    )
                  )
                  .getOrElse(Seq.empty[(String, String)])
              case _ =>
                error("Arrays of non-primitive types are only supported for enums")
          case freeObj: Map[String, Primitive] =>
            serializeModel(name, freeObj.map((key, value) => (key, value.asString)).toSeq, explode)
          case optObj: Option[t] =>
            inline summonInline[Mirror.Of[t]] match
              case mirror: Mirror.ProductOf[t] =>
                checkFields[mirror.MirroredElemTypes]
                val labels = allLabels[mirror.MirroredElemLabels]
                optObj
                  .map { obj =>
                    val keyVals = labels
                      .zip(
                        obj
                          .asInstanceOf[Product]
                          .productIterator
                          .toSeq
                          .asInstanceOf[Seq[Primitive | Option[Primitive]]]
                          .map(flattenKeyVals)
                      )
                      .filter((_, v) => v.isDefined)
                      .map((k, v) => (k, v.get))
                    serializeModel(name, keyVals, explode)
                  }
                  .getOrElse(Seq.empty[(String, String)])
              case mirror: Mirror.SumOf[t] =>
                optObj.map(v => (name, writeToString(v)(summonInline[JsonValueCodec[mirror.MirroredMonoType]]))).toSeq
          case obj =>
            inline summonInline[Mirror.Of[T]] match
              case _: Mirror.SumOf[T] =>
                Seq(name -> writeToString(obj)(summonInline[JsonValueCodec[T]]))
              case mirror: Mirror.ProductOf[T] =>
                checkFields[mirror.MirroredElemTypes]
                val labels = allLabels[mirror.MirroredElemLabels]
                val keyVals = labels
                  .zip(
                    obj
                      .asInstanceOf[Product]
                      .productIterator
                      .toSeq
                      .asInstanceOf[Seq[Primitive | Option[Primitive]]]
                      .map(flattenKeyVals)
                  )
                  .filter((_, v) => v.isDefined)
                  .map((k, v) => (k, v.get))
                serializeModel(name, keyVals, explode)
    }

  private inline def serializePrimitive(
      paramName: String,
      value: Primitive,
      inline explode: Boolean
  ): Seq[(String, String)] = Seq(paramName -> value.asString)

  private inline def serializeArray(
      paramName: String,
      values: Seq[Primitive],
      inline explode: Boolean
  ): Seq[(String, String)] =
    inline if explode then error("Not supported")
    else Seq(paramName -> values.map(_.asString).mkString(","))

  private inline def serializeModel(
      paramName: String,
      keyValPairs: Seq[(String, Primitive)],
      inline explode: Boolean
  ): Seq[(String, String)] =
    inline if explode then error("Not supported")
    else Seq(paramName -> keyValPairs.map((k, v) => s"$k,${v.asString}").mkString(","))
end CookieSerializable

object Helpers:
  extension (request: sttp.client4.Request[?])
    def fileBody(file: Option[File] | File): sttp.client4.Request[?] =
      file match
        case f: File         => request.body(f)
        case f: Option[File] => f.map(request.body(_)).getOrElse(request)

    def auth(
        authConfig: Authorization,
        location: ApiKeyLocation = ApiKeyLocation.NOAPIKEY,
        keyParamName: String = ""
    ): sttp.client4.Request[?] =
      authConfig match
        case Authorization.NoAuthorization               => request
        case Authorization.BasicAuth(username, password) => request.auth.basic(username, password)
        case Authorization.BearerToken(token)            => request.auth.bearer(token)
        case Authorization.ApiKey(apiKey)                =>
          location match
            case ApiKeyLocation.HEADER   => request.header(keyParamName, apiKey)
            case ApiKeyLocation.COOKIE   => request.cookie(keyParamName, apiKey)
            case ApiKeyLocation.QUERY    => request.copy(uri = request.uri.addParam(keyParamName, apiKey))
            case ApiKeyLocation.NOAPIKEY =>
              request // since it can be called multiple times in request (when there are for example 2 auth methods) we want to make this call idempotent
