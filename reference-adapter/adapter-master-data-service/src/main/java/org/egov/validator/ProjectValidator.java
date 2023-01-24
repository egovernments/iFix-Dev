package org.egov.validator;

import org.apache.commons.lang3.StringUtils;
import org.egov.common.contract.request.RequestHeader;
import org.egov.repository.ProjectRepository;
import org.egov.tracer.model.CustomException;
import org.egov.util.DepartmentUtil;
import org.egov.util.DtoWrapper;
import org.egov.util.ExpenditureUtil;
import org.egov.util.MasterDataConstants;
import org.egov.web.models.ProjectDTO;
import org.egov.web.models.ProjectRequest;
import org.egov.web.models.ProjectSearchCriteria;
import org.egov.web.models.ProjectSearchRequest;
import org.egov.web.models.persist.Project;
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

    @Autowired
    private DtoWrapper dtoWrapper;

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
                throw new CustomException(MasterDataConstants.DEPARTMENT_ENTITY_ID, "DepartmentConst Entity id length is invalid."
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

        if (projectRequest != null && projectRequest.getProjectDTO() != null && projectRequest.getRequestHeader() != null) {
            RequestHeader requestHeader = projectRequest.getRequestHeader();

            if (requestHeader.getUserInfo() == null || StringUtils.isEmpty(requestHeader.getUserInfo().getUuid())) {
                errorMap.put(MasterDataConstants.USER_INFO, "User information is missing");
            }

            ProjectDTO projectDTO = projectRequest.getProjectDTO();

            validateTenantId(errorMap, projectDTO.getTenantId());

            if (StringUtils.isEmpty(projectDTO.getCode())) {
                errorMap.put(MasterDataConstants.PROJECT_CODE, "Project code is missing in request data");
            } else if (projectDTO.getCode().length() < 1 || projectDTO.getCode().length() > 64) {
                errorMap.put(MasterDataConstants.PROJECT_CODE, "Project code length is invalid. Length range [1-64]");
            }

            if (StringUtils.isEmpty(projectDTO.getName())) {
                errorMap.put(MasterDataConstants.PROJECT_NAME, "Project name is missing in request data");
            } else if (projectDTO.getName().length() < 1 || projectDTO.getName().length() > 64) {
                errorMap.put(MasterDataConstants.PROJECT_NAME, "Project name length is invalid. Length range [1-64]");
            }

            if (!StringUtils.isEmpty(projectDTO.getExpenditureId())) {
                if (projectDTO.getExpenditureId().length() < 2 || projectDTO.getExpenditureId().length() > 64) {
                    errorMap.put(MasterDataConstants.EXPENDITURE_ID, "Expenditure id length is invalid. Length range [2-64]");
                }

                if (!expenditureUtil.validateExpenditure(projectDTO.getTenantId(),
                        Collections.singletonList(projectDTO.getExpenditureId()), requestHeader)) {
                    errorMap.put(MasterDataConstants.EXPENDITURE_ID, "Expenditure id : " + projectDTO.getExpenditureId()
                            + " doesn't exist in the system");
                }
            }

            validateDepartmentEntityId(errorMap, requestHeader, projectDTO);

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

        if (projectRequest != null && projectRequest.getProjectDTO() != null && projectRequest.getRequestHeader() != null) {
            RequestHeader requestHeader = projectRequest.getRequestHeader();

            if (requestHeader.getUserInfo() == null || StringUtils.isEmpty(requestHeader.getUserInfo().getUuid())) {
                errorMap.put(MasterDataConstants.USER_INFO, "User information is missing");
            }

            ProjectDTO projectDTO = projectRequest.getProjectDTO();

            validateTenantId(errorMap, projectDTO.getTenantId());

            if (StringUtils.isBlank(projectDTO.getId())) {
                errorMap.put(MasterDataConstants.ID, "Project id is missing in request data");
            } else {
                Optional<Project> optionalProject = projectRepository.findByProjectId(projectDTO.getId());
                if (!optionalProject.isPresent()) {
                    errorMap.put(MasterDataConstants.INVALID_ID, "Invalid project id");
                }
            }

            validateDepartmentEntityId(errorMap, requestHeader, projectDTO);

            if (!errorMap.isEmpty()) {
                throw new CustomException(errorMap);
            }
        } else {
            throw new CustomException(MasterDataConstants.REQUEST_PAYLOAD_MISSING,
                    "Request payload is missing some value");
        }
    }

    private void validateDepartmentEntityId(Map<String, String> errorMap, RequestHeader requestHeader, ProjectDTO projectDTO) {
        if (projectDTO.getDepartmentEntityIds() != null && !projectDTO.getDepartmentEntityIds().isEmpty()) {
            List<String> departmentEntitytIds =
                    dtoWrapper.getIdListFromDepartmentEntityDTO(projectDTO.getDepartmentEntityIds());

            for (String departmentEntitytId : departmentEntitytIds) {
                if (!StringUtils.isEmpty(departmentEntitytId)) {
                    if (departmentEntitytId.length() < 2 || departmentEntitytId.length() > 64) {
                        errorMap.put(MasterDataConstants.DEPARTMENT_ENTITY_ID, "DepartmentConst Entity id length is invalid. " +
                                MasterDataConstants.LENGTH_RANGE_2_64);
                    }
                }
            }

            if (!departmentUtil.validateDepartmentEntityIds(projectDTO.getTenantId(),
                    dtoWrapper.getIdListFromDepartmentEntityDTO(projectDTO.getDepartmentEntityIds()), requestHeader)) {

                errorMap.put(MasterDataConstants.DEPARTMENT_ENTITY_ID, "Some of the DepartmentConst Entity ids : "
                        + projectDTO.getDepartmentEntityIds() + " doesn't exist in the system");
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
