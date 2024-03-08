package org.digit.program.repository.rowmapper;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.digit.program.models.program.Program;
import org.digit.program.models.Status;
import org.digit.program.utils.CommonUtil;
import org.egov.common.contract.models.AuditDetails;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Component
@Slf4j
public class ProgramRowMapper implements ResultSetExtractor<List<Program>> {

    private final CommonUtil commonUtil;

    public ProgramRowMapper(CommonUtil commonUtil) {
        this.commonUtil = commonUtil;
    }

    @Override
    public List<Program> extractData(ResultSet rs) throws SQLException, DataAccessException {
        List<Program> programs = new ArrayList<>();
        while (rs.next()) {

            Program program = new Program();
            String id = rs.getString("id");
            String locationCode = rs.getString("location_code");
            String programCode = rs.getString("program_code");
            String name = rs.getString("name");
            String parentId = rs.getString("parent_id");
            String description = rs.getString("description");
            long startDate = rs.getLong("start_date");
            long endDate = rs.getLong("end_date");
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
            String status = rs.getString("status");
            String statusMessage = rs.getString("status_message");

            Status status1 = Status.builder().statusCode(org.digit.program.constants.Status.valueOf(status)).statusMessage(statusMessage).build();

            AuditDetails auditDetails = AuditDetails.builder().createdTime(createdTime).lastModifiedTime(lastModifiedTime).createdBy(createdBy).lastModifiedBy(lastModifiedBy).build();
            program.setId(id);
            program.setLocationCode(locationCode);
            program.setProgramCode(programCode);
            program.setName(name);
            program.setParentId(parentId);
            program.setDescription(description);
            program.setStartDate(startDate);
            program.setEndDate(endDate);
            program.setAdditionalDetails(additionalDetails);
            program.setAuditDetails(auditDetails);

            program.setFunctionCode(functionCode);
            program.setAdministrationCode(administrationCode);
            program.setRecipientSegmentCode(recipientSegmentCode);
            program.setEconomicSegmentCode(economicSegmentCode);
            program.setSourceOfFundCode(sourceOfFundCode);
            program.setTargetSegmentCode(targetSegmentCode);
            program.setStatus(status1);

            programs.add(program);

        }
        return programs;
    }
}
