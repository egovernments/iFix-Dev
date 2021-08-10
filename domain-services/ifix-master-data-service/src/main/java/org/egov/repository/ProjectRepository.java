package org.egov.repository;

import org.egov.repository.queryBuilder.ProjectQueryBuilder;
import org.egov.web.models.EAT;
import org.egov.web.models.Project;
import org.egov.web.models.ProjectSearchCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ProjectRepository {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    ProjectQueryBuilder projectQueryBuilder;

    public List<Project> findAllByCriteria(ProjectSearchCriteria projectSearchCriteria) {
        return mongoTemplate.find(projectQueryBuilder.buildQuerySearch(projectSearchCriteria), Project.class);
    }
}
