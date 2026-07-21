/**
 * Managed Database for PostgreSQL and MySQL API
 * Managed Database for PostgreSQL and MySQL provides fully-managed relational Database Instances, with MySQL or PostgreSQL as database engines. The resource allows you to focus on development rather than administration or configuration. It comes with a high-availability mode, data replication, and automatic backups.  Compared to traditional database management, which requires customers to provide their infrastructure and resources to manage their databases, Managed Database for PostgreSQL and MySQL Instance offers the user access to Database Instances without setting up the hardware or configuring the software. Scaleway handles the provisioning, manages the configuration, and provides useful features as high availability, automated backup, user management, and more.     ## Concepts  Refer to our [dedicated concepts page](https://www.scaleway.com/en/docs/managed-databases-for-postgresql-and-mysql/concepts/) to find definitions of the different terms referring to Managed Database for PostgreSQL and MySQL.     ## Quickstart  1. Configure your environment variables.     <Message type=\"note\">     This is an optional step that seeks to simplify your usage of the APIs.     </Message>      ```bash     export SCW_ACCESS_KEY=\"<API access key>\"     export SCW_SECRET_KEY=\"<API secret key>\"     export SCW_REGION=\"<Scaleway region>\"     ``` 2. Edit the POST request payload you will use to create your Database Instance. Replace the parameters in the following example:     ```json       '{       \"project_id\": \"d8e65f2b-cce9-40b7-80fc-6a2902db6826\",       \"name\": \"myDB\",       \"engine\": \"PostgreSQL-15\",       \"tags\": [\"donnerstag\"],       \"is_ha_cluster\": true,       \"node_type\": \"db-pro2-xxs\",       \"disable_backup\": false,       \"user_name\": \"my_initial_user\",       \"password\": \"thiZ_is_v0ry_s3cret\",       \"volume_type\": \"sbs_5k\",       \"volume_size\": \"30000000000\"       }'     ```      | Parameter        | Description                                                                                                                                                                                                                                                                                                                                                                          |     | :--------------- |:-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|     | `project_id`     | The ID of the Project you want to create your Database Instance in. To find your Project ID you can **[list the projects](/api/account/project-api/#path-projects-list-all-projects-of-an-organization)** or consult the **[Scaleway console](https://console.scaleway.com/project/settings)**.                                                                                                   |     | `engine`         | **REQUIRED** Version ID of the database engine. To check the list of available engines you can use the following endpoint: `https://api.scaleway.com/rdb/v1/regions/$SCW_REGION/database-engines`                                                                                                                                                                                     |     | `name`           | Name of the Database Instance                                                                                                                                                                                                                                                                                                                                                        |     | `node_type`      | **REQUIRED** The node type. To check the list of available node types you can use the following endpoint: `https://api.scaleway.com/rdb/v1/regions/$SCW_REGION/node-types`                                                                                                                                                                                                           |     | `is_ha_cluster`  | **BOOLEAN** Defines whether High Availability is enabled for the Database Instance                                                                                                                                                                                                                                                                                                   |     | `disable_backup` | **BOOLEAN** Defines whether automated backups are disabled for the Database Instance                                                                                                                                                                                                                                                                                                 |     | `tags`           | The list of tags `[\"tag1\", \"tag2\", ...]` that will be associated with the Database Instance. Tags can be appended to the query of the [List Database Instances](#path-database-instances-list-database-instances) call to show results for only the Database Instances using a specific tag. You can also combine tags to list Database Instances that possess all the appended tags. |     | `user_name`      | **REQUIRED** Identifier of the default user, which is created concurrently with the Database Instance                                                                                                                                                                                                                                                                                |     | `password`       | **REQUIRED** Password for the default user                                                                                                                                                                                                                                                                                                                                           |     | `volume_type`    | Type of volume where data is stored. You can specify either local volume (`lssd`) or block volume (`bssd`, `sbs_5k` or `sbs_15k`). The default value is `lssd`                                                                                                                                                                                                                       |     | `volume_size`    | Volume size when volume_type is `bssd`, `sbs_5k` or `sbs_15k`. The value should be expressed in bytes. For example 30GB is expressed as 30000000000                                                                                                                                                                                                                                  | 3. Create a Database Instance by running the following command. Make sure you include the payload you edited in the previous step.     ```bash     curl -X POST \\       -H \"X-Auth-Token: $SCW_SECRET_KEY\" \\       \"Content-Type: application/json\" \\       https://api.scaleway.com/rdb/v1/regions/$SCW_REGION/instances \\       -d '{         \"project_id\": \"d8e65f2b-cce9-40b7-80fc-6a2902db6826\",         \"name\": \"myDB\",         \"engine\": \"PostgreSQL-15\",         \"tags\": [\"donnerstag\"],         \"is_ha_cluster\": true,         \"node_type\": \"db-pro2-xxs\",         \"disable_backup\": false,         \"user_name\": \"my_initial_user\",         \"password\": \"thiZ_is_v0ry_s3cret\",         \"volume_type\": \"sbs_5k\",         \"volume_size\": \"30000000000\"       }'     ``` 4. List your Database Instances.     ```bash     curl -X GET \\       -H \"Content-Type: application/json\" \\       -H \"X-Auth-Token: $SCW_SECRET_KEY\" https://api.scaleway.com/rdb/v1/regions/$SCW_REGION/instances     ```      You should get a response like the following:      <Message type=\"note\">     This is a response example, the UUIDs and IP address displayed are not real.     </Message>      ```json     {           \"id\": \"f5122f66-fb50-4cef-aa02-487ef4fc1af0\",           \"name\": \"myDB\",           \"organization_id\": \"895693aa-3915-4896-8761-c2923b008be7\",           \"project_id\": \"d8e65f2b-cce9-40b7-80fc-6a2902db6826\",           \"status\": \"ready\",           \"engine\": \"PostgreSQL-15\",           \"endpoint\": {             \"ip\": \"198.51.100.0\",             \"port\": 22245,             \"name\": null           },           \"tags\": [             \"donnerstag\"           ],           \"settings\": [],           \"backup_schedule\": {             \"frequency\": 24,             \"retention\": 7,             \"disabled\": true           },           \"is_ha_cluster\": true,           \"read_replicas\": [],           \"node_type\": \"db-pro2-xxs\",           \"volume\": {             \"type\": \"sbs_5k\",             \"size\": 30000000000           }           \"created_at\": \"2019-04-19T16:24:52.591417Z\",           \"region\": \"fr-par\"     }     ``` 5. Retrieve your Database Instance IP and port from the response.     <Message type=\"note\">     In the example above, the IP and port are `198.51.100.0` and `22245`, respectively.     </Message> 6. Connect to your Database Instance with the database client of the engine you selected.     For MySQL, run the following command:     ```bash     mysql -h <ip-address> --port <port> -p -u <user_name>     ```      For PostgreSQL, run:     ```bash     psql -h <ip-address> -p <port> -U <username> -d rdb     ```      For the recurring example, the command would look like:      ```bash     psql -h 198.51.100.0 -p 22245 -U my_initial_user -d rdb     ``` 7. Enter the database password that you defined upon creation.  You are now connected to your Managed Database.   <Message type=\"requirement\"> To perform the following steps, you must first ensure that:   - you have an account and are logged into the [Scaleway console](https://console.scaleway.com/organization)   - you have created an [API key](https://www.scaleway.com/en/docs/iam/how-to/create-api-keys/) and that the API key has sufficient [IAM permissions](https://www.scaleway.com/en/docs/iam/reference-content/permission-sets/) to perform the actions described on this page.   - you have [installed `curl`](https://curl.se/download.html) </Message>   ## Technical Information  ### Regions  Scaleway's infrastructure is spread across different [regions and Availability Zones](https://www.scaleway.com/en/docs/account/reference-content/products-availability/).  Managed Database for PostgreSQL and MySQL is available in the Paris, Amsterdam and Warsaw regions, which are represented by the following path parameters:  - `fr-par` - `nl-ams` - `pl-waw`  ### PostgreSQL specifications  #### Versions  Scaleway Database for PostgreSQL supports PostgreSQL versions 11, 12, 13, 14 and 15.  #### System  Different modules are available for installation, including TimescaleDB and PostGIS. Refer to the [Managed Database for PostgreSQL and MySQL FAQ page](https://www.scaleway.com/en/docs/managed-databases-for-postgresql-and-mysql/faq/#which-postgresql-extensions-are-available) for an extensive list of PostgreSQL extensions.  #### Database Management  You can create logical databases through the Scaleway console, the Scaleway APIs or SQL.  - databases created using the Scaleway console or the API are owned by an internal system user. These are called \"managed databases\". - databases created using SQL will be owned by the creator. These are called \"unmanaged databases\".  ### MySQL specifications  #### Versions  Scaleway Database for MySQL supports MySQL 8.  #### System  - only the [InnoDB engine](https://dev.mysql.com/doc/refman/8.0/en/innodb-storage-engine.html) is supported - the [Global Transaction Identifier (GTID)](https://dev.mysql.com/doc/refman/8.0/en/replication-gtids-concepts.html) is enabled. - [`mysql_native_password`](https://dev.mysql.com/doc/refman/8.0/en/native-pluggable-authentication.html) (default) and [`caching_sha2_password`](https://dev.mysql.com/doc/refman/8.0/en/caching-sha2-pluggable-authentication.html) authentication are supported.  #### User Management  - users with an `admin` role have access to all logical databases and can create new ones. - users created via the API are authenticated using the default authentication plugin, which can be changed in the settings.  ## Technical Limitations  ### PostgreSQL  #### User Management  - users with an `admin` role have `CREATEROLE` and `CREATEDB` privileges. - users do NOT have `SUPERUSER` nor `REPLICATION` privileges. - permission management through the Scaleway console or API is only possible for the \"managed databases\".  #### Backup and restoration  Databases that have been backed up and then restored retain the user permission settings in use at the time of backup. If you delete users after backup and then restore your backup in the same database, or if you restore a backup to a different database with different or no users, the permissions configured for them continue to exist, but with no associated owner. This error will put a stop to the restoration process.  To avoid this issue, we recommend you re-create the users you deleted. In the occasion you restore the backup to a new database, you must create new users with the same names.  ## Going Further  For more information about Managed Database for PostgreSQL and MySQL, you can check out the following pages:  * [Managed Database for PostgreSQL and MySQL Documentation](https://www.scaleway.com/en/docs/managed-databases/postgresql-and-mysql/) * [Managed Database for PostgreSQL and MySQL FAQ](https://www.scaleway.com/en/docs/managed-databases-for-postgresql-and-mysql/faq/) * [Scaleway Slack Community](https://scaleway-community.slack.com/) join the #database channel * [Contact our support team](https://console.scaleway.com/support/tickets)  ### How to migrate a database  If you wish to migrate existing databases to a Managed Database for PostgreSQL or MySQL, you can refer to the [Migrating existing databases to a Database Instance](https://www.scaleway.com/en/docs/tutorials/migrate-databases-instance/) tutorial page.  ### Troubleshoooting  #### Disk full status  If your Database Instance uses local storage, your local volume might eventually approach full capacity and shift to `disk_full` mode. This mode grants you enough space to either [upgrade your node type](https://www.scaleway.com/en/docs/managed-databases-for-postgresql-and-mysql/how-to/upgrade-version/#how-to-change-the-node-type) or [clear out space in your volume](https://www.scaleway.com/en/docs/managed-databases-for-postgresql-and-mysql/troubleshooting/disk-full/).
 *
 * The version of the OpenAPI document: v1
 * 
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */
package scaleway.postgremysql

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
