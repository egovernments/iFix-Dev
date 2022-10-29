package org.egov.repository;

import org.apache.commons.lang3.StringUtils;
import org.egov.repository.queryBuilder.DepartmentEntityQueryBuilder;
import org.egov.web.models.DepartmentEntity;
import org.egov.web.models.DepartmentEntitySearchRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Repository
public class DepartmentEntityRepository {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private DepartmentEntityQueryBuilder queryBuilder;

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
        if (!results.isEmpty())
            return results.get(0);
        return null;
    }

    public List<DepartmentEntity> searchEntity(DepartmentEntitySearchRequest departmentEntitySearchRequest) {
        if (StringUtils.isNotBlank(departmentEntitySearchRequest.getCriteria().getTenantId())) {
            Query searchQuery = queryBuilder.buildPlainSearchQuery(departmentEntitySearchRequest.getCriteria());
            return (mongoTemplate.find(searchQuery, DepartmentEntity.class));
        }
        return Collections.emptyList();
    }

    /**
     * @param childIdList
     * @param hierarchyLevel
     * @return
     */
    public List<DepartmentEntity> searchChildDepartment(List<String> childIdList, Integer hierarchyLevel) {
        Optional<Query> optionalQuery = queryBuilder.buildChildrenValidationQuery(childIdList, hierarchyLevel);

        if (optionalQuery.isPresent()) {
            return mongoTemplate.find(optionalQuery.get(), DepartmentEntity.class);
        }

        return Collections.emptyList();
    }
}
