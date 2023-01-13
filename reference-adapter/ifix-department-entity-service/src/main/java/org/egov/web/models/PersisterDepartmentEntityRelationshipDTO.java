package org.egov.web.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.validation.constraints.Size;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PersisterDepartmentEntityRelationshipDTO {

    @JsonProperty("parentId")
    private String parentId = null;

    @JsonProperty("childId")
    private String childId = null;

    @JsonProperty("isTrue")
    private Boolean isTrue = null;

}

