package org.egov.repository.queryBuilder;


import lombok.extern.slf4j.Slf4j;
import org.egov.repository.criteriaBuilder.DepartmentQueryCriteria;
import org.egov.web.models.DepartmentHierarchyLevelSearchCriteria;
import org.egov.web.models.persist.DepartmentHierarchyLevel;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import static org.egov.util.DepartmentEntityConstant.DepartmentEntity.TENANT_ID;
import static org.egov.util.DepartmentEntityConstant.DepartmentHierarchyLevel.*;

@Component
@Slf4j
public class DepartmentHierarchyLevelQueryBuilder {

    public String getQueryByParamExistence(@NonNull DepartmentHierarchyLevelSearchCriteria searchCriteria) {
        DepartmentQueryCriteria departmentQueryCriteria = DepartmentQueryCriteria.builder(DepartmentHierarchyLevel.class);

        departmentQueryCriteria.where(TENANT_ID).is(searchCriteria.getTenantId());

        if (!StringUtils.isEmpty(searchCriteria.getDepartmentId())) {
            departmentQueryCriteria.and(DEPARTMENT_ID).is(searchCriteria.getDepartmentId());
        }

        if (!StringUtils.isEmpty(searchCriteria.getLabel())) {
            departmentQueryCriteria.and(LABEL).is(searchCriteria.getLabel());
        }

        if (!StringUtils.isEmpty(searchCriteria.getLevel())) {
            departmentQueryCriteria.and(LEVEL).is(searchCriteria.getLevel());
        }

        if (searchCriteria.getIds() != null && !searchCriteria.getIds().isEmpty()) {
            departmentQueryCriteria.and(ID).in(searchCriteria.getIds());
        }

        return departmentQueryCriteria.build();
    }

    public String findByDepartmentIdAndTenantIdAndParent(String departmentId, String tenantId,
                                                                          String parent) {
        if (!StringUtils.isEmpty(departmentId) && !StringUtils.isEmpty(tenantId) && !StringUtils.isEmpty(parent)) {
            return DepartmentQueryCriteria.builder(DepartmentHierarchyLevel.class)
                    .where(DEPARTMENT_ID).is(departmentId)
                    .and(TENANT_ID).is(tenantId)
                    .and(PARENT).is(parent)
                    .build();
        }
        return null;
    }

    /**
     * @param departmentId
     * @param tenantId
     * @param level
     * @return
     */
    public String findByDepartmentIdAndTenantIdAndLevel(String departmentId, String tenantId, Integer level) {
        if (!StringUtils.isEmpty(departmentId) && !StringUtils.isEmpty(tenantId) && level != null) {
            return DepartmentQueryCriteria.builder(DepartmentHierarchyLevel.class)
                    .where(DEPARTMENT_ID).is(departmentId)
                    .and(TENANT_ID).is(tenantId)
                    .and(LEVEL).is(level)
                    .build();
        }
        return null;
    }

    /**
     * @param id
     * @param tenantId
     * @return
     */
    public String findByIdAndTenantId(String id, String tenantId) {
        if (!StringUtils.isEmpty(id) && !StringUtils.isEmpty(tenantId)) {
            return DepartmentQueryCriteria.builder(DepartmentHierarchyLevel.class)
                    .where(ID).is(id)
                    .and(TENANT_ID).is(tenantId)
                    .build();
        }

        return null;
    }
}
