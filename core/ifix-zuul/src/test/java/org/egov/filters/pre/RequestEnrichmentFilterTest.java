package org.egov.filters.pre;

import com.netflix.zuul.context.RequestContext;
import org.apache.commons.io.IOUtils;
import org.egov.Resources;
import org.egov.contract.Role;
import org.egov.contract.User;
import org.egov.contract.UserInfo;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class RequestEnrichmentFilterTest {

    private RequestEnrichmentFilter filter;
    private Resources resources = new Resources();

    @Before
    public void before() {
        filter = new RequestEnrichmentFilter();
        RequestContext.getCurrentContext().clear();
    }

    @Test
    public void test_should_set_filter_order_to_execute_last() {
        assertEquals(5, filter.filterOrder());
    }

    @Test
    public void test_should_always_execute_filter() {
        assertTrue(filter.shouldFilter());
    }

    @Test
    public void test_should_add_correlation_id_request_header() {
        final RequestContext currentContext = RequestContext.getCurrentContext();
        final MockHttpServletRequest request = new MockHttpServletRequest();
        request.setMethod("GET");
        currentContext.setRequest(request);
        final String expectedCorrelationId = "someCorrelationId";
        currentContext.set("CORRELATION_ID", expectedCorrelationId);

        filter.run();

        final Map<String, String> zuulRequestHeaders = currentContext.getZuulRequestHeaders();
        assertEquals(2, zuulRequestHeaders.size());
        assertEquals(expectedCorrelationId, zuulRequestHeaders.get("x-correlation-id"));
    }

    @Test
    public void test_should_add_correlation_id_to_request_info_section_of_request_body() throws IOException {
        final RequestContext currentContext = RequestContext.getCurrentContext();
        final MockHttpServletRequest request = new MockHttpServletRequest();
        request.setMethod("POST");
        request.setRequestURI("http://foo/bar/v1/_create");
        request.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        request.setContent(getContent("postRequestFromConsumer.json"));
        currentContext.setRequest(request);
        final String expectedCorrelationId = "someCorrelationId";
        currentContext.set("CORRELATION_ID", expectedCorrelationId);
        currentContext.set("USER_INFO", null);

        filter.run();

        String expectedBody = resources.getFileContents("postRequestWithCorrelationId.json");
        assertEquals(expectedBody, IOUtils.toString(currentContext.getRequest().getInputStream()));
    }

    @Test
    public void test_should_add_user_info_to_request_info_section_of_request_body() throws IOException {
        final RequestContext currentContext = RequestContext.getCurrentContext();
        final MockHttpServletRequest request = new MockHttpServletRequest();
        request.setMethod("POST");
        request.setRequestURI("http://foo/bar/v1/_create");
        request.setContent(getContent("postRequestFromConsumer.json"));
        request.setContentType(MediaType.APPLICATION_JSON_VALUE);
        currentContext.setRequest(request);
        final String expectedCorrelationId = "someCorrelationId";
        currentContext.set("CORRELATION_ID", expectedCorrelationId);
        currentContext.set("USER_INFO", getUser());

        filter.run();

        String expectedBody = resources.getFileContents("enrichedPostRequest.json");
        assertEquals(expectedBody, IOUtils.toString(currentContext.getRequest().getInputStream()));
    }

    @Test
    public void test_should_add_user_info_request_header_for_GET_request_type() throws IOException {
        final RequestContext currentContext = RequestContext.getCurrentContext();
        final MockHttpServletRequest request = new MockHttpServletRequest();
        request.setMethod("GET");
        request.setRequestURI("http://foo/bar/v1/_search");
        currentContext.setRequest(request);
        currentContext.set("CORRELATION_ID", "someCorrelationId");
        currentContext.set("USER_INFO", getUser());

        filter.run();

        String expectedHeaderValue = resources.getFileContents("userInfoHeader.json");
        final Map<String, String> zuulRequestHeaders = currentContext.getZuulRequestHeaders();
        assertEquals(expectedHeaderValue, zuulRequestHeaders.get("x-user-info"));
    }

    @Test
    public void test_should_not_modify_request_body_when_request_info_section_is_not_present() throws IOException {
        final RequestContext currentContext = RequestContext.getCurrentContext();
        final MockHttpServletRequest request = new MockHttpServletRequest();
        request.setMethod("POST");
        request.setRequestURI("http://foo/bar/v1/_create");
        request.setContent(getContent("postRequestWithoutRequestHeaderFromConsumer.json"));
        currentContext.setRequest(request);
        final String expectedCorrelationId = "someCorrelationId";
        currentContext.set("CORRELATION_ID", expectedCorrelationId);
        currentContext.set("USER_INFO", getUser());

        filter.run();

        String expectedBody = resources.getFileContents("postRequestWithoutRequestHeaderFromConsumer.json");
        assertEquals(expectedBody, IOUtils.toString(currentContext.getRequest().getInputStream()));
    }

    private UserInfo getUser() {
        UserInfo mockUserInfo = new UserInfo();
        mockUserInfo.setUuid("abcd");
        mockUserInfo.setTenants(Arrays.asList(new String[]{"pb"}));
        mockUserInfo.setRoles(Arrays.asList(new String[]{"fiscal-event-producer"}));
        mockUserInfo.setAttributes(null);
        return mockUserInfo;
    }

    private byte[] getContent(String fileName) {
        try {
            return IOUtils.toByteArray(IOUtils.toInputStream(resources.getFileContents(fileName)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}