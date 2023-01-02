# Mongo Kafka Source Connector

It pulls data from mongoDb and pushes to kafka topics.
It does not do any validation on data while fetching and pushing to kafka topics.
It fetches all data from DB and pushes a single item at once. Till now, It iterates the whole record and pushes one record at a time without any validation.

### Note:
Before executing this application, make sure that there is no ongoing data getting processed.
All data processing from iFix should be processed before or should stop to do any activity which can lead to data ingestion in Druid.

### Presumptions:
Druid data should be cleaned or handled before pulling data from mongoDB because it could lead to duplicate or spurious data.