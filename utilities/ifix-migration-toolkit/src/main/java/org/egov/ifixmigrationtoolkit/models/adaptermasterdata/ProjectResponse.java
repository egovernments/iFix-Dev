package org.egov.ifixmigrationtoolkit.models.adaptermasterdata;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.egov.common.contract.response.ResponseHeader;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * Contains the ResponseHeader and the enriched Project information
 */
@Validated
@javax.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2021-08-02T16:24:12.742+05:30")

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProjectResponse {
    @JsonProperty("responseHeader")
    private ResponseHeader responseHeader = null;

    @JsonProperty("project")
    @Valid
    private List<Project> project = null;


    public ProjectResponse addProjectItem(Project projectItem) {
        if (this.project == null) {
            this.project = new ArrayList<>();
        }
        this.project.add(projectItem);
        return this;
    }

}

