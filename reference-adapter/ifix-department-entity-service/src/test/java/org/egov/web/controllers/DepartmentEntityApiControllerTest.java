package org.egov.web.controllers;

import com.google.gson.Gson;
import org.egov.common.contract.response.ResponseHeader;
import org.egov.config.TestDataFormatter;
import org.egov.service.DepartmentEntityService;
import org.egov.util.ResponseHeaderCreator;
import org.egov.web.models.DepartmentEntityRequest;
import org.egov.web.models.DepartmentEntityResponse;
import org.egov.web.models.DepartmentEntitySearchRequest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
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
 * API tests for ChartOfAccountApiController
 */
@Disabled("TODO: Need to work on it")
//@AutoConfigureDataMongo
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@WebMvcTest(DepartmentEntityApiController.class)
@ExtendWith(SpringExtension.class)
public class DepartmentEntityApiControllerTest {

    @Autowired
    private TestDataFormatter testDataFormatter;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DepartmentEntityService departmentEntityService;

    @MockBean
    private ResponseHeaderCreator responseHeaderCreator;

    private DepartmentEntityRequest departmentEntityRequest;
    private DepartmentEntityResponse departmentEntityResponse;
    private DepartmentEntitySearchRequest departmentEntitySearchRequest;
    private String departmentEntityCreateData;
    private String departmentEntitySearchData;


    @BeforeAll
    void init() throws IOException {
        departmentEntityRequest = testDataFormatter.getDeptEntityCreateRequestData();
        departmentEntityResponse = testDataFormatter.getDeptEntityCreateResponseData();
        departmentEntitySearchRequest = testDataFormatter.getDeptEntitySearchRequestData();

        departmentEntityCreateData = new Gson().toJson(departmentEntityRequest);
        departmentEntitySearchData = new Gson().toJson(departmentEntitySearchRequest);
    }

    @Test
    public void departmentEntityV1CreatePostSuccess() throws Exception {
        when(departmentEntityService.createDepartmentEntity(any())).thenReturn(departmentEntityRequest);
        doReturn(new ResponseHeader()).when(responseHeaderCreator)
                .createResponseHeaderFromRequestHeader(departmentEntityRequest.getRequestHeader(), true);

        mockMvc.perform(post("/departmentEntity/v1/_create")
                        .accept(MediaType.APPLICATION_JSON).content(departmentEntityCreateData)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(departmentEntityService, times(1)).createDepartmentEntity(any());

        verify(responseHeaderCreator)
                .createResponseHeaderFromRequestHeader(departmentEntityRequest.getRequestHeader(), true);
    }

    @Test
    public void departmentEntityV1CreatePostFailure() throws Exception {
        mockMvc.perform(post("/departmentEntity/v1/_create")
                        .accept(MediaType.APPLICATION_JSON).content("")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testDepartmentEntityV1SearchPostSuccess() throws Exception {
        doReturn(departmentEntityResponse.getDepartmentEntity()).when(departmentEntityService)
                .findAllByCriteria(departmentEntitySearchRequest);

        doReturn(new ResponseHeader()).when(responseHeaderCreator)
                .createResponseHeaderFromRequestHeader(departmentEntitySearchRequest.getRequestHeader(), true);

        mockMvc.perform(post("/departmentEntity/v1/_search")
                        .accept(MediaType.APPLICATION_JSON).content(departmentEntitySearchData)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testDepartmentEntityV1SearchPostFailure() throws Exception {
        mockMvc.perform(post("/departmentEntity/v1/_search").contentType(MediaType
                        .APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void departmentEntityV1UpdatePostSuccess() throws Exception {
        when(departmentEntityService.updateDepartmentEntity(any())).thenReturn(departmentEntityRequest);
        doReturn(new ResponseHeader()).when(responseHeaderCreator)
                .createResponseHeaderFromRequestHeader(departmentEntityRequest.getRequestHeader(), true);

        mockMvc.perform(post("/departmentEntity/v1/_update")
                        .accept(MediaType.APPLICATION_JSON).content(departmentEntityCreateData)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(departmentEntityService, times(1)).updateDepartmentEntity(any());

        verify(responseHeaderCreator)
                .createResponseHeaderFromRequestHeader(departmentEntityRequest.getRequestHeader(), true);
    }

    @Test
    public void departmentEntityV1UpdatePostFailure() throws Exception {
        mockMvc.perform(post("/departmentEntity/v1/_update")
                        .accept(MediaType.APPLICATION_JSON).content("")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

}
