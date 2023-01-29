package org.egov.ifixmigrationtoolkit.models.adaptermasterdata;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DepartmentEntity {

    @JsonProperty("id")
    private String id = null;

    @JsonProperty("code")
    private String code = null;

    @JsonProperty("name")
    private String name = null;

    @JsonProperty("departmentId")
    private String departmentId;

    @JsonProperty("hierarchyLevel")
    private Integer hierarchyLevel = null;

    @JsonProperty("ancestry")
    private List<DepartmentEntityAttributes> ancestry;

}
