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
import org.egov.common.contract.models.AuditDetails;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Disbursement extends ExchangeCode {

    @JsonProperty("location_code")
    @NotNull
    @Size(min = 2, max = 64)
    private String locationCode;

    @JsonProperty("program_code")
    @NotNull
    @Size(min = 2, max = 64)
    private String programCode;

    @JsonProperty("target_id")
    @NotNull
    @Size(min = 2, max = 64)
    private String targetId;

    @JsonProperty("disbursement_date")
    private String disbursementDate;

    @JsonProperty("sanction_id")
    @Size(min = 2, max = 64)
    private String sanctionId;

    @JsonProperty("account_code")
    @NotNull
    @Size(min = 2, max = 64)
    private String accountCode;

    @JsonProperty("net_amount")
    @NotNull
    private Double netAmount;

    @JsonProperty("gross_amount")
    @NotNull
    private Double grossAmount;

    @JsonProperty("individual")
    @NotNull
    private Individual individual;

//    @JsonProperty("disbursement")
//    private Disbursement disbursement;

    @JsonProperty("audit_details")
    private AuditDetails auditDetails;

    public Disbursement(JsonNode jsonNode) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        Disbursement disbursement;
        try {
            disbursement = mapper.treeToValue(jsonNode, Disbursement.class);
        }
        catch (JsonMappingException e) {
            throw new RuntimeException(e);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        // parent class fields
        this.setId(disbursement.getId());
        this.setFunctionCode(disbursement.getFunctionCode());
        this.setAdministrationCode(disbursement.getAdministrationCode());
        this.setRecipientSegmentCode(disbursement.getRecipientSegmentCode());
        this.setEconomicSegmentCode(disbursement.getEconomicSegmentCode());
        this.setSourceOfFundCode(disbursement.getSourceOfFundCode());
        this.setTargetSegmentCode(disbursement.getTargetSegmentCode());
        this.setCurrencyCode(disbursement.getCurrencyCode());
        this.setLocaleCode(disbursement.getLocaleCode());
        this.setStatus(disbursement.getStatus());

        // child class fields
        this.setLocationCode(disbursement.getLocationCode());
        this.setProgramCode(disbursement.getProgramCode());
        this.setTargetId(disbursement.getTargetId());
        this.setDisbursementDate(disbursement.getDisbursementDate());
        this.setSanctionId(disbursement.getSanctionId());
        this.setAccountCode(disbursement.getAccountCode());
        this.setNetAmount(disbursement.getNetAmount());
        this.setGrossAmount(disbursement.getGrossAmount());
        this.setIndividual(disbursement.getIndividual());
        this.setAuditDetails(disbursement.getAuditDetails());
    }

    public JsonNode toJsonNode(Disbursement disbursement) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        JsonNode jsonNode;
        jsonNode = mapper.valueToTree(disbursement);
        return jsonNode;
    }
}
