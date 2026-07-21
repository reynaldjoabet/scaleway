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
package scaleway.secretmanager.models

import java.time.OffsetDateTime
import com.github.plokhotnyuk.jsoniter_scala.macros.named

  /**
   * Ephemeral properties of the version. (Optional.) Properties that defines the version's expiration date, whether it expires after being accessed once, and the action to perform (disable or delete) once the version expires.
   */
case class UpdateSecretVersionRequestEphemeralProperties(
  /* The version's expiration date. (Optional.) If not specified, the version does not have an expiration date. (RFC 3339 format) */
  @named("expires_at") expiresAt: Option[OffsetDateTime] = scala.None,
  /* Returns `true` if the version expires after a single user access. (Optional.) If not specified, the version can be accessed an unlimited amount of times. */
  @named("expires_once_accessed") expiresOnceAccessed: Option[Boolean] = scala.None,
  /* Action to perform when the version of a secret expires. See `EphemeralPolicy.Action` enum for a description of values. */
  @named("action") action: Option[UpdateSecretVersionRequestEphemeralPropertiesEnums.Action] = scala.None
)

object UpdateSecretVersionRequestEphemeralPropertiesEnums:
  enum Action:
    case `unknown_action`
    case `delete`
    case `disable`

  object Action:
    import com.github.plokhotnyuk.jsoniter_scala.macros.*
    import com.github.plokhotnyuk.jsoniter_scala.core.*
    given actionCodec: JsonValueCodec[Action] = JsonCodecMaker.make {
      CodecMakerConfig
        .withAdtLeafClassNameMapper { x =>
          JsonCodecMaker.simpleClassName(x) match
            case "unknown_action" => "unknown_action"
            case "delete" => "delete"
            case "disable" => "disable"
        }
        .withDiscriminatorFieldName(scala.None)
    }
end UpdateSecretVersionRequestEphemeralPropertiesEnums
