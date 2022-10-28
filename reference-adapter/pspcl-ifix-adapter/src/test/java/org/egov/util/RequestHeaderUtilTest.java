package org.egov.util;

import org.egov.config.PspclIfixAdapterConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;

import static org.junit.Assert.assertNotNull;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RequestHeaderUtilTest {

    @Mock
    private PspclIfixAdapterConfiguration adapterConfiguration;

    private RequestHeaderUtil requestHeaderUtil;

    @BeforeEach
    private void init() throws IOException {
        MockitoAnnotations.openMocks(this);
        requestHeaderUtil = new RequestHeaderUtil(adapterConfiguration);
    }

    @Test
    void getRequestHeader(){
        assertNotNull(requestHeaderUtil.getRequestHeader());
    }



}