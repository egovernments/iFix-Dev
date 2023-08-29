package org.egov.repository.criteriaBuilder;

import org.egov.web.models.DepartmentDTO;
import org.egov.web.models.persist.Department;
import org.egov.web.models.persist.Expenditure;
import org.egov.web.models.persist.Project;
import org.egov.web.models.persist.ProjectDepartmentEntityRelationship;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;


public class QueryCriteria {
    private static String PROJECT_SELECT_ALL = "SELECT * FROM project, project_department_entity_relationship" +
            " where project.id = project_department_entity_relationship.project_id";
    private static String EXPENDITURE_SELECT_ALL = "SELECT * FROM expenditure";
    private static String DEPARTMENT_SELECT_ALL = "SELECT * FROM department";
    private static String PROJECT_DEPARTMENT_ENTITY_SELECT_ALL = "SELECT * FROM project_department_entity_relationship";

    private StringBuilder criteriaQuery = new StringBuilder();

    private QueryCriteria() {
    }

    public QueryCriteria(StringBuilder criteriaQuery) {
        this.criteriaQuery = criteriaQuery;
    }

    public static QueryCriteria builder(Class<? extends Object> entityClass) {

        if (Department.class.getTypeName().equals(entityClass.getTypeName())) {
            return new QueryCriteria(new StringBuilder(DEPARTMENT_SELECT_ALL));
        }

        if (Expenditure.class.getTypeName().equals(entityClass.getTypeName())) {
            return new QueryCriteria(new StringBuilder(EXPENDITURE_SELECT_ALL));
        }

        if (Project.class.getTypeName().equals(entityClass.getTypeName())) {
            return new QueryCriteria(new StringBuilder(PROJECT_SELECT_ALL));
        }

        if (ProjectDepartmentEntityRelationship.class.getTypeName().equals(entityClass.getTypeName())) {
            return new QueryCriteria(new StringBuilder(PROJECT_DEPARTMENT_ENTITY_SELECT_ALL));
        }

        return null;
    }

    /**
     * @param attribute
     * @return
     */
    public QueryCriteria where(String attribute) {
        if (!StringUtils.isEmpty(attribute)) {
            this.criteriaQuery.append(" WHERE " + attribute);
            return this;
        }

        return null;
    }

    public QueryCriteria is(String attributeValue) {
        if (!StringUtils.isEmpty(attributeValue)) {
            this.criteriaQuery.append(" = '" + attributeValue + "'");
            return this;
        }
        return null;
    }

    public QueryCriteria is(Integer attributeValue) {
        if (attributeValue != null) {
            this.criteriaQuery.append(" = " + attributeValue);
            return this;
        }
        return null;
    }

    public QueryCriteria and(String attribute) {
        if (!StringUtils.isEmpty(attribute)) {
            this.criteriaQuery.append(" AND " + attribute);
            return this;
        }
        return null;
    }

    public QueryCriteria in(List<String> attributeList) {
        if (attributeList != null && !attributeList.isEmpty()) {
            String inValues = attributeList.stream().collect(Collectors.joining("','", "'", "'"));

            this.criteriaQuery.append(" IN (" + inValues + ")");

            return this;
        }
        return null;
    }

    public QueryCriteria like(String attributeValue) {
        if (!StringUtils.isEmpty(attributeValue)) {
            this.criteriaQuery.append(" like '%" + attributeValue + "%'");

            return this;
        }

        return null;
    }

    public String build() {
        return criteriaQuery.append(";").toString();
    }

}
