# Spark Aggregation Framework

This is an Apache Spark based data aggregation framework to aggregate data at hourly and daily levels. 
The framework is designed to run on Databricks clusters as jar.
The framework reads necessary configuratons from application.conf file which is passed as an argument.

The framework supports follwing operation modes
	- `delta` = the aggregation is resume from last aggregated partition.
	- `adhoc` = the aggregation is performed on the specified date and hours.

The framework supports follwing processing modes
	- `singleStage` = the aggregation is performed through a single query without any intermediate data staging.
	- `multiStage` = the aggregation is performed through multiple queries with any intermediate data staging.
	
The framework supports follwing aggregation modules
	- `hourly` = the aggregation will be done at hourly level and aggregation boundary will contain date,start_hour,end_hour.
	- `daily` = the aggregation will be done at daily level and aggregation boundary will contain date,start_hour,end_hour.

The application.conf file must define the output `dailyPartitionName`. If config file contains `hourlyPartitionName` then hourly partitions are created within daily partitions.

### Supported Sources
Azure Storage Account(BLOB,ADLS),Databricks File System(DBFS)

### Supported Sinks
Azure Storage Account(BLOB,ADLS),Databricks File System(DBFS)

### Supported Output Formats
delta, parquet

### Main Classes
All main and utility classes are located under package `com.tmobile.qtm.agg`

| Class | Purpose | 
| --- | --- |
| `AggregationApp` | Invokes the spark aggregation application | 



### Configuration
The application supports numerous configurations. Please refer to the sample `application.conf.template` file for detailed list of configurations and their usage.



| Config Name | Description | Optional |
| --- | --- | --- |
|`spark.applicationName `| Spark application name| No |
|`spark.master` | Spark master | No |
|`spark.serializer` | Spark serializer class to be used. Defaulted to `org.apache.spark.serializer.KryoSerializer` | Yes |
|`spark.logLevel` | Spark log level | No |
|`storageAccount.url` | Storage account root URL. Format `abfss://{root_container}@{storage_account_name}.dfs.core.windows.net` | No |
|`storageAccount.names` | Comma separated list of storage accounts. Format - `["storageAccount1,storageAccount2"]` | No |
|`storageAccount.oauth2.enable`| Flag which enables oauth2 authentication | No |
|`storageAccount.clientId` | Stroage acccount clientId/servicePrincipalId | No |
|`storageAccount.tenantId` | Storage account tenantId | No |
|`storageAccount.secretScope` | Secret scope to access service principal access key | No |
|`storageAccount.secretKey ` | Secret key to access service principal access key | No |



### Execution

The databricks job should be configured to run as jar and the application config file should be passed as an argument.

- Task
  - Type: JAR
  - Main Class: com.tmobile.qtm.agg.AggregationApp
  - Parameters: ["/dbfs/FileStore/tables/conf/application.conf"]