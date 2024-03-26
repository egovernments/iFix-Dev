package org.digit.program.repository.querybuilder;

import org.digit.program.models.allocation.Allocation;
import org.digit.program.models.disburse.Disbursement;
import org.digit.program.models.program.Program;
import org.digit.program.models.sanction.Sanction;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ExchangeCodeQueryBuilder {

    public static final String EXCHANGE_CODE_INSERT_QUERY = " INSERT INTO eg_program_message_codes " +
            " ( id, location_code, reference_id, type, function_code, administration_code, program_code, " +
            " recipient_segment_code, economic_segment_code, source_of_fund_code, target_segment_code, currency_code, " +
            " locale_code, created_by, last_modified_by, created_time, last_modified_time ) " +
            " VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ) ";

    public static final String EXCHANGE_CODE_UPDATE_QUERY = " UPDATE eg_program_message_codes " +
            " SET function_code = ?, administration_code = ?, recipient_segment_code = ?, " +
            " economic_segment_code = ?, source_of_fund_code = ?, target_segment_code = ?, currency_code = ?, " +
            " locale_code = ?, last_modified_by = ?, last_modified_time = ? " +
            " WHERE id = ? ";

    public static final String ON_PROGRAM_UPDATE_QUERY = " UPDATE eg_program_message_codes " +
            " SET program_code = ?, function_code = ?, administration_code = ?, recipient_segment_code = ?, " +
            " economic_segment_code = ?, source_of_fund_code = ?, target_segment_code = ?, currency_code = ?, " +
            " locale_code = ?, last_modified_by = ?, last_modified_time = ? " +
            " WHERE id = ? ";

    public String buildExchangeCodeProgramInsertQuery(Program program, List<Object> preparedStmtList) {
        preparedStmtList.add(program.getId());
        preparedStmtList.add(program.getLocationCode());
        preparedStmtList.add(program.getId());
        preparedStmtList.add(program.getType().toString());
        preparedStmtList.add(program.getFunctionCode());
        preparedStmtList.add(program.getAdministrationCode());
        preparedStmtList.add(program.getProgramCode());
        preparedStmtList.add(program.getRecipientSegmentCode());
        preparedStmtList.add(program.getEconomicSegmentCode());
        preparedStmtList.add(program.getSourceOfFundCode());
        preparedStmtList.add(program.getTargetSegmentCode());
        preparedStmtList.add(program.getCurrencyCode());
        preparedStmtList.add(program.getLocaleCode());
        preparedStmtList.add(program.getAuditDetails().getCreatedBy());
        preparedStmtList.add(program.getAuditDetails().getLastModifiedBy());
        preparedStmtList.add(program.getAuditDetails().getCreatedTime());
        preparedStmtList.add(program.getAuditDetails().getLastModifiedTime());
        return EXCHANGE_CODE_INSERT_QUERY;
    }

    public String buildExchangeCodeProgramUpdateQuery(Program program, List<Object> preparedStmtList, Boolean isOnProgramCreate) {
        if (Boolean.TRUE.equals(isOnProgramCreate)) {
            preparedStmtList.add(program.getProgramCode());
        }
        preparedStmtList.add(program.getFunctionCode());
        preparedStmtList.add(program.getAdministrationCode());
        preparedStmtList.add(program.getRecipientSegmentCode());
        preparedStmtList.add(program.getEconomicSegmentCode());
        preparedStmtList.add(program.getSourceOfFundCode());
        preparedStmtList.add(program.getTargetSegmentCode());
        preparedStmtList.add(program.getCurrencyCode());
        preparedStmtList.add(program.getLocaleCode());
        preparedStmtList.add(program.getAuditDetails().getLastModifiedBy());
        preparedStmtList.add(program.getAuditDetails().getLastModifiedTime());
        preparedStmtList.add(program.getId());
        return Boolean.TRUE.equals(isOnProgramCreate) ? ON_PROGRAM_UPDATE_QUERY : EXCHANGE_CODE_UPDATE_QUERY;
    }

    public String buildExchangeCodeSanctionInsertQuery (Sanction sanction, List<Object> preparedStmtList) {
        preparedStmtList.add(sanction.getId());
        preparedStmtList.add(sanction.getLocationCode());
        preparedStmtList.add(sanction.getId());
        preparedStmtList.add(sanction.getType().toString());
        preparedStmtList.add(sanction.getFunctionCode());
        preparedStmtList.add(sanction.getAdministrationCode());
        preparedStmtList.add(sanction.getProgramCode());
        preparedStmtList.add(sanction.getRecipientSegmentCode());
        preparedStmtList.add(sanction.getEconomicSegmentCode());
        preparedStmtList.add(sanction.getSourceOfFundCode());
        preparedStmtList.add(sanction.getTargetSegmentCode());
        preparedStmtList.add(sanction.getCurrencyCode());
        preparedStmtList.add(sanction.getLocaleCode());
        preparedStmtList.add(sanction.getAuditDetails().getCreatedBy());
        preparedStmtList.add(sanction.getAuditDetails().getLastModifiedBy());
        preparedStmtList.add(sanction.getAuditDetails().getCreatedTime());
        preparedStmtList.add(sanction.getAuditDetails().getLastModifiedTime());
        return EXCHANGE_CODE_INSERT_QUERY;
    }

    public String buildExchangeCodeSanctionUpdateQuery (Sanction sanction, List<Object> preparedStmtList) {
        preparedStmtList.add(sanction.getFunctionCode());
        preparedStmtList.add(sanction.getAdministrationCode());
        preparedStmtList.add(sanction.getRecipientSegmentCode());
        preparedStmtList.add(sanction.getEconomicSegmentCode());
        preparedStmtList.add(sanction.getSourceOfFundCode());
        preparedStmtList.add(sanction.getTargetSegmentCode());
        preparedStmtList.add(sanction.getCurrencyCode());
        preparedStmtList.add(sanction.getLocaleCode());
        preparedStmtList.add(sanction.getAuditDetails().getLastModifiedBy());
        preparedStmtList.add(sanction.getAuditDetails().getLastModifiedTime());
        preparedStmtList.add(sanction.getId());
        return EXCHANGE_CODE_UPDATE_QUERY;
    }

    public String buildExchangeCodeAllocationInsertQuery (Allocation allocation, List<Object> preparedStmtList) {
        preparedStmtList.add(allocation.getId());
        preparedStmtList.add(allocation.getLocationCode());
        preparedStmtList.add(allocation.getId());
        preparedStmtList.add(allocation.getType().toString());
        preparedStmtList.add(allocation.getFunctionCode());
        preparedStmtList.add(allocation.getAdministrationCode());
        preparedStmtList.add(allocation.getProgramCode());
        preparedStmtList.add(allocation.getRecipientSegmentCode());
        preparedStmtList.add(allocation.getEconomicSegmentCode());
        preparedStmtList.add(allocation.getSourceOfFundCode());
        preparedStmtList.add(allocation.getTargetSegmentCode());
        preparedStmtList.add(allocation.getCurrencyCode());
        preparedStmtList.add(allocation.getLocaleCode());
        preparedStmtList.add(allocation.getAuditDetails().getCreatedBy());
        preparedStmtList.add(allocation.getAuditDetails().getLastModifiedBy());
        preparedStmtList.add(allocation.getAuditDetails().getCreatedTime());
        preparedStmtList.add(allocation.getAuditDetails().getLastModifiedTime());
        return EXCHANGE_CODE_INSERT_QUERY;
    }

    public String buildExchangeCodeAllocationUpdateQuery (Allocation allocation, List<Object> preparedStmtList) {
        preparedStmtList.add(allocation.getFunctionCode());
        preparedStmtList.add(allocation.getAdministrationCode());
        preparedStmtList.add(allocation.getRecipientSegmentCode());
        preparedStmtList.add(allocation.getEconomicSegmentCode());
        preparedStmtList.add(allocation.getSourceOfFundCode());
        preparedStmtList.add(allocation.getTargetSegmentCode());
        preparedStmtList.add(allocation.getCurrencyCode());
        preparedStmtList.add(allocation.getLocaleCode());
        preparedStmtList.add(allocation.getAuditDetails().getLastModifiedBy());
        preparedStmtList.add(allocation.getAuditDetails().getLastModifiedTime());
        preparedStmtList.add(allocation.getId());
        return EXCHANGE_CODE_UPDATE_QUERY;
    }

    public String buildExchangeCodeDisburseInsertQuery (Disbursement disbursement, List<Object> preparedStmtList) {
        preparedStmtList.add(disbursement.getId());
        preparedStmtList.add(disbursement.getLocationCode());
        preparedStmtList.add(disbursement.getId());
        preparedStmtList.add(disbursement.getType().toString());
        preparedStmtList.add(disbursement.getFunctionCode());
        preparedStmtList.add(disbursement.getAdministrationCode());
        preparedStmtList.add(disbursement.getProgramCode());
        preparedStmtList.add(disbursement.getRecipientSegmentCode());
        preparedStmtList.add(disbursement.getEconomicSegmentCode());
        preparedStmtList.add(disbursement.getSourceOfFundCode());
        preparedStmtList.add(disbursement.getTargetSegmentCode());
        preparedStmtList.add(disbursement.getCurrencyCode());
        preparedStmtList.add(disbursement.getLocaleCode());
        preparedStmtList.add(disbursement.getAuditDetails().getCreatedBy());
        preparedStmtList.add(disbursement.getAuditDetails().getLastModifiedBy());
        preparedStmtList.add(disbursement.getAuditDetails().getCreatedTime());
        preparedStmtList.add(disbursement.getAuditDetails().getLastModifiedTime());
        return EXCHANGE_CODE_INSERT_QUERY;
    }
}
