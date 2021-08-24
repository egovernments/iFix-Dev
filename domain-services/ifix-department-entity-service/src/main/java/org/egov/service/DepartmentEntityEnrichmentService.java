package org.egov.service;

import org.egov.common.contract.AuditDetails;
import org.egov.common.contract.request.RequestHeader;
import org.egov.util.DepartmentEntityUtil;
import org.egov.web.models.DepartmentEntity;
import org.egov.web.models.DepartmentEntityRequest;
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
        DepartmentEntity departmentEntity = departmentEntityRequest.getDepartmentEntity();
        RequestHeader requestHeader = departmentEntityRequest.getRequestHeader();

        AuditDetails auditDetails = null;

        if(departmentEntity.getAuditDetails() == null){
            auditDetails = enrichAuditDetails.enrichAuditDetails(requestHeader.getUserInfo().getUuid(), departmentEntity.getAuditDetails(), true);
        }else{
            auditDetails = enrichAuditDetails.enrichAuditDetails(requestHeader.getUserInfo().getUuid(), departmentEntity.getAuditDetails(), false);
        }

        departmentEntity.setId(UUID.randomUUID().toString());
        departmentEntity.setAuditDetails(auditDetails);
    }
}
