package org.egov.repository.rowmapper;

import org.egov.web.models.persist.DepartmentEntity;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class DepartmentEntityRowMapper implements ResultSetExtractor<List<DepartmentEntity>> {
    @Override
    public List<DepartmentEntity> extractData(ResultSet resultSet) throws SQLException, DataAccessException {
        List<DepartmentEntity> departmentEntityList = new ArrayList<>();

        while (resultSet.next()) {
            departmentEntityList.add(
                    DepartmentEntity.builder()
                            .id(resultSet.getString("id"))
                            .tenantId(resultSet.getString("tenant_id"))
                            .departmentId(resultSet.getString("department_id"))
                            .code(resultSet.getString("code"))
                            .name(resultSet.getString("name"))
                            .hierarchyLevel(resultSet.getInt("hierarchy_level"))
                            .createdBy(resultSet.getString("created_by"))
                            .createdTime(resultSet.getLong("created_time"))
                            .lastModifiedBy(resultSet.getString("last_modified_by"))
                            .lastModifiedTime(resultSet.getLong("last_modified_time"))
                            .build()
            );
        }

        return departmentEntityList;
    }
}
