package org.egov.repository;

import org.egov.web.models.DepartmentEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class DepartmentEntityRepository {

    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * @param departmentEntity
     */
    public void save(DepartmentEntity departmentEntity) {
        mongoTemplate.save(departmentEntity);
    }
}
