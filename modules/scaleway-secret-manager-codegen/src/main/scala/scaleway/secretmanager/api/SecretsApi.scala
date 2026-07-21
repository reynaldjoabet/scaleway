/**
 * Secret Manager API
 * Scaleway’s Secret Manager allows you to conveniently store, access and share sensitive data such as passwords, API keys and certificates. With Secret Manager you can manage secrets which are logical containers made up of zero or more immutable versions, that hold sensitive data. Your data is encrypted both in transit and at rest and it is automatically replicated to multiple zones within your region of choice.    ## Concepts  Refer to our [dedicated concepts page](https://www.scaleway.com/en/docs/identity-and-access-management/secret-manager/concepts/) to find definitions of the different terms referring to Secret Manager.    ## Quickstart  1. **Configure your environment variables.**      <Message type=\"note\">       This is an optional step that seeks to simplify your usage of the API.     </Message>      ```bash     export SCW_ACCESS_KEY=\"<API access key>\"     export SCW_SECRET_KEY=\"<API secret key>\"     export SCW_PROJECT_ID=\"<Scaleway Project ID>     ``` 2. **Create an opaque secret** in the root (`/`) folder.      ```bash     curl \"https://api.scaleway.com/secret-manager/v1beta1/regions/$REGION/secrets\" \\       -H \"Content-Type: application/json\" \\       -H \"X-Auth-Token: $SCW_SECRET_KEY\" \\       -d '{         \"name\": \"my-secret\",         \"project_id\": \"$PROJECT_ID\"       }'     ```     <Message type=\"note\">      The `opaque` type is the default secret type. If you want to create another secret type (e.g., for certificates or credentials), specify the `type` field in the request. Refer to our [concepts page](https://www.scaleway.com/en/docs/identity-and-access-management/secret-manager/concepts/) for supported types.     </Message> 3. **Create a secret version**. Run the following command to create a version and add your secret value:       ```bash     curl \"https://api.scaleway.com/secret-manager/v1beta1/regions/$REGION/secrets/<SECRET_ID>/versions\" \\       -H \"X-Auth-Token: $SCW_SECRET_KEY\" \\       -d \"{\\\"your-data\\\":\\\"$(echo -n \"y0ur-p@sSw0Rd_\" | base64)\\\"}\"     ```     <Message type=\"note\">       When creating a secret with data, two separate API calls are required:        `CreateSecret`: This initializes an empty container for your secret.       `CreateSecretVersion`: This associates the data with the secret as a version.       The [Scaleway console](https://console.scaleway.com/) automates these two steps for you, but when using the API, you must perform both calls in sequence.     </Message>  4. Create a `basic_credentials` secret type in the root (`/`) folder:     ```bash     curl \"https://api.scaleway.com/secret-manager/v1beta1/regions/$REGION/secrets\" \\       -H \"Content-Type: application/json\" \\       -H \"X-Auth-Token: $SCW_SECRET_KEY\" \\       -d '{         \"name\": \"my-secret\",         \"type\": \"basic_credentials\",         \"project_id\": \"$PROJECT_ID\"       }'     ``` 5. Create a version for your `basic_credentials` secret to store your credentials in your secret version:      ```bash     curl \"https://api.scaleway.com/secret-manager/v1beta1/regions/$REGION/secrets/<SECRET_ID>/versions\" \\       -H \"Content-Type: application/json\" \\       -H \"X-Auth-Token: $SCW_SECRET_KEY\" \\       -d '{        \"data\": \"'\"$(echo -n \"{\\\"username\\\": \\\"my-username\\\", \\\"password\\\": \\\"my-password\\\"}\" | base64)\"'\"}'     ```  6. **Access data from your latest secret version**. Run the following command to access the data of your most recent secret version:      ```bash     curl \"https://api.scaleway.com/secret-manager/v1beta1/regions/$REGION/secrets/<SECRET_ID>/versions/latest/access\" \\       -H \"Content-Type: application/json\" \\       -H \"X-Auth-Token: $SCW_SECRET_KEY\"     ```      <Message type=\"note\">       - The command above returns a base64-decoded JSON with your username and password if you have created the `basic_credentials` secret or any data you may have stored in other secrets.       - Requests can either target a specific version or the latest.     </Message>    <Message type=\"requirement\">   - You have your [Organization and your Project ID](https://console.scaleway.com/project/settings)  - You have [created an API key](https://www.scaleway.com/en/docs/iam/how-to/create-api-keys/)  - You have [installed `curl`](https://curl.se/download.html)  - You have created an [API key](https://www.scaleway.com/en/docs/iam/how-to/create-api-keys/) and that the API key has sufficient [IAM permissions](https://www.scaleway.com/en/docs/iam/reference-content/permission-sets/) to perform the actions described on this page </Message>   ## Technical information  ### Regions  Scaleway's infrastructure spans different [regions and Availability Zones](https://www.scaleway.com/en/docs/account/reference-content/products-availability/).  Secret Manager is available in the Paris, Amsterdam and Warsaw regions, which are represented by the following path parameters:  - fr-par - nl-ams - pl-waw  ## Technical limitations  - Operations on secrets and versions are limited to CRUDL - A secret's payload size is limited to 64KiB  ## Going further  For more information about Secret Manager, you can check out the following pages:  * [Secret Manager Documentation](https://www.scaleway.com/en/docs/identity-and-access-management/secret-manager/) * [Scaleway Slack Community](https://scaleway-community.slack.com/) join the #secret-manager channel * [Contact our support team](https://console.scaleway.com/support/tickets).
 *
 * The version of the OpenAPI document: v1beta1
 * 
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */
package scaleway.secretmanager.api

import scaleway.secretmanager.models.AddSecretOwnerRequest
import scaleway.secretmanager.models.CreateSecretRequest
import scaleway.secretmanager.models.ListSecretsResponse
import scaleway.secretmanager.models.OrderBy.*
import scaleway.secretmanager.models.OrderBy
import scaleway.secretmanager.models.Secret
import scaleway.secretmanager.models.SecretVersion
import scaleway.secretmanager.models.UpdateSecretRequest
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

object SecretsApi:
  def apply(baseUrl: String = "https://api.scaleway.com"): SecretsApi[scaleway.secretmanager.Authorization.NoAuthorization.type] = SecretsApi(baseUrl, scaleway.secretmanager.Authorization.NoAuthorization)
  def withBasicAuth(baseUrl: String, username: String, password: String): SecretsApi[scaleway.secretmanager.Authorization.BasicAuth] =
    SecretsApi(baseUrl, scaleway.secretmanager.Authorization.BasicAuth(username, password))

  def withApiKeyAuth(baseUrl: String, apiKey: String): SecretsApi[scaleway.secretmanager.Authorization.ApiKey] =
    SecretsApi(baseUrl, scaleway.secretmanager.Authorization.ApiKey(apiKey))

  def withBearerTokenAuth(baseUrl: String, token: String): SecretsApi[scaleway.secretmanager.Authorization.BearerToken] =
    SecretsApi(baseUrl, scaleway.secretmanager.Authorization.BearerToken(token))

case class SecretsApi[Auth <: scaleway.secretmanager.Authorization] private (baseUrl: String, authConfig: scaleway.secretmanager.Authorization):
  def withBasicAuth(username: String, password: String): SecretsApi[scaleway.secretmanager.Authorization.BasicAuth] =
    copy(authConfig = scaleway.secretmanager.Authorization.BasicAuth(username, password))

  def withApiKeyAuth(apiKey: String): SecretsApi[scaleway.secretmanager.Authorization.ApiKey] =
    copy(authConfig = scaleway.secretmanager.Authorization.ApiKey(apiKey))

  def withNoAuth: SecretsApi[scaleway.secretmanager.Authorization.NoAuthorization.type] =
    copy(authConfig = scaleway.secretmanager.Authorization.NoAuthorization)

  def withBearerTokenAuth(token: String): SecretsApi[scaleway.secretmanager.Authorization.BearerToken] =
    copy(authConfig = scaleway.secretmanager.Authorization.BearerToken(token))

  /**
   * Expected answers:
   *   code 204 :  ()
   * 
   * Available security schemes:
   *   scaleway (apiKey)
   * 
   * @param region The region you want to target
   * @param secretId ID of the secret. (UUID format)
   * @param addSecretOwnerRequest 
   */
  def addSecretOwner(region: String, secretId: String, addSecretOwnerRequest: AddSecretOwnerRequest)(using Auth <:< scaleway.secretmanager.Authorization.ApiKey): sttp.client4.Request[Either[ResponseException[String], Unit]] =
    val regionPathParam = PathSerializable.serialize("region", region, PathStyleFormat.SIMPLE, false)
    val secretIdPathParam = PathSerializable.serialize("secret_id", secretId, PathStyleFormat.SIMPLE, false)
    val requestURL =
      uri"$baseUrl/secret-manager/v1beta1/regions/${regionPathParam}/secrets/${secretIdPathParam}/add-owner"

    basicRequest
      .method(Method.POST, requestURL)
      .contentType("application/json")
      .auth(authConfig, scaleway.secretmanager.ApiKeyLocation.HEADER, "X-Auth-Token")
      .body(asJson(addSecretOwnerRequest))
      .response(asString.mapWithMetadata(ResponseAs.deserializeRightWithError(_ => Right(()))))

  /**
   * Create a secret in a given region specified by the `region` parameter.
   * 
   * Expected answers:
   *   code 200 : Secret ()
   * 
   * Available security schemes:
   *   scaleway (apiKey)
   * 
   * @param region The region you want to target
   * @param createSecretRequest 
   */
  def createSecret(region: String, createSecretRequest: CreateSecretRequest)(using Auth <:< scaleway.secretmanager.Authorization.ApiKey): sttp.client4.Request[Either[ResponseException[String], Secret]] =
    val regionPathParam = PathSerializable.serialize("region", region, PathStyleFormat.SIMPLE, false)
    val requestURL =
      uri"$baseUrl/secret-manager/v1beta1/regions/${regionPathParam}/secrets"

    basicRequest
      .method(Method.POST, requestURL)
      .contentType("application/json")
      .auth(authConfig, scaleway.secretmanager.ApiKeyLocation.HEADER, "X-Auth-Token")
      .body(asJson(createSecretRequest))
      .response(asJson[Secret])

  /**
   * Delete a given secret specified by the `region` and `secret_id` parameters.
   * 
   * Expected answers:
   *   code 204 :  ()
   * 
   * Available security schemes:
   *   scaleway (apiKey)
   * 
   * @param region The region you want to target
   * @param secretId ID of the secret. (UUID format)
   */
  def deleteSecret(region: String, secretId: String)(using Auth <:< scaleway.secretmanager.Authorization.ApiKey): sttp.client4.Request[Either[ResponseException[String], Unit]] =
    val regionPathParam = PathSerializable.serialize("region", region, PathStyleFormat.SIMPLE, false)
    val secretIdPathParam = PathSerializable.serialize("secret_id", secretId, PathStyleFormat.SIMPLE, false)
    val requestURL =
      uri"$baseUrl/secret-manager/v1beta1/regions/${regionPathParam}/secrets/${secretIdPathParam}"

    basicRequest
      .method(Method.DELETE, requestURL)
      .contentType("application/json")
      .auth(authConfig, scaleway.secretmanager.ApiKeyLocation.HEADER, "X-Auth-Token")
      .response(asString.mapWithMetadata(ResponseAs.deserializeRightWithError(_ => Right(()))))

  /**
   * Retrieve the metadata of a secret specified by the `region` and `secret_id` parameters.
   * 
   * Expected answers:
   *   code 200 : Secret ()
   * 
   * Available security schemes:
   *   scaleway (apiKey)
   * 
   * @param region The region you want to target
   * @param secretId ID of the secret. (UUID format)
   */
  def getSecret(region: String, secretId: String)(using Auth <:< scaleway.secretmanager.Authorization.ApiKey): sttp.client4.Request[Either[ResponseException[String], Secret]] =
    val regionPathParam = PathSerializable.serialize("region", region, PathStyleFormat.SIMPLE, false)
    val secretIdPathParam = PathSerializable.serialize("secret_id", secretId, PathStyleFormat.SIMPLE, false)
    val requestURL =
      uri"$baseUrl/secret-manager/v1beta1/regions/${regionPathParam}/secrets/${secretIdPathParam}"

    basicRequest
      .method(Method.GET, requestURL)
      .contentType("application/json")
      .auth(authConfig, scaleway.secretmanager.ApiKeyLocation.HEADER, "X-Auth-Token")
      .response(asJson[Secret])

  /**
   * Retrieve the list of secrets created within an Organization and/or Project.  If the user has permissions for all current and future projects: Either organization_id or project_id is required. If the user has permissions for all current projects or only specific projects: The `project_id` is required. The `region` parameter in path is needed in both case.
   * 
   * Expected answers:
   *   code 200 : ListSecretsResponse ()
   * 
   * Available security schemes:
   *   scaleway (apiKey)
   * 
   * @param region The region you want to target
   * @param scheduledForDeletion Filter by whether the secret was scheduled for deletion / not scheduled for deletion. By default, it will display only not scheduled for deletion secrets.
   * @param organizationId Filter by Organization ID (optional). (UUID format)
   * @param projectId Filter by Project ID (optional). (UUID format)
   * @param orderBy 
   * @param page 
   * @param pageSize 
   * @param tags List of tags to filter on (optional).
   * @param name Filter by secret name (optional).
   * @param path Filter by exact path (optional).
   * @param ephemeral Filter by ephemeral / not ephemeral (optional).
   * @param `type` Filter by secret type (optional).
   */
  def listSecrets(region: String, scheduledForDeletion: Boolean, organizationId: Option[String] = scala.None, projectId: Option[String] = scala.None, orderBy: Option[OrderBy] = scala.None, page: Option[Int] = scala.None, pageSize: Option[Int] = scala.None, tags: Seq[String], name: Option[String] = scala.None, path: Option[String] = scala.None, ephemeral: Option[Boolean] = scala.None, `type`: Option[String] = scala.None)(using Auth <:< scaleway.secretmanager.Authorization.ApiKey): sttp.client4.Request[Either[ResponseException[String], ListSecretsResponse]] =
    val regionPathParam = PathSerializable.serialize("region", region, PathStyleFormat.SIMPLE, false)
    val requestURL =
      uri"$baseUrl/secret-manager/v1beta1/regions/${regionPathParam}/secrets"
        .addParams(FormSerializable.serialize("organization_id", organizationId, FormStyleFormat.FORM, true): _*)
        .addParams(FormSerializable.serialize("project_id", projectId, FormStyleFormat.FORM, true): _*)
        .addParams(FormSerializable.serialize("order_by", orderBy, FormStyleFormat.FORM, true): _*)
        .addParams(FormSerializable.serialize("page", page, FormStyleFormat.FORM, true): _*)
        .addParams(FormSerializable.serialize("page_size", pageSize, FormStyleFormat.FORM, true): _*)
        .addParams(FormSerializable.serialize("tags", tags, FormStyleFormat.FORM, true): _*)
        .addParams(FormSerializable.serialize("name", name, FormStyleFormat.FORM, true): _*)
        .addParams(FormSerializable.serialize("path", path, FormStyleFormat.FORM, true): _*)
        .addParams(FormSerializable.serialize("ephemeral", ephemeral, FormStyleFormat.FORM, true): _*)
        .addParams(FormSerializable.serialize("type", `type`, FormStyleFormat.FORM, true): _*)
        .addParams(FormSerializable.serialize("scheduled_for_deletion", scheduledForDeletion, FormStyleFormat.FORM, true): _*)

    basicRequest
      .method(Method.GET, requestURL)
      .contentType("application/json")
      .auth(authConfig, scaleway.secretmanager.ApiKeyLocation.HEADER, "X-Auth-Token")
      .response(asJson[ListSecretsResponse])

  /**
   * Enable secret protection for a given secret specified by the `secret_id` parameter. Enabling secret protection means that your secret can be read and modified, but it cannot be deleted.
   * 
   * Expected answers:
   *   code 200 : Secret ()
   * 
   * Available security schemes:
   *   scaleway (apiKey)
   * 
   * @param region The region you want to target
   * @param secretId ID of the secret to enable secret protection for. (UUID format)
   * @param body 
   */
  def protectSecret(region: String, secretId: String, body: io.circe.Json)(using Auth <:< scaleway.secretmanager.Authorization.ApiKey): sttp.client4.Request[Either[ResponseException[String], Secret]] =
    val regionPathParam = PathSerializable.serialize("region", region, PathStyleFormat.SIMPLE, false)
    val secretIdPathParam = PathSerializable.serialize("secret_id", secretId, PathStyleFormat.SIMPLE, false)
    val requestURL =
      uri"$baseUrl/secret-manager/v1beta1/regions/${regionPathParam}/secrets/${secretIdPathParam}/protect"

    basicRequest
      .method(Method.POST, requestURL)
      .contentType("application/json")
      .auth(authConfig, scaleway.secretmanager.ApiKeyLocation.HEADER, "X-Auth-Token")
      .body(asJson(body))
      .response(asJson[Secret])

  /**
   * Restore a secret and all its versions scheduled for deletion specified by the `region` and `secret_id` parameters.
   * 
   * Expected answers:
   *   code 200 : Secret ()
   * 
   * Available security schemes:
   *   scaleway (apiKey)
   * 
   * @param region The region you want to target
   * @param secretId (UUID format)
   * @param body 
   */
  def restoreSecret(region: String, secretId: String, body: io.circe.Json)(using Auth <:< scaleway.secretmanager.Authorization.ApiKey): sttp.client4.Request[Either[ResponseException[String], Secret]] =
    val regionPathParam = PathSerializable.serialize("region", region, PathStyleFormat.SIMPLE, false)
    val secretIdPathParam = PathSerializable.serialize("secret_id", secretId, PathStyleFormat.SIMPLE, false)
    val requestURL =
      uri"$baseUrl/secret-manager/v1beta1/regions/${regionPathParam}/secrets/${secretIdPathParam}/restore"

    basicRequest
      .method(Method.POST, requestURL)
      .contentType("application/json")
      .auth(authConfig, scaleway.secretmanager.ApiKeyLocation.HEADER, "X-Auth-Token")
      .body(asJson(body))
      .response(asJson[Secret])

  /**
   * Restore a secret's version specified by the `region`, `secret_id` and `revision` parameters.
   * 
   * Expected answers:
   *   code 200 : SecretVersion ()
   * 
   * Available security schemes:
   *   scaleway (apiKey)
   * 
   * @param region The region you want to target
   * @param secretId (UUID format)
   * @param revision 
   * @param body 
   */
  def restoreSecretVersion(region: String, secretId: String, revision: String, body: io.circe.Json)(using Auth <:< scaleway.secretmanager.Authorization.ApiKey): sttp.client4.Request[Either[ResponseException[String], SecretVersion]] =
    val regionPathParam = PathSerializable.serialize("region", region, PathStyleFormat.SIMPLE, false)
    val secretIdPathParam = PathSerializable.serialize("secret_id", secretId, PathStyleFormat.SIMPLE, false)
    val revisionPathParam = PathSerializable.serialize("revision", revision, PathStyleFormat.SIMPLE, false)
    val requestURL =
      uri"$baseUrl/secret-manager/v1beta1/regions/${regionPathParam}/secrets/${secretIdPathParam}/versions/${revisionPathParam}/restore"

    basicRequest
      .method(Method.POST, requestURL)
      .contentType("application/json")
      .auth(authConfig, scaleway.secretmanager.ApiKeyLocation.HEADER, "X-Auth-Token")
      .body(asJson(body))
      .response(asJson[SecretVersion])

  /**
   * Disable secret protection for a given secret specified by the `secret_id` parameter. Disabling secret protection means that your secret can be read, modified and deleted.
   * 
   * Expected answers:
   *   code 200 : Secret ()
   * 
   * Available security schemes:
   *   scaleway (apiKey)
   * 
   * @param region The region you want to target
   * @param secretId ID of the secret to disable secret protection for. (UUID format)
   * @param body 
   */
  def unprotectSecret(region: String, secretId: String, body: io.circe.Json)(using Auth <:< scaleway.secretmanager.Authorization.ApiKey): sttp.client4.Request[Either[ResponseException[String], Secret]] =
    val regionPathParam = PathSerializable.serialize("region", region, PathStyleFormat.SIMPLE, false)
    val secretIdPathParam = PathSerializable.serialize("secret_id", secretId, PathStyleFormat.SIMPLE, false)
    val requestURL =
      uri"$baseUrl/secret-manager/v1beta1/regions/${regionPathParam}/secrets/${secretIdPathParam}/unprotect"

    basicRequest
      .method(Method.POST, requestURL)
      .contentType("application/json")
      .auth(authConfig, scaleway.secretmanager.ApiKeyLocation.HEADER, "X-Auth-Token")
      .body(asJson(body))
      .response(asJson[Secret])

  /**
   * Edit a secret's metadata such as name, tag(s), description and ephemeral policy. The secret to update is specified by the `secret_id` and `region` parameters.
   * 
   * Expected answers:
   *   code 200 : Secret ()
   * 
   * Available security schemes:
   *   scaleway (apiKey)
   * 
   * @param region The region you want to target
   * @param secretId ID of the secret. (UUID format)
   * @param updateSecretRequest 
   */
  def updateSecret(region: String, secretId: String, updateSecretRequest: UpdateSecretRequest)(using Auth <:< scaleway.secretmanager.Authorization.ApiKey): sttp.client4.Request[Either[ResponseException[String], Secret]] =
    val regionPathParam = PathSerializable.serialize("region", region, PathStyleFormat.SIMPLE, false)
    val secretIdPathParam = PathSerializable.serialize("secret_id", secretId, PathStyleFormat.SIMPLE, false)
    val requestURL =
      uri"$baseUrl/secret-manager/v1beta1/regions/${regionPathParam}/secrets/${secretIdPathParam}"

    basicRequest
      .method(Method.PATCH, requestURL)
      .contentType("application/json")
      .auth(authConfig, scaleway.secretmanager.ApiKeyLocation.HEADER, "X-Auth-Token")
      .body(asJson(updateSecretRequest))
      .response(asJson[Secret])

end SecretsApi