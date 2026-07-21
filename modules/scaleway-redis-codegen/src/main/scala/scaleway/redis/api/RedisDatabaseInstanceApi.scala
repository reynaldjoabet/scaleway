/**
 * Managed Database for Redis™ API
 * Managed Database for Redis™ is a low-latency caching solution. It allows you to easily set up a secure cache and lighten the load on your main database. Based on the in-memory data storage, Managed Database for Redis™ improves your application response time and helps you provide a better experience to your users.  Using Managed Database for Redis™ as a cache optimizes the speed of your requests as copies of the most frequently used data are stored in memory, making them accessible in milliseconds.     ## Concepts  Refer to our [dedicated concepts page](https://www.scaleway.com/en/docs/managed-databases-for-redis/concepts/) to find definitions of the different terms referring to Managed Database for Redis™.     ## Quickstart  1. Configure your environment variables.     <Message type=\"note\">     This is an optional step that seeks to simplify your usage of the APIs     </Message>       ```bash     export ACCESS_KEY=\"<access-key>\"     export SECRET_KEY=\"<secret-key>\"     export SCW_ZONE=\"<zone>\"     ``` 2. Edit the POST request payload you will use to create your Redis™ Database Instance cluster. Replace the parameters in the following example:     ```json     {     \"project_id\":\"50e8d5d3-c623-4df8-a5ef-1972a5432001\",     \"name\":\"cluster1\",     \"version\":\"7.0.5\",     \"tags\":[         \"tag1\"     ],     \"node_type\":\"RED1-micro\",     \"user_name\":\"redis-user\",     \"password\":\"lxbemloZiPT1l37*\",     \"cluster_size\": 3,     \"tls_enabled\": true     }     ```      | Parameter        | Description                                                        |     | :--------------- | :----------------------------------------------------------------- |     | `project_id`     | **REQUIRED** The ID of the Project you want to create your Database Instance in. To find your Project ID you can **[list the projects](/api/account/project-api/#path-projects-list-all-projects-of-an-organization)** or consult the **[Scaleway console](https://console.scaleway.com/project/settings)**. |     | `name`           | Name of the Redis™ Database Instance                                          |     | `version`        | **REQUIRED** Version of the Redis™ engine. To check the list of available versions you can use the following endpoint: `https://api.scaleway.com/redis/v1/zones/$SCW_ZONE/cluster-versions`                                           |     | `tags`           | The list of tags `[\"tag1\", \"tag2\", ...]` that will be associated with the Redis™ Database Instance. Tags can be appended to the query of the **[List Database Instances](#path-redistm-database-instance-list-redistm-database-instances)** call to show a list of the Database Instances using a specific tag. You can also combine tags to list Database Instances that possess **all** of the appended tags. |     | `node_type`      | **REQUIRED** The node type. To check the list of available node types you can use the following endpoint: `https://api.scaleway.com/redis/v1/zones/$SCW_ZONE/node-types` |     | `user_name`      | **REQUIRED** Identifier of the default user, which is created concurrently with the Redis™ Database Instance |     | `password`       | **REQUIRED** Password for the default user |     | `cluster_size`   | **INTEGER** The number of nodes in the Redis™ Database Instance cluster. You can either set it to 1 for a standalone Database Instance, 2 for High Availability, or from 3 to 6, for cluster mode |     | `tls_enabled`    | **BOOLEAN** Whether or not to enable TLS certificates | 3. Create a Redis™ Database Instance by running the following command. Make sure you include the payload you edited in the previous step.     ```bash     curl -X POST \\       -H \"Content-Type: application/json\" \\       -H \"X-Auth-Token: $SCW_SECRET_KEY\" https://api.scaleway.com/redis/v1/zones/$SCW_ZONE/clusters \\       -d  '{         \"project_id\":\"50e8d5d3-c623-4df8-a5ef-1972a5432001\",         \"name\":\"cluster1\",         \"version\":\"7.0.5\",         \"tags\":[             \"tag1\"         ],         \"node_type\":\"RED1-micro\",         \"user_name\":\"redis-user\",         \"password\":\"lxbemloZiPT1l37*\",         \"cluster_size\": 2,         \"tls_enabled\": true       }'     ``` 4. List your Redis™ Database Instances.     ```bash     curl -X GET       -H \"Content-Type: application/json\" \\       -H \"X-Auth-Token: $SCW_SECRET_KEY\" https://api.scaleway.com/redis/v1/zones/$SCW_ZONE/clusters     ``` 5. Retrieve your Redis™ Database Instance IP and port from the response.     <Message type=\"note\">     In this tutorial, we will use `192.0.2.0` and `6379` as the IP and port, respectively     </Message> 6. Connect to your Database Instance with the Redis™ client.     <Message type=\"note\">     You can use only one of your node IP addresses at a time to connect to your Redis™ Database Instance, as the Redis™ CLI does not support cluster mode.     </Message>      ```bash     redis-cli -h 192.0.2.0 -p 6379 --user redis-user --askpass --tls --cacert SSL_redis-cluster1.pem     ```      <Message type=\"note\">     The command above uses TLS to add an extra layer of security to your connection. The TLS certificate is generated automatically if you set tls_enabled to true. The certificates take on the following name structure: `SSL_redis-<name-of-your-redis-database-instance>.pem`. When using connectors other than redis-cli, you might need to specify the path to your certificate.     </Message>      <Message type=\"important\">     Scaleway supports TLS1.2 and TLS1.3. If you use older versions of `libssl`, you might encounter connexion issues when using `redis-cli`. If this is the case, we recommend you check the libssl versions installed on your local machine and update if necessary.     </Message> 7. Enter the database password that you defined upon creation.  You are now connected to your Managed Database for Redis™.   <Message type=\"requirement\"> To perform the following steps, you must first ensure that:   - you have an account and are logged into the [Scaleway console](https://console.scaleway.com/organization)   - you have created an [API key](https://www.scaleway.com/en/docs/iam/how-to/create-api-keys/) and that the API key has sufficient [IAM permissions](https://www.scaleway.com/en/docs/iam/reference-content/permission-sets/) to perform the actions described on this page.   - you have [installed `curl`](https://curl.se/download.html) </Message>   ## Going Further  For more information about Managed Database for Redis™, you can check out the following pages:  * [Managed Database for Redis™ Documentation](https://www.scaleway.com/en/docs/managed-databases-for-redis/quickstart/) * [Managed Database for Redis™ FAQ](https://www.scaleway.com/en/docs/managed-databases-for-redis/faq/) * [Scaleway Slack Community](https://scaleway-community.slack.com/) join the #database channel * [Contact our support team](https://console.scaleway.com/support/tickets/).
 *
 * The version of the OpenAPI document: v1
 * 
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */
package scaleway.redis.api

import scaleway.redis.models.Cluster
import scaleway.redis.models.ClusterMetricsResponse
import scaleway.redis.models.CreateClusterRequest
import scaleway.redis.models.ListClustersResponse
import scaleway.redis.models.MigrateClusterRequest
import java.time.OffsetDateTime
import scaleway.redis.models.UpdateClusterRequest
import scaleway.redis.JsonSupport.{*, given}
import scaleway.redis.FormSerializable
import scaleway.redis.FormStyleFormat
import scaleway.redis.HeaderSerializable
import scaleway.redis.ApiKeyLocation
import scaleway.redis.PathStyleFormat
import scaleway.redis.PathSerializable
import scaleway.redis.CookieSerializable
import scaleway.redis.Helpers.*
import sttp.client4.jsoniter.*
import sttp.client4.*
import sttp.model.Method

object RedisDatabaseInstanceApi:
  def apply(baseUrl: String = "https://api.scaleway.com"): RedisDatabaseInstanceApi[scaleway.redis.Authorization.NoAuthorization.type] = RedisDatabaseInstanceApi(baseUrl, scaleway.redis.Authorization.NoAuthorization)
  def withBasicAuth(baseUrl: String, username: String, password: String): RedisDatabaseInstanceApi[scaleway.redis.Authorization.BasicAuth] =
    RedisDatabaseInstanceApi(baseUrl, scaleway.redis.Authorization.BasicAuth(username, password))

  def withApiKeyAuth(baseUrl: String, apiKey: String): RedisDatabaseInstanceApi[scaleway.redis.Authorization.ApiKey] =
    RedisDatabaseInstanceApi(baseUrl, scaleway.redis.Authorization.ApiKey(apiKey))

  def withBearerTokenAuth(baseUrl: String, token: String): RedisDatabaseInstanceApi[scaleway.redis.Authorization.BearerToken] =
    RedisDatabaseInstanceApi(baseUrl, scaleway.redis.Authorization.BearerToken(token))

case class RedisDatabaseInstanceApi[Auth <: scaleway.redis.Authorization] private (baseUrl: String, authConfig: scaleway.redis.Authorization):
  def withBasicAuth(username: String, password: String): RedisDatabaseInstanceApi[scaleway.redis.Authorization.BasicAuth] =
    copy(authConfig = scaleway.redis.Authorization.BasicAuth(username, password))

  def withApiKeyAuth(apiKey: String): RedisDatabaseInstanceApi[scaleway.redis.Authorization.ApiKey] =
    copy(authConfig = scaleway.redis.Authorization.ApiKey(apiKey))

  def withNoAuth: RedisDatabaseInstanceApi[scaleway.redis.Authorization.NoAuthorization.type] =
    copy(authConfig = scaleway.redis.Authorization.NoAuthorization)

  def withBearerTokenAuth(token: String): RedisDatabaseInstanceApi[scaleway.redis.Authorization.BearerToken] =
    copy(authConfig = scaleway.redis.Authorization.BearerToken(token))

  /**
   * Create a new Redis™ Database Instance (Redis™ cluster). You must set the `zone`, `project_id`, `version`, `node_type`, `user_name` and `password` parameters. Optionally you can define `acl_rules`, `endpoints`, `tls_enabled` and `cluster_settings`.
   * 
   * Expected answers:
   *   code 200 : Cluster ()
   * 
   * Available security schemes:
   *   scaleway (apiKey)
   * 
   * @param zone The zone you want to target
   * @param createClusterRequest 
   */
  def createCluster(zone: String, createClusterRequest: CreateClusterRequest)(using Auth <:< scaleway.redis.Authorization.ApiKey): sttp.client4.Request[Either[ResponseException[String], Cluster]] =
    val zonePathParam = PathSerializable.serialize("zone", zone, PathStyleFormat.SIMPLE, false)
    val requestURL =
      uri"$baseUrl/redis/v1/zones/${zonePathParam}/clusters"

    basicRequest
      .method(Method.POST, requestURL)
      .contentType("application/json")
      .auth(authConfig, scaleway.redis.ApiKeyLocation.HEADER, "X-Auth-Token")
      .body(asJson(createClusterRequest))
      .response(asJson[Cluster])

  /**
   * Delete a Redis™ Database Instance (Redis™ cluster), specified by the `region` and `cluster_id` parameters. Deleting a Database Instance is permanent, and cannot be undone. Note that upon deletion all your data will be lost.
   * 
   * Expected answers:
   *   code 200 : Cluster ()
   * 
   * Available security schemes:
   *   scaleway (apiKey)
   * 
   * @param zone The zone you want to target
   * @param clusterId UUID of the Database Instance to delete. (UUID format)
   */
  def deleteCluster(zone: String, clusterId: String)(using Auth <:< scaleway.redis.Authorization.ApiKey): sttp.client4.Request[Either[ResponseException[String], Cluster]] =
    val zonePathParam = PathSerializable.serialize("zone", zone, PathStyleFormat.SIMPLE, false)
    val clusterIdPathParam = PathSerializable.serialize("cluster_id", clusterId, PathStyleFormat.SIMPLE, false)
    val requestURL =
      uri"$baseUrl/redis/v1/zones/${zonePathParam}/clusters/${clusterIdPathParam}"

    basicRequest
      .method(Method.DELETE, requestURL)
      .contentType("application/json")
      .auth(authConfig, scaleway.redis.ApiKeyLocation.HEADER, "X-Auth-Token")
      .response(asJson[Cluster])

  /**
   * Retrieve information about a Redis™ Database Instance (Redis™ cluster). Specify the `cluster_id` and `region` in your request to get information such as `id`, `status`, `version`, `tls_enabled`, `cluster_settings`, `upgradable_versions` and `endpoints` about your cluster in the response.
   * 
   * Expected answers:
   *   code 200 : Cluster ()
   * 
   * Available security schemes:
   *   scaleway (apiKey)
   * 
   * @param zone The zone you want to target
   * @param clusterId UUID of the cluster. (UUID format)
   */
  def getCluster(zone: String, clusterId: String)(using Auth <:< scaleway.redis.Authorization.ApiKey): sttp.client4.Request[Either[ResponseException[String], Cluster]] =
    val zonePathParam = PathSerializable.serialize("zone", zone, PathStyleFormat.SIMPLE, false)
    val clusterIdPathParam = PathSerializable.serialize("cluster_id", clusterId, PathStyleFormat.SIMPLE, false)
    val requestURL =
      uri"$baseUrl/redis/v1/zones/${zonePathParam}/clusters/${clusterIdPathParam}"

    basicRequest
      .method(Method.GET, requestURL)
      .contentType("application/json")
      .auth(authConfig, scaleway.redis.ApiKeyLocation.HEADER, "X-Auth-Token")
      .response(asJson[Cluster])

  /**
   * Retrieve the metrics of a Redis™ Database Instance (Redis™ cluster). You can define the period from which to retrieve metrics by specifying the `start_date` and `end_date`.
   * 
   * Expected answers:
   *   code 200 : ClusterMetricsResponse ()
   * 
   * Available security schemes:
   *   scaleway (apiKey)
   * 
   * @param zone The zone you want to target
   * @param clusterId UUID of the cluster. (UUID format)
   * @param startAt Start date. (RFC 3339 format)
   * @param endAt End date. (RFC 3339 format)
   * @param metricName Name of the metric to gather.
   */
  def getClusterMetrics(zone: String, clusterId: String, startAt: Option[OffsetDateTime] = scala.None, endAt: Option[OffsetDateTime] = scala.None, metricName: Option[String] = scala.None)(using Auth <:< scaleway.redis.Authorization.ApiKey): sttp.client4.Request[Either[ResponseException[String], ClusterMetricsResponse]] =
    val zonePathParam = PathSerializable.serialize("zone", zone, PathStyleFormat.SIMPLE, false)
    val clusterIdPathParam = PathSerializable.serialize("cluster_id", clusterId, PathStyleFormat.SIMPLE, false)
    val requestURL =
      uri"$baseUrl/redis/v1/zones/${zonePathParam}/clusters/${clusterIdPathParam}/metrics"
        .addParams(FormSerializable.serialize("start_at", startAt, FormStyleFormat.FORM, true): _*)
        .addParams(FormSerializable.serialize("end_at", endAt, FormStyleFormat.FORM, true): _*)
        .addParams(FormSerializable.serialize("metric_name", metricName, FormStyleFormat.FORM, true): _*)

    basicRequest
      .method(Method.GET, requestURL)
      .contentType("application/json")
      .auth(authConfig, scaleway.redis.ApiKeyLocation.HEADER, "X-Auth-Token")
      .response(asJson[ClusterMetricsResponse])

  /**
   * List all Redis™ Database Instances (Redis™ cluster) in the specified zone. By default, the Database Instances returned in the list are ordered by creation date in ascending order, though this can be modified via the order_by field. You can define additional parameters for your query, such as `tags`, `name`, `organization_id` and `version`.
   * 
   * Expected answers:
   *   code 200 : ListClustersResponse ()
   * 
   * Available security schemes:
   *   scaleway (apiKey)
   * 
   * @param zone The zone you want to target
   * @param tags Filter by Database Instance tags.
   * @param name Filter by Database Instance names.
   * @param orderBy Criteria to use when ordering the list.
   * @param projectId Filter by Project ID. (UUID format)
   * @param organizationId Filter by Organization ID. (UUID format)
   * @param version Filter by Redis™ engine version.
   * @param page 
   * @param pageSize 
   */
  def listClusters(zone: String, tags: Seq[String], name: Option[String] = scala.None, orderBy: Option[String] = scala.None, projectId: Option[String] = scala.None, organizationId: Option[String] = scala.None, version: Option[String] = scala.None, page: Option[Int] = scala.None, pageSize: Option[Int] = scala.None)(using Auth <:< scaleway.redis.Authorization.ApiKey): sttp.client4.Request[Either[ResponseException[String], ListClustersResponse]] =
    val zonePathParam = PathSerializable.serialize("zone", zone, PathStyleFormat.SIMPLE, false)
    val requestURL =
      uri"$baseUrl/redis/v1/zones/${zonePathParam}/clusters"
        .addParams(FormSerializable.serialize("tags", tags, FormStyleFormat.FORM, true): _*)
        .addParams(FormSerializable.serialize("name", name, FormStyleFormat.FORM, true): _*)
        .addParams(FormSerializable.serialize("order_by", orderBy, FormStyleFormat.FORM, true): _*)
        .addParams(FormSerializable.serialize("project_id", projectId, FormStyleFormat.FORM, true): _*)
        .addParams(FormSerializable.serialize("organization_id", organizationId, FormStyleFormat.FORM, true): _*)
        .addParams(FormSerializable.serialize("version", version, FormStyleFormat.FORM, true): _*)
        .addParams(FormSerializable.serialize("page", page, FormStyleFormat.FORM, true): _*)
        .addParams(FormSerializable.serialize("page_size", pageSize, FormStyleFormat.FORM, true): _*)

    basicRequest
      .method(Method.GET, requestURL)
      .contentType("application/json")
      .auth(authConfig, scaleway.redis.ApiKeyLocation.HEADER, "X-Auth-Token")
      .response(asJson[ListClustersResponse])

  /**
   * Upgrade your Redis™ Database Instance, either by upgrading to a bigger node type (vertical scaling) or by adding more nodes to your Database Instance to increase your number of endpoints and distribute cache (horizontal scaling, available for clusters only). Note that scaling horizontally your Redis™ Database Instance will not renew its TLS certificate. In order to refresh the TLS certificate, you must use the Renew TLS certificate endpoint.
   * 
   * Expected answers:
   *   code 200 : Cluster ()
   * 
   * Available security schemes:
   *   scaleway (apiKey)
   * 
   * @param zone The zone you want to target
   * @param clusterId UUID of the Database Instance to update. (UUID format)
   * @param migrateClusterRequest 
   */
  def migrateCluster(zone: String, clusterId: String, migrateClusterRequest: MigrateClusterRequest)(using Auth <:< scaleway.redis.Authorization.ApiKey): sttp.client4.Request[Either[ResponseException[String], Cluster]] =
    val zonePathParam = PathSerializable.serialize("zone", zone, PathStyleFormat.SIMPLE, false)
    val clusterIdPathParam = PathSerializable.serialize("cluster_id", clusterId, PathStyleFormat.SIMPLE, false)
    val requestURL =
      uri"$baseUrl/redis/v1/zones/${zonePathParam}/clusters/${clusterIdPathParam}/migrate"

    basicRequest
      .method(Method.POST, requestURL)
      .contentType("application/json")
      .auth(authConfig, scaleway.redis.ApiKeyLocation.HEADER, "X-Auth-Token")
      .body(asJson(migrateClusterRequest))
      .response(asJson[Cluster])

  /**
   * Update the parameters of a Redis™ Database Instance (Redis™ cluster), including `name`, `tags`, `user_name` and `password`.
   * 
   * Expected answers:
   *   code 200 : Cluster ()
   * 
   * Available security schemes:
   *   scaleway (apiKey)
   * 
   * @param zone The zone you want to target
   * @param clusterId UUID of the Database Instance to update. (UUID format)
   * @param updateClusterRequest 
   */
  def updateCluster(zone: String, clusterId: String, updateClusterRequest: UpdateClusterRequest)(using Auth <:< scaleway.redis.Authorization.ApiKey): sttp.client4.Request[Either[ResponseException[String], Cluster]] =
    val zonePathParam = PathSerializable.serialize("zone", zone, PathStyleFormat.SIMPLE, false)
    val clusterIdPathParam = PathSerializable.serialize("cluster_id", clusterId, PathStyleFormat.SIMPLE, false)
    val requestURL =
      uri"$baseUrl/redis/v1/zones/${zonePathParam}/clusters/${clusterIdPathParam}"

    basicRequest
      .method(Method.PATCH, requestURL)
      .contentType("application/json")
      .auth(authConfig, scaleway.redis.ApiKeyLocation.HEADER, "X-Auth-Token")
      .body(asJson(updateClusterRequest))
      .response(asJson[Cluster])

end RedisDatabaseInstanceApi