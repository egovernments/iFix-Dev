package org.egov.validator;

import org.apache.commons.lang3.StringUtils;
import org.egov.common.contract.request.RequestHeader;
import org.egov.tracer.model.CustomException;
import org.egov.util.DepartmentUtil;
import org.egov.util.ExpenditureUtil;
import org.egov.util.MasterDataConstants;
import org.egov.util.TenantUtil;
import org.egov.web.models.Project;
import org.egov.web.models.ProjectRequest;
import org.egov.web.models.ProjectSearchCriteria;
import org.egov.web.models.ProjectSearchRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ProjectValidator {

    @Autowired
    private TenantUtil tenantUtil;

    @Autowired
    private ExpenditureUtil expenditureUtil;

    @Autowired
    private DepartmentUtil departmentUtil;

    /**
     * @param projectSearchRequest
     */
    public void validateProjectSearchRequest(ProjectSearchRequest projectSearchRequest) {
        if (projectSearchRequest != null && projectSearchRequest.getRequestHeader() != null
                && projectSearchRequest.getCriteria() != null) {

            RequestHeader requestHeader = projectSearchRequest.getRequestHeader();

            if (requestHeader.getUserInfo() == null || StringUtils.isEmpty(requestHeader.getUserInfo().getUuid())) {
                throw new CustomException(MasterDataConstants.USER_INFO, "User information is missing");
            }

            ProjectSearchCriteria projectSearchCriteria = projectSearchRequest.getCriteria();

            if (StringUtils.isEmpty(projectSearchCriteria.getTenantId())) {
                throw new CustomException(MasterDataConstants.TENANT_ID, "Tenant id is missing in request data");
            }

            if (projectSearchCriteria.getTenantId().length() < 2 || projectSearchCriteria.getTenantId().length() > 64) {
                throw new CustomException(MasterDataConstants.TENANT_ID, "Tenant id length is invalid. " +
                        "Length range [2-64]");
            }

            if (!StringUtils.isEmpty(projectSearchCriteria.getName())
                    && (projectSearchCriteria.getName().length() < 2 || projectSearchCriteria.getName().length() > 256)) {
                throw new CustomException(MasterDataConstants.PROJECT_NAME, "Project name length is invalid. " +
                        "Length range [2-256]");
            }

            if (!StringUtils.isEmpty(projectSearchCriteria.getCode())
                    && (projectSearchCriteria.getCode().length() < 2 || projectSearchCriteria.getCode().length() > 64)) {
                throw new CustomException(MasterDataConstants.PROJECT_CODE, "Project code length is invalid. " +
                        "Length range [2-64]");
            }

            if (!StringUtils.isEmpty(projectSearchCriteria.getExpenditureId())
                    && (projectSearchCriteria.getExpenditureId().length() < 2 || projectSearchCriteria.getExpenditureId().length() > 64)) {
                throw new CustomException(MasterDataConstants.EXPENDITURE_ID, "Expenditure id length is invalid. " +
                        "Length range [2-64]");
            }

            if (!StringUtils.isEmpty(projectSearchCriteria.getDepartmentId())
                    && (projectSearchCriteria.getDepartmentId().length() < 2
                    || projectSearchCriteria.getDepartmentId().length() > 64)) {

                throw new CustomException(MasterDataConstants.DEPARTMENT_ID, "Department id length is invalid. " +
                        "Length range [2-64]");
            }

            if (!StringUtils.isEmpty(projectSearchCriteria.getLocationId())
                    && (projectSearchCriteria.getLocationId().length() < 2
                    || projectSearchCriteria.getLocationId().length() > 64)) {

                throw new CustomException(MasterDataConstants.LOCATION_ID, "Location id length is invalid. " +
                        "Length range [2-64]");
            }
        }
    }

    /**
     * @param projectRequest
     */
    public void validateProjectCreateRequest(ProjectRequest projectRequest) {
        Map<String, String> errorMap = new HashMap<>();

        if (projectRequest != null && projectRequest.getProject() != null && projectRequest.getRequestHeader() != null) {
            RequestHeader requestHeader = projectRequest.getRequestHeader();

            if (requestHeader.getUserInfo() == null || StringUtils.isEmpty(requestHeader.getUserInfo().getUuid())) {
                errorMap.put(MasterDataConstants.USER_INFO, "User information is missing");
            }

            Project project = projectRequest.getProject();

            if (StringUtils.isEmpty(project.getTenantId())) {
                errorMap.put(MasterDataConstants.TENANT_ID, "Tenant id is missing in request data");
            }else if (project.getTenantId().length() < 2 || project.getTenantId().length() > 64) {
                errorMap.put(MasterDataConstants.TENANT_ID, "Tenant id length is invalid. " +
                        "Length range [2-64]");
            }else if (!tenantUtil.validateTenant(project.getTenantId(),requestHeader)) {
                    errorMap.put(MasterDataConstants.TENANT_ID, "Tenant id : " + project.getTenantId()
                            + " doesn't exist in the system");
            }

            if (StringUtils.isEmpty(project.getCode())) {
                errorMap.put(MasterDataConstants.PROJECT_CODE, "Project code is missing in request data");
            }else if (project.getCode().length() < 1 || project.getCode().length() > 64) {
                errorMap.put(MasterDataConstants.PROJECT_CODE, "Project code length is invalid. Length range [1-64]");
            }

            if (StringUtils.isEmpty(project.getName())) {
                errorMap.put(MasterDataConstants.PROJECT_NAME, "Project name is missing in request data");
            }else if (project.getName().length() < 1 || project.getName().length() > 64) {
                errorMap.put(MasterDataConstants.PROJECT_NAME, "Project name length is invalid. Length range [1-64]");
            }

            if (!StringUtils.isEmpty(project.getExpenditureId())) {
                if (project.getExpenditureId().length() < 2 || project.getExpenditureId().length() > 64) {
                    errorMap.put(MasterDataConstants.EXPENDITURE_ID, "Expenditure id length is invalid. Length range [2-64]");
                }

                if (!expenditureUtil.validateExpenditure(project.getTenantId(),
                        Collections.singletonList(project.getExpenditureId()), requestHeader)) {
                    errorMap.put(MasterDataConstants.EXPENDITURE_ID, "Expenditure id : " + project.getExpenditureId()
                            + " doesn't exist in the system");
                }
            }

            if (!StringUtils.isEmpty(project.getDepartmentEntitytId())) {
                if (project.getDepartmentEntitytId().length() < 2 || project.getDepartmentEntitytId().length() > 64) {
                    errorMap.put(MasterDataConstants.DEPARTMENT_ENTITY_ID, "Department Entity id length is invalid. " +
                            "Length range [2-64]");
                }

                if (!departmentUtil.validateDepartmentEntity(project.getTenantId(),
                        Collections.singletonList(project.getDepartmentEntitytId()), requestHeader)) {
                    errorMap.put(MasterDataConstants.DEPARTMENT_ENTITY_ID, "Department Entity id : "
                            + project.getExpenditureId() + " doesn't exist in the system");
                }
            }

            if (!errorMap.isEmpty()) {
                throw new CustomException(errorMap);
            }
        }else {
            throw new CustomException(MasterDataConstants.REQUEST_PAYLOAD_MISSING,
                    "Request payload is missing some value");
        }
    }
}
