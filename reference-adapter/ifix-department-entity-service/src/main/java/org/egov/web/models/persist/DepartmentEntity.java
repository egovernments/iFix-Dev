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
//@Table(name = "department_entity")
public class DepartmentEntity {

    private String id = null;

    @Size(max = 64)
    private String tenantId = null;

    @Size(max = 64)
    private String departmentId = null;

    @Size(max = 64)
    private String code = null;

    private String name = null;

    private Integer hierarchyLevel = null;

    private String createdBy = null;
    private String lastModifiedBy = null;
    private Long createdTime = null;
    private Long lastModifiedTime = null;

}


