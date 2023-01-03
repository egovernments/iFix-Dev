package org.egov.ifix.models;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.egov.common.contract.AuditDetails;
import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * This object captures the fiscal information of external systems.
 */
@Validated
@javax.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2021-08-09T17:28:42.515+05:30")

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class FiscalEvent {
    @JsonProperty("id")
    private String id = null;

    @JsonProperty("tenantId")
    private String tenantId = null;

    @JsonProperty("projectId")
    private String projectId = null;

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

    @JsonProperty("amountDetails")
    @Valid
    private List<Amount> amountDetails = new ArrayList<>();

    @JsonProperty("auditDetails")
    private AuditDetails auditDetails = null;

    @JsonProperty("attributes")
    private Object attributes = null;


    public FiscalEvent addAmountDetailsItem(Amount amountDetailsItem) {
        this.amountDetails.add(amountDetailsItem);
        return this;
    }

}
