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

import scaleway.postgremysql.models.CreateReadReplicaEndpointRequest
import scaleway.postgremysql.models.CreateReadReplicaRequest
import scaleway.postgremysql.models.Instance
import scaleway.postgremysql.models.ReadReplica
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

object ReadReplicasApi:
  def apply(baseUrl: String = "https://api.scaleway.com"): ReadReplicasApi[scaleway.postgremysql.Authorization.NoAuthorization.type] = ReadReplicasApi(baseUrl, scaleway.postgremysql.Authorization.NoAuthorization)
  def withBasicAuth(baseUrl: String, username: String, password: String): ReadReplicasApi[scaleway.postgremysql.Authorization.BasicAuth] =
    ReadReplicasApi(baseUrl, scaleway.postgremysql.Authorization.BasicAuth(username, password))

  def withApiKeyAuth(baseUrl: String, apiKey: String): ReadReplicasApi[scaleway.postgremysql.Authorization.ApiKey] =
    ReadReplicasApi(baseUrl, scaleway.postgremysql.Authorization.ApiKey(apiKey))

  def withBearerTokenAuth(baseUrl: String, token: String): ReadReplicasApi[scaleway.postgremysql.Authorization.BearerToken] =
    ReadReplicasApi(baseUrl, scaleway.postgremysql.Authorization.BearerToken(token))

case class ReadReplicasApi[Auth <: scaleway.postgremysql.Authorization] private (baseUrl: String, authConfig: scaleway.postgremysql.Authorization):
  def withBasicAuth(username: String, password: String): ReadReplicasApi[scaleway.postgremysql.Authorization.BasicAuth] =
    copy(authConfig = scaleway.postgremysql.Authorization.BasicAuth(username, password))

  def withApiKeyAuth(apiKey: String): ReadReplicasApi[scaleway.postgremysql.Authorization.ApiKey] =
    copy(authConfig = scaleway.postgremysql.Authorization.ApiKey(apiKey))

  def withNoAuth: ReadReplicasApi[scaleway.postgremysql.Authorization.NoAuthorization.type] =
    copy(authConfig = scaleway.postgremysql.Authorization.NoAuthorization)

  def withBearerTokenAuth(token: String): ReadReplicasApi[scaleway.postgremysql.Authorization.BearerToken] =
    copy(authConfig = scaleway.postgremysql.Authorization.BearerToken(token))

  /**
   * Create a new Read Replica of a Database Instance. You must specify the `region` and the `instance_id`. You can only create a maximum of 3 Read Replicas per Database Instance.
   * 
   * Expected answers:
   *   code 200 : ReadReplica ()
   * 
   * Available security schemes:
   *   scaleway (apiKey)
   * 
   * @param region The region you want to target
   * @param createReadReplicaRequest 
   */
  def createReadReplica(region: String, createReadReplicaRequest: CreateReadReplicaRequest)(using Auth <:< scaleway.postgremysql.Authorization.ApiKey): sttp.client4.Request[Either[ResponseException[String], ReadReplica]] =
    val regionPathParam = PathSerializable.serialize("region", region, PathStyleFormat.SIMPLE, false)
    val requestURL =
      uri"$baseUrl/rdb/v1/regions/${regionPathParam}/read-replicas"

    basicRequest
      .method(Method.POST, requestURL)
      .contentType("application/json")
      .auth(authConfig, scaleway.postgremysql.ApiKeyLocation.HEADER, "X-Auth-Token")
      .body(asJson(createReadReplicaRequest))
      .response(asJson[ReadReplica])

  /**
   * Create a new endpoint for a Read Replica. Read Replicas can have at most one direct access and one Private Network endpoint.
   * 
   * Expected answers:
   *   code 200 : ReadReplica ()
   * 
   * Available security schemes:
   *   scaleway (apiKey)
   * 
   * @param region The region you want to target
   * @param readReplicaId UUID of the Read Replica. (UUID format)
   * @param createReadReplicaEndpointRequest 
   */
  def createReadReplicaEndpoint(region: String, readReplicaId: String, createReadReplicaEndpointRequest: CreateReadReplicaEndpointRequest)(using Auth <:< scaleway.postgremysql.Authorization.ApiKey): sttp.client4.Request[Either[ResponseException[String], ReadReplica]] =
    val regionPathParam = PathSerializable.serialize("region", region, PathStyleFormat.SIMPLE, false)
    val readReplicaIdPathParam = PathSerializable.serialize("read_replica_id", readReplicaId, PathStyleFormat.SIMPLE, false)
    val requestURL =
      uri"$baseUrl/rdb/v1/regions/${regionPathParam}/read-replicas/${readReplicaIdPathParam}/endpoints"

    basicRequest
      .method(Method.POST, requestURL)
      .contentType("application/json")
      .auth(authConfig, scaleway.postgremysql.ApiKeyLocation.HEADER, "X-Auth-Token")
      .body(asJson(createReadReplicaEndpointRequest))
      .response(asJson[ReadReplica])

  /**
   * Delete a Read Replica of a Database Instance. You must specify the `region` and `read_replica_id` parameters of the Read Replica you want to delete.
   * 
   * Expected answers:
   *   code 200 : ReadReplica ()
   * 
   * Available security schemes:
   *   scaleway (apiKey)
   * 
   * @param region The region you want to target
   * @param readReplicaId UUID of the Read Replica. (UUID format)
   */
  def deleteReadReplica(region: String, readReplicaId: String)(using Auth <:< scaleway.postgremysql.Authorization.ApiKey): sttp.client4.Request[Either[ResponseException[String], ReadReplica]] =
    val regionPathParam = PathSerializable.serialize("region", region, PathStyleFormat.SIMPLE, false)
    val readReplicaIdPathParam = PathSerializable.serialize("read_replica_id", readReplicaId, PathStyleFormat.SIMPLE, false)
    val requestURL =
      uri"$baseUrl/rdb/v1/regions/${regionPathParam}/read-replicas/${readReplicaIdPathParam}"

    basicRequest
      .method(Method.DELETE, requestURL)
      .contentType("application/json")
      .auth(authConfig, scaleway.postgremysql.ApiKeyLocation.HEADER, "X-Auth-Token")
      .response(asJson[ReadReplica])

  /**
   * Retrieve information about a Database Instance Read Replica. Full details about the Read Replica, like `endpoints`, `status` and `region` are returned in the response.
   * 
   * Expected answers:
   *   code 200 : ReadReplica ()
   * 
   * Available security schemes:
   *   scaleway (apiKey)
   * 
   * @param region The region you want to target
   * @param readReplicaId UUID of the Read Replica. (UUID format)
   */
  def getReadReplica(region: String, readReplicaId: String)(using Auth <:< scaleway.postgremysql.Authorization.ApiKey): sttp.client4.Request[Either[ResponseException[String], ReadReplica]] =
    val regionPathParam = PathSerializable.serialize("region", region, PathStyleFormat.SIMPLE, false)
    val readReplicaIdPathParam = PathSerializable.serialize("read_replica_id", readReplicaId, PathStyleFormat.SIMPLE, false)
    val requestURL =
      uri"$baseUrl/rdb/v1/regions/${regionPathParam}/read-replicas/${readReplicaIdPathParam}"

    basicRequest
      .method(Method.GET, requestURL)
      .contentType("application/json")
      .auth(authConfig, scaleway.postgremysql.ApiKeyLocation.HEADER, "X-Auth-Token")
      .response(asJson[ReadReplica])

  /**
   * Promote a Read Replica to Database Instance automatically.
   * 
   * Expected answers:
   *   code 200 : Instance ()
   * 
   * Available security schemes:
   *   scaleway (apiKey)
   * 
   * @param region The region you want to target
   * @param readReplicaId UUID of the Read Replica. (UUID format)
   * @param body 
   */
  def promoteReadReplica(region: String, readReplicaId: String, body: io.circe.Json)(using Auth <:< scaleway.postgremysql.Authorization.ApiKey): sttp.client4.Request[Either[ResponseException[String], Instance]] =
    val regionPathParam = PathSerializable.serialize("region", region, PathStyleFormat.SIMPLE, false)
    val readReplicaIdPathParam = PathSerializable.serialize("read_replica_id", readReplicaId, PathStyleFormat.SIMPLE, false)
    val requestURL =
      uri"$baseUrl/rdb/v1/regions/${regionPathParam}/read-replicas/${readReplicaIdPathParam}/promote"

    basicRequest
      .method(Method.POST, requestURL)
      .contentType("application/json")
      .auth(authConfig, scaleway.postgremysql.ApiKeyLocation.HEADER, "X-Auth-Token")
      .body(asJson(body))
      .response(asJson[Instance])

  /**
   * When you resync a Read Replica, first it is reset, then its data is resynchronized from the primary node. Your Read Replica remains unavailable during the resync process. The duration of this process is proportional to the size of your Database Instance. The configured endpoints do not change.
   * 
   * Expected answers:
   *   code 200 : ReadReplica ()
   * 
   * Available security schemes:
   *   scaleway (apiKey)
   * 
   * @param region The region you want to target
   * @param readReplicaId UUID of the Read Replica. (UUID format)
   * @param body 
   */
  def resetReadReplica(region: String, readReplicaId: String, body: io.circe.Json)(using Auth <:< scaleway.postgremysql.Authorization.ApiKey): sttp.client4.Request[Either[ResponseException[String], ReadReplica]] =
    val regionPathParam = PathSerializable.serialize("region", region, PathStyleFormat.SIMPLE, false)
    val readReplicaIdPathParam = PathSerializable.serialize("read_replica_id", readReplicaId, PathStyleFormat.SIMPLE, false)
    val requestURL =
      uri"$baseUrl/rdb/v1/regions/${regionPathParam}/read-replicas/${readReplicaIdPathParam}/reset"

    basicRequest
      .method(Method.POST, requestURL)
      .contentType("application/json")
      .auth(authConfig, scaleway.postgremysql.ApiKeyLocation.HEADER, "X-Auth-Token")
      .body(asJson(body))
      .response(asJson[ReadReplica])

end ReadReplicasApi