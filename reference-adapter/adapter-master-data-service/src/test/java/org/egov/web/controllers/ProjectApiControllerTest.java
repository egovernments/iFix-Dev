package org.egov.web.controllers;

import com.google.gson.Gson;
import org.egov.common.contract.response.ResponseHeader;
import org.egov.config.TestDataFormatter;
import org.egov.service.ProjectService;
import org.egov.util.ResponseHeaderCreator;
import org.egov.web.models.ProjectRequest;
import org.egov.web.models.ProjectResponse;
import org.egov.web.models.ProjectSearchRequest;
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
 * API tests for ProjectApiController
 */
@AutoConfigureDataMongo
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(SpringExtension.class)
@WebMvcTest(ProjectApiController.class)
public class ProjectApiControllerTest {

    @MockBean
    ProjectService projectService;
    @MockBean
    ResponseHeaderCreator responseHeaderCreator;
    @Autowired
    private TestDataFormatter testDataFormatter;
    @Autowired
    private MockMvc mockMvc;
    private String projectCreateData;
    private ProjectRequest projectRequest;
    private ProjectRequest projectUpdateRequest;
    private ProjectResponse projectSearchResponse;
    private ProjectSearchRequest projectSearchRequest;


    @BeforeAll
    void init() throws IOException {
        projectRequest = testDataFormatter.getProjectCreateRequestData();
        projectUpdateRequest = testDataFormatter.getProjectUpdateRequestData();
        projectCreateData = new Gson().toJson(projectRequest);
        projectSearchResponse = testDataFormatter.getProjectSearchReponseData();
        projectSearchRequest = testDataFormatter.getProjectSearchRequestData();
    }

    @Test
    public void projectV1CreatePostSuccess() throws Exception {
        doReturn(projectRequest).when(projectService).createProject((ProjectRequest) any());

        doReturn(new ResponseHeader()).when(responseHeaderCreator)
                .createResponseHeaderFromRequestHeader(projectRequest.getRequestHeader(), true);

        mockMvc.perform(post("/project/v1/_create")
                        .accept(MediaType.APPLICATION_JSON).content(projectCreateData)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(projectService, times(1)).createProject((ProjectRequest) any());
        verify(responseHeaderCreator)
                .createResponseHeaderFromRequestHeader(projectRequest.getRequestHeader(), true);
    }

    @Test
    public void projectV1CreatePostFailure() throws Exception {
        mockMvc.perform(post("/project/v1/_create")
                        .accept(MediaType.APPLICATION_JSON).content("")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void projectV1UpdatePostSuccess() throws Exception {
        doReturn(projectUpdateRequest).when(projectService).updateProject((ProjectRequest) any());

        doReturn(new ResponseHeader()).when(responseHeaderCreator)
                .createResponseHeaderFromRequestHeader(projectUpdateRequest.getRequestHeader(), true);

        mockMvc.perform(post("/project/v1/_update")
                        .accept(MediaType.APPLICATION_JSON).content(projectCreateData)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(projectService, times(1)).updateProject((ProjectRequest) any());
        verify(responseHeaderCreator)
                .createResponseHeaderFromRequestHeader(projectUpdateRequest.getRequestHeader(), true);
    }

    @Test
    public void projectV1UpdatePostFailure() throws Exception {
        mockMvc.perform(post("/project/v1/_update")
                        .accept(MediaType.APPLICATION_JSON).content("")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void projectV1SearchPostSuccess() throws Exception {
        doReturn(projectSearchResponse.getProject()).when(projectService).findAllByCriteria(projectSearchRequest);

        doReturn(new ResponseHeader()).when(responseHeaderCreator)
                .createResponseHeaderFromRequestHeader(projectSearchRequest.getRequestHeader(), true);

        mockMvc.perform(post("/project/v1/_search")
                        .accept(MediaType.APPLICATION_JSON).content(projectCreateData)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted());
    }

    @Test
    public void projectV1SearchPostFailure() throws Exception {
        mockMvc.perform(post("/project/v1/_search").contentType(MediaType
                        .APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

}
