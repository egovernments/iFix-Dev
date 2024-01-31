package org.digit.program.models;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.digit.program.constants.SortOrder;

@Getter
@Setter
@Builder
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
