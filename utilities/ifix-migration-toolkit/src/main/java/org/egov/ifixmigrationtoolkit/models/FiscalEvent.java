package org.egov.ifixmigrationtoolkit.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.*;
import org.egov.common.contract.AuditDetails;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
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
    private List<Amount> amountDetails = new ArrayList<>();

    @JsonProperty("auditDetails")
    private AuditDetails auditDetails = null;

    @JsonProperty("attributes")
    private Object attributes = null;


    public enum EventTypeEnum {
        Sanction("Sanction"),
        Appropriation("Appropriation"),
        Allocation("Allocation"),
        Intra_Transfer("Intra_Transfer"),
        Inter_Transfer("Inter_Transfer"),
        Demand("Demand"),
        Receipt("Receipt"),
        Bill("Bill"),
        Payment("Payment");

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
            return String.valueOf(value);
        }
    }
    public FiscalEvent addAmountDetailsItem(Amount amountDetailsItem) {
        this.amountDetails.add(amountDetailsItem);
        return this;
    }

}