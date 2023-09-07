package org.egov.ifixmigrationtoolkit.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class MigrationCount {

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

    @JsonProperty("createdTime")
    private Long createdTime = null;

}
