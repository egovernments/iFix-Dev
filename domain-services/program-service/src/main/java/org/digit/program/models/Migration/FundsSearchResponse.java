package org.digit.program.models.Migration;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.egov.common.contract.response.ResponseInfo;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Validated
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FundsSearchResponse {

    @JsonProperty("ResponseInfo")
    private ResponseInfo responseInfo = null;

    @JsonProperty("funds")
    List<SanctionDetail> funds = null;

}
