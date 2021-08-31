package org.egov.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.egov.tracer.config.TracerConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

@Component
@Data
@Import({TracerConfiguration.class})
@NoArgsConstructor
@AllArgsConstructor
public class FiscalEventPostProcessorConfig {

    @Value("${app.timezone}")
    private String timeZone;

    @PostConstruct
    public void initialize() {
        TimeZone.setDefault(TimeZone.getTimeZone(timeZone));
    }

    @Bean
    @Autowired
    public MappingJackson2HttpMessageConverter jacksonConverter(ObjectMapper objectMapper) {
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setObjectMapper(objectMapper);
        return converter;
    }

    @Value("${fiscal.event.processor.kafka.mongodb.topic}")
    private String fiscalEventMongoDbSink;

    @Value("${fiscal.event.kafka.dereferenced.topic}")
    private String fiscalEventDereferenceTopic;

    @Value("${fiscal.event.kafka.flattened.topic}")
    private String fiscalEventFlattenedTopic;

    @Value("${fiscal.event.processor.kafka.druid.topic}")
    private String fiscalEventDruidTopic;

    @Value("${ifix.master.coa.host}")
    private String ifixMasterCoaHost;

    @Value("${ifix.master.coa.context.path}")
    private String ifixMasterCoaContextPath;

    @Value("${ifix.master.coa.search.path}")
    private String ifixMasterCoaSearchPath;

    @Value("${ifix.master.government.host}")
    private String ifixMasterGovernmentHost;

    @Value("${ifix.master.government.context.path}")
    private String ifixMasterGovernmentContextPath;

    @Value("${ifix.master.government.search.path}")
    private String ifixMasterGovernmentSearchPath;

    @Value("${ifix.master.project.host}")
    private String ifixMasterProjectHost;

    @Value("${ifix.master.project.context.path}")
    private String ifixMasterProjectContextPath;

    @Value("${ifix.master.project.search.path}")
    private String ifixMasterProjectSearchPath;

    @Value("${ifix.master.expenditure.host}")
    private String ifixMasterExpenditureHost;

    @Value("${ifix.master.expenditure.context.path}")
    private String ifixMasterExpenditureContextPath;

    @Value("${ifix.master.expenditure.search.path}")
    private String ifixMasterExpenditureSearchPath;

    @Value("${ifix.master.department.host}")
    private String ifixMasterDepartmentHost;

    @Value("${ifix.master.department.context.path}")
    private String ifixMasterDepartmentContextPath;

    @Value("${ifix.master.department.search.path}")
    private String ifixMasterDepartmentSearchPath;
}
