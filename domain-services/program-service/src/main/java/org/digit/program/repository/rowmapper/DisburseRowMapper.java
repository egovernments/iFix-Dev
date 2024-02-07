package org.digit.program.repository.rowmapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.digit.program.models.Disbursement;
import org.digit.program.models.Individual;
import org.digit.program.models.Status;
import org.egov.common.contract.models.AuditDetails;
import org.egov.tracer.model.CustomException;
import org.postgresql.util.PGobject;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class DisburseRowMapper implements ResultSetExtractor<List<Disbursement>> {

    private final ObjectMapper objectMapper;

    public DisburseRowMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
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
            String parentId = rs.getString("disburse_parent_id");
            String sanctionId = rs.getString("sanction_id");
            String accountCode = rs.getString("account_code");
            JsonNode individual = getJsonNode(rs, "individual");
            Double netAmount = rs.getDouble("net_amount");
            Double grossAmount = rs.getDouble("gross_amount");
            String status = rs.getString("status");
            String statusMessage = rs.getString("status_message");
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
                throw new RuntimeException(e);
            }

            disbursement.setId(id);
            disbursement.setLocationCode(locationCode);
            disbursement.setProgramCode(programCode);
            disbursement.setTargetId(targetId);
            disbursement.setParentId(parentId);
            disbursement.setSanctionId(sanctionId);
            disbursement.setAccountCode(accountCode);
            disbursement.setIndividual(individual1);
            disbursement.setNetAmount(netAmount);
            disbursement.setGrossAmount(grossAmount);
            disbursement.setStatus(status1);
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

    private JsonNode getJsonNode(ResultSet rs, String key) throws SQLException {
        JsonNode individual = null;

        try {

            PGobject obj = (PGobject) rs.getObject(key);
            if (obj != null) {
                individual = objectMapper.readTree(obj.getValue());
            }

        } catch (IOException e) {
            throw new CustomException("PARSING_ERROR", "Error while parsing");
        }

        if(individual.isEmpty())
            individual = null;

        return individual;
    }
}
