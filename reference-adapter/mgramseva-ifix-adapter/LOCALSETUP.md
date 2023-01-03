# Local Setup

To setup the reference adapter service in your local system, clone the [ifix Service repository](https://github.com/egovernments/iFix-Dev).

## Dependencies

### Infra Dependency

- [x] Postgres DB
- [x] Redis
- [ ] Elasticsearch
- [x] Kafka
  - [x] Consumer
  - [x] Producer

## Running Locally

 
 

Update below listed properties in `application.properties` before running the project:

```ini

-spring.datasource.url=jdbc:postgresql://localhost:5432/{local postgres db name}

 
-keycloak.host={keycloak hostname}

-ifix.host = {ifix fiscal event  service hostname}


```
