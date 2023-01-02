package org.egov.service;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.egov.common.contract.AuditDetails;
import org.egov.common.contract.request.RequestHeader;
import org.egov.repository.DepartmentHierarchyLevelRepository;
import org.egov.tracer.model.CustomException;
import org.egov.util.DepartmentEntityUtil;
import org.egov.web.models.DepartmentHierarchyLevelDTO;
import org.egov.web.models.DepartmentHierarchyLevelRequest;
import org.egov.web.models.DepartmentHierarchyLevelSearchCriteria;
import org.egov.web.models.persist.DepartmentHierarchyLevel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class DepartmentHierarchyLevelEnrichmentService {

    @Autowired
    private DepartmentEntityUtil departmentEntityUtil;

    @Autowired
    private DepartmentHierarchyLevelRepository hierarchyLevelRepository;

    /**
     * Enrich the Department hierarchy level request
     *
     * @param request
     */
    public void enrichHierarchyLevelCreatePost(DepartmentHierarchyLevelRequest request) {
        RequestHeader requestHeader = request.getRequestHeader();
        DepartmentHierarchyLevelDTO departmentHierarchyLevelDTO = request.getDepartmentHierarchyLevelDTO();

        AuditDetails auditDetails = null;
        if (departmentHierarchyLevelDTO.getAuditDetails() == null) {
            auditDetails = departmentEntityUtil.enrichAuditDetails(requestHeader.getUserInfo().getUuid(), departmentHierarchyLevelDTO.getAuditDetails(), true);
        } else {
            auditDetails = departmentEntityUtil.enrichAuditDetails(requestHeader.getUserInfo().getUuid(), departmentHierarchyLevelDTO.getAuditDetails(), false);
        }

        if(StringUtils.isBlank(departmentHierarchyLevelDTO.getParent())){
            departmentHierarchyLevelDTO.setLevel(0);
            departmentHierarchyLevelDTO.setParent(null);
        }
        enrichDepartmentLevel(departmentHierarchyLevelDTO);

        departmentHierarchyLevelDTO.setId(UUID.randomUUID().toString());
        departmentHierarchyLevelDTO.setAuditDetails(auditDetails);
    }

    private void enrichDepartmentLevel(DepartmentHierarchyLevelDTO departmentHierarchyLevelDTO) {
        if (StringUtils.isNotBlank(departmentHierarchyLevelDTO.getParent()) && StringUtils.isNotBlank(departmentHierarchyLevelDTO.getTenantId())) {
            DepartmentHierarchyLevelSearchCriteria searchCriteria = new DepartmentHierarchyLevelSearchCriteria();
            searchCriteria.setIds(Collections.singletonList(departmentHierarchyLevelDTO.getParent()));
            searchCriteria.setTenantId(departmentHierarchyLevelDTO.getTenantId());

            setDepartmentEntityLevel(departmentHierarchyLevelDTO);
        }
    }


    /**
     * @param request
     * @return
     */
    public DepartmentHierarchyLevel getDepartmentHierarchyLevelData(DepartmentHierarchyLevelRequest request) {
        DepartmentHierarchyLevelDTO hierarchyLevelDTO = request.getDepartmentHierarchyLevelDTO();

        RequestHeader requestHeader = request.getRequestHeader();
        String userUUID = requestHeader.getUserInfo() != null ? requestHeader.getUserInfo().getUuid() : null;

        DepartmentHierarchyLevelDTO departmentHierarchyLevelDTO = request.getDepartmentHierarchyLevelDTO();

        setDepartmentEntityLevel(departmentHierarchyLevelDTO);

        return DepartmentHierarchyLevel.builder()
                .id(UUID.randomUUID().toString())
                .tenantId(hierarchyLevelDTO.getTenantId())
                .departmentId(hierarchyLevelDTO.getDepartmentId())
                .label(hierarchyLevelDTO.getLabel())
                .parent(hierarchyLevelDTO.getParent())
                .level(hierarchyLevelDTO.getLevel())
                .createdBy(userUUID)
                .lastModifiedBy(userUUID)
                .createdTime(System.currentTimeMillis())
                .lastModifiedTime(System.currentTimeMillis())
                .build();

    }

    /**
     * @param hierarchyLevelDTO
     */
    private void setDepartmentEntityLevel(DepartmentHierarchyLevelDTO hierarchyLevelDTO) {
        if (StringUtils.isNotBlank(hierarchyLevelDTO.getParent())
                && StringUtils.isNotBlank(hierarchyLevelDTO.getTenantId())) {

            Optional<DepartmentHierarchyLevel> departmentHierarchyLevelOptional = hierarchyLevelRepository
                    .findByIdAndTenantId(hierarchyLevelDTO.getParent(), hierarchyLevelDTO.getTenantId());

            if (!departmentHierarchyLevelOptional.isPresent()) {
                throw new CustomException("INVALID_PARENT_ID", "Given parent doesn't exist in the system");
            }else {
                hierarchyLevelDTO.setLevel(departmentHierarchyLevelOptional.get().getLevel() + 1);
            }
        }
    }

}
