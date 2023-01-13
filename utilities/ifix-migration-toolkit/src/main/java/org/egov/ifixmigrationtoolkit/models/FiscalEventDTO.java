package org.egov.ifixmigrationtoolkit.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class FiscalEventDTO {

    @JsonProperty("version")
    private String version = null;

    @JsonProperty("id")
    private String id = null;

    @JsonProperty("tenantId")
    private String tenantId = null;

    @JsonProperty("sender")
    private String sender = null;

    @JsonProperty("receivers")
    @Valid
    private List<ReceiverDTO> receivers = new ArrayList<>();

    @JsonProperty("eventType")
    private FiscalEvent.EventTypeEnum eventType = null;

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

    @JsonProperty("amountDetails")
    @Valid
    private List<Amount> amountDetails = new ArrayList<>();

    @JsonProperty("auditDetails")
    private AuditDetails auditDetails = null;

    @JsonProperty("attributes")
    private Object attributes = null;


    public FiscalEventDTO addAmountDetailsItem(Amount amountDetailsItem) {
        this.amountDetails.add(amountDetailsItem);
        return this;
    }

}
