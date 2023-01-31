package org.egov.repository.rowmapper;


import org.egov.web.models.persist.Department;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class DepartmentRowMapper implements ResultSetExtractor<List<Department>> {
    @Override
    public List<Department> extractData(ResultSet resultSet) throws SQLException, DataAccessException {
        List<Department> departmentList = new ArrayList<>();

        while (resultSet.next()) {
            departmentList.add(
                    Department.builder()
                            .id(resultSet.getString("id"))
                            .tenantId(resultSet.getString("tenant_id"))
                            .code(resultSet.getString("code"))
                            .name(resultSet.getString("name"))
                            .isNodal(resultSet.getBoolean("is_nodal"))
                            .parent(resultSet.getString("parent"))
                            .createdBy(resultSet.getString("created_by"))
                            .createdTime(resultSet.getLong("created_time"))
                            .lastModifiedBy(resultSet.getString("last_modified_by"))
                            .lastModifiedTime(resultSet.getLong("last_modified_time"))
                            .build()
            );
        }

        return departmentList;
    }
}
