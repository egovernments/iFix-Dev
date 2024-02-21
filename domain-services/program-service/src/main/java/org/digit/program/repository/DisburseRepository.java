package org.digit.program.repository;

import org.digit.program.models.disburse.DisburseSearch;
import org.digit.program.models.disburse.Disbursement;
import org.digit.program.models.sanction.Sanction;
import org.digit.program.repository.querybuilder.DisburseQueryBuilder;
import org.digit.program.repository.querybuilder.ExchangeCodeQueryBuilder;
import org.digit.program.repository.rowmapper.DisburseRowMapper;
import org.digit.program.utils.CommonUtil;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class DisburseRepository {

    private final JdbcTemplate jdbcTemplate;
    private final ExchangeCodeQueryBuilder exchangeCodeQueryBuilder;
    private final DisburseQueryBuilder disburseQueryBuilder;
    private final DisburseRowMapper disburseRowMapper;
    private final SanctionRepository sanctionRepository;
    private final CommonUtil commonUtil;

    public DisburseRepository(JdbcTemplate jdbcTemplate, ExchangeCodeQueryBuilder exchangeCodeQueryBuilder,
                              DisburseQueryBuilder disburseQueryBuilder, DisburseRowMapper disburseRowMapper,
                              SanctionRepository sanctionRepository, CommonUtil commonUtil) {
        this.jdbcTemplate = jdbcTemplate;
        this.exchangeCodeQueryBuilder = exchangeCodeQueryBuilder;
        this.disburseQueryBuilder = disburseQueryBuilder;
        this.disburseRowMapper = disburseRowMapper;
        this.sanctionRepository = sanctionRepository;
        this.commonUtil = commonUtil;
    }

    @Transactional
    public void saveDisburse(Disbursement disbursement, String parentId, Boolean isRoot) {
        List<Object> preparedStmtList = new ArrayList<>();
        String exchangeCodeInsertQuery = exchangeCodeQueryBuilder.buildExchangeCodeDisburseInsertQuery(disbursement, preparedStmtList);
        jdbcTemplate.update(exchangeCodeInsertQuery, preparedStmtList.toArray());

        preparedStmtList = new ArrayList<>();
        String disburseInsertQuery = disburseQueryBuilder.buildDisburseInsertQuery(disbursement, preparedStmtList, parentId);
        jdbcTemplate.update(disburseInsertQuery, preparedStmtList.toArray());

        if (Boolean.TRUE.equals(isRoot)) {
            preparedStmtList = new ArrayList<>();
            String transactionInsertQuery = disburseQueryBuilder.buildTransactionInsertQuery(disbursement, preparedStmtList);
            jdbcTemplate.update(transactionInsertQuery, preparedStmtList.toArray());
        }
        if (disbursement.getDisbursements() != null) {
            for (Disbursement childDisbursement : disbursement.getDisbursements()) {
                saveDisburse(childDisbursement, disbursement.getId(), false);
            }
        }
    }

    @Transactional
    public void updateDisburse(Disbursement disbursement, Boolean isOnCreate) {
        List<Object> preparedStmtList = new ArrayList<>();
        String disburseUpdateQuery = disburseQueryBuilder.buildDisburseUpdateQuery(disbursement, preparedStmtList, isOnCreate);
        jdbcTemplate.update(disburseUpdateQuery, preparedStmtList.toArray());

        if (disbursement.getDisbursements() != null) {
            for (Disbursement childDisbursement : disbursement.getDisbursements()) {
                updateDisburse(childDisbursement, isOnCreate);
            }
        }
    }

    @Transactional
    public void updateDisburseAndSanction(Disbursement disbursement, Sanction sanction) {
        if (sanction != null)
            sanctionRepository.updateSanctionOnAllocationOrDisburse(Collections.singletonList(sanction));
        updateDisburse(disbursement, true);
    }

    @Transactional
    public void createDisburseAndSanction(Disbursement disbursement, Sanction sanction) {
        if (sanction != null)
            sanctionRepository.updateSanctionOnAllocationOrDisburse(Collections.singletonList(sanction));
        saveDisburse(disbursement, null, true);
    }

    public List<Disbursement> searchDisbursements(DisburseSearch disburseSearch) {
        List<Object> preparedStmtList = new ArrayList<>();
        List<Disbursement> disbursements;
        disburseSearch.setPagination(commonUtil.enrichSearch(disburseSearch.getPagination()));
        String disburseSearchQuery = disburseQueryBuilder.buildDisburseSearchQuery(disburseSearch, preparedStmtList,
                null, true);
        disbursements = jdbcTemplate.query(disburseSearchQuery, preparedStmtList.toArray(), disburseRowMapper);

        if (disbursements == null || disbursements.isEmpty()) {
            return disbursements;
        }
        return setChildDisbursements(disbursements);

    }

    private List<Disbursement> setChildDisbursements(List<Disbursement> disbursements) {
        List<String> parentIds = disbursements.stream().map(Disbursement::getId).collect(Collectors.toList());
        List<Object> preparedStmtList = new ArrayList<>();
        String disburseChildSearchQuery = disburseQueryBuilder.buildDisburseSearchQuery(new DisburseSearch(),
                preparedStmtList, parentIds, false);
        List<Disbursement> childDisbursements = jdbcTemplate.query(disburseChildSearchQuery, preparedStmtList.toArray(),
                disburseRowMapper);

        Map<String, List<Disbursement>> disbursementsMap = childDisbursements.stream()
                .collect(Collectors.groupingBy(Disbursement::getParentId));

        for (Disbursement disbursement : disbursements) {
            if (disbursementsMap.containsKey(disbursement.getId()))
                disbursement.setDisbursements(disbursementsMap.get(disbursement.getId()));
        }
        return disbursements;
    }
}
