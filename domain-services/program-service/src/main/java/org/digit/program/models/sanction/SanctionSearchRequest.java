package org.digit.program.models.sanction;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.digit.program.models.RequestHeader;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SanctionSearchRequest {

    @JsonProperty("signature")
    private String signature;

    @NotNull
    @JsonProperty("header")
    @Valid
    private RequestHeader header;

    @NotNull
    @JsonProperty("message")
    @Valid
    private SanctionSearch sanctionSearch;
}
