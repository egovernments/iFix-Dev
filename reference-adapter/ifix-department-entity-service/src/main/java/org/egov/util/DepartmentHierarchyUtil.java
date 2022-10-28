package org.egov.util;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.egov.repository.DepartmentHierarchyLevelRepository;
import org.egov.web.models.DepartmentHierarchyLevel;
import org.egov.web.models.DepartmentHierarchyLevelSearchCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
@Slf4j
public class DepartmentHierarchyUtil {

    @Autowired
    private DepartmentHierarchyLevelRepository levelRepository;


    public List<DepartmentHierarchyLevel> validateHierarchyLevelMetaData(String departmentId, Integer hierarchyLevel, String tenantId) {
        if (StringUtils.isNotBlank(departmentId) && StringUtils.isNotBlank(tenantId) && hierarchyLevel != null) {
            DepartmentHierarchyLevelSearchCriteria searchCriteria = new DepartmentHierarchyLevelSearchCriteria();
            searchCriteria.setTenantId(tenantId);
            searchCriteria.setDepartmentId(departmentId);
            searchCriteria.setLevel(hierarchyLevel);
            return (levelRepository.searchDeptHierarchyLevel(searchCriteria));
        }
        return Collections.emptyList();
    }
}
