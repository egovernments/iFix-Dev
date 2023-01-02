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
public class IfixDepartmentEntityConfig {

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

    @Value("${ifix.master.department.host}")
    private String ifixMasterDepartmentHost;

    @Value("${ifix.master.department.context.path}")
    private String ifixMasterDepartmentContextPath;

    @Value("${ifix.master.department.search.path}")
    private String ifixMasterDepartmentSearchPath;

    @Value("${maximum.supported.department.hierarchy}")
    private Integer maximumSupportedDepartmentHierarchy;

    @Value("${persister.kafka.department.entity.create.topic}")
    private String persisterKafkaDepartmentEntityCreateTopic;

    @Value("${persister.kafka.department.entity.update.topic}")
    private String persisterKafkaDepartmentEntityUpdateTopic;

    @Value("${persister.kafka.department.hierarchy.create.topic}")
    private String persisterKafkaDepartmentHierarchyCreateTopic;


}
