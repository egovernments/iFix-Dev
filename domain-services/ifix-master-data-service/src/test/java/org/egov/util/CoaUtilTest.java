package org.egov.util;

import org.egov.common.contract.request.RequestHeader;
import org.egov.config.MasterDataServiceConfiguration;
import org.egov.config.TestDataFormatter;
import org.egov.repository.ServiceRequestRepository;
import org.egov.web.models.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doReturn;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
class CoaUtilTest {

    @InjectMocks
    private CoaUtil coaUtil;

    @Mock
    private ServiceRequestRepository serviceRequestRepository;

    @Mock
    private MasterDataServiceConfiguration mdsConfiguration;

    private GovernmentResponse governmentCreateResponse;
    private COASearchRequest coaSearchRequest;
    private COARequest coaRequest;

    @Autowired
    private TestDataFormatter testDataFormatter;

    @BeforeAll
    void init() throws IOException {
        governmentCreateResponse = testDataFormatter.getGovernmentCreateResponseData();
        coaSearchRequest = testDataFormatter.getCoaSearchRequestData();
        coaRequest = testDataFormatter.getCoaRequestData();
    }

    @Test
    void searchTenants() {
        ChartOfAccount chartOfAccount = coaRequest.getChartOfAccount();
        RequestHeader requestHeader = coaSearchRequest.getRequestHeader();

        LinkedHashMap<String, List<Government>> listLinkedHashMap = new LinkedHashMap<>();
        listLinkedHashMap.put("government", governmentCreateResponse.getGovernment());

        doReturn(listLinkedHashMap).when(serviceRequestRepository).fetchResult((String) any(), (GovernmentSearchRequest) any());

        List<Government> actualResult = coaUtil.searchTenants(requestHeader, chartOfAccount);

        assertNotNull(actualResult);
        assertTrue(actualResult.size() > 0);
    }

    @Test
    void searchTenantInvalid() {
        ChartOfAccount chartOfAccount = coaRequest.getChartOfAccount();
        RequestHeader requestHeader = coaSearchRequest.getRequestHeader();
        chartOfAccount.setTenantId("Asia");

        doReturn(null).when(serviceRequestRepository).fetchResult((String) any(), (GovernmentSearchRequest) any());

        List<Government> actualResult = coaUtil.searchTenants(requestHeader, chartOfAccount);

        assertNotNull(actualResult);
        assertEquals(0, actualResult.size());
    }

    @Test
    void searchTenantNull() {
        ChartOfAccount chartOfAccount = coaRequest.getChartOfAccount();
        RequestHeader requestHeader = coaSearchRequest.getRequestHeader();
        chartOfAccount.setTenantId(null);

        doReturn(null).when(serviceRequestRepository).fetchResult((String) any(), (GovernmentSearchRequest) any());

        List<Government> actualResult = coaUtil.searchTenants(requestHeader, chartOfAccount);

        assertNotNull(actualResult);
        assertEquals(0, actualResult.size());
    }
}