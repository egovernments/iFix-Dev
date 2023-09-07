package org.egov.ifixmigrationtoolkit.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class DepartmentEntityMigrationCountWrapper {

    @JsonProperty("DepartmentEntityMigrationCount")
    private DepartmentEntityMigrationCount migrationCount;

}
