package org.egov.service;

import org.egov.common.contract.AuditDetails;
import org.egov.common.contract.request.RequestHeader;
import org.egov.util.DepartmentEntityUtil;
import org.egov.web.models.DepartmentEntityDTO;
import org.egov.web.models.DepartmentEntityRequest;
import org.egov.web.models.persist.DepartmentEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class DepartmentEntityEnrichmentService {
    @Autowired
    DepartmentEntityUtil enrichAuditDetails;

    /**
     * @param departmentEntityRequest
     */
    public void enrichDepartmentEntityData(DepartmentEntityRequest departmentEntityRequest) {
        DepartmentEntityDTO departmentEntityDTO = departmentEntityRequest.getDepartmentEntityDTO();
        RequestHeader requestHeader = departmentEntityRequest.getRequestHeader();

        AuditDetails auditDetails = null;
        String userUUID = requestHeader.getUserInfo() != null ? requestHeader.getUserInfo().getUuid() : null;

        if (departmentEntityDTO.getAuditDetails() == null) {
            auditDetails = enrichAuditDetails.enrichAuditDetails(userUUID, departmentEntityDTO.getAuditDetails(), true);
        } else {
            auditDetails = enrichAuditDetails.enrichAuditDetails(userUUID, departmentEntityDTO.getAuditDetails(), false);
        }

        departmentEntityDTO.setId(UUID.randomUUID().toString());
        departmentEntityDTO.setAuditDetails(auditDetails);
    }

    /**
     * @param departmentEntityRequest 
     * @return
     */
    public DepartmentEntity getEnrichedDepartmentEntityData(DepartmentEntityRequest departmentEntityRequest) {
        enrichDepartmentEntityData(departmentEntityRequest);
        DepartmentEntityDTO departmentEntityDTO = departmentEntityRequest.getDepartmentEntityDTO();

        return DepartmentEntity.builder()
                .id(departmentEntityDTO.getId())
                .tenantId(departmentEntityDTO.getTenantId())
                .departmentId(departmentEntityDTO.getDepartmentId())
                .code(departmentEntityDTO.getCode())
                .name(departmentEntityDTO.getName())
                .hierarchyLevel(departmentEntityDTO.getHierarchyLevel())
                .createdBy(departmentEntityDTO.getAuditDetails().getCreatedBy())
                .lastModifiedBy(departmentEntityDTO.getAuditDetails().getLastModifiedBy())
                .createdTime(departmentEntityDTO.getAuditDetails().getCreatedTime())
                .lastModifiedTime(departmentEntityDTO.getAuditDetails().getLastModifiedTime())
                .children(String.join(",", departmentEntityDTO.getChildren()))
                .build();


    }
}
