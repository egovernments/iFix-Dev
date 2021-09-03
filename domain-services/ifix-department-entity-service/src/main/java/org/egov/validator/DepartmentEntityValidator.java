package org.egov.validator;

import org.apache.commons.lang3.StringUtils;
import org.egov.common.contract.request.RequestHeader;
import org.egov.tracer.model.CustomException;
import org.egov.util.DepartmentEntityConstant;
import org.egov.util.GovernmentUtil;
import org.egov.web.models.DepartmentEntity;
import org.egov.web.models.DepartmentEntityRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class DepartmentEntityValidator {

    @Autowired
    private GovernmentUtil governmentUtil;

    /**
     * TODO: validation of HierarchyLevel and department id check.
     * Both combination should be checked in Department Hierarchy Level meta data.
     *
     * @param departmentEntityRequest
     * @return
     */
    public void validateDepartmentEntityRequest(DepartmentEntityRequest departmentEntityRequest) {
        Map<String, String> errorMap = new HashMap<>();

        if (departmentEntityRequest != null && departmentEntityRequest.getRequestHeader() != null
                && departmentEntityRequest.getDepartmentEntity() != null) {

            RequestHeader requestHeader = departmentEntityRequest.getRequestHeader();

            if (requestHeader.getUserInfo() == null || StringUtils.isEmpty(requestHeader.getUserInfo().getUuid())) {
                errorMap.put(DepartmentEntityConstant.USER_INFO, "User information is missing");
            }

            DepartmentEntity departmentEntity = departmentEntityRequest.getDepartmentEntity();

            if (StringUtils.isEmpty(departmentEntity.getTenantId())) {
                errorMap.put(DepartmentEntityConstant.TENANT_ID, "Tenant id is missing in request data");
            }else if (departmentEntity.getTenantId().length() < 2 || departmentEntity.getTenantId().length() > 64) {
                errorMap.put(DepartmentEntityConstant.TENANT_ID, "Tenant id length is invalid. " +
                        "Length range [2-64]");
            }else {
                List<String> governments = governmentUtil.getGovernmentFromGovernmentService(departmentEntity.getTenantId(),requestHeader);
                if (governments.isEmpty())
                    errorMap.put(DepartmentEntityConstant.INVALID_TENANT_ID, "Tenant id : " + departmentEntity.getTenantId()
                            + " doesn't exist in the system");
            }

            if (StringUtils.isEmpty(departmentEntity.getCode())) {
                errorMap.put(DepartmentEntityConstant.DEPARTMENT_ENTITY_CODE, "Department entity code" +
                        " is missing in request data");
            }else if (departmentEntity.getCode().length() < 1 || departmentEntity.getCode().length() > 256) {
                errorMap.put(DepartmentEntityConstant.DEPARTMENT_ENTITY_CODE, "Department entity code " +
                        "length is invalid. Length range [2-256]");
            }

            if (StringUtils.isEmpty(departmentEntity.getName())) {
                errorMap.put(DepartmentEntityConstant.DEPARTMENT_ENTITY_NAME, "Department entity name" +
                        " is missing in request data");
            }else if (departmentEntity.getName().length() < 1 || departmentEntity.getName().length() > 256) {
                errorMap.put(DepartmentEntityConstant.DEPARTMENT_ENTITY_NAME, "Department entity name " +
                        "length is invalid. Length range [2-256]");
            }

            if (departmentEntity.getHierarchyLevel() == null) {
                errorMap.put(DepartmentEntityConstant.DEPARTMENT_HIERARCHY_ID, "Department hierarchy id" +
                        " is missing in request data");
            }else if (departmentEntity.getHierarchyLevel() < 1 || departmentEntity.getHierarchyLevel() > 256) {
                errorMap.put(DepartmentEntityConstant.DEPARTMENT_HIERARCHY_ID, "Department hierarchy id " +
                        "length is invalid. Length range [2-256]");
            }

            if (departmentEntity.getChildren() == null) {
                errorMap.put(DepartmentEntityConstant.DEPARTMENT_CHILDREN, "Department children information is missing");
            }

            if (!errorMap.isEmpty()) {
                throw new CustomException(errorMap);
            }

        }else {
            throw new CustomException(DepartmentEntityConstant.REQUEST_PAYLOAD_MISSING, "Request payload is missing some value");
        }
    }
}
