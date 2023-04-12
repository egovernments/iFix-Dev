package org.egov.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.egov.FiscalApplicationMain;
import org.egov.common.contract.AuditDetails;
import org.egov.common.contract.request.RequestHeader;
import org.egov.common.contract.request.UserInfo;
import org.egov.config.TestDataFormatter;
import org.egov.util.CoaUtil;
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
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(classes = FiscalApplicationMain.class)
class FiscalEventEnrichmentServiceTest {

    @InjectMocks
    private FiscalEventEnrichmentService fiscalEventEnrichmentService;

    @Mock
    private FiscalEventUtil fiscalEventUtil;
    @Mock
    private CoaUtil coaUtil;

    @Autowired
    private TestDataFormatter testDataFormatter;

    private AuditDetails auditDetails;
    private FiscalEventRequest fiscalEventRequest;
    private JsonNode coaResponse;


    @BeforeEach
    void init() throws IOException {
        fiscalEventRequest = testDataFormatter.getFiscalEventPushRequestData();
        coaResponse = testDataFormatter.getCOASearchResponse().get("chartOfAccounts");
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
        List<FiscalEvent> fiscalEvents = new ArrayList<>();
        fiscalEvents.add(new FiscalEvent());
        FiscalEventRequest fiscalEventRequest = new FiscalEventRequest(requestHeader, fiscalEvents);

        this.fiscalEventEnrichmentService.enrichFiscalEventPushPost(fiscalEventRequest);
        verify(this.fiscalEventUtil).enrichAuditDetails((String) any(), (AuditDetails) any(), any(Boolean.TRUE.getClass()));
        List<FiscalEvent> fiscalEvent = fiscalEventRequest.getFiscalEvent();
        assertTrue(fiscalEvent.get(0).getAmountDetails().isEmpty());
        assertSame(auditDetails, fiscalEvent.get(0).getAuditDetails());
    }

    @Test
    void testEnrichFiscalEventPushPostWithAuditDetails() {
        when(this.fiscalEventUtil.enrichAuditDetails((String) any(), (AuditDetails) any(), (Boolean) any()))
                .thenReturn(auditDetails);
        when(coaUtil.fetchCoaDetailsByCoaCodes(any(), any(), any())).thenReturn(coaResponse);
        fiscalEventRequest.getFiscalEvent().get(0).setAuditDetails(auditDetails);
        this.fiscalEventEnrichmentService.enrichFiscalEventPushPost(fiscalEventRequest);
        verify(this.fiscalEventUtil, atLeastOnce()).enrichAuditDetails((String) any(), (AuditDetails) any(),
                any(Boolean.FALSE.getClass()));
        List<FiscalEvent> fiscalEvent1 = fiscalEventRequest.getFiscalEvent();
        assertTrue(fiscalEvent1.get(0).getAmountDetails().size() > 0);
        assertEquals(auditDetails, fiscalEvent1.get(0).getAuditDetails());
    }

    @Test
    void testEnrichFiscalEventPushPostWithFiscalEventRequest() {
        when(this.fiscalEventUtil.enrichAuditDetails((String) any(), (AuditDetails) any(), (Boolean) any()))
                .thenReturn(auditDetails);
        when(coaUtil.fetchCoaDetailsByCoaCodes(any(), any(), any())).thenReturn(coaResponse);
        List<FiscalEvent> fiscalEvent = fiscalEventRequest.getFiscalEvent();
        this.fiscalEventEnrichmentService.enrichFiscalEventPushPost(fiscalEventRequest);
        verify(this.fiscalEventUtil, atLeastOnce()).enrichAuditDetails((String) any(), (AuditDetails) any(), (Boolean) any());
        assertNotNull(fiscalEvent.get(0).getAmountDetails());
        assertTrue(fiscalEvent.get(0).getAmountDetails().size() > 0);
        assertNotNull(fiscalEvent.get(0).getAmountDetails().get(0).getId());
        assertNotNull(fiscalEvent.get(0).getId());
        assertNotNull(fiscalEvent.get(0).getIngestionTime());
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

