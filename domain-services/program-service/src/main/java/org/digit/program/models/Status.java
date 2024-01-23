package org.digit.program.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter
@Setter
public class Status {

    @JsonProperty("status_code")
    private org.digit.program.constants.Status statusCode;

    @JsonProperty("status_message")
    private String statusMessage;

}
