package org.digit.program.models.sanction;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.digit.program.models.RequestHeader;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SanctionRequest {

    @JsonProperty("signature")
    private String signature;

    @JsonProperty("header")
    @NotNull
    @Valid
    private RequestHeader header;

    @JsonProperty("message")
    @NotNull
    @Valid
    private Sanction sanction;
}
