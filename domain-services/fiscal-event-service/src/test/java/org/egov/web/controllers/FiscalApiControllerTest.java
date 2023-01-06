package org.egov.web.controllers;

import com.google.gson.Gson;
import org.egov.common.contract.response.ResponseHeader;
import org.egov.config.TestDataFormatter;
import org.egov.service.FiscalEventService;
import org.egov.util.ResponseHeaderCreator;
import org.egov.web.models.FiscalEventGetRequest;
import org.egov.web.models.FiscalEventRequest;
import org.egov.web.models.FiscalEventResponse;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * API tests for FiscalApiController
 */

@AutoConfigureDataMongo
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(SpringExtension.class)
@WebMvcTest(FiscalApiController.class)
public class FiscalApiControllerTest {

    @Autowired
    private TestDataFormatter testDataFormatter;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FiscalEventService fiscalEventService;

    @MockBean
    private ResponseHeaderCreator responseHeaderCreator;

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
    public void fiscalEventsV1PushPostSuccess() throws Exception {
        doReturn(fiscalEventRequest).when(fiscalEventService).fiscalEventsV1PushPost((FiscalEventRequest) any());


        doReturn(new ResponseHeader()).when(responseHeaderCreator)
                .createResponseHeaderFromRequestHeader(fiscalEventRequest.getRequestHeader(), true);

        mockMvc.perform(post("/events/v1/_publish")
                .accept(MediaType.APPLICATION_JSON).content(fiscalEventPushData)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted());

        verify(fiscalEventService, times(1)).fiscalEventsV1PushPost((FiscalEventRequest) any());

        verify(responseHeaderCreator)
                .createResponseHeaderFromRequestHeader(fiscalEventRequest.getRequestHeader(), true);
    }

    @Test
    public void fiscalEventsV1PushPostFailure() throws Exception {
        mockMvc.perform(post("/events/v1/_publish").contentType(MediaType
                .APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void fiscalEventsV1SearchPostSuccess() throws Exception {
        doReturn(fiscalEventSearchResponse.getFiscalEvent()).when(fiscalEventService)
                .fiscalEventsV1SearchPost(fiscalEventGetRequest);

        doReturn(new ResponseHeader()).when(responseHeaderCreator)
                .createResponseHeaderFromRequestHeader(fiscalEventGetRequest.getRequestHeader(), true);

        mockMvc.perform(post("/events/v1/_search")
                .accept(MediaType.APPLICATION_JSON).content(fiscalEventSearchData)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void fiscalEventsV1SearchPostFailure() throws Exception {
        mockMvc.perform(post("/events/v1/_search").contentType(MediaType
                .APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest());
    }

}
