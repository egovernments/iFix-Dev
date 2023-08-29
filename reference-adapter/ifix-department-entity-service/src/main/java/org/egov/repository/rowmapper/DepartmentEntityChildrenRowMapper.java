package org.egov.repository.rowmapper;

import org.egov.web.models.persist.DepartmentEntityChildren;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class DepartmentEntityChildrenRowMapper implements ResultSetExtractor<List<DepartmentEntityChildren>> {
    @Override
    public List<DepartmentEntityChildren> extractData(ResultSet resultSet) throws SQLException, DataAccessException {
        List<DepartmentEntityChildren> departmentEntityChildrenList = new ArrayList<>();

        while (resultSet.next()) {
            departmentEntityChildrenList.add(
                    DepartmentEntityChildren.builder()
                            .parentId(resultSet.getString("parent_id"))
                            .childId(resultSet.getString("child_id"))
                            .status(resultSet.getBoolean("status"))
                            .build()
            );
        }

        return departmentEntityChildrenList;
    }
}
