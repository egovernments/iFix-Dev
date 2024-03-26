package org.digit.program.repository.rowmapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.digit.program.constants.MessageType;
import org.digit.program.models.disburse.Disbursement;
import org.digit.program.models.disburse.Individual;
import org.digit.program.models.Status;
import org.digit.program.utils.CommonUtil;
import org.egov.common.contract.models.AuditDetails;
import org.egov.tracer.model.CustomException;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class DisburseRowMapper implements ResultSetExtractor<List<Disbursement>> {

    private final ObjectMapper objectMapper;
    private final CommonUtil commonUtil;

    public DisburseRowMapper(ObjectMapper objectMapper, CommonUtil commonUtil) {
        this.objectMapper = objectMapper;
        this.commonUtil = commonUtil;
    }


    @Override
    public List<Disbursement> extractData(ResultSet rs) throws SQLException, DataAccessException {
        List<Disbursement> disbursements = new ArrayList<>();
        while (rs.next()) {
            Disbursement disbursement = new Disbursement();
            String id = rs.getString("disburse_id");
            String locationCode = rs.getString("disburse_location_code");
            String programCode = rs.getString("disburse_program_code");
            String parentId = rs.getString("disburse_parent_id");
            String targetId = rs.getString("disburse_target_id");
            String transactionId = rs.getString("disburse_transaction_id");
            String sanctionId = rs.getString("disburse_sanction_id");
            String accountCode = rs.getString("disburse_account_code");
            JsonNode individual = commonUtil.getJsonNode(rs, "disburse_individual");
            Double netAmount = rs.getDouble("disburse_net_amount");
            Double grossAmount = rs.getDouble("disburse_gross_amount");
            String status = rs.getString("disburse_status");
            String statusMessage = rs.getString("disburse_status_message");
            JsonNode additionalDetails = commonUtil.getJsonNode(rs, "disburse_additional_details");
            String createdBy = rs.getString("disburse_created_by");
            String lastModifiedBy = rs.getString("disburse_last_modified_by");
            Long createdTime = rs.getLong("disburse_created_time");
            Long lastModifiedTime = rs.getLong("disburse_last_modified_time");

            String type = rs.getString("message_type");
            String functionCode = rs.getString("message_function_code");
            String administrationCode = rs.getString("message_administration_code");
            String recipientSegmentCode = rs.getString("message_recipient_segment_code");
            String economicSegmentCode = rs.getString("message_economic_segment_code");
            String sourceOfFundCode = rs.getString("message_source_of_fund_code");
            String targetSegmentCode = rs.getString("message_target_segment_code");
            String currencyCode = rs.getString("message_currency_code");
            String localeCode = rs.getString("message_locale_code");

            Status status1 = Status.builder().statusCode(org.digit.program.constants.Status.valueOf(status)).statusMessage(statusMessage).build();
            AuditDetails auditDetails = AuditDetails.builder().createdTime(createdTime).lastModifiedTime(lastModifiedTime).createdBy(createdBy).lastModifiedBy(lastModifiedBy).build();
            Individual individual1;
            try {
                individual1 = objectMapper.treeToValue(individual, Individual.class);
            } catch (JsonProcessingException e) {
                throw new CustomException("JSON_PROCESSING_ERROR", e.getMessage());
            }

            disbursement.setId(id);
            disbursement.setLocationCode(locationCode);
            disbursement.setProgramCode(programCode);
            disbursement.setTargetId(targetId);
            disbursement.setParentId(parentId);
            disbursement.setSanctionId(sanctionId);
            disbursement.setTransactionId(transactionId);
            disbursement.setAccountCode(accountCode);
            disbursement.setIndividual(individual1);
            disbursement.setNetAmount(netAmount);
            disbursement.setGrossAmount(grossAmount);
            disbursement.setStatus(status1);
            disbursement.setAdditionalDetails(additionalDetails);
            disbursement.setAuditDetails(auditDetails);

            disbursement.setType(MessageType.fromValue(type));
            disbursement.setFunctionCode(functionCode);
            disbursement.setAdministrationCode(administrationCode);
            disbursement.setRecipientSegmentCode(recipientSegmentCode);
            disbursement.setEconomicSegmentCode(economicSegmentCode);
            disbursement.setSourceOfFundCode(sourceOfFundCode);
            disbursement.setTargetSegmentCode(targetSegmentCode);
            disbursement.setCurrencyCode(currencyCode);
            disbursement.setLocaleCode(localeCode);

            disbursements.add(disbursement);
        }
        return disbursements;
    }
}
