/** IAM API Identity and Access Management (IAM) allows you to share access to the management of your Scaleway resources
  * and Organization settings, in a controlled and secure manner. With IAM, you can invite other users to your
  * Organization, as well as create IAM applications which represent non-human users with their own API keys. You define
  * permissions for users and applications in your Organization via highly customizable policies. Policies let you
  * specify exactly what rights users and applications (or groups of users and applications) should have within your
  * Organization. ## Concepts Refer to our [dedicated IAM concepts page](https://www.scaleway.com/en/docs/iam/concepts/)
  * to find definitions of the different terms referring to IAM. ## Quickstart 1. Configure your environment variables. ```bash     export ACCESS_KEY=\"<access-key>\"     export SECRET_KEY=\"<secret-key>\"     export REGION=\"<region>\"     ``` 2.
  * Create an application. Replace the parameter values in the request payload with the details of your new application.
  * <Message type=\"note\"> The UUIDs used in the following code examples are not real </Message> ```bash     curl -X POST \\       -H \"Content-Type: application/json\" \\       -H \"X-Auth-Token: $SCW_SECRET_KEY\" https://api.scaleway.com/iam/v1alpha1/applications \\       -d '{         \"name\": \"prod1\",         \"organization_id\": \"c6842bac-7938-4c04-9e03-f48147eee1f1\",         \"description\": \"this is my new application\"         }'     ``` |
  * Parameter | Description | | :--------------- | :-------------------------------------- | | `name` | **REQUIRED** The
  * name of your new application | | `organization_id`| The ID of your Scaleway Organization | | `description` | The
  * description of your application | 3. Retrieve your application ID from the response. ```json     {       \"id\": \"950dde46-5cba-427d-a4f5-ce5a8a79717c\",       \"name\": \"prod1\",       \"description\": \"this is my new application\",       \"created_at\": \"2023-03-08T12:34:56.123456Z\",       \"updated_at\": \"2023-03-08T12:34:56.123456Z\",       \"organization_id\": \"c6842bac-7938-4c04-9e03-f48147eee1f1\",       \"editable\": \"true\",       \"nb_api_keys\": \"0\"     }     ``` 4.
  * Create a policy. Replace the parameter values in the request payload with the details of your new application,
  * including the application ID retrieved in the previous step. ```bash     curl -X POST \\       -H \"Content-Type: application/json\" \\       -H \"X-Auth-Token: $SCW_SECRET_KEY\" https://api.scaleway.com/iam/v1alpha1/policies \\       -d '{       \"name\": \"policy-prod1\",       \"description\": \"This policy grants full access to IAM in my Organization to application prod1\",       \"organization_id\": \"c6842bac-7938-4c04-9e03-f48147eee1f1\",       \"rules\": [         {           \"permission_set_names\": [             \"IAMManager\"           ],           \"organization_id\": \"c6842bac-7938-4c04-9e03-f48147eee1f1\"         }       ],       \"application_id\": \"950dde46-5cba-427d-a4f5-ce5a8a79717c\"     }'     ``` |
  * Parameter | Description | | :--------------- | :-------------------------------------- | | `name` | **REQUIRED** The
  * name of your new application | | `description`| The description of your policy | | `organization_id`| The ID of your
  * Scaleway Organization | | `rules`| The [rules](https://www.scaleway.com/en/docs/iam/reference-content/policy/#rules)
  * of your policy | | `permission_set_names` | The permission sets you want to grant. You can either [list all
  * permission sets](#path-permission-sets-list-permission-sets) or find a complete list in the [permission sets
  * documentation page](https://www.scaleway.com/en/docs/iam/reference-content/permission-sets/) | | `organization_id`|
  * The ID of the Scaleway Organization where you want your permission sets to apply. You can add one as the
  * [scope](https://www.scaleway.com/en/docs/iam/reference-content/policy/#scope) of your policy | | `application_id`|
  * The ID of your application | <Message type=\"note\"> To learn more about IAM policies, refer to our dedicated [IAM
  * policies reference page](https://www.scaleway.com/en/docs/iam/reference-content/policy/). </Message> 5. Create an
  * API key for your application. ```bash     curl -X POST \\       -H \"Content-Type: application/json\" \\       -H \"X-Auth-Token: $SCW_SECRET_KEY\" https://api.scaleway.com/iam/v1alpha1/api-keys \\       -d '{         \"application_id\": \"950dde46-5cba-427d-a4f5-ce5a8a79717c\",         \"expires_at\": \"2023-12-22T12:34:56.123456Z\",         \"default_project_id\": \"2aeadddc-c589-4784-8ef5-fae989a4bac8\",         \"description\": \"This is an API key for prod1\"       }'     ``` |
  * Parameter | Description | | :--------------- | :----------------------------------------------------------------- | |
  * `application_id` | The ID of your application | | `expires_at` | **OPTIONAL** The expiration date of your API key| |
  * `default_project_id` | **OPTIONAL** The Project ID of your preferred Project, to use with Object Storage. If no
  * Project ID is specified, the default project is used. Refer to the [Using API Keys with Object Storage documentation
  * page](https://www.scaleway.com/en/docs/iam/api-cli/using-api-key-object-storage/) | | `description` | The
  * description of your API key | 6. Retrieve your access and secret keys from the response. <Message type=\"note\"> The
  * secret key is only showed once. Make sure that you copy and store both keys somewhere safe. </Message> You can now
  * have an IAM configuration fully set up and can begin working on your Scaleway projects. <Message
  * type=\"requirement\"> To perform the following steps, you must first ensure that:<br /><br /> - you have an account
  * and are logged into the [Scaleway console](https://console.scaleway.com/organization) - you have created an [API
  * key](https://www.scaleway.com/en/docs/iam/how-to/create-api-keys/) and that the API key has sufficient [IAM
  * permissions](https://www.scaleway.com/en/docs/iam/reference-content/permission-sets/) to perform the actions
  * described on this page. - you have [installed `curl`](https://curl.se/download.html) </Message> ## Technical
  * Limitations * Currently, IAM users cannot be created within Scaleway Organizations, they can only be invited to join
  * them. Refer to the [Users, groups and applications reference
  * page](https://www.scaleway.com/en/docs/iam/reference-content/users-groups-and-applications/#users) to learn more
  * about users. * Access management at resource level is not yet available. You can currently scope the permission sets
  * to a Project or to an Organization. Refer to the [Permission sets reference
  * page](https://www.scaleway.com/en/docs/iam/reference-content/permission-sets/) to learn more about permission sets.
  * * Explicit deny permissions are not yet available. You can currently only explicitly allow access to different
  * products or Organization management features. ## Going Further For more information about IAM, you can check out the
  * following pages: * [Identity and Access Management
  * Documentation](https://www.scaleway.com/en/docs/iam/reference-content/overview/) * [Identity and Access Management
  * FAQ](https://www.scaleway.com/en/docs/iam/faq/) * [Scaleway Slack Community](https://scaleway-community.slack.com/)
  * join the #iam channel * [Contact our support team](https://console.scaleway.com/support/tickets) * [Scaleway CLI for
  * IAM](https://github.com/scaleway/scaleway-cli/blob/master/docs/commands/iam.md) * [Scaleway Provider Terraform
  * Documentation for IAM](https://registry.terraform.io/providers/scaleway/scaleway/latest/docs/resources/iam_api_key).
  *
  * The version of the OpenAPI document: v1alpha1
  *
  * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
  * https://openapi-generator.tech Do not edit the class manually.
  */
package scaleway.iam

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
