package org.digit.program.models.disburse;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.digit.program.models.Pagination;

import jakarta.validation.constraints.NotNull;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DisburseSearch {


    @JsonProperty("ids")
    private List<String> ids;

    @JsonProperty("location_code")
    @NotNull
    private String locationCode;

    @JsonProperty("program_code")
    private String programCode;

    @JsonProperty("target_id")
    private String targetId;

    @JsonProperty("transaction_id")
    private String transactionId;

    @JsonProperty("pagination")
    private Pagination pagination;

}
