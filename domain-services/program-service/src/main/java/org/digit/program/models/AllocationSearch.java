package org.digit.program.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AllocationSearch {

    @JsonProperty("ids")
    private List<String> ids;

    @JsonProperty("location_code")
    private String locationCode;

    @JsonProperty("program_code")
    private String programCode;

    @JsonProperty("sanction_id")
    private String sanctionId;

    @JsonProperty("pagination")
    private Pagination pagination;

}
