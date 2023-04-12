package org.egov.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.egov.MasterApplicationMain;
import org.egov.common.contract.request.RequestHeader;
import org.egov.config.MasterDataServiceConfiguration;
import org.egov.config.TestDataFormatter;
import org.egov.mdms.model.MdmsResponse;
import org.egov.mdms.service.MdmsClientService;
import org.egov.repository.ServiceRequestRepository;
import org.egov.web.models.*;
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
import java.util.LinkedHashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doReturn;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(classes = MasterApplicationMain.class)
class CoaUtilTest {

    @InjectMocks
    private CoaUtil coaUtil;

    @Mock
    private ServiceRequestRepository serviceRequestRepository;

    @Mock
    private MasterDataServiceConfiguration mdsConfiguration;
    @Mock
    private MdmsClientService mdmsClientService;

    @Spy
    private ObjectMapper objectMapper;

    private COASearchRequest coaSearchRequest;
    private COARequest coaRequest;
    private JsonNode validGovernmentSearchResult;

    @Autowired
    private TestDataFormatter testDataFormatter;

    @BeforeAll
    void init() throws IOException {
        coaSearchRequest = testDataFormatter.getCoaSearchRequestData();
        coaRequest = testDataFormatter.getCoaRequestData();
        validGovernmentSearchResult = testDataFormatter.getValidGovernmentSearchResponse();
    }

    @Test
    void testNullTenant() {
        RequestHeader requestHeader = coaRequest.getRequestHeader();
        assertFalse(coaUtil.validateTenant(null, requestHeader));
    }

    @Test
    void testValidTenant() {
        RequestHeader requestHeader = coaRequest.getRequestHeader();
        MdmsResponse mdmsResponse = objectMapper.convertValue(validGovernmentSearchResult, MdmsResponse.class);
        doReturn(mdmsResponse).when(mdmsClientService).getMaster(any());
        assertTrue(coaUtil.validateTenant("pb", requestHeader));
    }

    @Test
    void testInvalidTenant() {
        RequestHeader requestHeader = coaRequest.getRequestHeader();
        MdmsResponse mdmsResponse = objectMapper.convertValue(validGovernmentSearchResult, MdmsResponse.class);
        doReturn(mdmsResponse).when(mdmsClientService).getMaster(any());
        assertFalse(coaUtil.validateTenant("ab", requestHeader));
    }

}