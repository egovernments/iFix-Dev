package org.egov.repository.querybuilder;

import org.egov.FiscalApplicationMain;
import org.egov.config.FiscalEventConfiguration;
import org.egov.config.TestDataFormatter;
import org.egov.web.models.Criteria;
import org.egov.web.models.FiscalEventGetRequest;
import org.egov.web.models.FiscalEventResponse;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.ArrayList;

import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(classes = FiscalApplicationMain.class)
class FiscalEventQueryBuilderTest {

    @InjectMocks
    private FiscalEventQueryBuilder fiscalEventQueryBuilder;

    @Autowired
    private TestDataFormatter testDataFormatter;
    @Mock
    private FiscalEventConfiguration configuration;

    private FiscalEventGetRequest fiscalEventGetRequest;
    private FiscalEventResponse fiscalEventSearchResponse;


    @BeforeAll
    void init() throws IOException {
        fiscalEventGetRequest = testDataFormatter.getFiscalEventSearchRequestData();
        fiscalEventSearchResponse = testDataFormatter.getFiscalEventSearchResponseData();
    }


    @Test
    void testBuildSearchQueryWithEmptyCriteria() {
        when(configuration.getDefaultOffset()).thenReturn(0L);
        when(configuration.getDefaultLimit()).thenReturn(0L);
        fiscalEventQueryBuilder.buildUuidsSearchQuery(new Criteria(), new ArrayList<>());
    }

    @Test
    void testBuildSearchQueryWithCriteria() {
        when(configuration.getDefaultOffset()).thenReturn(0L);
        when(configuration.getDefaultLimit()).thenReturn(0L);
        fiscalEventQueryBuilder.buildUuidsSearchQuery(fiscalEventGetRequest.getCriteria(), new ArrayList<>());
    }
}
