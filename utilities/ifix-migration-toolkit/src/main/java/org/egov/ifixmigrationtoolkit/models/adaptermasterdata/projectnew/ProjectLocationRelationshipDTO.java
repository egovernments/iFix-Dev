package org.egov.ifixmigrationtoolkit.models.adaptermasterdata.projectnew;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectLocationRelationshipDTO {
    @JsonProperty("locationId")
    private String locationId = null;

    @JsonProperty("status")
    private Boolean status = null;
}
