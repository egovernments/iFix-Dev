package org.digit.program.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.validation.constraints.NotNull;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Slf4j
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RequestMessage {

    @JsonProperty("id")
    private String id;
    @JsonProperty("signature")
    private String signature;

    @JsonProperty("header")
    @NotNull
    private RequestHeader header;

    @NotNull
    @JsonProperty("message")
    private List<String> message;

//    public RequestMessage(){
//        UUID uuid = UUID.randomUUID();
//        this.id = uuid.toString();
//    }

}
