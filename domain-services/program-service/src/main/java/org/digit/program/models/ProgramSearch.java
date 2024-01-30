package org.digit.program.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ProgramSearch {

    @JsonProperty("ids")
    private List<String> ids;

    @JsonProperty("parent_id")
    private String parentId;

    @JsonProperty("name")
    private String name;

    @JsonProperty("program_code")
    private String programCode;

    @JsonProperty("location_code")
    private String locationCode;

    @JsonProperty("pagination")
    private Pagination pagination;


}
