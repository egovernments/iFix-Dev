package org.egov.repository;

import org.egov.repository.queryBuilder.ProjectQueryBuilder;
import org.egov.web.models.Project;
import org.egov.web.models.ProjectSearchCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ProjectRepository {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    ProjectQueryBuilder projectQueryBuilder;

    /**
     * @param projectSearchCriteria
     * @return
     */
    public List<Project> findAllByCriteria(ProjectSearchCriteria projectSearchCriteria) {
        return mongoTemplate.find(projectQueryBuilder.buildQuerySearch(projectSearchCriteria), Project.class);
    }

    /**
     * @param project
     */
    public void save(Project project) {
        mongoTemplate.save(project);
    }

    public Optional<Project> findByProjectId(String id) {
        return Optional.ofNullable(mongoTemplate.findById(id, Project.class));
    }
}
