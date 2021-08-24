package org.egov.service;


import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.AuditDetails;
import org.egov.common.contract.request.RequestHeader;
import org.egov.util.DepartmentEntityUtil;
import org.egov.web.models.DepartmentHierarchyLevel;
import org.egov.web.models.DepartmentHierarchyLevelRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class DepartmentHierarchyLevelEnrichmentService {

    @Autowired
    private DepartmentEntityUtil departmentEntityUtil;

    /**
     * Enrich the Department hierarchy level request
     * @param request
     */
    public void enrichHierarchyLevelCreatePost(DepartmentHierarchyLevelRequest request) {
        RequestHeader requestHeader = request.getRequestHeader();
        DepartmentHierarchyLevel departmentHierarchyLevel = request.getDepartmentHierarchyLevel();

        AuditDetails auditDetails = null;
        if (departmentHierarchyLevel.getAuditDetails() == null) {
            auditDetails = departmentEntityUtil.enrichAuditDetails(requestHeader.getUserInfo().getUuid(), departmentHierarchyLevel.getAuditDetails(), true);
        } else {
            auditDetails = departmentEntityUtil.enrichAuditDetails(requestHeader.getUserInfo().getUuid(), departmentHierarchyLevel.getAuditDetails(), false);
        }

        departmentHierarchyLevel.setId(UUID.randomUUID().toString());
        departmentHierarchyLevel.setAuditDetails(auditDetails);
    }
}
