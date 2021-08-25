package org.egov.service;

import org.egov.common.contract.AuditDetails;
import org.egov.common.contract.request.RequestHeader;
import org.egov.util.MasterDataServiceUtil;
import org.egov.web.models.Government;
import org.egov.web.models.GovernmentRequest;
import org.egov.web.models.Project;
import org.egov.web.models.ProjectRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ProjectEnrichmentService {

    @Autowired
    MasterDataServiceUtil enrichAuditDetails;

    /**
     * @param projectRequest
     */
    public void enrichProjectData(ProjectRequest projectRequest) {
        Project project = projectRequest.getProject();
        RequestHeader requestHeader = projectRequest.getRequestHeader();

        AuditDetails auditDetails = null;

        if(project.getAuditDetails() == null){
            auditDetails = enrichAuditDetails
                    .enrichAuditDetails(requestHeader.getUserInfo().getUuid(), project.getAuditDetails(), true);
        }else{
            auditDetails = enrichAuditDetails
                    .enrichAuditDetails(requestHeader.getUserInfo().getUuid(), project.getAuditDetails(), false);
        }

        project.setId(UUID.randomUUID().toString());
        project.setAuditDetails(auditDetails);
    }
}
