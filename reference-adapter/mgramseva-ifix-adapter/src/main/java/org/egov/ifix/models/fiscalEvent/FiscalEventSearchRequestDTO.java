package org.egov.ifix.models.fiscalEvent;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.egov.common.contract.request.RequestHeader;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FiscalEventSearchRequestDTO {
    @JsonProperty("requestHeader")
    private RequestHeader requestHeader = null;

    @JsonProperty("criteria")
    private FiscalSearchCriteriaDTO criteria = null;
}

