package org.egov.ifixmigrationtoolkit.models.adaptermasterdata.projectnew;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.egov.common.contract.request.RequestHeader;
import org.springframework.validation.annotation.Validated;

/**
 * ProjectConst request along with request metadata
 */
@Validated
@javax.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2021-08-02T16:24:12.742+05:30")

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProjectRequestNew {
    @JsonProperty("requestHeader")
    private RequestHeader requestHeader = null;

    @JsonProperty("project")
    private ProjectDTONew projectDTO = null;


}

