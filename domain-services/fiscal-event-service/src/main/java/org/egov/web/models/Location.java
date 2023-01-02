package org.egov.web.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Location {

    @JsonProperty("code")
    private String code = null;

    @JsonProperty("hierarchyType")
    private String hierarchyType = null;

    @JsonProperty("name")
    private String name = null;

    @JsonProperty("child")
    @Valid
    private List<String> child = new ArrayList<>();

}
