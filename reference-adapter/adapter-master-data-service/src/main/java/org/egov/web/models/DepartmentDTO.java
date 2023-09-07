package org.egov.web.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import lombok.*;
import org.egov.common.contract.AuditDetails;
import org.springframework.validation.annotation.Validated;

/**
 * Captures the department attributes
 */
@ApiModel(description = "Captures the department attributes")
@Validated
@javax.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2021-08-02T16:24:12.742+05:30")

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DepartmentDTO {
    @JsonProperty("id")
    private String id = null;

    @JsonProperty("tenantId")
    private String tenantId = null;

    @JsonProperty("code")
    private String code = null;

    @JsonProperty("name")
    private String name = null;

    @JsonProperty("isNodal")
    private Boolean isNodal = false;

    @JsonProperty("parent")
    private String parent = null;

    @JsonProperty("auditDetails")
    private AuditDetails auditDetails = null;


}

