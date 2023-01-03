package org.egov.web.controllers;

import com.google.gson.Gson;
import org.egov.common.contract.response.ResponseHeader;
import org.egov.config.TestDataFormatter;
import org.egov.service.DepartmentHierarchyLevelService;
import org.egov.util.ResponseHeaderCreator;
import org.egov.web.models.DepartmentHierarchyLevelRequest;
import org.egov.web.models.DepartmentHierarchyLevelResponse;
import org.egov.web.models.DepartmentHierarchyLevelSearchRequest;
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

@AutoConfigureDataMongo
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@WebMvcTest(DepartmentEntityApiController.class)
@ExtendWith(SpringExtension.class)
public class DepartmentHierarchyLevelApiControllerTest {

    @Autowired
    private TestDataFormatter testDataFormatter;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DepartmentHierarchyLevelService hierarchyLevelService;

    @MockBean
    private ResponseHeaderCreator responseHeaderCreator;

    private DepartmentHierarchyLevelRequest hierarchyLevelRequest;
    private DepartmentHierarchyLevelResponse hierarchyLevelResponse;
    private DepartmentHierarchyLevelSearchRequest hierarchyLevelSearchRequest;
    private String departmentHierarchyCreateData;
    private String departmentHierarchySearchData;


    @BeforeAll
    void init() throws IOException {
        hierarchyLevelRequest = testDataFormatter.getDeptHierarchyCreateRequestData();
        hierarchyLevelResponse = testDataFormatter.getDeptHierarchyCreateResponseData();
        hierarchyLevelSearchRequest = testDataFormatter.getDeptHierarchySearchRequestData();

        departmentHierarchyCreateData = new Gson().toJson(hierarchyLevelRequest);
        departmentHierarchySearchData = new Gson().toJson(hierarchyLevelSearchRequest);
    }

    @Test
    public void departmentEntityHierarchyLevelV1CreatePostSuccess() throws Exception {
        when(hierarchyLevelService.createDepartmentEntityHierarchyLevel(any())).thenReturn(hierarchyLevelRequest);
        doReturn(new ResponseHeader()).when(responseHeaderCreator)
                .createResponseHeaderFromRequestHeader(hierarchyLevelRequest.getRequestHeader(), true);

        mockMvc.perform(post("/departmentEntity/hierarchyLevel/v1/_create")
                        .accept(MediaType.APPLICATION_JSON).content(departmentHierarchyCreateData)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(hierarchyLevelService, times(1)).createDepartmentEntityHierarchyLevel(any());

        verify(responseHeaderCreator)
                .createResponseHeaderFromRequestHeader(hierarchyLevelRequest.getRequestHeader(), true);
    }

    @Test
    public void departmentEntityHierarchyLevelV1CreatePostFailure() throws Exception {
        mockMvc.perform(post("/departmentEntity/hierarchyLevel/v1/_create")
                        .accept(MediaType.APPLICATION_JSON).content("")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void departmentEntityHierarchyLevelV1SearchPostSuccess() throws Exception {
        doReturn(hierarchyLevelResponse.getDepartmentHierarchyLevel()).when(hierarchyLevelService)
                .searchDepartmentEntityHierarchyLevel(hierarchyLevelSearchRequest);

        doReturn(new ResponseHeader()).when(responseHeaderCreator)
                .createResponseHeaderFromRequestHeader(hierarchyLevelSearchRequest.getRequestHeader(), true);

        mockMvc.perform(post("/departmentEntity/hierarchyLevel/v1/_search")
                        .accept(MediaType.APPLICATION_JSON).content(departmentHierarchySearchData)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void departmentEntityHierarchyLevelV1SearchPostFailure() throws Exception {
        mockMvc.perform(post("/departmentEntity/hierarchyLevel/v1/_search").contentType(MediaType
                        .APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest());
    }
}
