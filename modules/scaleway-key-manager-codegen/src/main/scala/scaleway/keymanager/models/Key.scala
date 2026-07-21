/** Key Manager API Scaleway's Key Manager allows you to create, manage and use cryptographic keys in a centralized and
  * secure service. All your cryptographic operations can be delegated to the Key Manager, which in turn ensures the
  * security and availability of your keys. Key Manager supports the following cryptographic operations: data
  * encryption, data decryption, and data encryption key generation. ## Concepts Refer to our [dedicated concepts
  * page](https://www.scaleway.com/en/docs/key-manager/concepts/) to find definitions of the different terms referring
  * to Key Manager. ## Quickstart 1. **Configure your environment variables.** <Message type=\"note\"> This is an
  * optional step that seeks to simplify your usage of the API. </Message> ```bash     export SCW_ACCESS_KEY=\"<API access key>\"     export SCW_SECRET_KEY=\"<API secret key>\"     export SCW_PROJECT_ID=\"<Scaleway Project ID>\"     ``` 2.
  * **Create a key**. Run the following command to create a key that you can use to encrypt and decrypt your data: ```bash     curl \"https://api.scaleway.com/key-manager/v1alpha1/regions/$REGION/keys\" \\       -H \"Content-Type: application/json\" \\       -H \"X-Auth-Token: $SCW_SECRET_KEY\" \\       -d '{         \"project_id\": \"$PROJECT_ID\",         \"name\": \"my-key\",         \"usage\": {             \"symmetric_encryption\": \"aes_256_gcm\"         }        }'     ``` 3.
  * **Rotate your key**. Run the following command to generate a new version of your key. This operation renders your
  * previous key version obsolete. ```bash     curl \"https://api.scaleway.com/key-manager/v1alpha1/regions/$REGION/keys/<KEY_ID>/rotate\" \\       -H \"X-Auth-Token: $SCW_SECRET_KEY\" \\       -d '{         \"key_id\": \"$KEY_ID\"       }'     ``` 4.
  * **Encrypt data**. Run the following command to encrypt data with the key you have created in step 2: ```bash     curl \"https://api.scaleway.com/key-manager/v1alpha1/regions/$REGION/keys/<KEY_ID>/encrypt\" \\       -H \"X-Auth-Token: $SCW_SECRET_KEY\" \\       -d '{         \"key_id\": \"$KEY_ID\",         \"plaintext\": \"'\"$(echo -n \"plaintext-data\" | base64)\"'\"       }'     ``` 5.
  * **Generate a data encryption key**. Run the following command to generate a data encryption key that you can use for
  * cryptographic operations outside of Key Manager: ```bash     curl \"https://api.scaleway.com/key-manager/v1alpha1/regions/$REGION/keys/<KEY_ID>/generate-data-key\" \\       -H \"X-Auth-Token: $SCW_SECRET_KEY\" \\       -d '{         \"key_id\": \"$KEY_ID\",         \"algorithm\": \"aes_256_gcm\"       }'     ```
  * <Message type=\"requirement\"> To perform the following steps, you must first ensure that: - You have your
  * [Organization and your Project ID](https://console.scaleway.com/project/settings) - You have a [Scaleway
  * account](https://console.scaleway.com/) - You have created an [API
  * key](https://www.scaleway.com/en/docs/iam/how-to/create-api-keys/) and that the API key has sufficient [IAM
  * permissions](https://www.scaleway.com/en/docs/iam/reference-content/permission-sets/) to perform the actions
  * described on this page - You have [installed `curl`](https://curl.se/download.html) </Message> ## Technical
  * information ### Regions Scaleway's infrastructure spans different [regions and Availability
  * Zones](https://www.scaleway.com/en/docs/console/account/reference-content/products-availability/). Key Manager is
  * available in the Paris, Amsterdam and Warsaw regions, which are represented by the following path parameters: -
  * fr-par - nl-ams - pl-waw ## Technical limitations - While Scaleway Key Manager is responsible for generating,
  * encrypting, and decrypting [data encryption
  * keys](https://www.scaleway.com/en/docs/key-manager/concepts/#data-encryption-key-dek/), it does not store, manage,
  * or monitor them, nor does it engage in cryptographic operations with these keys. **You must use and manage data
  * encryption keys outside of Key Manager**. Read our
  * [documentation](https://www.scaleway.com/en/docs/key-manager/reference-content/security-recommendations/) to find
  * out about security measures to be aware of while using Key Manager. ### Symmetric encryption - The maximum payload
  * size that can be encrypted is 64KB of plaintext. - The maximum payload size that can be decrypted is around 131KB of
  * data. - The only symmetric algorithm currently supported by Key Manager is AES-256-GCM. ### Asymmetric encryption -
  * Key Manager supports the following asymmetric encryption algorithms: * RSA-OAEP-2048-SHA256 * RSA-OAEP-3072-SHA256 *
  * RSA-OAEP-4096-SHA256 ### Asymmetric signing - Key Manager supports the following asymmetric signing algorithms: *
  * EC-P256-SHA256 * EC-P384-SHA256 * RSA-PSS-2048-SHA256 * RSA-PSS-3072-SHA256 * RSA-PSS-4096-SHA256 *
  * RSA-PKCS1-2048-SHA256 * RSA-PKCS1-3072-SHA256 * RSA-PKCS1-4096-SHA256 ## Going further For more information about
  * Key Manager, you can check out the following pages: * [Key Manager
  * Documentation](https://www.scaleway.com/en/docs/key-manager/) * [Contact our support
  * team](https://console.scaleway.com/support/tickets).
  *
  * The version of the OpenAPI document: v1alpha1
  *
  * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
  * https://openapi-generator.tech Do not edit the class manually.
  */
package scaleway.keymanager.models

import java.time.OffsetDateTime
import com.github.plokhotnyuk.jsoniter_scala.macros.named

case class Key(
    /* ID of the key. (UUID format) */
    @named("id") id: Option[String] = scala.None,
    /* ID of the Project containing the key. (UUID format) */
    @named("project_id") projectId: Option[String] = scala.None,
    /* Name of the key. */
    @named("name") name: Option[String] = scala.None,
    @named("usage") usage: Option[CreateKeyRequestUsage] = scala.None,
    /* Key state. See the `Key.State` enum for a description of possible values. */
    @named("state") state: Option[KeyEnums.State] = scala.None,
    /* Number of key rotations. The rotation count tracks the number of times the key has been rotated. */
    @named("rotation_count") rotationCount: Option[Int] = scala.None,
    /* Key creation date. (RFC 3339 format) */
    @named("created_at") createdAt: Option[OffsetDateTime] = scala.None,
    /* Key last modification date. (RFC 3339 format) */
    @named("updated_at") updatedAt: Option[OffsetDateTime] = scala.None,
    /* Returns `true` if key protection is applied to the key. */
    @named("protected") `protected`: Option[Boolean] = scala.None,
    /* Returns `true` if the key is locked. */
    @named("locked") locked: Option[Boolean] = scala.None,
    /* Description of the key. */
    @named("description") description: Option[String] = scala.None,
    /* List of the key's tags. */
    @named("tags") tags: Option[Seq[String]] = scala.None,
    /* Key last rotation date. (RFC 3339 format) */
    @named("rotated_at") rotatedAt: Option[OffsetDateTime] = scala.None,
    @named("rotation_policy") rotationPolicy: Option[ScalewayKeyManagerV1alpha1KeyRotationPolicy] = scala.None,
    /* Key origin. Refer to the `Key.Origin` enum for a description of values. */
    @named("origin") origin: Option[KeyEnums.Origin] = scala.None,
    /* Returns the time at which deletion was requested. (RFC 3339 format) */
    @named("deletion_requested_at") deletionRequestedAt: Option[OffsetDateTime] = scala.None,
    /* Region where the key is stored. */
    @named("region") region: Option[String] = scala.None
)

object KeyEnums:
  enum State:
    case `unknown_state`
    case `enabled`
    case `disabled`
    case `pending_key_material`
    case `scheduled_for_deletion`

  object State:
    import com.github.plokhotnyuk.jsoniter_scala.macros.*
    import com.github.plokhotnyuk.jsoniter_scala.core.*
    given stateCodec: JsonValueCodec[State] = JsonCodecMaker.make {
      CodecMakerConfig
        .withAdtLeafClassNameMapper { x =>
          JsonCodecMaker.simpleClassName(x) match
            case "unknown_state"          => "unknown_state"
            case "enabled"                => "enabled"
            case "disabled"               => "disabled"
            case "pending_key_material"   => "pending_key_material"
            case "scheduled_for_deletion" => "scheduled_for_deletion"
        }
        .withDiscriminatorFieldName(scala.None)
    }
  enum Origin:
    case `unknown_origin`
    case `scaleway_kms`
    case `external`

  object Origin:
    import com.github.plokhotnyuk.jsoniter_scala.macros.*
    import com.github.plokhotnyuk.jsoniter_scala.core.*
    given originCodec: JsonValueCodec[Origin] = JsonCodecMaker.make {
      CodecMakerConfig
        .withAdtLeafClassNameMapper { x =>
          JsonCodecMaker.simpleClassName(x) match
            case "unknown_origin" => "unknown_origin"
            case "scaleway_kms"   => "scaleway_kms"
            case "external"       => "external"
        }
        .withDiscriminatorFieldName(scala.None)
    }
end KeyEnums
