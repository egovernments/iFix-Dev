package org.egov.web.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import lombok.*;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * The object contains all the search criteria of the fiscal events
 */
@ApiModel(description = "The object contains all the search criteria of the fiscal events")
@Validated
@javax.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2021-08-09T17:28:42.515+05:30")

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Criteria {
    @JsonProperty("Ids")
    @Valid
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
    @Valid
    private List<String> referenceId = null;

    @JsonProperty("receiver")
    @Valid
    private String receiver = null;

    @JsonProperty("fromIngestionTime")
    private Long fromIngestionTime = null;

    @JsonProperty("toIngestionTime")
    private Long toIngestionTime = null;

    @JsonProperty("offset")
    private Long offSet;

    @JsonProperty("limit")
    private Long limit;


    public Criteria addIdsItem(String idsItem) {
        if (this.ids == null) {
            this.ids = new ArrayList<>();
        }
        this.ids.add(idsItem);
        return this;
    }

    public Criteria addReferenceIdItem(String referenceIdItem) {
        if (this.referenceId == null) {
            this.referenceId = new ArrayList<>();
        }
        this.referenceId.add(referenceIdItem);
        return this;
    }


}

