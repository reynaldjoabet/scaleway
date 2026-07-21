/**
 * IAM API
 * Identity and Access Management (IAM) allows you to share access to the management of your Scaleway resources and Organization settings, in a controlled and secure manner. With IAM, you can invite other users to your Organization, as well as create IAM applications which represent non-human users with their own API keys. You define permissions for users and applications in your Organization via highly customizable policies. Policies let you specify exactly what rights users and applications (or groups of users and applications) should have within your Organization.     ## Concepts  Refer to our [dedicated IAM concepts page](https://www.scaleway.com/en/docs/iam/concepts/) to find definitions of the different terms referring to IAM.     ## Quickstart  1. Configure your environment variables.      ```bash     export ACCESS_KEY=\"<access-key>\"     export SECRET_KEY=\"<secret-key>\"     export REGION=\"<region>\"     ```  2. Create an application. Replace the parameter values in the request payload with the details of your new application.     <Message type=\"note\">     The UUIDs used in the following code examples are not real     </Message>      ```bash     curl -X POST \\       -H \"Content-Type: application/json\" \\       -H \"X-Auth-Token: $SCW_SECRET_KEY\" https://api.scaleway.com/iam/v1alpha1/applications \\       -d '{         \"name\": \"prod1\",         \"organization_id\": \"c6842bac-7938-4c04-9e03-f48147eee1f1\",         \"description\": \"this is my new application\"         }'     ```      | Parameter        | Description                                   |     | :--------------- | :--------------------------------------       |     | `name`           | **REQUIRED** The name of your new application |     | `organization_id`| The ID of your Scaleway Organization          |     | `description`    | The description of your application           | 3. Retrieve your application ID from the response.      ```json     {       \"id\": \"950dde46-5cba-427d-a4f5-ce5a8a79717c\",       \"name\": \"prod1\",       \"description\": \"this is my new application\",       \"created_at\": \"2023-03-08T12:34:56.123456Z\",       \"updated_at\": \"2023-03-08T12:34:56.123456Z\",       \"organization_id\": \"c6842bac-7938-4c04-9e03-f48147eee1f1\",       \"editable\": \"true\",       \"nb_api_keys\": \"0\"     }     ```  4. Create a policy. Replace the parameter values in the request payload with the details of your new application, including the application ID retrieved in the previous step.      ```bash     curl -X POST \\       -H \"Content-Type: application/json\" \\       -H \"X-Auth-Token: $SCW_SECRET_KEY\" https://api.scaleway.com/iam/v1alpha1/policies \\       -d '{       \"name\": \"policy-prod1\",       \"description\": \"This policy grants full access to IAM in my Organization to application prod1\",       \"organization_id\": \"c6842bac-7938-4c04-9e03-f48147eee1f1\",       \"rules\": [         {           \"permission_set_names\": [             \"IAMManager\"           ],           \"organization_id\": \"c6842bac-7938-4c04-9e03-f48147eee1f1\"         }       ],       \"application_id\": \"950dde46-5cba-427d-a4f5-ce5a8a79717c\"     }'     ```      | Parameter        | Description                                   |     | :--------------- | :--------------------------------------       |     | `name`           | **REQUIRED** The name of your new application |     | `description`| The description of your policy          |     | `organization_id`| The ID of your Scaleway Organization |     | `rules`| The [rules](https://www.scaleway.com/en/docs/iam/reference-content/policy/#rules) of your policy |     | `permission_set_names` | The permission sets you want to grant. You can either [list all permission sets](#path-permission-sets-list-permission-sets) or find a complete list in the [permission sets documentation page](https://www.scaleway.com/en/docs/iam/reference-content/permission-sets/) |     | `organization_id`| The ID of the Scaleway Organization where you want your permission sets to apply. You can add one as the [scope](https://www.scaleway.com/en/docs/iam/reference-content/policy/#scope) of your policy |     | `application_id`| The ID of your application |      <Message type=\"note\">     To learn more about IAM policies, refer to our dedicated [IAM policies reference page](https://www.scaleway.com/en/docs/iam/reference-content/policy/).     </Message> 5. Create an API key for your application.      ```bash     curl -X POST \\       -H \"Content-Type: application/json\" \\       -H \"X-Auth-Token: $SCW_SECRET_KEY\" https://api.scaleway.com/iam/v1alpha1/api-keys \\       -d '{         \"application_id\": \"950dde46-5cba-427d-a4f5-ce5a8a79717c\",         \"expires_at\": \"2023-12-22T12:34:56.123456Z\",         \"default_project_id\": \"2aeadddc-c589-4784-8ef5-fae989a4bac8\",         \"description\": \"This is an API key for prod1\"       }'     ```      | Parameter        | Description                                                        |     | :--------------- | :----------------------------------------------------------------- |     | `application_id`     | The ID of your application |     | `expires_at`         | **OPTIONAL** The expiration date of your API key|     | `default_project_id` | **OPTIONAL** The Project ID of your preferred Project, to use with Object Storage. If no Project ID is specified, the default project is used. Refer to the [Using API Keys with Object Storage documentation page](https://www.scaleway.com/en/docs/iam/api-cli/using-api-key-object-storage/) |     | `description`        | The description of your API key | 6. Retrieve your access and secret keys from the response.     <Message type=\"note\">     The secret key is only showed once. Make sure that you copy and store both keys somewhere safe.     </Message> You can now have an IAM configuration fully set up and can begin working on your Scaleway projects.   <Message type=\"requirement\">   To perform the following steps, you must first ensure that:<br /><br />     - you have an account and are logged into the [Scaleway console](https://console.scaleway.com/organization)     - you have created an [API key](https://www.scaleway.com/en/docs/iam/how-to/create-api-keys/) and that the API key has sufficient [IAM permissions](https://www.scaleway.com/en/docs/iam/reference-content/permission-sets/) to perform the actions described on this page.     - you have [installed `curl`](https://curl.se/download.html) </Message>   ## Technical Limitations  * Currently, IAM users cannot be created within Scaleway Organizations, they can only be invited to join them. Refer to the [Users, groups and applications reference page](https://www.scaleway.com/en/docs/iam/reference-content/users-groups-and-applications/#users) to learn more about users.  * Access management at resource level is not yet available. You can currently scope the permission sets to a Project or to an Organization. Refer to the [Permission sets reference page](https://www.scaleway.com/en/docs/iam/reference-content/permission-sets/) to learn more about permission sets.  * Explicit deny permissions are not yet available. You can currently only explicitly allow access to different products or Organization management features.  ## Going Further  For more information about IAM, you can check out the following pages:  * [Identity and Access Management Documentation](https://www.scaleway.com/en/docs/iam/reference-content/overview/) * [Identity and Access Management FAQ](https://www.scaleway.com/en/docs/iam/faq/) * [Scaleway Slack Community](https://scaleway-community.slack.com/) join the #iam channel * [Contact our support team](https://console.scaleway.com/support/tickets) * [Scaleway CLI for IAM](https://github.com/scaleway/scaleway-cli/blob/master/docs/commands/iam.md) * [Scaleway Provider Terraform Documentation for IAM](https://registry.terraform.io/providers/scaleway/scaleway/latest/docs/resources/iam_api_key).
 *
 * The version of the OpenAPI document: v1alpha1
 * 
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */
package scaleway.iam.api

import scaleway.iam.models.CreateSSHKeyRequest
import scaleway.iam.models.ListSSHKeysResponse
import scaleway.iam.models.SSHKey
import scaleway.iam.models.UpdateSSHKeyRequest
import scaleway.iam.JsonSupport.{*, given}
import scaleway.iam.FormSerializable
import scaleway.iam.FormStyleFormat
import scaleway.iam.HeaderSerializable
import scaleway.iam.ApiKeyLocation
import scaleway.iam.PathStyleFormat
import scaleway.iam.PathSerializable
import scaleway.iam.CookieSerializable
import scaleway.iam.Helpers.*
import sttp.client4.jsoniter.*
import sttp.client4.*
import sttp.model.Method

object SSHKeysApi:
  def apply(baseUrl: String = "https://api.scaleway.com"): SSHKeysApi[scaleway.iam.Authorization.NoAuthorization.type] = SSHKeysApi(baseUrl, scaleway.iam.Authorization.NoAuthorization)
  def withBasicAuth(baseUrl: String, username: String, password: String): SSHKeysApi[scaleway.iam.Authorization.BasicAuth] =
    SSHKeysApi(baseUrl, scaleway.iam.Authorization.BasicAuth(username, password))

  def withApiKeyAuth(baseUrl: String, apiKey: String): SSHKeysApi[scaleway.iam.Authorization.ApiKey] =
    SSHKeysApi(baseUrl, scaleway.iam.Authorization.ApiKey(apiKey))

  def withBearerTokenAuth(baseUrl: String, token: String): SSHKeysApi[scaleway.iam.Authorization.BearerToken] =
    SSHKeysApi(baseUrl, scaleway.iam.Authorization.BearerToken(token))

case class SSHKeysApi[Auth <: scaleway.iam.Authorization] private (baseUrl: String, authConfig: scaleway.iam.Authorization):
  def withBasicAuth(username: String, password: String): SSHKeysApi[scaleway.iam.Authorization.BasicAuth] =
    copy(authConfig = scaleway.iam.Authorization.BasicAuth(username, password))

  def withApiKeyAuth(apiKey: String): SSHKeysApi[scaleway.iam.Authorization.ApiKey] =
    copy(authConfig = scaleway.iam.Authorization.ApiKey(apiKey))

  def withNoAuth: SSHKeysApi[scaleway.iam.Authorization.NoAuthorization.type] =
    copy(authConfig = scaleway.iam.Authorization.NoAuthorization)

  def withBearerTokenAuth(token: String): SSHKeysApi[scaleway.iam.Authorization.BearerToken] =
    copy(authConfig = scaleway.iam.Authorization.BearerToken(token))

  /**
   * Add a new SSH key to a Scaleway Project. You must specify the `name`, `public_key` and `project_id`.
   * 
   * Expected answers:
   *   code 200 : SSHKey ()
   * 
   * Available security schemes:
   *   scaleway (apiKey)
   * 
   * @param createSSHKeyRequest 
   */
  def createSSHKey(createSSHKeyRequest: CreateSSHKeyRequest)(using Auth <:< scaleway.iam.Authorization.ApiKey): sttp.client4.Request[Either[ResponseException[String], SSHKey]] =
    val requestURL =
      uri"$baseUrl/iam/v1alpha1/ssh-keys"

    basicRequest
      .method(Method.POST, requestURL)
      .contentType("application/json")
      .auth(authConfig, scaleway.iam.ApiKeyLocation.HEADER, "X-Auth-Token")
      .body(asJson(createSSHKeyRequest))
      .response(asJson[SSHKey])

  /**
   * Delete a given SSH key, specified by the `ssh_key_id`. Deleting an SSH is permanent, and cannot be undone. Note that you might need to update any configurations that used the SSH key.
   * 
   * Expected answers:
   *   code 204 :  ()
   * 
   * Available security schemes:
   *   scaleway (apiKey)
   * 
   * @param sshKeyId 
   */
  def deleteSSHKey(sshKeyId: String)(using Auth <:< scaleway.iam.Authorization.ApiKey): sttp.client4.Request[Either[ResponseException[String], Unit]] =
    val sshKeyIdPathParam = PathSerializable.serialize("ssh_key_id", sshKeyId, PathStyleFormat.SIMPLE, false)
    val requestURL =
      uri"$baseUrl/iam/v1alpha1/ssh-keys/${sshKeyIdPathParam}"

    basicRequest
      .method(Method.DELETE, requestURL)
      .contentType("application/json")
      .auth(authConfig, scaleway.iam.ApiKeyLocation.HEADER, "X-Auth-Token")
      .response(asString.mapWithMetadata(ResponseAs.deserializeRightWithError(_ => Right(()))))

  /**
   * Retrieve information about a given SSH key, specified by the `ssh_key_id` parameter. The SSH key's full details, including `id`, `name`, `public_key`, and `project_id` are returned in the response.
   * 
   * Expected answers:
   *   code 200 : SSHKey ()
   * 
   * Available security schemes:
   *   scaleway (apiKey)
   * 
   * @param sshKeyId ID of the SSH key.
   */
  def getSSHKey(sshKeyId: String)(using Auth <:< scaleway.iam.Authorization.ApiKey): sttp.client4.Request[Either[ResponseException[String], SSHKey]] =
    val sshKeyIdPathParam = PathSerializable.serialize("ssh_key_id", sshKeyId, PathStyleFormat.SIMPLE, false)
    val requestURL =
      uri"$baseUrl/iam/v1alpha1/ssh-keys/${sshKeyIdPathParam}"

    basicRequest
      .method(Method.GET, requestURL)
      .contentType("application/json")
      .auth(authConfig, scaleway.iam.ApiKeyLocation.HEADER, "X-Auth-Token")
      .response(asJson[SSHKey])

  /**
   * List SSH keys. By default, the SSH keys listed are ordered by creation date in ascending order. This can be modified via the `order_by` field. You can define additional parameters for your query such as `organization_id`, `name`, `project_id` and `disabled`.
   * 
   * Expected answers:
   *   code 200 : ListSSHKeysResponse ()
   * 
   * Available security schemes:
   *   scaleway (apiKey)
   * 
   * @param orderBy Sort order of the SSH keys.
   * @param page Requested page number. Value must be greater or equal to 1.
   * @param pageSize Number of items per page. Value must be between 1 and 100.
   * @param organizationId Filter by Organization ID.
   * @param name Name of group to find.
   * @param projectId Filter by Project ID.
   * @param disabled Defines whether to include disabled SSH keys or not.
   */
  def listSSHKeys(orderBy: Option[String] = scala.None, page: Option[Int] = scala.None, pageSize: Option[Int] = scala.None, organizationId: Option[String] = scala.None, name: Option[String] = scala.None, projectId: Option[String] = scala.None, disabled: Option[Boolean] = scala.None)(using Auth <:< scaleway.iam.Authorization.ApiKey): sttp.client4.Request[Either[ResponseException[String], ListSSHKeysResponse]] =
    val requestURL =
      uri"$baseUrl/iam/v1alpha1/ssh-keys"
        .addParams(FormSerializable.serialize("order_by", orderBy, FormStyleFormat.FORM, true): _*)
        .addParams(FormSerializable.serialize("page", page, FormStyleFormat.FORM, true): _*)
        .addParams(FormSerializable.serialize("page_size", pageSize, FormStyleFormat.FORM, true): _*)
        .addParams(FormSerializable.serialize("organization_id", organizationId, FormStyleFormat.FORM, true): _*)
        .addParams(FormSerializable.serialize("name", name, FormStyleFormat.FORM, true): _*)
        .addParams(FormSerializable.serialize("project_id", projectId, FormStyleFormat.FORM, true): _*)
        .addParams(FormSerializable.serialize("disabled", disabled, FormStyleFormat.FORM, true): _*)

    basicRequest
      .method(Method.GET, requestURL)
      .contentType("application/json")
      .auth(authConfig, scaleway.iam.ApiKeyLocation.HEADER, "X-Auth-Token")
      .response(asJson[ListSSHKeysResponse])

  /**
   * Update the parameters of an SSH key, including `name` and `disable`.
   * 
   * Expected answers:
   *   code 200 : SSHKey ()
   * 
   * Available security schemes:
   *   scaleway (apiKey)
   * 
   * @param sshKeyId 
   * @param updateSSHKeyRequest 
   */
  def updateSSHKey(sshKeyId: String, updateSSHKeyRequest: UpdateSSHKeyRequest)(using Auth <:< scaleway.iam.Authorization.ApiKey): sttp.client4.Request[Either[ResponseException[String], SSHKey]] =
    val sshKeyIdPathParam = PathSerializable.serialize("ssh_key_id", sshKeyId, PathStyleFormat.SIMPLE, false)
    val requestURL =
      uri"$baseUrl/iam/v1alpha1/ssh-keys/${sshKeyIdPathParam}"

    basicRequest
      .method(Method.PATCH, requestURL)
      .contentType("application/json")
      .auth(authConfig, scaleway.iam.ApiKeyLocation.HEADER, "X-Auth-Token")
      .body(asJson(updateSSHKeyRequest))
      .response(asJson[SSHKey])

end SSHKeysApi