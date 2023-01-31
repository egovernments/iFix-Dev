package org.egov.validator;

import org.apache.commons.lang3.StringUtils;
import org.egov.common.contract.request.RequestHeader;
import org.egov.repository.DepartmentEntityRepository;
import org.egov.repository.DepartmentHierarchyLevelRepository;
import org.egov.tracer.model.CustomException;
import org.egov.util.DepartmentEntityConstant;
import org.egov.util.DepartmentHierarchyUtil;
import org.egov.util.DtoWrapper;
import org.egov.web.models.*;
import org.egov.web.models.persist.DepartmentEntity;
import org.egov.web.models.persist.DepartmentHierarchyLevel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class DepartmentEntityValidator {

    @Autowired
    private DepartmentEntityRepository departmentEntityRepository;

    @Autowired
    private DepartmentHierarchyLevelRepository hierarchyLevelRepository;

    @Autowired
    private DepartmentHierarchyUtil hierarchyUtil;

    @Autowired
    private DtoWrapper dtoWrapper;

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
                && departmentEntityRequest.getDepartmentEntityDTO() != null) {

            DepartmentEntityDTO departmentEntityDTO = departmentEntityRequest.getDepartmentEntityDTO();

            if (StringUtils.isEmpty(departmentEntityDTO.getTenantId())) {
                errorMap.put(DepartmentEntityConstant.TENANT_ID, "Tenant id is missing in request data");
            } else if (departmentEntityDTO.getTenantId().length() < 2 || departmentEntityDTO.getTenantId().length() > 64) {
                errorMap.put(DepartmentEntityConstant.TENANT_ID, "Tenant id length is invalid. " +
                        "Length range [2-64]");
            }

            if (StringUtils.isEmpty(departmentEntityDTO.getCode())) {
                errorMap.put(DepartmentEntityConstant.DEPARTMENT_ENTITY_CODE, "Department entity code" +
                        " is missing in request data");
            } else if (departmentEntityDTO.getCode().length() < 1 || departmentEntityDTO.getCode().length() > 256) {
                errorMap.put(DepartmentEntityConstant.DEPARTMENT_ENTITY_CODE, "Department entity code " +
                        "length is invalid. Length range [2-256]");
            }

            if (StringUtils.isEmpty(departmentEntityDTO.getName())) {
                errorMap.put(DepartmentEntityConstant.DEPARTMENT_ENTITY_NAME, "Department entity name" +
                        " is missing in request data");
            } else if (departmentEntityDTO.getName().length() < 1 || departmentEntityDTO.getName().length() > 256) {
                errorMap.put(DepartmentEntityConstant.DEPARTMENT_ENTITY_NAME, "Department entity name " +
                        "length is invalid. Length range [2-256]");
            }

            if (departmentEntityDTO.getHierarchyLevel() == null) {
                errorMap.put(DepartmentEntityConstant.DEPARTMENT_HIERARCHY_ID, "Department hierarchy id" +
                        " is missing in request data");
            }

            if (departmentEntityDTO.getChildren() == null) {
                errorMap.put(DepartmentEntityConstant.DEPARTMENT_CHILDREN, "Department children information is missing");
            }else if (!departmentEntityDTO.getChildren().isEmpty()) {

                List<DepartmentEntity> departmentEntityList = departmentEntityRepository
                        .findByIdsAndHierarchyLevel(
                                dtoWrapper.getChildListFromDepatmentEntityChildren(departmentEntityDTO.getChildren()),
                                departmentEntityDTO.getHierarchyLevel() + 1);

                if (departmentEntityList == null || departmentEntityList.isEmpty()
                        || (departmentEntityList.size() != departmentEntityDTO.getChildren().size())) {
                    errorMap.put(DepartmentEntityConstant.DEPARTMENT_CHILDREN, "Invalid children id list");
                }
            }

            if (StringUtils.isNotBlank(departmentEntityDTO.getDepartmentId())
                    && departmentEntityDTO.getHierarchyLevel() != null
                    && StringUtils.isNotBlank(departmentEntityDTO.getTenantId())) {

                List<DepartmentHierarchyLevel> departmentHierarchyLevelList = hierarchyLevelRepository
                        .findByDepartmentIdAndTenantIdAndLevel(departmentEntityDTO.getDepartmentId(),
                                departmentEntityDTO.getTenantId(), departmentEntityDTO.getHierarchyLevel());

                if (departmentHierarchyLevelList == null || departmentHierarchyLevelList.isEmpty()) {
                    errorMap.put(DepartmentEntityConstant.INVALID_HIERARCHY_LEVEL, "Given Hierarchy level of this department id : "
                            + departmentEntityDTO.getDepartmentId() + " doesn't exist in the system.");
                }
            }
            if (!errorMap.isEmpty()) {
                throw new CustomException(errorMap);
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

    /**
     * @param departmentEntityRequest
     */
    public void validateUpdateDepartmentEntityRequest(DepartmentEntityRequest departmentEntityRequest) {
        Map<String, String> errorMap = new HashMap<>();

        if (departmentEntityRequest != null && departmentEntityRequest.getRequestHeader() != null
                && departmentEntityRequest.getDepartmentEntityDTO() != null) {

            DepartmentEntityDTO departmentEntityDTO = departmentEntityRequest.getDepartmentEntityDTO();

            if (StringUtils.isEmpty(departmentEntityDTO.getId())) {
                errorMap.put(DepartmentEntityConstant.ID, "Department Entity id is missing in request data");
            }

            if (StringUtils.isEmpty(departmentEntityDTO.getTenantId())) {
                errorMap.put(DepartmentEntityConstant.TENANT_ID, "Tenant id is missing in request data");
            } else if (departmentEntityDTO.getTenantId().length() < 2 || departmentEntityDTO.getTenantId().length() > 64) {
                errorMap.put(DepartmentEntityConstant.TENANT_ID, "Tenant id length is invalid. " +
                        "Length range [2-64]");
            }

            if (!StringUtils.isEmpty(departmentEntityDTO.getCode())) {
                if (departmentEntityDTO.getCode().length() < 1 || departmentEntityDTO.getCode().length() > 256) {
                    errorMap.put(DepartmentEntityConstant.DEPARTMENT_ENTITY_CODE, "Department entity code " +
                            "length is invalid. Length range [2-256]");
                }
            }

            if (!StringUtils.isEmpty(departmentEntityDTO.getName())) {
                if (departmentEntityDTO.getName().length() < 1 || departmentEntityDTO.getName().length() > 256) {
                    errorMap.put(DepartmentEntityConstant.DEPARTMENT_ENTITY_NAME, "Department entity name " +
                            "length is invalid. Length range [2-256]");
                }
            }

            if (departmentEntityDTO.getChildren() != null && !departmentEntityDTO.getChildren().isEmpty()) {
                if (departmentEntityDTO.getHierarchyLevel() == null) {
                    errorMap.put(DepartmentEntityConstant.DEPARTMENT_HIERARCHY_ID, "Hierarchy level value is required " +
                            "while child exist");
                } else {
                    List<DepartmentEntity> departmentEntityList = departmentEntityRepository
                            .findByIdsAndHierarchyLevel(
                                    dtoWrapper.getChildListFromDepatmentEntityChildren(departmentEntityDTO.getChildren()),
                                    departmentEntityDTO.getHierarchyLevel() + 1);

                    if (departmentEntityList == null || departmentEntityList.isEmpty()
                            || (departmentEntityList.size() != departmentEntityDTO.getChildren().size())) {
                        errorMap.put(DepartmentEntityConstant.DEPARTMENT_CHILDREN, "Invalid children id list");
                    }
                }
            }

            if (StringUtils.isNotBlank(departmentEntityDTO.getDepartmentId())) {
                if (departmentEntityDTO.getHierarchyLevel() == null) {
                    errorMap.put(DepartmentEntityConstant.DEPARTMENT_HIERARCHY_ID, "Hierarchy level value is required " +
                            "while updating department id");
                } else {
                    if (departmentEntityDTO.getHierarchyLevel() != null
                            && StringUtils.isNotBlank(departmentEntityDTO.getTenantId())) {
                        List<DepartmentHierarchyLevel> departmentHierarchyLevelList = hierarchyLevelRepository
                                .findByDepartmentIdAndTenantIdAndLevel(departmentEntityDTO.getDepartmentId(),
                                        departmentEntityDTO.getTenantId(), departmentEntityDTO.getHierarchyLevel());

                        if (departmentHierarchyLevelList == null || departmentHierarchyLevelList.isEmpty()) {
                            errorMap.put(DepartmentEntityConstant.INVALID_HIERARCHY_LEVEL, "Given Hierarchy level of " +
                                    "this department id : " + departmentEntityDTO.getDepartmentId()
                                    + " doesn't exist in the system.");
                        }
                    }
                }
            }
            if (!errorMap.isEmpty()) {
                throw new CustomException(errorMap);
            }
        } else {
            throw new CustomException(DepartmentEntityConstant.REQUEST_PAYLOAD_MISSING, "Request payload is missing some value");
        }
    }
}
