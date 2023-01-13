package org.egov.repository.rowmapper;

import org.egov.web.models.persist.DepartmentEntityRelationship;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class DepartmentEntityRelationshipRowMapper implements ResultSetExtractor<List<DepartmentEntityRelationship>> {
    @Override
    public List<DepartmentEntityRelationship> extractData(ResultSet resultSet) throws SQLException, DataAccessException {
        List<DepartmentEntityRelationship> departmentEntityRelationshipList = new ArrayList<>();

        while (resultSet.next()) {
            departmentEntityRelationshipList.add(
                    DepartmentEntityRelationship.builder()
                            .parentId(resultSet.getString("parent_id"))
                            .childId(resultSet.getString("child_id"))
                            .status(resultSet.getBoolean("status"))
                            .build()
            );
        }

        return departmentEntityRelationshipList;
    }
}
