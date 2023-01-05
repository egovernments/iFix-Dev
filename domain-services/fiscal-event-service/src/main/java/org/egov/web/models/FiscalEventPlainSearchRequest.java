package org.egov.web.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.egov.common.contract.request.RequestHeader;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FiscalEventPlainSearchRequest {
    @JsonProperty("requestHeader")
    private RequestHeader requestHeader = null;

    @JsonProperty("criteria")
    private PlainsearchCriteria criteria = null;
}
