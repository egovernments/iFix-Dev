package org.digit.program.repository;

import lombok.extern.slf4j.Slf4j;
import org.digit.program.models.allocation.Allocation;
import org.digit.program.models.allocation.AllocationSearch;
import org.digit.program.models.sanction.Sanction;
import org.digit.program.repository.querybuilder.AllocationQueryBuilder;
import org.digit.program.repository.querybuilder.ExchangeCodeQueryBuilder;
import org.digit.program.repository.rowmapper.AllocationRowMapper;
import org.digit.program.utils.CommonUtil;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Repository
@Slf4j
public class AllocationRepository {

    private final JdbcTemplate jdbcTemplate;
    private final ExchangeCodeQueryBuilder exchangeCodeQueryBuilder;
    private final AllocationQueryBuilder allocationQueryBuilder;
    private final AllocationRowMapper allocationRowMapper;
    private final SanctionRepository sanctionRepository;
    private final CommonUtil commonUtil;


    public AllocationRepository(JdbcTemplate jdbcTemplate, ExchangeCodeQueryBuilder exchangeCodeQueryBuilder,
                                AllocationQueryBuilder allocationQueryBuilder, AllocationRowMapper allocationRowMapper,
                                SanctionRepository sanctionRepository, CommonUtil commonUtil) {
        this.jdbcTemplate = jdbcTemplate;
        this.exchangeCodeQueryBuilder = exchangeCodeQueryBuilder;
        this.allocationQueryBuilder = allocationQueryBuilder;
        this.allocationRowMapper = allocationRowMapper;
        this.sanctionRepository = sanctionRepository;
        this.commonUtil = commonUtil;
    }

    /**
     * Persists new allocations
     * @param allocations
     */
    @Transactional
    public void saveAllocation(List<Allocation> allocations) {
        log.info("Persisting Create Allocation");
        for (Allocation allocation : allocations) {
            List<Object> preparedStmtList = new ArrayList<>();
            String exchangeCodeInsertQuery = exchangeCodeQueryBuilder.buildExchangeCodeAllocationInsertQuery(allocation, preparedStmtList);
            jdbcTemplate.update(exchangeCodeInsertQuery, preparedStmtList.toArray());

            preparedStmtList = new ArrayList<>();
            String allocationInsertQuery = allocationQueryBuilder.buildAllocationInsertQuery(allocation, preparedStmtList);
            jdbcTemplate.update(allocationInsertQuery, preparedStmtList.toArray());
        }
        log.info("Persisted Create Allocation");
    }

    /**
     * Persists allocation update
     * @param allocations
     */
    @Transactional
    public void updateAllocation(List<Allocation> allocations) {
        log.info("Persisting Update Allocation");
        for (Allocation allocation : allocations) {
            List<Object> preparedStmtList = new ArrayList<>();
            String exchangeCodeUpdateQuery = exchangeCodeQueryBuilder.buildExchangeCodeAllocationUpdateQuery(allocation, preparedStmtList);
            jdbcTemplate.update(exchangeCodeUpdateQuery, preparedStmtList.toArray());

            preparedStmtList = new ArrayList<>();
            String allocationUpdateQuery = allocationQueryBuilder.buildAllocationUpdateQuery(allocation, preparedStmtList);
            jdbcTemplate.update(allocationUpdateQuery, preparedStmtList.toArray());
        }
        log.info("Persisted Update Allocation");
    }

    /**
     * Saves allocation and sanction amount update in transactional manner.
     * @param allocations
     * @param sanctions
     */
    @Transactional
    public void saveAllocationsAndSanctions(List<Allocation> allocations, List<Sanction> sanctions) {
        saveAllocation(allocations);
        sanctionRepository.updateSanctionOnAllocationOrDisburse(sanctions);
    }

    /**
     * Sets pagination and searches for allocationsxx
     * @param allocationSearch
     * @return
     */
    public List<Allocation> searchAllocation(AllocationSearch allocationSearch) {
        List<Object> preparedStmtList = new ArrayList<>();
        allocationSearch.setPagination(commonUtil.enrichSearch(allocationSearch.getPagination()));
        String allocationSearchQuery = allocationQueryBuilder.buildAllocationSearchQuery(allocationSearch, preparedStmtList);
        return jdbcTemplate.query(allocationSearchQuery, preparedStmtList.toArray(), allocationRowMapper);
    }

}
