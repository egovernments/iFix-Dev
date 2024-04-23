package org.digit.program.repository;

import lombok.extern.slf4j.Slf4j;
import org.digit.program.constants.Status;
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

import static org.digit.program.constants.ProgramConstants.CREDIT;
import static org.digit.program.constants.ProgramConstants.DEBIT;

@Repository
@Slf4j
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

    /**
     * Persists disbursement for create
     * @param disbursement
     * @param parentId
     * @param isRoot
     */
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
            String transactionInsertQuery = disburseQueryBuilder.buildTransactionInsertQuery(disbursement, preparedStmtList, DEBIT);
            jdbcTemplate.update(transactionInsertQuery, preparedStmtList.toArray());
        }
        if (disbursement.getDisbursements() != null) {
            for (Disbursement childDisbursement : disbursement.getDisbursements()) {
                saveDisburse(childDisbursement, disbursement.getId(), false);
            }
        }
        if (Boolean.TRUE.equals(isRoot))
            log.debug("Saved parent Disbursement for id : {}", disbursement.getId());
        else
            log.debug("Saved child Disbursement for id : {}", disbursement.getId());
    }

    /**
     * Persists disbursement for update
     * @param disbursement
     * @param isOnCreate
     */
    @Transactional
    public void updateDisburse(Disbursement disbursement, Boolean isOnCreate, Boolean isRoot) {
        List<Object> preparedStmtList = new ArrayList<>();
        String disburseUpdateQuery = disburseQueryBuilder.buildDisburseUpdateQuery(disbursement, preparedStmtList, isOnCreate);
        jdbcTemplate.update(disburseUpdateQuery, preparedStmtList.toArray());

        if (Boolean.TRUE.equals(isRoot) && disbursement.getStatus() != null &&
                (disbursement.getStatus().getStatusCode().equals(Status.FAILED)  ||
                disbursement.getStatus().getStatusCode().equals(Status.ERROR))) {
            preparedStmtList = new ArrayList<>();
            String transactionInsertQuery = disburseQueryBuilder.buildTransactionInsertQuery(disbursement, preparedStmtList, CREDIT);
            jdbcTemplate.update(transactionInsertQuery, preparedStmtList.toArray());
        }
        if (disbursement.getDisbursements() != null) {
            for (Disbursement childDisbursement : disbursement.getDisbursements()) {
                updateDisburse(childDisbursement, isOnCreate, false);
            }
        }
        log.debug("Updated Parent/Child Disbursement for id : {}", disbursement.getId());
    }

    /**
     * Updates sanction and disbursement in transactional manner
     * @param disbursement
     * @param sanction
     */
    @Transactional
    public void updateDisburseAndSanction(Disbursement disbursement, Sanction sanction, Boolean isOnCreate) {
        if (sanction != null)
            sanctionRepository.updateSanctionOnAllocationOrDisburse(Collections.singletonList(sanction));
        updateDisburse(disbursement, isOnCreate, true);
        log.info("Persisted disbursement update with id : {}", disbursement.getId());
    }

    /**
     * Updates sanction and creates disbursement in transactional manner
     * @param disbursement
     * @param sanction
     */
    @Transactional
    public void saveDisburseAndSanction(Disbursement disbursement, Sanction sanction) {
        if (sanction != null)
            sanctionRepository.updateSanctionOnAllocationOrDisburse(Collections.singletonList(sanction));
        saveDisburse(disbursement, null, true);
        log.info("Persisted disbursement with id : {}", disbursement.getId());
    }

    /**
     * Searches disbursement and sets child disbursement for corresponding disbursement
     * @param disburseSearch
     * @return
     */
    public List<Disbursement> searchDisbursements(DisburseSearch disburseSearch) {
        log.info("Searching for disbursements");
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

    /**
     * Set child disbursements for the given list of disbursements.
     * @param  disbursements  the list of disbursements
     * @return                the updated list of disbursements
     */
    private List<Disbursement> setChildDisbursements(List<Disbursement> disbursements) {
        List<String> parentIds = disbursements.stream().map(Disbursement::getId).collect(Collectors.toList());
        List<Object> preparedStmtList = new ArrayList<>();
        String disburseChildSearchQuery = disburseQueryBuilder.buildDisburseSearchQuery(new DisburseSearch(),
                preparedStmtList, parentIds, false);
        List<Disbursement> childDisbursements = jdbcTemplate.query(disburseChildSearchQuery, preparedStmtList.toArray(),
                disburseRowMapper);

        if (childDisbursements == null || childDisbursements.isEmpty()) {
            return disbursements;
        }
        // Creating a map of parentId and list of Child disbursements
        Map<String, List<Disbursement>> disbursementsMap = childDisbursements.stream()
                .collect(Collectors.groupingBy(Disbursement::getParentId));

        // Setting child disbursements for parent disbursements
        for (Disbursement disbursement : disbursements) {
            if (disbursementsMap.containsKey(disbursement.getId()))
                disbursement.setDisbursements(disbursementsMap.get(disbursement.getId()));
        }
        return disbursements;
    }
}
