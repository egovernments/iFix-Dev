package org.digit.program.repository.querybuilder;

import org.apache.commons.lang3.StringUtils;
import org.digit.program.models.program.Program;
import org.digit.program.models.program.ProgramSearch;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

@Component
public class ProgramQueryBuilder {
    public static final String PROGRAM_INSERT_QUERY = "INSERT INTO eg_program " +
            "( id, location_code, program_code, name, parent_id, description, client_host_url, status, status_message, " +
            " start_date, end_date, is_active, created_by, last_modified_by, created_time, last_modified_time ) " +
            " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ) ";

    public static final String PROGRAM_UPDATE_QUERY = "UPDATE eg_program " +
            " SET program_code = ?, name = ?, description = ?, client_host_url = ?, status = ?, status_message = ?, " +
            " end_date = ?, is_active = ?, last_modified_by = ?, last_modified_time = ? " +
            " WHERE id = ? ";

    public static final String PROGRAM_SEARCH_QUERY = "SELECT * FROM eg_program JOIN eg_program_message_codes " +
            " ON eg_program.id = eg_program_message_codes.reference_id ";


    public String buildProgramInsertQuery(Program program, List<Object> preparedStmtList) {
        preparedStmtList.add(program.getId());
        preparedStmtList.add(program.getLocationCode());
        preparedStmtList.add(program.getProgramCode());
        preparedStmtList.add(program.getName());
        preparedStmtList.add(program.getParentId());
        preparedStmtList.add(program.getDescription());
        preparedStmtList.add(program.getClientHostUrl());
        preparedStmtList.add(program.getStatus().getStatusCode().toString());
        preparedStmtList.add(program.getStatus().getStatusMessage());
        preparedStmtList.add(program.getStartDate());
        preparedStmtList.add(program.getEndDate());
        preparedStmtList.add(program.isActive());
        preparedStmtList.add(program.getAuditDetails().getCreatedBy());
        preparedStmtList.add(program.getAuditDetails().getLastModifiedBy());
        preparedStmtList.add(program.getAuditDetails().getCreatedTime());
        preparedStmtList.add(program.getAuditDetails().getLastModifiedTime());
        return PROGRAM_INSERT_QUERY;
    }



    public String buildProgramUpdateQuery(Program program, List<Object> preparedStmtList) {
        preparedStmtList.add(program.getProgramCode());
        preparedStmtList.add(program.getName());
        preparedStmtList.add(program.getDescription());
        preparedStmtList.add(program.getClientHostUrl());
        preparedStmtList.add(program.getStatus().getStatusCode().toString());
        preparedStmtList.add(program.getStatus().getStatusMessage());
        preparedStmtList.add(program.getEndDate());
        preparedStmtList.add(program.isActive());
        preparedStmtList.add(program.getAuditDetails().getLastModifiedBy());
        preparedStmtList.add(program.getAuditDetails().getLastModifiedTime());
        preparedStmtList.add(program.getId());
        return PROGRAM_UPDATE_QUERY;
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

        programSearchQuery.append(" ORDER BY ? ");
        preparedStmtList.add(programSearch.getPagination().getSortBy());
        programSearchQuery.append(programSearch.getPagination().getSortOrder().toString());
        programSearchQuery.append(" LIMIT ? OFFSET ? ");
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
