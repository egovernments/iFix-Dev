package org.digit.program.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class AllocationSearchRequest {


    @JsonProperty("signature")
    private String signature;

    @NotNull
    @JsonProperty("header")
    private RequestHeader header;

    @NotNull
    @JsonProperty("message")
    private AllocationSearch allocationSearch;

}
