package org.egov.service;

import org.egov.repository.ProjectRepository;
import org.egov.validator.ProjectValidator;
import org.egov.web.models.Project;
import org.egov.web.models.ProjectSearchRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectService {

    @Autowired
    ProjectValidator projectValidator;

    @Autowired
    ProjectRepository projectRepository;

    public List<Project> findAllByCriteria(ProjectSearchRequest projectSearchRequest) {
        projectValidator.validateProjectSearchRequest(projectSearchRequest);

        return projectRepository.findAllByCriteria(projectSearchRequest.getCriteria());
    }
}
