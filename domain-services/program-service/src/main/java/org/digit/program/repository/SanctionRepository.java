package org.digit.program.repository;

import org.digit.program.models.Sanction;
import org.digit.program.models.SanctionSearch;
import org.digit.program.repository.querybuilder.ExchangeCodeQueryBuilder;
import org.digit.program.repository.querybuilder.SanctionQueryBuilder;
import org.digit.program.repository.rowmapper.SanctionRowMapper;
import org.digit.program.service.EnrichmentService;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Repository
public class SanctionRepository {

    private final JdbcTemplate jdbcTemplate;
    private final ExchangeCodeQueryBuilder exchangeCodeQueryBuilder;
    private final SanctionQueryBuilder sanctionQueryBuilder;
    private final SanctionRowMapper sanctionRowMapper;
    private final EnrichmentService enrichmentService;

    public SanctionRepository(JdbcTemplate jdbcTemplate, ExchangeCodeQueryBuilder exchangeCodeQueryBuilder, SanctionQueryBuilder sanctionQueryBuilder, SanctionRowMapper sanctionRowMapper, EnrichmentService enrichmentService) {
        this.jdbcTemplate = jdbcTemplate;
        this.exchangeCodeQueryBuilder = exchangeCodeQueryBuilder;
        this.sanctionQueryBuilder = sanctionQueryBuilder;
        this.sanctionRowMapper = sanctionRowMapper;
        this.enrichmentService = enrichmentService;
    }

    @Transactional
    public void saveSanction(List<Sanction> sanctions) {
        for (Sanction sanction : sanctions) {
            List<Object> preparedStmtList = new ArrayList<>();
            String exchangeCodeInsertQuery = exchangeCodeQueryBuilder.buildExchangeCodeSanctionInsertQuery(sanction, preparedStmtList);
            jdbcTemplate.update(exchangeCodeInsertQuery, preparedStmtList.toArray());

            preparedStmtList = new ArrayList<>();
            String sanctionInsertQuery = sanctionQueryBuilder.buildSanctionInsertQuery(sanction, preparedStmtList);
            jdbcTemplate.update(sanctionInsertQuery, preparedStmtList.toArray());
        }
    }

    @Transactional
    public void updateSanction(List<Sanction> sanctions) {
        for (Sanction sanction : sanctions) {
            List<Object> preparedStmtList = new ArrayList<>();
            String exchangeCodeUpdateQuery = exchangeCodeQueryBuilder.buildExchangeCodeSanctionUpdateQuery(sanction, preparedStmtList);
            jdbcTemplate.update(exchangeCodeUpdateQuery, preparedStmtList.toArray());

            preparedStmtList = new ArrayList<>();
            String sanctionUpdateQuery = sanctionQueryBuilder.buildSanctionUpdateQuery(sanction, preparedStmtList);
            jdbcTemplate.update(sanctionUpdateQuery, preparedStmtList.toArray());
        }
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
        sanctionSearch.setPagination(enrichmentService.enrichSearch(sanctionSearch.getPagination()));
        String sanctionSearchQuery = sanctionQueryBuilder.buildSanctionSearchQuery(sanctionSearch, preparedStmtList);
        return jdbcTemplate.query(sanctionSearchQuery, preparedStmtList.toArray(), sanctionRowMapper);
    }
}
