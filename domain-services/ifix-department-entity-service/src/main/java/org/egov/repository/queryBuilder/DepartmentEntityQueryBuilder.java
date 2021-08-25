package org.egov.repository.queryBuilder;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.egov.web.models.DepartmentEntitySearchCriteria;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class DepartmentEntityQueryBuilder {

    public Query buildPlainSearchQuery(DepartmentEntitySearchCriteria searchCriteria) {

        Criteria criteria = Criteria.where("tenantId").is(searchCriteria.getTenantId());

        if (StringUtils.isNotBlank(searchCriteria.getDepartmentId()))
            criteria.and("departmentId").is(searchCriteria.getDepartmentId());

        if (StringUtils.isNotBlank(searchCriteria.getCode()))
            criteria.and("code").is(searchCriteria.getCode());

        if (StringUtils.isNotBlank(searchCriteria.getName()))
            criteria.and("name").is(searchCriteria.getName());

        if (StringUtils.isNotBlank(searchCriteria.getHierarchyLevel()))
            criteria.and("hierarchyLevel").is(searchCriteria.getHierarchyLevel());

        if (searchCriteria.getIds() != null && !searchCriteria.getIds().isEmpty())
            criteria.and("id").in(searchCriteria.getIds());

        return new Query(criteria);
    }
}
