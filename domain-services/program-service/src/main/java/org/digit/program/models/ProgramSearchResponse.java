package org.digit.program.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class ProgramSearchResponse {

    @JsonProperty("header")
    RequestHeader header;

    @JsonProperty("programs")
    List<Program> programs;

}
