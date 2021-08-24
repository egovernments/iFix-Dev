package org.egov.repository.queryBuilder;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.egov.web.models.DepartmentHierarchyLevelSearchCriteria;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class DepartmentHierarchyLevelQueryBuilder {

    public Query buildSearchQuery(DepartmentHierarchyLevelSearchCriteria searchCriteria) {

        Criteria criteria = Criteria.where("tenantId").is(searchCriteria.getTenantId());

        if (StringUtils.isNotBlank(searchCriteria.getDepartmentId()))
            criteria.and("departmentId").is(searchCriteria.getDepartmentId());

        if (StringUtils.isNotBlank(searchCriteria.getLabel()))
            criteria.and("label").is(searchCriteria.getLabel());

        if (searchCriteria.getLevel() != null)
            criteria.and("level").is(searchCriteria.getLevel());

        if (searchCriteria.getIds() != null && !searchCriteria.getIds().isEmpty())
            criteria.and("id").in(searchCriteria.getIds());

        return new Query(criteria);
    }
}
