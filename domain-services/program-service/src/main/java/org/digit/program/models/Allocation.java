package org.digit.program.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.digit.program.constants.AllocationType;
import org.egov.common.contract.models.AuditDetails;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Allocation extends ExchangeCode {

    @JsonProperty("location_code")
    @NotNull
    @Size(min = 2, max = 64)
    private String locationCode;

    @JsonProperty("program_code")
    @NotNull
    @Size(min = 2, max = 64)
    private String programCode;

    @JsonProperty("sanction_id")
    @NotNull
    @Size(min = 2, max = 64)
    private String sanctionId;

    @JsonProperty("amount")
    @NotNull
    private Double amount;

    @JsonProperty("type")
    @NotNull
    private AllocationType type;

    @JsonProperty("audit_details")
    private AuditDetails auditDetails;

    public Allocation(JsonNode jsonNode) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        Allocation allocation;
        try {
            allocation = mapper.treeToValue(jsonNode, Allocation.class);
        }
        catch (JsonMappingException e) {
            throw new RuntimeException(e);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        // parent class fields
        this.setId(allocation.getId());
        this.setFunctionCode(allocation.getFunctionCode());
        this.setAdministrationCode(allocation.getAdministrationCode());
        this.setRecipientSegmentCode(allocation.getRecipientSegmentCode());
        this.setEconomicSegmentCode(allocation.getEconomicSegmentCode());
        this.setSourceOfFundCode(allocation.getSourceOfFundCode());
        this.setTargetSegmentCode(allocation.getTargetSegmentCode());
        this.setCurrencyCode(allocation.getCurrencyCode());
        this.setLocaleCode(allocation.getLocaleCode());
        this.setStatus(allocation.getStatus());

        // child class fields
        this.setLocationCode(allocation.getLocationCode());
        this.setProgramCode(allocation.getProgramCode());
        this.setSanctionId(allocation.getSanctionId());
        this.setAmount(allocation.getAmount());
        this.setType(allocation.getType());
        this.setAuditDetails(allocation.getAuditDetails());
    }

    public JsonNode toJsonNode(Allocation allocation) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        JsonNode jsonNode;
        jsonNode = mapper.valueToTree(allocation);
        return jsonNode;
    }

}
