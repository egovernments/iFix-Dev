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
public class AllocationSearchRequest {


    @JsonProperty("signature")
    private String signature;

    @NotNull
    @JsonProperty("header")
    @Valid
    private RequestHeader header;

    @NotNull
    @JsonProperty("message")
    @Valid
    private AllocationSearch allocationSearch;

}
