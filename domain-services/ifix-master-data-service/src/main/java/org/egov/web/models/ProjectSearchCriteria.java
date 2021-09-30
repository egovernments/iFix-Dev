package org.egov.web.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import lombok.*;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * The object contains all the search criteria of the Project
 */
@ApiModel(description = "The object contains all the search criteria of the Project")
@Validated
@javax.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2021-08-02T16:24:12.742+05:30")

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProjectSearchCriteria {
    @JsonProperty("Ids")
    @Valid
    private List<String> ids = null;

    @JsonProperty("tenantId")
    private String tenantId = null;

    @JsonProperty("name")
    private String name = null;

    @JsonProperty("code")
    private String code = null;

    @JsonProperty("expenditureId")
    private String expenditureId = null;

    @JsonProperty("departmentEntitytId")
    private String getDepartmentEntitytId = null;

    @JsonProperty("locationId")
    private String locationId = null;


    public ProjectSearchCriteria addIdsItem(String idsItem) {
        if (this.ids == null) {
            this.ids = new ArrayList<>();
        }
        this.ids.add(idsItem);
        return this;
    }

}

