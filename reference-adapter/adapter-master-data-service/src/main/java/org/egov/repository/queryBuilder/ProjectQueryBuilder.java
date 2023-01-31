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

    public static final String ID = "project.id";
    public static final String TENANT_ID = "project.tenant_id";
    public static final String CODE = "project.code";
    public static final String NAME = "project.name";
    public static final String EXPENDITURE_ID = "project.expenditure_id";
    public static final String DEPARTMENT_ENTITY_ID = "project_department_entity_relationship.department_entity_id";

    /**
     * @param projectSearchCriteria
     * @return
     */
    public String buildQuerySearch(ProjectSearchCriteria projectSearchCriteria) {
        QueryCriteria queryCriteria = QueryCriteria.builder(Project.class);

        queryCriteria.and(TENANT_ID).is(projectSearchCriteria.getTenantId());

        if (!StringUtils.isEmpty(projectSearchCriteria.getCode())) {
            queryCriteria.and(CODE).is(projectSearchCriteria.getCode());
        }

        if (!StringUtils.isEmpty(projectSearchCriteria.getName())) {
            queryCriteria.and(NAME).is(projectSearchCriteria.getName());
        }

        if (!StringUtils.isEmpty(projectSearchCriteria.getExpenditureId())) {
            queryCriteria.and(EXPENDITURE_ID).is(projectSearchCriteria.getExpenditureId());
        }

        if (projectSearchCriteria.getIds() != null && !projectSearchCriteria.getIds().isEmpty()) {
            queryCriteria.and(ID).in(projectSearchCriteria.getIds());
        }

        if(!StringUtils.isEmpty(projectSearchCriteria.getDepartmentEntityId())) {
            queryCriteria.and(DEPARTMENT_ENTITY_ID).is(projectSearchCriteria.getDepartmentEntityId());
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
