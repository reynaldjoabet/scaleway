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
package scaleway.lb

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
