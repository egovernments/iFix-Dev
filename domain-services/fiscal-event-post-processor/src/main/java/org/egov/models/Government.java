package org.egov.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.validation.annotation.Validated;

/**
 * Government
 */
@Validated
@javax.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2021-08-09T17:56:59.067+05:30")

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Government {
    @JsonProperty("id")
    private String id = null;

    @JsonProperty("name")
    private String name = null;


}

