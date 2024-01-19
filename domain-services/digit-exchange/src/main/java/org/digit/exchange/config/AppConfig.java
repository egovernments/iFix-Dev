package org.digit.exchange.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import java.util.Map;


@Getter
@Setter
@ConfigurationProperties(prefix = "app")
public class AppConfig {

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper().disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    private String name;

    private String domain;

    private String host;

    private String path;

    private Map<String, String> routes;

    private String exchangeTopic;

    private String errorTopic;
}
