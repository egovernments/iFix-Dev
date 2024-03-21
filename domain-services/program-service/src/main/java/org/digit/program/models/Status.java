package org.digit.program.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Status {

    @JsonProperty("status_code")
    private org.digit.program.constants.Status statusCode;

    @JsonProperty("status_message")
    private String statusMessage;

}
