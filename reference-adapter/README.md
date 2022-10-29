# Ifix Refernce Adatpter Service
Ifix-Adapter is a system which works as mediator between ifix and its clients. This system will receive requests from client system and convert the data in the Ifix required format This  document contains the details about how to setup ifix-adapter service and describes the functionalities it provides   
### DB UML Diagram

- NA

### Service Dependencies
- mgramseva-ifix-adapter
- IFIX- fiscal-event-service
- IFIX-keycloak
 


### Swagger API Contract
- Please refer to the [Swagger API contarct](https://github.com/egovernments/iFix-Dev/blob/adapter-dev/reference-adapter/iFix-adapter-v1.0.yaml ) for Refernce Adatpter service to understand the structure of APIs and to have visualization of all internal APIs.


## Service Details
**Details of all the entities involved:**

**a) EventRequest:** The top level wrapper object containing the Service 

**b) Event:** The Event object contains the details of the data sent from client.

**c) Entity:** The Entity object contains the  the actual data like demand ,collection,payment details from client system

**d) FiscalEvent:** FiscalEvent is the one which is posted to IFIX





**Notification:**
- 


### Configurable properties

| Environment Variables                     | Description                                                                                                                                               | Value                                             |
| ----------------------------------------- | ----------------------------------------------------------------------------------------------------------------------------------------------------------|---------------------------------------------------|
| `state.goverment.code`                  | The top level tenant    id                                                                                        | pb                                         |
                                                                                                             | 
### API Details

`BasePath` /ifix-reference-adapter/v1/[API endpoint]

##### Method
**a) push request  `POST /_push` :** API to push data to ifix-adapter

### Kafka Consumers

- EventTypeConsumer will consume the data put on ifix-adapter and push to IFIX

### Kafka Producers

- Following are the Producer topic.
    - **ifix-adapter** :- This topic is used to recevice all data sent by client like mgramseva

