package org.digit.program.models;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.*;
import org.egov.common.contract.models.AuditDetails;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
@Validated
public class Program extends ExchangeCode {

    @JsonProperty("location_code")
    @NotNull
    @Size(min = 2, max = 64)
    private String locationCode;

    @JsonProperty("program_code")
    @Size(min = 2, max = 64)
    private String programCode;

    @JsonProperty("name")
    @NotNull
    @Size(min = 2, max = 64)
    private String name;

    @JsonProperty("parent_id")
    @Size(min = 2, max = 64)
    private String parentId;

    @JsonProperty("description")
    @Size(min = 2, max = 256)
    private String description;

    @JsonProperty("start_date")
    @NotNull
    private long startDate;

    @JsonProperty("end_date")
    private long endDate;

    @JsonProperty("client_host_url")
    @Size(min = 2, max = 128)
    private String clientHostUrl;

    @JsonProperty("audit_details")
    private AuditDetails auditDetails;

    public Program(JsonNode jsonNode) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        Program program;
        try {
            program = mapper.treeToValue(jsonNode, Program.class);
        }
        catch (JsonMappingException e) {
            throw new RuntimeException(e);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        this.setId(program.getId());
        this.setFunctionCode(program.getFunctionCode());
        this.setAdministrationCode(program.getAdministrationCode());
        this.setRecipientSegmentCode(program.getRecipientSegmentCode());
        this.setEconomicSegmentCode(program.getEconomicSegmentCode());
        this.setSourceOfFundCode(program.getSourceOfFundCode());
        this.setTargetSegmentCode(program.getTargetSegmentCode());
        this.setCurrencyCode(program.getCurrencyCode());
        this.setLocaleCode(program.getLocaleCode());
        this.setStatus(program.getStatus());

        // Set child class fields
        this.setLocationCode(program.getLocationCode());
        this.setProgramCode(program.getProgramCode());
        this.setName(program.getName());
        this.setParentId(program.getParentId());
        this.setDescription(program.getDescription());
        this.setStartDate(program.getStartDate());
        this.setEndDate(program.getEndDate());
        this.setClientHostUrl(program.getClientHostUrl());
        this.setAuditDetails(program.getAuditDetails());
    }

    public JsonNode toJsonNode(Program program) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        JsonNode jsonNode;
        jsonNode = mapper.valueToTree(program);
        return jsonNode;
    }
}
