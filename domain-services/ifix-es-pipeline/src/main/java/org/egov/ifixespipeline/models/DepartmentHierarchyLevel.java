package org.egov.ifixespipeline.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.egov.common.contract.AuditDetails;

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
