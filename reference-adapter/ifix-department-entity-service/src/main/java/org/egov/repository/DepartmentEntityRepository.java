package org.egov.repository;

import org.egov.repository.queryBuilder.DepartmentEntityQueryBuilder;
import org.egov.repository.rowmapper.DepartmentEntityRowMapper;
import org.egov.web.models.DepartmentEntitySearchCriteria;
import org.egov.web.models.persist.DepartmentEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Optional;

@Repository
public class DepartmentEntityRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private DepartmentEntityRowMapper departmentEntityRowMapper;

    @Autowired
    private DepartmentEntityQueryBuilder departmentEntityQueryBuilder;

    public List<DepartmentEntity> getDepartmentEntitiesByParamsExistence(DepartmentEntitySearchCriteria departmentEntitySearchCriteria) {
        return jdbcTemplate.query(
                departmentEntityQueryBuilder
                        .getQueryByParamExistence(departmentEntitySearchCriteria), departmentEntityRowMapper
        );
    }

    public List<DepartmentEntity> findByIdsAndHierarchyLevel(List<String> ids, Integer hierarchyLevel) {
        return jdbcTemplate.query(
                departmentEntityQueryBuilder
                        .getQueryByIdsAndHierarchy(ids, hierarchyLevel), departmentEntityRowMapper
        );
    }

    /**
     * @param id
     * @return It should be returning only one department entity against provided id.
     */
    public Optional<DepartmentEntity> findById(String id) {
        List<DepartmentEntity> departmentEntityList = jdbcTemplate.query(departmentEntityQueryBuilder.findById(id),
                departmentEntityRowMapper);

        if (!CollectionUtils.isEmpty(departmentEntityList) && departmentEntityList.size() == 1) {
            return Optional.ofNullable(departmentEntityList.get(0));
        }

        return Optional.empty();
    }

    /**
     * @param childId
     * @return It should be returning only one department entity against provided id.
     */
    public Optional<DepartmentEntity> getParent(String childId) {
        List<DepartmentEntity> departmentEntityList = jdbcTemplate
                .query(departmentEntityQueryBuilder.findByChildren(childId), departmentEntityRowMapper);

        if (!CollectionUtils.isEmpty(departmentEntityList) && departmentEntityList.size() == 1) {
            return Optional.ofNullable(departmentEntityList.get(0));
        }

        return Optional.empty();
    }
}
