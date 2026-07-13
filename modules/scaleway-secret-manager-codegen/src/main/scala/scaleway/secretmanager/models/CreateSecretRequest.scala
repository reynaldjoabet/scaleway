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
package scaleway.secretmanager.models

import com.github.plokhotnyuk.jsoniter_scala.macros.named

case class CreateSecretRequest(
    /* ID of the Project containing the secret. (UUID format) */
    @named("project_id") projectId: Option[String] = scala.None,
    /* Name of the secret. */
    @named("name") name: Option[String] = scala.None,
    /* List of the secret's tags. */
    @named("tags") tags: Option[Seq[String]] = scala.None,
    /* Description of the secret. */
    @named("description") description: Option[String] = scala.None,
    /* Type of the secret. (Optional.) See the `Secret.Type` enum for a description of values. If not specified, the type is `Opaque`. */
    @named("type") `type`: Option[CreateSecretRequestEnums.Type] = scala.None,
    /* Path of the secret. (Optional.) Location of the secret in the directory structure. If not specified, the path is `/`. */
    @named("path") path: Option[String] = scala.None,
    @named("ephemeral_policy") ephemeralPolicy: Option[CreateSecretRequestEphemeralPolicy] = scala.None,
    /* Returns `true` if secret protection is applied to a given secret. A protected secret cannot be deleted. */
    @named("protected") `protected`: Option[Boolean] = scala.None,
    /* ID of the Scaleway Key Manager key. (Optional.) The Scaleway Key Manager key ID will be used to encrypt and decrypt secret versions. If not specified, Secret Manager will use a Key Manager internal key. (UUID format) */
    @named("key_id") keyId: Option[String] = scala.None
)

object CreateSecretRequestEnums:
  enum Type:
    case `unknown_type`
    case `opaque`
    case `certificate`
    case `key_value`
    case `basic_credentials`
    case `database_credentials`
    case `ssh_key`

  object Type:
    import com.github.plokhotnyuk.jsoniter_scala.macros.*
    import com.github.plokhotnyuk.jsoniter_scala.core.*
    given typeCodec: JsonValueCodec[Type] = JsonCodecMaker.make {
      CodecMakerConfig
        .withAdtLeafClassNameMapper { x =>
          JsonCodecMaker.simpleClassName(x) match
            case "unknown_type"         => "unknown_type"
            case "opaque"               => "opaque"
            case "certificate"          => "certificate"
            case "key_value"            => "key_value"
            case "basic_credentials"    => "basic_credentials"
            case "database_credentials" => "database_credentials"
            case "ssh_key"              => "ssh_key"
        }
        .withDiscriminatorFieldName(scala.None)
    }
end CreateSecretRequestEnums
