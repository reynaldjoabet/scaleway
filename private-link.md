# Private Link
Private Link is a cloud networking technology (offered as AWS PrivateLink, Azure Private Link, and Google Cloud Private Service Connect) that provides secure, private connectivity between your virtual network (VPC/VNet) and cloud services.

Instead of sending traffic over the public internet to reach a cloud provider's managed services (like databases, storage, or third-party SaaS apps), Private Link keeps all traffic entirely on the cloud provider's global backbone network.

![alt text](image-19.png)
There is no need for gateways, network address translation (NAT) devices, or public IP addresses to communicate with the service.
## Problem it solves
Historically, if a virtual machine inside your private cloud network needed to access a managed cloud service (like an Amazon S3 bucket or an Azure SQL database), that managed service lived on a public IP address.

To reach it, your traffic had to leave your private network, go through an Internet Gateway or NAT device, and travel over the public internet to reach the service. 

### How Private Link Works

Private Link fundamentally flips this model. Instead of reaching out to a public service, it brings the service into your private network.

- Creation of a Private Endpoint: You create a "Private Endpoint" inside your virtual network. This endpoint is essentially a virtual network interface (ENI/NIC) that is assigned a private IP address directly from your own subnet (e.g., 10.1.1.5 in the diagram above).

- Mapping the Service: This private IP is securely mapped on the backend to the specific target service you want to access (like a specific storage bucket or database instance).

- Private Routing: When your applications send data to that private IP address, the cloud provider's underlying software-defined network intercepts the traffic.

- Backbone Transit: The traffic is routed directly to the destination service using the cloud provider's private fiber-optic backbone. It never touches the public internet.


### Private connectivity to your own service

> Quoted from the Azure Private Link GA announcement (2019) — "today" means 2019, and Azure Active Directory has since been renamed Microsoft Entra ID.

This new offering is not limited to Azure PaaS services, you can leverage it for your own service as well. Today, as a service provider in Azure, you have to make your service accessible over a public interface (IP address) in order for it to be accessible for other consumers running in Azure. You could use VNet peering and connect to the consumer’s VNet to make it private, but it is not scalable and will soon run into IP address conflicts. With today’s announcement, you can run your service completely private in your own VNet behind an Azure Standard Load Balancer, enable it for Azure Private Link, and allow it to be accessed by consumers running in different VNet, subscription, or Azure Active Directory (AD) tenant all using simple clicks and approval call flow. As a service consumer all you will have to do is create a private endpoint in your own VNet and consume the Azure Private Link service completely private without opening your access control lists (ACLs) to any public IP address space.

![alt text](image-20.png)
## DNS Zones
In DNS, a zone is the set of records for a particular suffix, held by whoever is authoritative for it. Microsoft owns and publishes the zone `blob.core.windows.net`, and inside it there's a record for `mystore`. Anyone on the internet can query it and get an answer. (`mystore.blob.core.windows.net`)

To make this seamless for applications, Private Link relies heavily on DNS.
If your application usually calls auth.identity-server.com, Private Link intercepts that DNS request using a Private DNS Zone. Instead of resolving to the public IP of the identity server, the DNS resolves locally to the private IP of your injected Endpoint (10.0.1.45).

### Why it has to be named exactly privatelink.blob.core.windows.net

This is the part that feels arbitrary but isn't. Once you attach a private endpoint, Azure rewrites the *public* answer for `mystore.blob.core.windows.net` into a CNAME pointing at `mystore.privatelink.blob.core.windows.net`. That name is now the thing being resolved, and the resolver will keep chasing the chain.

So your Private DNS Zone has to be named `privatelink.blob.core.windows.net` exactly, because that is the suffix the resolver is now asking about. Name the zone anything else and it never gets consulted — the chain walks past it to public DNS and you get the public IP back. You are not overriding `blob.core.windows.net`; you are becoming authoritative for the private suffix Microsoft deliberately redirected you to.

The chain resolves from anywhere on the internet, by design, so hybrid clients don't break. A successful lookup proves only that the resource name exists — it grants no access. If **Public network access** is Disabled, the service rejects the connection regardless of what DNS returned.


#### Storage

| Sub-resource | Zone name |
|---|---|
| blob, blob_secondary | `privatelink.blob.core.windows.net` |
| table, table_secondary | `privatelink.table.core.windows.net` |
| queue, queue_secondary | `privatelink.queue.core.windows.net` |
| file | `privatelink.file.core.windows.net` |
| web, web_secondary | `privatelink.web.core.windows.net` |
| dfs, dfs_secondary (Data Lake Gen2) | `privatelink.dfs.core.windows.net` |
| afs (File Sync) | `privatelink.afs.azure.net` |
| disks (Managed Disks) | `privatelink.blob.core.windows.net` |
| volumegroup (Elastic SAN) | `privatelink.blob.core.windows.net` |

#### Databases

| Service / sub-resource | Zone name |
|---|---|
| SQL Database (`sqlServer`) | `privatelink.database.windows.net` |
| SQL Managed Instance | `privatelink.{dnsPrefix}.database.windows.net` |
| Cosmos DB (`Sql`) | `privatelink.documents.azure.com` |
| Cosmos DB (`MongoDB`) | `privatelink.mongo.cosmos.azure.com` |
| Cosmos DB (`Cassandra`) | `privatelink.cassandra.cosmos.azure.com` |
| Cosmos DB (`Gremlin`) | `privatelink.gremlin.cosmos.azure.com` |
| Cosmos DB (`Table`) | `privatelink.table.cosmos.azure.com` |
| Cosmos DB (`Analytical`) | `privatelink.analytics.cosmos.azure.com` |
| Cosmos DB for PostgreSQL (`coordinator`) | `privatelink.postgres.cosmos.azure.com` |
| Cosmos DB for MongoDB vCore | `privatelink.mongocluster.cosmos.azure.com` |
| PostgreSQL single + flexible | `privatelink.postgres.database.azure.com` |
| MySQL single + flexible | `privatelink.mysql.database.azure.com` |
| MariaDB (service retired 19 Sept 2025; zone still listed in the docs) | `privatelink.mariadb.database.azure.com` |
| Cache for Redis | `privatelink.redis.cache.windows.net` |
| Redis Enterprise | `privatelink.redisenterprise.cache.azure.net` |
| Azure Managed Redis | `privatelink.redis.azure.net` |

#### Security

| Service / sub-resource | Zone name |
|---|---|
| Key Vault (`vault`) | `privatelink.vaultcore.azure.net` |
| Key Vault Managed HSM | `privatelink.managedhsm.azure.net` |
| App Configuration | `privatelink.azconfig.io` |
| Attestation | `privatelink.attest.azure.net` |


#### Web

| Service / sub-resource | Zone name |
|---|---|
| Web Apps / Function Apps | `privatelink.azurewebsites.net`, `scm.privatelink.azurewebsites.net` |
| Static Web Apps | `privatelink.azurestaticapps.net`, `privatelink.{partitionId}.azurestaticapps.net` |
| AI Search | `privatelink.search.windows.net` |
| Relay | `privatelink.servicebus.windows.net` |
| SignalR | `privatelink.service.signalr.net` |
| Web PubSub | `privatelink.webpubsub.azure.com` |
| Maps | `privatelink.account.maps.azure.com` |


A private endpoint on the App Service itself is inbound: it gives your app a private IP so that only clients inside your network can reach the site. It says nothing about what your app can reach.

Your app reaching other resources' private endpoints — storage, SQL, Key Vault — is outbound, and that needs regional VNet integration. An app with a private endpoint but no VNet integration still calls storage over the public internet

Even if `Service B` has VNet Integration and can reach the network, it is probably still trying to look up the public IP address of Service A (e.g., `serviceA.azurewebsites.net`).

`Service B `needs to know to look up the private IP address. You must ensure that an Azure Private DNS Zone (specifically `privatelink.azurewebsites.net`) is created, contains the A-record for Service A's private IP, and—most importantly—is Linked to the VNet that Service B is sitting in.


Because a multi-tenant App Service lives in Microsoft's network, it is completely disconnected from your private VNet (e.g., 10.0.0.0/16) by default. (An App Service Environment is the exception — an ASE is deployed directly into a subnet of your own VNet, so it needs neither of the features below to reach VNet resources.)This physical separation is exactly why the two networking features we discussed exist:
- `Private Endpoints` (For Inbound): Because the App Service is in Microsoft's network, it has a public endpoint by default. To make it private, Azure takes a Private Endpoint (a virtual network interface) and drops it into your VNet. It acts as a secure, one-way tunnel from your VNet into Microsoft's managed network where your app is running. 
- `VNet Integration` (For Outbound): When your App Service needs to talk to a database inside your VNet, it can't just reach out because it doesn't live there. VNet Integration creates a bridge from Microsoft's managed network into your VNet. Azure essentially mounts a virtual interface on the Microsoft worker nodes, giving them a temporary IP address from your subnet so they can route traffic inside.


`Any time you use an Azure service where you deploy code or logic, but Microsoft manages the underlying servers, you will rely on VNet Integration to allow that code to securely send traffic out into your private network.`

Because they control different directions of traffic, you can configure your App Service in one of three distinct ways:

### Private Endpoint ONLY (Inbound Private, Outbound Public)

You create a Private Endpoint but skip VNet Integration.
Users or services can only reach your App Service if they are inside your private network. However, when your App Service reaches out to the internet (e.g., to call a public API like Stripe, or a public Azure Storage URL), that traffic goes out over the normal public internet.


### VNet Integration ONLY (Inbound Public, Outbound Private)

You set up VNet Integration but do not create a Private Endpoint.
Anyone on the internet can access your App Service via its public URL. But when your App Service needs data, it routes that request through the virtual cable into your VNet to talk to a hidden, secure resource (like a private PostgreSQL database).
- When to use it: You are building a public-facing customer portal that requires a high-security backend database that cannot be exposed to the internet.

`VNet Integration is required anytime your App Service needs to initiate an outbound connection to ANY resource that is hidden inside a private network.`

### Private Endpoint + VNet Integration (Inbound Private, Outbound Private)
You set up both a Private Endpoint and VNet Integration on the same App Service.
The App Service is completely dark to the outside world. It can only receive traffic from inside your VNet (via the Private Endpoint), and it can only talk to things inside your VNet (via VNet Integration).
- When to use it: This is the standard for enterprise microservices. For example, Service A (an internal app) calls Service B via its Private Endpoint. Service B then uses VNet Integration to query an internal database.





It depends entirely on how your App Service Plans and Virtual Networks (VNets) are structured, rather than the Resource Groups themselves.

Because App Services are tied to App Service Plans (the underlying servers that run your code), the networking rules apply at the Plan and VNet level.