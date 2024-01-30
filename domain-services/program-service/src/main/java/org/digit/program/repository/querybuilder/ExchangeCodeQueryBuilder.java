package org.digit.program.repository.querybuilder;

import org.digit.program.models.Program;

import java.util.List;

public class ExchangeCodeQueryBuilder {

    public static final String EXCHANGE_CODE_INSERT_QUERY = "" +
            "INSERT INTO eg_program_message_codes " +
            "(id, location_code, parent_id, function_code, administration_code, program_code, " +
            "recipient_segment_code, economic_segment_code, source_of_fund_code, target_segment_code, " +
            " created_by, last_modified_by, created_time, last_modified_time) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    public static final String EXCHANGE_CODE_UPDATE_QUERY = "UPDATE eg_program_message_codes " +
            "SET function_code = ?, administration_code = ?, program_code = ?, recipient_segment_code = ?, " +
            "economic_segment_code = ?, source_of_fund_code = ?, target_segment_code = ?, " +
            "last_modified_by = ?, last_modified_time = ? " +
            "WHERE id = ?";

    public String buildExchangeCodeInsertQuery(Program program, List<Object> preparedStmtList) {
        preparedStmtList.add(program.getId());
        preparedStmtList.add(program.getLocationCode());
        preparedStmtList.add(program.getParentId());
        preparedStmtList.add(program.getFunctionCode());
        preparedStmtList.add(program.getAdministrationCode());
        preparedStmtList.add(program.getProgramCode());
        preparedStmtList.add(program.getRecipientSegmentCode());
        preparedStmtList.add(program.getEconomicSegmentCode());
        preparedStmtList.add(program.getSourceOfFundCode());
        preparedStmtList.add(program.getTargetSegmentCode());
        preparedStmtList.add(program.getAuditDetails().getCreatedBy());
        preparedStmtList.add(program.getAuditDetails().getLastModifiedBy());
        preparedStmtList.add(program.getAuditDetails().getCreatedTime());
        preparedStmtList.add(program.getAuditDetails().getLastModifiedTime());
        preparedStmtList.add(program.getId());
        return EXCHANGE_CODE_INSERT_QUERY;
    }

    public String buildExchangeCodeUpdateQuery(Program program, List<Object> preparedStmtList) {
        preparedStmtList.add(program.getFunctionCode());
        preparedStmtList.add(program.getAdministrationCode());
        preparedStmtList.add(program.getProgramCode());
        preparedStmtList.add(program.getRecipientSegmentCode());
        preparedStmtList.add(program.getEconomicSegmentCode());
        preparedStmtList.add(program.getSourceOfFundCode());
        preparedStmtList.add(program.getTargetSegmentCode());
        preparedStmtList.add(program.getAuditDetails().getLastModifiedBy());
        preparedStmtList.add(program.getAuditDetails().getLastModifiedTime());
        preparedStmtList.add(program.getId());
        return EXCHANGE_CODE_UPDATE_QUERY;
    }

}
