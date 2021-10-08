package org.egov.service;

import org.egov.common.contract.AuditDetails;
import org.egov.common.contract.request.RequestHeader;
import org.egov.common.contract.request.UserInfo;
import org.egov.config.TestDataFormatter;
import org.egov.util.FiscalEventUtil;
import org.egov.web.models.Amount;
import org.egov.web.models.FiscalEvent;
import org.egov.web.models.FiscalEventRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
class FiscalEventEnrichmentServiceTest {

    @InjectMocks
    private FiscalEventEnrichmentService fiscalEventEnrichmentService;

    @Mock
    private FiscalEventUtil fiscalEventUtil;

    @Autowired
    private TestDataFormatter testDataFormatter;

    private AuditDetails auditDetails;
    private FiscalEventRequest fiscalEventRequest;


    @BeforeEach
    void init() throws IOException {
        fiscalEventRequest = testDataFormatter.getFiscalEventPushRequestData();
        RequestHeader requestHeader = fiscalEventRequest.getRequestHeader();
        Long time = System.currentTimeMillis();
        auditDetails = AuditDetails.builder().createdBy(requestHeader.getUserInfo().getUuid())
                .lastModifiedBy(requestHeader.getUserInfo().getUuid()).createdTime(time).lastModifiedTime(time).build();
    }

    @Test
    void testEnrichFiscalEventPushPostWithDefaultRequest() {
        FiscalEventRequest fiscalEventRequest = new FiscalEventRequest();
        this.fiscalEventEnrichmentService.enrichFiscalEventPushPost(fiscalEventRequest);
        assertNull(fiscalEventRequest.getFiscalEvent());
        assertNull(fiscalEventRequest.getRequestHeader());
    }

    @Test
    void testEnrichFiscalEventPushPostWithoutAuditDetails() {
        AuditDetails auditDetails = new AuditDetails();
        when(this.fiscalEventUtil.enrichAuditDetails((String) any(), (AuditDetails) any(), (Boolean) any()))
                .thenReturn(auditDetails);

        RequestHeader requestHeader = new RequestHeader();
        requestHeader.setUserInfo(new UserInfo());
        FiscalEventRequest fiscalEventRequest = new FiscalEventRequest(requestHeader, new FiscalEvent());

        this.fiscalEventEnrichmentService.enrichFiscalEventPushPost(fiscalEventRequest);
        verify(this.fiscalEventUtil).enrichAuditDetails((String) any(), (AuditDetails) any(), any(Boolean.TRUE.getClass()));
        FiscalEvent fiscalEvent = fiscalEventRequest.getFiscalEvent();
        assertTrue(fiscalEvent.getAmountDetails().isEmpty());
        assertSame(auditDetails, fiscalEvent.getAuditDetails());
    }

    @Test
    void testEnrichFiscalEventPushPostWithAuditDetails() {
        when(this.fiscalEventUtil.enrichAuditDetails((String) any(), (AuditDetails) any(), (Boolean) any()))
                .thenReturn(auditDetails);
        fiscalEventRequest.getFiscalEvent().setAuditDetails(auditDetails);
        this.fiscalEventEnrichmentService.enrichFiscalEventPushPost(fiscalEventRequest);
        verify(this.fiscalEventUtil).enrichAuditDetails((String) any(), (AuditDetails) any(), any(Boolean.FALSE.getClass()));
        FiscalEvent fiscalEvent1 = fiscalEventRequest.getFiscalEvent();
        assertTrue(fiscalEvent1.getAmountDetails().size() > 0);
        assertEquals(auditDetails, fiscalEvent1.getAuditDetails());
    }

    @Test
    void testEnrichFiscalEventPushPostWithDefaultAmountDetails() {
        when(this.fiscalEventUtil.enrichAuditDetails((String) any(), (AuditDetails) any(), (Boolean) any()))
                .thenReturn(auditDetails);

        FiscalEvent fiscalEvent = new FiscalEvent();
        fiscalEvent.addAmountDetailsItem(new Amount());
        fiscalEvent.setAuditDetails(auditDetails);
        FiscalEventRequest fiscalEventRequest1 = FiscalEventRequest.builder().fiscalEvent(fiscalEvent)
                .requestHeader(fiscalEventRequest.getRequestHeader()).build();

        this.fiscalEventEnrichmentService.enrichFiscalEventPushPost(fiscalEventRequest1);
        verify(this.fiscalEventUtil).enrichAuditDetails((String) any(), (AuditDetails) any(), (Boolean) any());
        FiscalEvent fiscalEvent1 = fiscalEventRequest1.getFiscalEvent();
        List<Amount> amountDetails = fiscalEvent1.getAmountDetails();
        assertEquals(1, amountDetails.size());
        assertSame(auditDetails, fiscalEvent1.getAuditDetails());
        Amount getResult = amountDetails.get(0);
        assertNull(getResult.getFromBillingPeriod());
        assertNull(getResult.getCoaId());
        assertNull(getResult.getAmount());
        assertNull(getResult.getToBillingPeriod());
    }

    @Test
    void testEnrichFiscalEventPushPostWithFiscalEventRequest() {
        when(this.fiscalEventUtil.enrichAuditDetails((String) any(), (AuditDetails) any(), (Boolean) any()))
                .thenReturn(auditDetails);

        FiscalEvent fiscalEvent = fiscalEventRequest.getFiscalEvent();
        this.fiscalEventEnrichmentService.enrichFiscalEventPushPost(fiscalEventRequest);
        verify(this.fiscalEventUtil).enrichAuditDetails((String) any(), (AuditDetails) any(), (Boolean) any());
        assertNotNull(fiscalEvent.getAmountDetails());
        assertTrue(fiscalEvent.getAmountDetails().size() > 0);
        assertNotNull(fiscalEvent.getAmountDetails().get(0).getId());
        assertNotNull(fiscalEvent.getId());
        assertNotNull(fiscalEvent.getIngestionTime());
    }

    @Test
    void testEnrichFiscalEventPushPostWithNullFiscalEvent() {
        when(this.fiscalEventUtil.enrichAuditDetails((String) any(), (AuditDetails) any(), (Boolean) any()))
                .thenReturn(null);

        RequestHeader requestHeader = fiscalEventRequest.getRequestHeader();
        fiscalEventRequest.setFiscalEvent(null);
        this.fiscalEventEnrichmentService.enrichFiscalEventPushPost(fiscalEventRequest);
        assertSame(requestHeader, fiscalEventRequest.getRequestHeader());
    }
}

