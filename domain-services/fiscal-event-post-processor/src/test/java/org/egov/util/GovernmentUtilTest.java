package org.egov.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.egov.common.contract.request.RequestHeader;
import org.egov.config.FiscalEventPostProcessorConfig;
import org.egov.config.TestDataFormatter;
import org.egov.models.FiscalEvent;
import org.egov.models.FiscalEventRequest;
import org.egov.models.Government;
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
class GovernmentUtilTest {

    @Mock
    private FiscalEventPostProcessorConfig fiscalEventPostProcessorConfig;

    @InjectMocks
    private GovernmentUtil governmentUtil;

    @Spy
    private ObjectMapper objectMapper;

    @Mock
    private ServiceRequestRepository serviceRequestRepository;

    @Autowired
    private TestDataFormatter testDataFormatter;

    private Government government;
    private FiscalEventRequest fiscalEventRequest;
    private JsonNode govJsonNode;

    @BeforeAll
    void init() throws IOException {
        govJsonNode = testDataFormatter.getValidGovernmentSearchResponse();
        government = objectMapper.convertValue(govJsonNode.get("government").get(0), Government.class);

        fiscalEventRequest = testDataFormatter.getFiscalEventValidatedData();
    }

    @Test
    void testGetGovernmentFromGovernmentServiceWithNullFiscalEventRequest() {
        assertTrue(this.governmentUtil.getGovernmentFromGovernmentService(new FiscalEventRequest()).isEmpty());
        assertTrue(this.governmentUtil.getGovernmentFromGovernmentService(null).isEmpty());
    }

    @Test
    void testGetGovernmentFromGovernmentServiceWithDefaultFiscalEventRequest() {
        RequestHeader requestHeader = new RequestHeader();
        assertTrue(
                this.governmentUtil.getGovernmentFromGovernmentService(new FiscalEventRequest(requestHeader, new FiscalEvent()))
                        .isEmpty());
    }

    @Test
    void testGetGovernmentFromGovernmentServiceWithNullFiscalEvent() {
        FiscalEventRequest fiscalEventRequest = mock(FiscalEventRequest.class);
        when(fiscalEventRequest.getFiscalEvent()).thenReturn(null);
        when(fiscalEventRequest.getRequestHeader()).thenReturn(new RequestHeader());
        assertTrue(this.governmentUtil.getGovernmentFromGovernmentService(fiscalEventRequest).isEmpty());
        verify(fiscalEventRequest).getFiscalEvent();
        verify(fiscalEventRequest).getRequestHeader();
    }

    @Test
    void testGetGovernmentFromGovernmentServiceWithInvalidServiceResponse() {
        when(this.serviceRequestRepository.fetchResult((String) any(), (Object) any())).thenReturn("Fetch Result");
        when(this.fiscalEventPostProcessorConfig.getIfixMasterGovernmentSearchPath())
                .thenReturn("Ifix Master Government Search Path");
        when(this.fiscalEventPostProcessorConfig.getIfixMasterGovernmentContextPath())
                .thenReturn("Ifix Master Government Context Path");
        when(this.fiscalEventPostProcessorConfig.getIfixMasterGovernmentHost()).thenReturn("localhost");

        assertThrows(CustomException.class,
                () -> this.governmentUtil.getGovernmentFromGovernmentService(fiscalEventRequest));
        verify(this.fiscalEventPostProcessorConfig).getIfixMasterGovernmentContextPath();
        verify(this.fiscalEventPostProcessorConfig).getIfixMasterGovernmentHost();
        verify(this.fiscalEventPostProcessorConfig).getIfixMasterGovernmentSearchPath();
    }

    @Test
    void testGetGovernmentFromGovernmentServiceWithNullServiceResponse() {
        when(this.serviceRequestRepository.fetchResult((String) any(), (Object) any())).thenReturn(null);
        when(this.fiscalEventPostProcessorConfig.getIfixMasterGovernmentSearchPath())
                .thenReturn("Ifix Master Government Search Path");
        when(this.fiscalEventPostProcessorConfig.getIfixMasterGovernmentContextPath())
                .thenReturn("Ifix Master Government Context Path");
        when(this.fiscalEventPostProcessorConfig.getIfixMasterGovernmentHost()).thenReturn("localhost");

        assertThrows(CustomException.class,
                () -> this.governmentUtil.getGovernmentFromGovernmentService(fiscalEventRequest));
        verify(this.serviceRequestRepository).fetchResult((String) any(), (Object) any());
        verify(this.fiscalEventPostProcessorConfig).getIfixMasterGovernmentContextPath();
        verify(this.fiscalEventPostProcessorConfig).getIfixMasterGovernmentHost();
        verify(this.fiscalEventPostProcessorConfig).getIfixMasterGovernmentSearchPath();
    }

    @Test
    void testGetGovernmentFromGovernmentServiceWithEmptyTenantId() {
        when(this.serviceRequestRepository.fetchResult((String) any(), (Object) any())).thenReturn("Fetch Result");
        when(this.fiscalEventPostProcessorConfig.getIfixMasterGovernmentSearchPath())
                .thenReturn("Ifix Master Government Search Path");
        when(this.fiscalEventPostProcessorConfig.getIfixMasterGovernmentContextPath())
                .thenReturn("Ifix Master Government Context Path");
        when(this.fiscalEventPostProcessorConfig.getIfixMasterGovernmentHost()).thenReturn("localhost");
        FiscalEvent fiscalEvent = fiscalEventRequest.getFiscalEvent();
        fiscalEvent.setTenantId("");
        assertTrue(this.governmentUtil.getGovernmentFromGovernmentService(fiscalEventRequest).isEmpty());
    }

    @Test
    void testGetGovernmentFromGovernmentServiceWithValidResponseFromServiceRepo() {
        Map<String, Object> response = objectMapper.convertValue(govJsonNode, new TypeReference<Map<String, Object>>() {
        });
        when(this.serviceRequestRepository.fetchResult((String) any(), (Object) any())).thenReturn(response);
        when(this.fiscalEventPostProcessorConfig.getIfixMasterGovernmentSearchPath())
                .thenReturn("Ifix Master Government Search Path");
        when(this.fiscalEventPostProcessorConfig.getIfixMasterGovernmentContextPath())
                .thenReturn("Ifix Master Government Context Path");
        when(this.fiscalEventPostProcessorConfig.getIfixMasterGovernmentHost()).thenReturn("localhost");

        List<Government> governmentList = governmentUtil.getGovernmentFromGovernmentService(fiscalEventRequest);

        assertNotNull(governmentList);
        assertFalse(governmentList.isEmpty());

        verify(this.serviceRequestRepository).fetchResult((String) any(), (Object) any());
        verify(this.fiscalEventPostProcessorConfig).getIfixMasterGovernmentContextPath();
        verify(this.fiscalEventPostProcessorConfig).getIfixMasterGovernmentHost();
        verify(this.fiscalEventPostProcessorConfig).getIfixMasterGovernmentSearchPath();
    }
}

