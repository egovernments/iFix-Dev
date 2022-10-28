package org.egov.service;

import org.egov.config.TestDataFormatter;
import org.egov.repository.ProjectRepository;
import org.egov.validator.ProjectValidator;
import org.egov.web.models.*;
import org.egov.web.models.Project;
import org.egov.web.models.ProjectRequest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
class ProjectServiceTest {
    @Autowired
    private TestDataFormatter testDataFormatter;

    @Mock
    private ProjectEnrichmentService projectEnrichmentService;

    @Mock
    private ProjectRepository projectRepository;

    @InjectMocks
    private ProjectService projectService;

    @Mock
    private ProjectValidator projectValidator;

    private ProjectSearchRequest projectSearchRequest;
    private ProjectResponse projectResponse;
    private ProjectRequest projectUpdateResquest;

    @BeforeAll
    public void init() throws IOException {
        projectSearchRequest = testDataFormatter.getProjectSearchRequestData();
        projectResponse = testDataFormatter.getProjectSearchReponseData();
        projectUpdateResquest = testDataFormatter.getProjectUpdateRequestData();
    }

    @Test
    void testFindAllByCriteria() {
        List<Project> projectList = projectResponse.getProject();

        doNothing().when(this.projectValidator).validateProjectSearchRequest((projectSearchRequest));
        when(this.projectRepository.findAllByCriteria(projectSearchRequest.getCriteria())).thenReturn(projectList);

        List<Project> actualFindAllByCriteriaResult = projectService.findAllByCriteria(projectSearchRequest);

        assertSame(projectList, actualFindAllByCriteriaResult);
        assertNotNull(actualFindAllByCriteriaResult);

        verify(this.projectValidator).validateProjectSearchRequest(projectSearchRequest);
        verify(this.projectRepository).findAllByCriteria(projectSearchRequest.getCriteria());
    }

    @Test
    void testFindAllByCriteriaWithBlankSearchRequestData() {
        doNothing().when(this.projectValidator).validateProjectSearchRequest((ProjectSearchRequest) any());
        ArrayList<Project> projectList = new ArrayList();
        when(this.projectRepository.findAllByCriteria((ProjectSearchCriteria) any())).thenReturn(projectList);
        List<Project> actualFindAllByCriteriaResult = this.projectService.findAllByCriteria(new ProjectSearchRequest());

        assertSame(projectList, actualFindAllByCriteriaResult);
        assertTrue(actualFindAllByCriteriaResult.isEmpty());
        verify(this.projectValidator).validateProjectSearchRequest((ProjectSearchRequest) any());
        verify(this.projectRepository).findAllByCriteria((ProjectSearchCriteria) any());
    }

    @Test
    void testCreateProject() {
        doNothing().when(this.projectValidator).validateProjectCreateRequest((ProjectRequest) any());
        doNothing().when(this.projectRepository).save((Project) any());
        doNothing().when(this.projectEnrichmentService).enrichCreateProjectData((ProjectRequest) any());

        ProjectRequest projectRequest = new ProjectRequest();
        assertSame(projectRequest, this.projectService.createProject(projectRequest));
        verify(this.projectValidator).validateProjectCreateRequest((ProjectRequest) any());
        verify(this.projectRepository).save((Project) any());
        verify(this.projectEnrichmentService).enrichCreateProjectData((ProjectRequest) any());
    }

    @Test
    void testUpdateProjectWithDefaultRequest() {
        doNothing().when(this.projectValidator).validateProjectUpdateRequest((ProjectRequest) any());
        doNothing().when(this.projectRepository).save((Project) any());
        when(this.projectEnrichmentService.enrichUpdateProjectData((ProjectRequest) any())).thenReturn(new Project());
        ProjectRequest projectRequest = new ProjectRequest();
        projectRequest.setProject(new Project());
        assertSame(projectRequest, this.projectService.updateProject(projectRequest));
        verify(this.projectValidator).validateProjectUpdateRequest((ProjectRequest) any());
        verify(this.projectRepository).save((Project) any());
        verify(this.projectEnrichmentService).enrichUpdateProjectData((ProjectRequest) any());
    }

    @Test
    void testUpdateProjectWithUpdateRequest() {
        doNothing().when(this.projectValidator).validateProjectUpdateRequest((ProjectRequest) any());
        doNothing().when(this.projectRepository).save((Project) any());
        when(this.projectEnrichmentService.enrichUpdateProjectData((ProjectRequest) any())).thenReturn(projectUpdateResquest.getProject());
        assertNotNull(this.projectService.updateProject(projectUpdateResquest));
        verify(this.projectValidator).validateProjectUpdateRequest((ProjectRequest) any());
        verify(this.projectEnrichmentService).enrichUpdateProjectData((ProjectRequest) any());
    }
}

