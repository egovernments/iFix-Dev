package org.egov.repository;

import org.egov.repository.queryBuilder.ProjectQueryBuilder;
import org.egov.repository.rowmapper.ProjectRowMapper;
import org.egov.web.models.ProjectSearchCriteria;
import org.egov.web.models.persist.Project;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Optional;

@Repository
public class ProjectRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ProjectQueryBuilder projectQueryBuilder;

    @Autowired
    private ProjectRowMapper projectRowMapper;

    /**
     * @param projectSearchCriteria
     * @return
     */
    public List<Project> findAllByCriteria(ProjectSearchCriteria projectSearchCriteria) {
        return jdbcTemplate.query(
                projectQueryBuilder
                        .buildQuerySearch(projectSearchCriteria), projectRowMapper);
    }

    /**
     * @param id
     * @return
     */
    public Optional<Project> findByProjectId(String id) {
        List<Project> projectList = jdbcTemplate.query(projectQueryBuilder.findById(id), projectRowMapper);

        if (!CollectionUtils.isEmpty(projectList) && projectList.size() == 1) {
            return Optional.ofNullable(projectList.get(0));
        }

        return Optional.empty();
    }
}
