package org.digit.program.models.program;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.digit.program.models.Pagination;

import javax.validation.constraints.NotNull;
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

    @JsonProperty("is_active")
    private String isActive;

    @JsonProperty("pagination")
    private Pagination pagination;


}
