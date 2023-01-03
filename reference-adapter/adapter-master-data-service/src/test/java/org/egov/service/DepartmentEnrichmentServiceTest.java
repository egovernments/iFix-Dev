package org.egov.service;

import org.egov.common.contract.AuditDetails;
import org.egov.common.contract.request.RequestHeader;
import org.egov.config.TestDataFormatter;
import org.egov.util.MasterDataServiceUtil;
import org.egov.web.models.Department;
import org.egov.web.models.DepartmentRequest;
import org.egov.web.models.DepartmentSearchRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@SpringBootTest
class DepartmentEnrichmentServiceTest {
    @Autowired
    private TestDataFormatter testDataFormatter;

    @InjectMocks
    private DepartmentEnrichmentService departmentEnrichmentService;

    @Mock
    private MasterDataServiceUtil masterDataServiceUtil;

    private DepartmentRequest departmentRequest;
    private AuditDetails auditDetails;
    private String userId;

    @BeforeEach
    public void init() throws IOException {
        departmentRequest = testDataFormatter.getDepartmentCreateRequestData();

        Department department = departmentRequest.getDepartment();
        RequestHeader requestHeader = departmentRequest.getRequestHeader();
        Long time = System.currentTimeMillis();
        userId = requestHeader.getUserInfo().getUuid();

        auditDetails = new AuditDetails(userId, userId, time, time);
    }

    @Test
    void testEnrichDepartmentDataWithAuditDetails() {
        departmentRequest.getDepartment().setAuditDetails(auditDetails);

        doReturn(auditDetails).when(masterDataServiceUtil).enrichAuditDetails(userId, auditDetails, false);

        departmentEnrichmentService.enrichDepartmentData(departmentRequest);

        assertNotNull(departmentRequest.getDepartment().getAuditDetails());
        verify(masterDataServiceUtil).enrichAuditDetails(userId, auditDetails, false);
    }

    @Test
    void testEnrichDepartmentDataWithoutAuditDetails() {
        doReturn(auditDetails).when(masterDataServiceUtil)
                .enrichAuditDetails(userId, departmentRequest.getDepartment().getAuditDetails(), true);

        departmentEnrichmentService.enrichDepartmentData(departmentRequest);

        assertNotNull(departmentRequest.getDepartment().getAuditDetails());
        verify(masterDataServiceUtil).enrichAuditDetails(userId, null, true);
    }

    @Test
    void testEnrichSearchPost() {
        DepartmentSearchRequest departmentSearchRequest = new DepartmentSearchRequest();
        departmentEnrichmentService.enrichSearchPost(departmentSearchRequest);
        assertNull(departmentSearchRequest.getCriteria());
        assertNull(departmentSearchRequest.getRequestHeader());
    }

}

