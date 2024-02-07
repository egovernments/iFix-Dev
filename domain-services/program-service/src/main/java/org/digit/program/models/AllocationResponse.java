package org.digit.program.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AllocationResponse {

    @JsonProperty("header")
    RequestHeader header;

    @JsonProperty("allocations")
    List<Allocation> allocations;

}
