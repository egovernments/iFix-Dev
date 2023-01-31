package org.egov.web.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectDepartmentEntityRelationshipDTO {
    @JsonProperty("departmentEntityId")
    private String departmentEntityId = null;

    @JsonProperty("status")
    private Boolean status = null;
}
