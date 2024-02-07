package org.digit.program.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
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



}
