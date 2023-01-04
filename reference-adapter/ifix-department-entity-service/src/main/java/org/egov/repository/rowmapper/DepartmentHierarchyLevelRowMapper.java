package org.egov.repository.rowmapper;

import org.egov.web.models.persist.DepartmentHierarchyLevel;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class DepartmentHierarchyLevelRowMapper implements ResultSetExtractor<List<DepartmentHierarchyLevel>> {

    @Override
    public List<DepartmentHierarchyLevel> extractData(ResultSet resultSet) throws SQLException, DataAccessException {
        List<DepartmentHierarchyLevel> departmentHierarchyLevelList = new ArrayList<>();

        while (resultSet.next()) {
            departmentHierarchyLevelList.add(
                    DepartmentHierarchyLevel.builder()
                            .id(resultSet.getString("id"))
                            .createdBy(resultSet.getString("created_by"))
                            .createdTime(resultSet.getLong("created_time"))
                            .departmentId(resultSet.getString("department_id"))
                            .label(resultSet.getString("label"))
                            .lastModifiedBy(resultSet.getString("last_modified_by"))
                            .lastModifiedTime(resultSet.getLong("last_modified_time"))
                            .level(resultSet.getInt("level"))
                            .parent(resultSet.getString("parent"))
                            .tenantId(resultSet.getString("tenant_id"))
                            .build()
            );
        }
        return departmentHierarchyLevelList;
    }
}
