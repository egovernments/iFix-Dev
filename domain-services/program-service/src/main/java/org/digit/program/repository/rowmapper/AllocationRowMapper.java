package org.digit.program.repository.rowmapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.digit.program.constants.AllocationType;
import org.digit.program.models.Allocation;
import org.digit.program.models.Status;
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

    private ObjectMapper mapper;

    public AllocationRowMapper(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public List<Allocation> extractData(ResultSet rs) throws SQLException, DataAccessException {
        List<Allocation> allocations = new ArrayList<>();
        while (rs.next()) {
            Allocation allocation = new Allocation();
            String id = rs.getString("id");
            String locationCode = rs.getString("location_code");
            String programCode = rs.getString("program_code");
            String sanctionId = rs.getString("sanction_id");
            Double amount = rs.getDouble("amount");
            AllocationType allocationType = AllocationType.valueOf(rs.getString("type"));
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

            allocation.setId(id);
            allocation.setLocationCode(locationCode);
            allocation.setProgramCode(programCode);
            allocation.setSanctionId(sanctionId);
            allocation.setAmount(amount);
            allocation.setType(allocationType);
            allocation.setStatus(status1);
            allocation.setAuditDetails(auditDetails);

            allocation.setFunctionCode(functionCode);
            allocation.setAdministrationCode(administrationCode);
            allocation.setRecipientSegmentCode(recipientSegmentCode);
            allocation.setEconomicSegmentCode(economicSegmentCode);
            allocation.setSourceOfFundCode(sourceOfFundCode);
            allocation.setTargetSegmentCode(targetSegmentCode);

            allocations.add(allocation);

        }
        return allocations;
    }
}
