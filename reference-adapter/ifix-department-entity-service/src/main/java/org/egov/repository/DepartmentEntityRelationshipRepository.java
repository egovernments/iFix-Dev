package org.egov.repository;

import org.egov.repository.queryBuilder.DepartmentEntityQueryBuilder;
import org.egov.repository.queryBuilder.DepartmentEntityRelationShipQueryBuilder;
import org.egov.repository.rowmapper.DepartmentEntityRelationshipRowMapper;
import org.egov.repository.rowmapper.DepartmentEntityRowMapper;
import org.egov.tracer.model.CustomException;
import org.egov.web.models.DepartmentEntitySearchCriteria;
import org.egov.web.models.persist.DepartmentEntity;
import org.egov.web.models.persist.DepartmentEntityRelationship;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Optional;

@Repository
public class DepartmentEntityRelationshipRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private DepartmentEntityRelationshipRowMapper entityRelationshipRowMapper;

    @Autowired
    private DepartmentEntityRelationShipQueryBuilder entityRelationShipQueryBuilder;

    /**
     * @param parentId
     * @return
     */
    public List<DepartmentEntityRelationship> findByParentId(String parentId) {

        return jdbcTemplate.query(
                entityRelationShipQueryBuilder.findByParentId(parentId), entityRelationshipRowMapper);
    }

    /**
     * @param childId
     * @return
     */
    public Optional<DepartmentEntityRelationship> getParent(String childId) {
        List<DepartmentEntityRelationship> departmentEntityRelationshipList = jdbcTemplate
                .query(entityRelationShipQueryBuilder.findByChildren(childId), entityRelationshipRowMapper);

        if (!CollectionUtils.isEmpty(departmentEntityRelationshipList)) {
            if (departmentEntityRelationshipList.size() == 1) {
                return Optional.ofNullable(departmentEntityRelationshipList.get(0));
            }else {
                throw new CustomException("DE_PARENT_SEARCH_ERROR", "It returns multiple record");
            }
        }

        return Optional.empty();
    }

}
