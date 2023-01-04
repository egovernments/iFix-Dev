package org.egov.repository;

import org.egov.repository.queryBuilder.DepartmentHierarchyLevelQueryBuilder;
import org.egov.repository.rowmapper.DepartmentHierarchyLevelRowMapper;
import org.egov.web.models.DepartmentHierarchyLevelSearchCriteria;
import org.egov.web.models.persist.DepartmentHierarchyLevel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class DepartmentHierarchyLevelRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private DepartmentHierarchyLevelRowMapper departmentHierarchyLevelRowMapper;

    @Autowired
    private DepartmentHierarchyLevelQueryBuilder hierarchyLevelQueryBuilder;

    public List<DepartmentHierarchyLevel> findByParamExistence(DepartmentHierarchyLevelSearchCriteria searchCriteria) {
        return jdbcTemplate.query(
                hierarchyLevelQueryBuilder.getQueryByParamExistence(searchCriteria), departmentHierarchyLevelRowMapper
        );
    }

    /**
     * @param departmentId
     * @param tenantId
     * @param parent
     * @return
     */
    public List<DepartmentHierarchyLevel> findByDepartmentIdAndTenantIdAndParent(String departmentId, String tenantId,
                                                                                 String parent) {
        return jdbcTemplate.query(
                hierarchyLevelQueryBuilder.findByDepartmentIdAndTenantIdAndParent(departmentId, tenantId, parent),
                departmentHierarchyLevelRowMapper
        );
    }

    /**
     * @param departmentId
     * @param tenantId
     * @param level
     * @return
     */
    public List<DepartmentHierarchyLevel> findByDepartmentIdAndTenantIdAndLevel(String departmentId, String tenantId,
                                                                                Integer level) {
        return jdbcTemplate.query(
                hierarchyLevelQueryBuilder.findByDepartmentIdAndTenantIdAndLevel(departmentId, tenantId, level),
                departmentHierarchyLevelRowMapper
        );
    }

    /**
     * @param id
     * @param tenantId
     * @return
     */
    public Optional<DepartmentHierarchyLevel> findByIdAndTenantId(String id, String tenantId) {
        List<DepartmentHierarchyLevel> departmentHierarchyLevelList = jdbcTemplate
                .query(hierarchyLevelQueryBuilder.findByIdAndTenantId(id, tenantId), departmentHierarchyLevelRowMapper);

        if (departmentHierarchyLevelList != null && departmentHierarchyLevelList.size() == 1) {
            return Optional.ofNullable(departmentHierarchyLevelList.get(0));
        }

        return Optional.empty();
    }
}
