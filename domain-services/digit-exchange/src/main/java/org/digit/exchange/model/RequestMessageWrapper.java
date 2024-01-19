package org.digit.exchange.model;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestMessageWrapper {
    @JsonProperty("type")
    private String type;

    @JsonProperty("message")
    private RequestMessage requestMessage;

    public RequestMessageWrapper(String type, RequestMessage requestMessage) {
        this.type = type;
        this.requestMessage = requestMessage;
    }

}
