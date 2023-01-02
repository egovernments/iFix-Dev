package org.egov.ifixmigrationtoolkit.models;

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
@EqualsAndHashCode
public class COARequest {

    @JsonProperty("requestHeader")
    private RequestHeader requestHeader = null;

    @JsonProperty("chartOfAccount")
    private List<ChartOfAccount> chartOfAccount = null;


}
