/**
 * Serverless SQL Databases API
 * Scaleway's Serverless SQL DB is a fully-managed and flexible service offering SQL Databases. It provides elastic scaling and a pay-per-use model, ensuring you pay only for the queries you run and storage you consume.  Designed to free you from administrative and configuration tasks, it lets you focus solely on your data and application development. We handle high availability, regular backups, and configuration.  Your database only runs when in use, hosted in our energy-efficient datacenters in Europe, reducing your carbon footprint. Data is kept securely within our regions in Paris, Amsterdam, and Warsaw. Serverless SQL DB by Scaleway is a smart choice for efficient and sustainable database management.     ## Concepts  Refer to our [dedicated concepts page](https://www.scaleway.com/en/docs/serverless/sqldb/concepts/) to find definitions of the different terms referring to Serverless SQL DB.     ## Quickstart  1. Configure your environment variables.    <Message type=\"note\">    This is an optional step that seeks to simplify your usage of the APIs.    </Message>      ```bash     export SCW_ACCESS_KEY=\"<API access key>\"     export SCW_SECRET_KEY=\"<API secret key>\"     export SCW_REGION=\"<Scaleway region>\"     ``` 2. Edit the POST request payload you will use to create your Serverless SQL DB Database. Replace the parameters in the following example:     ```json         {           \"organization_id\": \"895693aa-3915-4896-8761-c2923b008be7\",           \"project_id\": \"d8e65f2b-cce9-40b7-80fc-6a2902db6826\",           \"name\": \"myDB\",           \"cpu_min\": 0,           \"cpu_max\": 5         }     ```     | Parameter         | Description                                                                                                                                                                                                                                                                                 |    |:------------------|:--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|    | `organization_id` | Your Organization ID. It must be in UUID format. To find your Organization ID, you can consult the **[Scaleway console](https://console.scaleway.com/organization/settings)**.                                                                                                              |    | `project_id`      | The ID of the Project you want to create your Serverless SQL DB Database in. To find your Project ID you can **[list the projects](/api/account#path-projects-list-all-projects-of-an-organization)** or consult the **[Scaleway console](https://console.scaleway.com/project/settings)**. |    | `name`            | Name of the Serverless SQL DB Database                                                                                                                                                                                                                                                      |    | `cpu_min`         | The minimum number of CPUs units your Serverless SQL DB Database can scale down to.                                                                                                                                                                                                         |    | `cpu_max`         | The maximum number of CPUs units your Serverless SQL DB Database can scale up to.                                                                                                                                                                                                           | 3. Create a Database by running the following command. Make sure you include the payload you edited in the previous step.     ```bash     curl -X POST \\       -H \"X-Auth-Token: $SCW_SECRET_KEY\" \\       \"Content-Type: application/json\" \\       https://api.scaleway.com/serverless-sqldb/v1alpha1/regions/$SCW_REGION/databases \\       -d '{           \"organization_id\": \"895693aa-3915-4896-8761-c2923b008be7\",           \"project_id\": \"d8e65f2b-cce9-40b7-80fc-6a2902db6826\",           \"name\": \"myDB\",           \"cpu_min\": 0,           \"cpu_max\": 5         }'     ``` 4. List your Databases.     ```bash     curl -X GET \\       -H \"Content-Type: application/json\" \\       -H \"X-Auth-Token: $SCW_SECRET_KEY\" https://api.scaleway.com/serverless-sqldb/v1alpha1/regions/$SCW_REGION/databases     ```     You should get a response like the following:      <Message type=\"note\">     This is a response example, the UUIDs and IP address displayed are not real.     </Message>      ```json     {           \"id\": \"f5122f66-fb50-4cef-aa02-487ef4fc1af0\",           \"name\": \"myDB\",           \"organization_id\": \"895693aa-3915-4896-8761-c2923b008be7\",           \"project_id\": \"d8e65f2b-cce9-40b7-80fc-6a2902db6826\",           \"status\": \"ready\",           \"endpoint\": \"postgres://f5122f66-fb50-4cef-aa02-487ef4fc1af0.pg.sdb.fr-par.scw.cloud/myDB\",           \"created_at\": \"2019-04-19T16:24:52.591417Z\",           \"region\": \"fr-par\"     }     ``` 5. Retrieve your Serverless SQL DB endpoint from the response.    <Message type=\"note\">    In the example above, the Endpoint is `postgres://f5122f66-fb50-4cef-aa02-487ef4fc1af0.pg.sdb.fr-par.scw.cloud/myDB`.    </Message> 6. Connect to your Database with the psql database client using the endpoint and one of your IAM principals:      ```bash     psql postgres://<iam-principal>:<iam-api-key>@f5122f66-fb50-4cef-aa02-487ef4fc1af0.pg.sdb.fr-par.scw.cloud/myDB     ```    <Message type=\"requirement\"> To perform the following steps, you must first ensure that: - you have an account and are logged into the [Scaleway console](https://console.scaleway.com/organization) - you have created an [API key](https://www.scaleway.com/en/docs/iam/how-to/create-api-keys/) and that the API key has sufficient [IAM permissions](https://www.scaleway.com/en/docs/iam/reference-content/permission-sets/) to perform the actions described on this page. - you have [installed `curl`](https://curl.se/download.html)   </Message>     ## Technical Information  ### Regions  Scaleway's infrastructure is spread across different [regions and Availability Zones](https://www.scaleway.com/en/docs/account/reference-content/products-availability/).  Serverless SQL DB is available in the Paris region, which is represented by the following path parameters:  - `fr-par`  ### PostgreSQL specifications  #### Versions  Scaleway Serverless SQL DB supports PostgreSQL version 14.   ## Technical Limitations  #### User Management  - users do NOT have `SUPERUSER` nor `REPLICATION` privileges.  [//]: # (TODO: add details to User Management limitations)    ## Going Further  For more information about Serverless SQL DB , you can check out the following pages:  * [Serverless SQL DB Documentation](https://www.scaleway.com/en/docs/serverless/sqldb/) * [Scaleway Slack Community](https://scaleway-community.slack.com/) join the #database channel * [Contact our support team](https://console.scaleway.com/support/tickets).
 *
 * The version of the OpenAPI document: v1alpha1
 * 
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */
package scaleway.serverlessdatabases.api

import scaleway.serverlessdatabases.models.DatabaseBackup
import scaleway.serverlessdatabases.models.ListDatabaseBackupsResponse
import com.github.plokhotnyuk.jsoniter_scala.circe.JsoniterScalaCodec.*
import scaleway.serverlessdatabases.JsonSupport.{*, given}
import scaleway.serverlessdatabases.FormSerializable
import scaleway.serverlessdatabases.FormStyleFormat
import scaleway.serverlessdatabases.HeaderSerializable
import scaleway.serverlessdatabases.ApiKeyLocation
import scaleway.serverlessdatabases.PathStyleFormat
import scaleway.serverlessdatabases.PathSerializable
import scaleway.serverlessdatabases.CookieSerializable
import scaleway.serverlessdatabases.Helpers.*
import sttp.client4.jsoniter.*
import sttp.client4.*
import sttp.model.Method

object ServerlessSQLDatabasesBackupsApi:
  def apply(baseUrl: String = "https://api.scaleway.com"): ServerlessSQLDatabasesBackupsApi[scaleway.serverlessdatabases.Authorization.NoAuthorization.type] = ServerlessSQLDatabasesBackupsApi(baseUrl, scaleway.serverlessdatabases.Authorization.NoAuthorization)
  def withBasicAuth(baseUrl: String, username: String, password: String): ServerlessSQLDatabasesBackupsApi[scaleway.serverlessdatabases.Authorization.BasicAuth] =
    ServerlessSQLDatabasesBackupsApi(baseUrl, scaleway.serverlessdatabases.Authorization.BasicAuth(username, password))

  def withApiKeyAuth(baseUrl: String, apiKey: String): ServerlessSQLDatabasesBackupsApi[scaleway.serverlessdatabases.Authorization.ApiKey] =
    ServerlessSQLDatabasesBackupsApi(baseUrl, scaleway.serverlessdatabases.Authorization.ApiKey(apiKey))

  def withBearerTokenAuth(baseUrl: String, token: String): ServerlessSQLDatabasesBackupsApi[scaleway.serverlessdatabases.Authorization.BearerToken] =
    ServerlessSQLDatabasesBackupsApi(baseUrl, scaleway.serverlessdatabases.Authorization.BearerToken(token))

case class ServerlessSQLDatabasesBackupsApi[Auth <: scaleway.serverlessdatabases.Authorization] private (baseUrl: String, authConfig: scaleway.serverlessdatabases.Authorization):
  def withBasicAuth(username: String, password: String): ServerlessSQLDatabasesBackupsApi[scaleway.serverlessdatabases.Authorization.BasicAuth] =
    copy(authConfig = scaleway.serverlessdatabases.Authorization.BasicAuth(username, password))

  def withApiKeyAuth(apiKey: String): ServerlessSQLDatabasesBackupsApi[scaleway.serverlessdatabases.Authorization.ApiKey] =
    copy(authConfig = scaleway.serverlessdatabases.Authorization.ApiKey(apiKey))

  def withNoAuth: ServerlessSQLDatabasesBackupsApi[scaleway.serverlessdatabases.Authorization.NoAuthorization.type] =
    copy(authConfig = scaleway.serverlessdatabases.Authorization.NoAuthorization)

  def withBearerTokenAuth(token: String): ServerlessSQLDatabasesBackupsApi[scaleway.serverlessdatabases.Authorization.BearerToken] =
    copy(authConfig = scaleway.serverlessdatabases.Authorization.BearerToken(token))

  /**
   * Export a database backup providing a download link once the export process is completed. You must provide the `backup_id` parameter.
   * 
   * Expected answers:
   *   code 200 : DatabaseBackup ()
   * 
   * Available security schemes:
   *   scaleway (apiKey)
   * 
   * @param region The region you want to target
   * @param backupId UUID of the Serverless SQL Database backup.
   * @param body 
   */
  def exportDatabaseBackup(region: String, backupId: String, body: io.circe.Json)(using Auth <:< scaleway.serverlessdatabases.Authorization.ApiKey): sttp.client4.Request[Either[ResponseException[String], DatabaseBackup]] =
    val regionPathParam = PathSerializable.serialize("region", region, PathStyleFormat.SIMPLE, false)
    val backupIdPathParam = PathSerializable.serialize("backup_id", backupId, PathStyleFormat.SIMPLE, false)
    val requestURL =
      uri"$baseUrl/serverless-sqldb/v1alpha1/regions/${regionPathParam}/backups/${backupIdPathParam}/export"

    basicRequest
      .method(Method.POST, requestURL)
      .contentType("application/json")
      .auth(authConfig, scaleway.serverlessdatabases.ApiKeyLocation.HEADER, "X-Auth-Token")
      .body(asJson(body))
      .response(asJson[DatabaseBackup])

  /**
   * Retrieve information about your Serverless SQL Database backup. You must provide the `backup_id` parameter.
   * 
   * Expected answers:
   *   code 200 : DatabaseBackup ()
   * 
   * Available security schemes:
   *   scaleway (apiKey)
   * 
   * @param region The region you want to target
   * @param backupId UUID of the Serverless SQL Database backup.
   */
  def getDatabaseBackup(region: String, backupId: String)(using Auth <:< scaleway.serverlessdatabases.Authorization.ApiKey): sttp.client4.Request[Either[ResponseException[String], DatabaseBackup]] =
    val regionPathParam = PathSerializable.serialize("region", region, PathStyleFormat.SIMPLE, false)
    val backupIdPathParam = PathSerializable.serialize("backup_id", backupId, PathStyleFormat.SIMPLE, false)
    val requestURL =
      uri"$baseUrl/serverless-sqldb/v1alpha1/regions/${regionPathParam}/backups/${backupIdPathParam}"

    basicRequest
      .method(Method.GET, requestURL)
      .contentType("application/json")
      .auth(authConfig, scaleway.serverlessdatabases.ApiKeyLocation.HEADER, "X-Auth-Token")
      .response(asJson[DatabaseBackup])

  /**
   * List all Serverless SQL Database backups for a given Scaleway Project or Database. By default, the backups returned in the list are ordered by creation date in descending order, though this can be modified via the order_by field.
   * 
   * Expected answers:
   *   code 200 : ListDatabaseBackupsResponse ()
   * 
   * Available security schemes:
   *   scaleway (apiKey)
   * 
   * @param region The region you want to target
   * @param databaseId Filter by the UUID of the Serverless SQL Database.
   * @param organizationId Filter by the UUID of the Scaleway organization. (UUID format)
   * @param projectId Filter by the UUID of the Scaleway project. (UUID format)
   * @param page Page number.
   * @param pageSize Page size.
   * @param orderBy Sorting criteria. One of `created_at_asc`, `created_at_desc`.
   */
  def listDatabaseBackups(region: String, databaseId: String, organizationId: Option[String] = scala.None, projectId: Option[String] = scala.None, page: Option[Int] = scala.None, pageSize: Option[Int] = scala.None, orderBy: Option[String] = scala.None)(using Auth <:< scaleway.serverlessdatabases.Authorization.ApiKey): sttp.client4.Request[Either[ResponseException[String], ListDatabaseBackupsResponse]] =
    val regionPathParam = PathSerializable.serialize("region", region, PathStyleFormat.SIMPLE, false)
    val requestURL =
      uri"$baseUrl/serverless-sqldb/v1alpha1/regions/${regionPathParam}/backups"
        .addParams(FormSerializable.serialize("organization_id", organizationId, FormStyleFormat.FORM, true): _*)
        .addParams(FormSerializable.serialize("project_id", projectId, FormStyleFormat.FORM, true): _*)
        .addParams(FormSerializable.serialize("database_id", databaseId, FormStyleFormat.FORM, true): _*)
        .addParams(FormSerializable.serialize("page", page, FormStyleFormat.FORM, true): _*)
        .addParams(FormSerializable.serialize("page_size", pageSize, FormStyleFormat.FORM, true): _*)
        .addParams(FormSerializable.serialize("order_by", orderBy, FormStyleFormat.FORM, true): _*)

    basicRequest
      .method(Method.GET, requestURL)
      .contentType("application/json")
      .auth(authConfig, scaleway.serverlessdatabases.ApiKeyLocation.HEADER, "X-Auth-Token")
      .response(asJson[ListDatabaseBackupsResponse])

end ServerlessSQLDatabasesBackupsApi