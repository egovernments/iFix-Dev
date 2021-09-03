package org.egov.service;


import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.AuditDetails;
import org.egov.common.contract.request.RequestHeader;
import org.egov.util.MasterDataServiceUtil;
import org.egov.web.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class DepartmentEnrichmentService {

    @Autowired
    MasterDataServiceUtil enrichAuditDetails;

    /**
     *  Enrich the department search request
     * @param searchRequest
     */
    public void enrichSearchPost(DepartmentSearchRequest searchRequest) {
        DepartmentSearchCriteria searchCriteria = searchRequest.getCriteria();
        //TODO- fill if any default search criteria

    }

    /**
     * @param departmentRequest
     */
    public void enrichDepartmentData(DepartmentRequest departmentRequest) {
        Department department = departmentRequest.getDepartment();
        RequestHeader requestHeader = departmentRequest.getRequestHeader();

        AuditDetails auditDetails = null;

        if(department.getAuditDetails() == null){
            auditDetails = enrichAuditDetails.enrichAuditDetails(requestHeader.getUserInfo().getUuid(), department.getAuditDetails(), true);
        }else{
            auditDetails = enrichAuditDetails.enrichAuditDetails(requestHeader.getUserInfo().getUuid(), department.getAuditDetails(), false);
        }

        department.setId(UUID.randomUUID().toString());
        department.setAuditDetails(auditDetails);
    }
}
