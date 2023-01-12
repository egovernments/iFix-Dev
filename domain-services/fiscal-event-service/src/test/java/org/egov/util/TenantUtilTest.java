//package org.egov.util;
//
//
//import com.fasterxml.jackson.core.type.TypeReference;
//import com.fasterxml.jackson.databind.JsonNode;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import lombok.extern.slf4j.Slf4j;
//import org.egov.common.contract.request.RequestHeader;
//import org.egov.config.FiscalEventConfiguration;
//import org.egov.config.TestDataFormatter;
//import org.egov.repository.ServiceRequestRepository;
//import org.egov.tracer.model.CustomException;
//import org.egov.web.models.FiscalEventRequest;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.TestInstance;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
//@SpringBootTest
//@Slf4j
//class TenantUtilTest {
//
//    @Autowired
//    private TestDataFormatter testDataFormatter;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @InjectMocks
//    private TenantUtil tenantUtil;
//
//    @Mock
//    private FiscalEventConfiguration fiscalEventConfiguration;
//    @Mock
//    private ServiceRequestRepository serviceRequestRepository;
//
//    private FiscalEventRequest fiscalEventRequest;
//    private JsonNode validGovernmentSearchResult;
//    private JsonNode emptyGovernmentSearchResult;
//
//    @BeforeAll
//    public void init() throws IOException {
//        fiscalEventRequest = testDataFormatter.getFiscalEventPushRequestData();
//        validGovernmentSearchResult = testDataFormatter.getValidGovernmentSearchResponse();
//        emptyGovernmentSearchResult = testDataFormatter.getEmptyGovernmentSearchResponse();
//    }
//
//    @Test
//    void testValidateTenantWithEmptyTenantIds() {
//        ArrayList<String> tenantIds = new ArrayList<String>();
//        assertFalse(this.tenantUtil.validateTenant(tenantIds, new RequestHeader()));
//    }
//
//    @Test
//    void testValidateTenantWithResultFromServiceReqRepo() {
//        when(this.serviceRequestRepository.fetchResult((String) any(), (Object) any())).thenReturn("Fetch Result");
//        when(this.fiscalEventConfiguration.getIfixMasterGovernmentSearchPath())
//                .thenReturn("Ifix Master Government Search Path");
//        when(this.fiscalEventConfiguration.getIfixMasterGovernmentContextPath())
//                .thenReturn("Ifix Master Government Context Path");
//        when(this.fiscalEventConfiguration.getIfixMasterGovernmentHost()).thenReturn("localhost");
//
//        ArrayList<String> stringList = new ArrayList<String>();
//        stringList.add("pb");
//        RequestHeader requestHeader = new RequestHeader();
//        assertThrows(CustomException.class, () -> this.tenantUtil.validateTenant(stringList, requestHeader));
//        verify(this.serviceRequestRepository).fetchResult((String) any(), (Object) any());
//        verify(this.fiscalEventConfiguration).getIfixMasterGovernmentContextPath();
//        verify(this.fiscalEventConfiguration).getIfixMasterGovernmentHost();
//        verify(this.fiscalEventConfiguration).getIfixMasterGovernmentSearchPath();
//    }
//
//    @Test
//    void testValidateTenantWithNullResultFromServiceReqRepo() {
//        when(this.serviceRequestRepository.fetchResult((String) any(), (Object) any())).thenReturn(null);
//        when(this.fiscalEventConfiguration.getIfixMasterGovernmentSearchPath())
//                .thenReturn("Ifix Master Government Search Path");
//        when(this.fiscalEventConfiguration.getIfixMasterGovernmentContextPath())
//                .thenReturn("Ifix Master Government Context Path");
//        when(this.fiscalEventConfiguration.getIfixMasterGovernmentHost()).thenReturn("localhost");
//
//        ArrayList<String> stringList = new ArrayList<String>();
//        stringList.add("pb");
//        RequestHeader requestHeader = new RequestHeader();
//        assertThrows(CustomException.class, () -> this.tenantUtil.validateTenant(stringList, requestHeader));
//        verify(this.serviceRequestRepository).fetchResult((String) any(), (Object) any());
//        verify(this.fiscalEventConfiguration).getIfixMasterGovernmentContextPath();
//        verify(this.fiscalEventConfiguration).getIfixMasterGovernmentHost();
//        verify(this.fiscalEventConfiguration).getIfixMasterGovernmentSearchPath();
//    }
//
//    @Test
//    void testValidateTenantWithNullHeader() {
//        when(this.serviceRequestRepository.fetchResult((String) any(), (Object) any())).thenReturn("Fetch Result");
//        when(this.fiscalEventConfiguration.getIfixMasterGovernmentSearchPath())
//                .thenReturn("Ifix Master Government Search Path");
//        when(this.fiscalEventConfiguration.getIfixMasterGovernmentContextPath())
//                .thenReturn("Ifix Master Government Context Path");
//        when(this.fiscalEventConfiguration.getIfixMasterGovernmentHost()).thenReturn("localhost");
//
//        ArrayList<String> stringList = new ArrayList<String>();
//        stringList.add("foo");
//        assertFalse(this.tenantUtil.validateTenant(stringList, null));
//    }
//
//    @Test
//    void testValidTenant() {
//        RequestHeader requestHeader = fiscalEventRequest.getRequestHeader();
//        Map<String, Object> map = objectMapper.convertValue(validGovernmentSearchResult, new TypeReference<Map<String, Object>>() {
//        });
//        doReturn(map).when(serviceRequestRepository).fetchResult(any(), any());
//        List<String> tenantIds = new ArrayList<>();
//        tenantIds.add("pb");
//        assertTrue(tenantUtil.validateTenant(tenantIds, requestHeader));
//    }
//
//    @Test
//    void testInvalidTenant() {
//        RequestHeader requestHeader = fiscalEventRequest.getRequestHeader();
//        Map<String, Object> map = objectMapper.convertValue(emptyGovernmentSearchResult, new TypeReference<Map<String, Object>>() {
//        });
//        doReturn(map).when(serviceRequestRepository).fetchResult(any(), any());
//        List<String> tenantIds = new ArrayList<>();
//        tenantIds.add("pb");
//        assertFalse(tenantUtil.validateTenant(tenantIds, requestHeader));
//    }
//
//    @Test
//    void testNullTenant() {
//        RequestHeader requestHeader = fiscalEventRequest.getRequestHeader();
//        doReturn(null).when(serviceRequestRepository).fetchResult((String) any(), (RequestHeader) any());
//        assertFalse(tenantUtil.validateTenant(null, requestHeader));
//    }
//
//}
