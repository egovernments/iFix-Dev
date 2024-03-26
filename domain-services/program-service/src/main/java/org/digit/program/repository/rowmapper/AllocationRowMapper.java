package org.digit.program.repository.rowmapper;

import com.fasterxml.jackson.databind.JsonNode;
import org.digit.program.constants.AllocationType;
import org.digit.program.constants.MessageType;
import org.digit.program.models.allocation.Allocation;
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
public class AllocationRowMapper implements ResultSetExtractor<List<Allocation>> {

    private final CommonUtil commonUtil;

    public AllocationRowMapper(CommonUtil commonUtil) {
        this.commonUtil = commonUtil;
    }

    @Override
    public List<Allocation> extractData(ResultSet rs) throws SQLException, DataAccessException {
        List<Allocation> allocations = new ArrayList<>();
        while (rs.next()) {
            Allocation allocation = new Allocation();
            String id = rs.getString("allocation_id");
            String locationCode = rs.getString("allocation_location_code");
            String programCode = rs.getString("allocation_program_code");
            String sanctionId = rs.getString("allocation_sanction_id");
            Double netAmount = rs.getDouble("allocation_net_amount");
            Double grossAmount = rs.getDouble("allocation_gross_amount");
            AllocationType allocationType = AllocationType.valueOf(rs.getString("allocation_type"));
            String status = rs.getString("allocation_status");
            String statusMessage = rs.getString("allocation_status_message");
            JsonNode additionalDetails = commonUtil.getJsonNode(rs, "allocation_additional_details");
            String createdBy = rs.getString("allocation_created_by");
            String lastModifiedBy = rs.getString("allocation_last_modified_by");
            Long createdTime = rs.getLong("allocation_created_time");
            Long lastModifiedTime = rs.getLong("allocation_last_modified_time");

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

            allocation.setId(id);
            allocation.setLocationCode(locationCode);
            allocation.setProgramCode(programCode);
            allocation.setSanctionId(sanctionId);
            allocation.setNetAmount(netAmount);
            allocation.setGrossAmount(grossAmount);
            allocation.setAllocationType(allocationType);
            allocation.setStatus(status1);
            allocation.setAdditionalDetails(additionalDetails);
            allocation.setAuditDetails(auditDetails);

            allocation.setType(MessageType.fromValue(type));
            allocation.setFunctionCode(functionCode);
            allocation.setAdministrationCode(administrationCode);
            allocation.setRecipientSegmentCode(recipientSegmentCode);
            allocation.setEconomicSegmentCode(economicSegmentCode);
            allocation.setSourceOfFundCode(sourceOfFundCode);
            allocation.setTargetSegmentCode(targetSegmentCode);
            allocation.setCurrencyCode(currencyCode);
            allocation.setLocaleCode(localeCode);

            allocations.add(allocation);

        }
        return allocations;
    }
}
