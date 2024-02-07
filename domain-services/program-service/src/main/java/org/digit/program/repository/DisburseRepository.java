package org.digit.program.repository;

import org.digit.program.models.disburse.DisburseSearch;
import org.digit.program.models.disburse.Disbursement;
import org.digit.program.repository.querybuilder.DisburseQueryBuilder;
import org.digit.program.repository.querybuilder.ExchangeCodeQueryBuilder;
import org.digit.program.repository.rowmapper.DisburseRowMapper;
import org.digit.program.service.EnrichmentService;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class DisburseRepository {

    private final JdbcTemplate jdbcTemplate;
    private final ExchangeCodeQueryBuilder exchangeCodeQueryBuilder;
    private final DisburseQueryBuilder disburseQueryBuilder;
    private final DisburseRowMapper disburseRowMapper;
    private final EnrichmentService enrichmentService;

    public DisburseRepository(JdbcTemplate jdbcTemplate, ExchangeCodeQueryBuilder exchangeCodeQueryBuilder, DisburseQueryBuilder disburseQueryBuilder, DisburseRowMapper disburseRowMapper, EnrichmentService enrichmentService) {
        this.jdbcTemplate = jdbcTemplate;
        this.exchangeCodeQueryBuilder = exchangeCodeQueryBuilder;
        this.disburseQueryBuilder = disburseQueryBuilder;
        this.disburseRowMapper = disburseRowMapper;
        this.enrichmentService = enrichmentService;
    }

    @Transactional
    public void saveDisburse(Disbursement disbursement, String parentId) {
        List<Object> preparedStmtList = new ArrayList<>();
        String exchangeCodeInsertQuery = exchangeCodeQueryBuilder.buildExchangeCodeDisburseInsertQuery(disbursement, preparedStmtList);
        jdbcTemplate.update(exchangeCodeInsertQuery, preparedStmtList.toArray());

        preparedStmtList = new ArrayList<>();
        String disburseInsertQuery = disburseQueryBuilder.buildDisburseInsertQuery(disbursement, preparedStmtList, parentId);
        jdbcTemplate.update(disburseInsertQuery, preparedStmtList.toArray());

        if (disbursement.getDisbursements() != null) {
            for (Disbursement childDisbursement : disbursement.getDisbursements()) {
                saveDisburse(childDisbursement, disbursement.getId());
            }
        }
    }

    public List<Disbursement> searchDisbursements(DisburseSearch disburseSearch) {
        List<Object> preparedStmtList = new ArrayList<>();
        List<Disbursement> disbursements;
        disburseSearch.setPagination(enrichmentService.enrichSearch(disburseSearch.getPagination()));
        String disburseSearchQuery = disburseQueryBuilder.buildDisburseSearchQuery(disburseSearch, preparedStmtList,
                null, true);
        disbursements = jdbcTemplate.query(disburseSearchQuery, preparedStmtList.toArray(), disburseRowMapper);

        if (disbursements.isEmpty()) {
            return disbursements;
        }
        List<String> parentIds = disbursements.stream().map(Disbursement::getId).collect(Collectors.toList());
        preparedStmtList = new ArrayList<>();
        String disburseChildSearchQuery = disburseQueryBuilder.buildDisburseSearchQuery(new DisburseSearch(),
                preparedStmtList, parentIds, false);
        List<Disbursement> childDisbursements = jdbcTemplate.query(disburseChildSearchQuery, preparedStmtList.toArray(),
                disburseRowMapper);

        Map<String, List<Disbursement>> disbursementsMap = childDisbursements.stream().collect(Collectors.groupingBy(Disbursement::getParentId));

        for (Disbursement disbursement : disbursements) {
            if (disbursementsMap.containsKey(disbursement.getId()))
                disbursement.setDisbursements(disbursementsMap.get(disbursement.getId()));
        }
        return disbursements;
    }




}
