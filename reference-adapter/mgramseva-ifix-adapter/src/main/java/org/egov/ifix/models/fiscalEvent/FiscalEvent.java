package org.egov.ifix.models.fiscalEvent;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.egov.common.contract.AuditDetails;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FiscalEvent {

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
    private List<String> receivers = new ArrayList<>();

    @JsonProperty("eventType")
    private EventTypeEnum eventType = null;

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
    private List<FiscalEventAmountDTO> amountDetails = new ArrayList<>();

    @JsonProperty("auditDetails")
    private AuditDetails auditDetails = null;

    @JsonProperty("attributes")
    private Object attributes = null;

    public FiscalEvent addAmountDetailsItem(FiscalEventAmountDTO amountDetailsItem) {
        this.amountDetails.add(amountDetailsItem);
        return this;
    }

    public enum EventTypeEnum {
        Sanction("SANCTION"),
        Appropriation("APPROPRIATION"),
        Allocation("ALLOCATION"),
        Intra_Transfer("INTRA_TRANSFER"),
        Inter_Transfer("INTER_TRANSFER"),
        Demand("DEMAND"),
        Receipt("RECEIPT"),
        Bill("BILL"),
        Payment("PAYMENT");

        private String value;

        EventTypeEnum(String value) {
            this.value = value;
        }

        @JsonCreator
        public static EventTypeEnum fromValue(String text) {
            for (EventTypeEnum eventTypeEnum : EventTypeEnum.values()) {
                if (String.valueOf(eventTypeEnum.value).equals(text)) {
                    return eventTypeEnum;
                }
            }
            return null;
        }

        @Override
        @JsonValue
        public String toString() {
            return name();
        }
    }

}

