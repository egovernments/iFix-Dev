package org.egov.repository.queryBuilder;

import org.egov.repository.criteriaBuilder.QueryCriteria;
import org.egov.web.models.persist.ProjectDepartmentEntityRelationship;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;

import static org.egov.util.MasterDataConstants.ProjectDepartmentEntityRelationshipConst.PROJECT_ID;
import static org.egov.util.MasterDataConstants.ProjectDepartmentEntityRelationshipConst.STATUS;

@Component
public class ProjectDepartmentEntityRelationshipQueryBuilder {


    public String findByProjectId(String projectId) {
        if (!StringUtils.isEmpty(projectId)) {
            return QueryCriteria.builder(ProjectDepartmentEntityRelationship.class)
                    .where(PROJECT_ID).is(projectId)
                    .and(STATUS)
                    .build();
        }
        return null;
    }

    public String findByProjectIdList(List<String> projectIdList) {
        if (!StringUtils.isEmpty(projectIdList)) {
            return QueryCriteria.builder(ProjectDepartmentEntityRelationship.class)
                    .where(PROJECT_ID).in(projectIdList)
                    .and(STATUS)
                    .build();
        }
        return null;
    }

}
