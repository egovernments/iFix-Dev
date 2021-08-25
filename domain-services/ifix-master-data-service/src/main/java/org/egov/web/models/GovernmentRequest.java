package org.egov.web.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import lombok.*;
import org.egov.common.contract.request.RequestHeader;
import org.springframework.validation.annotation.Validated;

/**
 * Tenant request along with request metadata
 */
@ApiModel(description = "Tenant request along with request metadata")
@Validated
@javax.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2021-08-02T16:24:12.742+05:30")

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@EqualsAndHashCode
public class GovernmentRequest {
    @JsonProperty("requestHeader")
    private RequestHeader requestHeader = null;

    @JsonProperty("government")
    private Government government = null;


}

