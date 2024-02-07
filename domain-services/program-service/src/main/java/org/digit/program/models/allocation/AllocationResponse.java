package org.digit.program.models.allocation;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.digit.program.models.RequestHeader;

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
