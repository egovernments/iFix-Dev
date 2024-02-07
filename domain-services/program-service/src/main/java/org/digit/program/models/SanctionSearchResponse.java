package org.digit.program.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SanctionSearchResponse {

    @JsonProperty("header")
    RequestHeader header;

    @JsonProperty("sanctions")
    List<Sanction> sanctions;

}
