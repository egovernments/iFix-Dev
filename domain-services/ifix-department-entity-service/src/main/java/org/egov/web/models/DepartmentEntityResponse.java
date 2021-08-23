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
 * Contains the ResponseHeader and the enriched Department Entity information
 */
@ApiModel(description = "Contains the ResponseHeader and the enriched Department Entity information")
@Validated
@javax.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2021-08-23T11:51:49.710+05:30")

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DepartmentEntityResponse {
    @JsonProperty("responseHeader")
    private ResponseHeader responseHeader = null;

    @JsonProperty("departmentEntity")
    @Valid
    private List<DepartmentEntity> departmentEntity = null;


    public DepartmentEntityResponse addDepartmentEntityItem(DepartmentEntity departmentEntityItem) {
        if (this.departmentEntity == null) {
            this.departmentEntity = new ArrayList<>();
        }
        this.departmentEntity.add(departmentEntityItem);
        return this;
    }

}

