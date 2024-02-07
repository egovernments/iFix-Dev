package org.digit.program.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProgramSearchResponse {

    @JsonProperty("header")
    RequestHeader header;

    @JsonProperty("programs")
    List<Program> programs;

}
