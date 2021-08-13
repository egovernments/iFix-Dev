package org.egov.web.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import lombok.*;
import org.springframework.validation.annotation.Validated;

/**
 * Captures the Project attributes
 */
@ApiModel(description = "Captures the Project attributes")
@Validated
@javax.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2021-08-09T17:56:59.067+05:30")

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Project {
    @JsonProperty("id")
    private String id = null;

    @JsonProperty("code")
    private String code = null;

    @JsonProperty("name")
    private String name = null;

    @JsonProperty("eatId")
    private String eatId = null;

    @JsonProperty("departmentId")
    private String departmentId = null;


}

