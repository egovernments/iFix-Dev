package org.egov.repository.queryBuilder;

import org.apache.commons.lang3.StringUtils;
import org.egov.web.models.ProjectSearchCriteria;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class ProjectQueryBuilder {

    public Query buildQuerySearch(ProjectSearchCriteria projectSearchCriteria) {
        Criteria criteria = Criteria.where("tenantId").is(projectSearchCriteria.getTenantId());

        if (!StringUtils.isEmpty(projectSearchCriteria.getName())) {
            criteria.and("name").is(projectSearchCriteria.getName());
        }

        if (!StringUtils.isEmpty(projectSearchCriteria.getCode())) {
            criteria.and("code").is(projectSearchCriteria.getCode());
        }

        if (!StringUtils.isEmpty(projectSearchCriteria.getLocationId())) {
            criteria.and("locationIds").in(Collections.singletonList(projectSearchCriteria.getLocationId()));
        }

        if (!StringUtils.isEmpty(projectSearchCriteria.getExpenditureId())) {
            criteria.and("expenditureId").is(projectSearchCriteria.getExpenditureId());
        }

        if (!StringUtils.isEmpty(projectSearchCriteria.getGetDepartmentEntitytId())) {
            criteria.and("departmentEntitytId").is(projectSearchCriteria.getGetDepartmentEntitytId());
        }

        if (projectSearchCriteria.getIds() != null && !projectSearchCriteria.getIds().isEmpty())
            criteria.and("id").in(projectSearchCriteria.getIds());

        return new Query(criteria);
    }
}
