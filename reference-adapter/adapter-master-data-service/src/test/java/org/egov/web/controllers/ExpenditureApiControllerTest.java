package org.egov.web.controllers;

import com.google.gson.Gson;
import org.egov.common.contract.response.ResponseHeader;
import org.egov.config.TestDataFormatter;
import org.egov.service.ExpenditureService;
import org.egov.util.ResponseHeaderCreator;
import org.egov.web.models.Expenditure;
import org.egov.web.models.ExpenditureRequest;
import org.egov.web.models.ExpenditureResponse;
import org.egov.web.models.ExpenditureSearchRequest;
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
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * API tests for ExpenditureIdApiController
 */
@AutoConfigureDataMongo
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(SpringExtension.class)
@WebMvcTest(ProjectApiController.class)
public class ExpenditureApiControllerTest {

    @MockBean
    ResponseHeaderCreator responseHeaderCreator;
    @Autowired
    private TestDataFormatter testDataFormatter;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ExpenditureService expenditureService;

    private ExpenditureRequest expenditureCreateRequest;
    private ExpenditureSearchRequest expenditureSearchRequest;
    private ExpenditureResponse expenditureSearchResponse;
    private String expenditureCreateRequestString;

    @BeforeAll
    void init() throws IOException {
        expenditureCreateRequest = testDataFormatter.getExpenditureCreateRequestData();
        expenditureSearchRequest = testDataFormatter.getExpenditureSearchRequestData();
        expenditureSearchResponse = testDataFormatter.getExpenditureSearchResponseData();
        expenditureCreateRequestString = new Gson().toJson(expenditureCreateRequest);
    }


    @Test
    public void eatV1CreatePostSuccess() throws Exception {
        doReturn(expenditureCreateRequest).when(expenditureService).createV1Expenditure((ExpenditureRequest) any());

        doReturn(new ResponseHeader()).when(responseHeaderCreator)
                .createResponseHeaderFromRequestHeader(expenditureCreateRequest.getRequestHeader(), true);

        mockMvc.perform(post("/expenditure/v1/_create")
                        .accept(MediaType.APPLICATION_JSON).content(expenditureCreateRequestString)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(expenditureService, times(1)).createV1Expenditure((ExpenditureRequest) any());

        verify(responseHeaderCreator)
                .createResponseHeaderFromRequestHeader(expenditureCreateRequest.getRequestHeader(), true);

    }

    @Test
    public void eatV1CreatePostFailure() throws Exception {
        mockMvc.perform(post("/expenditure/v1/_create")
                        .accept(MediaType.APPLICATION_JSON).content("")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void eatV1SearchPostSuccess() throws Exception {

        doReturn(expenditureSearchResponse.getExpenditure()).when(expenditureService)
                .findAllByCriteria((ExpenditureSearchRequest) any());

        List<Expenditure> expenditureList = expenditureService.findAllByCriteria(expenditureSearchRequest);

        doReturn(new ResponseHeader()).when(responseHeaderCreator)
                .createResponseHeaderFromRequestHeader(expenditureSearchRequest.getRequestHeader(), true);

        mockMvc.perform(post("/expenditure/v1/_search")
                        .accept(MediaType.APPLICATION_JSON).content(expenditureCreateRequestString)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted());
    }

    @Test
    public void eatV1SearchPostFailure() throws Exception {
        mockMvc.perform(post("/expenditure/v1/_search").contentType(MediaType
                        .APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}
