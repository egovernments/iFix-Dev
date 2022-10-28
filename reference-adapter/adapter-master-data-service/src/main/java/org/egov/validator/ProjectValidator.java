package org.egov.validator;

import org.apache.commons.lang3.StringUtils;
import org.egov.common.contract.request.RequestHeader;
import org.egov.repository.ProjectRepository;
import org.egov.tracer.model.CustomException;
import org.egov.util.DepartmentUtil;
import org.egov.util.ExpenditureUtil;
import org.egov.util.MasterDataConstants;
import org.egov.web.models.Project;
import org.egov.web.models.ProjectRequest;
import org.egov.web.models.ProjectSearchCriteria;
import org.egov.web.models.ProjectSearchRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class ProjectValidator {

    @Autowired
    private ExpenditureUtil expenditureUtil;

    @Autowired
    private DepartmentUtil departmentUtil;

    @Autowired
    private ProjectRepository projectRepository;

    /**
     * @param projectSearchRequest
     */
    public void validateProjectSearchRequest(ProjectSearchRequest projectSearchRequest) {
        if (projectSearchRequest != null && projectSearchRequest.getRequestHeader() != null
                && projectSearchRequest.getCriteria() != null) {

            RequestHeader requestHeader = projectSearchRequest.getRequestHeader();

            ProjectSearchCriteria projectSearchCriteria = projectSearchRequest.getCriteria();

            if (projectSearchCriteria == null) {
                throw new CustomException("INVALID_SEARCH_CRITERIA", "Search criteria is missing");
            }
            if (StringUtils.isEmpty(projectSearchCriteria.getTenantId())) {
                throw new CustomException(MasterDataConstants.TENANT_ID, MasterDataConstants.TENANT_ID_IS_MISSING_IN_REQUEST_DATA);
            }

            if (projectSearchCriteria.getTenantId().length() < 2 || projectSearchCriteria.getTenantId().length() > 64) {
                throw new CustomException(MasterDataConstants.TENANT_ID, MasterDataConstants.TENANT_ID_LENGTH_IS_INVALID +
                        MasterDataConstants.LENGTH_RANGE_2_64);
            }

            if (!StringUtils.isEmpty(projectSearchCriteria.getName())
                    && (projectSearchCriteria.getName().length() < 2 || projectSearchCriteria.getName().length() > 256)) {
                throw new CustomException(MasterDataConstants.PROJECT_NAME, "Project name length is invalid. " +
                        "Length range [2-256]");
            }

            if (!StringUtils.isEmpty(projectSearchCriteria.getCode())
                    && (projectSearchCriteria.getCode().length() < 2 || projectSearchCriteria.getCode().length() > 64)) {
                throw new CustomException(MasterDataConstants.PROJECT_CODE, "Project code length is invalid. " +
                        MasterDataConstants.LENGTH_RANGE_2_64);
            }

            if (!StringUtils.isEmpty(projectSearchCriteria.getExpenditureId())
                    && (projectSearchCriteria.getExpenditureId().length() < 2 || projectSearchCriteria.getExpenditureId().length() > 64)) {
                throw new CustomException(MasterDataConstants.EXPENDITURE_ID, "Expenditure id length is invalid. " +
                        MasterDataConstants.LENGTH_RANGE_2_64);
            }

            if (!StringUtils.isEmpty(projectSearchCriteria.getDepartmentEntityId())
                    && (projectSearchCriteria.getDepartmentEntityId().length() < 2
                    || projectSearchCriteria.getDepartmentEntityId().length() > 64)) {
                throw new CustomException(MasterDataConstants.DEPARTMENT_ENTITY_ID, "Department Entity id length is invalid."
                        + MasterDataConstants.LENGTH_RANGE_2_64);
            }

            if (!StringUtils.isEmpty(projectSearchCriteria.getLocationId())
                    && (projectSearchCriteria.getLocationId().length() < 2
                    || projectSearchCriteria.getLocationId().length() > 64)) {

                throw new CustomException(MasterDataConstants.LOCATION_ID, "Location id length is invalid. " +
                        MasterDataConstants.LENGTH_RANGE_2_64);
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

            validateTenantId(errorMap, project.getTenantId());

            if (StringUtils.isEmpty(project.getCode())) {
                errorMap.put(MasterDataConstants.PROJECT_CODE, "Project code is missing in request data");
            } else if (project.getCode().length() < 1 || project.getCode().length() > 64) {
                errorMap.put(MasterDataConstants.PROJECT_CODE, "Project code length is invalid. Length range [1-64]");
            }

            if (StringUtils.isEmpty(project.getName())) {
                errorMap.put(MasterDataConstants.PROJECT_NAME, "Project name is missing in request data");
            } else if (project.getName().length() < 1 || project.getName().length() > 64) {
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

            validateDepartmentEntityId(errorMap, requestHeader, project);

            if (!errorMap.isEmpty()) {
                throw new CustomException(errorMap);
            }
        } else {
            throw new CustomException(MasterDataConstants.REQUEST_PAYLOAD_MISSING,
                    "Request payload is missing some value");
        }
    }

    public void validateProjectUpdateRequest(ProjectRequest projectRequest) {
        Map<String, String> errorMap = new HashMap<>();

        if (projectRequest != null && projectRequest.getProject() != null && projectRequest.getRequestHeader() != null) {
            RequestHeader requestHeader = projectRequest.getRequestHeader();

            if (requestHeader.getUserInfo() == null || StringUtils.isEmpty(requestHeader.getUserInfo().getUuid())) {
                errorMap.put(MasterDataConstants.USER_INFO, "User information is missing");
            }

            Project project = projectRequest.getProject();

            validateTenantId(errorMap, project.getTenantId());

            if (StringUtils.isBlank(project.getId())) {
                errorMap.put(MasterDataConstants.ID, "Project id is missing in request data");
            } else {
                Optional<Project> optionalProject = projectRepository.findByProjectId(project.getId());
                if (!optionalProject.isPresent()) {
                    errorMap.put(MasterDataConstants.INVALID_ID, "Invalid project id");
                }
            }

            validateDepartmentEntityId(errorMap, requestHeader, project);

            if (!errorMap.isEmpty()) {
                throw new CustomException(errorMap);
            }
        } else {
            throw new CustomException(MasterDataConstants.REQUEST_PAYLOAD_MISSING,
                    "Request payload is missing some value");
        }
    }

    private void validateDepartmentEntityId(Map<String, String> errorMap, RequestHeader requestHeader, Project project) {
        if (project.getDepartmentEntityIds() != null && !project.getDepartmentEntityIds().isEmpty()) {
            List<String> departmentEntitytIds = project.getDepartmentEntityIds();
            for (String departmentEntitytId : departmentEntitytIds) {
                if (!StringUtils.isEmpty(departmentEntitytId)) {
                    if (departmentEntitytId.length() < 2 || departmentEntitytId.length() > 64) {
                        errorMap.put(MasterDataConstants.DEPARTMENT_ENTITY_ID, "Department Entity id length is invalid. " +
                                MasterDataConstants.LENGTH_RANGE_2_64);
                    }
                }
            }

            if (!departmentUtil.validateDepartmentEntityIds(project.getTenantId(),
                    project.getDepartmentEntityIds(), requestHeader)) {
                errorMap.put(MasterDataConstants.DEPARTMENT_ENTITY_ID, "Some of the Department Entity ids : "
                        + project.getDepartmentEntityIds() + " doesn't exist in the system");
            }
        }
    }

    private void validateTenantId(Map<String, String> errorMap, String tenantId) {
        if (StringUtils.isEmpty(tenantId)) {
            errorMap.put(MasterDataConstants.TENANT_ID, MasterDataConstants.TENANT_ID_IS_MISSING_IN_REQUEST_DATA);
        } else if (tenantId.length() < 2 || tenantId.length() > 64) {
            errorMap.put(MasterDataConstants.TENANT_ID, MasterDataConstants.TENANT_ID_LENGTH_IS_INVALID +
                    MasterDataConstants.LENGTH_RANGE_2_64);
        }
    }
}
