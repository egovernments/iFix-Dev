package org.egov.web.controllers;

import com.google.gson.Gson;
import org.egov.MasterApplicationMain;
import org.egov.common.contract.response.ResponseHeader;
import org.egov.config.TestDataFormatter;
import org.egov.service.ChartOfAccountService;
import org.egov.util.ResponseHeaderCreator;
import org.egov.web.models.COARequest;
import org.egov.web.models.COAResponse;
import org.egov.web.models.COASearchRequest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * API tests for ChartOfAccountApiController
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(classes = MasterApplicationMain.class)
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
public class ChartOfAccountApiControllerTest {

    @Autowired
    private TestDataFormatter testDataFormatter;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ChartOfAccountService chartOfAccountService;

    @MockBean
    private ResponseHeaderCreator responseHeaderCreator;

    private String coaCreateData;
    private String coaSearchData;
    private COARequest coaRequest;
    private COAResponse coaCreateResponse;
    private COASearchRequest coaSearchRequest;
    private COAResponse coaSearchResponse;


    @BeforeAll
    void init() throws IOException {
        coaRequest = testDataFormatter.getCoaRequestData();
        coaCreateResponse = testDataFormatter.getCoaCreateResponseData();

        coaCreateData = new Gson().toJson(coaRequest);

        coaSearchRequest = testDataFormatter.getCoaSearchRequestData();
        coaSearchData = new Gson().toJson(coaSearchRequest);
        coaSearchResponse = testDataFormatter.getCoaSearchResponseData();

    }

    @Test
    public void chartOfAccountV1CreatePostSuccess() throws Exception {
        doReturn(coaRequest).when(chartOfAccountService).chartOfAccountV1CreatePost(coaRequest);

        doReturn(new ResponseHeader()).when(responseHeaderCreator)
                .createResponseHeaderFromRequestHeader(coaRequest.getRequestHeader(), true);

        mockMvc.perform(post("/chartOfAccount/v1/_create")
                        .accept(MediaType.APPLICATION_JSON).content(coaCreateData)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted());

        verify(chartOfAccountService, times(1)).chartOfAccountV1CreatePost(coaRequest);

        verify(responseHeaderCreator)
                .createResponseHeaderFromRequestHeader(coaRequest.getRequestHeader(), true);
    }

    @Test
    public void chartOfAccountV1CreatePostFailure() throws Exception {
        mockMvc.perform(post("/chartOfAccount/v1/_create")
                        .accept(MediaType.APPLICATION_JSON).content("")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void chartOfAccountV1SearchPostSuccess() throws Exception {
        doReturn(coaSearchResponse.getChartOfAccounts()).when(chartOfAccountService)
                .chartOfAccountV1SearchPost(coaSearchRequest);

        doReturn(new ResponseHeader()).when(responseHeaderCreator)
                .createResponseHeaderFromRequestHeader(coaSearchRequest.getRequestHeader(), true);

        mockMvc.perform(post("/chartOfAccount/v1/_search")
                        .accept(MediaType.APPLICATION_JSON).content(coaSearchData)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void chartOfAccountV1SearchPostFailure() throws Exception {
        mockMvc.perform(post("/chartOfAccount/v1/_search").contentType(MediaType
                        .APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest());
    }

}
