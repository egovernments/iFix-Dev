package org.egov.repository;

import org.egov.web.models.DepartmentEntity;
import org.egov.web.models.DepartmentEntitySearchRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;

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

    public DepartmentEntity getParent(String childId) {
        Criteria criteria = Criteria.where("children").all(Collections.singletonList(childId));
        Query query = Query.query(criteria);
        List<DepartmentEntity> results = mongoTemplate.find(query, DepartmentEntity.class, "departmentEntity");
        if(!results.isEmpty())
            return results.get(0);
        return null;
    }

    public List<DepartmentEntity> searchEntity(DepartmentEntitySearchRequest departmentEntitySearchRequest) {

        DepartmentEntity departmentEntity = DepartmentEntity.builder()
                .build();

        departmentEntity.setId("525f7b94-1755-426e-982f-430904554dd3");
        departmentEntity.setChildren(Collections.singletonList("ads"));

        return Collections.singletonList(departmentEntity);
    }
}
