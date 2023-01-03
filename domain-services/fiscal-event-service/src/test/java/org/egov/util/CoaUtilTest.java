package org.egov.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.request.RequestHeader;
import org.egov.config.FiscalEventConfiguration;
import org.egov.config.TestDataFormatter;
import org.egov.repository.ServiceRequestRepository;
import org.egov.tracer.model.CustomException;
import org.egov.web.models.Amount;
import org.egov.web.models.FiscalEvent;
import org.egov.web.models.FiscalEventRequest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
@Slf4j
class CoaUtilTest {

    @Autowired
    private TestDataFormatter testDataFormatter;

    @Mock
    private FiscalEventConfiguration fiscalEventConfiguration;
    @Mock
    private ServiceRequestRepository serviceRequestRepository;
    @Spy
    private ObjectMapper objectMapper;

    @InjectMocks
    private CoaUtil coaUtil;

    private FiscalEventRequest fiscalEventRequest;
    private JsonNode validCOAResponse;

    @BeforeAll
    public void init() throws IOException {
        fiscalEventRequest = testDataFormatter.getFiscalEventPushRequestData();
        validCOAResponse = testDataFormatter.getCOASearchResponse();
    }

    @Test
    void testGetCOAIdsFromCOAServiceWithEmptyCOAIds() {
        when(this.serviceRequestRepository.fetchResult((String) any(), (Object) any())).thenReturn("Fetch Result");
        when(this.fiscalEventConfiguration.getIfixMasterCoaSearchPath()).thenReturn("Ifix Master Coa Search Path");
        when(this.fiscalEventConfiguration.getIfixMasterCoaContextPath()).thenReturn("Ifix Master Coa Context Path");
        when(this.fiscalEventConfiguration.getIfixMasterCoaHost()).thenReturn("localhost");
        RequestHeader requestHeader = fiscalEventRequest.getRequestHeader();
        HashSet<String> stringHashSet = new HashSet<String>();
        assertThrows(CustomException.class,
                () -> this.coaUtil.fetchCoaDetailsByCoaCodes(requestHeader, stringHashSet, "pb"));
        verify(this.serviceRequestRepository).fetchResult((String) any(), (Object) any());
        verify(this.fiscalEventConfiguration).getIfixMasterCoaContextPath();
        verify(this.fiscalEventConfiguration).getIfixMasterCoaHost();
        verify(this.fiscalEventConfiguration).getIfixMasterCoaSearchPath();
    }

    @Test
    void testGetCOAIdsFromCOAServiceWithEmptyTenantId() {
        when(this.serviceRequestRepository.fetchResult((String) any(), (Object) any())).thenReturn("Fetch Result");
        when(this.fiscalEventConfiguration.getIfixMasterCoaSearchPath()).thenReturn("Ifix Master Coa Search Path");
        when(this.fiscalEventConfiguration.getIfixMasterCoaContextPath()).thenReturn("Ifix Master Coa Context Path");
        when(this.fiscalEventConfiguration.getIfixMasterCoaHost()).thenReturn("localhost");
        RequestHeader requestHeader = fiscalEventRequest.getRequestHeader();
        HashSet<String> stringHashSet = new HashSet<String>();
        assertThrows(CustomException.class,
                () -> this.coaUtil.fetchCoaDetailsByCoaCodes(requestHeader, stringHashSet, ""));
        verify(this.serviceRequestRepository).fetchResult((String) any(), (Object) any());
        verify(this.fiscalEventConfiguration).getIfixMasterCoaContextPath();
        verify(this.fiscalEventConfiguration).getIfixMasterCoaHost();
        verify(this.fiscalEventConfiguration).getIfixMasterCoaSearchPath();
    }

    @Test
    public void testGetCOAIdsFromCOAServiceWithInvalidResponse() {
        doReturn(objectMapper.createObjectNode()).when(serviceRequestRepository).fetchResult(any(), any());
        Set<String> coaCodes = new HashSet<>();
        String tenantId = fiscalEventRequest.getFiscalEvent().get(0).getTenantId();
        for (FiscalEvent fiscalEvent : fiscalEventRequest.getFiscalEvent()) {
            for (Amount amount : fiscalEvent.getAmountDetails()) {
                coaCodes.add(amount.getCoaCode());
            }
        }
        RequestHeader requestHeader = fiscalEventRequest.getRequestHeader();
        assertThrows(CustomException.class,
                () -> coaUtil.fetchCoaDetailsByCoaCodes(requestHeader, coaCodes, tenantId));
    }

    @Test
    public void testGetCOAIdsFromCOAServiceWithValidResponse() {
        Map<String, Object> map = objectMapper.convertValue(validCOAResponse, new TypeReference<Map<String, Object>>() {
        });
        doReturn(map).when(serviceRequestRepository).fetchResult(any(), any());
        Set<String> coaCodes = new HashSet<>();
        String tenantId = fiscalEventRequest.getFiscalEvent().get(0).getTenantId();
        for (FiscalEvent fiscalEvent : fiscalEventRequest.getFiscalEvent()) {
            for (Amount amount : fiscalEvent.getAmountDetails()) {
                coaCodes.add(amount.getCoaCode());
            }
        }
        JsonNode validCOACodes = coaUtil.fetchCoaDetailsByCoaCodes(fiscalEventRequest.getRequestHeader(), coaCodes, tenantId);
        assertTrue(validCOACodes.size() > 0);
    }

}
