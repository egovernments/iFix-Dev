package org.egov.util;

import org.egov.common.contract.request.RequestHeader;
import org.egov.common.contract.request.UserInfo;
import org.egov.common.contract.response.ResponseHeader;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
class ResponseHeaderCreatorTest {

    @InjectMocks
    private ResponseHeaderCreator responseHeaderCreator;

    @Test
    void testCreateResponseHeaderFromRequestHeader() {
        ResponseHeader actualCreateResponseHeaderFromRequestHeaderResult = this.responseHeaderCreator
                .createResponseHeaderFromRequestHeader(new RequestHeader(), true);
        assertNull(actualCreateResponseHeaderFromRequestHeaderResult.getCorrelationId());
        assertNull(actualCreateResponseHeaderFromRequestHeaderResult.getVersion());
        assertNull(actualCreateResponseHeaderFromRequestHeaderResult.getTs());
        assertEquals("successful", actualCreateResponseHeaderFromRequestHeaderResult.getStatus());
        assertEquals("", actualCreateResponseHeaderFromRequestHeaderResult.getSignature());
        assertNull(actualCreateResponseHeaderFromRequestHeaderResult.getMsgId());
    }

    @Test
    void testCreateResponseHeaderFromRequestHeaderWithRequestHeaderDetails() {
        ResponseHeader actualCreateResponseHeaderFromRequestHeaderResult = this.responseHeaderCreator
                .createResponseHeaderFromRequestHeader(new RequestHeader(1L, "1.0.2", "ek9d96e8-3b6b-4e36-9503-0f14a01af74n",
                        new UserInfo(), "dk9d96e8-3b6b-4e36-9503-0f14a01af74m", "Signature"), true);
        assertEquals("dk9d96e8-3b6b-4e36-9503-0f14a01af74m", actualCreateResponseHeaderFromRequestHeaderResult.getCorrelationId());
        assertEquals("1.0.2", actualCreateResponseHeaderFromRequestHeaderResult.getVersion());
        assertEquals(1L, actualCreateResponseHeaderFromRequestHeaderResult.getTs().longValue());
        assertEquals("successful", actualCreateResponseHeaderFromRequestHeaderResult.getStatus());
        assertEquals("Signature", actualCreateResponseHeaderFromRequestHeaderResult.getSignature());
        assertEquals("ek9d96e8-3b6b-4e36-9503-0f14a01af74n", actualCreateResponseHeaderFromRequestHeaderResult.getMsgId());
    }
}

