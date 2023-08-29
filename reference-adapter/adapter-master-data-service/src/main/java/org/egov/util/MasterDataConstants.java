package org.egov.util;

public class MasterDataConstants {
    private MasterDataConstants() {
    }

    public static final String CRITERIA_TENANT_ID = "tenantId";
    public static final String CRITERIA_NAME = "name";
    public static final String CRITERIA_CODE = "code";
    public static final String CRITERIA_ID = "id";
    public static final String LENGTH_RANGE_2_64 = "Length range [2-64]";
    public static final String TENANT_ID_IS_MISSING_IN_REQUEST_DATA = "Tenant id is missing in request data";
    public static final String TENANT_ID_LENGTH_IS_INVALID = "Tenant id length is invalid. ";

    public static final String USER_INFO = "USER_INFO";
    public static final String GOVERNMENT_ID = "GOVERNMENT_ID";
    public static final String GOVERNMENT_NAME = "GOVERNMENT_NAME";
    public static final String TENANT_ID = "TENANT_ID";
    public static final String EXPENDITURE_NAME = "EXPENDITURE_NAME";
    public static final String EXPENDITURE_CODE = "EXPENDITURE_CODE";
    public static final String EXPENDITURE_TYPE = "EXPENDITURE_TYPE";

    public static final String REQUEST_PAYLOAD_MISSING = "REQUEST_PAYLOAD";
    public static final String PROJECT_NAME = "PROJECT_NAME";
    public static final String PROJECT_CODE = "PROJECT_CODE";
    public static final String EXPENDITURE_ID = "EXPENDITURE_ID";
    public static final String DEPARTMENT_ID = "DEPARTMENT_ID";
    public static final String LOCATION_ID = "LOCATION_ID";
    public static final String IDS = "Ids";
    public static final String REQUEST_HEADER = "requestHeader";
    public static final String CRITERIA = "criteria";
    public static final String TENANT_LIST = "$.government.*";
    public static final String EXPENDITURE_LIST = "$.expenditure.*";
    public static final String DEPARTMENT_ENTITY_LIST = "$.departmentEntity.*";
    public static final String DEPARTMENT_JSON_PATH = "$.department.*";
    public static final String DEPARTMENT_ENTITY_ID = "DEPARTMENT_ENTITY_ID";

    public static final String JSONPATH_ERROR = "JSONPATH_ERROR";
    public static final String GET_ANCESTRY = "getAncestry";

    public static final String REQUEST_HEADER_MISSING = "REQUEST_HEADER";
    public static final String DEPARTMENT_CODE = "DEPARTMENT_CODE";
    public static final String DEPARTMENT_NAME = "DEPARTMENT_NAME";
    public static final String DEPARTMENT_PARENT = "DEPARTMENT_PARENT";

    public static final String ID = "ID";
    public static final String INVALID_ID = "INVALID_ID";

    public class ProjectConst {
        public static final String ID = "id";
        public static final String TENANT_ID = "tenant_id";
        public static final String CODE = "code";
        public static final String NAME = "name";
        public static final String EXPENDITURE_ID = "expenditure_id";
        public static final String DEPARTMENT_ENTITY_ID = "departmentEntity_id";
        public static final String LOCATION_ID = "location_id";
        public static final String CREATED_BY = "created_by";
        public static final String CREATED_TIME = "created_time";
        public static final String LAST_MODIFIED_BY = "last_modified_by";
        public static final String LAST_MODIFIED_TIME = "last_modified_time";
    }

    public class DepartmentConst {
        public static final String ID = "id";
        public static final String TENANT_ID = "tenant_id";
        public static final String CODE = "code";
        public static final String NAME = "name";
        public static final String IS_NODAL = "isNodal";
        public static final String PARENT = "parent";
        public static final String CREATED_BY = "created_by";
        public static final String CREATED_TIME = "created_time";
        public static final String LAST_MODIFIED_BY = "last_modified_by";
        public static final String LAST_MODIFIED_TIME = "last_modified_time";
    }

    public class ExpenditureConst {
        public static final String ID = "id";
        public static final String TENANT_ID = "tenant_id";
        public static final String CODE = "code";
        public static final String NAME = "name";
        public static final String TYPE = "type";
        public static final String DEPARTMENT_ID = "departmentId";
        public static final String CREATED_BY = "created_by";
        public static final String CREATED_TIME = "created_time";
        public static final String LAST_MODIFIED_BY = "last_modified_by";
        public static final String LAST_MODIFIED_TIME = "last_modified_time";
    }

    public class ProjectDepartmentEntityRelationshipConst {
        public static final String PROJECT_ID = "project_id";
        public static final String PROJECT_DEPARTMENT_ENTITY_ID = "department_entity_id";
        public static final String STATUS = "status";
    }

}
