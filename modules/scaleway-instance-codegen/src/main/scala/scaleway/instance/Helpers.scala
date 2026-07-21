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
package scaleway.instance

import scala.deriving.*
import scala.compiletime.*
import java.io.File
import java.util.UUID
import java.time.{LocalDate, OffsetDateTime}
import java.time.format.DateTimeFormatter
import com.github.plokhotnyuk.jsoniter_scala.core.{JsonValueCodec, readFromString, writeToString}
import com.github.plokhotnyuk.jsoniter_scala.macros.JsonCodecMaker

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
    case _: (t *: ts) =>
      inline erasedValue[t] match
        case _: Primitive => checkFields[ts]
        case _: Option[Primitive] => checkFields[ts]
        case _ => error("Cannot derive structure, structure must consist only of primitive fields")
  }

extension (p: Primitive)
  def asString: String = p match
    case v: OffsetDateTime => DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(v)
    case v: LocalDate      => DateTimeFormatter.ISO_LOCAL_DATE.format(v)
    case _                 => p.toString
   
private val flattenKeyVals: Primitive | Option[Primitive] => Option[Primitive] = {
  case p: Primitive => Some(p)
  case opt: Option[Primitive] => opt
}

private val stringCodec: JsonValueCodec[String] = JsonCodecMaker.make

// Enums encode to a JSON scalar: strings come back quoted and escaped, numbers bare.
// Decode strings so the parameter carries the enum's actual wire value.
private def enumWireValue[T](v: T)(codec: JsonValueCodec[T]): String =
  val json = writeToString(v)(codec)
  if json.startsWith("\"") then readFromString(json)(stringCodec) else json

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
      case _ =>
        inline obj match
          case primitive: Primitive =>
            serializePrimitive(name, primitive, format, explode)
          case array: Seq[Primitive] =>
            serializeArray(name, array, format, explode)
          case optPrimitive: Option[Primitive] =>
            optPrimitive.map(value => serializePrimitive(name, value, format, explode))
              .getOrElse(Seq.empty[(String, String)])
          case optArray: Option[Seq[Primitive]] =>
            optArray.map(serializeArray(name, _, format, explode))
              .getOrElse(Seq.empty[(String, String)])
          case enumArray: Seq[t] =>
            inline summonInline[Mirror.Of[t]] match
              case mirror: Mirror.SumOf[t] =>
                serializeArray(name, enumArray.map(v => enumWireValue(v)(summonInline[JsonValueCodec[mirror.MirroredMonoType]])), format, explode)
              case _ =>
                error("Arrays of non-primitive types are only supported for enums")
          case optEnumArray: Option[Seq[t]] =>
            inline summonInline[Mirror.Of[t]] match
              case mirror: Mirror.SumOf[t] =>
                optEnumArray.map(seq => serializeArray(name, seq.map(v => enumWireValue(v)(summonInline[JsonValueCodec[mirror.MirroredMonoType]])), format, explode))
                  .getOrElse(Seq.empty[(String, String)])
              case _ =>
                error("Arrays of non-primitive types are only supported for enums")
          case set: Set[p] => // Set is invariant, so dispatch on the bound element type
            inline erasedValue[p] match
              case _: Primitive =>
                serializeArray(name, set.toSeq.asInstanceOf[Seq[Primitive]], format, explode)
              case _ =>
                inline summonInline[Mirror.Of[p]] match
                  case mirror: Mirror.SumOf[p] =>
                    serializeArray(name, set.toSeq.map(v => enumWireValue(v)(summonInline[JsonValueCodec[mirror.MirroredMonoType]])), format, explode)
                  case _ =>
                    error("Sets of non-primitive types are only supported for enums")
          case optSet: Option[Set[p]] =>
            inline erasedValue[p] match
              case _: Primitive =>
                optSet.map(s => serializeArray(name, s.toSeq.asInstanceOf[Seq[Primitive]], format, explode))
                  .getOrElse(Seq.empty[(String, String)])
              case _ =>
                inline summonInline[Mirror.Of[p]] match
                  case mirror: Mirror.SumOf[p] =>
                    optSet.map(s => serializeArray(name, s.toSeq.map(v => enumWireValue(v)(summonInline[JsonValueCodec[mirror.MirroredMonoType]])), format, explode))
                      .getOrElse(Seq.empty[(String, String)])
                  case _ =>
                    error("Sets of non-primitive types are only supported for enums")
          case freeObj: Map[String, Primitive] =>
            freeObj.map((key, value) => (key, value.asString)).toSeq
          case optObj: Option[t] =>
            inline summonInline[Mirror.Of[t]] match
              case mirror: Mirror.ProductOf[t] =>
                checkFields[mirror.MirroredElemTypes]
                val labels = allLabels[mirror.MirroredElemLabels]
                optObj.map { obj =>
                    val keyVals = labels.zip(obj.asInstanceOf[Product].productIterator.toSeq.asInstanceOf[Seq[Primitive | Option[Primitive]]].map(flattenKeyVals))
                      .filter((_, v) => v.isDefined)
                      .map((k, v) => (k, v.get))
                    serializeModel(name, keyVals, format, explode)
                  }.getOrElse(Seq.empty[(String, String)])
              case mirror: Mirror.SumOf[t] => optObj.map(v => (name, writeToString(v)(summonInline[JsonValueCodec[mirror.MirroredMonoType]]))).toSeq
          case obj =>
            inline summonInline[Mirror.Of[T]] match
              case _: Mirror.SumOf[T] =>
                Seq((name, writeToString(obj)(summonInline[JsonValueCodec[T]])))
              case mirror: Mirror.ProductOf[T] =>
                checkFields[mirror.MirroredElemTypes] // Stripe ma IDGAF bo używają deepObject np. tak lines[0][tax_amounts][0][amount] - mimo tego że spec na to nie pozwala
                val labels = allLabels[mirror.MirroredElemLabels]
                val keyVals = labels.zip(obj.asInstanceOf[Product].productIterator.toSeq.asInstanceOf[Seq[Primitive | Option[Primitive]]].map(flattenKeyVals))
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
    // an empty collection carries no value: omit it entirely rather than emit `name=`
    // (matches explode=true, which already yields no entries for an empty collection)
    if values.isEmpty then Seq.empty[(String, String)]
    else inline format match
      case FormStyleFormat.FORM =>
        inline if explode then values.map(s => (paramName, s.asString))
        else Seq(paramName -> values.map(_.asString).mkString(","))
      case FormStyleFormat.SPACEDELIMITED =>
        inline if explode then values.map(s => (paramName, s.asString))
        else Seq(paramName -> values.map(_.asString).mkString(" ")) // Sttp will encode space as +, from https://swagger.io/docs/specification/v3_0/serialization/#query-parameters it is not clear if it should be + or %20
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
      case _ => inline obj match
        case primitive: Primitive => Map(name -> primitive.asString)
        case optPrimitive: Option[Primitive] => optPrimitive.map(v => Map(name -> v.asString)).getOrElse(Map.empty[String, String])
        case seqPrimitive: Seq[Primitive] => Map(name -> seqPrimitive.map(_.asString).mkString(","))
        case optSeqPrimitive: Option[Seq[Primitive]] => optSeqPrimitive.map(v => Map(name -> v.map(_.asString).mkString(","))).getOrElse(Map.empty[String, String])
        case enumArray: Seq[t] =>
          inline summonInline[Mirror.Of[t]] match
            case mirror: Mirror.SumOf[t] => Map(name -> enumArray.map(v => enumWireValue(v)(summonInline[JsonValueCodec[mirror.MirroredMonoType]])).mkString(","))
            case _ => error("Arrays of non-primitive types are only supported for enums")
        case optEnumArray: Option[Seq[t]] =>
          inline summonInline[Mirror.Of[t]] match
            case mirror: Mirror.SumOf[t] => optEnumArray.map(seq => Map(name -> seq.map(v => enumWireValue(v)(summonInline[JsonValueCodec[mirror.MirroredMonoType]])).mkString(","))).getOrElse(Map.empty[String, String])
            case _ => error("Arrays of non-primitive types are only supported for enums")
        case set: Set[p] => // Set is invariant, so dispatch on the bound element type
          inline erasedValue[p] match
            case _: Primitive => Map(name -> set.toSeq.asInstanceOf[Seq[Primitive]].map(_.asString).mkString(","))
            case _ =>
              inline summonInline[Mirror.Of[p]] match
                case mirror: Mirror.SumOf[p] => Map(name -> set.toSeq.map(v => enumWireValue(v)(summonInline[JsonValueCodec[mirror.MirroredMonoType]])).mkString(","))
                case _ => error("Sets of non-primitive types are only supported for enums")
        case optSet: Option[Set[p]] =>
          inline erasedValue[p] match
            case _: Primitive => optSet.map(s => Map(name -> s.toSeq.asInstanceOf[Seq[Primitive]].map(_.asString).mkString(","))).getOrElse(Map.empty[String, String])
            case _ =>
              inline summonInline[Mirror.Of[p]] match
                case mirror: Mirror.SumOf[p] => optSet.map(s => Map(name -> s.toSeq.map(v => enumWireValue(v)(summonInline[JsonValueCodec[mirror.MirroredMonoType]])).mkString(","))).getOrElse(Map.empty[String, String])
                case _ => error("Sets of non-primitive types are only supported for enums")
        case mapPrimitive: Map[String, Primitive] => mapPrimitive.map((k, v) => (k, v.asString))
        case optObj: Option[t] =>
          inline summonInline[Mirror.Of[t]] match
            case mirror: Mirror.ProductOf[t] =>
              checkFields[mirror.MirroredElemTypes]
              val labels = allLabels[mirror.MirroredElemLabels]
              optObj.map { obj =>
                  val keyVals = labels.zip(obj.asInstanceOf[Product].productIterator.toSeq.asInstanceOf[Seq[Primitive | Option[Primitive]]].map(flattenKeyVals))
                    .filter((_, v) => v.isDefined)
                    .map((k, v) => (k, v.get.asString))
                  inline if explode then
                    Map(name ->keyVals.map((k, v) => s"$k=$v").mkString(","))
                  else
                    Map(name -> keyVals.flatMap((k, v) => Seq(k, v)).mkString(","))
                }.getOrElse(Map.empty[String, String])
            case mirror: Mirror.SumOf[t] => optObj.map(v => Map(name -> writeToString(v)(summonInline[JsonValueCodec[mirror.MirroredMonoType]]))).getOrElse(Map.empty[String, String])
        case obj: T =>
          inline summonInline[Mirror.Of[T]] match
            case mirror: Mirror.ProductOf[T] =>
              checkFields[mirror.MirroredElemTypes]
              val labels = allLabels[mirror.MirroredElemLabels]
              val keyVals = labels.zip(obj.asInstanceOf[Product].productIterator.toSeq.asInstanceOf[Seq[Primitive | Option[Primitive]]].map(flattenKeyVals))
                .filter((_, v) => v.isDefined)
                .map((k, v) => (k, v.get.asString))
              inline if explode then
                Map(name ->keyVals.map((k, v) => s"$k=$v").mkString(","))
              else
                Map(name -> keyVals.flatMap((k, v) => Seq(k, v)).mkString(","))
            case mirror: Mirror.SumOf[T] => Map(name -> writeToString(obj)(summonInline[JsonValueCodec[mirror.MirroredMonoType]]))
    }
end HeaderSerializable

trait PathSerializer[T]:
  inline def serialize[T](name: String, obj: T, inline style: PathStyleFormat, inline explode: Boolean): String

object PathSerializable:
  inline def serialize[T](name: String, obj: T, inline style: PathStyleFormat, inline explode: Boolean): String =
    summonFrom {
      case t: PathSerializer[T] => t.serialize(name, obj, style, explode)
      case _ =>
        inline obj match
          case primitive: Primitive =>
            serializePrimitive(name, primitive, style, explode)
          case array: Seq[Primitive] =>
            serializeArray(name, array, style, explode)
          case optPrimitive: Option[Primitive] =>
            optPrimitive.map(value => serializePrimitive(name, value, style, explode))
              .getOrElse("")
          case optArray: Option[Seq[Primitive]] =>
            optArray.map(serializeArray(name, _, style, explode))
              .getOrElse("")
          case enumArray: Seq[t] =>
            inline summonInline[Mirror.Of[t]] match
              case mirror: Mirror.SumOf[t] =>
                serializeArray(name, enumArray.map(v => enumWireValue(v)(summonInline[JsonValueCodec[mirror.MirroredMonoType]])), style, explode)
              case _ =>
                error("Arrays of non-primitive types are only supported for enums")
          case optEnumArray: Option[Seq[t]] =>
            inline summonInline[Mirror.Of[t]] match
              case mirror: Mirror.SumOf[t] =>
                optEnumArray.map(seq => serializeArray(name, seq.map(v => enumWireValue(v)(summonInline[JsonValueCodec[mirror.MirroredMonoType]])), style, explode))
                  .getOrElse("")
              case _ =>
                error("Arrays of non-primitive types are only supported for enums")
          case set: Set[p] => // Set is invariant, so dispatch on the bound element type
            inline erasedValue[p] match
              case _: Primitive =>
                serializeArray(name, set.toSeq.asInstanceOf[Seq[Primitive]], style, explode)
              case _ =>
                inline summonInline[Mirror.Of[p]] match
                  case mirror: Mirror.SumOf[p] =>
                    serializeArray(name, set.toSeq.map(v => enumWireValue(v)(summonInline[JsonValueCodec[mirror.MirroredMonoType]])), style, explode)
                  case _ =>
                    error("Sets of non-primitive types are only supported for enums")
          case optSet: Option[Set[p]] =>
            inline erasedValue[p] match
              case _: Primitive =>
                optSet.map(s => serializeArray(name, s.toSeq.asInstanceOf[Seq[Primitive]], style, explode))
                  .getOrElse("")
              case _ =>
                inline summonInline[Mirror.Of[p]] match
                  case mirror: Mirror.SumOf[p] =>
                    optSet.map(s => serializeArray(name, s.toSeq.map(v => enumWireValue(v)(summonInline[JsonValueCodec[mirror.MirroredMonoType]])), style, explode))
                      .getOrElse("")
                  case _ =>
                    error("Sets of non-primitive types are only supported for enums")
          case freeObj: Map[String, Primitive] =>
            serializeModel(name, freeObj.map((key, value) => (key, value.asString)).toSeq, style, explode)
          case optObj: Option[t] =>
            inline summonInline[Mirror.Of[t]] match
              case mirror: Mirror.ProductOf[t] =>
                checkFields[mirror.MirroredElemTypes]
                val labels = allLabels[mirror.MirroredElemLabels]
                optObj.map { obj =>
                  val keyVals = labels.zip(obj.asInstanceOf[Product].productIterator.toSeq.asInstanceOf[Seq[Primitive | Option[Primitive]]].map(flattenKeyVals))
                    .filter((_, v) => v.isDefined)
                    .map((k, v) => (k, v.get))
                    serializeModel(name, keyVals, style, explode)
                  }.getOrElse("")
              case mirror: Mirror.SumOf[t] => optObj.map(writeToString(_)(summonInline[JsonValueCodec[mirror.MirroredMonoType]])).getOrElse("")
          case obj =>
            inline summonInline[Mirror.Of[T]] match
              case _: Mirror.SumOf[T] =>
                writeToString(obj)(summonInline[JsonValueCodec[T]])
              case mirror: Mirror.ProductOf[T] =>
                checkFields[mirror.MirroredElemTypes]
                val labels = allLabels[mirror.MirroredElemLabels]
                val keyVals = labels.zip(obj.asInstanceOf[Product].productIterator.toSeq.asInstanceOf[Seq[Primitive | Option[Primitive]]].map(flattenKeyVals))
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
    case PathStyleFormat.LABEL => s".${value.asString}"
    case PathStyleFormat.MATRIX => s";$paramName=${value.asString}"

  private inline def serializeArray(
      paramName: String,
      values: Seq[Primitive],
      inline format: PathStyleFormat,
      inline explode: Boolean
  ): String = inline format match
    case PathStyleFormat.SIMPLE => values.map(_.asString).mkString(",")
    case PathStyleFormat.LABEL => inline if explode then values.map(_.asString).mkString(".", ".", "") else values.map(_.asString).mkString(".", ",", "")
    case PathStyleFormat.MATRIX => inline if explode then values.map(v => s";$paramName=${v.asString}").mkString else s";$paramName=" + values.map(_.asString).mkString(",")

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
      case _ =>
        inline obj match
          case primitive: Primitive =>
            serializePrimitive(name, primitive, explode)
          case array: Seq[Primitive] =>
            serializeArray(name, array, explode)
          case optPrimitive: Option[Primitive] =>
            optPrimitive.map(value => serializePrimitive(name, value, explode))
              .getOrElse(Seq.empty[(String, String)])
          case optArray: Option[Seq[Primitive]] =>
            optArray.map(serializeArray(name, _, explode))
              .getOrElse(Seq.empty[(String, String)])
          case enumArray: Seq[t] =>
            inline summonInline[Mirror.Of[t]] match
              case mirror: Mirror.SumOf[t] =>
                serializeArray(name, enumArray.map(v => enumWireValue(v)(summonInline[JsonValueCodec[mirror.MirroredMonoType]])), explode)
              case _ =>
                error("Arrays of non-primitive types are only supported for enums")
          case optEnumArray: Option[Seq[t]] =>
            inline summonInline[Mirror.Of[t]] match
              case mirror: Mirror.SumOf[t] =>
                optEnumArray.map(seq => serializeArray(name, seq.map(v => enumWireValue(v)(summonInline[JsonValueCodec[mirror.MirroredMonoType]])), explode))
                  .getOrElse(Seq.empty[(String, String)])
              case _ =>
                error("Arrays of non-primitive types are only supported for enums")
          case set: Set[p] => // Set is invariant, so dispatch on the bound element type
            inline erasedValue[p] match
              case _: Primitive =>
                serializeArray(name, set.toSeq.asInstanceOf[Seq[Primitive]], explode)
              case _ =>
                inline summonInline[Mirror.Of[p]] match
                  case mirror: Mirror.SumOf[p] =>
                    serializeArray(name, set.toSeq.map(v => enumWireValue(v)(summonInline[JsonValueCodec[mirror.MirroredMonoType]])), explode)
                  case _ =>
                    error("Sets of non-primitive types are only supported for enums")
          case optSet: Option[Set[p]] =>
            inline erasedValue[p] match
              case _: Primitive =>
                optSet.map(s => serializeArray(name, s.toSeq.asInstanceOf[Seq[Primitive]], explode))
                  .getOrElse(Seq.empty[(String, String)])
              case _ =>
                inline summonInline[Mirror.Of[p]] match
                  case mirror: Mirror.SumOf[p] =>
                    optSet.map(s => serializeArray(name, s.toSeq.map(v => enumWireValue(v)(summonInline[JsonValueCodec[mirror.MirroredMonoType]])), explode))
                      .getOrElse(Seq.empty[(String, String)])
                  case _ =>
                    error("Sets of non-primitive types are only supported for enums")
          case freeObj: Map[String, Primitive] =>
            serializeModel(name, freeObj.map((key, value) => (key, value.asString)).toSeq, explode)
          case optObj: Option[t] =>
            inline summonInline[Mirror.Of[t]] match
              case mirror: Mirror.ProductOf[t] =>
                checkFields[mirror.MirroredElemTypes]
                val labels = allLabels[mirror.MirroredElemLabels]
                optObj.map { obj =>
                  val keyVals = labels.zip(obj.asInstanceOf[Product].productIterator.toSeq.asInstanceOf[Seq[Primitive | Option[Primitive]]].map(flattenKeyVals))
                    .filter((_, v) => v.isDefined)
                    .map((k, v) => (k, v.get))
                    serializeModel(name, keyVals, explode)
                }.getOrElse(Seq.empty[(String, String)])
              case mirror: Mirror.SumOf[t] => optObj.map(v => (name, writeToString(v)(summonInline[JsonValueCodec[mirror.MirroredMonoType]]))).toSeq
          case obj =>
            inline summonInline[Mirror.Of[T]] match
              case _: Mirror.SumOf[T] =>
                Seq(name -> writeToString(obj)(summonInline[JsonValueCodec[T]]))
              case mirror: Mirror.ProductOf[T] =>
                checkFields[mirror.MirroredElemTypes]
                val labels = allLabels[mirror.MirroredElemLabels]
                val keyVals = labels.zip(obj.asInstanceOf[Product].productIterator.toSeq.asInstanceOf[Seq[Primitive | Option[Primitive]]].map(flattenKeyVals))
                  .filter((_, v) => v.isDefined)
                  .map((k, v) => (k, v.get))
                serializeModel(name, keyVals, explode)
    }

  private inline def serializePrimitive(
      paramName: String,
      value: Primitive,
      inline explode: Boolean
  ): Seq[(String, String)] =  Seq(paramName -> value.asString)

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

    def auth(authConfig: Authorization, location: ApiKeyLocation = ApiKeyLocation.NOAPIKEY, keyParamName: String = ""): sttp.client4.Request[?] =
      authConfig match
        case Authorization.NoAuthorization => request
        case Authorization.BasicAuth(username, password) => request.auth.basic(username, password)
        case Authorization.BearerToken(token) => request.auth.bearer(token)
        case Authorization.ApiKey(apiKey) =>location match
          case ApiKeyLocation.HEADER => request.header(keyParamName, apiKey)
          case ApiKeyLocation.COOKIE => request.cookie(keyParamName, apiKey)
          case ApiKeyLocation.QUERY => request.copy(uri = request.uri.addParam(keyParamName, apiKey))
          case ApiKeyLocation.NOAPIKEY => request  // since it can be called multiple times in request (when there are for example 2 auth methods) we want to make this call idempotent
