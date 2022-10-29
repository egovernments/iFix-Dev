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
 * API tests for ChartOfAccountApiController
 */
@Ignore
@RunWith(SpringRunner.class)
@WebMvcTest(ChartOfAccountApiController.class)
@Import(TestConfiguration.class)
public class ChartOfAccountApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void chartOfAccountV1CreatePostSuccess() throws Exception {
        mockMvc.perform(post("/eGovTrial/iFIX-Master-Data/1.0.0/chartOfAccount/v1/_create").contentType(MediaType
                .APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());
    }

    @Test
    public void chartOfAccountV1CreatePostFailure() throws Exception {
        mockMvc.perform(post("/eGovTrial/iFIX-Master-Data/1.0.0/chartOfAccount/v1/_create").contentType(MediaType
                .APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void chartOfAccountV1SearchPostSuccess() throws Exception {
        mockMvc.perform(post("/eGovTrial/iFIX-Master-Data/1.0.0/chartOfAccount/v1/_search").contentType(MediaType
                .APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());
    }

    @Test
    public void chartOfAccountV1SearchPostFailure() throws Exception {
        mockMvc.perform(post("/eGovTrial/iFIX-Master-Data/1.0.0/chartOfAccount/v1/_search").contentType(MediaType
                .APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest());
    }

}
