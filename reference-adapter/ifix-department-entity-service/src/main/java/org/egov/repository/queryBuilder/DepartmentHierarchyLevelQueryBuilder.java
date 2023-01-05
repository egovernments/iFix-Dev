package org.egov.repository.queryBuilder;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.egov.web.models.DepartmentHierarchyLevelSearchCriteria;
import org.egov.web.models.PlainSearchCriteria;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    public Query buildParentDeptHierarchyLevelSearchQuery(String departmentId, String tenantId, String parent) {
        Criteria criteria = Criteria.where("tenantId").is(tenantId);

        if (StringUtils.isNotBlank(departmentId)) {
            criteria.and("departmentId").is(departmentId);
        }
        criteria.and("parent").is(parent);

        return new Query(criteria);
    }

    public Query buildChunkSearchQuery(PlainSearchCriteria searchCriteria) {
        Criteria criteria = Criteria.where("tenantId").is(searchCriteria.getTenantId());
        Query finalQuery = new Query(criteria).with(Sort.by(Sort.Direction.ASC, "auditDetails.createdTime"));

        if(!searchCriteria.getIsCountCall()) {
            Pageable pageableRequest = PageRequest.of(searchCriteria.getOffset(), searchCriteria.getLimit());
            return finalQuery.with(pageableRequest);
        }

        return finalQuery;
    }
}
