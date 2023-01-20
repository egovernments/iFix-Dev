package org.egov.ifixmigrationtoolkit.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PersisterDepartmentHierarchyLevel {
    @JsonProperty("id")
    private String id = null;

    @JsonProperty("tenantId")
    private String tenantId = null;

    @JsonProperty("departmentId")
    private String departmentId = null;

    @JsonProperty("label")
    private String label = null;

    @JsonProperty("parent")
    private String parent = null;

    @JsonProperty("level")
    private Integer level = null;

    @JsonProperty("createdBy")
    private String createdBy = null;

    @JsonProperty("lastModifiedBy")
    private String lastModifiedBy = null;

    @JsonProperty("createdTime")
    private Long createdTime = null;

    @JsonProperty("lastModifiedTime")
    private Long lastModifiedTime = null;


}

