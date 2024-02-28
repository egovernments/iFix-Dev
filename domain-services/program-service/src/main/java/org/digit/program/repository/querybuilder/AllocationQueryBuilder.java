package org.digit.program.repository.querybuilder;

import org.digit.program.models.allocation.Allocation;
import org.digit.program.models.allocation.AllocationSearch;
import org.digit.program.utils.CommonUtil;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

@Component
public class AllocationQueryBuilder {

    private final CommonUtil commonUtil;

    public static final String ALLOCATION_INSERT_QUERY = "INSERT INTO eg_program_allocation " +
            " (id, location_code, program_code, sanction_id, net_amount, gross_amount, status, status_message, type, " +
            " additional_details, created_by, last_modified_by, created_time, last_modified_time) " +
            " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    public static final String ALLOCATION_UPDATE_QUERY = "UPDATE eg_program_allocation " +
            "SET status = ?, status_message = ?, additional_details = ?, last_modified_by = ?, last_modified_time = ? " +
            "WHERE id = ?";

    public static final String ALLOCATION_SEARCH_QUERY = "SELECT * FROM eg_program_allocation JOIN eg_program_message_codes " +
            "ON eg_program_allocation.id = eg_program_message_codes.reference_id ";

    public AllocationQueryBuilder(CommonUtil commonUtil) {
        this.commonUtil = commonUtil;
    }

    public String buildAllocationInsertQuery(Allocation allocation, List<Object> preparedStmtList) {
        preparedStmtList.add(allocation.getId());
        preparedStmtList.add(allocation.getLocationCode());
        preparedStmtList.add(allocation.getProgramCode());
        preparedStmtList.add(allocation.getSanctionId());
        preparedStmtList.add(allocation.getNetAmount());
        preparedStmtList.add(allocation.getGrossAmount());
        preparedStmtList.add(allocation.getStatus().getStatusCode().toString());
        preparedStmtList.add(allocation.getStatus().getStatusMessage());
        preparedStmtList.add(allocation.getType().toString());
        preparedStmtList.add(commonUtil.getPGObject(allocation.getAdditionalDetails()));
        preparedStmtList.add(allocation.getAuditDetails().getCreatedBy());
        preparedStmtList.add(allocation.getAuditDetails().getLastModifiedBy());
        preparedStmtList.add(allocation.getAuditDetails().getCreatedTime());
        preparedStmtList.add(allocation.getAuditDetails().getLastModifiedTime());
        return ALLOCATION_INSERT_QUERY;
    }

    public String buildAllocationUpdateQuery(Allocation allocation, List<Object> preparedStmtList) {
        preparedStmtList.add(allocation.getStatus().getStatusCode().toString());
        preparedStmtList.add(allocation.getStatus().getStatusMessage());
        preparedStmtList.add(commonUtil.getPGObject(allocation.getAdditionalDetails()));
        preparedStmtList.add(allocation.getAuditDetails().getLastModifiedBy());
        preparedStmtList.add(allocation.getAuditDetails().getLastModifiedTime());
        preparedStmtList.add(allocation.getId());
        return ALLOCATION_UPDATE_QUERY;
    }

    public String buildAllocationSearchQuery(AllocationSearch allocationSearch, List<Object> preparedStmtList) {
        StringBuilder allocationSearchQuery = new StringBuilder(ALLOCATION_SEARCH_QUERY);

        List<String> ids = allocationSearch.getIds();
        if (ids != null && !ids.isEmpty()) {
            addClauseIfRequired(allocationSearchQuery, preparedStmtList);
            allocationSearchQuery.append(" eg_program_allocation.id IN (").append(createQuery(ids)).append(")");
            addToPreparedStatement(preparedStmtList, ids);
        }
        if (allocationSearch.getLocationCode() != null) {
            addClauseIfRequired(allocationSearchQuery, preparedStmtList);
            allocationSearchQuery.append(" eg_program_allocation.location_code = ? ");
            preparedStmtList.add(allocationSearch.getLocationCode());
        }
        if (allocationSearch.getProgramCode() != null) {
            addClauseIfRequired(allocationSearchQuery, preparedStmtList);
            allocationSearchQuery.append(" eg_program_allocation.program_code = ? ");
            preparedStmtList.add(allocationSearch.getProgramCode());
        }
        if (allocationSearch.getSanctionId() != null) {
            addClauseIfRequired(allocationSearchQuery, preparedStmtList);
            allocationSearchQuery.append(" eg_program_allocation.sanction_id = ? ");
            preparedStmtList.add(allocationSearch.getSanctionId());
        }
        allocationSearchQuery.append(" ORDER BY ? ");
        preparedStmtList.add(allocationSearch.getPagination().getSortBy());
        allocationSearchQuery.append(" LIMIT ? OFFSET ?");
        preparedStmtList.add(allocationSearch.getPagination().getLimit());
        preparedStmtList.add(allocationSearch.getPagination().getOffset());

        return allocationSearchQuery.toString();
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
