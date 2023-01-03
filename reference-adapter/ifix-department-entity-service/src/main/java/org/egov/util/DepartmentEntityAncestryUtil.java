package org.egov.util;

import lombok.extern.slf4j.Slf4j;
import org.egov.web.models.DepartmentEntity;
import org.egov.web.models.DepartmentEntityAncestry;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
@Slf4j
public class DepartmentEntityAncestryUtil {

    public DepartmentEntityAncestry createDepartmentEntityAncestryFromDepartmentEntity(DepartmentEntity departmentEntity) {
        DepartmentEntityAncestry departmentEntityAncestry = DepartmentEntityAncestry.builder().build();
        departmentEntityAncestry.setId(departmentEntity.getId());
        departmentEntityAncestry.setTenantId(departmentEntity.getTenantId());
        departmentEntityAncestry.setDepartmentId(departmentEntity.getDepartmentId());
        departmentEntityAncestry.setCode(departmentEntity.getCode());
        departmentEntityAncestry.setName(departmentEntity.getName());
        departmentEntityAncestry.setHierarchyLevel(departmentEntity.getHierarchyLevel());
        departmentEntityAncestry.setAuditDetails(departmentEntity.getAuditDetails());

        departmentEntityAncestry.setChildren(new ArrayList<>());

        return departmentEntityAncestry;
    }

}
