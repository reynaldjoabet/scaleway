/** IPAM API IPAM is an **IP** **A**ddress **M**anagement tool for Scaleway's ecosystem. It acts as a single source of
  * truth for the IP addresses of Scaleway resources. You can use the IPAM API to list public IP addresses for products
  * such as Instances, Databases and Load Balancer which are already using [IP
  * mobility](https://www.scaleway.com/en/blog/ip-mobility-removing-nat/). More products will be integrated in the
  * future. Scaleway's IPAM also powers the addressing of resources of all resources in a VPC, simplifying management of
  * IP allocations, conflicts and lifecycle. Private Networks' DHCP server hands out the static private IP addresses
  * assigned by IPAM to resources attached to Private Networks. IPAM allows you to reserve IPs, to ensure they won't be
  * allocated to some other resource. You can use these reserved IPs directly on custom environments (e.g. VMs on an
  * Elastic Metal server). In the future, you will be also be able to specify a reserved IP to use when attaching a
  * resource to a Private Network. In the meantime, when you attach a resource to a Private Network, its IP address is
  * assigned by IPAM and then automatically attached by the product/resource. IPAM gives you a number of guarantees
  * about IPs: - once reserved, the IP will never change - once attached to a resource, no other resource will be able
  * to use the IP, ensuring unicity - once attached to a resource, the IP cannot be released unless detached from the
  * resource ## Concepts ### Resource An IPAM IP can be attached to a resource, which is identified by its type (e.g. an
  * Instance Private NIC) and its UUID. Once attached, most operations on the IP are not available anymore: it must
  * first be detached. Once an IP is attached to a resource, it is not possible anymore to: - release the IP - detach
  * the IP (without detaching the resource from the Private Network first) When an IP is attached to a resource, only
  * the product managing the resource can detach the IP, by detaching the resource from the Private Network. This
  * ensures that IPs cannot be reused and avoid mistakes. ### Source An IP can be reserved in several different sources,
  * which can be understood as distinct pools of available IP. Sources can be either **global** like the `zonal` source
  * which is shared across all users, or **specific**, like the `private_network` source, that is identified by the
  * Private Network ID, and will hand out IPs from a specific Private Network. ## Quickstart **Requirements**: - You
  * have a [Scaleway account](https://console.scaleway.com/) - You have created an [API
  * key](https://www.scaleway.com/en/docs/iam/how-to/create-api-keys/) and that the API key has sufficient [IAM
  * permissions](https://www.scaleway.com/en/docs/iam/reference-content/permission-sets/) to perform the actions
  * described on this page - You have [installed `curl`](https://curl.se/download.html) 1. Configure your environment
  * variables. <Message type=\"note\"> This is an optional step that seeks to simplify your usage of the IPAM API.
  * </Message> ```bash     export SCW_SECRET_KEY=\"<API secret key>\"     export SCW_DEFAULT_REGION=\"<Scaleway default Region>\"     export SCW_PROJECT_ID=\"<Scaleway Project ID>\"     export SCW_PRIVATE_NETWORK_ID=\"<Scaleway Private Network ID>\"     ``` 2.
  * Create an IP: run the following command to create an IP in a specified Private Network. You can customize the tags
  * as you wish. The IP will not be attached to any resource. ```bash     curl -X POST \\       -H \"X-Auth-Token: $SCW_SECRET_KEY\" \\       \"https://api.scaleway.com/ipam/v1/regions/$SCW_DEFAULT_REGION/ips\" \\       --json '{         \"project_id\": \"'\"$SCW_PROJECT_ID\"'\",         \"source\": {\"private_network_id\": \"'\"$SCW_PRIVATE_NETWORK_ID\"'\"},         \"tags\": [\"test\", \"another tag\"]       }'     ``` 3.
  * **Get a list of your IPs**: run the following command to get a list of all the IPs in your account, with their
  * details: ```bash     curl -X GET \\       -H \"X-Auth-Token: $SCW_SECRET_KEY\" \\       \"https://api.scaleway.com/ipam/v1/regions/$SCW_DEFAULT_REGION/ips\"     ``` 4.
  * **Get a list of your IPs in a Private Network**: run the following command to get a list of all the IPs in a Private
  * Network, with their details: ```bash     curl -X GET \\       -H \"X-Auth-Token: $SCW_SECRET_KEY\" \\       \"https://api.scaleway.com/ipam/v1/regions/$SCW_DEFAULT_REGION/ips?private_network_id=$SCW_PRIVATE_NETWORK_ID\"     ``` 5.
  * **Release an IP**: run the following command to delete an IP, if it is not attached to a resource. Ensure that you
  * replace `<IP-ID>` in the URL with the ID of the IP you want to release. ```bash     curl -X DELETE \\       -H \"X-Auth-Token: $SCW_SECRET_KEY\" \\       \"https://api.scaleway.com/ipam/v1/regions/$SCW_DEFAULT_REGION/ips/<IP-ID>\"     ```
  * <Message type=\"requirement\"> - You have a [Scaleway account](https://console.scaleway.com/) - You have created an
  * [API key](https://www.scaleway.com/en/docs/iam/how-to/create-api-keys/) and that the API key has sufficient [IAM
  * permissions](https://www.scaleway.com/en/docs/iam/reference-content/permission-sets/) to perform the actions
  * described on this page - You have [installed `curl`](https://curl.se/download.html) </Message> ## Technical
  * limitations Scaleway's IPAM is a new tool. Not all Scaleway products are currently integrated in terms of public IP
  * addressing, and not all functionalities are currently publicly available for the management of private IPs in a VPC
  * (for example, not all products support specifying a reserved IP when attaching a resource to a Private Network).
  * More features and functionalities will be added in due course. In the meantime, [read our blog
  * post](https://www.scaleway.com/en/blog/ip-mobility-removing-nat/) to find out more about how we are tackling
  * technical debt with IP mobility. ## Going further For more help using Scaleway IPAM and network products, check out
  * the following resources: - Our [VPC documentation](https://www.scaleway.com/en/docs/vpc) - The
  * #virtual-private-cloud channel on our [Slack
  * Community](https://www.scaleway.com/en/docs/tutorials/scaleway-slack-community/) - Our [support ticketing
  * system](https://www.scaleway.com/en/docs/account/how-to/open-a-support-ticket/).
  *
  * The version of the OpenAPI document: v1
  *
  * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
  * https://openapi-generator.tech Do not edit the class manually.
  */
package scaleway.ipam

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
                  enumArray.map(v => enumWireValue(v)(summonInline[JsonValueCodec[mirror.MirroredMonoType]])),
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
                      seq.map(v => enumWireValue(v)(summonInline[JsonValueCodec[mirror.MirroredMonoType]])),
                      format,
                      explode
                    )
                  )
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
                    serializeArray(
                      name,
                      set.toSeq.map(v => enumWireValue(v)(summonInline[JsonValueCodec[mirror.MirroredMonoType]])),
                      format,
                      explode
                    )
                  case _ =>
                    error("Sets of non-primitive types are only supported for enums")
          case optSet: Option[Set[p]] =>
            inline erasedValue[p] match
              case _: Primitive =>
                optSet
                  .map(s => serializeArray(name, s.toSeq.asInstanceOf[Seq[Primitive]], format, explode))
                  .getOrElse(Seq.empty[(String, String)])
              case _ =>
                inline summonInline[Mirror.Of[p]] match
                  case mirror: Mirror.SumOf[p] =>
                    optSet
                      .map(s =>
                        serializeArray(
                          name,
                          s.toSeq.map(v => enumWireValue(v)(summonInline[JsonValueCodec[mirror.MirroredMonoType]])),
                          format,
                          explode
                        )
                      )
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
    // an empty collection carries no value: omit it entirely rather than emit `name=`
    // (matches explode=true, which already yields no entries for an empty collection)
    if values.isEmpty then Seq.empty[(String, String)]
    else
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
                    .map(v => enumWireValue(v)(summonInline[JsonValueCodec[mirror.MirroredMonoType]]))
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
                        .map(v => enumWireValue(v)(summonInline[JsonValueCodec[mirror.MirroredMonoType]]))
                        .mkString(",")
                    )
                  )
                  .getOrElse(Map.empty[String, String])
              case _ => error("Arrays of non-primitive types are only supported for enums")
          case set: Set[p] => // Set is invariant, so dispatch on the bound element type
            inline erasedValue[p] match
              case _: Primitive => Map(name -> set.toSeq.asInstanceOf[Seq[Primitive]].map(_.asString).mkString(","))
              case _            =>
                inline summonInline[Mirror.Of[p]] match
                  case mirror: Mirror.SumOf[p] =>
                    Map(
                      name -> set.toSeq
                        .map(v => enumWireValue(v)(summonInline[JsonValueCodec[mirror.MirroredMonoType]]))
                        .mkString(",")
                    )
                  case _ => error("Sets of non-primitive types are only supported for enums")
          case optSet: Option[Set[p]] =>
            inline erasedValue[p] match
              case _: Primitive =>
                optSet
                  .map(s => Map(name -> s.toSeq.asInstanceOf[Seq[Primitive]].map(_.asString).mkString(",")))
                  .getOrElse(Map.empty[String, String])
              case _ =>
                inline summonInline[Mirror.Of[p]] match
                  case mirror: Mirror.SumOf[p] =>
                    optSet
                      .map(s =>
                        Map(
                          name -> s.toSeq
                            .map(v => enumWireValue(v)(summonInline[JsonValueCodec[mirror.MirroredMonoType]]))
                            .mkString(",")
                        )
                      )
                      .getOrElse(Map.empty[String, String])
                  case _ => error("Sets of non-primitive types are only supported for enums")
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
                  enumArray.map(v => enumWireValue(v)(summonInline[JsonValueCodec[mirror.MirroredMonoType]])),
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
                      seq.map(v => enumWireValue(v)(summonInline[JsonValueCodec[mirror.MirroredMonoType]])),
                      style,
                      explode
                    )
                  )
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
                    serializeArray(
                      name,
                      set.toSeq.map(v => enumWireValue(v)(summonInline[JsonValueCodec[mirror.MirroredMonoType]])),
                      style,
                      explode
                    )
                  case _ =>
                    error("Sets of non-primitive types are only supported for enums")
          case optSet: Option[Set[p]] =>
            inline erasedValue[p] match
              case _: Primitive =>
                optSet
                  .map(s => serializeArray(name, s.toSeq.asInstanceOf[Seq[Primitive]], style, explode))
                  .getOrElse("")
              case _ =>
                inline summonInline[Mirror.Of[p]] match
                  case mirror: Mirror.SumOf[p] =>
                    optSet
                      .map(s =>
                        serializeArray(
                          name,
                          s.toSeq.map(v => enumWireValue(v)(summonInline[JsonValueCodec[mirror.MirroredMonoType]])),
                          style,
                          explode
                        )
                      )
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
                  enumArray.map(v => enumWireValue(v)(summonInline[JsonValueCodec[mirror.MirroredMonoType]])),
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
                      seq.map(v => enumWireValue(v)(summonInline[JsonValueCodec[mirror.MirroredMonoType]])),
                      explode
                    )
                  )
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
                    serializeArray(
                      name,
                      set.toSeq.map(v => enumWireValue(v)(summonInline[JsonValueCodec[mirror.MirroredMonoType]])),
                      explode
                    )
                  case _ =>
                    error("Sets of non-primitive types are only supported for enums")
          case optSet: Option[Set[p]] =>
            inline erasedValue[p] match
              case _: Primitive =>
                optSet
                  .map(s => serializeArray(name, s.toSeq.asInstanceOf[Seq[Primitive]], explode))
                  .getOrElse(Seq.empty[(String, String)])
              case _ =>
                inline summonInline[Mirror.Of[p]] match
                  case mirror: Mirror.SumOf[p] =>
                    optSet
                      .map(s =>
                        serializeArray(
                          name,
                          s.toSeq.map(v => enumWireValue(v)(summonInline[JsonValueCodec[mirror.MirroredMonoType]])),
                          explode
                        )
                      )
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
