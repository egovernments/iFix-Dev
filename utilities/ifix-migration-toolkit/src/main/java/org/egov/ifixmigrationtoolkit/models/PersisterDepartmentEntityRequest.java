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
public class PersisterDepartmentEntityRequest {
    @JsonProperty("departmentEntity")
    private List<PersisterDepartmentEntity> departmentEntity = null;


}

