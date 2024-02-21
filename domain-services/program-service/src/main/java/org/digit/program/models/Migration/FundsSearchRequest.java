package org.digit.program.models.Migration;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.digit.program.models.Pagination;
import org.egov.common.contract.request.RequestInfo;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Validated
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FundsSearchRequest {
    @JsonProperty("RequestInfo")
    @NotNull
    @Valid
    private RequestInfo requestInfo = null;

    @JsonProperty("searchCriteria")
    @Valid
    @NotNull
    private SanctionDetailsSearchCriteria searchCriteria = null;

    @JsonProperty("pagination")
    @Valid
    private Pagination pagination = null;
}
