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
 * API tests for GovernmentApiController
 */
@Ignore
@RunWith(SpringRunner.class)
@WebMvcTest(GovernmentApiController.class)
@Import(TestConfiguration.class)
public class GovernmentApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void governmentV1CreatePostSuccess() throws Exception {
        mockMvc.perform(post("/eGovTrial/iFIX-Master-Data/1.0.0/government/v1/_create").contentType(MediaType
                .APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());
    }

    @Test
    public void governmentV1CreatePostFailure() throws Exception {
        mockMvc.perform(post("/eGovTrial/iFIX-Master-Data/1.0.0/government/v1/_create").contentType(MediaType
                .APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void governmentV1SearchPostSuccess() throws Exception {
        mockMvc.perform(post("/eGovTrial/iFIX-Master-Data/1.0.0/government/v1/_search").contentType(MediaType
                .APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());
    }

    @Test
    public void governmentV1SearchPostFailure() throws Exception {
        mockMvc.perform(post("/eGovTrial/iFIX-Master-Data/1.0.0/government/v1/_search").contentType(MediaType
                .APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest());
    }

}
