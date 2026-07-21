/**
 * Managed Database for PostgreSQL and MySQL API
 * Managed Database for PostgreSQL and MySQL provides fully-managed relational Database Instances, with MySQL or PostgreSQL as database engines. The resource allows you to focus on development rather than administration or configuration. It comes with a high-availability mode, data replication, and automatic backups.  Compared to traditional database management, which requires customers to provide their infrastructure and resources to manage their databases, Managed Database for PostgreSQL and MySQL Instance offers the user access to Database Instances without setting up the hardware or configuring the software. Scaleway handles the provisioning, manages the configuration, and provides useful features as high availability, automated backup, user management, and more.     ## Concepts  Refer to our [dedicated concepts page](https://www.scaleway.com/en/docs/managed-databases-for-postgresql-and-mysql/concepts/) to find definitions of the different terms referring to Managed Database for PostgreSQL and MySQL.     ## Quickstart  1. Configure your environment variables.     <Message type=\"note\">     This is an optional step that seeks to simplify your usage of the APIs.     </Message>      ```bash     export SCW_ACCESS_KEY=\"<API access key>\"     export SCW_SECRET_KEY=\"<API secret key>\"     export SCW_REGION=\"<Scaleway region>\"     ``` 2. Edit the POST request payload you will use to create your Database Instance. Replace the parameters in the following example:     ```json       '{       \"project_id\": \"d8e65f2b-cce9-40b7-80fc-6a2902db6826\",       \"name\": \"myDB\",       \"engine\": \"PostgreSQL-15\",       \"tags\": [\"donnerstag\"],       \"is_ha_cluster\": true,       \"node_type\": \"db-pro2-xxs\",       \"disable_backup\": false,       \"user_name\": \"my_initial_user\",       \"password\": \"thiZ_is_v0ry_s3cret\",       \"volume_type\": \"sbs_5k\",       \"volume_size\": \"30000000000\"       }'     ```      | Parameter        | Description                                                                                                                                                                                                                                                                                                                                                                          |     | :--------------- |:-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|     | `project_id`     | The ID of the Project you want to create your Database Instance in. To find your Project ID you can **[list the projects](/api/account/project-api/#path-projects-list-all-projects-of-an-organization)** or consult the **[Scaleway console](https://console.scaleway.com/project/settings)**.                                                                                                   |     | `engine`         | **REQUIRED** Version ID of the database engine. To check the list of available engines you can use the following endpoint: `https://api.scaleway.com/rdb/v1/regions/$SCW_REGION/database-engines`                                                                                                                                                                                     |     | `name`           | Name of the Database Instance                                                                                                                                                                                                                                                                                                                                                        |     | `node_type`      | **REQUIRED** The node type. To check the list of available node types you can use the following endpoint: `https://api.scaleway.com/rdb/v1/regions/$SCW_REGION/node-types`                                                                                                                                                                                                           |     | `is_ha_cluster`  | **BOOLEAN** Defines whether High Availability is enabled for the Database Instance                                                                                                                                                                                                                                                                                                   |     | `disable_backup` | **BOOLEAN** Defines whether automated backups are disabled for the Database Instance                                                                                                                                                                                                                                                                                                 |     | `tags`           | The list of tags `[\"tag1\", \"tag2\", ...]` that will be associated with the Database Instance. Tags can be appended to the query of the [List Database Instances](#path-database-instances-list-database-instances) call to show results for only the Database Instances using a specific tag. You can also combine tags to list Database Instances that possess all the appended tags. |     | `user_name`      | **REQUIRED** Identifier of the default user, which is created concurrently with the Database Instance                                                                                                                                                                                                                                                                                |     | `password`       | **REQUIRED** Password for the default user                                                                                                                                                                                                                                                                                                                                           |     | `volume_type`    | Type of volume where data is stored. You can specify either local volume (`lssd`) or block volume (`bssd`, `sbs_5k` or `sbs_15k`). The default value is `lssd`                                                                                                                                                                                                                       |     | `volume_size`    | Volume size when volume_type is `bssd`, `sbs_5k` or `sbs_15k`. The value should be expressed in bytes. For example 30GB is expressed as 30000000000                                                                                                                                                                                                                                  | 3. Create a Database Instance by running the following command. Make sure you include the payload you edited in the previous step.     ```bash     curl -X POST \\       -H \"X-Auth-Token: $SCW_SECRET_KEY\" \\       \"Content-Type: application/json\" \\       https://api.scaleway.com/rdb/v1/regions/$SCW_REGION/instances \\       -d '{         \"project_id\": \"d8e65f2b-cce9-40b7-80fc-6a2902db6826\",         \"name\": \"myDB\",         \"engine\": \"PostgreSQL-15\",         \"tags\": [\"donnerstag\"],         \"is_ha_cluster\": true,         \"node_type\": \"db-pro2-xxs\",         \"disable_backup\": false,         \"user_name\": \"my_initial_user\",         \"password\": \"thiZ_is_v0ry_s3cret\",         \"volume_type\": \"sbs_5k\",         \"volume_size\": \"30000000000\"       }'     ``` 4. List your Database Instances.     ```bash     curl -X GET \\       -H \"Content-Type: application/json\" \\       -H \"X-Auth-Token: $SCW_SECRET_KEY\" https://api.scaleway.com/rdb/v1/regions/$SCW_REGION/instances     ```      You should get a response like the following:      <Message type=\"note\">     This is a response example, the UUIDs and IP address displayed are not real.     </Message>      ```json     {           \"id\": \"f5122f66-fb50-4cef-aa02-487ef4fc1af0\",           \"name\": \"myDB\",           \"organization_id\": \"895693aa-3915-4896-8761-c2923b008be7\",           \"project_id\": \"d8e65f2b-cce9-40b7-80fc-6a2902db6826\",           \"status\": \"ready\",           \"engine\": \"PostgreSQL-15\",           \"endpoint\": {             \"ip\": \"198.51.100.0\",             \"port\": 22245,             \"name\": null           },           \"tags\": [             \"donnerstag\"           ],           \"settings\": [],           \"backup_schedule\": {             \"frequency\": 24,             \"retention\": 7,             \"disabled\": true           },           \"is_ha_cluster\": true,           \"read_replicas\": [],           \"node_type\": \"db-pro2-xxs\",           \"volume\": {             \"type\": \"sbs_5k\",             \"size\": 30000000000           }           \"created_at\": \"2019-04-19T16:24:52.591417Z\",           \"region\": \"fr-par\"     }     ``` 5. Retrieve your Database Instance IP and port from the response.     <Message type=\"note\">     In the example above, the IP and port are `198.51.100.0` and `22245`, respectively.     </Message> 6. Connect to your Database Instance with the database client of the engine you selected.     For MySQL, run the following command:     ```bash     mysql -h <ip-address> --port <port> -p -u <user_name>     ```      For PostgreSQL, run:     ```bash     psql -h <ip-address> -p <port> -U <username> -d rdb     ```      For the recurring example, the command would look like:      ```bash     psql -h 198.51.100.0 -p 22245 -U my_initial_user -d rdb     ``` 7. Enter the database password that you defined upon creation.  You are now connected to your Managed Database.   <Message type=\"requirement\"> To perform the following steps, you must first ensure that:   - you have an account and are logged into the [Scaleway console](https://console.scaleway.com/organization)   - you have created an [API key](https://www.scaleway.com/en/docs/iam/how-to/create-api-keys/) and that the API key has sufficient [IAM permissions](https://www.scaleway.com/en/docs/iam/reference-content/permission-sets/) to perform the actions described on this page.   - you have [installed `curl`](https://curl.se/download.html) </Message>   ## Technical Information  ### Regions  Scaleway's infrastructure is spread across different [regions and Availability Zones](https://www.scaleway.com/en/docs/account/reference-content/products-availability/).  Managed Database for PostgreSQL and MySQL is available in the Paris, Amsterdam and Warsaw regions, which are represented by the following path parameters:  - `fr-par` - `nl-ams` - `pl-waw`  ### PostgreSQL specifications  #### Versions  Scaleway Database for PostgreSQL supports PostgreSQL versions 11, 12, 13, 14 and 15.  #### System  Different modules are available for installation, including TimescaleDB and PostGIS. Refer to the [Managed Database for PostgreSQL and MySQL FAQ page](https://www.scaleway.com/en/docs/managed-databases-for-postgresql-and-mysql/faq/#which-postgresql-extensions-are-available) for an extensive list of PostgreSQL extensions.  #### Database Management  You can create logical databases through the Scaleway console, the Scaleway APIs or SQL.  - databases created using the Scaleway console or the API are owned by an internal system user. These are called \"managed databases\". - databases created using SQL will be owned by the creator. These are called \"unmanaged databases\".  ### MySQL specifications  #### Versions  Scaleway Database for MySQL supports MySQL 8.  #### System  - only the [InnoDB engine](https://dev.mysql.com/doc/refman/8.0/en/innodb-storage-engine.html) is supported - the [Global Transaction Identifier (GTID)](https://dev.mysql.com/doc/refman/8.0/en/replication-gtids-concepts.html) is enabled. - [`mysql_native_password`](https://dev.mysql.com/doc/refman/8.0/en/native-pluggable-authentication.html) (default) and [`caching_sha2_password`](https://dev.mysql.com/doc/refman/8.0/en/caching-sha2-pluggable-authentication.html) authentication are supported.  #### User Management  - users with an `admin` role have access to all logical databases and can create new ones. - users created via the API are authenticated using the default authentication plugin, which can be changed in the settings.  ## Technical Limitations  ### PostgreSQL  #### User Management  - users with an `admin` role have `CREATEROLE` and `CREATEDB` privileges. - users do NOT have `SUPERUSER` nor `REPLICATION` privileges. - permission management through the Scaleway console or API is only possible for the \"managed databases\".  #### Backup and restoration  Databases that have been backed up and then restored retain the user permission settings in use at the time of backup. If you delete users after backup and then restore your backup in the same database, or if you restore a backup to a different database with different or no users, the permissions configured for them continue to exist, but with no associated owner. This error will put a stop to the restoration process.  To avoid this issue, we recommend you re-create the users you deleted. In the occasion you restore the backup to a new database, you must create new users with the same names.  ## Going Further  For more information about Managed Database for PostgreSQL and MySQL, you can check out the following pages:  * [Managed Database for PostgreSQL and MySQL Documentation](https://www.scaleway.com/en/docs/managed-databases/postgresql-and-mysql/) * [Managed Database for PostgreSQL and MySQL FAQ](https://www.scaleway.com/en/docs/managed-databases-for-postgresql-and-mysql/faq/) * [Scaleway Slack Community](https://scaleway-community.slack.com/) join the #database channel * [Contact our support team](https://console.scaleway.com/support/tickets)  ### How to migrate a database  If you wish to migrate existing databases to a Managed Database for PostgreSQL or MySQL, you can refer to the [Migrating existing databases to a Database Instance](https://www.scaleway.com/en/docs/tutorials/migrate-databases-instance/) tutorial page.  ### Troubleshoooting  #### Disk full status  If your Database Instance uses local storage, your local volume might eventually approach full capacity and shift to `disk_full` mode. This mode grants you enough space to either [upgrade your node type](https://www.scaleway.com/en/docs/managed-databases-for-postgresql-and-mysql/how-to/upgrade-version/#how-to-change-the-node-type) or [clear out space in your volume](https://www.scaleway.com/en/docs/managed-databases-for-postgresql-and-mysql/troubleshooting/disk-full/).
 *
 * The version of the OpenAPI document: v1
 * 
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */
package scaleway.postgremysql.api

import scaleway.postgremysql.models.CloneInstanceRequest
import scaleway.postgremysql.models.CreateInstanceRequest
import java.io.File
import scaleway.postgremysql.models.Instance
import scaleway.postgremysql.models.InstanceLog
import scaleway.postgremysql.models.InstanceMetrics
import scaleway.postgremysql.models.ListInstanceLogsDetailsResponse
import scaleway.postgremysql.models.ListInstanceLogsResponse
import scaleway.postgremysql.models.ListInstancesResponse
import scaleway.postgremysql.models.Maintenance
import java.time.OffsetDateTime
import scaleway.postgremysql.models.PrepareInstanceLogsRequest
import scaleway.postgremysql.models.PrepareInstanceLogsResponse
import scaleway.postgremysql.models.PurgeInstanceLogsRequest
import scaleway.postgremysql.models.UpdateInstanceRequest
import scaleway.postgremysql.models.UpgradeInstanceRequest
import com.github.plokhotnyuk.jsoniter_scala.circe.JsoniterScalaCodec.*
import scaleway.postgremysql.JsonSupport.{*, given}
import scaleway.postgremysql.FormSerializable
import scaleway.postgremysql.FormStyleFormat
import scaleway.postgremysql.HeaderSerializable
import scaleway.postgremysql.ApiKeyLocation
import scaleway.postgremysql.PathStyleFormat
import scaleway.postgremysql.PathSerializable
import scaleway.postgremysql.CookieSerializable
import scaleway.postgremysql.Helpers.*
import sttp.client4.jsoniter.*
import sttp.client4.*
import sttp.model.Method

object DatabaseInstancesApi:
  def apply(baseUrl: String = "https://api.scaleway.com"): DatabaseInstancesApi[scaleway.postgremysql.Authorization.NoAuthorization.type] = DatabaseInstancesApi(baseUrl, scaleway.postgremysql.Authorization.NoAuthorization)
  def withBasicAuth(baseUrl: String, username: String, password: String): DatabaseInstancesApi[scaleway.postgremysql.Authorization.BasicAuth] =
    DatabaseInstancesApi(baseUrl, scaleway.postgremysql.Authorization.BasicAuth(username, password))

  def withApiKeyAuth(baseUrl: String, apiKey: String): DatabaseInstancesApi[scaleway.postgremysql.Authorization.ApiKey] =
    DatabaseInstancesApi(baseUrl, scaleway.postgremysql.Authorization.ApiKey(apiKey))

  def withBearerTokenAuth(baseUrl: String, token: String): DatabaseInstancesApi[scaleway.postgremysql.Authorization.BearerToken] =
    DatabaseInstancesApi(baseUrl, scaleway.postgremysql.Authorization.BearerToken(token))

case class DatabaseInstancesApi[Auth <: scaleway.postgremysql.Authorization] private (baseUrl: String, authConfig: scaleway.postgremysql.Authorization):
  def withBasicAuth(username: String, password: String): DatabaseInstancesApi[scaleway.postgremysql.Authorization.BasicAuth] =
    copy(authConfig = scaleway.postgremysql.Authorization.BasicAuth(username, password))

  def withApiKeyAuth(apiKey: String): DatabaseInstancesApi[scaleway.postgremysql.Authorization.ApiKey] =
    copy(authConfig = scaleway.postgremysql.Authorization.ApiKey(apiKey))

  def withNoAuth: DatabaseInstancesApi[scaleway.postgremysql.Authorization.NoAuthorization.type] =
    copy(authConfig = scaleway.postgremysql.Authorization.NoAuthorization)

  def withBearerTokenAuth(token: String): DatabaseInstancesApi[scaleway.postgremysql.Authorization.BearerToken] =
    copy(authConfig = scaleway.postgremysql.Authorization.BearerToken(token))

  /**
   * Apply maintenance tasks to your Database Instance. This will trigger pending maintenance tasks to start in your Database Instance and can generate service interruption. Maintenance tasks can be applied between `starts_at` and `stops_at` times, and are run directly by Scaleway at `forced_at` timestamp.
   * 
   * Expected answers:
   *   code 200 : Maintenance ()
   * 
   * Available security schemes:
   *   scaleway (apiKey)
   * 
   * @param region The region you want to target
   * @param instanceId UUID of the Database Instance you want to apply maintenance.
   * @param body 
   */
  def applyInstanceMaintenance(region: String, instanceId: String, body: io.circe.Json)(using Auth <:< scaleway.postgremysql.Authorization.ApiKey): sttp.client4.Request[Either[ResponseException[String], Maintenance]] =
    val regionPathParam = PathSerializable.serialize("region", region, PathStyleFormat.SIMPLE, false)
    val instanceIdPathParam = PathSerializable.serialize("instance_id", instanceId, PathStyleFormat.SIMPLE, false)
    val requestURL =
      uri"$baseUrl/rdb/v1/regions/${regionPathParam}/instances/${instanceIdPathParam}/apply-maintenance"

    basicRequest
      .method(Method.POST, requestURL)
      .contentType("application/json")
      .auth(authConfig, scaleway.postgremysql.ApiKeyLocation.HEADER, "X-Auth-Token")
      .body(asJson(body))
      .response(asJson[Maintenance])

  /**
   * Clone a given Database Instance, specified by the `region` and `instance_id` parameters. The clone feature allows you to create a new Database Instance from an existing one. The clone includes all existing databases, users and permissions. You can create a clone on a Database Instance bigger than your current one.
   * 
   * Expected answers:
   *   code 200 : Instance ()
   * 
   * Available security schemes:
   *   scaleway (apiKey)
   * 
   * @param region The region you want to target
   * @param instanceId UUID of the Database Instance you want to clone.
   * @param cloneInstanceRequest 
   */
  def cloneInstance(region: String, instanceId: String, cloneInstanceRequest: CloneInstanceRequest)(using Auth <:< scaleway.postgremysql.Authorization.ApiKey): sttp.client4.Request[Either[ResponseException[String], Instance]] =
    val regionPathParam = PathSerializable.serialize("region", region, PathStyleFormat.SIMPLE, false)
    val instanceIdPathParam = PathSerializable.serialize("instance_id", instanceId, PathStyleFormat.SIMPLE, false)
    val requestURL =
      uri"$baseUrl/rdb/v1/regions/${regionPathParam}/instances/${instanceIdPathParam}/clone"

    basicRequest
      .method(Method.POST, requestURL)
      .contentType("application/json")
      .auth(authConfig, scaleway.postgremysql.ApiKeyLocation.HEADER, "X-Auth-Token")
      .body(asJson(cloneInstanceRequest))
      .response(asJson[Instance])

  /**
   * Create a new Database Instance. You must set the `engine`, `user_name`, `password` and `node_type` parameters. Optionally, you can specify the volume type and size.
   * 
   * Expected answers:
   *   code 200 : Instance ()
   * 
   * Available security schemes:
   *   scaleway (apiKey)
   * 
   * @param region The region you want to target
   * @param createInstanceRequest 
   */
  def createInstance(region: String, createInstanceRequest: CreateInstanceRequest)(using Auth <:< scaleway.postgremysql.Authorization.ApiKey): sttp.client4.Request[Either[ResponseException[String], Instance]] =
    val regionPathParam = PathSerializable.serialize("region", region, PathStyleFormat.SIMPLE, false)
    val requestURL =
      uri"$baseUrl/rdb/v1/regions/${regionPathParam}/instances"

    basicRequest
      .method(Method.POST, requestURL)
      .contentType("application/json")
      .auth(authConfig, scaleway.postgremysql.ApiKeyLocation.HEADER, "X-Auth-Token")
      .body(asJson(createInstanceRequest))
      .response(asJson[Instance])

  /**
   * Delete a given Database Instance, specified by the `region` and `instance_id` parameters. Deleting a Database Instance is permanent, and cannot be undone. Note that upon deletion all your data will be lost.
   * 
   * Expected answers:
   *   code 200 : Instance ()
   * 
   * Available security schemes:
   *   scaleway (apiKey)
   * 
   * @param region The region you want to target
   * @param instanceId UUID of the Database Instance to delete.
   */
  def deleteInstance(region: String, instanceId: String)(using Auth <:< scaleway.postgremysql.Authorization.ApiKey): sttp.client4.Request[Either[ResponseException[String], Instance]] =
    val regionPathParam = PathSerializable.serialize("region", region, PathStyleFormat.SIMPLE, false)
    val instanceIdPathParam = PathSerializable.serialize("instance_id", instanceId, PathStyleFormat.SIMPLE, false)
    val requestURL =
      uri"$baseUrl/rdb/v1/regions/${regionPathParam}/instances/${instanceIdPathParam}"

    basicRequest
      .method(Method.DELETE, requestURL)
      .contentType("application/json")
      .auth(authConfig, scaleway.postgremysql.ApiKeyLocation.HEADER, "X-Auth-Token")
      .response(asJson[Instance])

  /**
   * Retrieve information about a given Database Instance, specified by the `region` and `instance_id` parameters. Its full details, including name, status, IP address and port, are returned in the response object.
   * 
   * Expected answers:
   *   code 200 : Instance ()
   * 
   * Available security schemes:
   *   scaleway (apiKey)
   * 
   * @param region The region you want to target
   * @param instanceId UUID of the Database Instance.
   */
  def getInstance(region: String, instanceId: String)(using Auth <:< scaleway.postgremysql.Authorization.ApiKey): sttp.client4.Request[Either[ResponseException[String], Instance]] =
    val regionPathParam = PathSerializable.serialize("region", region, PathStyleFormat.SIMPLE, false)
    val instanceIdPathParam = PathSerializable.serialize("instance_id", instanceId, PathStyleFormat.SIMPLE, false)
    val requestURL =
      uri"$baseUrl/rdb/v1/regions/${regionPathParam}/instances/${instanceIdPathParam}"

    basicRequest
      .method(Method.GET, requestURL)
      .contentType("application/json")
      .auth(authConfig, scaleway.postgremysql.ApiKeyLocation.HEADER, "X-Auth-Token")
      .response(asJson[Instance])

  /**
   * Retrieve information about the TLS certificate of a given Database Instance. Details like name and content are returned in the response.
   * 
   * Expected answers:
   *   code 200 : File ()
   * 
   * Available security schemes:
   *   scaleway (apiKey)
   * 
   * @param region The region you want to target
   * @param instanceId UUID of the Database Instance.
   */
  def getInstanceCertificate(region: String, instanceId: String)(using Auth <:< scaleway.postgremysql.Authorization.ApiKey): sttp.client4.Request[Either[ResponseException[String], File]] =
    val regionPathParam = PathSerializable.serialize("region", region, PathStyleFormat.SIMPLE, false)
    val instanceIdPathParam = PathSerializable.serialize("instance_id", instanceId, PathStyleFormat.SIMPLE, false)
    val requestURL =
      uri"$baseUrl/rdb/v1/regions/${regionPathParam}/instances/${instanceIdPathParam}/certificate"

    basicRequest
      .method(Method.GET, requestURL)
      .contentType("application/json")
      .auth(authConfig, scaleway.postgremysql.ApiKeyLocation.HEADER, "X-Auth-Token")
      .response(asFile(File.createTempFile("download", ".tmp")).mapWithMetadata((result, metadata) => result.left.map(errStr => ResponseException.DeserializationException(errStr, new Exception(errStr), metadata))))

  /**
   * Retrieve information about the logs of a Database Instance. Specify the `instance_log_id` and `region` in your request to get information such as `download_url`, `status`, `expires_at` and `created_at` about your logs in the response.
   * 
   * Expected answers:
   *   code 200 : InstanceLog ()
   * 
   * Available security schemes:
   *   scaleway (apiKey)
   * 
   * @param region The region you want to target
   * @param instanceLogId UUID of the instance_log you want.
   */
  def getInstanceLog(region: String, instanceLogId: String)(using Auth <:< scaleway.postgremysql.Authorization.ApiKey): sttp.client4.Request[Either[ResponseException[String], InstanceLog]] =
    val regionPathParam = PathSerializable.serialize("region", region, PathStyleFormat.SIMPLE, false)
    val instanceLogIdPathParam = PathSerializable.serialize("instance_log_id", instanceLogId, PathStyleFormat.SIMPLE, false)
    val requestURL =
      uri"$baseUrl/rdb/v1/regions/${regionPathParam}/logs/${instanceLogIdPathParam}"

    basicRequest
      .method(Method.GET, requestURL)
      .contentType("application/json")
      .auth(authConfig, scaleway.postgremysql.ApiKeyLocation.HEADER, "X-Auth-Token")
      .response(asJson[InstanceLog])

  /**
   * Retrieve the time series metrics of a given Database Instance. You can define the period from which to retrieve metrics by specifying the `start_date` and `end_date`. This method is deprecated and will be removed in a future version.
   * 
   * Expected answers:
   *   code 200 : InstanceMetrics ()
   * 
   * Available security schemes:
   *   scaleway (apiKey)
   * 
   * @param region The region you want to target
   * @param instanceId UUID of the Database Instance.
   * @param startDate Start date to gather metrics from. (RFC 3339 format)
   * @param endDate End date to gather metrics from. (RFC 3339 format)
   * @param metricName Name of the metric to gather.
   */
  def getInstanceMetrics(region: String, instanceId: String, startDate: Option[OffsetDateTime] = scala.None, endDate: Option[OffsetDateTime] = scala.None, metricName: Option[String] = scala.None)(using Auth <:< scaleway.postgremysql.Authorization.ApiKey): sttp.client4.Request[Either[ResponseException[String], InstanceMetrics]] =
    val regionPathParam = PathSerializable.serialize("region", region, PathStyleFormat.SIMPLE, false)
    val instanceIdPathParam = PathSerializable.serialize("instance_id", instanceId, PathStyleFormat.SIMPLE, false)
    val requestURL =
      uri"$baseUrl/rdb/v1/regions/${regionPathParam}/instances/${instanceIdPathParam}/metrics"
        .addParams(FormSerializable.serialize("start_date", startDate, FormStyleFormat.FORM, true): _*)
        .addParams(FormSerializable.serialize("end_date", endDate, FormStyleFormat.FORM, true): _*)
        .addParams(FormSerializable.serialize("metric_name", metricName, FormStyleFormat.FORM, true): _*)

    basicRequest
      .method(Method.GET, requestURL)
      .contentType("application/json")
      .auth(authConfig, scaleway.postgremysql.ApiKeyLocation.HEADER, "X-Auth-Token")
      .response(asJson[InstanceMetrics])

  /**
   * List the available logs of a Database Instance. By default, the logs returned in the list are ordered by creation date in ascending order, though this can be modified via the order_by field.
   * 
   * Expected answers:
   *   code 200 : ListInstanceLogsResponse ()
   * 
   * Available security schemes:
   *   scaleway (apiKey)
   * 
   * @param region The region you want to target
   * @param instanceId UUID of the Database Instance you want logs of.
   * @param orderBy Criteria to use when ordering Database Instance logs listing.
   */
  def listInstanceLogs(region: String, instanceId: String, orderBy: Option[String] = scala.None)(using Auth <:< scaleway.postgremysql.Authorization.ApiKey): sttp.client4.Request[Either[ResponseException[String], ListInstanceLogsResponse]] =
    val regionPathParam = PathSerializable.serialize("region", region, PathStyleFormat.SIMPLE, false)
    val instanceIdPathParam = PathSerializable.serialize("instance_id", instanceId, PathStyleFormat.SIMPLE, false)
    val requestURL =
      uri"$baseUrl/rdb/v1/regions/${regionPathParam}/instances/${instanceIdPathParam}/logs"
        .addParams(FormSerializable.serialize("order_by", orderBy, FormStyleFormat.FORM, true): _*)

    basicRequest
      .method(Method.GET, requestURL)
      .contentType("application/json")
      .auth(authConfig, scaleway.postgremysql.ApiKeyLocation.HEADER, "X-Auth-Token")
      .response(asJson[ListInstanceLogsResponse])

  /**
   * List remote log details. By default, the details returned in the list are ordered by creation date in ascending order, though this can be modified via the order_by field.
   * 
   * Expected answers:
   *   code 200 : ListInstanceLogsDetailsResponse ()
   * 
   * Available security schemes:
   *   scaleway (apiKey)
   * 
   * @param region The region you want to target
   * @param instanceId UUID of the Database Instance you want logs of.
   */
  def listInstanceLogsDetails(region: String, instanceId: String)(using Auth <:< scaleway.postgremysql.Authorization.ApiKey): sttp.client4.Request[Either[ResponseException[String], ListInstanceLogsDetailsResponse]] =
    val regionPathParam = PathSerializable.serialize("region", region, PathStyleFormat.SIMPLE, false)
    val instanceIdPathParam = PathSerializable.serialize("instance_id", instanceId, PathStyleFormat.SIMPLE, false)
    val requestURL =
      uri"$baseUrl/rdb/v1/regions/${regionPathParam}/instances/${instanceIdPathParam}/logs-details"

    basicRequest
      .method(Method.GET, requestURL)
      .contentType("application/json")
      .auth(authConfig, scaleway.postgremysql.ApiKeyLocation.HEADER, "X-Auth-Token")
      .response(asJson[ListInstanceLogsDetailsResponse])

  /**
   * List all Database Instances in the specified region, for a given Scaleway Organization or Scaleway Project. By default, the Database Instances returned in the list are ordered by creation date in ascending order, though this can be modified via the order_by field. You can define additional parameters for your query, such as `tags` and `name`. For the `name` parameter, the value you include will be checked against the whole name string to see if it includes the string you put in the parameter.
   * 
   * Expected answers:
   *   code 200 : ListInstancesResponse ()
   * 
   * Available security schemes:
   *   scaleway (apiKey)
   * 
   * @param region The region you want to target
   * @param tags List Database Instances that have a given tag.
   * @param name Lists Database Instances that match a name pattern.
   * @param orderBy Criteria to use when ordering Database Instance listings.
   * @param organizationId Please use project_id instead.
   * @param projectId Project ID to list the Database Instance of.
   * @param hasMaintenances Filter to only list instances with a scheduled maintenance.
   * @param page 
   * @param pageSize 
   */
  def listInstances(region: String, tags: Seq[String], name: Option[String] = scala.None, orderBy: Option[String] = scala.None, organizationId: Option[String] = scala.None, projectId: Option[String] = scala.None, hasMaintenances: Option[Boolean] = scala.None, page: Option[Int] = scala.None, pageSize: Option[Int] = scala.None)(using Auth <:< scaleway.postgremysql.Authorization.ApiKey): sttp.client4.Request[Either[ResponseException[String], ListInstancesResponse]] =
    val regionPathParam = PathSerializable.serialize("region", region, PathStyleFormat.SIMPLE, false)
    val requestURL =
      uri"$baseUrl/rdb/v1/regions/${regionPathParam}/instances"
        .addParams(FormSerializable.serialize("tags", tags, FormStyleFormat.FORM, true): _*)
        .addParams(FormSerializable.serialize("name", name, FormStyleFormat.FORM, true): _*)
        .addParams(FormSerializable.serialize("order_by", orderBy, FormStyleFormat.FORM, true): _*)
        .addParams(FormSerializable.serialize("organization_id", organizationId, FormStyleFormat.FORM, true): _*)
        .addParams(FormSerializable.serialize("project_id", projectId, FormStyleFormat.FORM, true): _*)
        .addParams(FormSerializable.serialize("has_maintenances", hasMaintenances, FormStyleFormat.FORM, true): _*)
        .addParams(FormSerializable.serialize("page", page, FormStyleFormat.FORM, true): _*)
        .addParams(FormSerializable.serialize("page_size", pageSize, FormStyleFormat.FORM, true): _*)

    basicRequest
      .method(Method.GET, requestURL)
      .contentType("application/json")
      .auth(authConfig, scaleway.postgremysql.ApiKeyLocation.HEADER, "X-Auth-Token")
      .response(asJson[ListInstancesResponse])

  /**
   * Prepare your Database Instance logs. You can define the `start_date` and `end_date` parameters for your query. The download URL is returned in the response. Logs are recorded from 00h00 to 23h59 and then aggregated in a `.log` file once a day. Therefore, even if you specify a timeframe from which you want to get the logs, you will receive logs from the full 24 hours.
   * 
   * Expected answers:
   *   code 200 : PrepareInstanceLogsResponse ()
   * 
   * Available security schemes:
   *   scaleway (apiKey)
   * 
   * @param region The region you want to target
   * @param instanceId UUID of the Database Instance you want logs of.
   * @param prepareInstanceLogsRequest 
   */
  def prepareInstanceLogs(region: String, instanceId: String, prepareInstanceLogsRequest: PrepareInstanceLogsRequest)(using Auth <:< scaleway.postgremysql.Authorization.ApiKey): sttp.client4.Request[Either[ResponseException[String], PrepareInstanceLogsResponse]] =
    val regionPathParam = PathSerializable.serialize("region", region, PathStyleFormat.SIMPLE, false)
    val instanceIdPathParam = PathSerializable.serialize("instance_id", instanceId, PathStyleFormat.SIMPLE, false)
    val requestURL =
      uri"$baseUrl/rdb/v1/regions/${regionPathParam}/instances/${instanceIdPathParam}/prepare-logs"

    basicRequest
      .method(Method.POST, requestURL)
      .contentType("application/json")
      .auth(authConfig, scaleway.postgremysql.ApiKeyLocation.HEADER, "X-Auth-Token")
      .body(asJson(prepareInstanceLogsRequest))
      .response(asJson[PrepareInstanceLogsResponse])

  /**
   * Purge a given remote log from a Database Instance. You can specify the `log_name` of the log you wish to clean from your Database Instance.
   * 
   * Expected answers:
   *   code 204 :  ()
   * 
   * Available security schemes:
   *   scaleway (apiKey)
   * 
   * @param region The region you want to target
   * @param instanceId UUID of the Database Instance you want logs of.
   * @param purgeInstanceLogsRequest 
   */
  def purgeInstanceLogs(region: String, instanceId: String, purgeInstanceLogsRequest: PurgeInstanceLogsRequest)(using Auth <:< scaleway.postgremysql.Authorization.ApiKey): sttp.client4.Request[Either[ResponseException[String], Unit]] =
    val regionPathParam = PathSerializable.serialize("region", region, PathStyleFormat.SIMPLE, false)
    val instanceIdPathParam = PathSerializable.serialize("instance_id", instanceId, PathStyleFormat.SIMPLE, false)
    val requestURL =
      uri"$baseUrl/rdb/v1/regions/${regionPathParam}/instances/${instanceIdPathParam}/purge-logs"

    basicRequest
      .method(Method.POST, requestURL)
      .contentType("application/json")
      .auth(authConfig, scaleway.postgremysql.ApiKeyLocation.HEADER, "X-Auth-Token")
      .body(asJson(purgeInstanceLogsRequest))
      .response(asString.mapWithMetadata(ResponseAs.deserializeRightWithError(_ => Right(()))))

  /**
   * Renew a TLS for a Database Instance. Renewing a certificate means that you will not be able to connect to your Database Instance using the previous certificate. You will also need to download and update the new certificate for all database clients.
   * 
   * Expected answers:
   *   code 204 :  ()
   * 
   * Available security schemes:
   *   scaleway (apiKey)
   * 
   * @param region The region you want to target
   * @param instanceId UUID of the Database Instance you want logs of.
   * @param body 
   */
  def renewInstanceCertificate(region: String, instanceId: String, body: io.circe.Json)(using Auth <:< scaleway.postgremysql.Authorization.ApiKey): sttp.client4.Request[Either[ResponseException[String], Unit]] =
    val regionPathParam = PathSerializable.serialize("region", region, PathStyleFormat.SIMPLE, false)
    val instanceIdPathParam = PathSerializable.serialize("instance_id", instanceId, PathStyleFormat.SIMPLE, false)
    val requestURL =
      uri"$baseUrl/rdb/v1/regions/${regionPathParam}/instances/${instanceIdPathParam}/renew-certificate"

    basicRequest
      .method(Method.POST, requestURL)
      .contentType("application/json")
      .auth(authConfig, scaleway.postgremysql.ApiKeyLocation.HEADER, "X-Auth-Token")
      .body(asJson(body))
      .response(asString.mapWithMetadata(ResponseAs.deserializeRightWithError(_ => Right(()))))

  /**
   * Restart a given Database Instance, specified by the `region` and `instance_id` parameters. The status of the Database Instance returned in the response.
   * 
   * Expected answers:
   *   code 200 : Instance ()
   * 
   * Available security schemes:
   *   scaleway (apiKey)
   * 
   * @param region The region you want to target
   * @param instanceId UUID of the Database Instance you want to restart.
   * @param body 
   */
  def restartInstance(region: String, instanceId: String, body: io.circe.Json)(using Auth <:< scaleway.postgremysql.Authorization.ApiKey): sttp.client4.Request[Either[ResponseException[String], Instance]] =
    val regionPathParam = PathSerializable.serialize("region", region, PathStyleFormat.SIMPLE, false)
    val instanceIdPathParam = PathSerializable.serialize("instance_id", instanceId, PathStyleFormat.SIMPLE, false)
    val requestURL =
      uri"$baseUrl/rdb/v1/regions/${regionPathParam}/instances/${instanceIdPathParam}/restart"

    basicRequest
      .method(Method.POST, requestURL)
      .contentType("application/json")
      .auth(authConfig, scaleway.postgremysql.ApiKeyLocation.HEADER, "X-Auth-Token")
      .body(asJson(body))
      .response(asJson[Instance])

  /**
   * Update the parameters of a Database Instance, including name, tags and backup schedule details.
   * 
   * Expected answers:
   *   code 200 : Instance ()
   * 
   * Available security schemes:
   *   scaleway (apiKey)
   * 
   * @param region The region you want to target
   * @param instanceId UUID of the Database Instance to update.
   * @param updateInstanceRequest 
   */
  def updateInstance(region: String, instanceId: String, updateInstanceRequest: UpdateInstanceRequest)(using Auth <:< scaleway.postgremysql.Authorization.ApiKey): sttp.client4.Request[Either[ResponseException[String], Instance]] =
    val regionPathParam = PathSerializable.serialize("region", region, PathStyleFormat.SIMPLE, false)
    val instanceIdPathParam = PathSerializable.serialize("instance_id", instanceId, PathStyleFormat.SIMPLE, false)
    val requestURL =
      uri"$baseUrl/rdb/v1/regions/${regionPathParam}/instances/${instanceIdPathParam}"

    basicRequest
      .method(Method.PATCH, requestURL)
      .contentType("application/json")
      .auth(authConfig, scaleway.postgremysql.ApiKeyLocation.HEADER, "X-Auth-Token")
      .body(asJson(updateInstanceRequest))
      .response(asJson[Instance])

  /**
   * Upgrade your current Database Instance specifications like node type, high availability, volume, or the database engine version. Note that upon upgrade the `enable_ha` parameter can only be set to `true`.
   * 
   * Expected answers:
   *   code 200 : Instance ()
   * 
   * Available security schemes:
   *   scaleway (apiKey)
   * 
   * @param region The region you want to target
   * @param instanceId UUID of the Database Instance you want to upgrade.
   * @param upgradeInstanceRequest 
   */
  def upgradeInstance(region: String, instanceId: String, upgradeInstanceRequest: UpgradeInstanceRequest)(using Auth <:< scaleway.postgremysql.Authorization.ApiKey): sttp.client4.Request[Either[ResponseException[String], Instance]] =
    val regionPathParam = PathSerializable.serialize("region", region, PathStyleFormat.SIMPLE, false)
    val instanceIdPathParam = PathSerializable.serialize("instance_id", instanceId, PathStyleFormat.SIMPLE, false)
    val requestURL =
      uri"$baseUrl/rdb/v1/regions/${regionPathParam}/instances/${instanceIdPathParam}/upgrade"

    basicRequest
      .method(Method.POST, requestURL)
      .contentType("application/json")
      .auth(authConfig, scaleway.postgremysql.ApiKeyLocation.HEADER, "X-Auth-Token")
      .body(asJson(upgradeInstanceRequest))
      .response(asJson[Instance])

end DatabaseInstancesApi