package org.egov.validator;

import org.apache.commons.lang3.StringUtils;
import org.egov.common.contract.request.RequestHeader;
import org.egov.tracer.model.CustomException;
import org.egov.util.DepartmentEntityConstant;
import org.egov.web.models.DepartmentEntity;
import org.egov.web.models.DepartmentEntityRequest;
import org.springframework.stereotype.Component;

@Component
public class DepartmentEntityValidator {

    /**
     * TODO: validation of HierarchyLevelId and department id check.
     * Both combination should be checked in Department Hierarchy Level meta data.
     *
     * @param departmentEntityRequest
     * @return
     */
    public void validateDepartmentEntityRequest(DepartmentEntityRequest departmentEntityRequest) {
        if (departmentEntityRequest != null && departmentEntityRequest.getRequestHeader() != null
                && departmentEntityRequest.getDepartmentEntity() != null) {

            RequestHeader requestHeader = departmentEntityRequest.getRequestHeader();

            if (requestHeader.getUserInfo() == null || StringUtils.isEmpty(requestHeader.getUserInfo().getUuid())) {
                throw new CustomException(DepartmentEntityConstant.USER_INFO, "User information is missing");
            }

            DepartmentEntity departmentEntity = departmentEntityRequest.getDepartmentEntity();

            if (StringUtils.isEmpty(departmentEntity.getTenantId())) {
                throw new CustomException(DepartmentEntityConstant.TENANT_ID, "Tenant id is missing in request data");
            }else if (departmentEntity.getTenantId().length() < 2 || departmentEntity.getTenantId().length() > 64) {
                throw new CustomException(DepartmentEntityConstant.TENANT_ID, "Tenant id length is invalid. " +
                        "Length range [2-64]");
            }

            if (StringUtils.isEmpty(departmentEntity.getCode())) {
                throw new CustomException(DepartmentEntityConstant.DEPARTMENT_ENTITY_CODE, "Department entity code" +
                        " is missing in request data");
            }else if (departmentEntity.getCode().length() < 1 || departmentEntity.getCode().length() > 256) {
                throw new CustomException(DepartmentEntityConstant.DEPARTMENT_ENTITY_CODE, "Department entity code " +
                        "length is invalid. Length range [2-256]");
            }

            if (StringUtils.isEmpty(departmentEntity.getName())) {
                throw new CustomException(DepartmentEntityConstant.DEPARTMENT_ENTITY_NAME, "Department entity name" +
                        " is missing in request data");
            }else if (departmentEntity.getName().length() < 1 || departmentEntity.getName().length() > 256) {
                throw new CustomException(DepartmentEntityConstant.DEPARTMENT_ENTITY_NAME, "Department entity name " +
                        "length is invalid. Length range [2-256]");
            }

            if (departmentEntity.getHierarchyLevelId() == null) {
                throw new CustomException(DepartmentEntityConstant.DEPARTMENT_HIERARCHY_ID, "Department hierarchy id" +
                        " is missing in request data");
            }else if (departmentEntity.getHierarchyLevelId() < 1 || departmentEntity.getHierarchyLevelId() > 256) {
                throw new CustomException(DepartmentEntityConstant.DEPARTMENT_HIERARCHY_ID, "Department hierarchy id " +
                        "length is invalid. Length range [2-256]");
            }

            if (departmentEntity.getChildren() == null || departmentEntity.getChildren().isEmpty()) {
                throw new CustomException(DepartmentEntityConstant.USER_INFO,
                        "Department children information is missing");
            }
        }else {
            throw new CustomException(DepartmentEntityConstant.REQUEST_PAYLOAD_MISSING, "Request payload is missing some value");
        }
    }
}
