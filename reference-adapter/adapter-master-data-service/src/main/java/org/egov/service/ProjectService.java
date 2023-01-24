package org.egov.service;

import org.apache.commons.lang3.StringUtils;
import org.egov.config.MasterDataServiceConfiguration;
import org.egov.repository.ProjectDepartmentEntityRelationRepository;
import org.egov.repository.ProjectRepository;
import org.egov.util.DtoWrapper;
import org.egov.util.KafkaProducer;
import org.egov.validator.ProjectValidator;
import org.egov.web.models.ProjectDTO;
import org.egov.web.models.ProjectDepartmentEntityRelationshipDTO;
import org.egov.web.models.ProjectRequest;
import org.egov.web.models.ProjectSearchRequest;
import org.egov.web.models.persist.Project;
import org.egov.web.models.persist.ProjectDepartmentEntityRelationship;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectService {

    @Autowired
    ProjectValidator projectValidator;

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    ProjectEnrichmentService projectEnrichmentService;

    @Autowired
    private KafkaProducer kafkaProducer;

    @Autowired
    private MasterDataServiceConfiguration masterDataServiceConfiguration;

    @Autowired
    private DtoWrapper dtoWrapper;

    @Autowired
    private ProjectDepartmentEntityRelationRepository projectDepartmentEntityRelationRepository;

    /**
     * @param projectSearchRequest
     * @return
     */
    public List<ProjectDTO> findAllByCriteria(ProjectSearchRequest projectSearchRequest) {
        projectValidator.validateProjectSearchRequest(projectSearchRequest);

        List<Project> projectList = projectRepository.findAllByCriteria(projectSearchRequest.getCriteria());

        return dtoWrapper.getProjectDTOListWithRelationship(projectList);
    }

    /**
     * @param projectRequest
     * @return
     */
    public ProjectRequest createProject(ProjectRequest projectRequest) {
        projectValidator.validateProjectCreateRequest(projectRequest);
        projectEnrichmentService.enrichCreateProjectData(projectRequest);

        kafkaProducer.push(masterDataServiceConfiguration.getPersisterKafkaProjectCreateTopic(), projectRequest);

        return projectRequest;
    }

    public ProjectRequest updateProject(ProjectRequest projectRequest) {
        projectValidator.validateProjectUpdateRequest(projectRequest);

        ProjectDTO updatedProjectDTO = projectEnrichmentService.enrichUpdateProjectData(projectRequest);

        projectRequest.setProjectDTO(updatedProjectDTO);

        return projectRequest;
    }




    public void setDepartmentIdIntoProject(ProjectDTO projectDTO) {
        if (projectDTO != null && !StringUtils.isEmpty(projectDTO.getId())) {
            List<ProjectDepartmentEntityRelationship> departmentEntityRelationshipList =
                    projectDepartmentEntityRelationRepository.findByProjectId(projectDTO.getId());

            if (departmentEntityRelationshipList != null && !departmentEntityRelationshipList.isEmpty()) {
                List<ProjectDepartmentEntityRelationshipDTO> departmentEntityRelationshipDTOList =
                        departmentEntityRelationshipList.stream()
                                .map(projectDepartmentEntityRelationship ->
                                        ProjectDepartmentEntityRelationshipDTO.builder()
                                                .departmentEntityId(projectDepartmentEntityRelationship.getDepartmentEntityId())
                                                .status(projectDepartmentEntityRelationship.getStatus())
                                                .build()
                                )
                                .collect(Collectors.toList());

                projectDTO.setDepartmentEntityIds(departmentEntityRelationshipDTOList);
            }
        }
    }
}
