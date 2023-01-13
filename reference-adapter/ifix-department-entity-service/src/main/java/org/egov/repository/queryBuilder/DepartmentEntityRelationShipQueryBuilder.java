package org.egov.repository.queryBuilder;

import lombok.extern.slf4j.Slf4j;
import org.egov.repository.criteriaBuilder.DepartmentQueryCriteria;
import org.egov.web.models.persist.DepartmentEntityRelationship;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;

import static org.egov.util.DepartmentEntityConstant.DepartmentEntityRelationshipConst.*;


@Component
@Slf4j
public class DepartmentEntityRelationShipQueryBuilder {

    public String findByParentId(String id) {
        if (!StringUtils.isEmpty(id)) {
            return DepartmentQueryCriteria.builder(DepartmentEntityRelationship.class)
                    .where(PARENT_ID).is(id)
                    .and(STATUS).is(Boolean.TRUE)
                    .build();
        }
        return null;
    }

    public String findByChildren(String childId) {
        if (!StringUtils.isEmpty(childId)) {
            return DepartmentQueryCriteria.builder(DepartmentEntityRelationship.class)
                    .where(CHILD_ID).is(childId)
                    .and(STATUS).is(Boolean.TRUE)
                    .build();
        }
        return null;
    }

    /**
     * @param parentIdList
     * @return
     */
    public String findByParentIdList(List<String> parentIdList) {
        if (parentIdList != null && !parentIdList.isEmpty()) {
            return DepartmentQueryCriteria.builder(DepartmentEntityRelationship.class)
                    .where(PARENT_ID).in(parentIdList)
                    .and(STATUS).is(Boolean.TRUE)
                    .build();
        }
        return null;
    }

}
