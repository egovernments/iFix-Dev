package org.egov.service;

import org.egov.common.contract.AuditDetails;
import org.egov.common.contract.request.RequestHeader;
import org.egov.config.TestDataFormatter;
import org.egov.util.MasterDataServiceUtil;
import org.egov.web.models.Government;
import org.egov.web.models.GovernmentRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@SpringBootTest
class GovernmentEnrichmentServiceTest {
    @Autowired
    private TestDataFormatter testDataFormatter;

    @InjectMocks
    private GovernmentEnrichmentService governmentEnrichmentService;

    @Mock
    private MasterDataServiceUtil masterDataServiceUtil;

    private GovernmentRequest governmentRequest;
    private AuditDetails auditDetails;
    private String userId;

    @BeforeEach
    public void init() throws IOException {
        governmentRequest = testDataFormatter.getGovernmentRequestData();

        Government government = governmentRequest.getGovernment();
        RequestHeader requestHeader = governmentRequest.getRequestHeader();
        Long time = System.currentTimeMillis();
        userId = requestHeader.getUserInfo().getUuid();

        auditDetails = new AuditDetails(userId, userId, time, time);
    }

    @Test
    void testEnrichGovernmentDataWithAuditDetails() {
        governmentRequest.getGovernment().setAuditDetails(auditDetails);

        doReturn(auditDetails).when(masterDataServiceUtil).enrichAuditDetails(userId, auditDetails, false);

        governmentEnrichmentService.enrichGovernmentData(governmentRequest);

        assertNotNull(governmentRequest.getGovernment().getAuditDetails());
        verify(masterDataServiceUtil).enrichAuditDetails(userId, auditDetails, false);

    }

    @Test
    void testEnrichGovernmentDataWithoutAuditDetails() {
        doReturn(auditDetails).when(masterDataServiceUtil)
                .enrichAuditDetails(userId, governmentRequest.getGovernment().getAuditDetails(), true);

        governmentEnrichmentService.enrichGovernmentData(governmentRequest);

        assertNotNull(governmentRequest.getGovernment().getAuditDetails());
        verify(masterDataServiceUtil).enrichAuditDetails(userId, null, true);

    }

}

