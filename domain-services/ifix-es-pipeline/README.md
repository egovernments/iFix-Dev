# iFIX-ES-Pipeline Kafka Pipeline service

### Setting up ES-Mapping
The default mapping created by ES will not read time-related attributes as date but will classify them as long. To 
avoid this we need to first create the index and set the mapping before pushing any data to the index(kafka-topic). 
In case the index has already been created, we will have to delete the index and follow the process to set the 
mapping and then reimport the data to ES from the database.

Use the DevTools tab of Kibana to execute the following commands:
1. Create the index using the following command:

    ```PUT /ifix-fiscal-events```
2. Create the mapping with the following command. Replace the json after the first line from the content of the 
   [es-mapping.json](./es-mapping.json) file

    ```
    PUT /ifix-fiscal-events/fiscalevent/_mapping
   {
        ...
   }
    ```

### DB UML Diagram
- NA

### Service Dependencies
- NA

### Swagger API Contract

- NA

### Functionalities



### API Details

- NA

### Kafka Consumers

- Kafka consumer topic - 

### Kafka Producers

