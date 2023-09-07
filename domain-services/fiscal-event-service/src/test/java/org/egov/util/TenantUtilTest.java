package org.egov.util;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONArray;
import org.egov.FiscalApplicationMain;
import org.egov.common.contract.request.RequestHeader;
import org.egov.config.FiscalEventConfiguration;
import org.egov.config.TestDataFormatter;
import org.egov.mdms.model.MdmsResponse;
import org.egov.mdms.service.MdmsClientService;
import org.egov.repository.ServiceRequestRepository;
import org.egov.tracer.model.CustomException;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(classes = FiscalApplicationMain.class)
@Slf4j
class TenantUtilTest {

    @Autowired
    private TestDataFormatter testDataFormatter;

    @Spy
    private ObjectMapper objectMapper;

    @InjectMocks
    private TenantUtil tenantUtil;

    @Mock
    private FiscalEventConfiguration fiscalEventConfiguration;
    @Mock
    private ServiceRequestRepository serviceRequestRepository;
    @Mock
    private MdmsClientService mdmsClientService;

    private FiscalEventRequest fiscalEventRequest;
    private JsonNode validGovernmentSearchResult;

    @BeforeAll
    public void init() throws IOException {
        fiscalEventRequest = testDataFormatter.getFiscalEventPushRequestData();
        validGovernmentSearchResult = testDataFormatter.getValidGovernmentSearchResponse();
    }

    @Test
    void testNullTenant() {
        RequestHeader requestHeader = fiscalEventRequest.getRequestHeader();
        assertFalse(tenantUtil.validateTenant(null, requestHeader));
    }

    @Test
    void testValidateTenantWithEmptyTenantIds() {
        ArrayList<String> tenantIds = new ArrayList<String>();
        assertFalse(this.tenantUtil.validateTenant(tenantIds, new RequestHeader()));
    }

    @Test
    void testValidTenant() {
        RequestHeader requestHeader = fiscalEventRequest.getRequestHeader();
        MdmsResponse mdmsResponse = objectMapper.convertValue(validGovernmentSearchResult, MdmsResponse.class);
        doReturn(mdmsResponse).when(mdmsClientService).getMaster(any());
        List<String> tenantIds = new ArrayList<>();
        tenantIds.add("pb");
        assertTrue(tenantUtil.validateTenant(tenantIds, requestHeader));
    }

    @Test
    void testInvalidTenant() {
        RequestHeader requestHeader = fiscalEventRequest.getRequestHeader();
        MdmsResponse mdmsResponse = objectMapper.convertValue(validGovernmentSearchResult, MdmsResponse.class);
        doReturn(mdmsResponse).when(mdmsClientService).getMaster(any());
        List<String> tenantIds = new ArrayList<>();
        tenantIds.add("ab");
        assertFalse(tenantUtil.validateTenant(tenantIds, requestHeader));
    }

}
