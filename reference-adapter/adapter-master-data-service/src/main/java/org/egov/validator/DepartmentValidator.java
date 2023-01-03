package org.egov.validator;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.egov.common.contract.request.RequestHeader;
import org.egov.tracer.model.CustomException;
import org.egov.util.MasterDataConstants;
import org.egov.web.models.Department;
import org.egov.web.models.DepartmentRequest;
import org.egov.web.models.DepartmentSearchCriteria;
import org.egov.web.models.DepartmentSearchRequest;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class DepartmentValidator {

    /**
     * Validate the department search attribute(s)
     *
     * @param searchRequest
     */
    public void validateSearchPost(DepartmentSearchRequest searchRequest) {
        log.info("Enter into DepartmentValidator.validateSearchPost()");
        DepartmentSearchCriteria searchCriteria = searchRequest.getCriteria();
        RequestHeader requestHeader = searchRequest.getRequestHeader();
        Map<String, String> errorMap = new HashMap<>();

        //Header validation
        if (requestHeader == null) {
            throw new CustomException("REQUEST_HEADER", "Request header is missing");
        }

        if (searchCriteria == null) {
            throw new CustomException("INVALID_SEARCH_CRITERIA", "Search criteria is missing");
        }

        //Tenant id validation
        if (StringUtils.isBlank(searchCriteria.getTenantId())) {
            throw new CustomException("TENANT_ID", "Tenant id is mandatory");
        }
        if (StringUtils.isNotBlank(searchCriteria.getTenantId())
                && (searchCriteria.getTenantId().length() < 2 || searchCriteria.getTenantId().length() > 64))
            errorMap.put("TENANT_ID_LENGTH", "Tenant id's length is invalid");

        //name
        if (StringUtils.isNotBlank(searchCriteria.getName())
                && (searchCriteria.getName().length() < 2 || searchCriteria.getName().length() > 256))
            errorMap.put("DEPARTMENT_NAME", "Department name's length is invalid");

        //code
        if (StringUtils.isNotBlank(searchCriteria.getCode())
                && (searchCriteria.getCode().length() < 2 || searchCriteria.getCode().length() > 64))
            errorMap.put("DEPARTMENT_CODE", "Department code's length is invalid");

        log.info("Exit from DepartmentValidator.validateSearchPost()");
        if (!errorMap.isEmpty())
            throw new CustomException(errorMap);
    }

    /**
     * @param departmentRequest
     */
    public void validateCreateRequestData(DepartmentRequest departmentRequest) {
        Map<String, String> errorMap = new HashMap<>();

        if (departmentRequest != null && departmentRequest.getDepartment() != null
                && departmentRequest.getRequestHeader() != null) {
            RequestHeader requestHeader = departmentRequest.getRequestHeader();

            if (requestHeader.getUserInfo() == null || StringUtils.isEmpty(requestHeader.getUserInfo().getUuid())) {
                errorMap.put(MasterDataConstants.USER_INFO, "User information is missing");
            }

            Department department = departmentRequest.getDepartment();

            if (StringUtils.isEmpty(department.getTenantId())) {
                errorMap.put(MasterDataConstants.TENANT_ID, "Tenant id is missing in request data");
            } else if (department.getTenantId().length() < 2 || department.getTenantId().length() > 64) {
                errorMap.put(MasterDataConstants.TENANT_ID, "Tenant id length is invalid. " +
                        "Length range [2-64]");
            }

            if (StringUtils.isEmpty(department.getCode())) {
                errorMap.put(MasterDataConstants.DEPARTMENT_CODE, "Department code is missing in request data");
            } else if (department.getCode().length() < 1 || department.getCode().length() > 64) {
                errorMap.put(MasterDataConstants.DEPARTMENT_CODE, "Department code length is invalid. " +
                        "Length range [1-64]");
            }

            if (StringUtils.isEmpty(department.getCode())) {
                errorMap.put(MasterDataConstants.DEPARTMENT_NAME, "Department name is missing in request data");
            } else if (department.getCode().length() < 1 || department.getCode().length() > 64) {
                errorMap.put(MasterDataConstants.DEPARTMENT_NAME, "Department name length is invalid. " +
                        "Length range [1-64]");
            }

            if (StringUtils.isNotBlank(department.getParent())
                    && (department.getParent().length() < 2 || department.getParent().length() > 64))
                errorMap.put(MasterDataConstants.DEPARTMENT_PARENT, "Department parent length is invalid. " +
                        "Length range [1-64]");
        }

        if (!errorMap.isEmpty())
            throw new CustomException(errorMap);
    }
}
