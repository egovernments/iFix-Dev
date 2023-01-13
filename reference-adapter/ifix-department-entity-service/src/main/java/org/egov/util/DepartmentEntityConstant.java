package org.egov.util;

public class DepartmentEntityConstant {

    private DepartmentEntityConstant() {
    }

    public static final String USER_INFO = "USER_INFO";
    public static final String TENANT_ID = "TENANT_ID";
    public static final String DEPARTMENT_ENTITY_CODE = "DEPARTMENT_ENTITY_CODE";
    public static final String DEPARTMENT_ENTITY_NAME = "DEPARTMENT_ENTITY_NAME";
    public static final String DEPARTMENT_HIERARCHY_ID = "DEPARTMENT_HIERARCHY_ID";
    public static final String DEPARTMENT_CHILDREN = "DEPARTMENT_CHILDREN";
    public static final String REQUEST_PAYLOAD_MISSING = "REQUEST_PAYLOAD";
    public static final String ERROR_REQUEST_HEADER = "REQUEST_HEADER";
    public static final String INVALID_REQUEST = "INVALID_REQUEST";
    public static final String DEPARTMENT_ID = "DEPARTMENT_ID";
    public static final String DEPARTMENT_LABEL = "DEPARTMENT_LABEL";
    public static final String INVALID_TENANT_ID = "INVALID_TENANT_ID";
    public static final String INVALID_DEPARTMENT_ID = "INVALID_DEPARTMENT_ID";
    public static final String ERROR_SEARCH_CRITERIA = "SEARCH_CRITERIA";
    public static final String INVALID_HIERARCHY_LEVEL = "INVALID_HIERARCHY_LEVEL";

    //Search key parameters
    public static final String IDS = "Ids";
    public static final String ID = "Id";
    public static final String REQUEST_HEADER = "requestHeader";
    public static final String CRITERIA = "criteria";
    public static final String CRITERIA_TENANT_ID = "tenantId";

    //json path
    public static final String GOVERNMENT_LIST = "$.government.*";
    public static final String DEPARTMENT_JSON_PATH = "$.department.*";

    public static final String JSONPATH_ERROR = "JSONPATH_ERROR";

    public static final String WHERE_CLAUSE = " WHERE ";
    public static final String ORDER_BY_CLAUSE = " ORDER BY ";
    public static final String EQUAL_TO = " = ";
    public static final String IN_CLAUSE = " IN ";
    public static final String GREATER_THAN_AND_EQUAL_TO = " >= ";
    public static final String GREATER_THAN = " > ";
    public static final String LESS_THAN_AND_EQUAL_TO = " <= ";
    public static final String LESS_THAN = " < ";
    public static final String AND = " AND ";
    public static final String DESC = " DESC ";
    public static final String SINGLE_QUOTE = "'";

    public class DepartmentEntityConst {
        public static final String ID = "id";
        public static final String CHILDREN = "children";
        public static final String CODE = "code";
        public static final String CREATED_BY = "created_by";
        public static final String CREATED_TIME = "created_time";
        public static final String DEPARTMENT_ID = "department_id";
        public static final String HIERARCHY_LEVEL = "hierarchy_level";
        public static final String LAST_MODIFIED_BY = "last_modified_by";
        public static final String LAST_MODIFIED_TIME = "last_modified_time";
        public static final String NAME = "name";
        public static final String TENANT_ID = "tenant_id";
    }

    public class DepartmentHierarchyLevelConst {
        public static final String ID = "id";
        public static final String CREATED_BY = "created_by";
        public static final String CREATED_TIME = "created_time";
        public static final String DEPARTMENT_ID = "department_id";
        public static final String LABEL = "label";
        public static final String LAST_MODIFIED_BY = "last_modified_by";
        public static final String LAST_MODIFIED_TIME = "last_modified_time";
        public static final String LEVEL = "level";
        public static final String PARENT = "parent";
        public static final String TENANT_ID = "tenant_id";
    }

    public class DepartmentEntityRelationshipConst {
        public static final String PARENT_ID = "parent_id";
        public static final String CHILD_ID = "child_id";
        public static final String IS_TRUE = "is_true";
    }
}
