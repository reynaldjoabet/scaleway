/**
 * Managed MongoDB®
 * Managed MongoDB® Databases provide fully-managed document Database Instances, with MongoDB® as a database engine.  Document databases enable users to store and retrieve data in a document format, such as `json`. Compared to traditional relational databases where data is stored in a table-like format, document-type storage supports storing multiple nested keys and values in each document key.  <Message type=\"note\">   Managed MongoDB® is currently in public beta. [Fill out the form](https://www.scaleway.com/en/managed-mongodb-form/) to be notified of the General Availability or if you want to get in touch with our team about Managed MongoDB®. </Message>     ## Concepts  Refer to our [dedicated concepts page](https://www.scaleway.com/en/docs/managed-mongodb-databases/concepts/) to find definitions of the different terms referring to Managed MongoDB®.     ## Quickstart  1. Configure your environment variables.     <Message type=\"note\">     This is an optional step that seeks to simplify your usage of the APIs.     </Message>      ```bash     export SCW_ACCESS_KEY=\"<API access key>\"     export SCW_SECRET_KEY=\"<API secret key>\"     export SCW_REGION=\"<Scaleway region>\"     ``` 2. Edit the POST request payload you will use to create your Database Instance. Replace the parameters in the following example:     ```json       '{       \"project_id\": \"d8e65f2b-cce9-40b7-80fc-6a2902db6826\",       \"name\": \"myDB\",       \"node_amount\": 1,       \"user_name\": \"my_initial_user\",       \"password\": \"thiZ_is_v0ry_s3cret\",       \"version\": \"7.0\",       \"tags\": [\"donnerstag\"],       \"node_type\": \"mgdb-pro2-l\",       \"volume\": [         {         \"size_bytes\": 10000000000,         \"type\": \"sbs_5k\"         },       ],       \"endpoints\": [         {           \"public_network\": {}         }       ]       }'     ```     | Parameter        | Description                                                        |     | :--------------- | :----------------------------------------------------------------- |     | `project_id`     | **REQUIRED** The ID of the Project you want to create your Database Instance in. To find your Project ID you can **[list the projects](/api/account#path-projects-list-all-projects-of-an-organization)** or consult the **[Scaleway console](https://console.scaleway.com/project/settings)**. |     | `name`           | **REQUIRED** Name of the Database Instance                                          |     | `node_amount`           | **REQUIRED** Number of nodes in the Database Instance. You can select either 1 or 3.                             |     | `user_name`      | **REQUIRED** Identifier of the default user, which is created concurrently with the Database Instance |     | `password`       | **REQUIRED** Password for the default user |     | `version`         | **REQUIRED** Version ID of the MongoDB® engine. To check the list of available version you can use the following endpoint: `https://api.scaleway.com/mongodb/v1/regions/$SCW_REGION/versions`                                           |     | `tags`           | The list of tags `[\"tag1\", \"tag2\", ...]` that will be associated with the Database Instance. Tags can be appended to the query of the [List Database Instances](#path-instances-list-mongodbtm-database-instances) call to show results for only the Database Instances using a specific tag. You can also combine tags to list Database Instances that possess all the appended tags. |     | `node_type`      | **REQUIRED** The node type. To check the list of available node types you can use the following endpoint: `https://api.scaleway.com/mongodb/v1/regions/$SCW_REGION/node-types`                             |     | `volume.type`    | **REQUIRED** Type of volume where data is stored. You can specify either `sbs_5k` or `sbs_15k`. The default value is `sbs_5k` |     | `volume.size_bytes`    | **REQUIRED** Volume size expressed in bytes. For example 30GB is expressed as 30000000000 |     | `endpoints`    | **REQUIRED** Network connection point that allows you to access and interact with your managed Database Instance. You can leave this empty as it will be automatically generated. It takes on the following format: `{instance_id}.mgdb.{region}.scw.cloud\"`  |  3. Create a Database Instance by running the following command. Make sure you include the payload you edited in the previous step.     ```bash     curl -X POST \\       -H \"X-Auth-Token: $SCW_SECRET_KEY\" \\       \"Content-Type: application/json\" \\       https://api.scaleway.com/mongodb/v1/regions/$SCW_REGION/instances \\       -d '{       \"project_id\": \"d8e65f2b-cce9-40b7-80fc-6a2902db6826\",       \"name\": \"myDB\",       \"node_amount\": 1,       \"user_name\": \"my_initial_user\",       \"password\": \"thiZ_is_v0ry_s3cret\",       \"version\": \"7.0\",       \"tags\": [\"donnerstag\"],       \"node_type\": \"mgdb-pro2-l\",       \"volume\": [         {         \"size_bytes\": 10000000000,         \"type\": \"sbs_5k\"         }       ],       \"endpoints\":[         {           \"public_network\": {}         }       ],       }'     ``` 4. List your Database Instances.     ```bash     curl -X GET \\       -H \"Content-Type: application/json\" \\       -H \"X-Auth-Token: $SCW_SECRET_KEY\" https://api.scaleway.com/mongodb/v1/regions/$SCW_REGION/instances     ```      You should get a response like the following:      <Message type=\"note\">     This is a response example, the UUIDs and IP address displayed are not real.     </Message>      ```json     {       \"id\": \"ffc473a3-250a-40a1-8d12-0d8c47b2ac63\",       \"name\": \"cluster\",       \"project_id\": \"d8e65f2b-cce9-40b7-80fc-6a2902db6826\",       \"status\": \"provisioning\",       \"version\": \"7.0\",       \"tags\": [],       \"settings\": [],       \"node_amount\": 1,       \"node_type\": \"mgdb-pro2-l\",       \"volume\": {         \"type\": \"sbs_5k\",         \"size_bytes\": 10000000000       },       \"endpoints\": [         {           \"id\": \"1210e327-5f6e-4757-834d-483729dcd330\",           \"ips\": [],           \"dns_records\": [             \"ffc473a3-250a-40a1-8d12-0d8c47b2ac63.mgdb.fr-par.scw.cloud\"           ],           \"port\": 27017,           \"public_network\": {}         }       ],       \"created_at\": \"2024-10-17T12:26:46.473753Z\",       \"region\": \"fr-par\"     }      ``` 5. Retrieve your Database Instance ID from the response. 6. Get your TLS certificate in the console. 7. Connect to your Database Instance with the `mongosh` client.     ```bash     mongosh \"mongodb+srv://{database_instance_id}.mgdb.{region}.scw.cloud\" --tlsCAFile {your_certificate.pem} -u {username}     ```     <Message type=\"note\">     Alternatively, you can connect using different clients. Refer to the [How to connect to a MongoDB Database Instance](https://www.scaleway.com/en/docs/managed-mongodb-databases/how-to/connect-database-instance/) documentation page for more information.     </Message>  8. Enter the database password that you defined upon creation.  You are now connected to your Managed Database.   <Message type=\"requirement\"> To perform the following steps, you must first ensure that:   - you have an account and are logged into the [Scaleway console](https://console.scaleway.com/organization)   - you have created an [API key](https://www.scaleway.com/en/docs/iam/how-to/create-api-keys/) and that the API key has sufficient [IAM permissions](https://www.scaleway.com/en/docs/iam/reference-content/permission-sets/) to perform the actions described on this page.   - you have [installed `curl`](https://curl.se/download.html) </Message>   ## Technical Information  ### Regions  Scaleway's infrastructure is spread across different [regions and Availability Zones](https://www.scaleway.com/en/docs/account/reference-content/products-availability/).  Currently, Managed MongoDB® is available only in the Paris region, which is represented by the following path parameter:  - `fr-par`   ### MongoDB® specifications  #### User Management  - Currently, a single user is created when you create your Database Instance. This user has an `admin` role. - Users with an `admin` role have access to all logical databases and can create new ones.  ## Going Further  For more information about Managed MongoDB®, you can check out the following pages:  * [Managed MongoDB® Documentation](https://www.scaleway.com/en/docs/managed-mongodb-databases/) * [Scaleway Slack Community](https://scaleway-community.slack.com/) join the #database channel * [Contact our support team](https://console.scaleway.com/support/tickets).
 *
 * The version of the OpenAPI document: v1
 * 
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */
package scaleway.mongodb.api

import scaleway.mongodb.models.ListMaintenancesResponse
import scaleway.mongodb.models.Maintenance
import com.github.plokhotnyuk.jsoniter_scala.circe.JsoniterScalaCodec.*
import scaleway.mongodb.JsonSupport.{*, given}
import scaleway.mongodb.FormSerializable
import scaleway.mongodb.FormStyleFormat
import scaleway.mongodb.HeaderSerializable
import scaleway.mongodb.ApiKeyLocation
import scaleway.mongodb.PathStyleFormat
import scaleway.mongodb.PathSerializable
import scaleway.mongodb.CookieSerializable
import scaleway.mongodb.Helpers.*
import sttp.client4.jsoniter.*
import sttp.client4.*
import sttp.model.Method

object MaintenanceOperations:
  def apply(baseUrl: String = "https://api.scaleway.com"): MaintenanceOperations[scaleway.mongodb.Authorization.NoAuthorization.type] = MaintenanceOperations(baseUrl, scaleway.mongodb.Authorization.NoAuthorization)
  def withBasicAuth(baseUrl: String, username: String, password: String): MaintenanceOperations[scaleway.mongodb.Authorization.BasicAuth] =
    MaintenanceOperations(baseUrl, scaleway.mongodb.Authorization.BasicAuth(username, password))

  def withApiKeyAuth(baseUrl: String, apiKey: String): MaintenanceOperations[scaleway.mongodb.Authorization.ApiKey] =
    MaintenanceOperations(baseUrl, scaleway.mongodb.Authorization.ApiKey(apiKey))

  def withBearerTokenAuth(baseUrl: String, token: String): MaintenanceOperations[scaleway.mongodb.Authorization.BearerToken] =
    MaintenanceOperations(baseUrl, scaleway.mongodb.Authorization.BearerToken(token))

case class MaintenanceOperations[Auth <: scaleway.mongodb.Authorization] private (baseUrl: String, authConfig: scaleway.mongodb.Authorization):
  def withBasicAuth(username: String, password: String): MaintenanceOperations[scaleway.mongodb.Authorization.BasicAuth] =
    copy(authConfig = scaleway.mongodb.Authorization.BasicAuth(username, password))

  def withApiKeyAuth(apiKey: String): MaintenanceOperations[scaleway.mongodb.Authorization.ApiKey] =
    copy(authConfig = scaleway.mongodb.Authorization.ApiKey(apiKey))

  def withNoAuth: MaintenanceOperations[scaleway.mongodb.Authorization.NoAuthorization.type] =
    copy(authConfig = scaleway.mongodb.Authorization.NoAuthorization)

  def withBearerTokenAuth(token: String): MaintenanceOperations[scaleway.mongodb.Authorization.BearerToken] =
    copy(authConfig = scaleway.mongodb.Authorization.BearerToken(token))

  /**
   * Expected answers:
   *   code 200 : Maintenance ()
   * 
   * Available security schemes:
   *   scaleway (apiKey)
   * 
   * @param region The region you want to target
   * @param maintenanceId (UUID format)
   * @param body 
   */
  def applyMaintenance(region: String, maintenanceId: String, body: io.circe.Json)(using Auth <:< scaleway.mongodb.Authorization.ApiKey): sttp.client4.Request[Either[ResponseException[String], Maintenance]] =
    val regionPathParam = PathSerializable.serialize("region", region, PathStyleFormat.SIMPLE, false)
    val maintenanceIdPathParam = PathSerializable.serialize("maintenance_id", maintenanceId, PathStyleFormat.SIMPLE, false)
    val requestURL =
      uri"$baseUrl/mongodb/v1/regions/${regionPathParam}/maintenances/${maintenanceIdPathParam}/apply"

    basicRequest
      .method(Method.POST, requestURL)
      .contentType("application/json")
      .auth(authConfig, scaleway.mongodb.ApiKeyLocation.HEADER, "X-Auth-Token")
      .body(asJson(body))
      .response(asJson[Maintenance])

  /**
   * Expected answers:
   *   code 200 : Maintenance ()
   * 
   * Available security schemes:
   *   scaleway (apiKey)
   * 
   * @param region The region you want to target
   * @param maintenanceId ID of the maintenance. (UUID format)
   */
  def getMaintenance(region: String, maintenanceId: String)(using Auth <:< scaleway.mongodb.Authorization.ApiKey): sttp.client4.Request[Either[ResponseException[String], Maintenance]] =
    val regionPathParam = PathSerializable.serialize("region", region, PathStyleFormat.SIMPLE, false)
    val maintenanceIdPathParam = PathSerializable.serialize("maintenance_id", maintenanceId, PathStyleFormat.SIMPLE, false)
    val requestURL =
      uri"$baseUrl/mongodb/v1/regions/${regionPathParam}/maintenances/${maintenanceIdPathParam}"

    basicRequest
      .method(Method.GET, requestURL)
      .contentType("application/json")
      .auth(authConfig, scaleway.mongodb.ApiKeyLocation.HEADER, "X-Auth-Token")
      .response(asJson[Maintenance])

  /**
   * Expected answers:
   *   code 200 : ListMaintenancesResponse ()
   * 
   * Available security schemes:
   *   scaleway (apiKey)
   * 
   * @param region The region you want to target
   * @param instanceId ID of the instance. (UUID format)
   * @param orderBy Criteria to use when requesting user listing.
   * @param page 
   * @param pageSize 
   */
  def listMaintenances(region: String, instanceId: String, orderBy: Option[String] = scala.None, page: Option[Int] = scala.None, pageSize: Option[Int] = scala.None)(using Auth <:< scaleway.mongodb.Authorization.ApiKey): sttp.client4.Request[Either[ResponseException[String], ListMaintenancesResponse]] =
    val regionPathParam = PathSerializable.serialize("region", region, PathStyleFormat.SIMPLE, false)
    val requestURL =
      uri"$baseUrl/mongodb/v1/regions/${regionPathParam}/maintenances"
        .addParams(FormSerializable.serialize("instance_id", instanceId, FormStyleFormat.FORM, true): _*)
        .addParams(FormSerializable.serialize("order_by", orderBy, FormStyleFormat.FORM, true): _*)
        .addParams(FormSerializable.serialize("page", page, FormStyleFormat.FORM, true): _*)
        .addParams(FormSerializable.serialize("page_size", pageSize, FormStyleFormat.FORM, true): _*)

    basicRequest
      .method(Method.GET, requestURL)
      .contentType("application/json")
      .auth(authConfig, scaleway.mongodb.ApiKeyLocation.HEADER, "X-Auth-Token")
      .response(asJson[ListMaintenancesResponse])

end MaintenanceOperations