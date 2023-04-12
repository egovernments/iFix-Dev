package org.egov.repository.querybuilder;

import org.egov.MasterApplicationMain;
import org.egov.config.TestDataFormatter;
import org.egov.web.models.COARequest;
import org.egov.web.models.COAResponse;
import org.egov.web.models.COASearchCriteria;
import org.egov.web.models.COASearchRequest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(classes = MasterApplicationMain.class)
class ChartOfAccountQueryBuilderTest {

    @InjectMocks
    private ChartOfAccountQueryBuilder chartOfAccountQueryBuilder;

    @Autowired
    private TestDataFormatter testDataFormatter;

    private COARequest coaRequest;
    private COASearchRequest coaSearchRequest;
    private COAResponse coaResponse;
    private COARequest headlessCoaRequest;

    @BeforeAll
    public void init() throws IOException {
        coaRequest = testDataFormatter.getCoaRequestData();
        headlessCoaRequest = testDataFormatter.getHeadlessCoaRequestData();
        coaSearchRequest = testDataFormatter.getCoaSearchRequestData();

        coaResponse = testDataFormatter.getCoaSearchResponseData();
    }

    @Test
    void testBuildSearchQueryWithEmptyCriteria() {
        String query = this.chartOfAccountQueryBuilder.buildSearchQuery(new COASearchCriteria(), new ArrayList<>());
        assertNotNull(query);
    }

    @Test
    void testBuildSearchQueryWithSearchCriteria() {
        COASearchCriteria coaSearchCriteria = coaSearchRequest.getCriteria();
        String query = this.chartOfAccountQueryBuilder.buildSearchQuery(coaSearchCriteria, new ArrayList<>());
        assertNotNull(query);
    }

}
