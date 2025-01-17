package org.digit.program.models.disburse;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.digit.program.models.ExchangeCode;
import org.egov.common.contract.models.AuditDetails;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;

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

    @JsonProperty("parent_id")
    @Size(min = 2, max = 64)
    private String parentId;

    @JsonProperty("sanction_id")
    @Size(min = 2, max = 64)
    private String sanctionId;

    @JsonProperty("transaction_id")
    @Size(min = 2, max = 64)
    private String transactionId;

    @JsonProperty("account_code")
    @Size(min = 2, max = 64)
    private String accountCode;

    @JsonProperty("net_amount")
    @NotNull
    @DecimalMin(value = "0.0", inclusive = false, message = "Amount must be greater than 0")
    private Double netAmount;

    @JsonProperty("gross_amount")
    @NotNull
    @DecimalMin(value = "0.0", inclusive = false, message = "Amount must be greater than 0")
    private Double grossAmount;

    @JsonProperty("individual")
    @Valid
    private Individual individual;

    @JsonProperty("additional_details")
    private JsonNode additionalDetails;

    @JsonProperty("audit_details")
    private AuditDetails auditDetails;

    @JsonProperty("children")
    @Valid
    private List<Disbursement> disbursements;

}
