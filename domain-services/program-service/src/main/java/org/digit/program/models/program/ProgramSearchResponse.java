package org.digit.program.models.program;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.digit.program.models.RequestHeader;

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
