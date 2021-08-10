package org.egov.web.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import lombok.*;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;

/**
 * This is the unbundled line item into individual record from a given fiscal event.
 */
@ApiModel(description = "This is the unbundled line item into individual record from a given fiscal event.")
@Validated
@javax.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2021-08-09T17:56:59.067+05:30")

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FiscalEventLineItemUnbundled {
    @JsonProperty("id")
    private String id = null;

    @JsonProperty("eventId")
    private String eventId = null;

    @JsonProperty("tenantId")
    private String tenantId = null;

    @JsonProperty("government")
    private Government government = null;

    @JsonProperty("department")
    private Department department = null;

    @JsonProperty("eat")
    private EAT eat = null;

    @JsonProperty("project")
    private Project project = null;

    @JsonProperty("eventType")
    private String eventType = null;

    @JsonProperty("eventTime")
    private Long eventTime = null;

    @JsonProperty("referenceId")
    private String referenceId = null;

    @JsonProperty("parentEventId")
    private String parentEventId = null;

    @JsonProperty("parentReferenceId")
    private String parentReferenceId = null;

    @JsonProperty("amount")
    private BigDecimal amount = null;

    @JsonProperty("coa")
    private ChartOfAccount coa = null;

    @JsonProperty("fromBillingPeriod")
    private Long fromBillingPeriod = null;

    @JsonProperty("toBillingPeriod")
    private Long toBillingPeriod = null;


}

