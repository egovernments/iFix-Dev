package org.egov.web.models.persist;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentEntityRelationship {

    @Size(max = 64)
    private String parentId = null;

    @Size(max = 64)
    private String childId = null;

    @Size(max = 64)
    private Boolean status = null;

}
