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

import scaleway.iam.models.*
import java.time.*
import com.github.plokhotnyuk.jsoniter_scala.macros.*
import com.github.plokhotnyuk.jsoniter_scala.core.*
import com.github.plokhotnyuk.jsoniter_scala.circe.JsoniterScalaCodec.*

object JsonSupport extends AdditionalTypeSerializers:
  inline given CodecMakerConfig = CodecMakerConfig.withAllowRecursiveTypes(true)

  inline def deriveJsonCodec[A](using inline config: CodecMakerConfig): JsonValueCodec[A] =
    JsonCodecMaker.make(config)

  given listSamlCertificatesResponseCodec: JsonValueCodec[ListSamlCertificatesResponse] = deriveJsonCodec
  given policyCodec: JsonValueCodec[Policy] = deriveJsonCodec
  given addGroupMembersRequestCodec: JsonValueCodec[AddGroupMembersRequest] = deriveJsonCodec
  given validateUserMFAOTPResponseCodec: JsonValueCodec[ValidateUserMFAOTPResponse] = deriveJsonCodec
  given organizationSecuritySettingsCodec: JsonValueCodec[OrganizationSecuritySettings] = deriveJsonCodec
  given organizationCodec: JsonValueCodec[Organization] = deriveJsonCodec
  given initiateUserConnectionResponseCodec: JsonValueCodec[InitiateUserConnectionResponse] = deriveJsonCodec
  given samlCertificateCodec: JsonValueCodec[SamlCertificate] = deriveJsonCodec
  given listSSHKeysResponseCodec: JsonValueCodec[ListSSHKeysResponse] = deriveJsonCodec
  given listGroupsResponseCodec: JsonValueCodec[ListGroupsResponse] = deriveJsonCodec
  given createApplicationRequestCodec: JsonValueCodec[CreateApplicationRequest] = deriveJsonCodec
  given userCodec: JsonValueCodec[User] = deriveJsonCodec
  given checkPermissionsRequestCodec: JsonValueCodec[CheckPermissionsRequest] = deriveJsonCodec
  given listPoliciesResponseCodec: JsonValueCodec[ListPoliciesResponse] = deriveJsonCodec
  given updateUserRequestCodec: JsonValueCodec[UpdateUserRequest] = deriveJsonCodec
  given createGroupRequestCodec: JsonValueCodec[CreateGroupRequest] = deriveJsonCodec
  given listGracePeriodsResponseCodec: JsonValueCodec[ListGracePeriodsResponse] = deriveJsonCodec
  given groupCodec: JsonValueCodec[Group] = deriveJsonCodec
  given joinUserConnectionRequestCodec: JsonValueCodec[JoinUserConnectionRequest] = deriveJsonCodec
  given getUserConnectionsResponseCodec: JsonValueCodec[GetUserConnectionsResponse] = deriveJsonCodec
  given removeUserConnectionRequestCodec: JsonValueCodec[RemoveUserConnectionRequest] = deriveJsonCodec
  given createAPIKeyRequestCodec: JsonValueCodec[CreateAPIKeyRequest] = deriveJsonCodec
  given jWTCodec: JsonValueCodec[JWT] = deriveJsonCodec
  given validateUserMFAOTPRequestCodec: JsonValueCodec[ValidateUserMFAOTPRequest] = deriveJsonCodec
  given updateSamlRequestCodec: JsonValueCodec[UpdateSamlRequest] = deriveJsonCodec
  given setRulesResponseCodec: JsonValueCodec[SetRulesResponse] = deriveJsonCodec
  given updateApplicationRequestCodec: JsonValueCodec[UpdateApplicationRequest] = deriveJsonCodec
  given addGroupMemberRequestCodec: JsonValueCodec[AddGroupMemberRequest] = deriveJsonCodec
  given createSSHKeyRequestCodec: JsonValueCodec[CreateSSHKeyRequest] = deriveJsonCodec
  given logCodec: JsonValueCodec[Log] = deriveJsonCodec
  given listApplicationsResponseCodec: JsonValueCodec[ListApplicationsResponse] = deriveJsonCodec
  given updatePolicyRequestCodec: JsonValueCodec[UpdatePolicyRequest] = deriveJsonCodec
  given listLogsResponseCodec: JsonValueCodec[ListLogsResponse] = deriveJsonCodec
  given setGroupMembersRequestCodec: JsonValueCodec[SetGroupMembersRequest] = deriveJsonCodec
  given listPermissionSetsResponseCodec: JsonValueCodec[ListPermissionSetsResponse] = deriveJsonCodec
  given updateUserUsernameRequestCodec: JsonValueCodec[UpdateUserUsernameRequest] = deriveJsonCodec
  given checkPermissionsResponseCodec: JsonValueCodec[CheckPermissionsResponse] = deriveJsonCodec
  given applicationCodec: JsonValueCodec[Application] = deriveJsonCodec
  given addSamlCertificateRequestCodec: JsonValueCodec[AddSamlCertificateRequest] = deriveJsonCodec
  given updateOrganizationLoginMethodsRequestCodec: JsonValueCodec[UpdateOrganizationLoginMethodsRequest] =
    deriveJsonCodec
  given listUsersResponseCodec: JsonValueCodec[ListUsersResponse] = deriveJsonCodec
  given quotumCodec: JsonValueCodec[Quotum] = deriveJsonCodec
  given setOrganizationAliasRequestCodec: JsonValueCodec[SetOrganizationAliasRequest] = deriveJsonCodec
  given sSHKeyCodec: JsonValueCodec[SSHKey] = deriveJsonCodec
  given listQuotaResponseCodec: JsonValueCodec[ListQuotaResponse] = deriveJsonCodec
  given updateUserPasswordRequestCodec: JsonValueCodec[UpdateUserPasswordRequest] = deriveJsonCodec
  given createPolicyRequestCodec: JsonValueCodec[CreatePolicyRequest] = deriveJsonCodec
  given setRulesRequestCodec: JsonValueCodec[SetRulesRequest] = deriveJsonCodec
  given samlCodec: JsonValueCodec[Saml] = deriveJsonCodec
  given createUserRequestCodec: JsonValueCodec[CreateUserRequest] = deriveJsonCodec
  given updateAPIKeyRequestCodec: JsonValueCodec[UpdateAPIKeyRequest] = deriveJsonCodec
  given updateOrganizationSecuritySettingsRequestCodec: JsonValueCodec[UpdateOrganizationSecuritySettingsRequest] =
    deriveJsonCodec
  given updateSSHKeyRequestCodec: JsonValueCodec[UpdateSSHKeyRequest] = deriveJsonCodec
  given listJWTsResponseCodec: JsonValueCodec[ListJWTsResponse] = deriveJsonCodec
  given mFAOTPCodec: JsonValueCodec[MFAOTP] = deriveJsonCodec
  given listRulesResponseCodec: JsonValueCodec[ListRulesResponse] = deriveJsonCodec
  given listAPIKeysResponseCodec: JsonValueCodec[ListAPIKeysResponse] = deriveJsonCodec
  given aPIKeyCodec: JsonValueCodec[APIKey] = deriveJsonCodec
