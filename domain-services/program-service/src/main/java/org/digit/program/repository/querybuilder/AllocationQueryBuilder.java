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

    public static final String ALLOCATION_INSERT_QUERY = " INSERT INTO eg_program_allocation " +
            " ( id, location_code, program_code, sanction_id, net_amount, gross_amount, status, status_message, allocation_type, " +
            " additional_details, created_by, last_modified_by, created_time, last_modified_time ) " +
            " VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ) ";

    public static final String ALLOCATION_UPDATE_QUERY = " UPDATE eg_program_allocation " +
            " SET status = ?, status_message = ?, additional_details = ?, last_modified_by = ?, last_modified_time = ? " +
            " WHERE id = ? ";

    public static final String ALLOCATION_SEARCH_QUERY = " SELECT " +
            "  pa.id AS allocation_id, " +
            "  pa.location_code AS allocation_location_code, " +
            "  pa.program_code AS allocation_program_code, " +
            "  pa.sanction_id AS allocation_sanction_id, " +
            "  pa.gross_amount AS allocation_gross_amount, " +
            "  pa.net_amount AS allocation_net_amount, " +
            "  pa.status AS allocation_status, " +
            "  pa.status_message AS allocation_status_message, " +
            "  pa.allocation_type AS allocation_type, " +
            "  pa.additional_details AS allocation_additional_details, " +
            "  pa.created_by AS allocation_created_by, " +
            "  pa.last_modified_by AS allocation_last_modified_by, " +
            "  pa.created_time AS allocation_created_time, " +
            "  pa.last_modified_time AS allocation_last_modified_time, " +

            "  pmc.id AS message_code_id, " +
            "  pmc.location_code AS message_location_code, " +
            "  pmc.type AS message_type, " +
            "  pmc.reference_id AS message_reference_id, " +
            "  pmc.function_code AS message_function_code, " +
            "  pmc.administration_code AS message_administration_code, " +
            "  pmc.program_code AS message_program_code, " +
            "  pmc.recipient_segment_code AS message_recipient_segment_code, " +
            "  pmc.economic_segment_code AS message_economic_segment_code, " +
            "  pmc.source_of_fund_code AS message_source_of_fund_code, " +
            "  pmc.target_segment_code AS message_target_segment_code, " +
            "  pmc.currency_code AS message_currency_code, " +
            "  pmc.locale_code AS message_locale_code, " +
            "  pmc.created_by AS message_created_by, " +
            "  pmc.last_modified_by AS message_last_modified_by, " +
            "  pmc.created_time AS message_created_time, " +
            "  pmc.last_modified_time AS message_last_modified_time " +

            " FROM eg_program_allocation pa " +
            " JOIN eg_program_message_codes pmc ON pa.sanction_id = pmc.reference_id ";

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
        preparedStmtList.add(allocation.getAllocationType().toString());
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
            allocationSearchQuery.append(" pa.id IN (").append(createQuery(ids)).append(")");
            addToPreparedStatement(preparedStmtList, ids);
        }
        if (allocationSearch.getLocationCode() != null) {
            addClauseIfRequired(allocationSearchQuery, preparedStmtList);
            allocationSearchQuery.append(" pa.location_code = ? ");
            preparedStmtList.add(allocationSearch.getLocationCode());
        }
        if (allocationSearch.getProgramCode() != null) {
            addClauseIfRequired(allocationSearchQuery, preparedStmtList);
            allocationSearchQuery.append(" pa.program_code = ? ");
            preparedStmtList.add(allocationSearch.getProgramCode());
        }
        if (allocationSearch.getSanctionId() != null) {
            addClauseIfRequired(allocationSearchQuery, preparedStmtList);
            allocationSearchQuery.append(" pa.sanction_id = ? ");
            preparedStmtList.add(allocationSearch.getSanctionId());
        }
        allocationSearchQuery.append(" ORDER BY ").append("pa.").append(allocationSearch.getPagination().getSortBy())
                .append(" ").append(allocationSearch.getPagination().getSortOrder()).append(" LIMIT ? OFFSET ? ");
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
