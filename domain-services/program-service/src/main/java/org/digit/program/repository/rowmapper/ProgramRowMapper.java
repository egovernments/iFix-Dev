package org.digit.program.repository.rowmapper;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.digit.program.constants.MessageType;
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
            String id = rs.getString("eg_program_id");
            String locationCode = rs.getString("eg_program_location_code");
            String programCode = rs.getString("eg_program_code");
            String name = rs.getString("eg_program_name");
            String parentId = rs.getString("eg_program_parent_id");
            String description = rs.getString("eg_program_description");
            long startDate = rs.getLong("eg_program_start_date");
            long endDate = rs.getLong("eg_program_end_date");
            JsonNode additionalDetails = commonUtil.getJsonNode(rs, "eg_program_additional_details");
            String createdBy = rs.getString("eg_program_created_by");
            String lastModifiedBy = rs.getString("eg_program_last_modified_by");
            Long createdTime = rs.getLong("eg_program_created_time");
            Long lastModifiedTime = rs.getLong("eg_program_last_modified_time");

            String type = rs.getString("eg_program_message_codes_type");
            String functionCode = rs.getString("eg_program_message_codes_function_code");
            String administrationCode = rs.getString("eg_program_message_codes_administration_code");
            String recipientSegmentCode = rs.getString("eg_program_message_codes_recipient_segment_code");
            String economicSegmentCode = rs.getString("eg_program_message_codes_economic_segment_code");
            String sourceOfFundCode = rs.getString("eg_program_message_codes_source_of_fund_code");
            String targetSegmentCode = rs.getString("eg_program_message_codes_target_segment_code");
            String currencyCode = rs.getString("eg_program_message_codes_currency_code");
            String localeCode = rs.getString("eg_program_message_codes_locale_code");
            String status = rs.getString("eg_program_status");
            String statusMessage = rs.getString("eg_program_status_message");

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

            program.setType(MessageType.fromValue(type));
            program.setFunctionCode(functionCode);
            program.setAdministrationCode(administrationCode);
            program.setRecipientSegmentCode(recipientSegmentCode);
            program.setEconomicSegmentCode(economicSegmentCode);
            program.setSourceOfFundCode(sourceOfFundCode);
            program.setTargetSegmentCode(targetSegmentCode);
            program.setCurrencyCode(currencyCode);
            program.setLocaleCode(localeCode);
            program.setStatus(status1);

            programs.add(program);

        }
        return programs;
    }
}
