package org.egov.ifix.models.fiscalEvent;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * The object contains all the search criteria of the fiscal events
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FiscalSearchCriteriaDTO {
    @JsonProperty("Ids")
    private List<String> ids = null;

    @JsonProperty("tenantId")
    private String tenantId = null;

    @JsonProperty("eventType")
    private String eventType = null;

    @JsonProperty("fromEventTime")
    private Long fromEventTime = null;

    @JsonProperty("toEventTime")
    private Long toEventTime = null;

    @JsonProperty("referenceId")
    private List<String> referenceId = null;

    @JsonProperty("receiver")
    private String receiver = null;

    @JsonProperty("fromIngestionTime")
    private Long fromIngestionTime = null;

    @JsonProperty("toIngestionTime")
    private Long toIngestionTime = null;


}

