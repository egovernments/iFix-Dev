package org.digit.program.models.sanction;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.digit.program.models.ExchangeCode;
import org.egov.common.contract.models.AuditDetails;

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
    private Double sanctionedAmount;

    @JsonProperty("allocated_amount")
    private Double allocatedAmount;

    @JsonProperty("available_amount")
    private Double availableAmount;

    @JsonProperty("audit_details")
    private AuditDetails auditDetails;


}
