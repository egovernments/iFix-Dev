package org.egov.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.egov.common.contract.request.RequestHeader;
import org.egov.config.TestDataFormatter;
import org.egov.models.*;
import org.egov.util.CoaUtil;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
class FiscalEventDereferenceServiceTest {

    @Mock
    private CoaUtil coaUtil;

    @Mock
    private FiscalEventDereferenceEnrichmentService fiscalEventDereferenceEnrichmentService;

    @InjectMocks
    private FiscalEventDereferenceService fiscalEventDereferenceService;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private TestDataFormatter testDataFormatter;

    private FiscalEventRequest fiscalEventRequest;
    private ChartOfAccount chartOfAccount;

    @BeforeAll
    void init() throws IOException {
        fiscalEventRequest = testDataFormatter.getFiscalEventValidatedData();

        JsonNode coaJsonNode = testDataFormatter.getCOASearchResponse();
        chartOfAccount = mapper.convertValue(coaJsonNode.get("chartOfAccounts").get(0), ChartOfAccount.class);
    }

    @Test
    void testDereference() {
        doNothing().when(this.fiscalEventDereferenceEnrichmentService)
                .enrich((FiscalEventRequest) any(), (FiscalEventDeReferenced) any());
        when(this.coaUtil.getCOAIdsFromCOAService((org.egov.common.contract.request.RequestHeader) any(),
                (FiscalEvent) any())).thenReturn(new ArrayList<ChartOfAccount>());

        FiscalEventRequest fiscalEventRequest = new FiscalEventRequest();
        fiscalEventRequest.setFiscalEvent(new FiscalEvent());
        FiscalEventDeReferenced actualDereferenceResult = this.fiscalEventDereferenceService
                .dereference(fiscalEventRequest);
        assertTrue(actualDereferenceResult.getAmountDetails().isEmpty());
        assertNull(actualDereferenceResult.getVersion());
        assertNull(actualDereferenceResult.getTenantId());
        assertNull(actualDereferenceResult.getReferenceId());
        assertNull(actualDereferenceResult.getLinkedReferenceId());
        assertNull(actualDereferenceResult.getLinkedEventId());
        assertNull(actualDereferenceResult.getIngestionTime());
        assertNull(actualDereferenceResult.getId());
        assertNull(actualDereferenceResult.getEventType());
        assertNull(actualDereferenceResult.getEventTime());
        assertNull(actualDereferenceResult.getAuditDetails());
        assertNull(actualDereferenceResult.getAttributes());
        verify(this.fiscalEventDereferenceEnrichmentService).enrich((FiscalEventRequest) any(),
                (FiscalEventDeReferenced) any());
        verify(this.coaUtil).getCOAIdsFromCOAService((org.egov.common.contract.request.RequestHeader) any(),
                (FiscalEvent) any());
    }

    @Test
    void testDereferenceWithAllDataFromMasterServices() {
        doNothing().when(this.fiscalEventDereferenceEnrichmentService)
                .enrich((FiscalEventRequest) any(), (FiscalEventDeReferenced) any());

        List<ChartOfAccount> chartOfAccounts = new ArrayList<>();
        chartOfAccounts.add(chartOfAccount);
        when(this.coaUtil.getCOAIdsFromCOAService((RequestHeader) any(), (FiscalEvent) any()))
                .thenReturn(chartOfAccounts);

        FiscalEventDeReferenced actualDereferenceResult = this.fiscalEventDereferenceService
                .dereference(fiscalEventRequest);

        assertFalse(actualDereferenceResult.getAmountDetails().isEmpty());
        assertNull(actualDereferenceResult.getVersion());
        assertNotNull(actualDereferenceResult.getTenantId());
        assertNull(actualDereferenceResult.getReferenceId());
        assertNull(actualDereferenceResult.getLinkedReferenceId());
        assertNull(actualDereferenceResult.getLinkedEventId());
        assertNull(actualDereferenceResult.getIngestionTime());
        assertNull(actualDereferenceResult.getId());
        assertNull(actualDereferenceResult.getEventType());
        assertNull(actualDereferenceResult.getEventTime());
        assertNull(actualDereferenceResult.getAuditDetails());
        assertNull(actualDereferenceResult.getAttributes());
        verify(this.fiscalEventDereferenceEnrichmentService).enrich((FiscalEventRequest) any(),
                (FiscalEventDeReferenced) any());
        verify(this.coaUtil).getCOAIdsFromCOAService((RequestHeader) any(), (FiscalEvent) any());
    }

    @Test
    void testDereferenceWithDefaultFiscalEvent() {
        doNothing().when(this.fiscalEventDereferenceEnrichmentService)
                .enrich((FiscalEventRequest) any(), (FiscalEventDeReferenced) any());

        ArrayList<ChartOfAccount> chartOfAccountList = new ArrayList<ChartOfAccount>();
        chartOfAccountList.add(new ChartOfAccount("6cbcb4a1-2431-4f78-89d7-b4f0565aba37", "Coa Code", "Major Head", "Major Head Name", "Major Head Type",
                "Sub Major Head", "Sub Major Head Name", "Minor Head", "Minor Head Name", "Sub Head", "Sub Head Name",
                "Group Head", "Group Head Name", "Object Head", "Object Head Name"));
        when(this.coaUtil.getCOAIdsFromCOAService((org.egov.common.contract.request.RequestHeader) any(),
                (FiscalEvent) any())).thenReturn(chartOfAccountList);

        FiscalEventRequest fiscalEventRequest = new FiscalEventRequest();
        fiscalEventRequest.setFiscalEvent(new FiscalEvent());
        FiscalEventDeReferenced actualDereferenceResult = this.fiscalEventDereferenceService
                .dereference(fiscalEventRequest);
        assertTrue(actualDereferenceResult.getAmountDetails().isEmpty());
        assertNull(actualDereferenceResult.getVersion());
        assertNull(actualDereferenceResult.getTenantId());
        assertNull(actualDereferenceResult.getReferenceId());
        assertNull(actualDereferenceResult.getLinkedReferenceId());
        assertNull(actualDereferenceResult.getLinkedEventId());
        assertNull(actualDereferenceResult.getIngestionTime());
        assertNull(actualDereferenceResult.getId());
        assertNull(actualDereferenceResult.getEventType());
        assertNull(actualDereferenceResult.getEventTime());
        assertNull(actualDereferenceResult.getAuditDetails());
        assertNull(actualDereferenceResult.getAttributes());
        verify(this.fiscalEventDereferenceEnrichmentService).enrich((FiscalEventRequest) any(),
                (FiscalEventDeReferenced) any());
        verify(this.coaUtil).getCOAIdsFromCOAService((org.egov.common.contract.request.RequestHeader) any(),
                (FiscalEvent) any());
    }

    @Test
    void testDereferenceWithFiscalEventData() {
        doNothing().when(this.fiscalEventDereferenceEnrichmentService)
                .enrich((FiscalEventRequest) any(), (FiscalEventDeReferenced) any());
        when(this.coaUtil.getCOAIdsFromCOAService((RequestHeader) any(), (FiscalEvent) any()))
                .thenReturn(new ArrayList<ChartOfAccount>());

        FiscalEventDeReferenced actualDereferenceResult = this.fiscalEventDereferenceService
                .dereference(fiscalEventRequest);
        assertTrue(actualDereferenceResult.getAmountDetails().isEmpty());
        assertNull(actualDereferenceResult.getVersion());
        assertNotNull(actualDereferenceResult.getTenantId());
        assertNull(actualDereferenceResult.getReferenceId());
        assertNull(actualDereferenceResult.getLinkedReferenceId());
        assertNull(actualDereferenceResult.getLinkedEventId());
        assertNull(actualDereferenceResult.getIngestionTime());
        assertNull(actualDereferenceResult.getId());
        assertNull(actualDereferenceResult.getEventType());
        assertNull(actualDereferenceResult.getEventTime());
        assertNull(actualDereferenceResult.getAuditDetails());
        assertNull(actualDereferenceResult.getAttributes());
        verify(this.fiscalEventDereferenceEnrichmentService).enrich((FiscalEventRequest) any(),
                (FiscalEventDeReferenced) any());
        verify(this.coaUtil).getCOAIdsFromCOAService((RequestHeader) any(), (FiscalEvent) any());
    }

    @Test
    void testDereferenceWithProjectDetails() {
        doNothing().when(this.fiscalEventDereferenceEnrichmentService)
                .enrich((FiscalEventRequest) any(), (FiscalEventDeReferenced) any());
        when(this.coaUtil.getCOAIdsFromCOAService((RequestHeader) any(), (FiscalEvent) any()))
                .thenReturn(new ArrayList<ChartOfAccount>());
        FiscalEvent fiscalEvent = mock(FiscalEvent.class);
        when(fiscalEvent.getAmountDetails()).thenReturn(new ArrayList<Amount>());
        when(fiscalEvent.getTenantId()).thenReturn("pb");
        RequestHeader requestHeader = new RequestHeader();

        FiscalEventRequest fiscalEventRequest = new FiscalEventRequest(requestHeader, new FiscalEvent());
        fiscalEventRequest.setFiscalEvent(fiscalEvent);
        FiscalEventDeReferenced actualDereferenceResult = this.fiscalEventDereferenceService
                .dereference(fiscalEventRequest);
        assertTrue(actualDereferenceResult.getAmountDetails().isEmpty());
        assertNull(actualDereferenceResult.getVersion());
        assertEquals("pb", actualDereferenceResult.getTenantId());
        assertNull(actualDereferenceResult.getReferenceId());
        assertNull(actualDereferenceResult.getLinkedReferenceId());
        assertNull(actualDereferenceResult.getLinkedEventId());
        assertNull(actualDereferenceResult.getIngestionTime());
        assertNull(actualDereferenceResult.getId());
        assertNull(actualDereferenceResult.getEventType());
        assertNull(actualDereferenceResult.getEventTime());
        assertNull(actualDereferenceResult.getAuditDetails());
        assertNull(actualDereferenceResult.getAttributes());
        verify(this.fiscalEventDereferenceEnrichmentService).enrich((FiscalEventRequest) any(),
                (FiscalEventDeReferenced) any());
        verify(this.coaUtil).getCOAIdsFromCOAService((RequestHeader) any(), (FiscalEvent) any());
        verify(fiscalEvent, atLeast(1)).getAmountDetails();
        verify(fiscalEvent, atLeast(1)).getTenantId();
    }

    @Test
    void testDereferenceWithServiceReturnsProjectDetails() {
        doNothing().when(this.fiscalEventDereferenceEnrichmentService)
                .enrich((FiscalEventRequest) any(), (FiscalEventDeReferenced) any());
        when(this.coaUtil.getCOAIdsFromCOAService((RequestHeader) any(), (FiscalEvent) any()))
                .thenReturn(new ArrayList<ChartOfAccount>());
        FiscalEventDeReferenced actualDereferenceResult = this.fiscalEventDereferenceService
                .dereference(fiscalEventRequest);
        assertTrue(actualDereferenceResult.getAmountDetails().isEmpty());
        assertNull(actualDereferenceResult.getVersion());
        assertEquals("pb", actualDereferenceResult.getTenantId());
        assertNull(actualDereferenceResult.getReferenceId());
        assertNull(actualDereferenceResult.getLinkedReferenceId());
        assertNull(actualDereferenceResult.getLinkedEventId());
        assertNull(actualDereferenceResult.getIngestionTime());
        assertNull(actualDereferenceResult.getId());
        assertNull(actualDereferenceResult.getEventType());
        assertNull(actualDereferenceResult.getEventTime());
        assertNull(actualDereferenceResult.getAuditDetails());
        assertNull(actualDereferenceResult.getAttributes());
        verify(this.fiscalEventDereferenceEnrichmentService).enrich((FiscalEventRequest) any(),
                (FiscalEventDeReferenced) any());
        verify(this.coaUtil).getCOAIdsFromCOAService((RequestHeader) any(), (FiscalEvent) any());
    }
}

