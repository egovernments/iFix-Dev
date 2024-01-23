package org.digit.program.models;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.digit.program.constants.SortOrder;

@Getter
@Setter
public class Pagination {

    @JsonProperty("limit")
    private double limit;

    @JsonProperty("offset")
    private double offset;

    @JsonProperty("total_count")
    private double totalCount;

    @JsonProperty("sort_by")
    private String sortBy;

    @JsonProperty("sort_order")
    private SortOrder sortOrder;

}
