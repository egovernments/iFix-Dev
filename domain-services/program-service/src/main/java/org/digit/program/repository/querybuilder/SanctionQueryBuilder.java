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

    public static final String SANCTION_INSERT_QUERY = " INSERT INTO eg_program_sanction " +
            " ( id, location_code, program_code, net_amount, gross_amount, allocated_amount, available_amount, status, " +
            " status_message, additional_details, created_by, last_modified_by, created_time, last_modified_time ) " +
            " VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ) ";

    public static final String SANCTION_UPDATE_QUERY = "UPDATE eg_program_sanction " +
            " SET status = ?, status_message = ?, additional_details = ?, last_modified_by = ?, last_modified_time = ? " +
            " WHERE id = ? ";

    public static final String SANCTION_UPDATE_ON_ALLOCATION_QUERY = "UPDATE eg_program_sanction " +
            " SET allocated_amount = ?, available_amount = ?, additional_details = ?, last_modified_by = ?, " +
            " last_modified_time = ? " +
            " WHERE id = ? ";

    public static final String SANCTION_SEARCH_QUERY = " SELECT " +
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
            "  pmc.last_modified_time AS message_last_modified_time, " +

            "  ps.id AS sanction_id, " +
            "  ps.location_code AS sanction_location_code, " +
            "  ps.program_code AS sanction_program_code, " +
            "  ps.gross_amount, " +
            "  ps.net_amount, " +
            "  ps.allocated_amount, " +
            "  ps.available_amount, " +
            "  ps.status AS sanction_status, " +
            "  ps.status_message AS sanction_status_message, " +
            "  ps.additional_details AS sanction_additional_details, " +
            "  ps.created_by AS sanction_created_by, " +
            "  ps.last_modified_by AS sanction_last_modified_by, " +
            "  ps.created_time AS sanction_created_time, " +
            "  ps.last_modified_time AS sanction_last_modified_time " +

            "  FROM eg_program_message_codes pmc " +
            "  JOIN eg_program_sanction ps ON pmc.reference_id = ps.id ";

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
            sanctionSearchQuery.append(" ps.id IN (").append(createQuery(ids)).append(")");
            addToPreparedStatement(preparedStmtList, ids);
        }
        if (sanctionSearch.getLocationCode() != null) {
            addClauseIfRequired(sanctionSearchQuery, preparedStmtList);
            sanctionSearchQuery.append(" ps.location_code = ?");
            preparedStmtList.add(sanctionSearch.getLocationCode());
        }
        if (sanctionSearch.getProgramCode() != null) {
            addClauseIfRequired(sanctionSearchQuery, preparedStmtList);
            sanctionSearchQuery.append(" ps.program_code = ?");
            preparedStmtList.add(sanctionSearch.getProgramCode());
        }

        if (Boolean.TRUE.equals(keepPagination)) {
            sanctionSearchQuery.append(" ORDER BY ").append("ps.").append(sanctionSearch.getPagination().getSortBy())
                    .append(" ").append(sanctionSearch.getPagination().getSortOrder().toString()).append(" LIMIT ? OFFSET ? ");
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
