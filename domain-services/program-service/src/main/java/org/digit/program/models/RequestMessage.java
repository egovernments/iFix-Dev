package org.digit.program.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

@Embeddable
@Getter
@Setter
@Slf4j
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RequestMessage {

    @Id
    private String id;
    @JsonProperty("signature")
    private String signature;

    @JsonProperty("header")
    @NotNull
    @Transient
    private RequestHeader header;

    @NotNull
    @JsonProperty("message")
    private String message;

//    public RequestMessage(){
//        UUID uuid = UUID.randomUUID();
//        this.id = uuid.toString();
//    }

}
