package org.egov.ifixmigrationtoolkit.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.egov.common.contract.response.ResponseHeader;

import javax.validation.Valid;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DepartmentEntityResponse {
    @JsonProperty("responseHeader")
    private ResponseHeader responseHeader = null;

    @JsonProperty("departmentEntity")
    @Valid
    private List<DepartmentEntity> departmentEntity = null;
}
