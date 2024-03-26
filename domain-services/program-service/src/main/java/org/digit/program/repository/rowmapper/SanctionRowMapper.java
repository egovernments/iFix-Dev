package org.digit.program.repository.rowmapper;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.digit.program.constants.MessageType;
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
            String id = rs.getString("sanction_id");
            String locationCode = rs.getString("sanction_location_code");
            String programCode = rs.getString("sanction_program_code");
            Double netAmount = rs.getDouble("net_amount");
            Double grossAmount = rs.getDouble("gross_amount");
            Double allocatedAmount = rs.getDouble("allocated_amount");
            Double availableAmount = rs.getDouble("available_amount");
            String status = rs.getString("sanction_status");
            String statusMessage = rs.getString("sanction_status_message");
            JsonNode additionalDetails = commonUtil.getJsonNode(rs, "sanction_additional_details");
            String createdBy = rs.getString("sanction_created_by");
            String lastModifiedBy = rs.getString("sanction_last_modified_by");
            Long createdTime = rs.getLong("sanction_created_time");
            Long lastModifiedTime = rs.getLong("sanction_last_modified_time");

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

            sanction.setId(id);
            sanction.setLocationCode(locationCode);
            sanction.setProgramCode(programCode);
            sanction.setNetAmount(netAmount);
            sanction.setGrossAmount(grossAmount);
            sanction.setAllocatedAmount(allocatedAmount);
            sanction.setAvailableAmount(availableAmount);
            sanction.setStatus(status1);
            sanction.setAdditionalDetails(additionalDetails);
            sanction.setAuditDetails(auditDetails);

            sanction.setType(MessageType.fromValue(type));
            sanction.setFunctionCode(functionCode);
            sanction.setAdministrationCode(administrationCode);
            sanction.setRecipientSegmentCode(recipientSegmentCode);
            sanction.setEconomicSegmentCode(economicSegmentCode);
            sanction.setSourceOfFundCode(sourceOfFundCode);
            sanction.setTargetSegmentCode(targetSegmentCode);
            sanction.setCurrencyCode(currencyCode);
            sanction.setLocaleCode(localeCode);

            sanctions.add(sanction);
        }
        return sanctions;
    }
}
