package org.egov.repository.rowmapper;


import org.egov.web.models.persist.Expenditure;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class ExpenditureRowMapper implements ResultSetExtractor<List<Expenditure>> {
    @Override
    public List<Expenditure> extractData(ResultSet resultSet) throws SQLException, DataAccessException {
        List<Expenditure> expenditureList = new ArrayList<>();

        while (resultSet.next()) {
            expenditureList.add(
                    Expenditure.builder()
                            .id(resultSet.getString("id"))
                            .tenantId(resultSet.getString("tenant_id"))
                            .code(resultSet.getString("code"))
                            .name(resultSet.getString("name"))
                            .type(resultSet.getString("type"))
                            .departmentId(resultSet.getString("department_id"))
                            .createdBy(resultSet.getString("created_by"))
                            .createdTime(resultSet.getLong("created_time"))
                            .lastModifiedBy(resultSet.getString("last_modified_by"))
                            .lastModifiedTime(resultSet.getLong("last_modified_time"))
                            .build()
            );
        }

        return expenditureList;
    }
}
