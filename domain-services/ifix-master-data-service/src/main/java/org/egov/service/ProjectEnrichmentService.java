package org.egov.service;

import org.egov.common.contract.AuditDetails;
import org.egov.common.contract.request.RequestHeader;
import org.egov.util.MasterDataServiceUtil;
import org.egov.web.models.DepartmentEntity;
import org.egov.web.models.Project;
import org.egov.web.models.ProjectRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ProjectEnrichmentService {

    @Autowired
    MasterDataServiceUtil enrichAuditDetails;
    @Autowired
    private ProjectDepartmentEntityIntegration projectDepartmentEntityIntegration;

    /**
     * @param projectRequest
     */
    public void enrichProjectData(ProjectRequest projectRequest) {
        Project project = projectRequest.getProject();
        addDepartmentEntityDetails(projectRequest);
        RequestHeader requestHeader = projectRequest.getRequestHeader();

        AuditDetails auditDetails = null;

        if (project.getAuditDetails() == null) {
            auditDetails = enrichAuditDetails
                    .enrichAuditDetails(requestHeader.getUserInfo().getUuid(), project.getAuditDetails(), true);
        } else {
            auditDetails = enrichAuditDetails
                    .enrichAuditDetails(requestHeader.getUserInfo().getUuid(), project.getAuditDetails(), false);
        }

        project.setId(UUID.randomUUID().toString());
        project.setAuditDetails(auditDetails);
    }

    private void addDepartmentEntityDetails(ProjectRequest projectRequest) {
        Project project = projectRequest.getProject();
        DepartmentEntity departmentEntity =
                projectDepartmentEntityIntegration.getDepartmentEntityForId(projectRequest.getRequestHeader(),
                        project.getTenantId(), project.getDepartmentEntitytId());
        project.setDepartmentEntity(departmentEntity);
    }

}
