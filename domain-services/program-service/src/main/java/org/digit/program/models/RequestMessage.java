package org.digit.program.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RequestMessage {

    @JsonProperty("signature")
    private String signature;

    @JsonProperty("header")
    @NotNull
    private RequestHeader header;

    @NotNull
    @JsonProperty("message")
    private String message;

}
