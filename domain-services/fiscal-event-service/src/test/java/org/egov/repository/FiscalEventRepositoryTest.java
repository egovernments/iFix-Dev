package org.egov.repository;

import org.egov.FiscalApplicationMain;
import org.egov.config.TestDataFormatter;
import org.egov.repository.querybuilder.FiscalEventQueryBuilder;
import org.egov.repository.rowmapper.FiscalEventRowMapper;
import org.egov.tracer.model.CustomException;
import org.egov.web.models.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(classes = FiscalApplicationMain.class)
class FiscalEventRepositoryTest {

    @InjectMocks
    private FiscalEventRepository repository;

    @Mock
    private FiscalEventQueryBuilder eventQueryBuilder;
    @Mock
    private JdbcTemplate jdbcTemplate;
    @Mock
    private FiscalEventRowMapper fiscalEventRowMapper;

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
    void getCountTest() {
        String query = eventQueryBuilder.buildCountQuery(new PlainsearchCriteria(), new ArrayList<>());
        when(jdbcTemplate.queryForObject(query, new Object[]{}, Long.class)).thenReturn(1L);
        assertEquals(1L, this.repository.getFiscalEventsCount(new PlainsearchCriteria()));
    }

    @Test
    void searchFiscalEventUuids() {
        String query = eventQueryBuilder.buildUuidsSearchQuery(new Criteria(), new ArrayList<>());
        when(jdbcTemplate.query(query, new Object[]{}, fiscalEventRowMapper)).thenReturn(Collections.emptyList());
        List<String> fiscalEventUuids = this.repository.searchFiscalEventUuids(new Criteria());
        assertNotNull(fiscalEventUuids);
        assertEquals(0, fiscalEventUuids.size());
    }

    @Test
    void searchFiscalEventEmptyResult() {
        String query = eventQueryBuilder.buildSearchQuery(new Criteria(), new ArrayList<>());
        when(jdbcTemplate.query(query, new Object[]{}, fiscalEventRowMapper)).thenReturn(Collections.emptyList());
        List<FiscalEvent> fiscalEventsActual = this.repository.searchFiscalEvent(new Criteria());
        assertNotNull(fiscalEventsActual);
        assertEquals(0, fiscalEventsActual.size());
    }

    @Test
    void exceptionWhileSearching() {
        String query = eventQueryBuilder.buildSearchQuery(new Criteria(), new ArrayList<>());
        when(jdbcTemplate.query(query, new Object[]{}, fiscalEventRowMapper)).thenThrow(new CustomException());
        assertThrows(CustomException.class, () -> this.repository.searchFiscalEvent(new Criteria()));
    }

}