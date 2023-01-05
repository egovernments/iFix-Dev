package org.egov.repository;


import lombok.extern.slf4j.Slf4j;
import org.egov.repository.queryBuilder.DepartmentHierarchyLevelQueryBuilder;
import org.egov.web.models.DepartmentEntity;
import org.egov.web.models.DepartmentHierarchyLevel;
import org.egov.web.models.DepartmentHierarchyLevelSearchCriteria;
import org.egov.web.models.PlainSearchCriteria;
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

    @Autowired
    private DepartmentHierarchyLevelQueryBuilder queryBuilder;

    public void save(DepartmentHierarchyLevel departmentHierarchyLevel) {
        mongoTemplate.save(departmentHierarchyLevel);
    }

    public List<DepartmentHierarchyLevel> searchDeptHierarchyLevel(DepartmentHierarchyLevelSearchCriteria searchCriteria) {
        Query searchQuery = queryBuilder.buildSearchQuery(searchCriteria);
        return (mongoTemplate.find(searchQuery, DepartmentHierarchyLevel.class));
    }

    public List<DepartmentHierarchyLevel> searchParentDeptHierarchyLevel(String departmentId, String tenantId, String parent) {
        Query searchQuery = queryBuilder.buildParentDeptHierarchyLevelSearchQuery(departmentId,tenantId,parent);
        return (mongoTemplate.find(searchQuery, DepartmentHierarchyLevel.class));
    }

    /**
     * @PARAM CRITERIA
     * @return
     */
    public long getDepartmentHierarchyLevelCount(PlainSearchCriteria criteria) {
        criteria.setIsCountCall(Boolean.TRUE);
        Query countQuery = queryBuilder.buildChunkSearchQuery(criteria);
        return mongoTemplate.count(countQuery, Integer.class, "departmentHierarchyLevel");
    }

    /**
     * @param criteria
     * @return
     */
    public List<DepartmentHierarchyLevel> plainSearchDepartmentEntity(PlainSearchCriteria criteria) {
        Query searchQuery = queryBuilder.buildChunkSearchQuery(criteria);
        return mongoTemplate.find(searchQuery, DepartmentHierarchyLevel.class);
    }
}
