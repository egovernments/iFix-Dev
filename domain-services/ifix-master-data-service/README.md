# iFIX-Master-Data-Service

## MongoDB

Please copy the commands written in the files mentioned below and execute them in Mongo Shell. Also, make sure you 
are using the correct `database name`. You can switch to a particular database by executing the following command in 
Mongo shell
```
use <db-name>
```

### Indexes
The list of necessary indexes are stored in the [migrations directory](./src/main/resources/db/migration).

### Sample data
The commands to insert the sample data are stored in [seed directory](./src/main/resources/db/seed). Please follow 
the order of the list of files while inserting this data. 

We can use the same sample data as an example to insert the actual data for whichever master data the create APIs 
haven't been implemented yet.
