package org.egov.ifix.aggregate.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import in.zapr.druid.druidry.client.DruidClient;
import in.zapr.druid.druidry.client.DruidConfiguration;
import in.zapr.druid.druidry.client.DruidJerseyClient;
import in.zapr.druid.druidry.client.DruidQueryProtocol;
import in.zapr.druid.druidry.client.exception.ConnectionException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.TimeZone;


@Configuration
@Slf4j
public class FiscalEventConfiguration {

    @Autowired
    private ConfigProperties configProperties;

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper().disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                .setTimeZone(TimeZone.getTimeZone(configProperties.getTimeZone()));
    }

    @Bean
    public DruidClient getDruidClient() {
        DruidConfiguration config = DruidConfiguration
                .builder()
                .protocol(DruidQueryProtocol.valueOf(configProperties.getDruidConnectProtocol()))
                .port(Integer.valueOf(configProperties.getDruidConnectPort()))
                .host(configProperties.getDruidHost())
                .endpoint(configProperties.getDruidEndPoint())
                .build();

        DruidClient client = new DruidJerseyClient(config);
        try {
            client.connect();
            log.debug("Druid Client connection: " + client);
        } catch (ConnectionException e) {
            log.error("Exception occurred while getting the druid client -{}", e.getStackTrace());
        }
        return client;
    }
}
