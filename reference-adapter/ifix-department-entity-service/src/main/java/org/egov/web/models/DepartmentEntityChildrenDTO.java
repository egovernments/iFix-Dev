package org.egov.web.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DepartmentEntityChildrenDTO {
    @JsonProperty("childId")
    private String childId = null;

    @JsonProperty("status")
    private Boolean status = null;
}
