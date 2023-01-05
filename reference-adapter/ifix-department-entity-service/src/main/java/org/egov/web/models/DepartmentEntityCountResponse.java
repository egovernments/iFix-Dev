package org.egov.web.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.egov.common.contract.response.ResponseHeader;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DepartmentEntityCountResponse {
    @JsonProperty("responseHeader")
    private ResponseHeader responseHeader = null;

    @JsonProperty("count")
    private Long count = null;

}
