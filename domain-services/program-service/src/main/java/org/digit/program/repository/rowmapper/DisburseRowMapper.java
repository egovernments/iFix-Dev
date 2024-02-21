package org.digit.program.repository.rowmapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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
            String id = rs.getString("id");
            String locationCode = rs.getString("location_code");
            String programCode = rs.getString("program_code");
            String targetId = rs.getString("target_id");
            String parentId = rs.getString("parent_id");
            String sanctionId = rs.getString("sanction_id");
            String transactionId = rs.getString("transaction_id");
            String accountCode = rs.getString("account_code");
            JsonNode individual = commonUtil.getJsonNode(rs, "individual");
            Double netAmount = rs.getDouble("net_amount");
            Double grossAmount = rs.getDouble("gross_amount");
            String status = rs.getString("status");
            String statusMessage = rs.getString("status_message");
            JsonNode additionalDetails = commonUtil.getJsonNode(rs, "additional_details");
            String createdBy = rs.getString("created_by");
            String lastModifiedBy = rs.getString("last_modified_by");
            Long createdTime = rs.getLong("created_time");
            Long lastModifiedTime = rs.getLong("last_modified_time");

            String functionCode = rs.getString("function_code");
            String administrationCode = rs.getString("administration_code");
            String recipientSegmentCode = rs.getString("recipient_segment_code");
            String economicSegmentCode = rs.getString("economic_segment_code");
            String sourceOfFundCode = rs.getString("source_of_fund_code");
            String targetSegmentCode = rs.getString("target_segment_code");

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

            disbursement.setFunctionCode(functionCode);
            disbursement.setAdministrationCode(administrationCode);
            disbursement.setRecipientSegmentCode(recipientSegmentCode);
            disbursement.setEconomicSegmentCode(economicSegmentCode);
            disbursement.setSourceOfFundCode(sourceOfFundCode);
            disbursement.setTargetSegmentCode(targetSegmentCode);

            disbursements.add(disbursement);
        }
        return disbursements;
    }
}
