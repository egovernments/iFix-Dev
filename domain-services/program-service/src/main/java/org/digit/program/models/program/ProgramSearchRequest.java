package org.digit.program.models.program;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.digit.program.models.RequestHeader;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProgramSearchRequest {

    @JsonProperty("signature")
    private String signature;

    @NotNull
    @JsonProperty("header")
    @Valid
    private RequestHeader header;

    @JsonProperty("message")
    @NotNull
    @Valid
    private ProgramSearch programSearch;

}
