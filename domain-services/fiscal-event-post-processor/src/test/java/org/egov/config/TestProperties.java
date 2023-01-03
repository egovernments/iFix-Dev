package org.egov.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Data
@Component
@PropertySource("classpath:application_test.properties")
public class TestProperties {

    @Value("${test.data.package}")
    private String testDataPackage;

    @Value("${fiscal.event.validated.data}")
    private String fiscalEventValidatedData;

    @Value("${fiscal.event.dereferenced.data}")
    private String fiscalEventDereferencedData;

    @Value("${gov.search.response.data}")
    private String govSearchResponseData;

    @Value("${coa.search.response.data}")
    private String coaSearchResponseData;
}
