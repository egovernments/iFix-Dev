package org.digit.program.repository.querybuilder;

import org.digit.program.constants.MessageType;
import org.digit.program.models.disburse.DisburseSearch;
import org.digit.program.models.disburse.Disbursement;
import org.digit.program.utils.CommonUtil;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Component
public class DisburseQueryBuilder {

    private final CommonUtil commonUtil;


    public static final String DISBURSE_INSERT_QUERY = "INSERT INTO eg_program_disburse " +
            "( id, location_code, program_code, parent_id, target_id, sanction_id, transaction_id, account_code, " +
            " individual, net_amount, gross_amount, status, status_message, additional_details, " +
            " created_by, last_modified_by, created_time, last_modified_time) " +
            " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    public static final String DISBURSE_UPDATE_QUERY = "UPDATE eg_program_disburse " +
            " SET status = ?, status_message = ?, additional_detail = ? last_modified_by = ?, last_modified_time = ? " +
            " WHERE id = ?";

    public static final String ON_DISBURSE_CREATE_QUERY = "UPDATE eg_program_disburse " +
            " SET transaction_id = ?, status = ?, status_message = ?, additional_detail = ? last_modified_by = ?, " +
            " last_modified_time = ? " +
            " WHERE id = ?";

    public static final String DISBURSE_SEARCH_QUERY = "SELECT * FROM eg_program_disburse JOIN eg_program_message_codes " +
            " ON eg_program_disburse.id = eg_program_message_codes.reference_id ";

    public static final String TRANSACTION_LOGS_INSERT_QUERY = "INSERT INTO eg_program_transaction_logs " +
            " (id, location_code, program_code, sanction_id, disburse_id, type, amount, created_by, created_time) " +
            " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

    public DisburseQueryBuilder(CommonUtil commonUtil) {
        this.commonUtil = commonUtil;
    }

    public String buildDisburseInsertQuery(Disbursement disbursement, List<Object> preparedStmtList, String parentId) {
        preparedStmtList.add(disbursement.getId());
        preparedStmtList.add(disbursement.getLocationCode());
        preparedStmtList.add(disbursement.getProgramCode());
        preparedStmtList.add(parentId);
        preparedStmtList.add(disbursement.getTargetId());
        preparedStmtList.add(disbursement.getSanctionId());
        preparedStmtList.add(disbursement.getTransactionId());
        preparedStmtList.add(disbursement.getAccountCode());
        preparedStmtList.add(commonUtil.getPGObject(disbursement.getIndividual()));
        preparedStmtList.add(disbursement.getNetAmount());
        preparedStmtList.add(disbursement.getGrossAmount());
        preparedStmtList.add(disbursement.getStatus().getStatusCode().toString());
        preparedStmtList.add(disbursement.getStatus().getStatusMessage());
        preparedStmtList.add(commonUtil.getPGObject(disbursement.getAdditionalDetails()));
        preparedStmtList.add(disbursement.getAuditDetails().getCreatedBy());
        preparedStmtList.add(disbursement.getAuditDetails().getLastModifiedBy());
        preparedStmtList.add(disbursement.getAuditDetails().getCreatedTime());
        preparedStmtList.add(disbursement.getAuditDetails().getLastModifiedTime());
        return DISBURSE_INSERT_QUERY;
    }

    public String buildDisburseUpdateQuery(Disbursement disbursement, List<Object> preparedStmtList, Boolean isOnCreate) {
        if (Boolean.TRUE.equals(isOnCreate))
            preparedStmtList.add(disbursement.getTransactionId());
        preparedStmtList.add(disbursement.getStatus().getStatusCode().toString());
        preparedStmtList.add(disbursement.getStatus().getStatusMessage());
        preparedStmtList.add(commonUtil.getPGObject(disbursement.getAdditionalDetails()));
        preparedStmtList.add(disbursement.getAuditDetails().getLastModifiedBy());
        preparedStmtList.add(disbursement.getAuditDetails().getLastModifiedTime());
        preparedStmtList.add(disbursement.getId());
        return Boolean.TRUE.equals(isOnCreate) ? ON_DISBURSE_CREATE_QUERY : DISBURSE_UPDATE_QUERY;
    }

    public String buildTransactionInsertQuery (Disbursement disbursement, List<Object> preparedStmtList) {
        preparedStmtList.add(UUID.randomUUID().toString());
        preparedStmtList.add(disbursement.getLocationCode());
        preparedStmtList.add(disbursement.getProgramCode());
        preparedStmtList.add(disbursement.getSanctionId());
        preparedStmtList.add(disbursement.getId());
        preparedStmtList.add(MessageType.DISBURSE.toString());
        preparedStmtList.add(disbursement.getNetAmount());
        preparedStmtList.add(disbursement.getAuditDetails().getCreatedBy());
        preparedStmtList.add(disbursement.getAuditDetails().getCreatedTime());
        return TRANSACTION_LOGS_INSERT_QUERY;
    }

    public String buildDisburseSearchQuery(DisburseSearch disburseSearch, List<Object> preparedStmtList,
                                           List<String> parentIds, Boolean keepPagination) {
        StringBuilder disburseSearchQuery = new StringBuilder(DISBURSE_SEARCH_QUERY);

        List<String> ids = disburseSearch.getIds();
        if (ids != null && !ids.isEmpty()) {
            addClauseIfRequired(disburseSearchQuery, preparedStmtList);
            disburseSearchQuery.append(" eg_program_disburse.id IN (").append(createQuery(ids)).append(")");
            addToPreparedStatement(preparedStmtList, ids);
        }
        if (disburseSearch.getLocationCode() != null) {
            addClauseIfRequired(disburseSearchQuery, preparedStmtList);
            disburseSearchQuery.append(" eg_program_disburse.location_code = ? ");
            preparedStmtList.add(disburseSearch.getLocationCode());
        }
        if (disburseSearch.getProgramCode() != null) {
            addClauseIfRequired(disburseSearchQuery, preparedStmtList);
            disburseSearchQuery.append(" eg_program_disburse.program_code = ? ");
            preparedStmtList.add(disburseSearch.getProgramCode());
        }
        if (disburseSearch.getTargetId() != null) {
            addClauseIfRequired(disburseSearchQuery, preparedStmtList);
            disburseSearchQuery.append(" eg_program_disburse.target_id = ? ");
            preparedStmtList.add(disburseSearch.getTargetId());
        }
        if (parentIds == null || parentIds.isEmpty()) {
            addClauseIfRequired(disburseSearchQuery, preparedStmtList);
            disburseSearchQuery.append(" eg_program_disburse.parent_id IS NULL ");
        } else {
            addClauseIfRequired(disburseSearchQuery, preparedStmtList);
            disburseSearchQuery.append(" eg_program_disburse.parent_id IN (").append(createQuery(parentIds)).append(")");
            addToPreparedStatement(preparedStmtList, parentIds);
        }

        if (Boolean.TRUE.equals(keepPagination)) {
            disburseSearchQuery.append(" ORDER BY ? ");
            preparedStmtList.add(disburseSearch.getPagination().getSortBy());
            disburseSearchQuery.append(" LIMIT ? OFFSET ? ");
            preparedStmtList.add(disburseSearch.getPagination().getLimit());
            preparedStmtList.add(disburseSearch.getPagination().getOffset());
        }


        return disburseSearchQuery.toString();

    }

    private void addToPreparedStatement(List<Object> preparedStmtList, Collection<String> ids) {
        preparedStmtList.addAll(ids);
    }

    private String createQuery(Collection<String> ids) {
        StringBuilder builder = new StringBuilder();
        int length = ids.size();
        for (int i = 0; i < length; i++) {
            builder.append(" ? ");
            if (i != length - 1) builder.append(",");
        }
        return builder.toString();
    }

    private void addClauseIfRequired(StringBuilder query, List<Object> preparedStmtList) {
        if (preparedStmtList.isEmpty()) {
            query.append(" WHERE ");
        } else {
            query.append(" AND ");
        }
    }
}
