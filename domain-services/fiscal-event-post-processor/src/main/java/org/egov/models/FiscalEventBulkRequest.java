package org.egov.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.egov.common.contract.request.RequestHeader;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FiscalEventBulkRequest {


    @JsonProperty("requestHeader")
    private RequestHeader requestHeader = null;

    @JsonProperty("fiscalEvent")
    private List<FiscalEvent> fiscalEvent = null;

}
