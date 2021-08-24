package org.egov.repository;


import lombok.extern.slf4j.Slf4j;
import org.egov.repository.queryBuilder.DepartmentHierarchyLevelQueryBuilder;
import org.egov.web.models.DepartmentHierarchyLevel;
import org.egov.web.models.DepartmentHierarchyLevelSearchCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Slf4j
public class DepartmentHierarchyLevelRepository {

    @Autowired
    private MongoTemplate mongoTemplate;

    public void save(DepartmentHierarchyLevel departmentHierarchyLevel) {
        mongoTemplate.save(departmentHierarchyLevel);
    }

}
