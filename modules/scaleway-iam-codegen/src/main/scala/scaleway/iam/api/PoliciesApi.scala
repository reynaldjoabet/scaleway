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
package scaleway.iam.api

import scaleway.iam.models.CreatePolicyRequest
import scaleway.iam.models.ListPoliciesResponse
import scaleway.iam.models.Policy
import scaleway.iam.models.UpdatePolicyRequest
import com.github.plokhotnyuk.jsoniter_scala.circe.JsoniterScalaCodec.*
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

object PoliciesApi:
  def apply(
      baseUrl: String = "https://api.scaleway.com"
  ): PoliciesApi[scaleway.iam.Authorization.NoAuthorization.type] =
    PoliciesApi(baseUrl, scaleway.iam.Authorization.NoAuthorization)
  def withBasicAuth(
      baseUrl: String,
      username: String,
      password: String
  ): PoliciesApi[scaleway.iam.Authorization.BasicAuth] =
    PoliciesApi(baseUrl, scaleway.iam.Authorization.BasicAuth(username, password))

  def withApiKeyAuth(baseUrl: String, apiKey: String): PoliciesApi[scaleway.iam.Authorization.ApiKey] =
    PoliciesApi(baseUrl, scaleway.iam.Authorization.ApiKey(apiKey))

  def withBearerTokenAuth(baseUrl: String, token: String): PoliciesApi[scaleway.iam.Authorization.BearerToken] =
    PoliciesApi(baseUrl, scaleway.iam.Authorization.BearerToken(token))

case class PoliciesApi[Auth <: scaleway.iam.Authorization] private (
    baseUrl: String,
    authConfig: scaleway.iam.Authorization
):
  def withBasicAuth(username: String, password: String): PoliciesApi[scaleway.iam.Authorization.BasicAuth] =
    copy(authConfig = scaleway.iam.Authorization.BasicAuth(username, password))

  def withApiKeyAuth(apiKey: String): PoliciesApi[scaleway.iam.Authorization.ApiKey] =
    copy(authConfig = scaleway.iam.Authorization.ApiKey(apiKey))

  def withNoAuth: PoliciesApi[scaleway.iam.Authorization.NoAuthorization.type] =
    copy(authConfig = scaleway.iam.Authorization.NoAuthorization)

  def withBearerTokenAuth(token: String): PoliciesApi[scaleway.iam.Authorization.BearerToken] =
    copy(authConfig = scaleway.iam.Authorization.BearerToken(token))

  /** Clone a policy. You must define specify the `policy_id` parameter in your request.
    *
    * Expected answers: code 200 : Policy ()
    *
    * Available security schemes: scaleway (apiKey)
    *
    * @param policyId
    * @param body
    */
  def clonePolicy(policyId: String, body: io.circe.Json)(using
      Auth <:< scaleway.iam.Authorization.ApiKey
  ): sttp.client4.Request[Either[ResponseException[String], Policy]] =
    val policyIdPathParam = PathSerializable.serialize("policy_id", policyId, PathStyleFormat.SIMPLE, false)
    val requestURL =
      uri"$baseUrl/iam/v1alpha1/policies/${policyIdPathParam}/clone"

    basicRequest
      .method(Method.POST, requestURL)
      .contentType("application/json")
      .auth(authConfig, scaleway.iam.ApiKeyLocation.HEADER, "X-Auth-Token")
      .body(asJson(body))
      .response(asJson[Policy])

  /** Create a new application. You must define the `name` parameter in the request. You can specify parameters such as
    * `user_id`, `groups_id`, `application_id`, `no_principal`, `rules` and its child attributes.
    *
    * Expected answers: code 200 : Policy ()
    *
    * Available security schemes: scaleway (apiKey)
    *
    * @param createPolicyRequest
    */
  def createPolicy(createPolicyRequest: CreatePolicyRequest)(using
      Auth <:< scaleway.iam.Authorization.ApiKey
  ): sttp.client4.Request[Either[ResponseException[String], Policy]] =
    val requestURL =
      uri"$baseUrl/iam/v1alpha1/policies"

    basicRequest
      .method(Method.POST, requestURL)
      .contentType("application/json")
      .auth(authConfig, scaleway.iam.ApiKeyLocation.HEADER, "X-Auth-Token")
      .body(asJson(createPolicyRequest))
      .response(asJson[Policy])

  /** Delete a policy. You must define specify the `policy_id` parameter in your request. Note that when deleting a
    * policy, all permissions it gives to its principal (user, group or application) will be revoked.
    *
    * Expected answers: code 204 : ()
    *
    * Available security schemes: scaleway (apiKey)
    *
    * @param policyId
    *   Id of policy to delete.
    */
  def deletePolicy(policyId: String)(using
      Auth <:< scaleway.iam.Authorization.ApiKey
  ): sttp.client4.Request[Either[ResponseException[String], Unit]] =
    val policyIdPathParam = PathSerializable.serialize("policy_id", policyId, PathStyleFormat.SIMPLE, false)
    val requestURL =
      uri"$baseUrl/iam/v1alpha1/policies/${policyIdPathParam}"

    basicRequest
      .method(Method.DELETE, requestURL)
      .contentType("application/json")
      .auth(authConfig, scaleway.iam.ApiKeyLocation.HEADER, "X-Auth-Token")
      .response(asString.mapWithMetadata(ResponseAs.deserializeRightWithError(_ => Right(()))))

  /** Retrieve information about a policy, specified by the `policy_id` parameter. The policy's full details, including
    * `id`, `name`, `organization_id`, `nb_rules` and `nb_scopes`, `nb_permission_sets` are returned in the response.
    *
    * Expected answers: code 200 : Policy ()
    *
    * Available security schemes: scaleway (apiKey)
    *
    * @param policyId
    *   Id of policy to search.
    */
  def getPolicy(policyId: String)(using
      Auth <:< scaleway.iam.Authorization.ApiKey
  ): sttp.client4.Request[Either[ResponseException[String], Policy]] =
    val policyIdPathParam = PathSerializable.serialize("policy_id", policyId, PathStyleFormat.SIMPLE, false)
    val requestURL =
      uri"$baseUrl/iam/v1alpha1/policies/${policyIdPathParam}"

    basicRequest
      .method(Method.GET, requestURL)
      .contentType("application/json")
      .auth(authConfig, scaleway.iam.ApiKeyLocation.HEADER, "X-Auth-Token")
      .response(asJson[Policy])

  /** List the policies of an Organization. By default, the policies listed are ordered by creation date in ascending
    * order. This can be modified via the `order_by` field. You must define the `organization_id` in the query path of
    * your request. You can also define additional parameters to filter your query, such as `user_ids`, `groups_ids`,
    * `application_ids`, and `policy_name`.
    *
    * Expected answers: code 200 : ListPoliciesResponse ()
    *
    * Available security schemes: scaleway (apiKey)
    *
    * @param orderBy
    *   Criteria for sorting results.
    * @param pageSize
    *   Number of results per page. Value must be between 1 and 100.
    * @param page
    *   Page number. Value must be greater than 1.
    * @param organizationId
    *   ID of the Organization to filter.
    * @param editable
    *   Defines whether or not filter out editable policies.
    * @param userIds
    *   Defines whether or not to filter by list of user IDs.
    * @param groupIds
    *   Defines whether or not to filter by list of group IDs.
    * @param applicationIds
    *   Filter by a list of application IDs.
    * @param noPrincipal
    *   Defines whether or not the policy is attributed to a principal.
    * @param policyName
    *   Name of the policy to fetch.
    * @param tag
    *   Filter by tags containing a given string.
    * @param policyIds
    *   Filter by a list of IDs.
    */
  def listPolicies(
      orderBy: Option[String] = scala.None,
      pageSize: Option[Int] = scala.None,
      page: Option[Int] = scala.None,
      organizationId: Option[String] = scala.None,
      editable: Option[Boolean] = scala.None,
      userIds: Seq[String],
      groupIds: Seq[String],
      applicationIds: Seq[String],
      noPrincipal: Option[Boolean] = scala.None,
      policyName: Option[String] = scala.None,
      tag: Option[String] = scala.None,
      policyIds: Seq[String]
  )(using
      Auth <:< scaleway.iam.Authorization.ApiKey
  ): sttp.client4.Request[Either[ResponseException[String], ListPoliciesResponse]] =
    val requestURL =
      uri"$baseUrl/iam/v1alpha1/policies"
        .addParams(FormSerializable.serialize("order_by", orderBy, FormStyleFormat.FORM, true): _*)
        .addParams(FormSerializable.serialize("page_size", pageSize, FormStyleFormat.FORM, true): _*)
        .addParams(FormSerializable.serialize("page", page, FormStyleFormat.FORM, true): _*)
        .addParams(FormSerializable.serialize("organization_id", organizationId, FormStyleFormat.FORM, true): _*)
        .addParams(FormSerializable.serialize("editable", editable, FormStyleFormat.FORM, true): _*)
        .addParams(FormSerializable.serialize("user_ids", userIds, FormStyleFormat.FORM, true): _*)
        .addParams(FormSerializable.serialize("group_ids", groupIds, FormStyleFormat.FORM, true): _*)
        .addParams(FormSerializable.serialize("application_ids", applicationIds, FormStyleFormat.FORM, true): _*)
        .addParams(FormSerializable.serialize("no_principal", noPrincipal, FormStyleFormat.FORM, true): _*)
        .addParams(FormSerializable.serialize("policy_name", policyName, FormStyleFormat.FORM, true): _*)
        .addParams(FormSerializable.serialize("tag", tag, FormStyleFormat.FORM, true): _*)
        .addParams(FormSerializable.serialize("policy_ids", policyIds, FormStyleFormat.FORM, true): _*)

    basicRequest
      .method(Method.GET, requestURL)
      .contentType("application/json")
      .auth(authConfig, scaleway.iam.ApiKeyLocation.HEADER, "X-Auth-Token")
      .response(asJson[ListPoliciesResponse])

  /** Update the parameters of a policy, including `name`, `description`, `user_id`, `group_id`, `application_id` and
    * `no_principal`.
    *
    * Expected answers: code 200 : Policy ()
    *
    * Available security schemes: scaleway (apiKey)
    *
    * @param policyId
    *   Id of policy to update.
    * @param updatePolicyRequest
    */
  def updatePolicy(policyId: String, updatePolicyRequest: UpdatePolicyRequest)(using
      Auth <:< scaleway.iam.Authorization.ApiKey
  ): sttp.client4.Request[Either[ResponseException[String], Policy]] =
    val policyIdPathParam = PathSerializable.serialize("policy_id", policyId, PathStyleFormat.SIMPLE, false)
    val requestURL =
      uri"$baseUrl/iam/v1alpha1/policies/${policyIdPathParam}"

    basicRequest
      .method(Method.PATCH, requestURL)
      .contentType("application/json")
      .auth(authConfig, scaleway.iam.ApiKeyLocation.HEADER, "X-Auth-Token")
      .body(asJson(updatePolicyRequest))
      .response(asJson[Policy])

end PoliciesApi
