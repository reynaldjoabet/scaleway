/**
 * Key Manager API
 * Scaleway's Key Manager allows you to create, manage and use cryptographic keys in a centralized and secure service. All your cryptographic operations can be delegated to the Key Manager, which in turn ensures the security and availability of your keys.  Key Manager supports the following cryptographic operations: data encryption, data decryption, and data encryption key generation.     ## Concepts  Refer to our [dedicated concepts page](https://www.scaleway.com/en/docs/key-manager/concepts/) to find definitions of the different terms referring to Key Manager.    ## Quickstart  1. **Configure your environment variables.**      <Message type=\"note\">       This is an optional step that seeks to simplify your usage of the API.     </Message>      ```bash     export SCW_ACCESS_KEY=\"<API access key>\"     export SCW_SECRET_KEY=\"<API secret key>\"     export SCW_PROJECT_ID=\"<Scaleway Project ID>\"     ``` 2. **Create a key**. Run the following command to create a key that you can use to encrypt and decrypt your data:      ```bash     curl \"https://api.scaleway.com/key-manager/v1alpha1/regions/$REGION/keys\" \\       -H \"Content-Type: application/json\" \\       -H \"X-Auth-Token: $SCW_SECRET_KEY\" \\       -d '{         \"project_id\": \"$PROJECT_ID\",         \"name\": \"my-key\",         \"usage\": {             \"symmetric_encryption\": \"aes_256_gcm\"         }        }'     ``` 3. **Rotate your key**. Run the following command to generate a new version of your key. This operation renders your previous key version obsolete.      ```bash     curl \"https://api.scaleway.com/key-manager/v1alpha1/regions/$REGION/keys/<KEY_ID>/rotate\" \\       -H \"X-Auth-Token: $SCW_SECRET_KEY\" \\       -d '{         \"key_id\": \"$KEY_ID\"       }'     ``` 4. **Encrypt data**. Run the following command to encrypt data with the key you have created in step 2:      ```bash     curl \"https://api.scaleway.com/key-manager/v1alpha1/regions/$REGION/keys/<KEY_ID>/encrypt\" \\       -H \"X-Auth-Token: $SCW_SECRET_KEY\" \\       -d '{         \"key_id\": \"$KEY_ID\",         \"plaintext\": \"'\"$(echo -n \"plaintext-data\" | base64)\"'\"       }'     ``` 5. **Generate a data encryption key**. Run the following command to generate a data encryption key that you can use for cryptographic operations outside of Key Manager:      ```bash     curl \"https://api.scaleway.com/key-manager/v1alpha1/regions/$REGION/keys/<KEY_ID>/generate-data-key\" \\       -H \"X-Auth-Token: $SCW_SECRET_KEY\" \\       -d '{         \"key_id\": \"$KEY_ID\",         \"algorithm\": \"aes_256_gcm\"       }'     ```    <Message type=\"requirement\">  To perform the following steps, you must first ensure that:   - You have your [Organization and your Project ID](https://console.scaleway.com/project/settings)  - You have a [Scaleway account](https://console.scaleway.com/)  - You have created an [API key](https://www.scaleway.com/en/docs/iam/how-to/create-api-keys/) and that the API key has sufficient [IAM permissions](https://www.scaleway.com/en/docs/iam/reference-content/permission-sets/) to perform the actions described on this page  - You have [installed `curl`](https://curl.se/download.html) </Message>    ## Technical information  ### Regions  Scaleway's infrastructure spans different [regions and Availability Zones](https://www.scaleway.com/en/docs/console/account/reference-content/products-availability/).  Key Manager is available in the Paris, Amsterdam and Warsaw regions, which are represented by the following path parameters:  - fr-par - nl-ams - pl-waw  ## Technical limitations   - While Scaleway Key Manager is responsible for generating, encrypting, and decrypting [data encryption keys](https://www.scaleway.com/en/docs/key-manager/concepts/#data-encryption-key-dek/), it does not store, manage, or monitor them, nor does it engage in cryptographic operations with these keys. **You must use and manage data encryption keys outside of Key Manager**. Read our [documentation](https://www.scaleway.com/en/docs/key-manager/reference-content/security-recommendations/) to find out about security measures to be aware of while using Key Manager.  ### Symmetric encryption  - The maximum payload size that can be encrypted is 64KB of plaintext. - The maximum payload size that can be decrypted is around 131KB of data. - The only symmetric algorithm currently supported by Key Manager is AES-256-GCM.  ### Asymmetric encryption  - Key Manager supports the following asymmetric encryption algorithms:    * RSA-OAEP-2048-SHA256    * RSA-OAEP-3072-SHA256    * RSA-OAEP-4096-SHA256  ### Asymmetric signing  - Key Manager supports the following asymmetric signing algorithms:    * EC-P256-SHA256    * EC-P384-SHA256    * RSA-PSS-2048-SHA256    * RSA-PSS-3072-SHA256    * RSA-PSS-4096-SHA256    * RSA-PKCS1-2048-SHA256    * RSA-PKCS1-3072-SHA256    * RSA-PKCS1-4096-SHA256   ## Going further  For more information about Key Manager, you can check out the following pages:  * [Key Manager Documentation](https://www.scaleway.com/en/docs/key-manager/) * [Contact our support team](https://console.scaleway.com/support/tickets).
 *
 * The version of the OpenAPI document: v1alpha1
 * 
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */
package scaleway.keymanager.api

import scaleway.keymanager.models.CreateKeyRequest
import scaleway.keymanager.models.DataKey
import scaleway.keymanager.models.DecryptRequest
import scaleway.keymanager.models.DecryptResponse
import scaleway.keymanager.models.EncryptRequest
import scaleway.keymanager.models.EncryptResponse
import scaleway.keymanager.models.GenerateDataKeyRequest
import scaleway.keymanager.models.ImportKeyMaterialRequest
import scaleway.keymanager.models.Key
import scaleway.keymanager.models.ListAlgorithmsResponse
import scaleway.keymanager.models.ListKeysResponse
import scaleway.keymanager.models.OrderBy.*
import scaleway.keymanager.models.OrderBy
import scaleway.keymanager.models.PublicKey
import scaleway.keymanager.models.SignRequest
import scaleway.keymanager.models.SignResponse
import scaleway.keymanager.models.UpdateKeyRequest
import scaleway.keymanager.models.Usage.*
import scaleway.keymanager.models.Usage
import scaleway.keymanager.models.VerifyRequest
import scaleway.keymanager.models.VerifyResponse
import com.github.plokhotnyuk.jsoniter_scala.circe.JsoniterScalaCodec.*
import scaleway.keymanager.JsonSupport.{*, given}
import scaleway.keymanager.FormSerializable
import scaleway.keymanager.FormStyleFormat
import scaleway.keymanager.HeaderSerializable
import scaleway.keymanager.ApiKeyLocation
import scaleway.keymanager.PathStyleFormat
import scaleway.keymanager.PathSerializable
import scaleway.keymanager.CookieSerializable
import scaleway.keymanager.Helpers.*
import sttp.client4.jsoniter.*
import sttp.client4.*
import sttp.model.Method

object KeysApi:
  def apply(baseUrl: String = "https://api.scaleway.com"): KeysApi[scaleway.keymanager.Authorization.NoAuthorization.type] = KeysApi(baseUrl, scaleway.keymanager.Authorization.NoAuthorization)
  def withBasicAuth(baseUrl: String, username: String, password: String): KeysApi[scaleway.keymanager.Authorization.BasicAuth] =
    KeysApi(baseUrl, scaleway.keymanager.Authorization.BasicAuth(username, password))

  def withApiKeyAuth(baseUrl: String, apiKey: String): KeysApi[scaleway.keymanager.Authorization.ApiKey] =
    KeysApi(baseUrl, scaleway.keymanager.Authorization.ApiKey(apiKey))

  def withBearerTokenAuth(baseUrl: String, token: String): KeysApi[scaleway.keymanager.Authorization.BearerToken] =
    KeysApi(baseUrl, scaleway.keymanager.Authorization.BearerToken(token))

case class KeysApi[Auth <: scaleway.keymanager.Authorization] private (baseUrl: String, authConfig: scaleway.keymanager.Authorization):
  def withBasicAuth(username: String, password: String): KeysApi[scaleway.keymanager.Authorization.BasicAuth] =
    copy(authConfig = scaleway.keymanager.Authorization.BasicAuth(username, password))

  def withApiKeyAuth(apiKey: String): KeysApi[scaleway.keymanager.Authorization.ApiKey] =
    copy(authConfig = scaleway.keymanager.Authorization.ApiKey(apiKey))

  def withNoAuth: KeysApi[scaleway.keymanager.Authorization.NoAuthorization.type] =
    copy(authConfig = scaleway.keymanager.Authorization.NoAuthorization)

  def withBearerTokenAuth(token: String): KeysApi[scaleway.keymanager.Authorization.BearerToken] =
    copy(authConfig = scaleway.keymanager.Authorization.BearerToken(token))

  /**
   * Create a key in a given region specified by the `region` parameter. You can use keys to encrypt or decrypt arbitrary payloads, to sign and verify messages or to generate data encryption keys. **Data encryption keys are not stored in Key Manager**.
   * 
   * Expected answers:
   *   code 200 : Key ()
   * 
   * Available security schemes:
   *   scaleway (apiKey)
   * 
   * @param region The region you want to target
   * @param createKeyRequest 
   */
  def createKey(region: String, createKeyRequest: CreateKeyRequest)(using Auth <:< scaleway.keymanager.Authorization.ApiKey): sttp.client4.Request[Either[ResponseException[String], Key]] =
    val regionPathParam = PathSerializable.serialize("region", region, PathStyleFormat.SIMPLE, false)
    val requestURL =
      uri"$baseUrl/key-manager/v1alpha1/regions/${regionPathParam}/keys"

    basicRequest
      .method(Method.POST, requestURL)
      .contentType("application/json")
      .auth(authConfig, scaleway.keymanager.ApiKeyLocation.HEADER, "X-Auth-Token")
      .body(asJson(createKeyRequest))
      .response(asJson[Key])

  /**
   * Decrypt an encrypted payload using an existing key, specified by the `key_id` parameter. The maximum payload size that can be decrypted is equivalent to the encrypted output of 64 KB of data (around 131 KB).
   * 
   * Expected answers:
   *   code 200 : DecryptResponse ()
   * 
   * Available security schemes:
   *   scaleway (apiKey)
   * 
   * @param region The region you want to target
   * @param keyId ID of the key to decrypt with. The key must have an usage set to `symmetric_encryption` or `asymmetric_encryption`. (UUID format)
   * @param decryptRequest 
   */
  def decrypt(region: String, keyId: String, decryptRequest: DecryptRequest)(using Auth <:< scaleway.keymanager.Authorization.ApiKey): sttp.client4.Request[Either[ResponseException[String], DecryptResponse]] =
    val regionPathParam = PathSerializable.serialize("region", region, PathStyleFormat.SIMPLE, false)
    val keyIdPathParam = PathSerializable.serialize("key_id", keyId, PathStyleFormat.SIMPLE, false)
    val requestURL =
      uri"$baseUrl/key-manager/v1alpha1/regions/${regionPathParam}/keys/${keyIdPathParam}/decrypt"

    basicRequest
      .method(Method.POST, requestURL)
      .contentType("application/json")
      .auth(authConfig, scaleway.keymanager.ApiKeyLocation.HEADER, "X-Auth-Token")
      .body(asJson(decryptRequest))
      .response(asJson[DecryptResponse])

  /**
   * Permanently delete a key specified by the `region` and `key_id` parameters. This action is irreversible. Any data encrypted with this key, including data encryption keys, will no longer be decipherable.
   * 
   * Expected answers:
   *   code 204 :  ()
   * 
   * Available security schemes:
   *   scaleway (apiKey)
   * 
   * @param region The region you want to target
   * @param keyId ID of the key to delete. (UUID format)
   */
  def deleteKey(region: String, keyId: String)(using Auth <:< scaleway.keymanager.Authorization.ApiKey): sttp.client4.Request[Either[ResponseException[String], Unit]] =
    val regionPathParam = PathSerializable.serialize("region", region, PathStyleFormat.SIMPLE, false)
    val keyIdPathParam = PathSerializable.serialize("key_id", keyId, PathStyleFormat.SIMPLE, false)
    val requestURL =
      uri"$baseUrl/key-manager/v1alpha1/regions/${regionPathParam}/keys/${keyIdPathParam}"

    basicRequest
      .method(Method.DELETE, requestURL)
      .contentType("application/json")
      .auth(authConfig, scaleway.keymanager.ApiKeyLocation.HEADER, "X-Auth-Token")
      .response(asString.mapWithMetadata(ResponseAs.deserializeRightWithError(_ => Right(()))))

  /**
   * Delete previously imported key material. This renders the associated cryptographic key unusable for any operation. The key's origin must be `external`.
   * 
   * Expected answers:
   *   code 204 :  ()
   * 
   * Available security schemes:
   *   scaleway (apiKey)
   * 
   * @param region The region you want to target
   * @param keyId ID of the key of which to delete the key material. (UUID format)
   * @param body 
   */
  def deleteKeyMaterial(region: String, keyId: String, body: io.circe.Json)(using Auth <:< scaleway.keymanager.Authorization.ApiKey): sttp.client4.Request[Either[ResponseException[String], Unit]] =
    val regionPathParam = PathSerializable.serialize("region", region, PathStyleFormat.SIMPLE, false)
    val keyIdPathParam = PathSerializable.serialize("key_id", keyId, PathStyleFormat.SIMPLE, false)
    val requestURL =
      uri"$baseUrl/key-manager/v1alpha1/regions/${regionPathParam}/keys/${keyIdPathParam}/delete-key-material"

    basicRequest
      .method(Method.POST, requestURL)
      .contentType("application/json")
      .auth(authConfig, scaleway.keymanager.ApiKeyLocation.HEADER, "X-Auth-Token")
      .body(asJson(body))
      .response(asString.mapWithMetadata(ResponseAs.deserializeRightWithError(_ => Right(()))))

  /**
   * Disable a given key, preventing it to be used for cryptographic operations. Disabling a key renders it unusable. You must specify the `region` and `key_id` parameters.
   * 
   * Expected answers:
   *   code 200 : Key ()
   * 
   * Available security schemes:
   *   scaleway (apiKey)
   * 
   * @param region The region you want to target
   * @param keyId ID of the key to disable. (UUID format)
   * @param body 
   */
  def disableKey(region: String, keyId: String, body: io.circe.Json)(using Auth <:< scaleway.keymanager.Authorization.ApiKey): sttp.client4.Request[Either[ResponseException[String], Key]] =
    val regionPathParam = PathSerializable.serialize("region", region, PathStyleFormat.SIMPLE, false)
    val keyIdPathParam = PathSerializable.serialize("key_id", keyId, PathStyleFormat.SIMPLE, false)
    val requestURL =
      uri"$baseUrl/key-manager/v1alpha1/regions/${regionPathParam}/keys/${keyIdPathParam}/disable"

    basicRequest
      .method(Method.POST, requestURL)
      .contentType("application/json")
      .auth(authConfig, scaleway.keymanager.ApiKeyLocation.HEADER, "X-Auth-Token")
      .body(asJson(body))
      .response(asJson[Key])

  /**
   * Enable a given key to be used for cryptographic operations. Enabling a key allows you to make a disabled key usable again. You must specify the `region` and `key_id` parameters.
   * 
   * Expected answers:
   *   code 200 : Key ()
   * 
   * Available security schemes:
   *   scaleway (apiKey)
   * 
   * @param region The region you want to target
   * @param keyId ID of the key to enable. (UUID format)
   * @param body 
   */
  def enableKey(region: String, keyId: String, body: io.circe.Json)(using Auth <:< scaleway.keymanager.Authorization.ApiKey): sttp.client4.Request[Either[ResponseException[String], Key]] =
    val regionPathParam = PathSerializable.serialize("region", region, PathStyleFormat.SIMPLE, false)
    val keyIdPathParam = PathSerializable.serialize("key_id", keyId, PathStyleFormat.SIMPLE, false)
    val requestURL =
      uri"$baseUrl/key-manager/v1alpha1/regions/${regionPathParam}/keys/${keyIdPathParam}/enable"

    basicRequest
      .method(Method.POST, requestURL)
      .contentType("application/json")
      .auth(authConfig, scaleway.keymanager.ApiKeyLocation.HEADER, "X-Auth-Token")
      .body(asJson(body))
      .response(asJson[Key])

  /**
   * Encrypt a payload using an existing key, specified by the `key_id` parameter. The maximum payload size that can be encrypted is 64 KB of plaintext.
   * 
   * Expected answers:
   *   code 200 : EncryptResponse ()
   * 
   * Available security schemes:
   *   scaleway (apiKey)
   * 
   * @param region The region you want to target
   * @param keyId ID of the key to use for encryption. The key must have an usage set to `symmetric_encryption` or `asymmetric_encryption`. (UUID format)
   * @param encryptRequest 
   */
  def encrypt(region: String, keyId: String, encryptRequest: EncryptRequest)(using Auth <:< scaleway.keymanager.Authorization.ApiKey): sttp.client4.Request[Either[ResponseException[String], EncryptResponse]] =
    val regionPathParam = PathSerializable.serialize("region", region, PathStyleFormat.SIMPLE, false)
    val keyIdPathParam = PathSerializable.serialize("key_id", keyId, PathStyleFormat.SIMPLE, false)
    val requestURL =
      uri"$baseUrl/key-manager/v1alpha1/regions/${regionPathParam}/keys/${keyIdPathParam}/encrypt"

    basicRequest
      .method(Method.POST, requestURL)
      .contentType("application/json")
      .auth(authConfig, scaleway.keymanager.ApiKeyLocation.HEADER, "X-Auth-Token")
      .body(asJson(encryptRequest))
      .response(asJson[EncryptResponse])

  /**
   * Create a new data encryption key for cryptographic operations outside of Key Manager. The data encryption key is encrypted and must be decrypted using the key you have created in Key Manager.  The data encryption key is returned in plaintext and ciphertext but it should only be stored in its encrypted form (ciphertext). Key Manager does not store your data encryption key. To retrieve your key's plaintext, use the `Decrypt` method with your key's ID and ciphertext.
   * 
   * Expected answers:
   *   code 200 : DataKey ()
   * 
   * Available security schemes:
   *   scaleway (apiKey)
   * 
   * @param region The region you want to target
   * @param keyId ID of the key. (UUID format)
   * @param generateDataKeyRequest 
   */
  def generateDataKey(region: String, keyId: String, generateDataKeyRequest: GenerateDataKeyRequest)(using Auth <:< scaleway.keymanager.Authorization.ApiKey): sttp.client4.Request[Either[ResponseException[String], DataKey]] =
    val regionPathParam = PathSerializable.serialize("region", region, PathStyleFormat.SIMPLE, false)
    val keyIdPathParam = PathSerializable.serialize("key_id", keyId, PathStyleFormat.SIMPLE, false)
    val requestURL =
      uri"$baseUrl/key-manager/v1alpha1/regions/${regionPathParam}/keys/${keyIdPathParam}/generate-data-key"

    basicRequest
      .method(Method.POST, requestURL)
      .contentType("application/json")
      .auth(authConfig, scaleway.keymanager.ApiKeyLocation.HEADER, "X-Auth-Token")
      .body(asJson(generateDataKeyRequest))
      .response(asJson[DataKey])

  /**
   * Retrieve metadata for a specified key using the `region` and `key_id` parameters.
   * 
   * Expected answers:
   *   code 200 : Key ()
   * 
   * Available security schemes:
   *   scaleway (apiKey)
   * 
   * @param region The region you want to target
   * @param keyId ID of the key to target. (UUID format)
   */
  def getKey(region: String, keyId: String)(using Auth <:< scaleway.keymanager.Authorization.ApiKey): sttp.client4.Request[Either[ResponseException[String], Key]] =
    val regionPathParam = PathSerializable.serialize("region", region, PathStyleFormat.SIMPLE, false)
    val keyIdPathParam = PathSerializable.serialize("key_id", keyId, PathStyleFormat.SIMPLE, false)
    val requestURL =
      uri"$baseUrl/key-manager/v1alpha1/regions/${regionPathParam}/keys/${keyIdPathParam}"

    basicRequest
      .method(Method.GET, requestURL)
      .contentType("application/json")
      .auth(authConfig, scaleway.keymanager.ApiKeyLocation.HEADER, "X-Auth-Token")
      .response(asJson[Key])

  /**
   * Retrieves the public portion of an asymmetric cryptographic key in PEM format.
   * 
   * Expected answers:
   *   code 200 : PublicKey ()
   * 
   * Available security schemes:
   *   scaleway (apiKey)
   * 
   * @param region The region you want to target
   * @param keyId ID of the key. (UUID format)
   */
  def getPublicKey(region: String, keyId: String)(using Auth <:< scaleway.keymanager.Authorization.ApiKey): sttp.client4.Request[Either[ResponseException[String], PublicKey]] =
    val regionPathParam = PathSerializable.serialize("region", region, PathStyleFormat.SIMPLE, false)
    val keyIdPathParam = PathSerializable.serialize("key_id", keyId, PathStyleFormat.SIMPLE, false)
    val requestURL =
      uri"$baseUrl/key-manager/v1alpha1/regions/${regionPathParam}/keys/${keyIdPathParam}/public-key"

    basicRequest
      .method(Method.GET, requestURL)
      .contentType("application/json")
      .auth(authConfig, scaleway.keymanager.ApiKeyLocation.HEADER, "X-Auth-Token")
      .response(asJson[PublicKey])

  /**
   * Import externally generated key material into Key Manager to derive a new cryptographic key. The key's origin must be `external`.
   * 
   * Expected answers:
   *   code 200 : Key ()
   * 
   * Available security schemes:
   *   scaleway (apiKey)
   * 
   * @param region The region you want to target
   * @param keyId ID of the key in which to import key material. The key's origin must be `external`. (UUID format)
   * @param importKeyMaterialRequest 
   */
  def importKeyMaterial(region: String, keyId: String, importKeyMaterialRequest: ImportKeyMaterialRequest)(using Auth <:< scaleway.keymanager.Authorization.ApiKey): sttp.client4.Request[Either[ResponseException[String], Key]] =
    val regionPathParam = PathSerializable.serialize("region", region, PathStyleFormat.SIMPLE, false)
    val keyIdPathParam = PathSerializable.serialize("key_id", keyId, PathStyleFormat.SIMPLE, false)
    val requestURL =
      uri"$baseUrl/key-manager/v1alpha1/regions/${regionPathParam}/keys/${keyIdPathParam}/import-key-material"

    basicRequest
      .method(Method.POST, requestURL)
      .contentType("application/json")
      .auth(authConfig, scaleway.keymanager.ApiKeyLocation.HEADER, "X-Auth-Token")
      .body(asJson(importKeyMaterialRequest))
      .response(asJson[Key])

  /**
   * Lists all cryptographic algorithms supported by the Key Manager service.
   * 
   * Expected answers:
   *   code 200 : ListAlgorithmsResponse ()
   * 
   * Available security schemes:
   *   scaleway (apiKey)
   * 
   * @param region The region you want to target
   * @param usages Filter by key usage.
   */
  def listAlgorithms(region: String, usages: Seq[Usage])(using Auth <:< scaleway.keymanager.Authorization.ApiKey): sttp.client4.Request[Either[ResponseException[String], ListAlgorithmsResponse]] =
    val regionPathParam = PathSerializable.serialize("region", region, PathStyleFormat.SIMPLE, false)
    val requestURL =
      uri"$baseUrl/key-manager/v1alpha1/regions/${regionPathParam}/algorithms"
        .addParams(FormSerializable.serialize("usages", usages, FormStyleFormat.FORM, true): _*)

    basicRequest
      .method(Method.GET, requestURL)
      .contentType("application/json")
      .auth(authConfig, scaleway.keymanager.ApiKeyLocation.HEADER, "X-Auth-Token")
      .response(asJson[ListAlgorithmsResponse])

  /**
   * Retrieve a list of keys across all Projects in an Organization or within a specific Project.  If the user has permissions for all current and future projects: Either organization_id or project_id is required. If the user has permissions for all current projects or only specific projects: The project_id is required. The `region` parameter in path is needed in both case.
   * 
   * Expected answers:
   *   code 200 : ListKeysResponse ()
   * 
   * Available security schemes:
   *   scaleway (apiKey)
   * 
   * @param region The region you want to target
   * @param scheduledForDeletion Filter keys based on their deletion status. By default, only keys not scheduled for deletion are returned in the output.
   * @param organizationId (Optional) Filter by Organization ID. (UUID format)
   * @param projectId (Optional) Filter by Project ID. (UUID format)
   * @param orderBy 
   * @param page 
   * @param pageSize 
   * @param tags (Optional) List of tags to filter on.
   * @param name (Optional) Filter by key name.
   * @param usage (Optional) Filter keys by usage. Select from symmetric encryption, asymmetric encryption, or asymmetric signing.
   */
  def listKeys(region: String, scheduledForDeletion: Boolean, organizationId: Option[String] = scala.None, projectId: Option[String] = scala.None, orderBy: Option[OrderBy] = scala.None, page: Option[Int] = scala.None, pageSize: Option[Int] = scala.None, tags: Seq[String], name: Option[String] = scala.None, usage: Option[String] = scala.None)(using Auth <:< scaleway.keymanager.Authorization.ApiKey): sttp.client4.Request[Either[ResponseException[String], ListKeysResponse]] =
    val regionPathParam = PathSerializable.serialize("region", region, PathStyleFormat.SIMPLE, false)
    val requestURL =
      uri"$baseUrl/key-manager/v1alpha1/regions/${regionPathParam}/keys"
        .addParams(FormSerializable.serialize("organization_id", organizationId, FormStyleFormat.FORM, true): _*)
        .addParams(FormSerializable.serialize("project_id", projectId, FormStyleFormat.FORM, true): _*)
        .addParams(FormSerializable.serialize("order_by", orderBy, FormStyleFormat.FORM, true): _*)
        .addParams(FormSerializable.serialize("page", page, FormStyleFormat.FORM, true): _*)
        .addParams(FormSerializable.serialize("page_size", pageSize, FormStyleFormat.FORM, true): _*)
        .addParams(FormSerializable.serialize("tags", tags, FormStyleFormat.FORM, true): _*)
        .addParams(FormSerializable.serialize("name", name, FormStyleFormat.FORM, true): _*)
        .addParams(FormSerializable.serialize("usage", usage, FormStyleFormat.FORM, true): _*)
        .addParams(FormSerializable.serialize("scheduled_for_deletion", scheduledForDeletion, FormStyleFormat.FORM, true): _*)

    basicRequest
      .method(Method.GET, requestURL)
      .contentType("application/json")
      .auth(authConfig, scaleway.keymanager.ApiKeyLocation.HEADER, "X-Auth-Token")
      .response(asJson[ListKeysResponse])

  /**
   * Apply protection to a given key specified by the `key_id` parameter. Applying key protection means that your key can be used and modified, but it cannot be deleted.
   * 
   * Expected answers:
   *   code 200 : Key ()
   * 
   * Available security schemes:
   *   scaleway (apiKey)
   * 
   * @param region The region you want to target
   * @param keyId ID of the key to apply key protection to. (UUID format)
   * @param body 
   */
  def protectKey(region: String, keyId: String, body: io.circe.Json)(using Auth <:< scaleway.keymanager.Authorization.ApiKey): sttp.client4.Request[Either[ResponseException[String], Key]] =
    val regionPathParam = PathSerializable.serialize("region", region, PathStyleFormat.SIMPLE, false)
    val keyIdPathParam = PathSerializable.serialize("key_id", keyId, PathStyleFormat.SIMPLE, false)
    val requestURL =
      uri"$baseUrl/key-manager/v1alpha1/regions/${regionPathParam}/keys/${keyIdPathParam}/protect"

    basicRequest
      .method(Method.POST, requestURL)
      .contentType("application/json")
      .auth(authConfig, scaleway.keymanager.ApiKeyLocation.HEADER, "X-Auth-Token")
      .body(asJson(body))
      .response(asJson[Key])

  /**
   * Restore a key and all its rotations scheduled for deletion specified by the `region` and `key_id` parameters.
   * 
   * Expected answers:
   *   code 200 : Key ()
   * 
   * Available security schemes:
   *   scaleway (apiKey)
   * 
   * @param region The region you want to target
   * @param keyId 
   * @param body 
   */
  def restoreKey(region: String, keyId: String, body: io.circe.Json)(using Auth <:< scaleway.keymanager.Authorization.ApiKey): sttp.client4.Request[Either[ResponseException[String], Key]] =
    val regionPathParam = PathSerializable.serialize("region", region, PathStyleFormat.SIMPLE, false)
    val keyIdPathParam = PathSerializable.serialize("key_id", keyId, PathStyleFormat.SIMPLE, false)
    val requestURL =
      uri"$baseUrl/key-manager/v1alpha1/regions/${regionPathParam}/keys/${keyIdPathParam}/restore"

    basicRequest
      .method(Method.POST, requestURL)
      .contentType("application/json")
      .auth(authConfig, scaleway.keymanager.ApiKeyLocation.HEADER, "X-Auth-Token")
      .body(asJson(body))
      .response(asJson[Key])

  /**
   * Generate a new version of an existing key with new key material. Previous key versions remain usable to decrypt previously encrypted data, but the key's new version will be used for subsequent encryption operations and data key generation.
   * 
   * Expected answers:
   *   code 200 : Key ()
   * 
   * Available security schemes:
   *   scaleway (apiKey)
   * 
   * @param region The region you want to target
   * @param keyId ID of the key to rotate. (UUID format)
   * @param body 
   */
  def rotateKey(region: String, keyId: String, body: io.circe.Json)(using Auth <:< scaleway.keymanager.Authorization.ApiKey): sttp.client4.Request[Either[ResponseException[String], Key]] =
    val regionPathParam = PathSerializable.serialize("region", region, PathStyleFormat.SIMPLE, false)
    val keyIdPathParam = PathSerializable.serialize("key_id", keyId, PathStyleFormat.SIMPLE, false)
    val requestURL =
      uri"$baseUrl/key-manager/v1alpha1/regions/${regionPathParam}/keys/${keyIdPathParam}/rotate"

    basicRequest
      .method(Method.POST, requestURL)
      .contentType("application/json")
      .auth(authConfig, scaleway.keymanager.ApiKeyLocation.HEADER, "X-Auth-Token")
      .body(asJson(body))
      .response(asJson[Key])

  /**
   * Use a given key to sign a message digest. The key must have its usage set to `asymmetric_signing`. The digest must be created using the same digest algorithm that is defined in the key's algorithm configuration.
   * 
   * Expected answers:
   *   code 200 : SignResponse ()
   * 
   * Available security schemes:
   *   scaleway (apiKey)
   * 
   * @param region The region you want to target
   * @param keyId ID of the key to use for signing. (UUID format)
   * @param signRequest 
   */
  def sign(region: String, keyId: String, signRequest: SignRequest)(using Auth <:< scaleway.keymanager.Authorization.ApiKey): sttp.client4.Request[Either[ResponseException[String], SignResponse]] =
    val regionPathParam = PathSerializable.serialize("region", region, PathStyleFormat.SIMPLE, false)
    val keyIdPathParam = PathSerializable.serialize("key_id", keyId, PathStyleFormat.SIMPLE, false)
    val requestURL =
      uri"$baseUrl/key-manager/v1alpha1/regions/${regionPathParam}/keys/${keyIdPathParam}/sign"

    basicRequest
      .method(Method.POST, requestURL)
      .contentType("application/json")
      .auth(authConfig, scaleway.keymanager.ApiKeyLocation.HEADER, "X-Auth-Token")
      .body(asJson(signRequest))
      .response(asJson[SignResponse])

  /**
   * Remove key protection from a given key specified by the `key_id` parameter. Removing key protection means that your key can be deleted anytime.
   * 
   * Expected answers:
   *   code 200 : Key ()
   * 
   * Available security schemes:
   *   scaleway (apiKey)
   * 
   * @param region The region you want to target
   * @param keyId ID of the key to remove key protection from. (UUID format)
   * @param body 
   */
  def unprotectKey(region: String, keyId: String, body: io.circe.Json)(using Auth <:< scaleway.keymanager.Authorization.ApiKey): sttp.client4.Request[Either[ResponseException[String], Key]] =
    val regionPathParam = PathSerializable.serialize("region", region, PathStyleFormat.SIMPLE, false)
    val keyIdPathParam = PathSerializable.serialize("key_id", keyId, PathStyleFormat.SIMPLE, false)
    val requestURL =
      uri"$baseUrl/key-manager/v1alpha1/regions/${regionPathParam}/keys/${keyIdPathParam}/unprotect"

    basicRequest
      .method(Method.POST, requestURL)
      .contentType("application/json")
      .auth(authConfig, scaleway.keymanager.ApiKeyLocation.HEADER, "X-Auth-Token")
      .body(asJson(body))
      .response(asJson[Key])

  /**
   * Modify a key's metadata including name, description and tags, specified by the `key_id` and `region` parameters.
   * 
   * Expected answers:
   *   code 200 : Key ()
   * 
   * Available security schemes:
   *   scaleway (apiKey)
   * 
   * @param region The region you want to target
   * @param keyId ID of the key to update. (UUID format)
   * @param updateKeyRequest 
   */
  def updateKey(region: String, keyId: String, updateKeyRequest: UpdateKeyRequest)(using Auth <:< scaleway.keymanager.Authorization.ApiKey): sttp.client4.Request[Either[ResponseException[String], Key]] =
    val regionPathParam = PathSerializable.serialize("region", region, PathStyleFormat.SIMPLE, false)
    val keyIdPathParam = PathSerializable.serialize("key_id", keyId, PathStyleFormat.SIMPLE, false)
    val requestURL =
      uri"$baseUrl/key-manager/v1alpha1/regions/${regionPathParam}/keys/${keyIdPathParam}"

    basicRequest
      .method(Method.PATCH, requestURL)
      .contentType("application/json")
      .auth(authConfig, scaleway.keymanager.ApiKeyLocation.HEADER, "X-Auth-Token")
      .body(asJson(updateKeyRequest))
      .response(asJson[Key])

  /**
   * Use a given key to verify a message signature against a message digest. The key must have its usage set to `asymmetric_signing`. The message digest must be generated using the same digest algorithm that is defined in the key's algorithm configuration.
   * 
   * Expected answers:
   *   code 200 : VerifyResponse ()
   * 
   * Available security schemes:
   *   scaleway (apiKey)
   * 
   * @param region The region you want to target
   * @param keyId ID of the key to use for signature verification. (UUID format)
   * @param verifyRequest 
   */
  def verify(region: String, keyId: String, verifyRequest: VerifyRequest)(using Auth <:< scaleway.keymanager.Authorization.ApiKey): sttp.client4.Request[Either[ResponseException[String], VerifyResponse]] =
    val regionPathParam = PathSerializable.serialize("region", region, PathStyleFormat.SIMPLE, false)
    val keyIdPathParam = PathSerializable.serialize("key_id", keyId, PathStyleFormat.SIMPLE, false)
    val requestURL =
      uri"$baseUrl/key-manager/v1alpha1/regions/${regionPathParam}/keys/${keyIdPathParam}/verify"

    basicRequest
      .method(Method.POST, requestURL)
      .contentType("application/json")
      .auth(authConfig, scaleway.keymanager.ApiKeyLocation.HEADER, "X-Auth-Token")
      .body(asJson(verifyRequest))
      .response(asJson[VerifyResponse])

end KeysApi