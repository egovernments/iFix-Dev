package org.egov.service;

import org.egov.common.contract.AuditDetails;
import org.egov.common.contract.request.RequestHeader;
import org.egov.config.TestDataFormatter;
import org.egov.util.MasterDataServiceUtil;
import org.egov.web.models.ExpenditureDTO;
import org.egov.web.models.ExpenditureRequest;
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

//@SpringBootTest
class ExpenditureEnrichmentServiceTest {

   /* @Autowired
    private TestDataFormatter testDataFormatter;

    @InjectMocks
    private ExpenditureEnrichmentService expenditureEnrichmentService;

    @Mock
    private MasterDataServiceUtil masterDataServiceUtil;

    private ExpenditureRequest expenditureRequest;
    private AuditDetails auditDetails;
    private String userId;

    @BeforeEach
    public void init() throws IOException {
        expenditureRequest = testDataFormatter.getExpenditureCreateRequestData();

        ExpenditureDTO expenditureDTO = expenditureRequest.getExpenditureDTO();
        RequestHeader requestHeader = expenditureRequest.getRequestHeader();
        Long time = System.currentTimeMillis();
        userId = requestHeader.getUserInfo().getUuid();

        auditDetails = new AuditDetails(userId, userId, time, time);
    }

    @Test
    void testEnrichCreateExpenditureWithAuditDetails() {
        expenditureRequest.getExpenditureDTO().setAuditDetails(auditDetails);

        doReturn(auditDetails).when(masterDataServiceUtil).enrichAuditDetails(userId, auditDetails, false);

        expenditureEnrichmentService.enrichCreateExpenditure(expenditureRequest);

        assertNotNull(expenditureRequest.getExpenditureDTO().getAuditDetails());
        verify(masterDataServiceUtil).enrichAuditDetails(userId, auditDetails, false);
    }

    @Test
    void testEnrichCreateExpenditureWithoutAuditDetails() {
        doReturn(auditDetails).when(masterDataServiceUtil)
                .enrichAuditDetails(userId, expenditureRequest.getExpenditureDTO().getAuditDetails(), true);

        expenditureEnrichmentService.enrichCreateExpenditure(expenditureRequest);

        assertNotNull(expenditureRequest.getExpenditureDTO().getAuditDetails());
        verify(masterDataServiceUtil).enrichAuditDetails(userId, null, true);
    }
*/
}

