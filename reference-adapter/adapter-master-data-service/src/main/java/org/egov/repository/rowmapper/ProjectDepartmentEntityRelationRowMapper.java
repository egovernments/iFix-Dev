package org.egov.repository.rowmapper;


import org.egov.web.models.persist.ProjectDepartmentEntityRelationship;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class ProjectDepartmentEntityRelationRowMapper implements ResultSetExtractor<List<ProjectDepartmentEntityRelationship>> {
    @Override
    public List<ProjectDepartmentEntityRelationship> extractData(ResultSet resultSet) throws SQLException, DataAccessException {
        List<ProjectDepartmentEntityRelationship> departmentEntityRelationshipList = new ArrayList<>();

        while (resultSet.next()) {
            departmentEntityRelationshipList.add(
                    ProjectDepartmentEntityRelationship.builder()
                            .projectId(resultSet.getString("project_id"))
                            .departmentEntityId(resultSet.getString("department_entity_id"))
                            .status(resultSet.getBoolean("status"))
                            .build()
            );
        }

        return departmentEntityRelationshipList;
    }
}
