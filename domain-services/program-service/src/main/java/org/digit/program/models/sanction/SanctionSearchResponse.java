package org.digit.program.models.sanction;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.digit.program.models.RequestHeader;

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
