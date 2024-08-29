package org.digit.program.models.allocation;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.digit.program.models.Pagination;

import jakarta.validation.constraints.NotNull;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AllocationSearch {

    @JsonProperty("ids")
    private List<String> ids;

    @JsonProperty("location_code")
    @NotNull
    private String locationCode;

    @JsonProperty("program_code")
    private String programCode;

    @JsonProperty("sanction_id")
    private String sanctionId;

    @JsonProperty("pagination")
    private Pagination pagination;

}
