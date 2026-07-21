/** VPC API VPC allows you to build your own **V**irtual **P**rivate **C**loud on top of Scaleway’s shared public cloud.
  * VPC currently comprises the regional Private Networks product. Layer 2 regional Private Networks sit inside the
  * layer 3 VPC. Private Networks allows Scaleway resources (Instances, Load Balancers, Managed Databases etc.) within a
  * single region to be interconnected through a dedicated, private, and flexible [L2
  * network](https://en.wikipedia.org/wiki/Data_link_layer). You can add as many resources to your networks as you want,
  * and add up to eight (8) different networks per resource. This allows you to run services isolated from the public
  * internet and expose them to the rest of your infrastructure without worrying about public network filtering.
  * <Message type=\"note\"> VPC v2 is now in **General Availability**. </Message> <Message type=\"tip\"> Check out our
  * [IPAM API](https://www.scaleway.com/en/developers/api/ipam/) to facilitate the management of IP addresses across
  * your different Scaleway resources. </Message> ## Concepts Refer to our [dedicated concepts
  * page](https://www.scaleway.com/en/docs/vpc/concepts/) to find definitions of all concepts and terminology related to
  * VPC. ## Quickstart 1. **Configure your environment variables** <Message type=\"note\"> This is an optional step that
  * seeks to simplify your usage of the API. See the [Technical information](#technical-information) section below for
  * help choosing an Availability Zone and Region. You can find your Project ID in the [Scaleway
  * console](https://console.scaleway.com/project/settings). </Message> ```bash     export SCW_SECRET_KEY=\"<API secret key>\"     export SCW_DEFAULT_REGION=\"<Scaleway region>\"     export SCW_DEFAULT_ZONE=\"<Scaleway Availability Zone>\"     export SCW_PROJECT_ID=\"<Scaleway Project ID>\"     ``` 2.
  * **Create a Private Network**. Run the following command to create a Private Network. You can customize the details
  * in the payload (name, tags etc.) to your needs. ```bash     curl -X POST \\         -H \"X-Auth-Token: $SCW_SECRET_KEY\" \\         -H \"Content-Type: application/json\" \\         \"https://api.scaleway.com/vpc/v2/regions/$SCW_DEFAULT_REGION/private-networks\" \\         -d '{             \"name\": \"My new Private Network\",             \"project_id\": \"'\"$SCW_PROJECT_ID\"'\",              \"tags\": [\"test\", \"dev\"]         }'     ```
  * <Message type=\"tip\"> Keep the `id` field of the response: it is your Private Network ID, and is useable across all
  * Scaleway products that support Private Networks. It may be useful to you to export the Private Network ID as a new
  * environment variable `export PN_ID=\"<Your Private Network ID>` </Message> <Message type=\"tip\"> If you create a
  * Private Network without specifying a VPC to create it in, the behavior depends on when you created your Scaleway
  * Project. [Find out more](https://www.scaleway.com/en/docs/vpc/concepts/#default-vpc) </Message> 3. **Attach a
  * resource to your Private Network**. Each Scaleway product has its own API to interact with Private Networks. To
  * attach an Instance, Managed Database, Elastic Metal server, Load Balancer or Public Gateway to your Private Network,
  * see instructions in the documentation of the relevant product API. Here, we take the example of an Instance. Use the
  * following call to attach an Instance to your Private Network. Ensure you replace `<Instance ID>` with the ID of your
  * Instance, and `<Private Network ID>` with the ID of your Private Network. Note that the Instance must be in an
  * Availability Zone that is part of the region of your Private Network. ```bash     curl -X POST \\         -H \"X-Auth-Token: $SCW_SECRET_KEY\" \\         -H \"Content-Type: application/json\" \\         \"https://api.scaleway.com/instance/v1/zones/$SCW_DEFAULT_ZONE/servers/<Instance ID>/private_nics\" \\         -d '{\"private_network_id\": \"<Private Network ID>\"}'     ```
  * <Message type=\"tip\"> Keep the `id` field of the response: it is your Private NIC ID. It may be useful to you to
  * export the Private NIC ID as a new environment variable `export NIC_ID=\"<Your Private NIC ID>`. </Message> <Message
  * type=\"tip\"> Keep the `mac_address` field of the response, as it will allow you to identify the Private NIC inside
  * your Instance. If successful, a new network interface will appear inside your Instance, ready to be configured to
  * transmit traffic to other Instances of the same network, with the MAC address returned by the API call. </Message> 4.
  * **Confirm that the network interface has been plugged in**. To do this, connect to your Instance and run `dmseg`.
  * You should see an output similar to the following: ```bash     [1579004.592869] pci 0000:00:05.0: [1af4:1000] type 00 class 0x020000     [1579004.594835] pci 0000:00:05.0: reg 0x10: [io  0x0000-0x003f]     [1579004.596715] pci 0000:00:05.0: reg 0x14: [mem 0x00000000-0x00000fff]     [1579004.598732] pci 0000:00:05.0: reg 0x20: [mem 0x00000000-0x00003fff 64bit pref]     [1579004.600765] pci 0000:00:05.0: reg 0x30: [mem 0x00000000-0x0007ffff pref]     [1579004.603819] pci 0000:00:05.0: BAR 6: assigned [mem 0xc0100000-0xc017ffff pref]     [1579004.604582] pci 0000:00:05.0: BAR 4: assigned [mem 0x100000c000-0x100000ffff 64bit pref]     [1579004.605555] pci 0000:00:05.0: BAR 1: assigned [mem 0xc0003000-0xc0003fff]     [1579004.606383] pci 0000:00:05.0: BAR 0: assigned [io  0x1000-0x103f]     [1579004.607212] virtio-pci 0000:00:05.0: enabling device (0000 -> 0003)     [1579004.625149] PCI Interrupt Link [LNKA] enabled at IRQ 11     [1579004.644930] virtio_net virtio3 ens5: renamed from eth0     ``` 5.
  * **Confirm the presence of the network interface, and confirm its name if several networks are plugged into your
  * Instance**. To do this, run `ip -br link`. You should see an output similar to the following: ```bash     lo               UNKNOWN        00:00:00:00:00:00 <LOOPBACK,UP,LOWER_UP>     ens2             UP             de:1c:94:44:d0:04 <BROADCAST,MULTICAST,UP,LOWER_UP>     ens5             DOWN           02:00:00:00:00:31 <BROADCAST,MULTICAST>     ens6             DOWN           02:00:00:00:01:5b <BROADCAST,MULTICAST>     ens7             DOWN           02:00:00:00:01:5e <BROADCAST,MULTICAST>     ``` 6.
  * **Configure the Instance's IP address**. DHCP is activated by default on new Private Networks, and automatically
  * assigns IP addresses to resources on the network. If you have an older Private Network, [check whether DHCP is
  * activated](https://www.scaleway.com/en/docs/vpc/reference-content/vpc-migration/) and either activate DHCP for
  * automatic IP configuration, or [manually
  * configure](https://www.scaleway.com/en/docs/instances/reference-content/manual-configuration-private-ips/) the
  * network interface on your Instance if necessary. 7. **Delete your Private NIC**, which equates to unplugging your
  * Instance from the Private Network. Use the following call. Ensure you replace `<Instance ID>` with the ID of your
  * Instance, `<Private Network ID>` with the ID of your Private Network, and `<NIC ID>` with the ID of your Private
  * NIC. ```bash     curl -X DELETE \\         -H \"X-Auth-Token: $SCW_SECRET_KEY\" \\         -H \"Content-Type: application/json\" \\         \"https://api.scaleway.com/instance/v1/zones/$SCW_DEFAULT_ZONE/servers/<Instance ID>/private_nics/<NIC ID>\"     ```
  * The network interface disappears from your Instance. 8. **Delete your Private Network**. Use the following call.
  * Ensure you replace `<Private Network ID>` with the ID of your Private Network. ```bash     curl -X DELETE \\         -H \"X-Auth-Token: $SCW_SECRET_KEY\" \\         -H \"Content-Type: application/json\" \\         \"https://api.scaleway.com/vpc/v2/regions/$SCW_DEFAULT_REGION/private-networks/<Private Network ID>\"     ```
  * <Message type=\"note\"> Private Networks must be empty to be deleted. Ensure you have detached all resources and
  * deleted all reserved IPs from your network prior to deletion. </Message> <Message type=\"requirement\"> - You have a
  * [Scaleway account](https://console.scaleway.com/) - You have created an [API
  * key](https://www.scaleway.com/en/docs/iam/how-to/create-api-keys/) and that the API key has sufficient [IAM
  * permissions](https://www.scaleway.com/en/docs/iam/reference-content/permission-sets/) to perform the actions
  * described on this page - You have [installed `curl`](https://curl.se/download.html) </Message> ## Technical
  * information VPC and Private Networks are available in the Paris, Amsterdam and Warsaw regions, which are represented
  * by the following path parameters: * `fr-par` * `nl-ams` * `pl-waw` ## Technical limitations The following
  * limitations apply to Scaleway VPC: - Up to 250 resources can be attached to a Private Network. - A resource can be
  * attached to up to 8 Private Networks. - The following resource types can be attached to a Private Network: -
  * Instances - Elastic Metal servers - Apple silicon - Managed Inference - Load Balancers - Public Gateways - Managed
  * Databases for PostgreSQL and MySQL - Managed Databases for Redis (only at the time of resource creation) -
  * Kubernetes Kapsule (only at the time of resource creation) - The MAC address of an Instance in a Private Network
  * cannot be changed. - Broadcast and multicast traffic, while supported, are heavily rate-limited. ## Going further
  * For more help using Scaleway VPC and Private Networks, check out the following resources: - Our [main
  * documentation](https://www.scaleway.com/en/docs/vpc/) - The #virtual-private-cloud channel on our [Slack
  * Community](https://www.scaleway.com/en/docs/tutorials/scaleway-slack-community/) - Our [support ticketing
  * system](https://www.scaleway.com/en/docs/account/how-to/open-a-support-ticket/).
  *
  * The version of the OpenAPI document: v2
  *
  * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
  * https://openapi-generator.tech Do not edit the class manually.
  */
package scaleway.vpc

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
