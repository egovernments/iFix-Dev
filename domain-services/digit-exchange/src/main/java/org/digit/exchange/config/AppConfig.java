package org.digit.exchange.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.*;
import org.digit.exchange.filters.pre.SignValidator;
import org.egov.tracer.config.TracerConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Map;
import java.util.TimeZone;
import com.fasterxml.jackson.core.type.TypeReference;

@Getter
@Setter
@Import({TracerConfiguration.class})
@NoArgsConstructor
@AllArgsConstructor
@Configuration
public class AppConfig {


    @Value("${app.timezone}")
    private String timeZone;

    @PostConstruct
    public void initialize() {
        TimeZone.setDefault(TimeZone.getTimeZone(timeZone));
    }

    @PostConstruct
    public void init() {
        // Parse the JSON string and convert it to a Map during initialization
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            receiverEndpoints = objectMapper.readValue(receiverEndpointsStr, new TypeReference<Map<String, String>>() {});
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper().disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    }

    @Bean
    @Autowired
    public MappingJackson2HttpMessageConverter jacksonConverter(ObjectMapper objectMapper) {
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setObjectMapper(objectMapper);
        return converter;
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Value("${app.name}")
    private String name;
    @Value("${service.host.url}")
    private String domain;

    @Value("${app.exchange.topic.name}")
    private String exchangeTopic;

    @Value("${app.error.topic.name}")
    private String errorTopic;

    @Value("${app.events.log.topic.name}")
    private String exchangeEventLogTopic;

    @Value("${app.enable.events.log}")
    private boolean enabledEventLogs;

    @Value("${app.receiver.endpoints}")
    private String receiverEndpointsStr;
    private Map<String, String> receiverEndpoints;

    @Value("${app.auth.sign.validation}")
    private boolean authEnabledExternalService;
    @Value("${app.auth.private-key}")
    private String serverPrivateKey;
    @Value("${app.auth.public-key}")
    private String serverPublicKey;



}
