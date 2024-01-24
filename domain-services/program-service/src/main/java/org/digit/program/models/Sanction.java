package org.digit.program.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.egov.common.contract.models.AuditDetails;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Sanction extends ExchangeCode {

    @JsonProperty("location_code")
    @NotNull
    @Size(min = 2, max = 64)
    private String locationCode;

    @JsonProperty("program_code")
    @NotNull
    @Size(min = 2, max = 64)
    private String programCode;

    @JsonProperty("sanctioned_amount")
    @NotNull
    private long sanctionedAmount;

    @JsonProperty("allocated_amount")
    private long allocatedAmount;

    @JsonProperty("available_amount")
    private long availableAmount;

    @JsonProperty("audit_details")
    private AuditDetails auditDetails;

    public Sanction(JsonNode jsonNode) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        Sanction sanction;
        try {
            sanction = mapper.treeToValue(jsonNode, Sanction.class);
        }
        catch (JsonMappingException e) {
            throw new RuntimeException(e);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        // parent class fields
        this.setId(sanction.getId());
        this.setFunctionCode(sanction.getFunctionCode());
        this.setAdministrationCode(sanction.getAdministrationCode());
        this.setRecipientSegmentCode(sanction.getRecipientSegmentCode());
        this.setEconomicSegmentCode(sanction.getEconomicSegmentCode());
        this.setSourceOfFundCode(sanction.getSourceOfFundCode());
        this.setTargetSegmentCode(sanction.getTargetSegmentCode());
        this.setCurrencyCode(sanction.getCurrencyCode());
        this.setLocaleCode(sanction.getLocaleCode());
        this.setStatus(sanction.getStatus());

        // child class fields
        this.setLocationCode(sanction.getLocationCode());
        this.setProgramCode(sanction.getProgramCode());
        this.setSanctionedAmount(sanction.getSanctionedAmount());
        this.setAllocatedAmount(sanction.getAllocatedAmount());
        this.setAvailableAmount(sanction.getAvailableAmount());
        this.setAuditDetails(sanction.getAuditDetails());
    }


}
