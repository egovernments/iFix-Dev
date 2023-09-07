package org.egov.web.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import lombok.*;
import org.egov.common.contract.request.RequestHeader;
import org.springframework.validation.annotation.Validated;

/**
 * DepartmentConst request along with request metadata
 */
@ApiModel(description = "DepartmentConst request along with request metadata")
@Validated
@javax.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2021-08-02T16:24:12.742+05:30")

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DepartmentRequest {
    @JsonProperty("requestHeader")
    private RequestHeader requestHeader = null;

    @JsonProperty("department")
    private DepartmentDTO departmentDTO = null;


}

