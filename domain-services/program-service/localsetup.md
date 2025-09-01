# Local Setup Guide - Program Service

This guide provides configuration for running two instances of the Program Service locally:
1. **HUDD Program Service** - Running on port 8082
2. **IFMS Program Service** - Running on port 8083

## Prerequisites
- PostgreSQL running on localhost:5432
- Kafka running on localhost:9092
- Required databases created

## Configuration Files

### 1. HUDD Program Service (application-hudd.properties)

Create `src/main/resources/application-hudd.properties`:

```properties
server.port=8082
server.servlet.contextPath=/mukta/program-service

# DataSource settings
spring.datasource.driver-class-name=org.postgresql.Driver
spring.datasource.url=jdbc:postgresql://localhost:5432/digit-works-hudd
spring.datasource.username=postgres
spring.datasource.password=postgres

# Management
management.endpoints.web.base-path=/

#FLYWAY CONFIGURATION
spring.flyway.table=program_service_schema_hudd
spring.flyway.baseline-on-migrate=true
spring.flyway.enabled=true

#Configurations
exchange.service.external.url=http://localhost:8084/mukta/digit-exchange

exchange.host=https://unified-dev.digit.org/
exchange.path=mukta/digit-exchange/v1/exchange/

adapter.host=https://unified-dev.digit.org/
adapter.path=ifms/v1/

egov.idgen.host=https://unified-dev.digit.org/
egov.idgen.path=/egov-idgen/id/_generate
egov.idgen.idname=ifix.program.number

egov.mdms.host=https://unified-dev.digit.org/
egov.mdms.path=egov-mdms-service/v1/_search
exchange.url.code=MUKTA

encryption.vector=reYAfdVWwyiZwVMWKlpc3VerPmCxc99dyDuubeEKXEc
encryption.key=-----BEGIN PRIVATE KEY-----770A8A65DA156D24EE2A093277530142-----END PRIVATE KEY-----

is.program.async=true
is.sanction.async=false
is.allocation.async=false
is.disburse.async=true

spring.kafka.producer.key-serializer=org.apache.kafka.common.serialization.StringSerializer
spring.kafka.producer.value-serializer=org.springframework.kafka.support.serializer.JsonSerializer
spring.kafka.consumer.properties.spring.deserializer.value.delegate.class=org.springframework.kafka.support.serializer.JsonDeserializer
spring.kafka.consumer.key-deserializer=org.apache.kafka.common.serialization.StringDeserializer
spring.kafka.listener.missing-topics-fatal=false
spring.kafka.consumer.properties.spring.json.use.type.headers=false
spring.kafka.bootstrap-servers=localhost:9092
spring.kafka.consumer.group-id=program-service-hudd

program.kafka.topic=program-kafka-topic-hudd
sanction.kafka.topic=sanction-kafka-topic-hudd
allocation.kafka.topic=allocation-kafka-topic-hudd
disburse.kafka.topic=disburse-kafka-topic-hudd
error.kafka.topic=error-queue-hudd

search.default.limit=20
search.max.limit=100
```

### 2. IFMS Program Service (application-ifms.properties)

Create `src/main/resources/application-ifms.properties`:

```properties
server.port=8083
server.servlet.contextPath=/ifms/program-service

# DataSource settings
spring.datasource.driver-class-name=org.postgresql.Driver
spring.datasource.url=jdbc:postgresql://localhost:5432/digit-works-ifms
spring.datasource.username=postgres
spring.datasource.password=postgres

# Management
management.endpoints.web.base-path=/

#FLYWAY CONFIGURATION
spring.flyway.table=program_service_schema_ifms
spring.flyway.baseline-on-migrate=true
spring.flyway.enabled=true

#Configurations
exchange.service.external.url=http://localhost:8085/ifms/digit-exchange

exchange.host=https://unified-dev.digit.org/
exchange.path=ifms/digit-exchange/v1/exchange/

adapter.host=https://unified-dev.digit.org/
adapter.path=ifms/v1/

egov.idgen.host=https://unified-dev.digit.org/
egov.idgen.path=/egov-idgen/id/_generate
egov.idgen.idname=ifix.program.number

egov.mdms.host=https://unified-dev.digit.org/
egov.mdms.path=egov-mdms-service/v1/_search
exchange.url.code=IFMS

encryption.vector=reYAfdVWwyiZwVMWKlpc3VerPmCxc99dyDuubeEKXEc
encryption.key=-----BEGIN PRIVATE KEY-----770A8A65DA156D24EE2A093277530142-----END PRIVATE KEY-----

is.program.async=true
is.sanction.async=false
is.allocation.async=false
is.disburse.async=true

spring.kafka.producer.key-serializer=org.apache.kafka.common.serialization.StringSerializer
spring.kafka.producer.value-serializer=org.springframework.kafka.support.serializer.JsonSerializer
spring.kafka.consumer.properties.spring.deserializer.value.delegate.class=org.springframework.kafka.support.serializer.JsonDeserializer
spring.kafka.consumer.key-deserializer=org.apache.kafka.common.serialization.StringDeserializer
spring.kafka.listener.missing-topics-fatal=false
spring.kafka.consumer.properties.spring.json.use.type.headers=false
spring.kafka.bootstrap-servers=localhost:9092
spring.kafka.consumer.group-id=program-service-ifms

program.kafka.topic=program-kafka-topic-ifms
sanction.kafka.topic=sanction-kafka-topic-ifms
allocation.kafka.topic=allocation-kafka-topic-ifms
disburse.kafka.topic=disburse-kafka-topic-ifms
error.kafka.topic=error-queue-ifms

search.default.limit=20
search.max.limit=100
```

## Running the Services

### Option 1: Using Spring Profiles

1. **Start HUDD Program Service:**
   ```bash
   mvn spring-boot:run -Dspring-boot.run.profiles=hudd
   ```

2. **Start IFMS Program Service:**
   ```bash
   mvn spring-boot:run -Dspring-boot.run.profiles=ifms
   ```

### Option 2: Using JAR with Profile

1. **Build the application:**
   ```bash
   mvn clean package
   ```

2. **Run HUDD instance:**
   ```bash
   java -jar target/program-service-*.jar --spring.profiles.active=hudd
   ```

3. **Run IFMS instance:**
   ```bash
   java -jar target/program-service-*.jar --spring.profiles.active=ifms
   ```

## Database Setup

Create separate databases for each instance:

```sql
-- HUDD Database
CREATE DATABASE "digit-works-hudd";

-- IFMS Database
CREATE DATABASE "digit-works-ifms";
```

## Service Endpoints

- **HUDD Program Service:** http://localhost:8082/mukta/program-service/
- **IFMS Program Service:** http://localhost:8083/ifms/program-service/

## Key Differences Between Instances

| Configuration | HUDD | IFMS |
|--------------|------|------|
| Port | 8082 | 8083 |
| Context Path | /mukta/program-service | /ifms/program-service |
| Database | digit-works-hudd | digit-works-ifms |
| Kafka Group ID | program-service-hudd | program-service-ifms |
| Kafka Topics | *-hudd suffix | *-ifms suffix |
| Exchange URL Code | MUKTA | IFMS |
| Flyway Table | program_service_schema_hudd | program_service_schema_ifms |

## Troubleshooting

1. **Port conflicts:** Ensure ports 8082 and 8083 are available
2. **Database connections:** Verify PostgreSQL is running and databases exist
3. **Kafka issues:** Check if Kafka is running on localhost:9092
4. **Profile loading:** Ensure the correct profile is specified when starting the service