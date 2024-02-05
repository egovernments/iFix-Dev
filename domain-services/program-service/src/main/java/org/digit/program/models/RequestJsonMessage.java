package org.digit.program.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

@Getter
@Setter
@Slf4j
public class RequestJsonMessage {

    private String id;
    @JsonProperty("signature")
    private String signature;

    @JsonProperty("header")
    @NotNull
    private RequestHeader header;

    @JsonProperty("message")
    @NotNull
    private JsonNode message;

    public RequestJsonMessage(){
        UUID uuid = UUID.randomUUID();
        this.id = uuid.toString();
    }

}
