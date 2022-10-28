package org.egov.validator;

import com.google.gson.Gson;
import org.egov.common.contract.request.RequestHeader;
import org.egov.common.contract.request.UserInfo;
import org.egov.config.FiscalEventConfiguration;
import org.egov.config.TestDataFormatter;
import org.egov.repository.FiscalEventRepository;
import org.egov.tracer.model.CustomException;
import org.egov.util.CoaUtil;
import org.egov.util.FiscalEventMapperUtil;
import org.egov.util.TenantUtil;
import org.egov.web.models.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
class FiscalEventValidatorTest {

    @MockBean
    private FiscalEventMapperUtil fiscalEventMapperUtil;
    @Autowired
    private TestDataFormatter testDataFormatter;

    @Mock
    private CoaUtil coaUtil;

    @Mock
    private FiscalEventRepository fiscalEventRepository;

    @InjectMocks
    private FiscalEventValidator fiscalEventValidator;

    @Mock
    private FiscalEventConfiguration fiscalEventConfiguration;

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
        FiscalEventRequest fiscalEventRequest = new FiscalEventRequest();
        assertThrows(CustomException.class,
                () -> this.fiscalEventValidator.validateFiscalEventPushPost(fiscalEventRequest));
        assertThrows(CustomException.class, () -> this.fiscalEventValidator.validateFiscalEventPushPost(null));
    }

    @Test
    void testValidateFiscalEventPushPostWithDefaultRequestHeader() {
        RequestHeader requestHeader = new RequestHeader();
        List<FiscalEvent> fiscalEvents = new ArrayList<>();
        fiscalEvents.add(new FiscalEvent());
        when(fiscalEventConfiguration.getFiscalEventPushReqMaxSize()).thenReturn("10");
        FiscalEventRequest fiscalEventRequest = new FiscalEventRequest(requestHeader, fiscalEvents);
        assertThrows(CustomException.class, () -> this.fiscalEventValidator
                .validateFiscalEventPushPost(fiscalEventRequest));
    }

    @Test
    void testValidateFiscalEventPushPostWithNullUserInfo() {
        FiscalEventRequest fiscalEventRequest = mock(FiscalEventRequest.class);
        List<FiscalEvent> fiscalEvents = new ArrayList<>();
        fiscalEvents.add(new FiscalEvent());
        when(fiscalEventRequest.getFiscalEvent()).thenReturn(fiscalEvents);
        when(fiscalEventRequest.getRequestHeader()).thenReturn(new RequestHeader());
        when(fiscalEventConfiguration.getFiscalEventPushReqMaxSize()).thenReturn("10");

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
        when(fiscalEventConfiguration.getFiscalEventPushReqMaxSize()).thenReturn("10");
        assertThrows(CustomException.class,
                () -> this.fiscalEventValidator.validateFiscalEventPushPost(fiscalEventRequest1));

        verify(fiscalEventRequest1, atLeast(1)).getFiscalEvent();
        verify(fiscalEventRequest1, atLeast(1)).getRequestHeader();
    }

    @Test
    void testValidateFiscalEventPushPost() {
        FiscalEventRequest fiscalEventRequest = new FiscalEventRequest();
        assertThrows(CustomException.class,
                () -> this.fiscalEventValidator.validateFiscalEventPushPost(fiscalEventRequest));
        assertThrows(CustomException.class, () -> this.fiscalEventValidator.validateFiscalEventPushPost(null));
    }

    @Test
    void testValidateFiscalEventPushPost2() {
        RequestHeader requestHeader = new RequestHeader();
        ArrayList<FiscalEvent> fiscalEvents = new ArrayList<FiscalEvent>();
        FiscalEventRequest fiscalEventRequest = new FiscalEventRequest(requestHeader, fiscalEvents);
        assertThrows(CustomException.class, () -> this.fiscalEventValidator
                .validateFiscalEventPushPost(fiscalEventRequest));
    }

    @Test
    void testValidateFiscalEventPushPost3() {
        FiscalEventRequest fiscalEventRequest = mock(FiscalEventRequest.class);
        when(fiscalEventRequest.getFiscalEvent()).thenReturn(new ArrayList<FiscalEvent>());
        when(fiscalEventRequest.getRequestHeader()).thenReturn(new RequestHeader());
        assertThrows(CustomException.class,
                () -> this.fiscalEventValidator.validateFiscalEventPushPost(fiscalEventRequest));
        verify(fiscalEventRequest, atLeast(1)).getFiscalEvent();
        verify(fiscalEventRequest).getRequestHeader();
    }

    @Test
    void testValidateFiscalEventPushPost4() {
        when(this.fiscalEventConfiguration.getFiscalEventPushReqMaxSize()).thenReturn(null);

        ArrayList<FiscalEvent> fiscalEventList = new ArrayList<FiscalEvent>();
        fiscalEventList.add(new FiscalEvent());
        FiscalEventRequest fiscalEventRequest = mock(FiscalEventRequest.class);
        when(fiscalEventRequest.getFiscalEvent()).thenReturn(fiscalEventList);
        when(fiscalEventRequest.getRequestHeader()).thenReturn(new RequestHeader());
        assertThrows(CustomException.class,
                () -> this.fiscalEventValidator.validateFiscalEventPushPost(fiscalEventRequest));
        verify(this.fiscalEventConfiguration).getFiscalEventPushReqMaxSize();
        verify(fiscalEventRequest, atLeast(1)).getFiscalEvent();
        verify(fiscalEventRequest, atLeast(1)).getRequestHeader();
    }

    @Test
    void testValidateFiscalEventPushPost5() {
        when(this.fiscalEventConfiguration.getFiscalEventPushReqMaxSize()).thenReturn("42");

        ArrayList<FiscalEvent> fiscalEventList = new ArrayList<FiscalEvent>();
        fiscalEventList.add(new FiscalEvent());
        FiscalEventRequest fiscalEventRequest = mock(FiscalEventRequest.class);
        when(fiscalEventRequest.getFiscalEvent()).thenReturn(fiscalEventList);
        when(fiscalEventRequest.getRequestHeader()).thenReturn(new RequestHeader());
        assertThrows(CustomException.class,
                () -> this.fiscalEventValidator.validateFiscalEventPushPost(fiscalEventRequest));
        verify(this.fiscalEventConfiguration, atLeast(1)).getFiscalEventPushReqMaxSize();
        verify(fiscalEventRequest, atLeast(1)).getFiscalEvent();
        verify(fiscalEventRequest, atLeast(1)).getRequestHeader();
    }

    @Test
    void testValidateFiscalEventPushPost6() {
        when(this.fiscalEventConfiguration.getFiscalEventPushReqMaxSize()).thenReturn("");

        ArrayList<FiscalEvent> fiscalEventList = new ArrayList<FiscalEvent>();
        fiscalEventList.add(new FiscalEvent());
        FiscalEventRequest fiscalEventRequest = mock(FiscalEventRequest.class);
        when(fiscalEventRequest.getFiscalEvent()).thenReturn(fiscalEventList);
        when(fiscalEventRequest.getRequestHeader()).thenReturn(new RequestHeader());
        assertThrows(CustomException.class,
                () -> this.fiscalEventValidator.validateFiscalEventPushPost(fiscalEventRequest));
        verify(this.fiscalEventConfiguration).getFiscalEventPushReqMaxSize();
        verify(fiscalEventRequest, atLeast(1)).getFiscalEvent();
        verify(fiscalEventRequest, atLeast(1)).getRequestHeader();
    }

    @Test
    void testValidateFiscalEventPushPost7() {
        when(this.fiscalEventConfiguration.getFiscalEventPushReqMaxSize()).thenReturn(null);

        ArrayList<FiscalEvent> fiscalEventList = new ArrayList<FiscalEvent>();
        ArrayList<Amount> amountDetails = new ArrayList<Amount>();
        fiscalEventList.add(new FiscalEvent());
        FiscalEventRequest fiscalEventRequest = mock(FiscalEventRequest.class);
        when(fiscalEventRequest.getFiscalEvent()).thenReturn(fiscalEventList);
        when(fiscalEventRequest.getRequestHeader()).thenReturn(new RequestHeader());
        assertThrows(CustomException.class,
                () -> this.fiscalEventValidator.validateFiscalEventPushPost(fiscalEventRequest));
        verify(this.fiscalEventConfiguration).getFiscalEventPushReqMaxSize();
        verify(fiscalEventRequest, atLeast(1)).getFiscalEvent();
        verify(fiscalEventRequest, atLeast(1)).getRequestHeader();
    }

    @Test
    void testValidateFiscalEventPushPost8() {
        when(this.fiscalEventConfiguration.getFiscalEventPushReqMaxSize()).thenReturn(null);
        FiscalEvent fiscalEvent = mock(FiscalEvent.class);
        when(fiscalEvent.getEventType()).thenReturn(FiscalEvent.EventTypeEnum.Sanction);
        when(fiscalEvent.getReferenceId()).thenReturn("42");

        ArrayList<FiscalEvent> fiscalEventList = new ArrayList<FiscalEvent>();
        fiscalEventList.add(fiscalEvent);
        FiscalEventRequest fiscalEventRequest = mock(FiscalEventRequest.class);
        when(fiscalEventRequest.getFiscalEvent()).thenReturn(fiscalEventList);
        when(fiscalEventRequest.getRequestHeader()).thenReturn(new RequestHeader());
        assertThrows(CustomException.class,
                () -> this.fiscalEventValidator.validateFiscalEventPushPost(fiscalEventRequest));
        verify(this.fiscalEventConfiguration).getFiscalEventPushReqMaxSize();
        verify(fiscalEventRequest, atLeast(1)).getFiscalEvent();
        verify(fiscalEventRequest, atLeast(1)).getRequestHeader();
        verify(fiscalEvent, atLeast(1)).getEventType();
        verify(fiscalEvent).getReferenceId();
    }

    @Test
    void testValidateFiscalEventPushPost9() {
        when(this.fiscalEventConfiguration.getFiscalEventPushReqMaxSize()).thenReturn(null);
        FiscalEvent fiscalEvent = mock(FiscalEvent.class);
        when(fiscalEvent.getEventType()).thenReturn(null);
        when(fiscalEvent.getReferenceId()).thenReturn("42");

        ArrayList<FiscalEvent> fiscalEventList = new ArrayList<FiscalEvent>();
        fiscalEventList.add(fiscalEvent);
        FiscalEventRequest fiscalEventRequest = mock(FiscalEventRequest.class);
        when(fiscalEventRequest.getFiscalEvent()).thenReturn(fiscalEventList);
        when(fiscalEventRequest.getRequestHeader()).thenReturn(new RequestHeader());
        assertThrows(CustomException.class,
                () -> this.fiscalEventValidator.validateFiscalEventPushPost(fiscalEventRequest));
        verify(this.fiscalEventConfiguration).getFiscalEventPushReqMaxSize();
        verify(fiscalEventRequest, atLeast(1)).getFiscalEvent();
        verify(fiscalEventRequest, atLeast(1)).getRequestHeader();
        verify(fiscalEvent,atLeast(1)).getEventType();
        verify(fiscalEvent).getReferenceId();
    }

    @Test
    void testValidateFiscalEventPushPost10() {
        when(this.fiscalEventConfiguration.getFiscalEventPushReqMaxSize()).thenReturn(null);
        FiscalEvent fiscalEvent = mock(FiscalEvent.class);
        when(fiscalEvent.getEventType()).thenReturn(null);
        when(fiscalEvent.getReferenceId()).thenReturn(null);

        ArrayList<FiscalEvent> fiscalEventList = new ArrayList<FiscalEvent>();
        fiscalEventList.add(fiscalEvent);
        FiscalEventRequest fiscalEventRequest = mock(FiscalEventRequest.class);
        when(fiscalEventRequest.getFiscalEvent()).thenReturn(fiscalEventList);
        when(fiscalEventRequest.getRequestHeader()).thenReturn(new RequestHeader());
        assertThrows(CustomException.class,
                () -> this.fiscalEventValidator.validateFiscalEventPushPost(fiscalEventRequest));
        verify(this.fiscalEventConfiguration).getFiscalEventPushReqMaxSize();
        verify(fiscalEventRequest, atLeast(1)).getFiscalEvent();
        verify(fiscalEventRequest, atLeast(1)).getRequestHeader();
        verify(fiscalEvent,atLeast(1)).getEventType();
        verify(fiscalEvent).getReferenceId();
    }

    @Test
    void testValidateFiscalEventPushPost11() {
        when(this.fiscalEventConfiguration.getFiscalEventPushReqMaxSize()).thenReturn(null);
        FiscalEvent fiscalEvent = mock(FiscalEvent.class);
        when(fiscalEvent.getEventType()).thenReturn(null);
        when(fiscalEvent.getReferenceId()).thenReturn("42");

        ArrayList<FiscalEvent> fiscalEventList = new ArrayList<FiscalEvent>();
        fiscalEventList.add(fiscalEvent);
        FiscalEventRequest fiscalEventRequest = mock(FiscalEventRequest.class);
        when(fiscalEventRequest.getFiscalEvent()).thenReturn(fiscalEventList);
        when(fiscalEventRequest.getRequestHeader())
                .thenReturn(new RequestHeader(1L, "1.0.2", "42", new UserInfo(), "42", "Signature"));
        assertThrows(CustomException.class,
                () -> this.fiscalEventValidator.validateFiscalEventPushPost(fiscalEventRequest));
        verify(this.fiscalEventConfiguration).getFiscalEventPushReqMaxSize();
        verify(fiscalEventRequest, atLeast(1)).getFiscalEvent();
        verify(fiscalEventRequest, atLeast(1)).getRequestHeader();
        verify(fiscalEvent,atLeast(1)).getEventType();
        verify(fiscalEvent).getReferenceId();
    }

    @Test
    void testValidateFiscalEventPushPost12() {
        when(this.fiscalEventConfiguration.getFiscalEventPushReqMaxSize()).thenReturn(null);
        RequestHeader requestHeader = mock(RequestHeader.class);
        when(requestHeader.getUserInfo()).thenReturn(new UserInfo());
        FiscalEvent fiscalEvent = mock(FiscalEvent.class);
        when(fiscalEvent.getEventType()).thenReturn(null);
        when(fiscalEvent.getReferenceId()).thenReturn("42");

        ArrayList<FiscalEvent> fiscalEventList = new ArrayList<FiscalEvent>();
        fiscalEventList.add(fiscalEvent);
        FiscalEventRequest fiscalEventRequest = mock(FiscalEventRequest.class);
        when(fiscalEventRequest.getFiscalEvent()).thenReturn(fiscalEventList);
        when(fiscalEventRequest.getRequestHeader()).thenReturn(requestHeader);
        assertThrows(CustomException.class,
                () -> this.fiscalEventValidator.validateFiscalEventPushPost(fiscalEventRequest));
        verify(this.fiscalEventConfiguration).getFiscalEventPushReqMaxSize();
        verify(fiscalEventRequest, atLeast(1)).getFiscalEvent();
        verify(fiscalEventRequest, atLeast(1)).getRequestHeader();
        verify(fiscalEvent,atLeast(1)).getEventType();
        verify(fiscalEvent).getReferenceId();
        verify(requestHeader, atLeast(1)).getUserInfo();
    }

    @Test
    void testValidateTenantId() {
        FiscalEventRequest fiscalEventRequest = new FiscalEventRequest();
        fiscalEventRequest.setFiscalEvent(new ArrayList<FiscalEvent>());
        HashMap<String, String> stringStringMap = new HashMap<String, String>(1);
        this.fiscalEventValidator.validateTenantId(fiscalEventRequest, stringStringMap);
        assertEquals(1, stringStringMap.size());
    }

    @Test
    void testValidateTenantIdWithEmptyFiscalEvents() {
        FiscalEventRequest fiscalEventRequest = mock(FiscalEventRequest.class);
        when(fiscalEventRequest.getFiscalEvent()).thenReturn(new ArrayList<FiscalEvent>());
        HashMap<String, String> map = new HashMap<String, String>(1);
        this.fiscalEventValidator.validateTenantId(fiscalEventRequest, map);
        verify(fiscalEventRequest).getFiscalEvent();
        assertEquals(1, map.size());
    }

    @Test
    void testValidateTenantIdWithDefaultFiscalEventValues() {
        ArrayList<FiscalEvent> fiscalEventList = new ArrayList<FiscalEvent>();
        fiscalEventList.add(new FiscalEvent());
        FiscalEventRequest fiscalEventRequest = mock(FiscalEventRequest.class);
        when(fiscalEventRequest.getFiscalEvent()).thenReturn(fiscalEventList);
        HashMap<String, String> stringStringMap = new HashMap<String, String>(1);
        this.fiscalEventValidator.validateTenantId(fiscalEventRequest, stringStringMap);
        verify(fiscalEventRequest).getFiscalEvent();
        assertEquals(1, stringStringMap.size());
    }

    @Test
    void testValidateTenantIdWithFiscalEventDetails() {
        when(this.tenantUtil.validateTenant((List<String>) any(), (RequestHeader) any())).thenReturn(true);
        FiscalEventRequest fiscalEventRequest1 = mock(FiscalEventRequest.class);
        when(fiscalEventRequest1.getRequestHeader()).thenReturn(fiscalEventRequest.getRequestHeader());
        when(fiscalEventRequest1.getFiscalEvent()).thenReturn(fiscalEventRequest.getFiscalEvent());
        this.fiscalEventValidator.validateTenantId(fiscalEventRequest1, new HashMap<String, String>(1));
        verify(this.tenantUtil).validateTenant((List<String>) any(), (RequestHeader) any());
        verify(fiscalEventRequest1).getFiscalEvent();
        verify(fiscalEventRequest1).getRequestHeader();
    }

    //search validation
    @Test
    void testValidateFiscalEventSearchPostWithNullHeader() {
        FiscalEventGetRequest fiscalEventGetRequest = new FiscalEventGetRequest();
        assertThrows(CustomException.class,
                () -> this.fiscalEventValidator.validateFiscalEventSearchPost(fiscalEventGetRequest));
    }

    @Test
    void testValidateFiscalEventSearchPostWithNullUserInfo() {
        RequestHeader requestHeader = new RequestHeader();
        Criteria criteria = new Criteria();
        FiscalEventGetRequest fiscalEventGetRequest = new FiscalEventGetRequest(requestHeader, criteria);
        assertThrows(CustomException.class, () -> this.fiscalEventValidator
                .validateFiscalEventSearchPost(fiscalEventGetRequest));
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
    void testValidateFiscalEventSearchPostWithReqHeader() {
        FiscalEventGetRequest fiscalEventGetRequest = new FiscalEventGetRequest();
        assertThrows(CustomException.class,
                () -> this.fiscalEventValidator.validateFiscalEventSearchPost(fiscalEventGetRequest));
    }

    @Test
    void testValidateFiscalEventSearchPostWithRequestHeader() {
        RequestHeader requestHeader = new RequestHeader();
        Criteria criteria = new Criteria();
        FiscalEventGetRequest fiscalEventGetRequest = new FiscalEventGetRequest(requestHeader, criteria);
        assertThrows(CustomException.class, () -> this.fiscalEventValidator
                .validateFiscalEventSearchPost(fiscalEventGetRequest));
    }

    @Test
    void testValidateFiscalEventSearchPostDefaultCriteriaAndRequestHeader() {
        FiscalEventGetRequest fiscalEventGetRequest = mock(FiscalEventGetRequest.class);
        when(fiscalEventGetRequest.getCriteria()).thenReturn(new Criteria());
        when(fiscalEventGetRequest.getRequestHeader()).thenReturn(new RequestHeader());
        assertThrows(CustomException.class,
                () -> this.fiscalEventValidator.validateFiscalEventSearchPost(fiscalEventGetRequest));
        verify(fiscalEventGetRequest).getCriteria();
        verify(fiscalEventGetRequest).getRequestHeader();
    }

    @Test
    void testValidateFiscalEventSearchPostWithDefaultCriteria() {
        FiscalEventGetRequest fiscalEventGetRequest = mock(FiscalEventGetRequest.class);
        when(fiscalEventGetRequest.getCriteria()).thenReturn(new Criteria());
        when(fiscalEventGetRequest.getRequestHeader())
                .thenReturn(fiscalEventRequest.getRequestHeader());
        assertThrows(CustomException.class,
                () -> this.fiscalEventValidator.validateFiscalEventSearchPost(fiscalEventGetRequest));
        verify(fiscalEventGetRequest).getCriteria();
        verify(fiscalEventGetRequest).getRequestHeader();
    }

    @Test
    void testValidateFiscalEventSearchPostWithUserInfoInHeader() {
        UserInfo userInfo = new UserInfo();
        userInfo.setUuid("01234567-89AB-CDEF-FEDC-BA9876543210");
        RequestHeader requestHeader = mock(RequestHeader.class);
        when(requestHeader.getUserInfo()).thenReturn(userInfo);
        FiscalEventGetRequest fiscalEventGetRequest = mock(FiscalEventGetRequest.class);
        when(fiscalEventGetRequest.getCriteria()).thenReturn(new Criteria());
        when(fiscalEventGetRequest.getRequestHeader()).thenReturn(requestHeader);
        assertThrows(CustomException.class,
                () -> this.fiscalEventValidator.validateFiscalEventSearchPost(fiscalEventGetRequest));
        verify(fiscalEventGetRequest).getCriteria();
        verify(fiscalEventGetRequest).getRequestHeader();
        verify(requestHeader, atLeast(1)).getUserInfo();
    }

    @Test
    void testValidateFiscalEventSearchPostWithInvalidEventType() {
        UserInfo userInfo = new UserInfo();
        userInfo.setUuid("01234567-89AB-CDEF-FEDC-BA9876543210");
        RequestHeader requestHeader = mock(RequestHeader.class);
        when(requestHeader.getUserInfo()).thenReturn(userInfo);

        Criteria criteria = new Criteria();
        criteria.setEventType("TENANT_ID");
        FiscalEventGetRequest fiscalEventGetRequest = mock(FiscalEventGetRequest.class);
        when(fiscalEventGetRequest.getCriteria()).thenReturn(criteria);
        when(fiscalEventGetRequest.getRequestHeader()).thenReturn(requestHeader);
        assertThrows(CustomException.class,
                () -> this.fiscalEventValidator.validateFiscalEventSearchPost(fiscalEventGetRequest));
        verify(fiscalEventGetRequest).getCriteria();
        verify(fiscalEventGetRequest).getRequestHeader();
        verify(requestHeader, atLeast(1)).getUserInfo();
    }

    @Test
    void testValidateFiscalEventSearchPostWithDefaultEventTime() {
        UserInfo userInfo = new UserInfo();
        userInfo.setUuid("01234567-89AB-CDEF-FEDC-BA9876543210");
        RequestHeader requestHeader = mock(RequestHeader.class);
        when(requestHeader.getUserInfo()).thenReturn(userInfo);

        Criteria criteria = new Criteria();
        criteria.setToEventTime(0L);
        FiscalEventGetRequest fiscalEventGetRequest = mock(FiscalEventGetRequest.class);
        when(fiscalEventGetRequest.getCriteria()).thenReturn(criteria);
        when(fiscalEventGetRequest.getRequestHeader()).thenReturn(requestHeader);
        assertThrows(CustomException.class,
                () -> this.fiscalEventValidator.validateFiscalEventSearchPost(fiscalEventGetRequest));
        verify(fiscalEventGetRequest).getCriteria();
        verify(fiscalEventGetRequest).getRequestHeader();
        verify(requestHeader, atLeast(1)).getUserInfo();
    }

    @Test
    void testValidateFiscalEventSearchPostWithValidEventTypeAndTenantId() {
        when(this.tenantUtil.validateTenant((java.util.List<String>) any(), (RequestHeader) any())).thenReturn(true);

        UserInfo userInfo = new UserInfo();
        userInfo.setUuid("01234567-89AB-CDEF-FEDC-BA9876543210");
        RequestHeader requestHeader = mock(RequestHeader.class);
        when(requestHeader.getUserInfo()).thenReturn(userInfo);

        Criteria criteria = new Criteria();
        criteria.setEventType("SANCTION");
        criteria.setTenantId("pb");
        FiscalEventGetRequest fiscalEventGetRequest = mock(FiscalEventGetRequest.class);
        when(fiscalEventGetRequest.getCriteria()).thenReturn(criteria);
        when(fiscalEventGetRequest.getRequestHeader()).thenReturn(requestHeader);
        this.fiscalEventValidator.validateFiscalEventSearchPost(fiscalEventGetRequest);
        verify(this.tenantUtil).validateTenant((java.util.List<String>) any(), (RequestHeader) any());
        verify(fiscalEventGetRequest).getCriteria();
        verify(fiscalEventGetRequest).getRequestHeader();
        verify(requestHeader, atLeast(1)).getUserInfo();
    }

    @Test
    void testValidateFiscalEventSearchPostWithDefaultFromEventTime() {
        when(this.tenantUtil.validateTenant((java.util.List<String>) any(), (RequestHeader) any())).thenReturn(true);

        UserInfo userInfo = new UserInfo();
        userInfo.setUuid("01234567-89AB-CDEF-FEDC-BA9876543210");
        RequestHeader requestHeader = mock(RequestHeader.class);
        when(requestHeader.getUserInfo()).thenReturn(userInfo);

        Criteria criteria = new Criteria();
        criteria.setFromEventTime(0L);
        criteria.setTenantId("pb");
        FiscalEventGetRequest fiscalEventGetRequest = mock(FiscalEventGetRequest.class);
        when(fiscalEventGetRequest.getCriteria()).thenReturn(criteria);
        when(fiscalEventGetRequest.getRequestHeader()).thenReturn(requestHeader);
        assertThrows(CustomException.class,
                () -> this.fiscalEventValidator.validateFiscalEventSearchPost(fiscalEventGetRequest));
        verify(this.tenantUtil).validateTenant((java.util.List<String>) any(), (RequestHeader) any());
        verify(fiscalEventGetRequest).getCriteria();
        verify(fiscalEventGetRequest).getRequestHeader();
        verify(requestHeader, atLeast(1)).getUserInfo();
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
        when(this.tenantUtil.validateTenant((List<String>) any(), (RequestHeader) any())).thenReturn(true);

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
        when(this.tenantUtil.validateTenant((List<String>) any(), (RequestHeader) any())).thenReturn(true);

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

