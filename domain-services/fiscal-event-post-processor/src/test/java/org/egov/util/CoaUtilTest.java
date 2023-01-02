package org.egov.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.egov.common.contract.request.RequestHeader;
import org.egov.config.FiscalEventPostProcessorConfig;
import org.egov.config.TestDataFormatter;
import org.egov.models.ChartOfAccount;
import org.egov.models.FiscalEvent;
import org.egov.models.FiscalEventRequest;
import org.egov.resposioty.ServiceRequestRepository;
import org.egov.tracer.model.CustomException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
class CoaUtilTest {

    @InjectMocks
    private CoaUtil coaUtil;

    @Mock
    private FiscalEventPostProcessorConfig fiscalEventPostProcessorConfig;

    @Spy
    private ObjectMapper objectMapper;

    @Mock
    private ServiceRequestRepository serviceRequestRepository;

    @Autowired
    private TestDataFormatter testDataFormatter;

    private ChartOfAccount chartOfAccount;
    private FiscalEventRequest fiscalEventRequest;
    private JsonNode coaJsonNode;

    @BeforeAll
    void init() throws IOException {
        coaJsonNode = testDataFormatter.getCOASearchResponse();
        chartOfAccount = objectMapper.convertValue(coaJsonNode.get("chartOfAccounts").get(0), ChartOfAccount.class);
        fiscalEventRequest = testDataFormatter.getFiscalEventValidatedData();
    }

    @Test
    void testGetCOAIdsFromCOAServiceWithDefaultFiscalEvent() {
        when(this.serviceRequestRepository.fetchResult((String) any(), (Object) any())).thenReturn("Fetch Result");
        when(this.fiscalEventPostProcessorConfig.getIfixMasterCoaSearchPath()).thenReturn("Ifix Master Coa Search Path");
        when(this.fiscalEventPostProcessorConfig.getIfixMasterCoaContextPath()).thenReturn("Ifix Master Coa Context Path");
        when(this.fiscalEventPostProcessorConfig.getIfixMasterCoaHost()).thenReturn("localhost");
        RequestHeader requestHeader = new RequestHeader();
        FiscalEvent fiscalEvent = new FiscalEvent();
        assertThrows(CustomException.class, () -> this.coaUtil.getCOAIdsFromCOAService(requestHeader, fiscalEvent));
        verify(this.serviceRequestRepository).fetchResult((String) any(), (Object) any());
        verify(this.fiscalEventPostProcessorConfig).getIfixMasterCoaContextPath();
        verify(this.fiscalEventPostProcessorConfig).getIfixMasterCoaHost();
        verify(this.fiscalEventPostProcessorConfig).getIfixMasterCoaSearchPath();
    }

    @Test
    void testGetCOAIdsFromCOAServiceWithNullResponseFromCoaService() {
        when(this.serviceRequestRepository.fetchResult((String) any(), (Object) any())).thenReturn(null);
        when(this.fiscalEventPostProcessorConfig.getIfixMasterCoaSearchPath()).thenReturn("Ifix Master Coa Search Path");
        when(this.fiscalEventPostProcessorConfig.getIfixMasterCoaContextPath()).thenReturn("Ifix Master Coa Context Path");
        when(this.fiscalEventPostProcessorConfig.getIfixMasterCoaHost()).thenReturn("localhost");
        RequestHeader requestHeader = new RequestHeader();
        FiscalEvent fiscalEvent = new FiscalEvent();
        assertThrows(CustomException.class, () -> this.coaUtil.getCOAIdsFromCOAService(requestHeader, fiscalEvent));
        verify(this.serviceRequestRepository).fetchResult((String) any(), (Object) any());
        verify(this.fiscalEventPostProcessorConfig).getIfixMasterCoaContextPath();
        verify(this.fiscalEventPostProcessorConfig).getIfixMasterCoaHost();
        verify(this.fiscalEventPostProcessorConfig).getIfixMasterCoaSearchPath();
    }

    @Test
    void testGetCOAIdsFromCOAServiceWithNullFiscalEvent() {
        when(this.serviceRequestRepository.fetchResult((String) any(), (Object) any())).thenReturn("Fetch Result");
        when(this.fiscalEventPostProcessorConfig.getIfixMasterCoaSearchPath()).thenReturn("Ifix Master Coa Search Path");
        when(this.fiscalEventPostProcessorConfig.getIfixMasterCoaContextPath()).thenReturn("Ifix Master Coa Context Path");
        when(this.fiscalEventPostProcessorConfig.getIfixMasterCoaHost()).thenReturn("localhost");
        RequestHeader requestHeader = new RequestHeader();
        assertThrows(CustomException.class, () -> this.coaUtil.getCOAIdsFromCOAService(requestHeader, null));
        verify(this.serviceRequestRepository).fetchResult((String) any(), (Object) any());
        verify(this.fiscalEventPostProcessorConfig).getIfixMasterCoaContextPath();
        verify(this.fiscalEventPostProcessorConfig).getIfixMasterCoaHost();
        verify(this.fiscalEventPostProcessorConfig).getIfixMasterCoaSearchPath();
    }

    @Test
    void testGetCOAIdsFromCOAServiceWithInvalidResponseFromServiceRepo() {
        when(this.serviceRequestRepository.fetchResult((String) any(), (Object) any())).thenReturn("Fetch Result");
        when(this.fiscalEventPostProcessorConfig.getIfixMasterCoaSearchPath()).thenReturn("Ifix Master Coa Search Path");
        when(this.fiscalEventPostProcessorConfig.getIfixMasterCoaContextPath()).thenReturn("Ifix Master Coa Context Path");
        when(this.fiscalEventPostProcessorConfig.getIfixMasterCoaHost()).thenReturn("localhost");
        RequestHeader requestHeader = fiscalEventRequest.getRequestHeader();
        FiscalEvent fiscalEvent = fiscalEventRequest.getFiscalEvent();
        assertThrows(CustomException.class,
                () -> this.coaUtil.getCOAIdsFromCOAService(requestHeader, fiscalEvent));
        verify(this.serviceRequestRepository).fetchResult((String) any(), (Object) any());
        verify(this.fiscalEventPostProcessorConfig).getIfixMasterCoaContextPath();
        verify(this.fiscalEventPostProcessorConfig).getIfixMasterCoaHost();
        verify(this.fiscalEventPostProcessorConfig).getIfixMasterCoaSearchPath();
    }

    @Test
    void testGetCOAIdsFromCOAService8() {
        Map<String, Object> response = objectMapper.convertValue(coaJsonNode, new TypeReference<Map<String, Object>>() {
        });
        when(this.serviceRequestRepository.fetchResult((String) any(), (Object) any())).thenReturn(response);
        when(this.fiscalEventPostProcessorConfig.getIfixMasterCoaSearchPath()).thenReturn("Ifix Master Coa Search Path");
        when(this.fiscalEventPostProcessorConfig.getIfixMasterCoaContextPath()).thenReturn("Ifix Master Coa Context Path");
        when(this.fiscalEventPostProcessorConfig.getIfixMasterCoaHost()).thenReturn("localhost");

        List<ChartOfAccount> chartOfAccountList = coaUtil.getCOAIdsFromCOAService(fiscalEventRequest.getRequestHeader(), fiscalEventRequest.getFiscalEvent());

        assertNotNull(chartOfAccountList);
        assertFalse(chartOfAccountList.isEmpty());

        verify(this.serviceRequestRepository).fetchResult((String) any(), (Object) any());
        verify(this.fiscalEventPostProcessorConfig).getIfixMasterCoaContextPath();
        verify(this.fiscalEventPostProcessorConfig).getIfixMasterCoaHost();
        verify(this.fiscalEventPostProcessorConfig).getIfixMasterCoaSearchPath();
    }
}

