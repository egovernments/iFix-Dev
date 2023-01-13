//package org.egov.web.controllers;
//
//import com.google.gson.Gson;
//import org.egov.common.contract.response.ResponseHeader;
//import org.egov.config.TestDataFormatter;
//import org.egov.service.GovernmentService;
//import org.egov.util.ResponseHeaderCreator;
//import org.egov.web.models.GovernmentRequest;
//import org.egov.web.models.GovernmentResponse;
//import org.egov.web.models.GovernmentSearchRequest;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.TestInstance;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//import org.springframework.test.web.servlet.MockMvc;
//
//import java.io.IOException;
//
//import static org.mockito.Mockito.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
///**
// * API tests for GovernmentApiController
// */
//@AutoConfigureDataMongo
//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
//@ExtendWith(SpringExtension.class)
//@WebMvcTest(GovernmentApiController.class)
//public class GovernmentApiControllerTest {
//
//    @MockBean
//    GovernmentService governmentService;
//    @Autowired
//    private TestDataFormatter testDataFormatter;
//    @Autowired
//    private MockMvc mockMvc;
//    @MockBean
//    private ResponseHeaderCreator responseHeaderCreator;
//
//    private String govCreateData;
//    private GovernmentRequest governmentRequest;
//    private GovernmentResponse governmentCreateRequest;
//    private GovernmentSearchRequest governmentSearchRequest;
//    private GovernmentResponse governmentSearchResponse;
//
//
//    @BeforeAll
//    void init() throws IOException {
//        governmentRequest = testDataFormatter.getGovernmentRequestData();
//        governmentCreateRequest = testDataFormatter.getGovernmentCreateResponseData();
//
//        govCreateData = new Gson().toJson(governmentRequest);
//
//        governmentSearchRequest = testDataFormatter.getGovernmentSearchRequestData();
//        governmentSearchResponse = testDataFormatter.getGovernmentSearchResponseData();
//
//    }
//
//
//    @Test
//    public void governmentV1CreatePostSuccess() throws Exception {
//        doReturn(governmentRequest).when(governmentService).addGovernment(governmentRequest);
//
//        doReturn(new ResponseHeader()).when(responseHeaderCreator)
//                .createResponseHeaderFromRequestHeader(governmentRequest.getRequestHeader(), true);
//
//        mockMvc.perform(post("/government/v1/_create")
//                        .accept(MediaType.APPLICATION_JSON).content(govCreateData)
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isAccepted());
//
//        verify(governmentService, times(1)).addGovernment(governmentRequest);
//
//        verify(responseHeaderCreator)
//                .createResponseHeaderFromRequestHeader(governmentRequest.getRequestHeader(), true);
//    }
//
//    @Test
//    public void governmentV1CreatePostFailure() throws Exception {
//        mockMvc.perform(post("/government/v1/_create")
//                        .accept(MediaType.APPLICATION_JSON).content("")
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isBadRequest());
//    }
//
//    @Test
//    public void governmentV1SearchPostSuccess() throws Exception {
//        doReturn(governmentSearchResponse.getGovernment()).when(governmentService)
//                .searchAllGovernmentByIdList(governmentSearchRequest);
//
//        doReturn(new ResponseHeader()).when(responseHeaderCreator)
//                .createResponseHeaderFromRequestHeader(governmentSearchRequest.getRequestHeader(), true);
//
//        mockMvc.perform(post("/government/v1/_search")
//                        .accept(MediaType.APPLICATION_JSON).content(govCreateData)
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isAccepted());
//
//    }
//
//    @Test
//    public void governmentV1SearchPostFailure() throws Exception {
//        mockMvc.perform(post("/government/v1/_search").contentType(MediaType
//                        .APPLICATION_JSON_UTF8))
//                .andExpect(status().isBadRequest());
//    }
//
//}
