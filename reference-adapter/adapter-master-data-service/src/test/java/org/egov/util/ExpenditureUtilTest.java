package org.egov.util;

import org.egov.common.contract.request.RequestHeader;
import org.egov.config.MasterDataServiceConfiguration;
import org.egov.config.TestDataFormatter;
import org.egov.repository.ServiceRequestRepository;
import org.egov.tracer.model.CustomException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
//@SpringBootTest
class ExpenditureUtilTest {

   /* @Autowired
    private TestDataFormatter testDataFormatter;

    @Mock
    private MasterDataServiceConfiguration configuration;

    @Mock
    private ServiceRequestRepository searchRequestRepository;

    @InjectMocks
    private ExpenditureUtil expenditureUtil;

    private ArrayList<String> idList;
    private RequestHeader requestHeader = new RequestHeader();
    private Object expenditureSearchResponse;

    @BeforeAll
    public void init() throws IOException {
        idList = new ArrayList<>();
        idList.add("DEPARTME-NT0B-CDEF-FEDC-ENTITY0UID10");

        expenditureSearchResponse = testDataFormatter.getExpenditureSearchResponseDataAsObject();

        doReturn(new String()).when(configuration).getIfixMasterExpenditureHost();
        doReturn(new String()).when(configuration).getIfixMasterExpenditureContextPath();
        doReturn(new String()).when(configuration).getIfixMasterExpenditureSearchPath();

    }

    @Test
    void testValidateExpenditure() {
        doReturn(expenditureSearchResponse).when(searchRequestRepository).fetchResult((String) any(), any());

        assertTrue(expenditureUtil.validateExpenditure("pb", idList, requestHeader));
    }

    @Test
    void testFalseValidateExpenditure() {
        assertFalse(expenditureUtil.validateExpenditure(null, new ArrayList(), requestHeader));
    }

    @Test
    void testValidateExpenditureException() {
        doReturn(new Object()).when(searchRequestRepository).fetchResult((String) any(), any());

        assertThrows(CustomException.class,
                () -> expenditureUtil.validateExpenditure("pb", idList, requestHeader));
    }*/
}

