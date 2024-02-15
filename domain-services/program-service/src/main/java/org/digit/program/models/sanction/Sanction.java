package org.digit.program.models.sanction;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.digit.program.models.ExchangeCode;
import org.egov.common.contract.models.AuditDetails;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
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
    @DecimalMin(value = "0.0", inclusive = false, message = "Amount must be greater than 0")
    private Double sanctionedAmount;

    @JsonProperty("allocated_amount")
    @DecimalMin(value = "0.0", message = "Amount must be equal to 0")
    @DecimalMax(value = "0.0", message = "Amount must be equal to 0")
    private Double allocatedAmount;

    @JsonProperty("available_amount")
    @DecimalMin(value = "0.0", message = "Amount must be equal to 0")
    @DecimalMax(value = "0.0", message = "Amount must be equal to 0")
    private Double availableAmount;

    @JsonProperty("additional_details")
    private JsonNode additionalDetails;

    @JsonProperty("audit_details")
    private AuditDetails auditDetails;


}
