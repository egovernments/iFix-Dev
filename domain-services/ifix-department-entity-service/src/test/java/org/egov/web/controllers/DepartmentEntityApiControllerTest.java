package org.egov.web.controllers;

import org.egov.TestConfiguration;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * API tests for DepartmentEntityApiController
 */
@Ignore
@RunWith(SpringRunner.class)
@WebMvcTest(DepartmentEntityApiController.class)
@Import(TestConfiguration.class)
public class DepartmentEntityApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void departmentEntityHierarchyLevelV1CreatePostSuccess() throws Exception {
        mockMvc.perform(post("/eGovTrial/iFIX-Department-Hierarchy-Master/1.0.0/departmentEntity/hierarchyLevel/v1/_create").contentType(MediaType
                .APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());
    }

    @Test
    public void departmentEntityHierarchyLevelV1CreatePostFailure() throws Exception {
        mockMvc.perform(post("/eGovTrial/iFIX-Department-Hierarchy-Master/1.0.0/departmentEntity/hierarchyLevel/v1/_create").contentType(MediaType
                .APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void departmentEntityHierarchyLevelV1SearchPostSuccess() throws Exception {
        mockMvc.perform(post("/eGovTrial/iFIX-Department-Hierarchy-Master/1.0.0/departmentEntity/hierarchyLevel/v1/_search").contentType(MediaType
                .APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());
    }

    @Test
    public void departmentEntityHierarchyLevelV1SearchPostFailure() throws Exception {
        mockMvc.perform(post("/eGovTrial/iFIX-Department-Hierarchy-Master/1.0.0/departmentEntity/hierarchyLevel/v1/_search").contentType(MediaType
                .APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void departmentEntityV1CreatePostSuccess() throws Exception {
        mockMvc.perform(post("/eGovTrial/iFIX-Department-Hierarchy-Master/1.0.0/departmentEntity/v1/_create").contentType(MediaType
                .APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());
    }

    @Test
    public void departmentEntityV1CreatePostFailure() throws Exception {
        mockMvc.perform(post("/eGovTrial/iFIX-Department-Hierarchy-Master/1.0.0/departmentEntity/v1/_create").contentType(MediaType
                .APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void departmentEntityV1SearchPostSuccess() throws Exception {
        mockMvc.perform(post("/eGovTrial/iFIX-Department-Hierarchy-Master/1.0.0/departmentEntity/v1/_search").contentType(MediaType
                .APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());
    }

    @Test
    public void departmentEntityV1SearchPostFailure() throws Exception {
        mockMvc.perform(post("/eGovTrial/iFIX-Department-Hierarchy-Master/1.0.0/departmentEntity/v1/_search").contentType(MediaType
                .APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest());
    }

}
