package org.egov.ifixmigrationtoolkit.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PersisterDepartmentEntityChildren {
    @JsonProperty("childId")
    private String childId = null;

    @JsonProperty("status")
    private Boolean status = null;
}
