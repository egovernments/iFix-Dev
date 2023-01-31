package org.egov.repository;


import java.util.ArrayList;
import java.util.List;

import org.egov.repository.querybuilder.FiscalEventQueryBuilder;
import org.egov.repository.rowmapper.FiscalEventRowMapper;
import org.egov.tracer.model.CustomException;
import org.egov.web.models.Criteria;
import org.egov.web.models.FiscalEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SingleColumnRowMapper;
import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class FiscalEventRepository {

    @Autowired
    private FiscalEventQueryBuilder eventQueryBuilder;

    @Autowired
    private FiscalEventRowMapper fiscalEventRowMapper;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<FiscalEvent> searchFiscalEvent(Criteria searchCriteria) {
        List<FiscalEvent> fiscalEvents = new ArrayList<>();
        List<Object> preparedStmtList = new ArrayList<>();

        try {
            // Fetch fiscal events based on returned ids
            String finalFiscalEventSearchQuery = eventQueryBuilder.buildSearchQuery(searchCriteria, preparedStmtList);
            fiscalEvents = jdbcTemplate.query(finalFiscalEventSearchQuery, preparedStmtList.toArray(), fiscalEventRowMapper);
        } catch (Exception e) {
            log.error("Exception while fetching data from DB: " + e);
            throw new CustomException("IFIX_FISCAL_EVENTS_SEARCH_ERR", "Some error occurred while running search operation.");
        }

        return fiscalEvents;
    }

    public List<String> searchFiscalEventUuids(Criteria searchCriteria) {
        List<Object> preparedStmtList = new ArrayList<>();

        // Fetch ids according to given criteria
        String idQuery = eventQueryBuilder.buildUuidsSearchQuery(searchCriteria, preparedStmtList);
        log.info("Fiscal event ids query: " + idQuery);
        log.info("Parameters: " + preparedStmtList.toString());
        List<String> fiscalEventIds = jdbcTemplate.query(idQuery, preparedStmtList.toArray(), new SingleColumnRowMapper<>(String.class));
        return fiscalEventIds;
    }
}
