package org.egov.service;

import org.apache.commons.lang3.StringUtils;
import org.egov.common.contract.AuditDetails;
import org.egov.common.contract.request.RequestHeader;
import org.egov.repository.ProjectRepository;
import org.egov.util.MasterDataServiceUtil;
import org.egov.web.models.Project;
import org.egov.web.models.ProjectRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class ProjectEnrichmentService {

    @Autowired
    MasterDataServiceUtil enrichAuditDetails;
    @Autowired
    private ProjectDepartmentEntityIntegration projectDepartmentEntityIntegration;

    @Autowired
    private ProjectRepository projectRepository;

    /**
     * @param projectRequest
     */
    public void enrichCreateProjectData(ProjectRequest projectRequest) {
        Project project = projectRequest.getProject();
        RequestHeader requestHeader = projectRequest.getRequestHeader();

        AuditDetails auditDetails = null;

        String userId = (requestHeader.getUserInfo() != null ? requestHeader.getUserInfo().getUuid() : null);
        if (project.getAuditDetails() == null) {
            auditDetails = enrichAuditDetails
                    .enrichAuditDetails(userId, project.getAuditDetails(), true);
        } else {
            auditDetails = enrichAuditDetails
                    .enrichAuditDetails(userId, project.getAuditDetails(), false);
        }

        project.setId(UUID.randomUUID().toString());
        project.setAuditDetails(auditDetails);
    }

    public Project enrichUpdateProjectData(ProjectRequest projectRequest) {
        Project reqProject = projectRequest.getProject();
        RequestHeader requestHeader = projectRequest.getRequestHeader();

        Optional<Project> optionalExistingProject = projectRepository.findByProjectId(reqProject.getId());

        if (optionalExistingProject.isPresent()) {
            Project existingProject = optionalExistingProject.get();
            boolean isModified = false;
            if (StringUtils.isNotBlank(reqProject.getCode())) {
                existingProject.setCode(reqProject.getCode());
                isModified = true;
            }
            if (StringUtils.isNotBlank(reqProject.getName())) {
                existingProject.setName(reqProject.getName());
                isModified = true;
            }
            if (StringUtils.isNotBlank(reqProject.getExpenditureId())) {
                existingProject.setExpenditureId(reqProject.getExpenditureId());
                isModified = true;
            }
            if (reqProject.getDepartmentEntityIds() != null && !reqProject.getDepartmentEntityIds().isEmpty()) {
                existingProject.setDepartmentEntityIds(reqProject.getDepartmentEntityIds());
                isModified = true;
            }
            if (isModified) {
                AuditDetails existingAuditDetails = existingProject.getAuditDetails();
                existingAuditDetails.setLastModifiedTime(System.currentTimeMillis());
                existingAuditDetails.setLastModifiedBy(
                        requestHeader.getUserInfo() != null && StringUtils.isNotBlank(requestHeader.getUserInfo().getUuid())
                                ? requestHeader.getUserInfo().getUuid()
                                : existingAuditDetails.getLastModifiedBy()
                );

                existingProject.setAuditDetails(existingAuditDetails);
            }

            return existingProject;
        }
        return null;
    }
}
