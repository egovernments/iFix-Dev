package org.digit.program.repository.querybuilder;

import org.apache.commons.lang3.StringUtils;
import org.digit.program.constants.Status;
import org.digit.program.models.program.Program;
import org.digit.program.models.program.ProgramSearch;
import org.digit.program.utils.CommonUtil;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

@Component
public class ProgramQueryBuilder {

    private final CommonUtil commonUtil;
    public static final String PROGRAM_INSERT_QUERY = " INSERT INTO eg_program " +
            " ( id, location_code, program_code, name, parent_id, description, status, status_message, " +
            " start_date, end_date, additional_details, created_by, last_modified_by, created_time, last_modified_time ) " +
            " VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ) ";

    public static final String PROGRAM_UPDATE_QUERY = " UPDATE eg_program " +
            " SET name = ?, description = ?, status = ?, status_message = ?, " +
            " end_date = ?, additional_details = ?, last_modified_by = ?, last_modified_time = ? " +
            " WHERE id = ? ";

    public static final String ON_PROGRAM_UPDATE_QUERY = " UPDATE eg_program " +
            " SET program_code = ?, name = ?, description = ?, status = ?, status_message = ?, " +
            " end_date = ?, additional_details = ?, last_modified_by = ?, last_modified_time = ? " +
            " WHERE id = ? ";

    public static final String PROGRAM_SEARCH_QUERY = " SELECT " +
            "    eg_program.id AS eg_program_id, " +
            "    eg_program.location_code AS eg_program_location_code, " +
            "    eg_program.program_code AS eg_program_code, " +
            "    eg_program.name AS eg_program_name, " +
            "    eg_program.parent_id AS eg_program_parent_id, " +
            "    eg_program.description AS eg_program_description, " +
            "    eg_program.status AS eg_program_status, " +
            "    eg_program.status_message AS eg_program_status_message, " +
            "    eg_program.start_date AS eg_program_start_date, " +
            "    eg_program.end_date AS eg_program_end_date, " +
            "    eg_program.additional_details AS eg_program_additional_details, " +
            "    eg_program.created_by AS eg_program_created_by, " +
            "    eg_program.last_modified_by AS eg_program_last_modified_by, " +
            "    eg_program.created_time AS eg_program_created_time, " +
            "    eg_program.last_modified_time AS eg_program_last_modified_time, " +

            "    eg_program_message_codes.id AS eg_program_message_codes_id, " +
            "    eg_program_message_codes.location_code AS eg_program_message_codes_location_code, " +
            "    eg_program_message_codes.type AS eg_program_message_codes_type, " +
            "    eg_program_message_codes.reference_id AS eg_program_message_codes_reference_id, " +
            "    eg_program_message_codes.function_code AS eg_program_message_codes_function_code, " +
            "    eg_program_message_codes.administration_code AS eg_program_message_codes_administration_code, " +
            "    eg_program_message_codes.program_code AS eg_program_message_codes_program_code, " +
            "    eg_program_message_codes.recipient_segment_code AS eg_program_message_codes_recipient_segment_code, " +
            "    eg_program_message_codes.economic_segment_code AS eg_program_message_codes_economic_segment_code, " +
            "    eg_program_message_codes.source_of_fund_code AS eg_program_message_codes_source_of_fund_code, " +
            "    eg_program_message_codes.target_segment_code AS eg_program_message_codes_target_segment_code, " +
            "    eg_program_message_codes.currency_code AS eg_program_message_codes_currency_code, " +
            "    eg_program_message_codes.locale_code AS eg_program_message_codes_locale_code, " +
            "    eg_program_message_codes.created_by AS eg_program_message_codes_created_by, " +
            "    eg_program_message_codes.last_modified_by AS eg_program_message_codes_last_modified_by, " +
            "    eg_program_message_codes.created_time AS eg_program_message_codes_created_time, " +
            "    eg_program_message_codes.last_modified_time AS eg_program_message_codes_last_modified_time " +
            " FROM " +
            "    eg_program " +
            " JOIN "  +
            "    eg_program_message_codes ON eg_program.id = eg_program_message_codes.reference_id";

    public ProgramQueryBuilder(CommonUtil commonUtil) {
        this.commonUtil = commonUtil;
    }

    public String buildProgramInsertQuery(Program program, List<Object> preparedStmtList) {
        preparedStmtList.add(program.getId());
        preparedStmtList.add(program.getLocationCode());
        preparedStmtList.add(program.getProgramCode());
        preparedStmtList.add(program.getName());
        preparedStmtList.add(program.getParentId());
        preparedStmtList.add(program.getDescription());
        preparedStmtList.add(program.getStatus().getStatusCode().toString());
        preparedStmtList.add(program.getStatus().getStatusMessage());
        preparedStmtList.add(program.getStartDate());
        preparedStmtList.add(program.getEndDate());
        preparedStmtList.add(commonUtil.getPGObject(program.getAdditionalDetails()));
        preparedStmtList.add(program.getAuditDetails().getCreatedBy());
        preparedStmtList.add(program.getAuditDetails().getLastModifiedBy());
        preparedStmtList.add(program.getAuditDetails().getCreatedTime());
        preparedStmtList.add(program.getAuditDetails().getLastModifiedTime());
        return PROGRAM_INSERT_QUERY;
    }



    public String buildProgramUpdateQuery(Program program, List<Object> preparedStmtList, Boolean isOnProgramCreate) {
        if (Boolean.TRUE.equals(isOnProgramCreate)) {
            preparedStmtList.add(program.getProgramCode());
        }
        preparedStmtList.add(program.getName());
        preparedStmtList.add(program.getDescription());
        preparedStmtList.add(program.getStatus().getStatusCode().toString());
        preparedStmtList.add(program.getStatus().getStatusMessage());
        preparedStmtList.add(program.getEndDate());
        preparedStmtList.add(commonUtil.getPGObject(program.getAdditionalDetails()));
        preparedStmtList.add(program.getAuditDetails().getLastModifiedBy());
        preparedStmtList.add(program.getAuditDetails().getLastModifiedTime());
        preparedStmtList.add(program.getId());
        return Boolean.TRUE.equals(isOnProgramCreate) ? ON_PROGRAM_UPDATE_QUERY : PROGRAM_UPDATE_QUERY;
    }

    public String buildProgramSearchQuery(ProgramSearch programSearch, List<Object> preparedStmtList) {
        StringBuilder programSearchQuery = new StringBuilder(PROGRAM_SEARCH_QUERY);

        List<String> ids = programSearch.getIds();
        if (ids != null && !ids.isEmpty()) {
            addClauseIfRequired(programSearchQuery, preparedStmtList);
            programSearchQuery.append(" eg_program.id IN (").append(createQuery(ids)).append(")");
            addToPreparedStatement(preparedStmtList, ids);
        }

        if(StringUtils.isNotBlank(programSearch.getLocationCode())) {
            addClauseIfRequired(programSearchQuery, preparedStmtList);
            programSearchQuery.append(" eg_program.location_code = ? ");
            preparedStmtList.add(programSearch.getLocationCode());
        }

        if(StringUtils.isNotBlank(programSearch.getProgramCode())) {
            addClauseIfRequired(programSearchQuery, preparedStmtList);
            programSearchQuery.append(" eg_program.program_code = ? ");
            preparedStmtList.add(programSearch.getProgramCode());
        }

        if(StringUtils.isNotBlank(programSearch.getName())) {
            addClauseIfRequired(programSearchQuery, preparedStmtList);
            programSearchQuery.append(" eg_program.name = ? ");
            preparedStmtList.add(programSearch.getName());
        }

        if (StringUtils.isNotBlank(programSearch.getParentId())) {
            addClauseIfRequired(programSearchQuery, preparedStmtList);
            programSearchQuery.append(" eg_program.parent_id = ? ");
            preparedStmtList.add(programSearch.getParentId());
        }
        addClauseIfRequired(programSearchQuery, preparedStmtList);
        programSearchQuery.append(" eg_program.status = ? ");
        if (programSearch.getStatus() != null) {
            preparedStmtList.add(programSearch.getStatus().toString());
        } else {
            preparedStmtList.add(Status.ACTIVE.toString());
        }

        programSearchQuery.append(" ORDER BY ").append("eg_program.").append(programSearch.getPagination().getSortBy())
                .append(" ").append(programSearch.getPagination().getSortOrder().toString()).append(" LIMIT ? OFFSET ? ");
        preparedStmtList.add(programSearch.getPagination().getLimit());
        preparedStmtList.add(programSearch.getPagination().getOffset());

        return programSearchQuery.toString();
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
