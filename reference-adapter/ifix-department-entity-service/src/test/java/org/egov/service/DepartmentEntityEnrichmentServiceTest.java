package org.egov.service;

import org.egov.common.contract.AuditDetails;
import org.egov.config.TestDataFormatter;
import org.egov.util.DepartmentEntityUtil;
import org.egov.web.models.DepartmentEntityRequest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@Disabled("TODO: Need to work on it")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
class DepartmentEntityEnrichmentServiceTest {

    @InjectMocks
    private DepartmentEntityEnrichmentService departmentEntityEnrichmentService;

    @Mock
    private DepartmentEntityUtil departmentEntityUtil;

    @Autowired
    private TestDataFormatter testDataFormatter;

    private DepartmentEntityRequest departmentEntityRequest;
    private AuditDetails auditDetails;

    @BeforeAll
    void init() throws IOException {
        departmentEntityRequest = testDataFormatter.getDeptEntityCreateRequestData();
        auditDetails = testDataFormatter.getDeptEntityCreateResponseData().getDepartmentEntity().get(0).getAuditDetails();
    }

    @Test
    void testEnrichDepartmentEntityDataWithoutAuditDetails() {
        when(this.departmentEntityUtil.enrichAuditDetails((String) any(), (AuditDetails) any(), (Boolean) any()))
                .thenReturn(new AuditDetails());
        this.departmentEntityEnrichmentService.enrichDepartmentEntityData(departmentEntityRequest);
        verify(this.departmentEntityUtil).enrichAuditDetails((String) any(), (AuditDetails) any(), (Boolean) any());
        assertNotNull(departmentEntityRequest);
        assertNotNull(departmentEntityRequest.getDepartmentEntityDTO());
        assertNotNull(departmentEntityRequest.getDepartmentEntityDTO().getAuditDetails());
    }

    @Test
    void testEnrichDepartmentEntityDataWithAuditDetails() {
        when(this.departmentEntityUtil.enrichAuditDetails((String) any(), (AuditDetails) any(), (Boolean) any()))
                .thenReturn(auditDetails);
        departmentEntityRequest.getDepartmentEntityDTO().setAuditDetails(auditDetails);
        this.departmentEntityEnrichmentService.enrichDepartmentEntityData(departmentEntityRequest);
        verify(this.departmentEntityUtil).enrichAuditDetails((String) any(), (AuditDetails) any(), (Boolean) any());
        assertNotNull(departmentEntityRequest);
        assertNotNull(departmentEntityRequest.getDepartmentEntityDTO());
        assertNotNull(departmentEntityRequest.getDepartmentEntityDTO().getAuditDetails());
    }
}

