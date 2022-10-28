package org.egov.web.controllers;

import com.google.gson.Gson;
import org.egov.common.contract.response.ResponseHeader;
import org.egov.config.TestDataFormatter;
import org.egov.service.DepartmentService;
import org.egov.util.ResponseHeaderCreator;
import org.egov.web.models.DepartmentRequest;
import org.egov.web.models.DepartmentResponse;
import org.egov.web.models.DepartmentSearchRequest;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * API tests for DepartmentApiController
 */

@AutoConfigureDataMongo
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(SpringExtension.class)
@WebMvcTest(ProjectApiController.class)
public class DepartmentApiControllerTest {
    @MockBean
    ResponseHeaderCreator responseHeaderCreator;
    @Autowired
    private TestDataFormatter testDataFormatter;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private DepartmentService departmentService;
    private DepartmentRequest departmentCreateRequest;
    private DepartmentResponse departmentResponse;
    private DepartmentSearchRequest departmentSearchRequest;
    private String createDepartmentRequestString;

    @BeforeAll
    void init() throws IOException {
        departmentCreateRequest = testDataFormatter.getDepartmentCreateRequestData();
        departmentResponse = testDataFormatter.getDepartmentSearchResponseData();
        departmentSearchRequest = testDataFormatter.getDepartmentSearchRequestData();

        createDepartmentRequestString = new Gson().toJson(departmentCreateRequest);
    }

    @Test
    public void departmentV1CreatePostSuccess() throws Exception {
        doReturn(departmentCreateRequest).when(departmentService).createDepartment((DepartmentRequest) any());

        doReturn(new ResponseHeader()).when(responseHeaderCreator)
                .createResponseHeaderFromRequestHeader(departmentCreateRequest.getRequestHeader(), true);

        mockMvc.perform(post("/department/v1/_create")
                        .accept(MediaType.APPLICATION_JSON).content(createDepartmentRequestString)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted());

        verify(departmentService, times(1)).createDepartment((DepartmentRequest) any());

        verify(responseHeaderCreator)
                .createResponseHeaderFromRequestHeader(departmentCreateRequest.getRequestHeader(), true);
    }

    @Test
    public void departmentV1CreatePostFailure() throws Exception {
        mockMvc.perform(post("/department/v1/_create")
                        .accept(MediaType.APPLICATION_JSON).content("")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void departmentV1SearchPostSuccess() throws Exception {
        doReturn(departmentResponse.getDepartment()).when(departmentService)
                .departmentV1SearchPost(departmentSearchRequest);

        doReturn(new ResponseHeader()).when(responseHeaderCreator)
                .createResponseHeaderFromRequestHeader(departmentSearchRequest.getRequestHeader(), true);

        mockMvc.perform(post("/department/v1/_search")
                        .accept(MediaType.APPLICATION_JSON).content(createDepartmentRequestString)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void departmentV1SearchPostFailure() throws Exception {
        mockMvc.perform(post("/department/v1/_search").contentType(MediaType
                        .APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

}
