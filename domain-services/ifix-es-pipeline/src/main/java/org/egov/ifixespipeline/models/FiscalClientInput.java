package org.egov.ifixespipeline.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class FiscalClientInput {

    @JsonProperty("topic")
    private String topic;

    @JsonProperty("json")
    private String json;

}
