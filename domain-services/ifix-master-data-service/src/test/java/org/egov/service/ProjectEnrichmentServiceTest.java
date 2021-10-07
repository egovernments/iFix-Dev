package org.egov.service;

import org.egov.common.contract.AuditDetails;
import org.egov.common.contract.request.RequestHeader;
import org.egov.config.TestDataFormatter;
import org.egov.util.MasterDataServiceUtil;
import org.egov.web.models.DepartmentEntity;
import org.egov.web.models.Project;
import org.egov.web.models.ProjectRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@SpringBootTest
class ProjectEnrichmentServiceTest {
    @Autowired
    private TestDataFormatter testDataFormatter;

    @Mock
    private MasterDataServiceUtil masterDataServiceUtil;

    @Mock
    private ProjectDepartmentEntityIntegration projectDepartmentEntityIntegration;

    @InjectMocks
    private ProjectEnrichmentService projectEnrichmentService;

    private ProjectRequest projectRequest;
    private AuditDetails auditDetails;
    private String userId;

    @BeforeEach
    public void init() throws IOException {
        projectRequest = testDataFormatter.getProjectCreateRequestData();

        Project project = projectRequest.getProject();
        RequestHeader requestHeader = projectRequest.getRequestHeader();
        Long time = System.currentTimeMillis();
        userId = requestHeader.getUserInfo().getUuid();

        auditDetails = new AuditDetails(userId, userId, time, time);
    }

    @Test
    void testEnrichProjectDataWithAuditDetails() {
        projectRequest.getProject().setAuditDetails(auditDetails);

        when(this.projectDepartmentEntityIntegration.getDepartmentEntityForId((RequestHeader) any(), (String) any(),
                (String) any())).thenReturn(new DepartmentEntity());
        doReturn(auditDetails).when(masterDataServiceUtil).enrichAuditDetails(userId, auditDetails, false);

        projectEnrichmentService.enrichProjectData(projectRequest);

        assertNotNull(projectRequest.getProject().getAuditDetails());

        verify(this.projectDepartmentEntityIntegration).getDepartmentEntityForId((RequestHeader) any(), (String) any(),
                (String) any());
        verify(masterDataServiceUtil).enrichAuditDetails(userId, auditDetails, false);

    }

    @Test
    void testEnrichProjectDataWithoutAuditDetails() {
        when(this.projectDepartmentEntityIntegration.getDepartmentEntityForId((RequestHeader) any(), (String) any(),
                (String) any())).thenReturn(new DepartmentEntity());
        doReturn(auditDetails).when(masterDataServiceUtil).enrichAuditDetails(userId, projectRequest.getProject().getAuditDetails(), true);

        projectEnrichmentService.enrichProjectData(projectRequest);

        assertNotNull(projectRequest.getProject().getAuditDetails());

        verify(this.projectDepartmentEntityIntegration).getDepartmentEntityForId((RequestHeader) any(), (String) any(),
                (String) any());
        verify(masterDataServiceUtil).enrichAuditDetails(userId, null, true);

    }

}

