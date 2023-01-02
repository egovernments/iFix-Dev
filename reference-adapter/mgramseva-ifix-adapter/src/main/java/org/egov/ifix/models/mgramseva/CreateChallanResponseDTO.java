package org.egov.ifix.models.mgramseva;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateChallanResponseDTO {
    @JsonProperty("responseInfo")
    private ResponseInfoDTO responseInfo;
    @JsonProperty("challans")
    private List<ChallanResponseDTO> challans = null;
}
