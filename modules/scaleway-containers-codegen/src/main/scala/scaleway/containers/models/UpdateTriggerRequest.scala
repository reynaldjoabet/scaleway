/** Serverless Containers API Scaleway Serverless Containers is a «Container As A Service» product which gives users the
  * ability to deploy atomic serverless workloads and only pay for resources used while containers are running. It
  * provides many advantages, such as: - Containers are only executed when an event is triggered, which allows users to
  * save money while code is not running - Auto-Scalability: - Automated `Scaling up and down` based on user
  * configuration (e.g. min: 0, max: 100 replicas of my container). - Automated `Scaling to zero` when a container is
  * not executed, which is cost-effective for the user and saves computing resources for the cloud provider. -
  * Endpoint-only scaling ### Serverless Framework This page explains how to use the Scaleway Containers API, including
  * a quickstart and the full API documentation. However, you may prefer to use the [Serverless Framework
  * plugin](https://github.com/scaleway/serverless-scaleway-functions) enabling users to deploy their serverless
  * workloads much more easily with a single `serverless deploy` command. If what you are looking for is an easy way to
  * deploy your code, you may prefer Serverless Framework. Below, you will find a step-by-step guide on how to create a
  * `namespace`, configure and deploy `containers`, and trigger your `containers` via HTTP and CRON. ## Concepts Refer
  * to our [dedicated concepts page](https://www.scaleway.com/en/docs/serverless/containers/concepts/) to find
  * definitions of the different terms referring to Serverless Containers. ## Quickstart 1. Configure your environment
  * variables. ```bash     export $SCW_SECRET_KEY=\"<Secret key of your token>\"     export SCW_DEFAULT_REGION=\"<Choose your location (pl-waw/nl-ams/fr-par)>\"     export SCW_PROJECT_ID=\"<Your Project ID>\"     ```
  * <Message type=\"tip\"> This is an optional step that seeks to simplify your usage of the Serverless Containers API.
  * See [Regions](#availability-zones) below for help choosing a region. You can find your Project ID in the [Scaleway
  * console](https://console.scaleway.com/Project/settings). </Message> 2. Set the name for your namespace and configure
  * your Project ID: ```bash     curl -X POST \"https://api.scaleway.com/containers/v1beta1/regions/$SCW_DEFAULT_REGION/namespaces\" \\       -H \"accept: application/json\" \\       -H \"X-Auth-Token: $SCW_SECRET_KEY\" \\       -H \"Content-Type: application/json\" \\       -d \"{           \\\"name\\\": \\\"your-namespace-name\\\", \\           \\\"project_id\\\": \\\"$SCW_PROJECT_ID\\\", \\           \\\"environment_variables\\\": {\\\"YOUR_VARIABLE\\\": \\\"content\\\"} \\           }\"     ``` 3.
  * **Copy the response's `id` field**, you will need it for the next steps. For the sake of simplicity, we will save
  * the ID to a variable, which will be used in the following examples: ```bash     export NAMESPACE_ID=\"<your namespace id>\"     ```
  * <Message type=\"note\"> We suppose you already have a working image here. It can be anything which listens on a env
  * variable \\$PORT variable. Note that we run your container as user 1000, not root, so it must be runnable under
  * these conditions. For more information on how to push your image, refer to the [Container Registry
  * documentation](https://www.scaleway.com/developers/api/registry/). </Message> 4. **Edit the POST request payload**
  * to use in the next step to create an Elastic Metal server. Modify the values in the example according to your
  * requirements, using the information in the payload values section to help. For container resource criteria and
  * default values, please refer to the [Containers Limitation
  * documentation](https://www.scaleway.com/en/docs/serverless/containers/reference-content/containers-limitations/). ```json     {       \"namespace_id\": \"string\",       \"name\": \"string\",       \"environment_variables\": {         \"<key>\": \"string\"       },       \"min_scale\": \"integer\",       \"max_scale\": \"integer\",       \"memory_limit\": \"integer\",       \"cpu_limit\": \"integer\",       \"timeout\": \"integer\",       \"privacy\": \"unknown_privacy\",       \"description\": \"string\",       \"registry_image\": \"string\",       \"max_concurrency\": \"integer\",       \"protocol\": \"unknown_protocol\",       \"port\": \"integer\",       \"secret_environment_variables\": [         {           \"key\": \"string\",           \"value\": \"string\"         }       ],       \"http_option\": \"enabled\"     }     ``` |
  * Parameter | Description | | :--------------- | :----------------------------------------------------------------- | |
  * `region` | The region you want to target. Possible values are fr-par, nl-ams and pl-waw. | | `namespace_id`| UUID of
  * the namespace the container belongs to. | | `name`| Name of the container. | | `environment_variables` |
  * **NULLABLE** Environment variables of the container. | | `min_scale` | **NULLABLE** Minimum number of instances to
  * scale the container to. | | `max_scale` | **NULLABLE** Maximum number of instances to scale the container to. | |
  * `memory_limit`| **NULLABLE** Memory limit of the container in MiB. | | `cpu_limit`| **NULLABLE** CPU limit of the
  * container in mvCPU. | | `timeout` | **NULLABLE** Request processing time limit for the container. (in seconds). | |
  * `privacy` | Privacy setting of the container. | | `description` | **NULLABLE** Description of the container. | |
  * `registry_image`| **NULLABLE** Name of the registry image (e.g. \"rg.fr-par.scw.cloud/something/image:tag\"). | |
  * `max_concurrency` | **NULLABLE** Number of maximum concurrent executions of the container. | | `protocol` | Protocol
  * the container uses. Possible values are unknown_protocol, http1 and h2c. The default value is unknown_protocol. | |
  * `port` | **NULLABLE** Port the container listens on. | | `secret_environment_variables` | Secret environment
  * variables of the container. | | `http_option` | Configure how HTTP and HTTPS requests are handled. Possible
  * values:<br /> - `redirected`: Responds to HTTP request with a 301 redirect to ask the clients to use HTTPS.<br /> -
  * `enabled`: Serve both HTTP and HTTPS traffic. | <Message type=\"important\"> All parameters are `required`, except
  * for those marked as nullable. </Message> 5. Run the following command to create your container. Make sure you
  * include the payload you edited in the previous step. ```bash     export REGISTRY_IMAGE=\"rg.fr-par.scw.cloud/myregistrynamespace/mycontainer:latest\"     curl -X POST \\       -H \"X-Auth-Token: $SCW_SECRET_KEY\" \\       \"https://api.scaleway.com/containers/v1beta1/regions/$SCW_DEFAULT_REGION/containers\" \\       -d \"{           \"name\": MyContainer, \\           \"registry_image\": \"$REGISTRY_IMAGE\", \\           \"namespace_id\": \"$NAMESPACE_ID\", \\           \"memory_limit\": 300, \\           \"cpu_limit\": 200, \\           \"min_scale\": 0, \\           \"max_scale\": 20 \\           }\"     ``` 6.
  * Run the following command to deploy your container: ```bash     curl -X POST \\       -H \"X-Auth-Token: $SCW_SECRET_KEY\" \\       \"https://api.scaleway.com/containers/v1beta1/regions/$SCW_DEFAULT_REGION/containers/$CONTAINER_ID/deploy\" \\       -d \"{}\"     ``` 7.
  * Run the following command to trigger your container. ```bash     curl -X GET \\       -H \"X-Auth-Token: $SCW_SECRET_KEY\" \\       \"https://api.scaleway.com/containers/v1beta1/regions/$SCW_DEFAULT_REGION/containers/$CONTAINER_ID\"     export CONTAINER_ENDPOINT=\"<endpoint>\"     curl -X GET \"$CONTAINER_ENDPOINT\"     ``` 8.
  * (optional) Connect to your Cockpit (Serverless Containers Logs dashboard) to see your logs:
  * https://www.scaleway.com/en/docs/observability/cockpit/how-to/access-grafana-and-managed-dashboards/. 9. (optional)
  * Use the following call to destroy a namespace (along with all containers and crons): ```bash     curl -s \\       -H \"X-Auth-Token: $SCW_SECRET_KEY\" \\       -X DELETE \"https://api.scaleway.com/containers/v1beta1/regions/$SCW_DEFAULT_REGION/namespaces/$NAMESPACE_ID\"     ```
  * <Message type=\"requirement\"> To perform the following steps, you must first ensure that: - You have a [Scaleway
  * account](https://console.scaleway.com/) - You have created an [API
  * key](https://www.scaleway.com/en/docs/iam/how-to/create-api-keys/) and that the API key has sufficient [IAM
  * permissions](https://www.scaleway.com/en/docs/iam/reference-content/permission-sets/) to perform the actions
  * described on this page - You have [installed `curl`](https://curl.se/download.html) - You have [installed
  * `jq`](https://stedolan.github.io/jq/) to improve readability of the API outputs </Message> ## Technical information
  * A **Container** in Scaleway Containers consists of multiple components: - **Environment variables**: Users may
  * configure specific environment variables (Database host/credentials for example) which are safely encrypted in our
  * Database, and will be mounted inside your containers. **Note** that environment variables set at `namespace` level
  * will also be mounted (in every container). Environment variables written at `container` level override the ones set
  * at `namespace` level (if two of them bear the same name for example). - **Docker image**: A Docker image contains
  * all the elements required to run your software: code, a runtime environment, tools, scripts, libraries, etc. -
  * **Resources**: Users may decide how much computing resource to allocate to each container -> `Memory Limit` (in MB).
  * We will then allocate the right amount of `CPU` based on Memory Limit choice. The right choice for your container's
  * resources is very important, as you will be billed based on compute usage over time and the number of Containers
  * executions. ### Product features - Fully isolated environments - Scaling to zero (saves money and computing
  * resources while the code is not executed) - High Availability and scalability (automated and configurable, each
  * container may scale automatically according to incoming workloads) - Multiple event sources: - HTTP (request on our
  * gateway will execute the container) - CRON (time-based job, runs according to configurable cron schedule) -
  * Integrated to the Scaleway Container Registry product: - Deploy any docker image from one of your registry namespace -
  * Flexible resources: you can choose values separately for your memory and CPU within a range in respect with maximum
  * and minimum allowed values in [Containers Limitation
  * documentation](https://www.scaleway.com/en/docs/serverless/containers/reference-content/containers-limitations/).
  * ### Regions Serverless Containers is available in the Paris, Amsterdam and Warsaw regions, which are represented by
  * the following path parameters: * `fr-par` * `nl-ams` * `pl-waw` ### CRON A `CRON` is a type of event which triggers
  * a Scaleway Container: it is an `add-on` to your container. CRONs inside Scaleway Containers have the following
  * properties: - `schedule`: UNIX Formatted CRON schedule. Your container will be executed based on this schedule. For
  * example, `5 4 * * 0` means \"execute my container at 04:05 AM every Sunday\" (see this [page from Ubuntu's official
  * documentation](https://doc.ubuntu-fr.org/cron)). The timezone is UTC+0. - `args`: JSON object passed to your
  * container. You can use this property to define data that will be passed to your container's `event.body` object. For
  * Containers, you might handle these arguments as the HTTP request's body. Under the hood, CRON Triggers are
  * [Kubernetes JOBs](https://kubernetes.io/docs/concepts/workloads/controllers/jobs-run-to-completion/) sending HTTP
  * POST requests to your container. ### Authentication By default new containers are `public` meaning that no
  * credentials are required to invoke them. A container can be `private` or `public`. This can be configured through
  * the `privacy` parameter. Calling a `private` container without authentication will return HTTP code `403`. ### Logs
  * Containers logs are sent to the project's
  * [Cockpit](https://www.scaleway.com/en/developers/api/cockpit/regional-api/). The **Serverless Containers Logs**
  * dashboard in Grafana can be used to see containers logs. More complex queries can be done using the \"Explore\"
  * section of Grafana, and LogQL queries: ```logql {resource_type=\"serverless_container\"} ``` Additionally, the loki
  * endpoint (`https://logs.cockpit.fr-par.scw.cloud`) can be used to query programmatically the containers logs using a
  * [token](https://www.scaleway.com/en/docs/observability/cockpit/how-to/create-token/). ## Going further For more help
  * using Scaleway Serverless containers, check out the following resources: * Our [main
  * documentation](https://www.scaleway.com/en/docs/serverless/containers/) * The #serverless-containers channel on our
  * [Slack Community](https://www.scaleway.com/en/docs/tutorials/scaleway-slack-community/) * Our [support ticketing
  * system](https://www.scaleway.com/en/docs/console/account/how-to/open-a-support-ticket/).
  *
  * The version of the OpenAPI document: v1beta1
  *
  * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
  * https://openapi-generator.tech Do not edit the class manually.
  */
package scaleway.containers.models

import com.github.plokhotnyuk.jsoniter_scala.macros.named

case class UpdateTriggerRequest(
    /* Name of the trigger. */
    @named("name") name: Option[String] = scala.None,
    /* Description of the trigger. */
    @named("description") description: Option[String] = scala.None
)
