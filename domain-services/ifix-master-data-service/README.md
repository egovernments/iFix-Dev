# iFIX-Master-Data-Service

## [API-Contract Link](https://redocly.github.io/redoc/?url=https://raw.githubusercontent.com/egovernments/iFix-Dev/develop/domain-services/ifix-master-data-service/ifix-master-data-service-0.1.0.yaml)
## [Postman Collection Link](https://www.getpostman.com/collections/6a3b9e0f07d03934725a)

## Connect to MongoDB through Playground pod
Check the correct running mongodb pod and execute the below command
```
kubectl exec -it <playground_pod_name> -- /bin/bash
```

For connecting to particular running mongodb, run the below command
```
mongo --host <mongodb_url:port> -u <username> -p <password>
```

## MongoDB

Please copy the commands written in the files mentioned below and execute them in Mongo Shell. Also, make sure you 
are using the correct `database name`. You can switch to a particular database by executing the following command in 
Mongo shell
```
use <db-name>
```

### Indexes
When application get started, indexes will be created by mongoCK script.

### Sample data
The commands to insert the sample data are stored in [seed directory](./src/main/resources/db/seed). Please follow 
the order of the list of files while inserting this data. 

We can use the same sample data as an example to insert the actual data for whichever master data the create APIs 
haven't been implemented yet.
