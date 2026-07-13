/** Secret Manager API Scaleway’s Secret Manager allows you to conveniently store, access and share sensitive data such
  * as passwords, API keys and certificates. With Secret Manager you can manage secrets which are logical containers
  * made up of zero or more immutable versions, that hold sensitive data. Your data is encrypted both in transit and at
  * rest and it is automatically replicated to multiple zones within your region of choice. ## Concepts Refer to our
  * [dedicated concepts page](https://www.scaleway.com/en/docs/identity-and-access-management/secret-manager/concepts/)
  * to find definitions of the different terms referring to Secret Manager. ## Quickstart 1. **Configure your
  * environment variables.** <Message type=\"note\"> This is an optional step that seeks to simplify your usage of the
  * API. </Message> ```bash     export SCW_ACCESS_KEY=\"<API access key>\"     export SCW_SECRET_KEY=\"<API secret key>\"     export SCW_PROJECT_ID=\"<Scaleway Project ID>     ``` 2.
  * **Create an opaque secret** in the root (`/`) folder. ```bash     curl \"https://api.scaleway.com/secret-manager/v1beta1/regions/$REGION/secrets\" \\       -H \"Content-Type: application/json\" \\       -H \"X-Auth-Token: $SCW_SECRET_KEY\" \\       -d '{         \"name\": \"my-secret\",         \"project_id\": \"$PROJECT_ID\"       }'     ```
  * <Message type=\"note\"> The `opaque` type is the default secret type. If you want to create another secret type
  * (e.g., for certificates or credentials), specify the `type` field in the request. Refer to our [concepts
  * page](https://www.scaleway.com/en/docs/identity-and-access-management/secret-manager/concepts/) for supported types.
  * </Message> 3. **Create a secret version**. Run the following command to create a version and add your secret value: ```bash     curl \"https://api.scaleway.com/secret-manager/v1beta1/regions/$REGION/secrets/<SECRET_ID>/versions\" \\       -H \"X-Auth-Token: $SCW_SECRET_KEY\" \\       -d \"{\\\"your-data\\\":\\\"$(echo -n \"y0ur-p@sSw0Rd_\" | base64)\\\"}\"     ```
  * <Message type=\"note\"> When creating a secret with data, two separate API calls are required: `CreateSecret`: This
  * initializes an empty container for your secret. `CreateSecretVersion`: This associates the data with the secret as a
  * version. The [Scaleway console](https://console.scaleway.com/) automates these two steps for you, but when using the
  * API, you must perform both calls in sequence. </Message> 4. Create a `basic_credentials` secret type in the root
  * (`/`) folder: ```bash     curl \"https://api.scaleway.com/secret-manager/v1beta1/regions/$REGION/secrets\" \\       -H \"Content-Type: application/json\" \\       -H \"X-Auth-Token: $SCW_SECRET_KEY\" \\       -d '{         \"name\": \"my-secret\",         \"type\": \"basic_credentials\",         \"project_id\": \"$PROJECT_ID\"       }'     ``` 5.
  * Create a version for your `basic_credentials` secret to store your credentials in your secret version: ```bash     curl \"https://api.scaleway.com/secret-manager/v1beta1/regions/$REGION/secrets/<SECRET_ID>/versions\" \\       -H \"Content-Type: application/json\" \\       -H \"X-Auth-Token: $SCW_SECRET_KEY\" \\       -d '{        \"data\": \"'\"$(echo -n \"{\\\"username\\\": \\\"my-username\\\", \\\"password\\\": \\\"my-password\\\"}\" | base64)\"'\"}'     ``` 6.
  * **Access data from your latest secret version**. Run the following command to access the data of your most recent
  * secret version: ```bash     curl \"https://api.scaleway.com/secret-manager/v1beta1/regions/$REGION/secrets/<SECRET_ID>/versions/latest/access\" \\       -H \"Content-Type: application/json\" \\       -H \"X-Auth-Token: $SCW_SECRET_KEY\"     ```
  * <Message type=\"note\"> - The command above returns a base64-decoded JSON with your username and password if you
  * have created the `basic_credentials` secret or any data you may have stored in other secrets. - Requests can either
  * target a specific version or the latest. </Message> <Message type=\"requirement\"> - You have your [Organization and
  * your Project ID](https://console.scaleway.com/project/settings) - You have [created an API
  * key](https://www.scaleway.com/en/docs/iam/how-to/create-api-keys/) - You have [installed
  * `curl`](https://curl.se/download.html) - You have created an [API
  * key](https://www.scaleway.com/en/docs/iam/how-to/create-api-keys/) and that the API key has sufficient [IAM
  * permissions](https://www.scaleway.com/en/docs/iam/reference-content/permission-sets/) to perform the actions
  * described on this page </Message> ## Technical information ### Regions Scaleway's infrastructure spans different
  * [regions and Availability Zones](https://www.scaleway.com/en/docs/account/reference-content/products-availability/).
  * Secret Manager is available in the Paris, Amsterdam and Warsaw regions, which are represented by the following path
  * parameters: - fr-par - nl-ams - pl-waw ## Technical limitations - Operations on secrets and versions are limited to
  * CRUDL - A secret's payload size is limited to 64KiB ## Going further For more information about Secret Manager, you
  * can check out the following pages: * [Secret Manager
  * Documentation](https://www.scaleway.com/en/docs/identity-and-access-management/secret-manager/) * [Scaleway Slack
  * Community](https://scaleway-community.slack.com/) join the #secret-manager channel * [Contact our support
  * team](https://console.scaleway.com/support/tickets).
  *
  * The version of the OpenAPI document: v1beta1
  *
  * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
  * https://openapi-generator.tech Do not edit the class manually.
  */
package scaleway.secretmanager.api

import scaleway.secretmanager.models.AccessSecretVersionResponse
import scaleway.secretmanager.models.CreateSecretVersionRequest
import scaleway.secretmanager.models.ListSecretVersionsResponse
import scaleway.secretmanager.models.SecretVersion
import scaleway.secretmanager.models.Status.*
import scaleway.secretmanager.models.Status
import scaleway.secretmanager.models.UpdateSecretVersionRequest
import com.github.plokhotnyuk.jsoniter_scala.circe.JsoniterScalaCodec.*
import scaleway.secretmanager.JsonSupport.{*, given}
import scaleway.secretmanager.FormSerializable
import scaleway.secretmanager.FormStyleFormat
import scaleway.secretmanager.HeaderSerializable
import scaleway.secretmanager.ApiKeyLocation
import scaleway.secretmanager.PathStyleFormat
import scaleway.secretmanager.PathSerializable
import scaleway.secretmanager.CookieSerializable
import scaleway.secretmanager.Helpers.*
import sttp.client4.jsoniter.*
import sttp.client4.*
import sttp.model.Method

object SecretVersionsApi:
  def apply(
      baseUrl: String = "https://api.scaleway.com"
  ): SecretVersionsApi[scaleway.secretmanager.Authorization.NoAuthorization.type] =
    SecretVersionsApi(baseUrl, scaleway.secretmanager.Authorization.NoAuthorization)
  def withBasicAuth(
      baseUrl: String,
      username: String,
      password: String
  ): SecretVersionsApi[scaleway.secretmanager.Authorization.BasicAuth] =
    SecretVersionsApi(baseUrl, scaleway.secretmanager.Authorization.BasicAuth(username, password))

  def withApiKeyAuth(baseUrl: String, apiKey: String): SecretVersionsApi[scaleway.secretmanager.Authorization.ApiKey] =
    SecretVersionsApi(baseUrl, scaleway.secretmanager.Authorization.ApiKey(apiKey))

  def withBearerTokenAuth(
      baseUrl: String,
      token: String
  ): SecretVersionsApi[scaleway.secretmanager.Authorization.BearerToken] =
    SecretVersionsApi(baseUrl, scaleway.secretmanager.Authorization.BearerToken(token))

case class SecretVersionsApi[Auth <: scaleway.secretmanager.Authorization] private (
    baseUrl: String,
    authConfig: scaleway.secretmanager.Authorization
):
  def withBasicAuth(
      username: String,
      password: String
  ): SecretVersionsApi[scaleway.secretmanager.Authorization.BasicAuth] =
    copy(authConfig = scaleway.secretmanager.Authorization.BasicAuth(username, password))

  def withApiKeyAuth(apiKey: String): SecretVersionsApi[scaleway.secretmanager.Authorization.ApiKey] =
    copy(authConfig = scaleway.secretmanager.Authorization.ApiKey(apiKey))

  def withNoAuth: SecretVersionsApi[scaleway.secretmanager.Authorization.NoAuthorization.type] =
    copy(authConfig = scaleway.secretmanager.Authorization.NoAuthorization)

  def withBearerTokenAuth(token: String): SecretVersionsApi[scaleway.secretmanager.Authorization.BearerToken] =
    copy(authConfig = scaleway.secretmanager.Authorization.BearerToken(token))

  /** Access sensitive data in a secret's version specified by the `region`, `secret_id` and `revision` parameters.
    *
    * Expected answers: code 200 : AccessSecretVersionResponse ()
    *
    * Available security schemes: scaleway (apiKey)
    *
    * @param region
    *   The region you want to target
    * @param secretId
    *   ID of the secret. (UUID format)
    * @param revision
    *   Version number. The first version of the secret is numbered 1, and all subsequent revisions augment by 1. Value
    *   can be either: - a number (the revision number) - \"latest\" (the latest revision) - \"latest_enabled\" (the
    *   latest enabled revision).
    */
  def accessSecretVersion(region: String, secretId: String, revision: String)(using
      Auth <:< scaleway.secretmanager.Authorization.ApiKey
  ): sttp.client4.Request[Either[ResponseException[String], AccessSecretVersionResponse]] =
    val regionPathParam = PathSerializable.serialize("region", region, PathStyleFormat.SIMPLE, false)
    val secretIdPathParam = PathSerializable.serialize("secret_id", secretId, PathStyleFormat.SIMPLE, false)
    val revisionPathParam = PathSerializable.serialize("revision", revision, PathStyleFormat.SIMPLE, false)
    val requestURL =
      uri"$baseUrl/secret-manager/v1beta1/regions/${regionPathParam}/secrets/${secretIdPathParam}/versions/${revisionPathParam}/access"

    basicRequest
      .method(Method.GET, requestURL)
      .contentType("application/json")
      .auth(authConfig, scaleway.secretmanager.ApiKeyLocation.HEADER, "X-Auth-Token")
      .response(asJson[AccessSecretVersionResponse])

  /** Access sensitive data in a secret's version specified by the `region`, `secret_name`, `secret_path` and `revision`
    * parameters.
    *
    * Expected answers: code 200 : AccessSecretVersionResponse ()
    *
    * Available security schemes: scaleway (apiKey)
    *
    * @param region
    *   The region you want to target
    * @param revision
    *   Version number. The first version of the secret is numbered 1, and all subsequent revisions augment by 1. Value
    *   can be either: - an integer (the revision number) - \"latest\" (the latest revision) - \"latest_enabled\" (the
    *   latest enabled revision).
    * @param secretPath
    *   Secret's path.
    * @param secretName
    *   Secret's name.
    * @param projectId
    *   ID of the Project to target. (UUID format)
    */
  def accessSecretVersionByPath(
      region: String,
      revision: String,
      secretPath: String,
      secretName: String,
      projectId: String
  )(using
      Auth <:< scaleway.secretmanager.Authorization.ApiKey
  ): sttp.client4.Request[Either[ResponseException[String], AccessSecretVersionResponse]] =
    val regionPathParam = PathSerializable.serialize("region", region, PathStyleFormat.SIMPLE, false)
    val revisionPathParam = PathSerializable.serialize("revision", revision, PathStyleFormat.SIMPLE, false)
    val requestURL =
      uri"$baseUrl/secret-manager/v1beta1/regions/${regionPathParam}/secrets-by-path/versions/${revisionPathParam}/access"
        .addParams(FormSerializable.serialize("secret_path", secretPath, FormStyleFormat.FORM, true): _*)
        .addParams(FormSerializable.serialize("secret_name", secretName, FormStyleFormat.FORM, true): _*)
        .addParams(FormSerializable.serialize("project_id", projectId, FormStyleFormat.FORM, true): _*)

    basicRequest
      .method(Method.GET, requestURL)
      .contentType("application/json")
      .auth(authConfig, scaleway.secretmanager.ApiKeyLocation.HEADER, "X-Auth-Token")
      .response(asJson[AccessSecretVersionResponse])

  /** Create a version of a given secret specified by the `region` and `secret_id` parameters.
    *
    * Expected answers: code 200 : SecretVersion ()
    *
    * Available security schemes: scaleway (apiKey)
    *
    * @param region
    *   The region you want to target
    * @param secretId
    *   ID of the secret. (UUID format)
    * @param createSecretVersionRequest
    */
  def createSecretVersion(region: String, secretId: String, createSecretVersionRequest: CreateSecretVersionRequest)(
      using Auth <:< scaleway.secretmanager.Authorization.ApiKey
  ): sttp.client4.Request[Either[ResponseException[String], SecretVersion]] =
    val regionPathParam = PathSerializable.serialize("region", region, PathStyleFormat.SIMPLE, false)
    val secretIdPathParam = PathSerializable.serialize("secret_id", secretId, PathStyleFormat.SIMPLE, false)
    val requestURL =
      uri"$baseUrl/secret-manager/v1beta1/regions/${regionPathParam}/secrets/${secretIdPathParam}/versions"

    basicRequest
      .method(Method.POST, requestURL)
      .contentType("application/json")
      .auth(authConfig, scaleway.secretmanager.ApiKeyLocation.HEADER, "X-Auth-Token")
      .body(asJson(createSecretVersionRequest))
      .response(asJson[SecretVersion])

  /** Delete a secret's version and the sensitive data contained in it. Deleting a version is permanent and cannot be
    * undone.
    *
    * Expected answers: code 204 : ()
    *
    * Available security schemes: scaleway (apiKey)
    *
    * @param region
    *   The region you want to target
    * @param secretId
    *   ID of the secret. (UUID format)
    * @param revision
    *   Version number. The first version of the secret is numbered 1, and all subsequent revisions augment by 1. Value
    *   can be either: - a number (the revision number) - \"latest\" (the latest revision) - \"latest_enabled\" (the
    *   latest enabled revision).
    */
  def deleteSecretVersion(region: String, secretId: String, revision: String)(using
      Auth <:< scaleway.secretmanager.Authorization.ApiKey
  ): sttp.client4.Request[Either[ResponseException[String], Unit]] =
    val regionPathParam = PathSerializable.serialize("region", region, PathStyleFormat.SIMPLE, false)
    val secretIdPathParam = PathSerializable.serialize("secret_id", secretId, PathStyleFormat.SIMPLE, false)
    val revisionPathParam = PathSerializable.serialize("revision", revision, PathStyleFormat.SIMPLE, false)
    val requestURL =
      uri"$baseUrl/secret-manager/v1beta1/regions/${regionPathParam}/secrets/${secretIdPathParam}/versions/${revisionPathParam}"

    basicRequest
      .method(Method.DELETE, requestURL)
      .contentType("application/json")
      .auth(authConfig, scaleway.secretmanager.ApiKeyLocation.HEADER, "X-Auth-Token")
      .response(asString.mapWithMetadata(ResponseAs.deserializeRightWithError(_ => Right(()))))

  /** Make a specific version inaccessible. You must specify the `region`, `secret_id` and `revision` parameters.
    *
    * Expected answers: code 200 : SecretVersion ()
    *
    * Available security schemes: scaleway (apiKey)
    *
    * @param region
    *   The region you want to target
    * @param secretId
    *   ID of the secret. (UUID format)
    * @param revision
    *   Version number. The first version of the secret is numbered 1, and all subsequent revisions augment by 1. Value
    *   can be either: - a number (the revision number) - \"latest\" (the latest revision) - \"latest_enabled\" (the
    *   latest enabled revision).
    * @param body
    */
  def disableSecretVersion(region: String, secretId: String, revision: String, body: io.circe.Json)(using
      Auth <:< scaleway.secretmanager.Authorization.ApiKey
  ): sttp.client4.Request[Either[ResponseException[String], SecretVersion]] =
    val regionPathParam = PathSerializable.serialize("region", region, PathStyleFormat.SIMPLE, false)
    val secretIdPathParam = PathSerializable.serialize("secret_id", secretId, PathStyleFormat.SIMPLE, false)
    val revisionPathParam = PathSerializable.serialize("revision", revision, PathStyleFormat.SIMPLE, false)
    val requestURL =
      uri"$baseUrl/secret-manager/v1beta1/regions/${regionPathParam}/secrets/${secretIdPathParam}/versions/${revisionPathParam}/disable"

    basicRequest
      .method(Method.POST, requestURL)
      .contentType("application/json")
      .auth(authConfig, scaleway.secretmanager.ApiKeyLocation.HEADER, "X-Auth-Token")
      .body(asJson(body))
      .response(asJson[SecretVersion])

  /** Make a specific version accessible. You must specify the `region`, `secret_id` and `revision` parameters.
    *
    * Expected answers: code 200 : SecretVersion ()
    *
    * Available security schemes: scaleway (apiKey)
    *
    * @param region
    *   The region you want to target
    * @param secretId
    *   ID of the secret. (UUID format)
    * @param revision
    *   Version number. The first version of the secret is numbered 1, and all subsequent revisions augment by 1. Value
    *   can be either: - a number (the revision number) - \"latest\" (the latest revision) - \"latest_enabled\" (the
    *   latest enabled revision).
    * @param body
    */
  def enableSecretVersion(region: String, secretId: String, revision: String, body: io.circe.Json)(using
      Auth <:< scaleway.secretmanager.Authorization.ApiKey
  ): sttp.client4.Request[Either[ResponseException[String], SecretVersion]] =
    val regionPathParam = PathSerializable.serialize("region", region, PathStyleFormat.SIMPLE, false)
    val secretIdPathParam = PathSerializable.serialize("secret_id", secretId, PathStyleFormat.SIMPLE, false)
    val revisionPathParam = PathSerializable.serialize("revision", revision, PathStyleFormat.SIMPLE, false)
    val requestURL =
      uri"$baseUrl/secret-manager/v1beta1/regions/${regionPathParam}/secrets/${secretIdPathParam}/versions/${revisionPathParam}/enable"

    basicRequest
      .method(Method.POST, requestURL)
      .contentType("application/json")
      .auth(authConfig, scaleway.secretmanager.ApiKeyLocation.HEADER, "X-Auth-Token")
      .body(asJson(body))
      .response(asJson[SecretVersion])

  /** Retrieve the metadata of a secret's given version specified by the `region`, `secret_id` and `revision`
    * parameters.
    *
    * Expected answers: code 200 : SecretVersion ()
    *
    * Available security schemes: scaleway (apiKey)
    *
    * @param region
    *   The region you want to target
    * @param secretId
    *   ID of the secret. (UUID format)
    * @param revision
    *   Version number. The first version of the secret is numbered 1, and all subsequent revisions augment by 1. Value
    *   can be either: - a number (the revision number) - \"latest\" (the latest revision) - \"latest_enabled\" (the
    *   latest enabled revision).
    */
  def getSecretVersion(region: String, secretId: String, revision: String)(using
      Auth <:< scaleway.secretmanager.Authorization.ApiKey
  ): sttp.client4.Request[Either[ResponseException[String], SecretVersion]] =
    val regionPathParam = PathSerializable.serialize("region", region, PathStyleFormat.SIMPLE, false)
    val secretIdPathParam = PathSerializable.serialize("secret_id", secretId, PathStyleFormat.SIMPLE, false)
    val revisionPathParam = PathSerializable.serialize("revision", revision, PathStyleFormat.SIMPLE, false)
    val requestURL =
      uri"$baseUrl/secret-manager/v1beta1/regions/${regionPathParam}/secrets/${secretIdPathParam}/versions/${revisionPathParam}"

    basicRequest
      .method(Method.GET, requestURL)
      .contentType("application/json")
      .auth(authConfig, scaleway.secretmanager.ApiKeyLocation.HEADER, "X-Auth-Token")
      .response(asJson[SecretVersion])

  /** Retrieve the list of a given secret's versions specified by the `secret_id` and `region` parameters.
    *
    * Expected answers: code 200 : ListSecretVersionsResponse ()
    *
    * Available security schemes: scaleway (apiKey)
    *
    * @param region
    *   The region you want to target
    * @param secretId
    *   ID of the secret. (UUID format)
    * @param page
    * @param pageSize
    * @param status
    *   Filter results by status.
    */
  def listSecretVersions(
      region: String,
      secretId: String,
      page: Option[Int] = scala.None,
      pageSize: Option[Int] = scala.None,
      status: Seq[Status]
  )(using
      Auth <:< scaleway.secretmanager.Authorization.ApiKey
  ): sttp.client4.Request[Either[ResponseException[String], ListSecretVersionsResponse]] =
    val regionPathParam = PathSerializable.serialize("region", region, PathStyleFormat.SIMPLE, false)
    val secretIdPathParam = PathSerializable.serialize("secret_id", secretId, PathStyleFormat.SIMPLE, false)
    val requestURL =
      uri"$baseUrl/secret-manager/v1beta1/regions/${regionPathParam}/secrets/${secretIdPathParam}/versions"
        .addParams(FormSerializable.serialize("page", page, FormStyleFormat.FORM, true): _*)
        .addParams(FormSerializable.serialize("page_size", pageSize, FormStyleFormat.FORM, true): _*)
        .addParams(FormSerializable.serialize("status", status, FormStyleFormat.FORM, true): _*)

    basicRequest
      .method(Method.GET, requestURL)
      .contentType("application/json")
      .auth(authConfig, scaleway.secretmanager.ApiKeyLocation.HEADER, "X-Auth-Token")
      .response(asJson[ListSecretVersionsResponse])

  /** Edit the metadata of a secret's given version, specified by the `region`, `secret_id` and `revision` parameters.
    *
    * Expected answers: code 200 : SecretVersion ()
    *
    * Available security schemes: scaleway (apiKey)
    *
    * @param region
    *   The region you want to target
    * @param secretId
    *   ID of the secret. (UUID format)
    * @param revision
    *   Version number. The first version of the secret is numbered 1, and all subsequent revisions augment by 1. Value
    *   can be either: - a number (the revision number) - \"latest\" (the latest revision) - \"latest_enabled\" (the
    *   latest enabled revision).
    * @param updateSecretVersionRequest
    */
  def updateSecretVersion(
      region: String,
      secretId: String,
      revision: String,
      updateSecretVersionRequest: UpdateSecretVersionRequest
  )(using
      Auth <:< scaleway.secretmanager.Authorization.ApiKey
  ): sttp.client4.Request[Either[ResponseException[String], SecretVersion]] =
    val regionPathParam = PathSerializable.serialize("region", region, PathStyleFormat.SIMPLE, false)
    val secretIdPathParam = PathSerializable.serialize("secret_id", secretId, PathStyleFormat.SIMPLE, false)
    val revisionPathParam = PathSerializable.serialize("revision", revision, PathStyleFormat.SIMPLE, false)
    val requestURL =
      uri"$baseUrl/secret-manager/v1beta1/regions/${regionPathParam}/secrets/${secretIdPathParam}/versions/${revisionPathParam}"

    basicRequest
      .method(Method.PATCH, requestURL)
      .contentType("application/json")
      .auth(authConfig, scaleway.secretmanager.ApiKeyLocation.HEADER, "X-Auth-Token")
      .body(asJson(updateSecretVersionRequest))
      .response(asJson[SecretVersion])

end SecretVersionsApi
