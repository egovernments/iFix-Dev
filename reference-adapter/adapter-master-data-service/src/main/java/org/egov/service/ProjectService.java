package org.egov.service;

import org.egov.repository.ProjectRepository;
import org.egov.validator.ProjectValidator;
import org.egov.web.models.Project;
import org.egov.web.models.ProjectRequest;
import org.egov.web.models.ProjectSearchRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectService {

    @Autowired
    ProjectValidator projectValidator;

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    ProjectEnrichmentService projectEnrichmentService;

    /**
     * @param projectSearchRequest
     * @return
     */
    public List<Project> findAllByCriteria(ProjectSearchRequest projectSearchRequest) {
        projectValidator.validateProjectSearchRequest(projectSearchRequest);

        return projectRepository.findAllByCriteria(projectSearchRequest.getCriteria());
    }

    /**
     * @param projectRequest
     * @return
     */
    public ProjectRequest createProject(ProjectRequest projectRequest) {
        projectValidator.validateProjectCreateRequest(projectRequest);
        projectEnrichmentService.enrichCreateProjectData(projectRequest);
        projectRepository.save(projectRequest.getProject());

        return projectRequest;
    }

    public ProjectRequest updateProject(ProjectRequest projectRequest) {
        projectValidator.validateProjectUpdateRequest(projectRequest);
        Project updatedProject = projectEnrichmentService.enrichUpdateProjectData(projectRequest);
        if (updatedProject != null) {
            projectRepository.save(updatedProject);
        }
        BeanUtils.copyProperties(updatedProject,projectRequest.getProject());
        return projectRequest;
    }
}
