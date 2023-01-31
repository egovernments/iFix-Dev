package org.egov.service;

import org.egov.config.TestDataFormatter;
import org.egov.repository.ProjectRepository;
import org.egov.validator.ProjectValidator;
import org.egov.web.models.*;
import org.egov.web.models.ProjectDTO;
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
   /* @Autowired
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
        List<ProjectDTO> projectDTOList = projectResponse.getProjectDTO();

        doNothing().when(this.projectValidator).validateProjectSearchRequest((projectSearchRequest));
        when(this.projectRepository.findAllByCriteria(projectSearchRequest.getCriteria())).thenReturn(projectDTOList);

        List<ProjectDTO> actualFindAllByCriteriaResult = projectService.findAllByCriteria(projectSearchRequest);

        assertSame(projectDTOList, actualFindAllByCriteriaResult);
        assertNotNull(actualFindAllByCriteriaResult);

        verify(this.projectValidator).validateProjectSearchRequest(projectSearchRequest);
        verify(this.projectRepository).findAllByCriteria(projectSearchRequest.getCriteria());
    }

    @Test
    void testFindAllByCriteriaWithBlankSearchRequestData() {
        doNothing().when(this.projectValidator).validateProjectSearchRequest((ProjectSearchRequest) any());
        ArrayList<ProjectDTO> projectDTOList = new ArrayList();
        when(this.projectRepository.findAllByCriteria((ProjectSearchCriteria) any())).thenReturn(projectDTOList);
        List<ProjectDTO> actualFindAllByCriteriaResult = this.projectService.findAllByCriteria(new ProjectSearchRequest());

        assertSame(projectDTOList, actualFindAllByCriteriaResult);
        assertTrue(actualFindAllByCriteriaResult.isEmpty());
        verify(this.projectValidator).validateProjectSearchRequest((ProjectSearchRequest) any());
        verify(this.projectRepository).findAllByCriteria((ProjectSearchCriteria) any());
    }

    @Test
    void testCreateProject() {
        doNothing().when(this.projectValidator).validateProjectCreateRequest((ProjectRequest) any());
        doNothing().when(this.projectRepository).save((ProjectDTO) any());
        doNothing().when(this.projectEnrichmentService).enrichCreateProjectData((ProjectRequest) any());

        ProjectRequest projectRequest = new ProjectRequest();
        assertSame(projectRequest, this.projectService.createProject(projectRequest));
        verify(this.projectValidator).validateProjectCreateRequest((ProjectRequest) any());
        verify(this.projectRepository).save((ProjectDTO) any());
        verify(this.projectEnrichmentService).enrichCreateProjectData((ProjectRequest) any());
    }

    @Test
    void testUpdateProjectWithDefaultRequest() {
        doNothing().when(this.projectValidator).validateProjectUpdateRequest((ProjectRequest) any());
        doNothing().when(this.projectRepository).save((ProjectDTO) any());
        when(this.projectEnrichmentService.enrichUpdateProjectData((ProjectRequest) any())).thenReturn(new ProjectDTO());
        ProjectRequest projectRequest = new ProjectRequest();
        projectRequest.setProjectDTO(new ProjectDTO());
        assertSame(projectRequest, this.projectService.updateProject(projectRequest));
        verify(this.projectValidator).validateProjectUpdateRequest((ProjectRequest) any());
        verify(this.projectRepository).save((ProjectDTO) any());
        verify(this.projectEnrichmentService).enrichUpdateProjectData((ProjectRequest) any());
    }

    @Test
    void testUpdateProjectWithUpdateRequest() {
        doNothing().when(this.projectValidator).validateProjectUpdateRequest((ProjectRequest) any());
        doNothing().when(this.projectRepository).save((ProjectDTO) any());
        when(this.projectEnrichmentService.enrichUpdateProjectData((ProjectRequest) any())).thenReturn(projectUpdateResquest.getProjectDTO());
        assertNotNull(this.projectService.updateProject(projectUpdateResquest));
        verify(this.projectValidator).validateProjectUpdateRequest((ProjectRequest) any());
        verify(this.projectEnrichmentService).enrichUpdateProjectData((ProjectRequest) any());
    }*/
}

