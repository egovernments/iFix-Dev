package org.egov.repository;

import org.egov.repository.queryBuilder.DepartmentEntityChildrenQueryBuilder;
import org.egov.repository.rowmapper.DepartmentEntityChildrenRowMapper;
import org.egov.tracer.model.CustomException;
import org.egov.web.models.persist.DepartmentEntityChildren;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Optional;

@Repository
public class DepartmentEntityChildrenRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private DepartmentEntityChildrenRowMapper entityChildrenRowMapper;

    @Autowired
    private DepartmentEntityChildrenQueryBuilder entityChildrenQueryBuilder;

    /**
     * @param parentId
     * @return
     */
    public List<DepartmentEntityChildren> findByParentId(String parentId) {

        return jdbcTemplate.query(
                entityChildrenQueryBuilder.findByParentId(parentId), entityChildrenRowMapper);
    }

    public List<DepartmentEntityChildren> findByParentIdList(List<String> parentIdList) {

        return jdbcTemplate.query(
                entityChildrenQueryBuilder.findByParentIdList(parentIdList), entityChildrenRowMapper);
    }

    /**
     * @param childId
     * @return
     */
    public Optional<DepartmentEntityChildren> getParent(String childId) {
        List<DepartmentEntityChildren> departmentEntityChildrenList = jdbcTemplate
                .query(entityChildrenQueryBuilder.findByChildren(childId), entityChildrenRowMapper);

        if (!CollectionUtils.isEmpty(departmentEntityChildrenList)) {
            if (departmentEntityChildrenList.size() == 1) {
                return Optional.ofNullable(departmentEntityChildrenList.get(0));
            }else {
                throw new CustomException("DE_PARENT_SEARCH_ERROR", "It returns multiple record");
            }
        }

        return Optional.empty();
    }

}
