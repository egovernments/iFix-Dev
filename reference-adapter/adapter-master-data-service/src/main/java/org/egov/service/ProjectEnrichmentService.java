package org.egov.service;

import org.apache.commons.lang3.StringUtils;
import org.egov.common.contract.AuditDetails;
import org.egov.common.contract.request.RequestHeader;
import org.egov.config.MasterDataServiceConfiguration;
import org.egov.repository.ProjectDepartmentEntityRelationRepository;
import org.egov.repository.ProjectRepository;
import org.egov.util.DtoWrapper;
import org.egov.util.KafkaProducer;
import org.egov.util.MasterDataServiceUtil;
import org.egov.web.models.ProjectDTO;
import org.egov.web.models.ProjectDepartmentEntityRelationshipDTO;
import org.egov.web.models.ProjectRequest;
import org.egov.web.models.persist.Project;
import org.egov.web.models.persist.ProjectDepartmentEntityRelationship;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class ProjectEnrichmentService {

    @Autowired
    MasterDataServiceUtil enrichAuditDetails;
    @Autowired
    private ProjectDepartmentEntityIntegration projectDepartmentEntityIntegration;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private DtoWrapper dtoWrapper;

    @Autowired
    private KafkaProducer kafkaProducer;

    @Autowired
    private MasterDataServiceConfiguration masterDataServiceConfiguration;

    /**
     * @param projectRequest
     */
    public void enrichCreateProjectData(ProjectRequest projectRequest) {
        ProjectDTO projectDTO = projectRequest.getProjectDTO();
        RequestHeader requestHeader = projectRequest.getRequestHeader();

        AuditDetails auditDetails = null;

        String userId = (requestHeader.getUserInfo() != null ? requestHeader.getUserInfo().getUuid() : null);
        if (projectDTO.getAuditDetails() == null) {
            auditDetails = enrichAuditDetails
                    .enrichAuditDetails(userId, projectDTO.getAuditDetails(), true);
        } else {
            auditDetails = enrichAuditDetails
                    .enrichAuditDetails(userId, projectDTO.getAuditDetails(), false);
        }

        projectDTO.setId(UUID.randomUUID().toString());
        projectDTO.setAuditDetails(auditDetails);
    }

    /**
     * @param projectRequest
     * @return
     */
    public @NonNull ProjectDTO enrichUpdateProjectData(ProjectRequest projectRequest) {
        ProjectDTO projectDTO = projectRequest.getProjectDTO();
        RequestHeader requestHeader = projectRequest.getRequestHeader();

        Optional<Project> optionalExistingProject = projectRepository.findByProjectId(projectDTO.getId());

        if (optionalExistingProject.isPresent()) {
            Project existingProject = optionalExistingProject.get();
            boolean isModified = false;

            if (StringUtils.isNotBlank(projectDTO.getCode())) {
                existingProject.setCode(projectDTO.getCode());
                isModified = true;
            }
            if (StringUtils.isNotBlank(projectDTO.getName())) {
                existingProject.setName(projectDTO.getName());
                isModified = true;
            }
            if (StringUtils.isNotBlank(projectDTO.getExpenditureId())) {
                existingProject.setExpenditureId(projectDTO.getExpenditureId());
                isModified = true;
            }

            if (isModified) {
                String userUUID = projectRequest.getRequestHeader().getUserInfo() != null
                        ? projectRequest.getRequestHeader().getUserInfo().getUuid()
                        : null;

                existingProject.setLastModifiedBy(userUUID);
                existingProject.setLastModifiedTime(new Date().getTime());

                copyProjectToProjectDTO(existingProject, projectRequest.getProjectDTO());

                kafkaProducer.push(masterDataServiceConfiguration.getPersisterKafkaProjectUpdateTopic(), projectRequest);
            }

            projectDTO =  dtoWrapper.wrapProjectIntoProjectDTO(existingProject);
        }

        return projectDTO;
    }


    /**
     * @param project
     * @param projectDTO
     */
    private void copyProjectToProjectDTO(@NonNull Project project, @NonNull ProjectDTO projectDTO) {
        projectDTO.setId(project.getId());
        projectDTO.setTenantId(project.getTenantId());
        projectDTO.setCode(project.getCode());
        projectDTO.setName(project.getName());
        projectDTO.setExpenditureId(project.getExpenditureId());
        projectDTO.setAuditDetails(
                AuditDetails.builder()
                        .createdBy(project.getCreatedBy())
                        .lastModifiedBy(project.getLastModifiedBy())
                        .createdTime(project.getCreatedTime())
                        .lastModifiedTime(project.getLastModifiedTime())
                        .build()
        );
    }
}
