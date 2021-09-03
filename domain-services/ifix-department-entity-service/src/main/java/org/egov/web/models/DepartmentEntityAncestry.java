package org.egov.web.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import lombok.*;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * This object captures the information for department entity
 */
@ApiModel(description = "This object captures the information for department entity")
@Validated
@javax.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2021-08-23T11:51:49.710+05:30")

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DepartmentEntityAncestry extends DepartmentEntityAbstract {

    @JsonProperty("children")
    @Valid
    private List<DepartmentEntityAncestry> children = new ArrayList<>();

    public DepartmentEntityAncestry addChildrenItem(DepartmentEntityAncestry childrenItem) {
        this.children.add(childrenItem);
        return this;
    }

}

