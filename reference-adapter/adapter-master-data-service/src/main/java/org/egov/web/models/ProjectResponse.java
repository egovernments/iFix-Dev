package org.egov.web.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import lombok.*;
import org.egov.common.contract.response.ResponseHeader;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * Contains the ResponseHeader and the enriched ProjectConst information
 */
@ApiModel(description = "Contains the ResponseHeader and the enriched ProjectConst information")
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
    private List<ProjectDTO> projectDTO = null;


    public ProjectResponse addProjectItem(ProjectDTO projectDTOItem) {
        if (this.projectDTO == null) {
            this.projectDTO = new ArrayList<>();
        }
        this.projectDTO.add(projectDTOItem);
        return this;
    }

}

