package org.egov.web.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import lombok.*;
import org.egov.common.contract.AuditDetails;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * Captures the Project attributes
 */
@ApiModel(description = "Captures the Project attributes")
@Validated
@javax.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2021-08-02T16:24:12.742+05:30")

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Project {
    @JsonProperty("id")
    private String id = null;

    @JsonProperty("tenantId")
    private String tenantId = null;

    @JsonProperty("code")
    private String code = null;

    @JsonProperty("name")
    private String name = null;

    @JsonProperty("expenditureId")
    private String expenditureId = null;

    @JsonProperty("departmentEntitytId")
    private String departmentEntitytId = null;

    @JsonProperty("departmentEntity")
    private DepartmentEntity departmentEntity = null;

    @JsonProperty("locationIds")
    @Valid
    private List<String> locationIds = null;

    @JsonProperty("auditDetails")
    private AuditDetails auditDetails = null;


    public Project addLocationIdsItem(String locationIdsItem) {
        if (this.locationIds == null) {
            this.locationIds = new ArrayList<>();
        }
        this.locationIds.add(locationIdsItem);
        return this;
    }

}

