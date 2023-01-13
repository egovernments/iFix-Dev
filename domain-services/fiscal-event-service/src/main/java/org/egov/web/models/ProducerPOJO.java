package org.egov.web.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.egov.common.contract.request.RequestHeader;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class ProducerPOJO {

    @JsonProperty("RequestHeader")
    private RequestHeader requestHeader;

    @JsonProperty("Records")
    private List<FiscalEvent> records;

}
