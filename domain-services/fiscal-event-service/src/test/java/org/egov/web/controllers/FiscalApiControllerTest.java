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
 * API tests for FiscalApiController
 */
@Ignore
@RunWith(SpringRunner.class)
@WebMvcTest(FiscalApiController.class)
@Import(TestConfiguration.class)
public class FiscalApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void fiscalEventsV1PushPostSuccess() throws Exception {
        mockMvc.perform(post("/eGovTrial/iFIX-Fiscal-Event/1.0.0/fiscal/events/v1/_push").contentType(MediaType
                .APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());
    }

    @Test
    public void fiscalEventsV1PushPostFailure() throws Exception {
        mockMvc.perform(post("/eGovTrial/iFIX-Fiscal-Event/1.0.0/fiscal/events/v1/_push").contentType(MediaType
                .APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void fiscalEventsV1SearchPostSuccess() throws Exception {
        mockMvc.perform(post("/eGovTrial/iFIX-Fiscal-Event/1.0.0/fiscal/events/v1/_search").contentType(MediaType
                .APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());
    }

    @Test
    public void fiscalEventsV1SearchPostFailure() throws Exception {
        mockMvc.perform(post("/eGovTrial/iFIX-Fiscal-Event/1.0.0/fiscal/events/v1/_search").contentType(MediaType
                .APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest());
    }

}
