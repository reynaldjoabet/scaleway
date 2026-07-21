/**
 * Site-to-Site VPN
 * Scaleway Site-to-Site VPN connects your infrastructure (on-premise, in a private cloud, or in another public cloud) to your Scaleway VPC with IPsec.  Site-to-Site VPN establishes a private & encrypted tunnel over the public internet using IPsec. It allows your remote infrastructure to securely reach resources hosted in your Scaleway Private Networks without requiring dedicated physical links. This managed solution ensures confidentiality and integrity of through traffic, with a deployment that is simple and cost-effective, even across geographically distributed infrastructure.  You can establish one or multiple VPN tunnels depending on your needs, and take advantage of dynamic routing with BGP to ensure high availability and flexibility across your infrastructure.  Once connection is established with your VPC, Site-to-Site VPN allows you to control BGP route propagation: you can fine-tune which IPv4 or IPv6 prefixes are allowed to flow.   <Message type=\"note\"> This product is currently in [Public Beta](https://www.scaleway.com/en/betas/). </Message>   ## Concepts  ### VPN Gateway  A VPN gateway is a managed resource that serves IPsec tunnels between your external infrastructure and your Scaleway VPC. Each connection within the gateway represents an IPsec tunnel established over the public internet. A single VPN gateway can host multiple connections.  ### Customer Gateway  A customer gateway is a logical resource that represents your on-premises network device. It acts as the endpoint of the IPsec tunnel established with the Scaleway VPN gateway. Each customer gateway can be configured with one public IPv4 address, one public IPv6 address, and the ASN of the BGP router used to establish the BGP session over the IPsec tunnel.  This resource is required to define the remote side of the VPN tunnel. Its information will be used during the setup of the connection.  ### Connection  A connection represents the bridge between your VPN gateway and your customer gateway. One connection can support up to two IPsec tunnels: one over IPv4 and one over IPv6. This allows for a highly available tunnel configuration, provided that both the VPN gateway and the customer gateway have configured public IP addresses through Scaleway resources, using the correct IP version on both ends.     ## Quickstart  1. Configure your environment variables.     <Message type=\"note\">    This is an optional step that seeks to simplify your usage of the Site-to-Site VPN API.    </Message>     ```bash    export SCW_SECRET_KEY=\"<API secret key>\"    export SCW_DEFAULT_REGION=\"<Scaleway default region>\"    export SCW_PROJECT_ID=\"<Scaleway Project ID>\"    ```  2. **Choose a VPN gateway type**: VPN gateways come in different shapes, sizes, and pricing. When you create your VPN gateway, you need to specify the required gateway type in the request. Use the following call to get a list of available VPN gateway offer types and their details:      ```bash     curl -X GET \\         -H \"X-Auth-Token: $SCW_SECRET_KEY\" \\         -H \"Content-Type: application/json\" \\         \"https://api.scaleway.com/s2s-vpn/v1alpha1/regions/$SCW_DEFAULT_REGION/vpn-gateway-types\"     ```  3. **Create a VPN gateway**: run the following command to create a VPN Gateway. You can customize the details in the payload to your needs, using the table below to help.     ```bash    curl -X POST \\      -H \"X-Auth-Token: $SCW_SECRET_KEY\" \\      -H \"Content-Type: application/json\" \\      \"https://api.scaleway.com/s2s-vpn/v1alpha1/regions/$SCW_DEFAULT_REGION/vpn-gateways\" \\      -d '{        \"project_id\": \"'\"$SCW_PROJECT_ID\"'\",        \"name\": \"My New Gateway\",        \"tags\": [\"test\", \"another tag\"],        \"gateway_type\": \"VGW-S\",        \"private_network_id\": \"00cfd222-2402-4598-9438-b12b995e9e80\"      }'    ```     | Parameter          | Description                                         | Valid values OR Example                   |    | ------------------ | --------------------------------------------------- | ----------------------------------------- |    | project_id         | ID of project                                       | `any valid project UUID`                  |    | name               | Name for VPN Gateway                                | `desired name for VPN Gateway`            |    | tags               | Tags for VPN Gateway                                | `a list of tags`                          |    | gateway_type       | Offer type for VPN Gateway                          | `any valid offer type string, e.g. VGW-S` |    | private_network_id | ID of a Private Network (cannot be detach later)    | `any valid private network UUID`          |  4. **Get a list of your VPN gateways**: run the following command to get a list of all the VPN gateways in your account, with their details:     ```bash    curl -X GET \\      -H \"Content-Type: application/json\" \\      -H \"X-Auth-Token: $SCW_SECRET_KEY\" \\      \"https://api.scaleway.com/s2s-vpn/v1alpha1/regions/$SCW_DEFAULT_REGION/vpn-gateways\"    ```  5. **Create a customer gateway**: run the following command to create a customer gateway. You can customize the details in the payload to your needs, using the table below to help.     ```bash    curl -X POST \\      -H \"X-Auth-Token: $SCW_SECRET_KEY\" \\      -H \"Content-Type: application/json\" \\      \"https://api.scaleway.com/s2s-vpn/v1alpha1/regions/$SCW_DEFAULT_REGION/customer-gateways\" \\      -d '{        \"project_id\": \"'\"$SCW_PROJECT_ID\"'\",        \"name\": \"My Device\",        \"tags\": [\"test\", \"another tag\"],        \"ipv4_public\": \"51.158.0.1\",        \"asn\": \"11111\"      }'    ```     | Parameter   | Description                     | Valid values OR Example                       |    | ----------- | ------------------------------- | --------------------------------------------- |    | project_id  | ID of project                   | `any valid project UUID`                      |    | name        | Name for Customer Gateway       | `desired name for Connection`                 |    | tags        | Tags for Customer Gateway       | `a list of tags`                              |    | ipv4_public | Public IPv4 of Customer Gateway | `any valid public ipv4 ip`                    |    | asn         | BGP ASN of Customer Gateway     | `any valid asn except reserved scw ASN 12876` |  6. **Create a routing policy**: A routing policy is required for each traffic type (IPv4 and/or IPv6) to be supported over the connection.     ```bash    curl -X POST \\      -H \"X-Auth-Token: $SCW_SECRET_KEY\" \\      -H \"Content-Type: application/json\" \\      \"https://api.scaleway.com/s2s-vpn/v1alpha1/regions/$SCW_DEFAULT_REGION/routing-policies\" \\      -d '{        \"project_id\": \"'\"$SCW_PROJECT_ID\"'\",        \"name\": \"my-rp-v4\",        \"tags\": [\"test\", \"another tag\"],        \"is_ipv6\": false,        \"prefix_filter_in\": [          \"10.0.0.0/8\"        ],        \"prefix_filter_out\": [          \"172.16.12.0/24\"        ]      }'    ```     | Parameter           | Description                                                                               | Valid values OR Example        |    | ------------------- | ----------------------------------------------------------------------------------------- | ------------------------------ |    | project_id          | ID of Project                                                                             | `any valid project UUID`       |    | name                | Name for routing policy                                                                   | `desired name for Connection`  |    | tags                | Tags for routing policy                                                                   | `a list of tags`               |    | is_ipv6             | Whether routing policy should be IPv6 or IPv4                                             | `true or false`                |    | prefix_filter_in    | IP prefixes to accept from the customer gateway (ranges of route announcements to accept) | `a list of IP subnets`         |    | prefix_filter_out   | IP prefixes to advertise to the customer gateway (ranges of routes to advertise)          | `a list of IP subnets`         |  7. **Create a connection between a VPN gateway and a customer gateway**: When your VPN gateway has been provisioned and has **active** status, you must create a connection.     ```bash    curl -X POST \\      -H \"Content-Type: application/json\" \\      -H \"X-Auth-Token: $SCW_SECRET_KEY\" \\      \"https://api.scaleway.com/s2s-vpn/v1alpha1/regions/$SCW_DEFAULT_REGION/connections\" \\      -d '{        \"project_id\": \"'\"$SCW_PROJECT_ID\"'\",        \"name\": \"My New Connection\",        \"tags\": [\"test\", \"another tag\"],        \"vpn_gateway_id\": \"ec5b8281-0c4f-49ea-a8fd-bd37554f5896\",        \"customer_gateway_id\": \"beba6285-b502-4579-82d6-2815fb1ef1f1\",        \"ikev2_ciphers\": [{\"encryption\": \"aes256\", \"integrity\": \"sha256\", \"dh_group\": \"curve25519\"}],        \"esp_ciphers\": [{\"encryption\": \"aes256gcm\", \"dh_group\": \"curve25519\"}],        \"bgp_config_ipv4\": {          \"routing_policy_id\": \"3fe0612d-6d61-41a8-87f4-243900e07616\"        }      }'    ```     | Parameter           | Description                                                              | Valid values OR Example                   |    | ------------------- | ------------------------------------------------------------------------ | ----------------------------------------- |    | project_id          | ID of project                                                            | `any valid project UUID`                  |    | name                | Name for Connection                                                      | `desired name for Connection`             |    | tags                | Tags for Connection                                                      | `a list of tags`                          |    | vpn_gateway_id      | ID of a VPN Gateway                                                      | `any valid VPN Gateway UUID`              |    | customer_gateway_id | ID of a Customer Gateway                                                 | `any valid Customer Gateway UUID`         |    | routing_policy_id   | ID of a Routing Policy (same parameter available for bgp_config_ipv6)    | `any valid Routing Policy UUID`           |     Refer to the [documentation](https://www.scaleway.com/en/docs/site-to-site-vpn/) for help with these steps if necessary.  7. **Configure your customer gateway device**: Configure your real physical or software-based networking device, located on the remote network you want to connect to your Scaleway VPC. It is the physical device represented by your customer gateway. To successfully configure the device, you will need the public IP address(es) of the VPN gateway, the Scaleway ASN (12876) and the pre-shared key of the connection.  8. **Enable route propagation**: Enable route propagation to prompt the two gateways to initiate BGP sessions and share routing information. This is the final step in allowing traffic to flow across the Site-to-Site VPN connection. Ensure that you replace `{connection-id}` in the URL with the ID of the connection on which you want to enable route propagation.     ```bash    curl -X POST \\      -H \"Content-Type: application/json\" \\      -H \"X-Auth-Token: $SCW_SECRET_KEY\" \\      \"https://api.scaleway.com/s2s-vpn/v1alpha1/regions/$SCW_DEFAULT_REGION/connections/{connection-id}/enable-route-propagation\"    ```  9. **Delete your VPN gateway**: run the following command to delete a VPN gateway when you no longer need it. Ensure that you replace `{vpn-gateway-id}` in the URL with the ID of the VPN gateway you want to delete.     ```bash    curl -X DELETE \\      -H \"Content-Type: application/json\" \\      -H \"X-Auth-Token: $SCW_SECRET_KEY\" \\      \"https://api.scaleway.com/s2s-vpn/v1alpha1/regions/$SCW_DEFAULT_REGION/vpn-gateways/{vpn-gateway-id}\"    ```   <Message type=\"requirement\">  - You have a [Scaleway account](https://console.scaleway.com/) - You have created an [API key](https://www.scaleway.com/en/docs/iam/how-to/create-api-keys/) and that the API key has sufficient [IAM permissions](https://www.scaleway.com/en/docs/iam/reference-content/permission-sets/) to perform the actions described on this page - You have [installed `curl`](https://curl.se/download.html)   </Message>     ### Regions  The Scaleway Site-to-Site VPN is a **regional** API, meaning that each call must specify in its path parameters the Region for the resources concerned by the call.  The following Regions are available for Site-to-Site VPN:  | Name      | API ID                | |-----------|-----------------------| | Paris     | `fr-par` | | Milan     | `it-mil` | | Amsterdam | `nl-ams` | | Warsaw    | `pl-waw` |  ## Going further  For more help using Scaleway Site-to-Site VPN, check out the following resources:  - Our [main documentation](https://www.scaleway.com/en/docs/site-to-site-vpn/) - The `#virtual-private-cloud` channel on our [Slack Community](https://www.scaleway.com/en/docs/tutorials/scaleway-slack-community/).
 *
 * The version of the OpenAPI document: v1alpha1
 * 
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */
package scaleway.s2svpn

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
