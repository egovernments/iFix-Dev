package org.egov.ifix.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CoaMappingDTO {
    @JsonProperty("clientCode")
    private String clientCode;

    @JsonProperty("iFixCoaCode")
    private String iFixCoaCode;
}
