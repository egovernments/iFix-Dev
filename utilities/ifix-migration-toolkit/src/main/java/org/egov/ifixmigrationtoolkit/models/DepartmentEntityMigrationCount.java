package org.egov.ifixmigrationtoolkit.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class DepartmentEntityMigrationCount {

    @JsonProperty("id")
    private String id = null;

    @JsonProperty("tenantId")
    private String tenantId = null;

    @JsonProperty("pageNumber")
    private Integer pageNumber = null;

    @JsonProperty("batchSize")
    private Integer batchSize = null;

    @JsonProperty("totalNumberOfRecordsMigrated")
    private Long totalNumberOfRecordsMigrated = null;

    @JsonProperty("service_type")
    private String serviceType = null;

    @JsonProperty("createdTime")
    private Long createdTime = null;

}
