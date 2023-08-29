package org.egov.util;

import org.egov.common.contract.request.RequestHeader;
import org.egov.common.contract.response.ResponseHeader;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
//@SpringBootTest
class ResponseHeaderCreatorTest {

   /* @InjectMocks
    private ResponseHeaderCreator responseHeaderCreator;

    @Test
    void testCreateResponseHeaderFromRequestHeader() {
        ResponseHeader actualCreateResponseHeaderFromRequestHeaderResult = responseHeaderCreator
                .createResponseHeaderFromRequestHeader(new RequestHeader(), true);

        assertNull(actualCreateResponseHeaderFromRequestHeaderResult.getCorrelationId());
        assertNull(actualCreateResponseHeaderFromRequestHeaderResult.getVersion());
        assertNull(actualCreateResponseHeaderFromRequestHeaderResult.getTs());
        assertEquals("successful", actualCreateResponseHeaderFromRequestHeaderResult.getStatus());
        assertEquals("", actualCreateResponseHeaderFromRequestHeaderResult.getSignature());
        assertNull(actualCreateResponseHeaderFromRequestHeaderResult.getMsgId());
    }*/
}

