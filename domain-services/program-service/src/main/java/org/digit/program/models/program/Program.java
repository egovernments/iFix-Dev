package org.digit.program.models.program;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.*;
import org.digit.program.models.ExchangeCode;
import org.egov.common.contract.models.AuditDetails;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
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
    @Min(value = 1, message = "Value must be greater than 0")
    private long startDate;

    @JsonProperty("end_date")
    private long endDate;

    @JsonProperty("client_host_url")
    @Size(min = 2, max = 128)
    private String clientHostUrl;

    @JsonProperty("is_active")
    private boolean isActive;

    @JsonProperty("additional_details")
    private JsonNode additionalDetails;

    @JsonProperty("audit_details")
    private AuditDetails auditDetails;
}
