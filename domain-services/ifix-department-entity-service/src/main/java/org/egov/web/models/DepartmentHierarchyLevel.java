package org.egov.web.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import lombok.*;
import org.egov.common.contract.AuditDetails;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;

/**
 * This object captures the information for level of the department hierarchy and it&#39;s alias
 */
@ApiModel(description = "This object captures the information for level of the department hierarchy and it's alias")
@Validated
@javax.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2021-08-23T11:51:49.710+05:30")

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DepartmentHierarchyLevel {
    @JsonProperty("id")
    private String id = null;

    @JsonProperty("tenantId")
    private String tenantId = null;

    @JsonProperty("departmentId")
    private String departmentId = null;

    @JsonProperty("label")
    private String label = null;

    @JsonProperty("parent")
    private String parent = null;

    @JsonProperty("level")
    private Integer level = null;

    @JsonProperty("auditDetails")
    private AuditDetails auditDetails = null;


}

