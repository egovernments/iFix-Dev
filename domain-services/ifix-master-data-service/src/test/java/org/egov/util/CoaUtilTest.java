package org.egov.util;

import org.egov.common.contract.request.RequestHeader;
import org.egov.common.contract.request.UserInfo;
import org.egov.config.MasterDataServiceConfiguration;
import org.egov.config.TestDataFormatter;
import org.egov.repository.ServiceRequestRepository;
import org.egov.web.models.ChartOfAccount;
import org.egov.web.models.Government;
import org.egov.web.models.GovernmentResponse;
import org.egov.web.models.GovernmentSearchRequest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

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

    @Autowired
    private TestDataFormatter testDataFormatter;

    @BeforeAll
    void init() throws IOException {
        governmentCreateResponse = testDataFormatter.getGovernmentCreateResponseData();
    }

    @Test
    void searchTenants() {
        ChartOfAccount chartOfAccount = mock(ChartOfAccount.class);
        chartOfAccount.setTenantId("pb");
        RequestHeader requestHeader = mock(RequestHeader.class);
        when(requestHeader.getUserInfo()).thenReturn(new UserInfo());

        LinkedHashMap<String, List<Government>> listLinkedHashMap = new LinkedHashMap<>();
        listLinkedHashMap.put("government", governmentCreateResponse.getGovernment());

        doReturn(listLinkedHashMap).when(serviceRequestRepository).fetchResult((String) any(), (GovernmentSearchRequest) any());

        List<Government> actualResult = coaUtil.searchTenants(requestHeader, chartOfAccount);

        assertNotNull(actualResult);
        assertTrue(actualResult.size() > 0);
    }
}