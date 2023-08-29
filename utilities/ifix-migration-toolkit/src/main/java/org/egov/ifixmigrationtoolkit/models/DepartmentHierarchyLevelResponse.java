package org.egov.ifixmigrationtoolkit.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.egov.common.contract.response.ResponseHeader;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DepartmentHierarchyLevelResponse {
    @JsonProperty("responseHeader")
    private ResponseHeader responseHeader = null;

    @JsonProperty("departmentHierarchyLevel")
    @Valid
    private List<DepartmentHierarchyLevel> departmentHierarchyLevel = null;


    public DepartmentHierarchyLevelResponse addDepartmentHierarchyLevelItem(DepartmentHierarchyLevel departmentHierarchyLevelItem) {
        if (this.departmentHierarchyLevel == null) {
            this.departmentHierarchyLevel = new ArrayList<>();
        }
        this.departmentHierarchyLevel.add(departmentHierarchyLevelItem);
        return this;
    }

}
