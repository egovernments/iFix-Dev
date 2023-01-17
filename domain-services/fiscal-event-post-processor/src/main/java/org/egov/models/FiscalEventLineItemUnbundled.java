package org.egov.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import lombok.*;
import org.egov.common.contract.AuditDetails;
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

    @JsonProperty("version")
    private String version = null;

    @JsonProperty("id")
    private String id = null;

    @JsonProperty("eventId")
    private String eventId = null;

    @JsonProperty("tenantId")
    private String tenantId = null;

    @JsonProperty("eventType")
    private String eventType = null;

    @JsonProperty("ingestionTime")
    private Long ingestionTime = null;

    @JsonProperty("eventTime")
    private Long eventTime = null;

    @JsonProperty("referenceId")
    private String referenceId = null;

    @JsonProperty("linkedEventId")
    private String linkedEventId = null;

    @JsonProperty("linkedReferenceId")
    private String linkedReferenceId = null;

    @JsonProperty("amount")
    private BigDecimal amount = null;

    @JsonProperty("coa")
    private ChartOfAccount coa = null;

    @JsonProperty("fromBillingPeriod")
    private Long fromBillingPeriod = null;

    @JsonProperty("toBillingPeriod")
    private Long toBillingPeriod = null;

    @JsonProperty("auditDetails")
    private AuditDetails auditDetails = null;

    @JsonProperty("attributes")
    private Object attributes = null;

}

