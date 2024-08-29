package org.digit.program.models.program;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.*;
import org.digit.program.models.ExchangeCode;
import org.egov.common.contract.models.AuditDetails;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Size;
import java.util.List;

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

    @JsonProperty("additional_details")
    private JsonNode additionalDetails;

    @JsonProperty("audit_details")
    private AuditDetails auditDetails;

    @JsonProperty("children")
    @Null
    private List<Program> programs;

}
