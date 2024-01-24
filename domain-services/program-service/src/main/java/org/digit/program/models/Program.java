package org.digit.program.models;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import lombok.*;
import org.egov.common.contract.models.AuditDetails;

@Getter
@Setter
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Program extends ExchangeCode {

    @JsonProperty("location_code")
    private String locationCode;

    @JsonProperty("program_code")
    private String programCode;

    @JsonProperty("name")
    private String name;

    @JsonProperty("parent_id")
    private String parentId;

    @JsonProperty("start_date")
    private long startDate;

    @JsonProperty("end_date")
    private long endDate;

    @JsonProperty("client_host_url")
    private String clientHostUrl;

    @JsonProperty("audit_details")
    @Embedded
    private AuditDetails auditDetails;

    public Program(JsonNode jsonNode) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
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
        this.setStartDate(program.getStartDate());
        this.setEndDate(program.getEndDate());
        this.setClientHostUrl(program.getClientHostUrl());
        this.setAuditDetails(program.getAuditDetails());
    }

    public JsonNode toJsonNode (Program program) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        JsonNode jsonNode;
        jsonNode = mapper.valueToTree(program);
        return jsonNode;
    }
}
