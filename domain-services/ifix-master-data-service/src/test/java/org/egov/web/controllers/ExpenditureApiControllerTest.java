package org.egov.web.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * API tests for ExpenditureIdApiController
 */
//@Ignore
//@RunWith(SpringRunner.class)
//@WebMvcTest(ExpenditureApiController.class)
//@Import(TestConfiguration.class)
public class ExpenditureApiControllerTest {

    /*@Autowired
    private MockMvc mockMvc;

    @Test
    public void eatV1CreatePostSuccess() throws Exception {
        mockMvc.perform(post("/eGovTrial/iFIX-Master-Data/1.0.0/expenditure/v1/_create").contentType(MediaType
                        .APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());
    }

    @Test
    public void eatV1CreatePostFailure() throws Exception {
        mockMvc.perform(post("/eGovTrial/iFIX-Master-Data/1.0.0/expenditure/v1/_create").contentType(MediaType
                        .APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void eatV1SearchPostSuccess() throws Exception {
        mockMvc.perform(post("/eGovTrial/iFIX-Master-Data/1.0.0/expenditure/v1/_search").contentType(MediaType
                        .APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());
    }

    @Test
    public void eatV1SearchPostFailure() throws Exception {
        mockMvc.perform(post("/eGovTrial/iFIX-Master-Data/1.0.0/expenditure/v1/_search").contentType(MediaType
                        .APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest());
    }*/

}
