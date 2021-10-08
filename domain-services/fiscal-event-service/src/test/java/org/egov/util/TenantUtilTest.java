package org.egov.util;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doReturn;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.request.RequestHeader;
import org.egov.config.FiscalEventConfiguration;
import org.egov.config.TestDataFormatter;
import org.egov.repository.ServiceRequestRepository;
import org.egov.web.models.FiscalEventRequest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
@Slf4j
class TenantUtilTest {

    @Autowired
    private TestDataFormatter testDataFormatter;

    @Autowired
    private ObjectMapper objectMapper;

    @InjectMocks
    private TenantUtil tenantUtil;

    @Mock
    private FiscalEventConfiguration fiscalEventConfiguration;
    @Mock
    private ServiceRequestRepository serviceRequestRepository;

    private FiscalEventRequest fiscalEventRequest;
    private JsonNode validGovernmentSearchResult;
    private JsonNode emptyGovernmentSearchResult;

    @BeforeAll
    public void init() throws IOException {
        fiscalEventRequest = testDataFormatter.getFiscalEventPushRequestData();
        validGovernmentSearchResult = testDataFormatter.getValidGovernmentSearchResponse();
        emptyGovernmentSearchResult = testDataFormatter.getEmptyGovernmentSearchResponse();
    }

    @Test
    void testValidTenant() {
        RequestHeader requestHeader = fiscalEventRequest.getRequestHeader();
        Map<String, Object> map = objectMapper.convertValue(validGovernmentSearchResult, new TypeReference<Map<String, Object>>() {});
        doReturn(map).when(serviceRequestRepository).fetchResult(any(), any());
        assertTrue(tenantUtil.validateTenant("pb", requestHeader));
    }

    @Test
    void testInvalidTenant() {
        RequestHeader requestHeader = fiscalEventRequest.getRequestHeader();
        Map<String, Object> map = objectMapper.convertValue(emptyGovernmentSearchResult, new TypeReference<Map<String, Object>>() {});
        doReturn(map).when(serviceRequestRepository).fetchResult(any(), any());
        assertFalse(tenantUtil.validateTenant("ab", requestHeader));
    }

    @Test
    void testNullTenant() {
        RequestHeader requestHeader = fiscalEventRequest.getRequestHeader();
        doReturn(null).when(serviceRequestRepository).fetchResult((String) any(), (RequestHeader) any());
        assertFalse(tenantUtil.validateTenant(null, requestHeader));
    }

}
