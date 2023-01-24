package org.egov.repository.queryBuilder;

import org.egov.repository.criteriaBuilder.QueryCriteria;
import org.egov.web.models.ProjectSearchCriteria;
import org.egov.web.models.persist.Project;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Collections;

import static org.egov.util.MasterDataConstants.DEPARTMENT_ID;
import static org.egov.util.MasterDataConstants.ProjectConst.*;

@Component
public class ProjectQueryBuilder {

    /**
     * @param projectSearchCriteria
     * @return
     */
    public String buildQuerySearch(ProjectSearchCriteria projectSearchCriteria) {
        QueryCriteria queryCriteria = QueryCriteria.builder(Project.class);


        queryCriteria.where(TENANT_ID).is(projectSearchCriteria.getTenantId());

        if (!org.springframework.util.StringUtils.isEmpty(projectSearchCriteria.getCode())) {
            queryCriteria.and(CODE).is(projectSearchCriteria.getCode());
        }

        if (!org.springframework.util.StringUtils.isEmpty(projectSearchCriteria.getName())) {
            queryCriteria.and(NAME).is(projectSearchCriteria.getName());
        }

        if (!org.springframework.util.StringUtils.isEmpty(projectSearchCriteria.getExpenditureId())) {
            queryCriteria.and(EXPENDITURE_ID).is(projectSearchCriteria.getExpenditureId());
        }

        if (projectSearchCriteria.getIds() != null && !projectSearchCriteria.getIds().isEmpty()) {
            queryCriteria.and(ID).in(projectSearchCriteria.getIds());
        }

        return queryCriteria.build();
    }

    /**
     * @param id
     * @return
     */
    public String findById(String id) {
        if (!StringUtils.isEmpty(id)) {
            return QueryCriteria.builder(Project.class)
                    .where(ID).is(id)
                    .build();
        }
        return null;
    }
}
