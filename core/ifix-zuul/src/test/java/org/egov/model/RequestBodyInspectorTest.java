package org.egov.model;

import org.junit.Test;

import java.util.HashMap;
import java.util.Set;

import static org.junit.Assert.*;

public class RequestBodyInspectorTest {

    @Test
    public void test_should_return_request_info_when_request_info_container_field_name_has_pascal_case() {
        final HashMap<String, Object> requestBody = new HashMap<>();
        final HashMap<Object, Object> requestHeaderBody = new HashMap<>();
        requestBody.put("requestHeader", requestHeaderBody);

        final RequestBodyInspector requestBodyInspector = new RequestBodyInspector(requestBody);

        assertEquals(requestHeaderBody, requestBodyInspector.getRequestHeader());
    }

    @Test
    public void test_should_return_request_info_when_request_info_container_field_name_has_camel_case() {
        final HashMap<String, Object> requestBody = new HashMap<>();
        final HashMap<Object, Object> requestHeaderBody = new HashMap<>();
        requestBody.put("requestHeader", requestHeaderBody);

        final RequestBodyInspector requestBodyInspector = new RequestBodyInspector(requestBody);

        assertEquals(requestHeaderBody, requestBodyInspector.getRequestHeader());
    }

    @Test
    public void test_should_return_null_when_request_body_is_empty() {
        final HashMap<String, Object> requestBody = new HashMap<>();
        final RequestBodyInspector requestBodyInspector = new RequestBodyInspector(requestBody);

        assertNull(requestBodyInspector.getRequestHeader());
    }

    @Test
    public void test_should_return_null_when_request_body_does_not_have_request_info() {
        final HashMap<String, Object> requestBody = new HashMap<>();
        requestBody.put("someField", new HashMap<>());
        final RequestBodyInspector requestBodyInspector = new RequestBodyInspector(requestBody);

        assertNull(requestBodyInspector.getRequestHeader());
    }

    @Test
    public void test_should_update_request_info() {
        final HashMap<String, Object> requestBody = new HashMap<>();
        final HashMap<Object, Object> originalRequestHeaderBody = new HashMap<>();
        requestBody.put("requestHeader", originalRequestHeaderBody);

        final RequestBodyInspector requestBodyInspector = new RequestBodyInspector(requestBody);

        final HashMap<String, Object> updatedRequestHeader = new HashMap<>();
        updatedRequestHeader.put("foo", "bar");

        requestBodyInspector.updateRequestHeader(updatedRequestHeader);

        final HashMap<String, Object> actualRequestBody = requestBodyInspector.getRequestBody();
        final HashMap<String, Object> actualRequestHeader =
            (HashMap<String, Object>) actualRequestBody.get("requestHeader");
        assertNotNull(actualRequestHeader);
        assertTrue(actualRequestHeader.containsKey("foo"));
    }

    @Test
    public void test_should_not_update_request_body_with_new_request_info_when_original_request_body_does_not_have_request_info_field() {
        final HashMap<String, Object> requestBody = new HashMap<>();
        requestBody.put("foo", new HashMap<>());

        final RequestBodyInspector requestBodyInspector = new RequestBodyInspector(requestBody);

        final HashMap<String, Object> updatedRequestHeader = new HashMap<>();
        updatedRequestHeader.put("userInfo", "user");

        requestBodyInspector.updateRequestHeader(updatedRequestHeader);

        final HashMap<String, Object> actualRequestBody = requestBodyInspector.getRequestBody();
        final Set<String> keys = actualRequestBody.keySet();
        assertEquals(1, keys.size());
        assertTrue(keys.contains("foo"));
    }

}