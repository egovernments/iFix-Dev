package org.egov.validator;

import com.google.gson.Gson;
import org.egov.common.contract.AuditDetails;
import org.egov.common.contract.request.RequestHeader;
import org.egov.config.TestDataFormatter;
import org.egov.repository.FiscalEventRepository;
import org.egov.tracer.model.CustomException;
import org.egov.util.CoaUtil;
import org.egov.util.ProjectUtil;
import org.egov.util.TenantUtil;
import org.egov.web.models.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
class FiscalEventValidatorTest {

    @Autowired
    private TestDataFormatter testDataFormatter;

    @Mock
    private CoaUtil coaUtil;

    @Mock
    private FiscalEventRepository fiscalEventRepository;

    @InjectMocks
    private FiscalEventValidator fiscalEventValidator;

    @Mock
    private ProjectUtil projectUtil;

    @Mock
    private TenantUtil tenantUtil;

    private FiscalEventGetRequest fiscalEventGetRequest;
    private FiscalEventResponse fiscalEventSearchResponse;
    private String fiscalEventSearchData;
    private FiscalEventRequest fiscalEventRequest;
    private FiscalEventResponse fiscalEventPushResponse;
    private String fiscalEventPushData;

    @BeforeAll
    void init() throws IOException {
        fiscalEventGetRequest = testDataFormatter.getFiscalEventSearchRequestData();
        fiscalEventSearchData = new Gson().toJson(fiscalEventGetRequest);
        fiscalEventSearchResponse = testDataFormatter.getFiscalEventSearchResponseData();
        fiscalEventRequest = testDataFormatter.getFiscalEventPushRequestData();
        fiscalEventPushData = new Gson().toJson(fiscalEventRequest);
        fiscalEventPushResponse = testDataFormatter.getFiscalEventPushResponseData();
    }

    @Test
    void testValidateFiscalEventPushPostWithDefaultFiscalEventRequest() {
        assertThrows(CustomException.class,
                () -> this.fiscalEventValidator.validateFiscalEventPushPost(new FiscalEventRequest()));
        assertThrows(CustomException.class, () -> this.fiscalEventValidator.validateFiscalEventPushPost(null));
    }

    @Test
    void testValidateFiscalEventPushPostWithDefaultRequestHeader() {
        RequestHeader requestHeader = new RequestHeader();
        assertThrows(CustomException.class, () -> this.fiscalEventValidator
                .validateFiscalEventPushPost(new FiscalEventRequest(requestHeader, new FiscalEvent())));
    }

    @Test
    void testValidateFiscalEventPushPostWithNullUserInfo() {
        FiscalEventRequest fiscalEventRequest = mock(FiscalEventRequest.class);
        when(fiscalEventRequest.getFiscalEvent()).thenReturn(new FiscalEvent());
        when(fiscalEventRequest.getRequestHeader()).thenReturn(new RequestHeader());
        assertThrows(CustomException.class,
                () -> this.fiscalEventValidator.validateFiscalEventPushPost(fiscalEventRequest));
        verify(fiscalEventRequest, atLeast(1)).getFiscalEvent();
        verify(fiscalEventRequest, atLeast(1)).getRequestHeader();
    }

    @Test
    void testValidateFiscalEventPushPostWithNullFiscalEvent() {
        FiscalEventRequest fiscalEventRequest = mock(FiscalEventRequest.class);
        when(fiscalEventRequest.getFiscalEvent()).thenReturn(null);
        when(fiscalEventRequest.getRequestHeader()).thenReturn(new RequestHeader());
        assertThrows(CustomException.class,
                () -> this.fiscalEventValidator.validateFiscalEventPushPost(fiscalEventRequest));
        verify(fiscalEventRequest).getFiscalEvent();
        verify(fiscalEventRequest).getRequestHeader();
    }

    @Test
    void testValidateFiscalEventPushPostValidationException() {
        FiscalEventRequest fiscalEventRequest1 = mock(FiscalEventRequest.class);

        when(fiscalEventRequest1.getFiscalEvent()).thenReturn(fiscalEventRequest.getFiscalEvent());
        when(fiscalEventRequest1.getRequestHeader()).thenReturn(fiscalEventRequest.getRequestHeader());

        assertThrows(CustomException.class,
                () -> this.fiscalEventValidator.validateFiscalEventPushPost(fiscalEventRequest1));

        verify(fiscalEventRequest1, atLeast(1)).getFiscalEvent();
        verify(fiscalEventRequest1, atLeast(1)).getRequestHeader();
    }

    @Test
    void testValidateFiscalEventPushPostMissingProjectId() {
        fiscalEventRequest.getFiscalEvent().setProjectId(null);
        assertThrows(CustomException.class,
                () -> this.fiscalEventValidator.validateFiscalEventPushPost(fiscalEventRequest));
    }


    //search validation
    @Test
    void testValidateFiscalEventSearchPostWithNullHeader() {
        assertThrows(CustomException.class,
                () -> this.fiscalEventValidator.validateFiscalEventSearchPost(new FiscalEventGetRequest()));
    }

    @Test
    void testValidateFiscalEventSearchPostWithNullUserInfo() {
        RequestHeader requestHeader = new RequestHeader();
        assertThrows(CustomException.class, () -> this.fiscalEventValidator
                .validateFiscalEventSearchPost(new FiscalEventGetRequest(requestHeader, new Criteria())));
    }

    @Test
    void testValidateFiscalEventSearchPostWithNullUserId() {
        FiscalEventGetRequest fiscalEventGetRequest1 = mock(FiscalEventGetRequest.class);
        when(fiscalEventGetRequest1.getCriteria()).thenReturn(new Criteria());
        RequestHeader requestHeader = fiscalEventGetRequest.getRequestHeader();
        requestHeader.getUserInfo().setUuid(null);
        when(fiscalEventGetRequest1.getRequestHeader())
                .thenReturn(requestHeader);
        assertThrows(CustomException.class,
                () -> this.fiscalEventValidator.validateFiscalEventSearchPost(fiscalEventGetRequest1));
        verify(fiscalEventGetRequest1).getCriteria();
        verify(fiscalEventGetRequest1).getRequestHeader();
    }

    @Test
    void testValidateFiscalEventSearchPostWithNullCriteria() {
        RequestHeader requestHeader = fiscalEventGetRequest.getRequestHeader();
        FiscalEventGetRequest fiscalEventGetRequest = mock(FiscalEventGetRequest.class);
        when(fiscalEventGetRequest.getCriteria()).thenReturn(null);
        when(fiscalEventGetRequest.getRequestHeader()).thenReturn(requestHeader);
        assertThrows(CustomException.class,
                () -> this.fiscalEventValidator.validateFiscalEventSearchPost(fiscalEventGetRequest));
        verify(fiscalEventGetRequest).getCriteria();
        verify(fiscalEventGetRequest).getRequestHeader();
    }

    @Test
    void testValidateFiscalEventSearchPostWithMissingMandatoryField() {
        RequestHeader requestHeader = fiscalEventGetRequest.getRequestHeader();
        FiscalEventGetRequest fiscalEventGetRequest = mock(FiscalEventGetRequest.class);
        when(fiscalEventGetRequest.getCriteria()).thenReturn(new Criteria());
        when(fiscalEventGetRequest.getRequestHeader()).thenReturn(requestHeader);
        assertThrows(CustomException.class,
                () -> this.fiscalEventValidator.validateFiscalEventSearchPost(fiscalEventGetRequest));
        verify(fiscalEventGetRequest).getCriteria();
        verify(fiscalEventGetRequest).getRequestHeader();
    }

    @Test
    void testValidateFiscalEventSearchPostWithInvalidTenant() {
        RequestHeader requestHeader = fiscalEventGetRequest.getRequestHeader();

        Criteria criteria = fiscalEventGetRequest.getCriteria();
        criteria.setTenantId("TENANT_ID");
        FiscalEventGetRequest fiscalEventGetRequest = mock(FiscalEventGetRequest.class);
        when(fiscalEventGetRequest.getCriteria()).thenReturn(criteria);
        when(fiscalEventGetRequest.getRequestHeader()).thenReturn(requestHeader);
        assertThrows(CustomException.class,
                () -> this.fiscalEventValidator.validateFiscalEventSearchPost(fiscalEventGetRequest));
        verify(fiscalEventGetRequest).getCriteria();
        verify(fiscalEventGetRequest).getRequestHeader();
    }

    @Test
    void testValidateFiscalEventSearchPostWithoutToTime() {
        when(this.tenantUtil.validateTenant((String) any(), (RequestHeader) any())).thenReturn(true);
        RequestHeader requestHeader = fiscalEventGetRequest.getRequestHeader();

        Criteria criteria = fiscalEventGetRequest.getCriteria();
        criteria.setToEventTime(null);
        FiscalEventGetRequest fiscalEventGetRequest = mock(FiscalEventGetRequest.class);
        when(fiscalEventGetRequest.getCriteria()).thenReturn(criteria);
        when(fiscalEventGetRequest.getRequestHeader()).thenReturn(requestHeader);
        assertThrows(CustomException.class,
                () -> this.fiscalEventValidator.validateFiscalEventSearchPost(fiscalEventGetRequest));
        verify(fiscalEventGetRequest).getCriteria();
        verify(fiscalEventGetRequest).getRequestHeader();
    }

    @Test
    void testValidateFiscalEventSearchPostWithoutFromEventTime() {
        when(this.tenantUtil.validateTenant((String) any(), (RequestHeader) any())).thenReturn(true);
        RequestHeader requestHeader = fiscalEventGetRequest.getRequestHeader();

        Criteria criteria = fiscalEventGetRequest.getCriteria();
        criteria.setFromEventTime(null);
        FiscalEventGetRequest fiscalEventGetRequest = mock(FiscalEventGetRequest.class);
        when(fiscalEventGetRequest.getCriteria()).thenReturn(criteria);
        when(fiscalEventGetRequest.getRequestHeader()).thenReturn(requestHeader);
        assertThrows(CustomException.class,
                () -> this.fiscalEventValidator.validateFiscalEventSearchPost(fiscalEventGetRequest));
        verify(fiscalEventGetRequest).getCriteria();
        verify(fiscalEventGetRequest).getRequestHeader();
    }
}

