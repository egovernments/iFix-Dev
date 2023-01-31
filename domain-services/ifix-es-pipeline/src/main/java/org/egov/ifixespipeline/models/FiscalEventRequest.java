package org.egov.ifixespipeline.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.egov.common.contract.request.RequestHeader;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class FiscalEventRequest {

    @JsonProperty("requestHeader")
    private RequestHeader requestHeader = null;

    @JsonProperty("fiscalEvent")
    private FiscalEvent fiscalEvent = null;

}
