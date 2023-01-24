package org.egov.repository.queryBuilder;


import lombok.extern.slf4j.Slf4j;
import org.egov.repository.criteriaBuilder.QueryCriteria;
import org.egov.web.models.DepartmentSearchCriteria;
import org.egov.web.models.persist.Department;
import org.springframework.stereotype.Component;

import static org.egov.util.MasterDataConstants.DepartmentConst.*;

@Component
@Slf4j
public class DepartmentQueryBuilder {

    /**
     * @param searchCriteria
     * @return
     */
    public String buildSearchQuery(DepartmentSearchCriteria searchCriteria) {
        QueryCriteria queryCriteria = QueryCriteria.builder(Department.class);

        queryCriteria.where(TENANT_ID).is(searchCriteria.getTenantId());

        if (!org.springframework.util.StringUtils.isEmpty(searchCriteria.getCode())) {
            queryCriteria.and(CODE).is(searchCriteria.getCode());
        }

        if (!org.springframework.util.StringUtils.isEmpty(searchCriteria.getName())) {
            queryCriteria.and(NAME).is(searchCriteria.getName());
        }

        if (searchCriteria.getIds() != null && !searchCriteria.getIds().isEmpty()) {
            queryCriteria.and(ID).in(searchCriteria.getIds());
        }

        return queryCriteria.build();
    }
}
