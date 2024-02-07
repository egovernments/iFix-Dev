package org.digit.program.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DisburseSearchResponse {

    @JsonProperty("header")
    @NotNull
    private RequestHeader header;

    @JsonProperty("disbursements")
    private List<Disbursement> disbursements;

}
