package org.egov.service;

import org.egov.config.FiscalEventConfiguration;
import org.egov.config.TestDataFormatter;
import org.egov.producer.Producer;
import org.egov.repository.FiscalEventRepository;
import org.egov.tracer.model.CustomException;
import org.egov.util.FiscalEventMapperUtil;
import org.egov.validator.FiscalEventValidator;
import org.egov.web.models.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
class FiscalEventServiceTest {
    @Mock
    private FiscalEventConfiguration fiscalEventConfiguration;

    @Mock
    private FiscalEventEnrichmentService fiscalEventEnrichmentService;

    @Mock
    private FiscalEventMapperUtil fiscalEventMapperUtil;

    @Mock
    private FiscalEventRepository fiscalEventRepository;

    @InjectMocks
    private FiscalEventService fiscalEventService;

    @Mock
    private FiscalEventValidator fiscalEventValidator;

    @Mock
    private Producer producer;

    @Autowired
    private TestDataFormatter testDataFormatter;

    private FiscalEventRequest fiscalEventRequest;
    private FiscalEventGetRequest fiscalEventGetRequest;
    private FiscalEventResponse fiscalEventSearchResponse;


    @BeforeAll
    void init() throws IOException {
        fiscalEventRequest = testDataFormatter.getFiscalEventPushRequestData();
        fiscalEventGetRequest = testDataFormatter.getFiscalEventSearchRequestData();
        fiscalEventSearchResponse = testDataFormatter.getFiscalEventSearchResponseData();
    }

    @Test
    void testFiscalEventsV1PushPostWithDefaultRequest() {
        doNothing().when(this.fiscalEventValidator).validateFiscalEventPushPost((FiscalEventRequest) any());
        doNothing().when(this.fiscalEventEnrichmentService).enrichFiscalEventPushPost((FiscalEventRequest) any());
        doNothing().when(this.producer).push((String) any(), (Object) any());
        FiscalEventRequest fiscalEventRequest = new FiscalEventRequest();
        assertNotSame(fiscalEventRequest, this.fiscalEventService.fiscalEventsV1PushPost(fiscalEventRequest));
        verify(this.fiscalEventValidator).validateFiscalEventPushPost((FiscalEventRequest) any());
        verify(this.fiscalEventEnrichmentService).enrichFiscalEventPushPost((FiscalEventRequest) any());
    }

    @Test
    void testFiscalEventsV1PushPost() {
        doNothing().when(this.fiscalEventValidator).validateFiscalEventPushPost((FiscalEventRequest) any());
        doNothing().when(this.fiscalEventEnrichmentService).enrichFiscalEventPushPost((FiscalEventRequest) any());
        doNothing().when(this.producer).push((String) any(), (Object) any());

        assertNotSame(fiscalEventRequest, this.fiscalEventService.fiscalEventsV1PushPost(fiscalEventRequest));

        verify(this.fiscalEventValidator).validateFiscalEventPushPost((FiscalEventRequest) any());
        verify(this.fiscalEventEnrichmentService).enrichFiscalEventPushPost((FiscalEventRequest) any());
        verify(this.producer,atLeast(1)).push((String) any(), (Object) any());
    }

    @Test
    void testFiscalEventsV1PushPostWithValidationException() {
        doThrow(new CustomException()).when(this.fiscalEventValidator).validateFiscalEventPushPost((FiscalEventRequest) any());

        assertThrows(CustomException.class,
                () -> this.fiscalEventService.fiscalEventsV1PushPost(fiscalEventRequest));
    }

    @Test
    void testFiscalEventsV1SearchPostWithDefaultSearchCriteria() {
        doNothing().when(this.fiscalEventValidator).validateFiscalEventSearchPost((FiscalEventGetRequest) any());

        FiscalEventGetRequest fiscalEventGetRequest = new FiscalEventGetRequest();
        fiscalEventGetRequest.setCriteria(new Criteria());
        assertTrue(this.fiscalEventService.fiscalEventsV1SearchPost(fiscalEventGetRequest).isEmpty());
        verify(this.fiscalEventValidator).validateFiscalEventSearchPost((FiscalEventGetRequest) any());
    }

    @Test
    void testFiscalEventsV1SearchPostEmptyResult() {
        doNothing().when(this.fiscalEventValidator).validateFiscalEventSearchPost((FiscalEventGetRequest) any());
        when(this.fiscalEventRepository.searchFiscalEvent((Criteria) any())).thenReturn(new ArrayList<Object>());

        assertTrue(this.fiscalEventService.fiscalEventsV1SearchPost(fiscalEventGetRequest).isEmpty());
        verify(this.fiscalEventValidator).validateFiscalEventSearchPost((FiscalEventGetRequest) any());
        verify(this.fiscalEventRepository).searchFiscalEvent((Criteria) any());
    }

    @Test
    void testFiscalEventsV1SearchPostNonEmptyResult() {
        doNothing().when(this.fiscalEventValidator).validateFiscalEventSearchPost((FiscalEventGetRequest) any());

        ArrayList<Object> objectList = new ArrayList<Object>();
        objectList.add("42");
        when(this.fiscalEventRepository.searchFiscalEvent((Criteria) any())).thenReturn(objectList);

        List<FiscalEvent> fiscalEventList = fiscalEventSearchResponse.getFiscalEvent();
        when(this.fiscalEventMapperUtil.mapDereferencedFiscalEventToFiscalEvent((List<Object>) any()))
                .thenReturn(fiscalEventList);

        List<FiscalEvent> actualFiscalEventsV1SearchPostResult = this.fiscalEventService
                .fiscalEventsV1SearchPost(fiscalEventGetRequest);

        assertSame(fiscalEventList, actualFiscalEventsV1SearchPostResult);
        assertTrue(actualFiscalEventsV1SearchPostResult.size() > 0);
        verify(this.fiscalEventValidator).validateFiscalEventSearchPost((FiscalEventGetRequest) any());
        verify(this.fiscalEventRepository).searchFiscalEvent((Criteria) any());
        verify(this.fiscalEventMapperUtil).mapDereferencedFiscalEventToFiscalEvent((List<Object>) any());
    }

    @Test
    void testFiscalEventsV1SearchPostNonEmptyResultWithIdSearchCriteria() {
        doNothing().when(this.fiscalEventValidator).validateFiscalEventSearchPost((FiscalEventGetRequest) any());

        ArrayList<Object> objectList = new ArrayList<Object>();
        objectList.add("42");
        when(this.fiscalEventRepository.searchFiscalEvent((Criteria) any())).thenReturn(objectList);
        List<FiscalEvent> fiscalEventList = fiscalEventSearchResponse.getFiscalEvent();
        when(this.fiscalEventMapperUtil.mapDereferencedFiscalEventToFiscalEvent((List<Object>) any()))
                .thenReturn(fiscalEventList);

        ArrayList<String> idList = new ArrayList<String>();
        idList.add("ek9d96e8-3b6b-4e36-9503-0f14a01af785");
        fiscalEventGetRequest.getCriteria().setIds(idList);

        List<FiscalEvent> actualFiscalEventsV1SearchPostResult = this.fiscalEventService
                .fiscalEventsV1SearchPost(fiscalEventGetRequest);

        assertSame(fiscalEventList, actualFiscalEventsV1SearchPostResult);
        assertTrue(actualFiscalEventsV1SearchPostResult.size() > 0);
        verify(this.fiscalEventValidator).validateFiscalEventSearchPost((FiscalEventGetRequest) any());
        verify(this.fiscalEventRepository).searchFiscalEvent((Criteria) any());
        verify(this.fiscalEventMapperUtil).mapDereferencedFiscalEventToFiscalEvent((List<Object>) any());
    }
}

