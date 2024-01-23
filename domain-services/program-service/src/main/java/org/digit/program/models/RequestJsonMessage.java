package org.digit.program.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

@Embeddable
@Getter
@Setter
@Slf4j
public class RequestJsonMessage {

    @Id
    private String id;
    @JsonProperty("signature")
    private String signature;

    @JsonProperty("header")
    @NotNull
    private RequestHeader header;

    @NotNull
    @JsonProperty("message")
    private JsonNode message;

    public RequestJsonMessage(){
        UUID uuid = UUID.randomUUID();
        this.id = uuid.toString();
    }

}
