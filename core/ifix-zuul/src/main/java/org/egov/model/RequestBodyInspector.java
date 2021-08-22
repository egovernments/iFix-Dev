package org.egov.model;

import java.util.HashMap;

import static org.egov.constants.RequestContextConstants.REQUEST_INFO_FIELD_NAME_CAMEL_CASE;
import static org.egov.constants.RequestContextConstants.REQUEST_INFO_FIELD_NAME_PASCAL_CASE;

public class RequestBodyInspector {
    private HashMap<String, Object> requestBody;

    public RequestBodyInspector(HashMap<String, Object> requestBody) {
        this.requestBody = requestBody;
    }

    public boolean isRequestHeaderPresent() {
        return requestBody != null && isRequestHeaderContainerFieldPresent();
    }

    public HashMap<String, Object> getRequestBody() {
        return requestBody;
    }

    public void updateRequestHeader(HashMap<String, Object> requestHeader) {
        if (!isRequestHeaderPresent()) {
            return;
        }
        requestBody.put(getRequestHeaderFieldNamePresent(), requestHeader);
    }

    @SuppressWarnings("unchecked")
    public HashMap<String, Object> getRequestHeader() {
        if (isRequestHeaderPresent()) {
            return (HashMap<String, Object>) requestBody.get(getRequestHeaderFieldNamePresent());
        }
        return null;
    }

    private String getRequestHeaderFieldNamePresent() {
        if (isPascalCasePresent()) {
            return REQUEST_INFO_FIELD_NAME_PASCAL_CASE;
        } else {
            return REQUEST_INFO_FIELD_NAME_CAMEL_CASE;
        }
    }

    private boolean isRequestHeaderContainerFieldPresent() {
        return isPascalCasePresent() || isCamelCasePresent();
    }

    private boolean isCamelCasePresent() {
        return requestBody.containsKey(REQUEST_INFO_FIELD_NAME_CAMEL_CASE);
    }

    private boolean isPascalCasePresent() {
        return requestBody.containsKey(REQUEST_INFO_FIELD_NAME_PASCAL_CASE);
    }

}
