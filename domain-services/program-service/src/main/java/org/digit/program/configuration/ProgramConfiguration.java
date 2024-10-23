package org.digit.program.configuration;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.Getter;
import lombok.Setter;
import org.egov.tracer.config.TracerConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import jakarta.annotation.PostConstruct;
import java.util.TimeZone;


@Getter
@Setter
@Configuration
@Import({TracerConfiguration.class})
public class ProgramConfiguration {

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper().disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES).registerModule(new JavaTimeModule());
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }


    @Bean
    @Autowired
    public MappingJackson2HttpMessageConverter jacksonConverter(ObjectMapper objectMapper) {
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setObjectMapper(objectMapper);
        return converter;
    }

    @Value("${app.timezone}")
    private String timeZone;

    @PostConstruct
    public void initialize() {
        TimeZone.setDefault(TimeZone.getTimeZone(timeZone));
    }

    @Value("${exchange.service.external.url}")
    private String domain;

    // Exchange configuration
    @Value("${exchange.host}")
    private String exchangeHost;
    @Value("${exchange.path}")
    private String exchangePath;

    // Adapter configuration
    @Value("${adapter.host}")
    private String adapterHost;
    @Value("${adapter.path}")
    private String adapterPath;

    // Idgen configuration
    @Value("${egov.idgen.host}")
    private String idGenHost;
    @Value("${egov.idgen.path}")
    private String idGenPath;
    @Value("${egov.idgen.idname}")
    private String idName;

    // Mdms configuration
    @Value("${egov.mdms.host}")
    private String mdmsHost;
    @Value("${egov.mdms.path}")
    private String mdmsPath;
    @Value("${exchange.url.code}")
    private String urlCode;

    // Search configuration
    @Value("${search.max.limit}")
    private Integer searchMaxLimit;
    @Value("${search.default.limit}")
    private Integer searchDefaultLimit;

    // Kafka configuration
    @Value("${is.program.async}")
    private Boolean isProgramAsync;
    @Value("${is.sanction.async}")
    private Boolean isSanctionAsync;
    @Value("${is.allocation.async}")
    private Boolean isAllocationAsync;
    @Value("${is.disburse.async}")
    private Boolean isDisburseAsync;
    @Value("${program.kafka.topic}")
    private String programTopic;
    @Value("${sanction.kafka.topic}")
    private String sanctionTopic;
    @Value("${allocation.kafka.topic}")
    private String allocationTopic;
    @Value("${disburse.kafka.topic}")
    private String disburseTopic;
    @Value("${error.kafka.topic}")
    private String errorTopic;

    // Encryption Service
    @Value("${encryption.key}")
    private String encryptionKey;
    @Value("${encryption.vector}")
    private String encryptionVector;

    @Value("${original.disburse.expire.days}")
    private Integer originalExpireDays;
    @Value("${original.disburse.expire.financial.year.date}")
    private Integer originalExpireFinancialYearDate;
    @Value("${original.disburse.expire.financial.year.month}")
    private Integer originalExpireFinancialYearMonth;

}
