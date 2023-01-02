package org.egov.util;

import lombok.extern.slf4j.Slf4j;
import org.egov.web.models.DepartmentEntityDTO;
import org.egov.web.models.DepartmentEntityAncestry;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
@Slf4j
public class DepartmentEntityAncestryUtil {

    public DepartmentEntityAncestry createDepartmentEntityAncestryFromDepartmentEntity(DepartmentEntityDTO departmentEntityDTO) {
        DepartmentEntityAncestry departmentEntityAncestry = DepartmentEntityAncestry.builder().build();
        departmentEntityAncestry.setId(departmentEntityDTO.getId());
        departmentEntityAncestry.setTenantId(departmentEntityDTO.getTenantId());
        departmentEntityAncestry.setDepartmentId(departmentEntityDTO.getDepartmentId());
        departmentEntityAncestry.setCode(departmentEntityDTO.getCode());
        departmentEntityAncestry.setName(departmentEntityDTO.getName());
        departmentEntityAncestry.setHierarchyLevel(departmentEntityDTO.getHierarchyLevel());
        departmentEntityAncestry.setAuditDetails(departmentEntityDTO.getAuditDetails());

        departmentEntityAncestry.setChildren(new ArrayList<>());

        return departmentEntityAncestry;
    }

}
