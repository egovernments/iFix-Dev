package org.digit.program.repository;

import org.digit.program.models.Sanction;
import org.digit.program.models.SanctionSearch;
import org.digit.program.repository.querybuilder.ExchangeCodeQueryBuilder;
import org.digit.program.repository.querybuilder.SanctionQueryBuilder;
import org.digit.program.repository.rowmapper.SanctionRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Repository
public class SanctionRepository {

    private JdbcTemplate jdbcTemplate;
    private ExchangeCodeQueryBuilder exchangeCodeQueryBuilder;
    private SanctionQueryBuilder sanctionQueryBuilder;
    private SanctionRowMapper sanctionRowMapper;

    public SanctionRepository(JdbcTemplate jdbcTemplate, ExchangeCodeQueryBuilder exchangeCodeQueryBuilder, SanctionQueryBuilder sanctionQueryBuilder, SanctionRowMapper sanctionRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.exchangeCodeQueryBuilder = exchangeCodeQueryBuilder;
        this.sanctionQueryBuilder = sanctionQueryBuilder;
        this.sanctionRowMapper = sanctionRowMapper;
    }

    @Transactional
    public void saveSanction(Sanction sanction) {
        List<Object> preparedStmtList = new ArrayList<>();
        String exchangeCodeInsertQuery = exchangeCodeQueryBuilder.buildExchangeCodeSanctionInsertQuery(sanction, preparedStmtList);
        jdbcTemplate.update(exchangeCodeInsertQuery, preparedStmtList.toArray());

        preparedStmtList = new ArrayList<>();
        String sanctionInsertQuery = sanctionQueryBuilder.buildSanctionInsertQuery(sanction, preparedStmtList);
        jdbcTemplate.update(sanctionInsertQuery, preparedStmtList.toArray());
    }

    @Transactional
    public void updateSanction(Sanction sanction) {
        List<Object> preparedStmtList = new ArrayList<>();
        String exchangeCodeUpdateQuery = exchangeCodeQueryBuilder.buildExchangeCodeSanctionUpdateQuery(sanction, preparedStmtList);
        jdbcTemplate.update(exchangeCodeUpdateQuery, preparedStmtList.toArray());

        preparedStmtList = new ArrayList<>();
        String sanctionUpdateQuery = sanctionQueryBuilder.buildSanctionUpdateQuery(sanction, preparedStmtList);
        jdbcTemplate.update(sanctionUpdateQuery, preparedStmtList.toArray());
    }

    @Transactional
    public void updateSanctionOnAllocation(Sanction sanction) {
        List<Object> preparedStmtList = new ArrayList<>();
        String exchangeCodeUpdateQuery = exchangeCodeQueryBuilder.buildExchangeCodeSanctionUpdateQuery(sanction, preparedStmtList);
        jdbcTemplate.update(exchangeCodeUpdateQuery, preparedStmtList.toArray());

        preparedStmtList = new ArrayList<>();
        String sanctionUpdateQuery = sanctionQueryBuilder.buildSanctionUpdateOnAllocationQuery(sanction, preparedStmtList);
        jdbcTemplate.update(sanctionUpdateQuery, preparedStmtList.toArray());
    }

    public List<Sanction> searchSanction(SanctionSearch sanctionSearch) {
        List<Object> preparedStmtList = new ArrayList<>();
        String sanctionSearchQuery = sanctionQueryBuilder.buildSanctionSearchQuery(sanctionSearch, preparedStmtList);
        return jdbcTemplate.query(sanctionSearchQuery, preparedStmtList.toArray(), sanctionRowMapper);
    }
}