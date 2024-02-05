package org.digit.program.repository;

import org.digit.program.models.Allocation;
import org.digit.program.models.AllocationSearch;
import org.digit.program.repository.querybuilder.AllocationQueryBuilder;
import org.digit.program.repository.querybuilder.ExchangeCodeQueryBuilder;
import org.digit.program.repository.rowmapper.AllocationRowMapper;
import org.digit.program.service.EnrichmentService;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class AllocationRepository {

    private final JdbcTemplate jdbcTemplate;
    private final ExchangeCodeQueryBuilder exchangeCodeQueryBuilder;
    private final AllocationQueryBuilder allocationQueryBuilder;
    private final AllocationRowMapper allocationRowMapper;
    private final EnrichmentService enrichmentService;


    public AllocationRepository(JdbcTemplate jdbcTemplate, ExchangeCodeQueryBuilder exchangeCodeQueryBuilder, AllocationQueryBuilder allocationQueryBuilder, AllocationRowMapper allocationRowMapper, EnrichmentService enrichmentService) {
        this.jdbcTemplate = jdbcTemplate;
        this.exchangeCodeQueryBuilder = exchangeCodeQueryBuilder;
        this.allocationQueryBuilder = allocationQueryBuilder;
        this.allocationRowMapper = allocationRowMapper;
        this.enrichmentService = enrichmentService;
    }

    public void saveAllocation(Allocation allocation) {
        List<Object> preparedStmtList = new ArrayList<>();
        String exchangeCodeInsertQuery = exchangeCodeQueryBuilder.buildExchangeCodeAllocationInsertQuery(allocation, preparedStmtList);
        jdbcTemplate.update(exchangeCodeInsertQuery, preparedStmtList.toArray());

        preparedStmtList = new ArrayList<>();
        String allocationInsertQuery = allocationQueryBuilder.buildAllocationInsertQuery(allocation, preparedStmtList);
        jdbcTemplate.update(allocationInsertQuery, preparedStmtList.toArray());
    }

    public void updateAllocation(Allocation allocation) {
        List<Object> preparedStmtList = new ArrayList<>();
        String exchangeCodeUpdateQuery = exchangeCodeQueryBuilder.buildExchangeCodeAllocationUpdateQuery(allocation, preparedStmtList);
        jdbcTemplate.update(exchangeCodeUpdateQuery, preparedStmtList.toArray());

        preparedStmtList = new ArrayList<>();
        String allocationUpdateQuery = allocationQueryBuilder.buildAllocationUpdateQuery(allocation, preparedStmtList);
        jdbcTemplate.update(allocationUpdateQuery, preparedStmtList.toArray());
    }

    public List<Allocation> searchAllocation(AllocationSearch allocationSearch) {
        List<Object> preparedStmtList = new ArrayList<>();
        allocationSearch.setPagination(enrichmentService.enrichSearch(allocationSearch.getPagination()));
        String allocationSearchQuery = allocationQueryBuilder.buildAllocationSearchQuery(allocationSearch, preparedStmtList);
        return jdbcTemplate.query(allocationSearchQuery, preparedStmtList.toArray(), allocationRowMapper);
    }

}
