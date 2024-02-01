package org.digit.program.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Getter
@Setter
public class SanctionSearch {

    @JsonProperty("ids")
    private List<String> ids;

    @JsonProperty("location_code")
    private String locationCode;

    @JsonProperty("program_code")
    private String programCode;

    @JsonProperty("pagination")
    private Pagination pagination;

}
