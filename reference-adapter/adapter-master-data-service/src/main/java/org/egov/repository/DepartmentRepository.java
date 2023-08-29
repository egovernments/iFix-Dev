package org.egov.repository;


import lombok.extern.slf4j.Slf4j;
import org.egov.repository.queryBuilder.DepartmentQueryBuilder;
import org.egov.repository.rowmapper.DepartmentRowMapper;
import org.egov.web.models.DepartmentSearchCriteria;
import org.egov.web.models.persist.Department;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Slf4j
public class DepartmentRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private DepartmentRowMapper departmentRowMapper;

    @Autowired
    private DepartmentQueryBuilder departmentQueryBuilder;

    public List<Department> search(DepartmentSearchCriteria searchCriteria) {
        return jdbcTemplate.query(
                departmentQueryBuilder
                        .buildSearchQuery(searchCriteria), departmentRowMapper);


    }
}
