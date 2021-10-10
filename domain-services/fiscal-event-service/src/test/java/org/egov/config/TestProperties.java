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

    @Value("${fiscal.event.search.request.data}")
    private String fiscalEventSearchRequest;

    @Value("${fiscal.event.search.response.data}")
    private String fiscalEventSearchResponse;

    @Value("${fiscal.event.push.request.data}")
    private String fiscalEventPushRequest;

    @Value("${fiscal.event.push.response.data}")
    private String fiscalEventPushResponse;

    @Value("${fiscal.event.push.headless.request.data}")
    private String fiscalEventPushHeadlessRequest;

    @Value("${gov.search.response.data}")
    private String govSearchResponseData;

    @Value("${project.search.response.data}")
    private String projectSearchResponseData;

    @Value("${coa.search.response.data}")
    private String coaSearchResponseData;

}
