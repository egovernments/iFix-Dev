package org.egov.ifixmigrationtoolkit.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.egov.common.contract.request.RequestHeader;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class MigrationRequest {

    @JsonProperty("requestHeader")
    private RequestHeader requestHeader = null;

    @JsonProperty("tenantId")
    private String tenantId = null;

}
