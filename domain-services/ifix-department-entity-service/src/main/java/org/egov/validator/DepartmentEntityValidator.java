package org.egov.validator;

import org.apache.commons.lang3.StringUtils;
import org.egov.common.contract.request.RequestHeader;
import org.egov.repository.DepartmentEntityRepository;
import org.egov.tracer.model.CustomException;
import org.egov.util.DepartmentEntityConstant;
import org.egov.util.DepartmentHierarchyUtil;
import org.egov.util.GovernmentUtil;
import org.egov.web.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class DepartmentEntityValidator {

    @Autowired
    private GovernmentUtil governmentUtil;

    @Autowired
    DepartmentEntityRepository departmentEntityRepository;

    @Autowired
    private DepartmentHierarchyUtil hierarchyUtil;

    /**
     *
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
            } else if (departmentEntity.getTenantId().length() < 2 || departmentEntity.getTenantId().length() > 64) {
                errorMap.put(DepartmentEntityConstant.TENANT_ID, "Tenant id length is invalid. " +
                        "Length range [2-64]");
            } else {
                List<String> governments = governmentUtil.getGovernmentFromGovernmentService(departmentEntity.getTenantId(), requestHeader);
                if (governments.isEmpty())
                    errorMap.put(DepartmentEntityConstant.INVALID_TENANT_ID, "Tenant id : " + departmentEntity.getTenantId()
                            + " doesn't exist in the system");
            }

            if (StringUtils.isEmpty(departmentEntity.getCode())) {
                errorMap.put(DepartmentEntityConstant.DEPARTMENT_ENTITY_CODE, "Department entity code" +
                        " is missing in request data");
            } else if (departmentEntity.getCode().length() < 1 || departmentEntity.getCode().length() > 256) {
                errorMap.put(DepartmentEntityConstant.DEPARTMENT_ENTITY_CODE, "Department entity code " +
                        "length is invalid. Length range [2-256]");
            }

            if (StringUtils.isEmpty(departmentEntity.getName())) {
                errorMap.put(DepartmentEntityConstant.DEPARTMENT_ENTITY_NAME, "Department entity name" +
                        " is missing in request data");
            } else if (departmentEntity.getName().length() < 1 || departmentEntity.getName().length() > 256) {
                errorMap.put(DepartmentEntityConstant.DEPARTMENT_ENTITY_NAME, "Department entity name " +
                        "length is invalid. Length range [2-256]");
            }

            if (departmentEntity.getHierarchyLevel() == null) {
                errorMap.put(DepartmentEntityConstant.DEPARTMENT_HIERARCHY_ID, "Department hierarchy id" +
                        " is missing in request data");
            } else if (departmentEntity.getHierarchyLevel() < 1 || departmentEntity.getHierarchyLevel() > 256) {
                errorMap.put(DepartmentEntityConstant.DEPARTMENT_HIERARCHY_ID, "Department hierarchy id " +
                        "length is invalid. Length range [2-256]");
            }

            if (departmentEntity.getChildren() == null) {
                errorMap.put(DepartmentEntityConstant.DEPARTMENT_CHILDREN, "Department children information is missing");
            }else {
                List<DepartmentEntity> departmentEntityList = departmentEntityRepository
                        .searchChildDepartment(departmentEntity.getChildren(), departmentEntity.getHierarchyLevel());

                if (departmentEntityList == null || departmentEntityList.isEmpty()
                        || (departmentEntityList.size() != departmentEntity.getChildren().size())) {
                    errorMap.put(DepartmentEntityConstant.DEPARTMENT_CHILDREN, "Invalid children id list");
                }
            }

            if (!errorMap.isEmpty()) {
                throw new CustomException(errorMap);
            }
            if (StringUtils.isNotBlank(departmentEntity.getDepartmentId())
                    && departmentEntity.getHierarchyLevel() != null
                    && StringUtils.isNotBlank(departmentEntity.getTenantId())) {
                List<DepartmentHierarchyLevel> departmentHierarchyLevels = hierarchyUtil.validateHierarchyLevelMetaData(departmentEntity.getDepartmentId()
                        , departmentEntity.getHierarchyLevel()
                        , departmentEntity.getTenantId());
                if (departmentHierarchyLevels == null || departmentHierarchyLevels.isEmpty()) {
                    errorMap.put(DepartmentEntityConstant.INVALID_HIERARCHY_LEVEL, "Given Hierarchy level of this department id : "
                            + departmentEntity.getDepartmentId() + " doesn't exist in the system.");
                }
            }
        } else {
            throw new CustomException(DepartmentEntityConstant.REQUEST_PAYLOAD_MISSING, "Request payload is missing some value");
        }
    }

    public void validateSearchDepartmentEntity(DepartmentEntitySearchRequest departmentEntitySearchRequest) {
        DepartmentEntitySearchCriteria searchCriteria = departmentEntitySearchRequest.getCriteria();
        RequestHeader requestHeader = departmentEntitySearchRequest.getRequestHeader();
        Map<String, String> errorMap = new HashMap<>();
        //header
        if (requestHeader == null) {
            throw new CustomException(DepartmentEntityConstant.ERROR_REQUEST_HEADER, "Request header is missing");
        }
        if (requestHeader.getUserInfo() == null || requestHeader.getUserInfo().getUuid() == null) {
            errorMap.put(DepartmentEntityConstant.USER_INFO, "User info is missing");
        }
        //criteria
        if (searchCriteria == null) {
            throw new CustomException(DepartmentEntityConstant.ERROR_SEARCH_CRITERIA, "Search criteria is missing");
        }
        if (StringUtils.isEmpty(searchCriteria.getTenantId())) {
            errorMap.put(DepartmentEntityConstant.TENANT_ID, "Tenant id is missing in request data");
        }
        if (!errorMap.isEmpty()) {
            throw new CustomException(errorMap);
        }
    }
}
