package org.egov.repository;

import org.egov.MasterApplicationMain;
import org.egov.config.TestDataFormatter;
import org.egov.repository.querybuilder.ChartOfAccountQueryBuilder;
import org.egov.repository.rowmapper.COARowMapper;
import org.egov.tracer.model.CustomException;
import org.egov.web.models.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(classes = MasterApplicationMain.class)
class ChartOfAccountRepositoryTest {

    @InjectMocks
    private ChartOfAccountRepository chartOfAccountRepository;

    @Mock
    private JdbcTemplate jdbcTemplate;
    @Mock
    private ChartOfAccountQueryBuilder chartOfAccountQueryBuilder;
    @Mock
    private COARowMapper coaRowMapper;

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
    void searchEmptyResult() {
        String query = chartOfAccountQueryBuilder.buildSearchQuery(new COASearchCriteria(), new ArrayList<>());
        when(jdbcTemplate.queryForList(query, new Object[]{}, coaRowMapper)).thenReturn(Collections.emptyList());
        List<ChartOfAccount> chartOfAccountActual = this.chartOfAccountRepository.search(new COASearchCriteria());
        assertNotNull(chartOfAccountActual);
        assertEquals(0, chartOfAccountActual.size());
    }

    @Test
    void exceptionWhileSearching() {
        String query = chartOfAccountQueryBuilder.buildSearchQuery(new COASearchCriteria(), new ArrayList<>());
        when(jdbcTemplate.query(query, new Object[]{}, coaRowMapper)).thenThrow(new CustomException());
        assertThrows(CustomException.class, () -> this.chartOfAccountRepository.search(new COASearchCriteria()));
    }

}