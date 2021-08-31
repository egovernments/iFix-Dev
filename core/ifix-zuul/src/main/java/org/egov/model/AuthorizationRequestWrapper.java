package org.egov.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.egov.common.contract.request.RequestHeader;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class AuthorizationRequestWrapper {

    @JsonProperty("requestHeader")
    private RequestHeader requestHeader;

    @JsonProperty("AuthorizationRequest")
    private AuthorizationRequest authorizationRequest;

}
