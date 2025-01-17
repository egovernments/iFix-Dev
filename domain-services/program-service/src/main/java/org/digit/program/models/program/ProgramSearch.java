package org.digit.program.models.program;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.digit.program.constants.Status;
import org.digit.program.models.Pagination;

import jakarta.validation.constraints.NotNull;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProgramSearch {

    @JsonProperty("ids")
    private List<String> ids;

    @JsonProperty("location_code")
    @NotNull
    private String locationCode;

    @JsonProperty("parent_id")
    private String parentId;

    @JsonProperty("name")
    private String name;

    @JsonProperty("program_code")
    private String programCode;

    @JsonProperty("status")
    private Status status;

    @JsonProperty("pagination")
    private Pagination pagination;


}
