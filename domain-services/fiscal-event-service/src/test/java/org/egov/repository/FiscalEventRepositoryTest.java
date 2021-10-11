package org.egov.repository;

import org.egov.config.TestDataFormatter;
import org.egov.repository.querybuilder.FiscalEventQueryBuilder;
import org.egov.web.models.FiscalEventGetRequest;
import org.egov.web.models.FiscalEventResponse;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doReturn;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
class FiscalEventRepositoryTest {

    @InjectMocks
    private FiscalEventRepository repository;

    @Mock
    private MongoTemplate mongoTemplate;

    @Mock
    private FiscalEventQueryBuilder eventQueryBuilder;

    @Autowired
    private TestDataFormatter testDataFormatter;

    private FiscalEventGetRequest fiscalEventGetRequest;
    private FiscalEventResponse fiscalEventSearchResponse;

    @BeforeAll
    void init() throws IOException {
        fiscalEventGetRequest = testDataFormatter.getFiscalEventSearchRequestData();
        fiscalEventSearchResponse = testDataFormatter.getFiscalEventSearchResponseData();
    }

    @Test
    void searchFiscalEvent() {
        Query searchQuery = eventQueryBuilder.buildSearchQuery(fiscalEventGetRequest.getCriteria());

        doReturn(fiscalEventSearchResponse.getFiscalEvent()).when(mongoTemplate).find(searchQuery, Object.class, "fiscal_event");

        List<Object> fiscalEventsActual = mongoTemplate.find(searchQuery, Object.class, "fiscal_event");
        assertNotNull(fiscalEventsActual);
        assertTrue(fiscalEventsActual.size() > 0);
    }

    @Test
    void searchFiscalEventEmptyResult() {
        Query searchQuery = eventQueryBuilder.buildSearchQuery(fiscalEventGetRequest.getCriteria());

        doReturn(new ArrayList<>()).when(mongoTemplate).find(searchQuery, Object.class, "fiscal_event");

        List<Object> fiscalEventsActual = mongoTemplate.find(searchQuery, Object.class, "fiscal_event");
        assertNotNull(fiscalEventsActual);
        assertTrue(fiscalEventsActual.size() == 0);
    }
}