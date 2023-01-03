package org.egov.ifix.models.mgramseva;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TaxHeadEstimateResponseDTO {
    @JsonProperty("taxHeadCode")
    private String taxHeadCode;
    @JsonProperty("estimateAmount")
    private Integer estimateAmount;
    @JsonProperty("category")
    private Object category;
}
