/**
 * Managed MongoDB®
 * Managed MongoDB® Databases provide fully-managed document Database Instances, with MongoDB® as a database engine.  Document databases enable users to store and retrieve data in a document format, such as `json`. Compared to traditional relational databases where data is stored in a table-like format, document-type storage supports storing multiple nested keys and values in each document key.  <Message type=\"note\">   Managed MongoDB® is currently in public beta. [Fill out the form](https://www.scaleway.com/en/managed-mongodb-form/) to be notified of the General Availability or if you want to get in touch with our team about Managed MongoDB®. </Message>     ## Concepts  Refer to our [dedicated concepts page](https://www.scaleway.com/en/docs/managed-mongodb-databases/concepts/) to find definitions of the different terms referring to Managed MongoDB®.     ## Quickstart  1. Configure your environment variables.     <Message type=\"note\">     This is an optional step that seeks to simplify your usage of the APIs.     </Message>      ```bash     export SCW_ACCESS_KEY=\"<API access key>\"     export SCW_SECRET_KEY=\"<API secret key>\"     export SCW_REGION=\"<Scaleway region>\"     ``` 2. Edit the POST request payload you will use to create your Database Instance. Replace the parameters in the following example:     ```json       '{       \"project_id\": \"d8e65f2b-cce9-40b7-80fc-6a2902db6826\",       \"name\": \"myDB\",       \"node_amount\": 1,       \"user_name\": \"my_initial_user\",       \"password\": \"thiZ_is_v0ry_s3cret\",       \"version\": \"7.0\",       \"tags\": [\"donnerstag\"],       \"node_type\": \"mgdb-pro2-l\",       \"volume\": [         {         \"size_bytes\": 10000000000,         \"type\": \"sbs_5k\"         },       ],       \"endpoints\": [         {           \"public_network\": {}         }       ]       }'     ```     | Parameter        | Description                                                        |     | :--------------- | :----------------------------------------------------------------- |     | `project_id`     | **REQUIRED** The ID of the Project you want to create your Database Instance in. To find your Project ID you can **[list the projects](/api/account#path-projects-list-all-projects-of-an-organization)** or consult the **[Scaleway console](https://console.scaleway.com/project/settings)**. |     | `name`           | **REQUIRED** Name of the Database Instance                                          |     | `node_amount`           | **REQUIRED** Number of nodes in the Database Instance. You can select either 1 or 3.                             |     | `user_name`      | **REQUIRED** Identifier of the default user, which is created concurrently with the Database Instance |     | `password`       | **REQUIRED** Password for the default user |     | `version`         | **REQUIRED** Version ID of the MongoDB® engine. To check the list of available version you can use the following endpoint: `https://api.scaleway.com/mongodb/v1/regions/$SCW_REGION/versions`                                           |     | `tags`           | The list of tags `[\"tag1\", \"tag2\", ...]` that will be associated with the Database Instance. Tags can be appended to the query of the [List Database Instances](#path-instances-list-mongodbtm-database-instances) call to show results for only the Database Instances using a specific tag. You can also combine tags to list Database Instances that possess all the appended tags. |     | `node_type`      | **REQUIRED** The node type. To check the list of available node types you can use the following endpoint: `https://api.scaleway.com/mongodb/v1/regions/$SCW_REGION/node-types`                             |     | `volume.type`    | **REQUIRED** Type of volume where data is stored. You can specify either `sbs_5k` or `sbs_15k`. The default value is `sbs_5k` |     | `volume.size_bytes`    | **REQUIRED** Volume size expressed in bytes. For example 30GB is expressed as 30000000000 |     | `endpoints`    | **REQUIRED** Network connection point that allows you to access and interact with your managed Database Instance. You can leave this empty as it will be automatically generated. It takes on the following format: `{instance_id}.mgdb.{region}.scw.cloud\"`  |  3. Create a Database Instance by running the following command. Make sure you include the payload you edited in the previous step.     ```bash     curl -X POST \\       -H \"X-Auth-Token: $SCW_SECRET_KEY\" \\       \"Content-Type: application/json\" \\       https://api.scaleway.com/mongodb/v1/regions/$SCW_REGION/instances \\       -d '{       \"project_id\": \"d8e65f2b-cce9-40b7-80fc-6a2902db6826\",       \"name\": \"myDB\",       \"node_amount\": 1,       \"user_name\": \"my_initial_user\",       \"password\": \"thiZ_is_v0ry_s3cret\",       \"version\": \"7.0\",       \"tags\": [\"donnerstag\"],       \"node_type\": \"mgdb-pro2-l\",       \"volume\": [         {         \"size_bytes\": 10000000000,         \"type\": \"sbs_5k\"         }       ],       \"endpoints\":[         {           \"public_network\": {}         }       ],       }'     ``` 4. List your Database Instances.     ```bash     curl -X GET \\       -H \"Content-Type: application/json\" \\       -H \"X-Auth-Token: $SCW_SECRET_KEY\" https://api.scaleway.com/mongodb/v1/regions/$SCW_REGION/instances     ```      You should get a response like the following:      <Message type=\"note\">     This is a response example, the UUIDs and IP address displayed are not real.     </Message>      ```json     {       \"id\": \"ffc473a3-250a-40a1-8d12-0d8c47b2ac63\",       \"name\": \"cluster\",       \"project_id\": \"d8e65f2b-cce9-40b7-80fc-6a2902db6826\",       \"status\": \"provisioning\",       \"version\": \"7.0\",       \"tags\": [],       \"settings\": [],       \"node_amount\": 1,       \"node_type\": \"mgdb-pro2-l\",       \"volume\": {         \"type\": \"sbs_5k\",         \"size_bytes\": 10000000000       },       \"endpoints\": [         {           \"id\": \"1210e327-5f6e-4757-834d-483729dcd330\",           \"ips\": [],           \"dns_records\": [             \"ffc473a3-250a-40a1-8d12-0d8c47b2ac63.mgdb.fr-par.scw.cloud\"           ],           \"port\": 27017,           \"public_network\": {}         }       ],       \"created_at\": \"2024-10-17T12:26:46.473753Z\",       \"region\": \"fr-par\"     }      ``` 5. Retrieve your Database Instance ID from the response. 6. Get your TLS certificate in the console. 7. Connect to your Database Instance with the `mongosh` client.     ```bash     mongosh \"mongodb+srv://{database_instance_id}.mgdb.{region}.scw.cloud\" --tlsCAFile {your_certificate.pem} -u {username}     ```     <Message type=\"note\">     Alternatively, you can connect using different clients. Refer to the [How to connect to a MongoDB Database Instance](https://www.scaleway.com/en/docs/managed-mongodb-databases/how-to/connect-database-instance/) documentation page for more information.     </Message>  8. Enter the database password that you defined upon creation.  You are now connected to your Managed Database.   <Message type=\"requirement\"> To perform the following steps, you must first ensure that:   - you have an account and are logged into the [Scaleway console](https://console.scaleway.com/organization)   - you have created an [API key](https://www.scaleway.com/en/docs/iam/how-to/create-api-keys/) and that the API key has sufficient [IAM permissions](https://www.scaleway.com/en/docs/iam/reference-content/permission-sets/) to perform the actions described on this page.   - you have [installed `curl`](https://curl.se/download.html) </Message>   ## Technical Information  ### Regions  Scaleway's infrastructure is spread across different [regions and Availability Zones](https://www.scaleway.com/en/docs/account/reference-content/products-availability/).  Currently, Managed MongoDB® is available only in the Paris region, which is represented by the following path parameter:  - `fr-par`   ### MongoDB® specifications  #### User Management  - Currently, a single user is created when you create your Database Instance. This user has an `admin` role. - Users with an `admin` role have access to all logical databases and can create new ones.  ## Going Further  For more information about Managed MongoDB®, you can check out the following pages:  * [Managed MongoDB® Documentation](https://www.scaleway.com/en/docs/managed-mongodb-databases/) * [Scaleway Slack Community](https://scaleway-community.slack.com/) join the #database channel * [Contact our support team](https://console.scaleway.com/support/tickets).
 *
 * The version of the OpenAPI document: v1
 * 
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */
package scaleway.mongodb

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
