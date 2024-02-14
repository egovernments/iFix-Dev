package org.digit.program.repository.rowmapper;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.digit.program.models.sanction.Sanction;
import org.digit.program.models.Status;
import org.digit.program.utils.CommonUtil;
import org.egov.common.contract.models.AuditDetails;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class SanctionRowMapper implements ResultSetExtractor<List<Sanction>> {

    private final CommonUtil commonUtil;

    public SanctionRowMapper(CommonUtil commonUtil) {
        this.commonUtil = commonUtil;
    }


    @Override
    public List<Sanction> extractData(ResultSet rs) throws SQLException, DataAccessException {
        List<Sanction> sanctions = new ArrayList<>();
        while (rs.next()) {
            Sanction sanction = new Sanction();
            String id = rs.getString("id");
            String locationCode = rs.getString("location_code");
            String programCode = rs.getString("program_code");
            Double sanctionAmount = rs.getDouble("sanctioned_amount");
            Double allocatedAmount = rs.getDouble("allocated_amount");
            Double availableAmount = rs.getDouble("available_amount");
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

            sanction.setId(id);
            sanction.setLocationCode(locationCode);
            sanction.setProgramCode(programCode);
            sanction.setSanctionedAmount(sanctionAmount);
            sanction.setAllocatedAmount(allocatedAmount);
            sanction.setAvailableAmount(availableAmount);
            sanction.setStatus(status1);
            sanction.setAdditionalDetails(additionalDetails);
            sanction.setAuditDetails(auditDetails);

            sanction.setFunctionCode(functionCode);
            sanction.setAdministrationCode(administrationCode);
            sanction.setRecipientSegmentCode(recipientSegmentCode);
            sanction.setEconomicSegmentCode(economicSegmentCode);
            sanction.setSourceOfFundCode(sourceOfFundCode);
            sanction.setTargetSegmentCode(targetSegmentCode);

            sanctions.add(sanction);
        }
        return sanctions;
    }
}
