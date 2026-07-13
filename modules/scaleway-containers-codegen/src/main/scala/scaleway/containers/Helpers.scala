/** Serverless Containers API Scaleway Serverless Containers is a «Container As A Service» product which gives users the
  * ability to deploy atomic serverless workloads and only pay for resources used while containers are running. It
  * provides many advantages, such as: - Containers are only executed when an event is triggered, which allows users to
  * save money while code is not running - Auto-Scalability: - Automated `Scaling up and down` based on user
  * configuration (e.g. min: 0, max: 100 replicas of my container). - Automated `Scaling to zero` when a container is
  * not executed, which is cost-effective for the user and saves computing resources for the cloud provider. -
  * Endpoint-only scaling ### Serverless Framework This page explains how to use the Scaleway Containers API, including
  * a quickstart and the full API documentation. However, you may prefer to use the [Serverless Framework
  * plugin](https://github.com/scaleway/serverless-scaleway-functions) enabling users to deploy their serverless
  * workloads much more easily with a single `serverless deploy` command. If what you are looking for is an easy way to
  * deploy your code, you may prefer Serverless Framework. Below, you will find a step-by-step guide on how to create a
  * `namespace`, configure and deploy `containers`, and trigger your `containers` via HTTP and CRON. ## Concepts Refer
  * to our [dedicated concepts page](https://www.scaleway.com/en/docs/serverless/containers/concepts/) to find
  * definitions of the different terms referring to Serverless Containers. ## Quickstart 1. Configure your environment
  * variables. ```bash     export $SCW_SECRET_KEY=\"<Secret key of your token>\"     export SCW_DEFAULT_REGION=\"<Choose your location (pl-waw/nl-ams/fr-par)>\"     export SCW_PROJECT_ID=\"<Your Project ID>\"     ```
  * <Message type=\"tip\"> This is an optional step that seeks to simplify your usage of the Serverless Containers API.
  * See [Regions](#availability-zones) below for help choosing a region. You can find your Project ID in the [Scaleway
  * console](https://console.scaleway.com/Project/settings). </Message> 2. Set the name for your namespace and configure
  * your Project ID: ```bash     curl -X POST \"https://api.scaleway.com/containers/v1beta1/regions/$SCW_DEFAULT_REGION/namespaces\" \\       -H \"accept: application/json\" \\       -H \"X-Auth-Token: $SCW_SECRET_KEY\" \\       -H \"Content-Type: application/json\" \\       -d \"{           \\\"name\\\": \\\"your-namespace-name\\\", \\           \\\"project_id\\\": \\\"$SCW_PROJECT_ID\\\", \\           \\\"environment_variables\\\": {\\\"YOUR_VARIABLE\\\": \\\"content\\\"} \\           }\"     ``` 3.
  * **Copy the response's `id` field**, you will need it for the next steps. For the sake of simplicity, we will save
  * the ID to a variable, which will be used in the following examples: ```bash     export NAMESPACE_ID=\"<your namespace id>\"     ```
  * <Message type=\"note\"> We suppose you already have a working image here. It can be anything which listens on a env
  * variable \\$PORT variable. Note that we run your container as user 1000, not root, so it must be runnable under
  * these conditions. For more information on how to push your image, refer to the [Container Registry
  * documentation](https://www.scaleway.com/developers/api/registry/). </Message> 4. **Edit the POST request payload**
  * to use in the next step to create an Elastic Metal server. Modify the values in the example according to your
  * requirements, using the information in the payload values section to help. For container resource criteria and
  * default values, please refer to the [Containers Limitation
  * documentation](https://www.scaleway.com/en/docs/serverless/containers/reference-content/containers-limitations/). ```json     {       \"namespace_id\": \"string\",       \"name\": \"string\",       \"environment_variables\": {         \"<key>\": \"string\"       },       \"min_scale\": \"integer\",       \"max_scale\": \"integer\",       \"memory_limit\": \"integer\",       \"cpu_limit\": \"integer\",       \"timeout\": \"integer\",       \"privacy\": \"unknown_privacy\",       \"description\": \"string\",       \"registry_image\": \"string\",       \"max_concurrency\": \"integer\",       \"protocol\": \"unknown_protocol\",       \"port\": \"integer\",       \"secret_environment_variables\": [         {           \"key\": \"string\",           \"value\": \"string\"         }       ],       \"http_option\": \"enabled\"     }     ``` |
  * Parameter | Description | | :--------------- | :----------------------------------------------------------------- | |
  * `region` | The region you want to target. Possible values are fr-par, nl-ams and pl-waw. | | `namespace_id`| UUID of
  * the namespace the container belongs to. | | `name`| Name of the container. | | `environment_variables` |
  * **NULLABLE** Environment variables of the container. | | `min_scale` | **NULLABLE** Minimum number of instances to
  * scale the container to. | | `max_scale` | **NULLABLE** Maximum number of instances to scale the container to. | |
  * `memory_limit`| **NULLABLE** Memory limit of the container in MiB. | | `cpu_limit`| **NULLABLE** CPU limit of the
  * container in mvCPU. | | `timeout` | **NULLABLE** Request processing time limit for the container. (in seconds). | |
  * `privacy` | Privacy setting of the container. | | `description` | **NULLABLE** Description of the container. | |
  * `registry_image`| **NULLABLE** Name of the registry image (e.g. \"rg.fr-par.scw.cloud/something/image:tag\"). | |
  * `max_concurrency` | **NULLABLE** Number of maximum concurrent executions of the container. | | `protocol` | Protocol
  * the container uses. Possible values are unknown_protocol, http1 and h2c. The default value is unknown_protocol. | |
  * `port` | **NULLABLE** Port the container listens on. | | `secret_environment_variables` | Secret environment
  * variables of the container. | | `http_option` | Configure how HTTP and HTTPS requests are handled. Possible
  * values:<br /> - `redirected`: Responds to HTTP request with a 301 redirect to ask the clients to use HTTPS.<br /> -
  * `enabled`: Serve both HTTP and HTTPS traffic. | <Message type=\"important\"> All parameters are `required`, except
  * for those marked as nullable. </Message> 5. Run the following command to create your container. Make sure you
  * include the payload you edited in the previous step. ```bash     export REGISTRY_IMAGE=\"rg.fr-par.scw.cloud/myregistrynamespace/mycontainer:latest\"     curl -X POST \\       -H \"X-Auth-Token: $SCW_SECRET_KEY\" \\       \"https://api.scaleway.com/containers/v1beta1/regions/$SCW_DEFAULT_REGION/containers\" \\       -d \"{           \"name\": MyContainer, \\           \"registry_image\": \"$REGISTRY_IMAGE\", \\           \"namespace_id\": \"$NAMESPACE_ID\", \\           \"memory_limit\": 300, \\           \"cpu_limit\": 200, \\           \"min_scale\": 0, \\           \"max_scale\": 20 \\           }\"     ``` 6.
  * Run the following command to deploy your container: ```bash     curl -X POST \\       -H \"X-Auth-Token: $SCW_SECRET_KEY\" \\       \"https://api.scaleway.com/containers/v1beta1/regions/$SCW_DEFAULT_REGION/containers/$CONTAINER_ID/deploy\" \\       -d \"{}\"     ``` 7.
  * Run the following command to trigger your container. ```bash     curl -X GET \\       -H \"X-Auth-Token: $SCW_SECRET_KEY\" \\       \"https://api.scaleway.com/containers/v1beta1/regions/$SCW_DEFAULT_REGION/containers/$CONTAINER_ID\"     export CONTAINER_ENDPOINT=\"<endpoint>\"     curl -X GET \"$CONTAINER_ENDPOINT\"     ``` 8.
  * (optional) Connect to your Cockpit (Serverless Containers Logs dashboard) to see your logs:
  * https://www.scaleway.com/en/docs/observability/cockpit/how-to/access-grafana-and-managed-dashboards/. 9. (optional)
  * Use the following call to destroy a namespace (along with all containers and crons): ```bash     curl -s \\       -H \"X-Auth-Token: $SCW_SECRET_KEY\" \\       -X DELETE \"https://api.scaleway.com/containers/v1beta1/regions/$SCW_DEFAULT_REGION/namespaces/$NAMESPACE_ID\"     ```
  * <Message type=\"requirement\"> To perform the following steps, you must first ensure that: - You have a [Scaleway
  * account](https://console.scaleway.com/) - You have created an [API
  * key](https://www.scaleway.com/en/docs/iam/how-to/create-api-keys/) and that the API key has sufficient [IAM
  * permissions](https://www.scaleway.com/en/docs/iam/reference-content/permission-sets/) to perform the actions
  * described on this page - You have [installed `curl`](https://curl.se/download.html) - You have [installed
  * `jq`](https://stedolan.github.io/jq/) to improve readability of the API outputs </Message> ## Technical information
  * A **Container** in Scaleway Containers consists of multiple components: - **Environment variables**: Users may
  * configure specific environment variables (Database host/credentials for example) which are safely encrypted in our
  * Database, and will be mounted inside your containers. **Note** that environment variables set at `namespace` level
  * will also be mounted (in every container). Environment variables written at `container` level override the ones set
  * at `namespace` level (if two of them bear the same name for example). - **Docker image**: A Docker image contains
  * all the elements required to run your software: code, a runtime environment, tools, scripts, libraries, etc. -
  * **Resources**: Users may decide how much computing resource to allocate to each container -> `Memory Limit` (in MB).
  * We will then allocate the right amount of `CPU` based on Memory Limit choice. The right choice for your container's
  * resources is very important, as you will be billed based on compute usage over time and the number of Containers
  * executions. ### Product features - Fully isolated environments - Scaling to zero (saves money and computing
  * resources while the code is not executed) - High Availability and scalability (automated and configurable, each
  * container may scale automatically according to incoming workloads) - Multiple event sources: - HTTP (request on our
  * gateway will execute the container) - CRON (time-based job, runs according to configurable cron schedule) -
  * Integrated to the Scaleway Container Registry product: - Deploy any docker image from one of your registry namespace -
  * Flexible resources: you can choose values separately for your memory and CPU within a range in respect with maximum
  * and minimum allowed values in [Containers Limitation
  * documentation](https://www.scaleway.com/en/docs/serverless/containers/reference-content/containers-limitations/).
  * ### Regions Serverless Containers is available in the Paris, Amsterdam and Warsaw regions, which are represented by
  * the following path parameters: * `fr-par` * `nl-ams` * `pl-waw` ### CRON A `CRON` is a type of event which triggers
  * a Scaleway Container: it is an `add-on` to your container. CRONs inside Scaleway Containers have the following
  * properties: - `schedule`: UNIX Formatted CRON schedule. Your container will be executed based on this schedule. For
  * example, `5 4 * * 0` means \"execute my container at 04:05 AM every Sunday\" (see this [page from Ubuntu's official
  * documentation](https://doc.ubuntu-fr.org/cron)). The timezone is UTC+0. - `args`: JSON object passed to your
  * container. You can use this property to define data that will be passed to your container's `event.body` object. For
  * Containers, you might handle these arguments as the HTTP request's body. Under the hood, CRON Triggers are
  * [Kubernetes JOBs](https://kubernetes.io/docs/concepts/workloads/controllers/jobs-run-to-completion/) sending HTTP
  * POST requests to your container. ### Authentication By default new containers are `public` meaning that no
  * credentials are required to invoke them. A container can be `private` or `public`. This can be configured through
  * the `privacy` parameter. Calling a `private` container without authentication will return HTTP code `403`. ### Logs
  * Containers logs are sent to the project's
  * [Cockpit](https://www.scaleway.com/en/developers/api/cockpit/regional-api/). The **Serverless Containers Logs**
  * dashboard in Grafana can be used to see containers logs. More complex queries can be done using the \"Explore\"
  * section of Grafana, and LogQL queries: ```logql {resource_type=\"serverless_container\"} ``` Additionally, the loki
  * endpoint (`https://logs.cockpit.fr-par.scw.cloud`) can be used to query programmatically the containers logs using a
  * [token](https://www.scaleway.com/en/docs/observability/cockpit/how-to/create-token/). ## Going further For more help
  * using Scaleway Serverless containers, check out the following resources: * Our [main
  * documentation](https://www.scaleway.com/en/docs/serverless/containers/) * The #serverless-containers channel on our
  * [Slack Community](https://www.scaleway.com/en/docs/tutorials/scaleway-slack-community/) * Our [support ticketing
  * system](https://www.scaleway.com/en/docs/console/account/how-to/open-a-support-ticket/).
  *
  * The version of the OpenAPI document: v1beta1
  *
  * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
  * https://openapi-generator.tech Do not edit the class manually.
  */
package scaleway.containers

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
