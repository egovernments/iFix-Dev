# Local Setup Guide - Digit Exchange Service

This guide provides configuration for running two instances of the Digit Exchange Service locally:
1. **HUDD Digit Exchange Service** - Running on port 8084
2. **IFMS Digit Exchange Service** - Running on port 8085

## Prerequisites
- PostgreSQL running on localhost:5432
- Kafka running on localhost:9092
- Required databases created
- Program services running (HUDD on 8082, IFMS on 8083)

## Configuration Files

### 1. HUDD Digit Exchange Service (application-hudd.properties)

Create `src/main/resources/application-hudd.properties`:

```properties
server.port=8084
server.servlet.contextPath=/mukta/digit-exchange
app.timezone=UTC

#My domain
app.name=digit-exchange-hudd
service.host.url=http://localhost:8084/mukta/digit-exchange

# Routing - Points to HUDD Program Service
app.receiver.endpoints={"program":"http://localhost:8082/mukta/program-service/v1/"}

#Logging
#logging.level.root=info
#logging.level.org.springframework.context.annotation = DEBUG
#logging.level.org.springframework.web=DEBUG
#logging.level.org.springframework.data=DEBUG
#logging.level.com.fasterxml.jackson=DEBUG

# DataSource settings
spring.datasource.driver-class-name=org.postgresql.Driver
spring.datasource.url=jdbc:postgresql://localhost:5432/digit-works-hudd
spring.datasource.username=postgres
spring.datasource.password=postgres

#FLYWAY CONFIGURATION
spring.flyway.url=jdbc:postgresql://localhost:5432/digit-works-hudd
spring.flyway.user=postgres
spring.flyway.password=postgres
spring.flyway.table=exchange_service_schema_hudd
spring.flyway.baseline-on-migrate=true
spring.flyway.outOfOrder=true
spring.flyway.locations=classpath:/db/migration/main
spring.flyway.enabled=false

# Kafka configuration
spring.kafka.bootstrap-servers=localhost:9092
spring.kafka.consumer.group-id=digit-exchange-hudd
# other Kafka properties
app.exchange.topic.name=exchange-topic-hudd
app.error.topic.name=exchange-error-queue-hudd
app.events.log.topic.name=exchange-event-log-queue-hudd
app.enable.events.log=true
spring.kafka.producer.key-serializer=org.apache.kafka.common.serialization.StringSerializer
spring.kafka.producer.value-serializer=org.springframework.kafka.support.serializer.JsonSerializer
spring.kafka.consumer.properties.spring.deserializer.value.delegate.class=org.springframework.kafka.support.serializer.JsonDeserializer
spring.kafka.consumer.key-deserializer=org.apache.kafka.common.serialization.StringDeserializer
spring.kafka.listener.missing-topics-fatal=false
spring.kafka.consumer.properties.spring.json.use.type.headers=false

# authentication
app.auth.sign.validation=false
app.auth.private-key=-----BEGIN PRIVATE KEY-----MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQDrD9z2aCCj1FPsp0c2NbijCcXWFXBu6G+VBHAZLXZjW7uf72xGNgQAgigvJZkz7wqj+B8NIv9vrAAJEnPjUXsATBAgf3GIkKJY+lflT0Cz0TVO8nfzKSXWYKFhyzgA8+185vvFllediuQUCSBnaVy+bYHlup4dZOLuqx/l2NW+kqVoQXDpwigdfAD6gpFvytSrhS10Dcep79Z9n5ajMHRVy83B+YnF5rQWElJIcAdaqFdwE/4xig0r7eueGNvCzEK23R9TvFITPO7HVzbpQ+l4jBM1YiYS5XlgjyHOzE+BW11yyhGp51Jiu0d4ILn6/owHWrvCXDqJK9L096XNm+bHAgMBAAECggEABIDLy16oTWsqZmy39QZncJwl0Zmci0tho9mvahQQYgvmVsSH7vpm7jmQGoMeYbvRGN4Ofjpu6U1CvOnBFZhAgntyjaTTYHng2lKFb4uqoic+XbJQcPSXWHmGbfCuNHp30L4+EYV1TTvVbbrVB5YhpDZF2EhQciC2JjtaA5W5VorAQNt6vcNZ2+CotRRdxwN9ksQfAsVRM17SzeZYyB8ZhObYS8sEndliuMmzdKaIHoAqQsyExE41fY3MuV+G7vOLzzk5mz6hoAoY22u3owwdoJZvRXfGh3yFY7syLdO1ejEuctnqsH5aPlePNq2ZEkrf4Q/aYNzt3IXERrfi6l5ZCQKBgQD6Ok0icgSdBRyqSRjXkzI9RVQ1vXc2kwYqOjwMQ0HDVRzj7SXuG/xr7wAqoYHgdO52lA8pneJ2Rsz+GCJv2t6oZocsaYlAepD1jEoutqhOgsbCqu8n8M2RiOwhhY6DhwfHUbI3mqhmJDqUa27cxaRlnRZ0lzf0UtYLaAhUWe+NyQKBgQDwfABrKyf0q5Yr0yTWBldtPa1KuUYiBrKVYW8GJnS6lffEsWKqEOK5FFPdJ+bBpywCtvQY0XEwYVkl+BjRjlEn+KaGcdkslCP90Jk0FbQ98FrN2+YYsGhExUScxt5O51TH+hK+9XGLWcORdYoNWhDP3w5BhGNwOeCTjvDR9enYDwKBgQDPV7oMvc/G0LKVA+b60rTBgo4pzapX5XIpUYHqY1y93+wFjb9tU3FReoR3zsaQ2DO0vDDoOjSp7zbzocn/R6xSfEqr6XNao3U0kp/xn1dNXx4VtpBfVzDv1DZd/I4/vIcciTyUyKmboY+M5ozBkWAM9yLhT2CFKpLEdtrYEoHmyQKBgEGdA5w53R/zhPiWFlHSzx2+Gz7tCAaWnzkEqX8bFPQnoL9oyouuKb3nnu4TSfGCRA7FjhoLYv+ZP/KNG3BpjRkDzaOXAnMYRC2nMXW1ikTwBEZwffo0f25sCEliNRuAIHCjeWPiocUsi6BvWmhEL5wXttMHZP4kvHJmKKyQlOnfAoGBAKuBIACao1N5UGL78HTgrG7bDPq+DFpvmtrnJdhi+BkOdfb8ykvsiyt0/XdXzMjHPzHnhQPgDSenT/kK4f1JelJEw7wya0KF2fum3aM6HgbIqpzni1iOKWwDTfmyiPY3wWg3yFMBvOHejAHkP0XbVL4KVa0q9j+xWyeXbShuyLYj-----END PRIVATE KEY-----
app.auth.public-key=-----BEGIN PUBLIC KEY-----MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA6w/c9mggo9RT7KdHNjW4ownF1hVwbuhvlQRwGS12Y1u7n+9sRjYEAIIoLyWZM+8Ko/gfDSL/b6wACRJz41F7AEwQIH9xiJCiWPpX5U9As9E1TvJ38ykl1mChYcs4APPtfOb7xZZXnYrkFAkgZ2lcvm2B5bqeHWTi7qsf5djVvpKlaEFw6cIoHXwA+oKRb8rUq4UtdA3Hqe/WfZ+WozB0VcvNwfmJxea0FhJSSHAHWqhXcBP+MYoNK+3rnhjbwsxCtt0fU7xSEzzux1c26UPpeIwTNWImEuV5YI8hzsxPgVtdcsoRqedSYrtHeCC5+v6MB1q7wlw6iSvS9PelzZvmxwIDAQAB-----END PUBLIC KEY-----
```

### 2. IFMS Digit Exchange Service (application-ifms.properties)

Create `src/main/resources/application-ifms.properties`:

```properties
server.port=8085
server.servlet.contextPath=/ifms/digit-exchange
app.timezone=UTC

#My domain
app.name=digit-exchange-ifms
service.host.url=http://localhost:8085/ifms/digit-exchange

# Routing - Points to IFMS Program Service
app.receiver.endpoints={"program":"http://localhost:8083/ifms/program-service/v1/"}

#Logging
#logging.level.root=info
#logging.level.org.springframework.context.annotation = DEBUG
#logging.level.org.springframework.web=DEBUG
#logging.level.org.springframework.data=DEBUG
#logging.level.com.fasterxml.jackson=DEBUG

# DataSource settings
spring.datasource.driver-class-name=org.postgresql.Driver
spring.datasource.url=jdbc:postgresql://localhost:5432/digit-works-ifms
spring.datasource.username=postgres
spring.datasource.password=postgres

#FLYWAY CONFIGURATION
spring.flyway.url=jdbc:postgresql://localhost:5432/digit-works-ifms
spring.flyway.user=postgres
spring.flyway.password=postgres
spring.flyway.table=exchange_service_schema_ifms
spring.flyway.baseline-on-migrate=true
spring.flyway.outOfOrder=true
spring.flyway.locations=classpath:/db/migration/main
spring.flyway.enabled=false

# Kafka configuration
spring.kafka.bootstrap-servers=localhost:9092
spring.kafka.consumer.group-id=digit-exchange-ifms
# other Kafka properties
app.exchange.topic.name=exchange-topic-ifms
app.error.topic.name=exchange-error-queue-ifms
app.events.log.topic.name=exchange-event-log-queue-ifms
app.enable.events.log=true
spring.kafka.producer.key-serializer=org.apache.kafka.common.serialization.StringSerializer
spring.kafka.producer.value-serializer=org.springframework.kafka.support.serializer.JsonSerializer
spring.kafka.consumer.properties.spring.deserializer.value.delegate.class=org.springframework.kafka.support.serializer.JsonDeserializer
spring.kafka.consumer.key-deserializer=org.apache.kafka.common.serialization.StringDeserializer
spring.kafka.listener.missing-topics-fatal=false
spring.kafka.consumer.properties.spring.json.use.type.headers=false

# authentication
app.auth.sign.validation=false
app.auth.private-key=-----BEGIN PRIVATE KEY-----MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQDrD9z2aCCj1FPsp0c2NbijCcXWFXBu6G+VBHAZLXZjW7uf72xGNgQAgigvJZkz7wqj+B8NIv9vrAAJEnPjUXsATBAgf3GIkKJY+lflT0Cz0TVO8nfzKSXWYKFhyzgA8+185vvFllediuQUCSBnaVy+bYHlup4dZOLuqx/l2NW+kqVoQXDpwigdfAD6gpFvytSrhS10Dcep79Z9n5ajMHRVy83B+YnF5rQWElJIcAdaqFdwE/4xig0r7eueGNvCzEK23R9TvFITPO7HVzbpQ+l4jBM1YiYS5XlgjyHOzE+BW11yyhGp51Jiu0d4ILn6/owHWrvCXDqJK9L096XNm+bHAgMBAAECggEABIDLy16oTWsqZmy39QZncJwl0Zmci0tho9mvahQQYgvmVsSH7vpm7jmQGoMeYbvRGN4Ofjpu6U1CvOnBFZhAgntyjaTTYHng2lKFb4uqoic+XbJQcPSXWHmGbfCuNHp30L4+EYV1TTvVbbrVB5YhpDZF2EhQciC2JjtaA5W5VorAQNt6vcNZ2+CotRRdxwN9ksQfAsVRM17SzeZYyB8ZhObYS8sEndliuMmzdKaIHoAqQsyExE41fY3MuV+G7vOLzzk5mz6hoAoY22u3owwdoJZvRXfGh3yFY7syLdO1ejEuctnqsH5aPlePNq2ZEkrf4Q/aYNzt3IXERrfi6l5ZCQKBgQD6Ok0icgSdBRyqSRjXkzI9RVQ1vXc2kwYqOjwMQ0HDVRzj7SXuG/xr7wAqoYHgdO52lA8pneJ2Rsz+GCJv2t6oZocsaYlAepD1jEoutqhOgsbCqu8n8M2RiOwhhY6DhwfHUbI3mqhmJDqUa27cxaRlnRZ0lzf0UtYLaAhUWe+NyQKBgQDwfABrKyf0q5Yr0yTWBldtPa1KuUYiBrKVYW8GJnS6lffEsWKqEOK5FFPdJ+bBpywCtvQY0XEwYVkl+BjRjlEn+KaGcdkslCP90Jk0FbQ98FrN2+YYsGhExUScxt5O51TH+hK+9XGLWcORdYoNWhDP3w5BhGNwOeCTjvDR9enYDwKBgQDPV7oMvc/G0LKVA+b60rTBgo4pzapX5XIpUYHqY1y93+wFjb9tU3FReoR3zsaQ2DO0vDDoOjSp7zbzocn/R6xSfEqr6XNao3U0kp/xn1dNXx4VtpBfVzDv1DZd/I4/vIcciTyUyKmboY+M5ozBkWAM9yLhT2CFKpLEdtrYEoHmyQKBgEGdA5w53R/zhPiWFlHSzx2+Gz7tCAaWnzkEqX8bFPQnoL9oyouuKb3nnu4TSfGCRA7FjhoLYv+ZP/KNG3BpjRkDzaOXAnMYRC2nMXW1ikTwBEZwffo0f25sCEliNRuAIHCjeWPiocUsi6BvWmhEL5wXttMHZP4kvHJmKKyQlOnfAoGBAKuBIACao1N5UGL78HTgrG7bDPq+DFpvmtrnJdhi+BkOdfb8ykvsiyt0/XdXzMjHPzHnhQPgDSenT/kK4f1JelJEw7wya0KF2fum3aM6HgbIqpzni1iOKWwDTfmyiPY3wWg3yFMBvOHejAHkP0XbVL4KVa0q9j+xWyeXbShuyLYj-----END PRIVATE KEY-----
app.auth.public-key=-----BEGIN PUBLIC KEY-----MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA6w/c9mggo9RT7KdHNjW4ownF1hVwbuhvlQRwGS12Y1u7n+9sRjYEAIIoLyWZM+8Ko/gfDSL/b6wACRJz41F7AEwQIH9xiJCiWPpX5U9As9E1TvJ38ykl1mChYcs4APPtfOb7xZZXnYrkFAkgZ2lcvm2B5bqeHWTi7qsf5djVvpKlaEFw6cIoHXwA+oKRb8rUq4UtdA3Hqe/WfZ+WozB0VcvNwfmJxea0FhJSSHAHWqhXcBP+MYoNK+3rnhjbwsxCtt0fU7xSEzzux1c26UPpeIwTNWImEuV5YI8hzsxPgVtdcsoRqedSYrtHeCC5+v6MB1q7wlw6iSvS9PelzZvmxwIDAQAB-----END PUBLIC KEY-----
```

## Running the Services

### Option 1: Using Spring Profiles

1. **Start HUDD Digit Exchange Service:**
   ```bash
   mvn spring-boot:run -Dspring-boot.run.profiles=hudd
   ```

2. **Start IFMS Digit Exchange Service:**
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
   java -jar target/digit-exchange-*.jar --spring.profiles.active=hudd
   ```

3. **Run IFMS instance:**
   ```bash
   java -jar target/digit-exchange-*.jar --spring.profiles.active=ifms
   ```

## Database Setup

Ensure the same databases used by program services are available:

```sql
-- HUDD Database (shared with HUDD Program Service)
CREATE DATABASE "digit-works-hudd";

-- IFMS Database (shared with IFMS Program Service)
CREATE DATABASE "digit-works-ifms";
```

## Service Endpoints

- **HUDD Digit Exchange Service:** http://localhost:8084/mukta/digit-exchange/
- **IFMS Digit Exchange Service:** http://localhost:8085/ifms/digit-exchange/

## Service Dependencies

Each Digit Exchange service routes to its corresponding Program Service:

| Exchange Service | Port | Routes To | Program Service Port |
|------------------|------|-----------|---------------------|
| HUDD Exchange | 8084 | HUDD Program Service | 8082 |
| IFMS Exchange | 8085 | IFMS Program Service | 8083 |

## Key Differences Between Instances

| Configuration | HUDD | IFMS |
|--------------|------|------|
| Port | 8084 | 8085 |
| Context Path | /mukta/digit-exchange | /ifms/digit-exchange |
| App Name | digit-exchange-hudd | digit-exchange-ifms |
| Database | digit-works-hudd | digit-works-ifms |
| Kafka Group ID | digit-exchange-hudd | digit-exchange-ifms |
| Kafka Topics | *-hudd suffix | *-ifms suffix |
| Program Service URL | http://localhost:8082/mukta/program-service/v1/ | http://localhost:8083/ifms/program-service/v1/ |
| Flyway Table | exchange_service_schema_hudd | exchange_service_schema_ifms |

## Startup Order

1. **Start PostgreSQL and Kafka**
2. **Start Program Services first:**
   - HUDD Program Service (port 8082)
   - IFMS Program Service (port 8083)
3. **Then start Digit Exchange Services:**
   - HUDD Digit Exchange Service (port 8084)
   - IFMS Digit Exchange Service (port 8085)

## Troubleshooting

1. **Port conflicts:** Ensure ports 8084 and 8085 are available
2. **Database connections:** Verify PostgreSQL is running and databases exist
3. **Kafka issues:** Check if Kafka is running on localhost:9092
4. **Program Service dependency:** Ensure respective program services are running before starting exchange services
5. **Profile loading:** Ensure the correct profile is specified when starting the service
6. **Routing errors:** Verify the program service endpoints are correct and accessible

## Testing the Setup

Test each instance by calling their health endpoints:
- HUDD: `curl http://localhost:8084/mukta/digit-exchange/actuator/health`
- IFMS: `curl http://localhost:8085/ifms/digit-exchange/actuator/health`