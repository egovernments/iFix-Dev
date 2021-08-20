package org.egov.web.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import lombok.*;
import org.egov.common.contract.AuditDetails;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * It gets the whole master data objects based on the reference ids present in the incoming request
 */
@ApiModel(description = "It gets the whole master data objects based on the reference ids present in the incoming request")
@Validated
@javax.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2021-08-09T17:56:59.067+05:30")

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FiscalEventDeReferenced {

    @JsonProperty("version")
    private String version = null;

    @JsonProperty("id")
    private String id = null;

    @JsonProperty("tenantId")
    private String tenantId = null;

    @JsonProperty("government")
    private Government government = null;

    @JsonProperty("department")
    private Department department = null;

    @JsonProperty("expenditure")
    private Expenditure expenditure = null;

    @JsonProperty("project")
    private Project project = null;

    @JsonProperty("eventType")
    private String eventType = null;

    @JsonProperty("ingestionTime")
    private Long ingestionTime = null;

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
    private List<AmountDetailsDeReferenced> amountDetails = new ArrayList<>();

    @JsonProperty("auditDetails")
    private AuditDetails auditDetails = null;

    @JsonProperty("attributes")
    private Object attributes = null;

    public FiscalEventDeReferenced addAmountDetailsItem(AmountDetailsDeReferenced amountDetailsItem) {
        this.amountDetails.add(amountDetailsItem);
        return this;
    }

}

