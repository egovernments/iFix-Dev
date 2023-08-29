package org.egov.service;

import org.egov.common.contract.AuditDetails;
import org.egov.common.contract.request.RequestHeader;
import org.egov.config.TestDataFormatter;
import org.egov.repository.ProjectRepository;
import org.egov.util.MasterDataServiceUtil;
import org.egov.web.models.DepartmentEntity;
import org.egov.web.models.ProjectDTO;
import org.egov.web.models.ProjectRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
//@SpringBootTest
class ProjectEnrichmentServiceTest {
    /*@MockBean
    private ProjectRepository projectRepository;

    @Autowired
    private TestDataFormatter testDataFormatter;

    @Mock
    private MasterDataServiceUtil masterDataServiceUtil;

    @Mock
    private ProjectDepartmentEntityIntegration projectDepartmentEntityIntegration;

    @InjectMocks
    private ProjectEnrichmentService projectEnrichmentService;

    private ProjectRequest projectRequest;

    private ProjectRequest projectUpdateRequest;
    private AuditDetails auditDetails;
    private String userId;

    @BeforeEach
    public void init() throws IOException {
        projectRequest = testDataFormatter.getProjectCreateRequestData();

        ProjectDTO projectDTO = projectRequest.getProjectDTO();
        RequestHeader requestHeader = projectRequest.getRequestHeader();
        Long time = System.currentTimeMillis();
        userId = requestHeader.getUserInfo().getUuid();

        auditDetails = new AuditDetails(userId, userId, time, time);

        projectUpdateRequest = testDataFormatter.getProjectUpdateRequestData();
    }

    @Test
    void testEnrichProjectDataWithAuditDetails() {
        projectRequest.getProjectDTO().setAuditDetails(auditDetails);

        when(this.projectDepartmentEntityIntegration.getDepartmentEntityForIds((RequestHeader) any(), (String) any(),
                (List<String>) any())).thenReturn(Collections.singletonList(new DepartmentEntity()));
        doReturn(auditDetails).when(masterDataServiceUtil).enrichAuditDetails(userId, auditDetails, false);

        projectEnrichmentService.enrichCreateProjectData(projectRequest);

        assertNotNull(projectRequest.getProjectDTO().getAuditDetails());

        verify(this.projectDepartmentEntityIntegration, atLeast(0)).getDepartmentEntityForIds((RequestHeader) any(),
                (String) any(), (List<String>) any());
        verify(masterDataServiceUtil).enrichAuditDetails(userId, auditDetails, false);

    }

    @Test
    void testEnrichProjectDataWithoutAuditDetails() {
        when(this.projectDepartmentEntityIntegration.getDepartmentEntityForIds((RequestHeader) any(), (String) any(),
                (List<String>) any())).thenReturn(Collections.singletonList(new DepartmentEntity()));
        doReturn(auditDetails).when(masterDataServiceUtil).enrichAuditDetails(userId, projectRequest.getProjectDTO().getAuditDetails(), true);

        projectEnrichmentService.enrichCreateProjectData(projectRequest);

        assertNotNull(projectRequest.getProjectDTO().getAuditDetails());

        verify(this.projectDepartmentEntityIntegration, atLeast(0)).getDepartmentEntityForIds((RequestHeader) any(),
                (String) any(), (List<String>) any());
        verify(masterDataServiceUtil).enrichAuditDetails(userId, null, true);

    }

    @Test
    void testEnrichUpdateProjectDataWithDefaultValues() {
        ProjectDTO projectDTO = new ProjectDTO();
        when(this.projectRepository.findByProjectId((String) any())).thenReturn(Optional.of(projectDTO));

        ProjectRequest projectRequest = new ProjectRequest();
        projectRequest.setProjectDTO(new ProjectDTO());
        assertSame(projectDTO, this.projectEnrichmentService.enrichUpdateProjectData(projectRequest));
        verify(this.projectRepository).findByProjectId((String) any());
    }

    @Test
    void testEnrichUpdateProjectDataWithEmptySearchResult() {
        when(this.projectRepository.findByProjectId((String) any())).thenReturn(Optional.empty());

        ProjectRequest projectRequest = new ProjectRequest();
        projectRequest.setProjectDTO(new ProjectDTO());
        assertNull(this.projectEnrichmentService.enrichUpdateProjectData(projectRequest));
        verify(this.projectRepository).findByProjectId((String) any());
    }

    @Test
    void testEnrichUpdateProjectDataWithProjectUpdateRequestDetails() {
        AuditDetails auditDetails = mock(AuditDetails.class);
        when(auditDetails.getLastModifiedBy()).thenReturn("May 02, 2022 9:00am GMT+0100");
        doNothing().when(auditDetails).setLastModifiedBy((String) any());
        doNothing().when(auditDetails).setLastModifiedTime((Long) any());

        ProjectDTO projectDTO = projectUpdateRequest.getProjectDTO();
        projectDTO.setAuditDetails(auditDetails);

        Optional<ProjectDTO> ofResult = Optional.of(projectDTO);
        when(this.projectRepository.findByProjectId((String) any())).thenReturn(ofResult);
        RequestHeader requestHeader = new RequestHeader();

        ProjectRequest projectRequest = new ProjectRequest(requestHeader, new ProjectDTO());
        ArrayList<String> departmentEntityIds1 = new ArrayList<>();
        ArrayList<String> locationIds = new ArrayList<>();
        projectRequest.setProjectDTO(
                new ProjectDTO("205180a0-4d60-4805-a77f-92143bd115b4", "pb", "Code", "Name", "42", departmentEntityIds1, locationIds, new AuditDetails()));
        ProjectDTO actualEnrichUpdateProjectDTODataResult = this.projectEnrichmentService.enrichUpdateProjectData(projectUpdateRequest);
        assertSame(projectDTO, actualEnrichUpdateProjectDTODataResult);
        assertNotNull(actualEnrichUpdateProjectDTODataResult.getName());
        assertNotNull(actualEnrichUpdateProjectDTODataResult.getExpenditureId());
        assertNotNull(actualEnrichUpdateProjectDTODataResult.getCode());
        verify(this.projectRepository).findByProjectId((String) any());
        AuditDetails expectedAuditDetails = actualEnrichUpdateProjectDTODataResult.getAuditDetails();
        assertNotNull(expectedAuditDetails);
        assertNotNull(expectedAuditDetails.getLastModifiedBy());
        assertNotNull(expectedAuditDetails.getLastModifiedTime());
    }*/

}

