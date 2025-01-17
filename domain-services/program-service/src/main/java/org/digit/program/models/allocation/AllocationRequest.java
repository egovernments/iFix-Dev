package org.digit.program.models.allocation;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.digit.program.models.RequestHeader;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AllocationRequest {

    @JsonProperty("signature")
    private String signature;

    @JsonProperty("header")
    @NotNull
    @Valid
    private RequestHeader header;

    @JsonProperty("message")
    @NotNull
    @Valid
    private Allocation allocation;

}
