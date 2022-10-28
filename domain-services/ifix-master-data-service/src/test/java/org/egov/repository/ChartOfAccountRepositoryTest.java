package org.egov.repository;

import org.egov.config.TestDataFormatter;
import org.egov.repository.queryBuilder.ChartOfAccountQueryBuilder;
import org.egov.web.models.COARequest;
import org.egov.web.models.COAResponse;
import org.egov.web.models.COASearchRequest;
import org.egov.web.models.ChartOfAccount;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest()
class ChartOfAccountRepositoryTest {

    @Mock
    private MongoTemplate mongoTemplate;

    @Mock
    private ChartOfAccountQueryBuilder coaQueryBuilder;

    @InjectMocks
    private ChartOfAccountRepository chartOfAccountRepository;

    @Autowired
    private TestDataFormatter testDataFormatter;

    private COARequest coaRequest;
    private COASearchRequest coaSearchRequest;
    private COAResponse coaResponse;
    private COARequest headlessCoaRequest;
    private COAResponse coaCreateResponse;

    @BeforeAll
    public void init() throws IOException {
        coaRequest = testDataFormatter.getCoaRequestData();
        headlessCoaRequest = testDataFormatter.getHeadlessCoaRequestData();
        coaSearchRequest = testDataFormatter.getCoaSearchRequestData();
        coaCreateResponse = testDataFormatter.getCoaCreateResponseData();
        coaResponse = testDataFormatter.getCoaSearchResponseData();
    }

    @Test
    void save() {
        ChartOfAccount chartOfAccount = coaCreateResponse.getChartOfAccounts().get(0);
        doReturn(chartOfAccount).when(mongoTemplate).save(chartOfAccount);
        ChartOfAccount actual = mongoTemplate.save(chartOfAccount);
        assertNotNull(actual);
    }

    @Test
    void saveNull() {
        ChartOfAccount chartOfAccount = coaCreateResponse.getChartOfAccounts().get(0);
        doReturn(null).when(mongoTemplate).save(null);
        ChartOfAccount actual = mongoTemplate.save(chartOfAccount);
        assertNull(actual);
    }

    @Test
    void search() {
        Query searchQuery = coaQueryBuilder.buildSearchQuery(coaSearchRequest.getCriteria());

        doReturn(coaResponse.getChartOfAccounts()).when(mongoTemplate).find(searchQuery, ChartOfAccount.class);

        List<ChartOfAccount> chartOfAccountActual = mongoTemplate.find(searchQuery, ChartOfAccount.class);
        assertNotNull(chartOfAccountActual);
        assertTrue(chartOfAccountActual.size() > 0);

    }

    @Test
    void searchEmptyResult() {
        Query searchQuery = coaQueryBuilder.buildSearchQuery(coaSearchRequest.getCriteria());

        doReturn(new ArrayList<>()).when(mongoTemplate).find(searchQuery, ChartOfAccount.class);

        List<ChartOfAccount> chartOfAccountActual = mongoTemplate.find(searchQuery, ChartOfAccount.class);
        assertNotNull(chartOfAccountActual);
        assertEquals(0, chartOfAccountActual.size());

    }
}