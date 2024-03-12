package org.digit.program.repository;

import lombok.extern.slf4j.Slf4j;
import org.digit.program.models.sanction.Sanction;
import org.digit.program.models.sanction.SanctionSearch;
import org.digit.program.repository.querybuilder.ExchangeCodeQueryBuilder;
import org.digit.program.repository.querybuilder.SanctionQueryBuilder;
import org.digit.program.repository.rowmapper.SanctionRowMapper;
import org.digit.program.utils.CommonUtil;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Repository
@Slf4j
public class SanctionRepository {

    private final JdbcTemplate jdbcTemplate;
    private final ExchangeCodeQueryBuilder exchangeCodeQueryBuilder;
    private final SanctionQueryBuilder sanctionQueryBuilder;
    private final SanctionRowMapper sanctionRowMapper;
    private final CommonUtil commonUtil;

    public SanctionRepository(JdbcTemplate jdbcTemplate, ExchangeCodeQueryBuilder exchangeCodeQueryBuilder, SanctionQueryBuilder sanctionQueryBuilder, SanctionRowMapper sanctionRowMapper, CommonUtil commonUtil) {
        this.jdbcTemplate = jdbcTemplate;
        this.exchangeCodeQueryBuilder = exchangeCodeQueryBuilder;
        this.sanctionQueryBuilder = sanctionQueryBuilder;
        this.sanctionRowMapper = sanctionRowMapper;
        this.commonUtil = commonUtil;
    }

    /**
     * Persists new sanctions
     * @param sanctions
     */
    @Transactional
    public void saveSanction(List<Sanction> sanctions) {
        log.info("Persisting Create Sanctions");
        for (Sanction sanction : sanctions) {
            List<Object> preparedStmtList = new ArrayList<>();
            String exchangeCodeInsertQuery = exchangeCodeQueryBuilder.buildExchangeCodeSanctionInsertQuery(sanction, preparedStmtList);
            jdbcTemplate.update(exchangeCodeInsertQuery, preparedStmtList.toArray());

            preparedStmtList = new ArrayList<>();
            String sanctionInsertQuery = sanctionQueryBuilder.buildSanctionInsertQuery(sanction, preparedStmtList);
            jdbcTemplate.update(sanctionInsertQuery, preparedStmtList.toArray());
        }
        log.info("Sanctions persisted");
    }

    /**
     * Persists sanction update
     * @param sanctions
     */
    @Transactional
    public void updateSanction(List<Sanction> sanctions) {
        log.info("Persisting Sanction Update");
        for (Sanction sanction : sanctions) {
            List<Object> preparedStmtList = new ArrayList<>();
            String exchangeCodeUpdateQuery = exchangeCodeQueryBuilder.buildExchangeCodeSanctionUpdateQuery(sanction, preparedStmtList);
            jdbcTemplate.update(exchangeCodeUpdateQuery, preparedStmtList.toArray());

            preparedStmtList = new ArrayList<>();
            String sanctionUpdateQuery = sanctionQueryBuilder.buildSanctionUpdateQuery(sanction, preparedStmtList);
            jdbcTemplate.update(sanctionUpdateQuery, preparedStmtList.toArray());
        }
        log.info("Sanction Update persisted");
    }

    /**
     * Updates sanction amounts on allocation or disburse
     * @param sanctions
     */
    @Transactional
    public void updateSanctionOnAllocationOrDisburse(List<Sanction> sanctions) {
        log.info("Persisting Sanction Update on Allocation or Disburse");
        for (Sanction sanction : sanctions) {
            List<Object> preparedStmtList = new ArrayList<>();
            String exchangeCodeUpdateQuery = exchangeCodeQueryBuilder.buildExchangeCodeSanctionUpdateQuery(sanction, preparedStmtList);
            jdbcTemplate.update(exchangeCodeUpdateQuery, preparedStmtList.toArray());

            preparedStmtList = new ArrayList<>();
            String sanctionUpdateQuery = sanctionQueryBuilder.buildSanctionUpdateOnAllocationOrDisburseQuery(sanction, preparedStmtList);
            jdbcTemplate.update(sanctionUpdateQuery, preparedStmtList.toArray());
        }
        log.info("Sanction Update on Allocation or Disburse persisted");
    }

    /**
     * Sets pagination and searches for sanctions
     * @param sanctionSearch
     * @return
     */
    public List<Sanction> searchSanction(SanctionSearch sanctionSearch, Boolean keepPagination) {
        List<Object> preparedStmtList = new ArrayList<>();
        sanctionSearch.setPagination(commonUtil.enrichSearch(sanctionSearch.getPagination()));
        String sanctionSearchQuery = sanctionQueryBuilder.buildSanctionSearchQuery(sanctionSearch, preparedStmtList, keepPagination);
        return jdbcTemplate.query(sanctionSearchQuery, preparedStmtList.toArray(), sanctionRowMapper);
    }
}
