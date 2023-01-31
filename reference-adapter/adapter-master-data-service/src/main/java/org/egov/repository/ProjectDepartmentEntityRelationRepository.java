package org.egov.repository;

import org.egov.repository.queryBuilder.ProjectDepartmentEntityRelationshipQueryBuilder;
import org.egov.repository.rowmapper.ProjectDepartmentEntityRelationRowMapper;
import org.egov.web.models.persist.ProjectDepartmentEntityRelationship;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ProjectDepartmentEntityRelationRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ProjectDepartmentEntityRelationshipQueryBuilder projectDepartmentEntityRelationshipQueryBuilder;

    @Autowired
    private ProjectDepartmentEntityRelationRowMapper departmentEntityRelationRowMapper;


    /**
     * @param projectId
     * @return
     */
    public List<ProjectDepartmentEntityRelationship> findByProjectId(String projectId) {
        return jdbcTemplate.query(projectDepartmentEntityRelationshipQueryBuilder.findByProjectId(projectId),
                departmentEntityRelationRowMapper);

    }

    public List<ProjectDepartmentEntityRelationship> findByProjectIdList(List<String> projectIdList) {
        return jdbcTemplate.query(projectDepartmentEntityRelationshipQueryBuilder.findByProjectIdList(projectIdList),
                departmentEntityRelationRowMapper);
    }
}
