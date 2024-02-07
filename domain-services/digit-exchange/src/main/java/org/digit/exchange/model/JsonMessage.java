package org.digit.exchange.model;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@Builder
public class JsonMessage {

    @JsonProperty("signature")
    private String signature;
    @JsonProperty("header")
    @NotNull
    private RequestHeader header;
    @NotNull
    @JsonProperty("message")
    private JsonNode jsonNode;



}
