package org.digit.program.models;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.digit.program.constants.SortOrder;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Pagination {

    @JsonProperty("limit")
    private Integer limit;

    @JsonProperty("offset")
    private Integer offset;

    @JsonProperty("total_count")
    private Integer totalCount;

    @JsonProperty("sort_by")
    private String sortBy;

    @JsonProperty("sort_order")
    private SortOrder sortOrder;

}
