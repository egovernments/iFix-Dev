# Fiscal Event Post Processor

We are going to store all the **fiscal-event** in 2 data stores:
1. MongoDB
2. Druid

## Kafka to DataStore Sink

### MongoDB Sink
We can reuse kafka-connect to dump the data from a kafka topic to push it to MongoDB. We will have to follow these steps to start the connector:
1. Connect(port-forward) with the kafka-connect server.
2. We can create a new connector with a POST API call to localhost:8083/connectors.
3. The request body for that API call is written in the file [fiscal-event-mongodb-sink](./fiscal-event-mongodb-sink.json).
4. Within that file, wherever ${---} replace it with the actual value based on the environment.
5. *(Optional)* Verify and make changes to the topic names.  
6. The connector is ready. You can check it using API call GET localhost:8083/connectors. 

### Druid Sink

