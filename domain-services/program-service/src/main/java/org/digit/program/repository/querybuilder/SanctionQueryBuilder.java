package org.digit.program.repository.querybuilder;

import org.digit.program.models.sanction.Sanction;
import org.digit.program.models.sanction.SanctionSearch;
import org.digit.program.utils.CommonUtil;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

@Component
public class SanctionQueryBuilder {

    private final CommonUtil commonUtil;

    public static final String SANCTION_INSERT_QUERY = "INSERT INTO eg_program_sanction " +
            "( id, location_code, program_code, net_amount, gross_amount, allocated_amount, available_amount, status, status_message, " +
            " additional_details, created_by, last_modified_by, created_time, last_modified_time) " +
            " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ";

    public static final String SANCTION_UPDATE_QUERY = "UPDATE eg_program_sanction " +
            " SET status = ?, status_message = ?, additional_details = ?, last_modified_by = ?, last_modified_time = ? " +
            " WHERE id = ? ";

    public static final String SANCTION_UPDATE_ON_ALLOCATION_QUERY = "UPDATE eg_program_sanction " +
            " SET allocated_amount = ?, available_amount = ?, additional_details = ?, last_modified_by = ?, " +
            " last_modified_time = ? " +
            " WHERE id = ? ";

    public static final String SANCTION_SEARCH_QUERY = "SELECT * FROM eg_program_sanction JOIN eg_program_message_codes " +
            " ON eg_program_sanction.id = eg_program_message_codes.reference_id ";

    public SanctionQueryBuilder(CommonUtil commonUtil) {
        this.commonUtil = commonUtil;
    }

    public String buildSanctionInsertQuery(Sanction sanction, List<Object> preparedStmtList) {
        preparedStmtList.add(sanction.getId());
        preparedStmtList.add(sanction.getLocationCode());
        preparedStmtList.add(sanction.getProgramCode());
        preparedStmtList.add(sanction.getNetAmount());
        preparedStmtList.add(sanction.getGrossAmount());
        preparedStmtList.add(sanction.getAllocatedAmount());
        preparedStmtList.add(sanction.getAvailableAmount());
        preparedStmtList.add(sanction.getStatus().getStatusCode().toString());
        preparedStmtList.add(sanction.getStatus().getStatusMessage());
        preparedStmtList.add(commonUtil.getPGObject(sanction.getAdditionalDetails()));
        preparedStmtList.add(sanction.getAuditDetails().getCreatedBy());
        preparedStmtList.add(sanction.getAuditDetails().getLastModifiedBy());
        preparedStmtList.add(sanction.getAuditDetails().getCreatedTime());
        preparedStmtList.add(sanction.getAuditDetails().getLastModifiedTime());
        return SANCTION_INSERT_QUERY;

    }

    public String buildSanctionUpdateQuery(Sanction sanction, List<Object> preparedStmtList) {
        preparedStmtList.add(sanction.getStatus().getStatusCode().toString());
        preparedStmtList.add(sanction.getStatus().getStatusMessage());
        preparedStmtList.add(commonUtil.getPGObject(sanction.getAdditionalDetails()));
        preparedStmtList.add(sanction.getAuditDetails().getLastModifiedBy());
        preparedStmtList.add(sanction.getAuditDetails().getLastModifiedTime());
        preparedStmtList.add(sanction.getId());
        return SANCTION_UPDATE_QUERY;
    }

    public String buildSanctionUpdateOnAllocationOrDisburseQuery(Sanction sanction, List<Object> preparedStmtList) {
        preparedStmtList.add(sanction.getAllocatedAmount());
        preparedStmtList.add(sanction.getAvailableAmount());
        preparedStmtList.add(commonUtil.getPGObject(sanction.getAdditionalDetails()));
        preparedStmtList.add(sanction.getAuditDetails().getLastModifiedBy());
        preparedStmtList.add(sanction.getAuditDetails().getLastModifiedTime());
        preparedStmtList.add(sanction.getId());
        return SANCTION_UPDATE_ON_ALLOCATION_QUERY;
    }

    public String buildSanctionSearchQuery(SanctionSearch sanctionSearch, List<Object> preparedStmtList, Boolean keepPagination) {
        StringBuilder sanctionSearchQuery = new StringBuilder(SANCTION_SEARCH_QUERY);

        List<String> ids = sanctionSearch.getIds();
        if (ids != null && !ids.isEmpty()) {
            addClauseIfRequired(sanctionSearchQuery, preparedStmtList);
            sanctionSearchQuery.append(" eg_program_sanction.id IN (").append(createQuery(ids)).append(")");
            addToPreparedStatement(preparedStmtList, ids);
        }
        if (sanctionSearch.getLocationCode() != null) {
            addClauseIfRequired(sanctionSearchQuery, preparedStmtList);
            sanctionSearchQuery.append(" eg_program_sanction.location_code = ?");
            preparedStmtList.add(sanctionSearch.getLocationCode());
        }
        if (sanctionSearch.getProgramCode() != null) {
            addClauseIfRequired(sanctionSearchQuery, preparedStmtList);
            sanctionSearchQuery.append(" eg_program_sanction.program_code = ?");
            preparedStmtList.add(sanctionSearch.getProgramCode());
        }

        if (Boolean.TRUE.equals(keepPagination)) {
            sanctionSearchQuery.append(" ORDER BY ? ");
            preparedStmtList.add(sanctionSearch.getPagination().getSortBy());
            sanctionSearchQuery.append(sanctionSearch.getPagination().getSortOrder().toString());
            sanctionSearchQuery.append(" LIMIT ? OFFSET ?");
            preparedStmtList.add(sanctionSearch.getPagination().getLimit());
            preparedStmtList.add(sanctionSearch.getPagination().getOffset());
        }


        return sanctionSearchQuery.toString();
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
