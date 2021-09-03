package org.egov.repository;


import lombok.extern.slf4j.Slf4j;
import org.egov.repository.queryBuilder.DepartmentQueryBuilder;
import org.egov.web.models.ChartOfAccount;
import org.egov.web.models.Department;
import org.egov.web.models.DepartmentSearchCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Slf4j
public class DepartmentRepository {


    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private DepartmentQueryBuilder departmentQueryBuilder;

    public List<Department> search(DepartmentSearchCriteria searchCriteria) {
        Query searchQuery = departmentQueryBuilder.buildSearchQuery(searchCriteria);
        return (mongoTemplate.find(searchQuery, Department.class));
    }

    /**
     * @param department
     */
    public void save(Department department) {
        mongoTemplate.save(department);
    }
}
