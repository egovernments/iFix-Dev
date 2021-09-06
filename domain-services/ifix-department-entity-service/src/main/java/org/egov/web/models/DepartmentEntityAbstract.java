package org.egov.web.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.egov.common.contract.AuditDetails;
import org.springframework.validation.annotation.Validated;

/**
 * This object captures the information for department entity
 */
@ApiModel(description = "This object captures the information for department entity")
@Validated
@javax.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2021-08-23T11:51:49.710+05:30")

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public abstract class DepartmentEntityAbstract {
    @JsonProperty("id")
    private String id = null;

    @JsonProperty("tenantId")
    private String tenantId = null;

    @JsonProperty("departmentId")
    private String departmentId = null;

    @JsonProperty("code")
    private String code = null;

    @JsonProperty("name")
    private String name = null;

    @JsonProperty("hierarchyLevel")
    private Integer hierarchyLevel = null;

    @JsonProperty("auditDetails")
    private AuditDetails auditDetails = null;

}

