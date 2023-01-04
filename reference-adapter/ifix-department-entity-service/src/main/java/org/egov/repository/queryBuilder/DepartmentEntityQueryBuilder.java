package org.egov.repository.queryBuilder;

import lombok.extern.slf4j.Slf4j;
import org.egov.repository.criteriaBuilder.DepartmentQueryCriteria;
import org.egov.web.models.DepartmentEntitySearchCriteria;
import org.egov.web.models.persist.DepartmentEntity;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;

import static org.egov.util.DepartmentEntityConstant.DepartmentEntity.*;

@Component
@Slf4j
public class DepartmentEntityQueryBuilder {

    public String getQueryByParamExistence(@NonNull DepartmentEntitySearchCriteria departmentEntitySearchCriteria) {
        DepartmentQueryCriteria departmentQueryCriteria = DepartmentQueryCriteria.builder(DepartmentEntity.class);

        departmentQueryCriteria.where(TENANT_ID).is(departmentEntitySearchCriteria.getTenantId());

        if (!StringUtils.isEmpty(departmentEntitySearchCriteria.getDepartmentId())) {
            departmentQueryCriteria.and(DEPARTMENT_ID).is(departmentEntitySearchCriteria.getDepartmentId());
        }

        if (!StringUtils.isEmpty(departmentEntitySearchCriteria.getCode())) {
            departmentQueryCriteria.and(CODE).is(departmentEntitySearchCriteria.getCode());
        }

        if (!StringUtils.isEmpty(departmentEntitySearchCriteria.getName())) {
            departmentQueryCriteria.and(NAME).is(departmentEntitySearchCriteria.getName());
        }

        if (!StringUtils.isEmpty(departmentEntitySearchCriteria.getHierarchyLevel())) {
            departmentQueryCriteria.and(HIERARCHY_LEVEL).is(departmentEntitySearchCriteria.getHierarchyLevel());
        }

        if (departmentEntitySearchCriteria.getIds() != null && !departmentEntitySearchCriteria.getIds().isEmpty()) {
            departmentQueryCriteria.and(ID).in(departmentEntitySearchCriteria.getIds());
        }

        return departmentQueryCriteria.build();
    }

    public String getQueryByIdsAndHierarchy(List<String> ids, Integer hierarchyLevel) {
        if (ids != null && !ids.isEmpty() && hierarchyLevel != null) {
            return DepartmentQueryCriteria.builder(DepartmentEntity.class)
                    .where(ID).in(ids)
                    .and(HIERARCHY_LEVEL).is(hierarchyLevel)
                    .build();
        }
        return null;
    }

    public String findById(String id) {
        if (!StringUtils.isEmpty(id)) {
            return DepartmentQueryCriteria.builder(DepartmentEntity.class)
                    .where(ID).is(id)
                    .build();
        }
        return null;
    }

    public String findByChildren(String childId) {
        if (!StringUtils.isEmpty(childId)) {
            return DepartmentQueryCriteria.builder(DepartmentEntity.class)
                    .where(CHILDREN).like(childId)
                    .build();
        }
        return null;
    }
}