package org.egov.web.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.annotations.ApiModel;
import lombok.*;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;

/**
 * This is the flattened structure of line item that will get persisted to Druid
 */
@ApiModel(description = "This is the flattened structure of line item that will get persisted to Druid")
@Validated
@javax.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2021-08-09T17:56:59.067+05:30")

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FiscalEventLineItemFlattened {
    @JsonProperty("id")
    private String id = null;

    @JsonProperty("eventId")
    private String eventId = null;

    @JsonProperty("tenantId")
    private String tenantId = null;

    @JsonProperty("government.id")
    private String governmentId = null;

    @JsonProperty("government.name")
    private String governmentName = null;

    @JsonProperty("department.id")
    private String departmentId = null;

    @JsonProperty("department.code")
    private String departmentCode = null;

    @JsonProperty("department.name")
    private String departmentName = null;

    @JsonProperty("eat.id")
    private String eatId = null;

    @JsonProperty("eat.code")
    private String eatCode = null;

    @JsonProperty("eat.name")
    private String eatName = null;
    @JsonProperty("eat.type")
    private EatTypeEnum eatType = null;
    @JsonProperty("project.id")
    private String projectId = null;
    @JsonProperty("project.code")
    private String projectCode = null;
    @JsonProperty("project.name")
    private String projectName = null;
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
    @JsonProperty("coa.id")
    private String coaId = null;
    @JsonProperty("coa.coaCode")
    private String coaCoaCode = null;
    @JsonProperty("coa.majorHead")
    private String coaMajorHead = null;
    @JsonProperty("coa.majorHeadName")
    private String coaMajorHeadName = null;
    @JsonProperty("coa.majorHeadType")
    private String coaMajorHeadType = null;
    @JsonProperty("coa.subMajorHead")
    private String coaSubMajorHead = null;
    @JsonProperty("coa.subMajorHeadName")
    private String coaSubMajorHeadName = null;
    @JsonProperty("coa.minorHead")
    private String coaMinorHead = null;
    @JsonProperty("coa.minorHeadName")
    private String coaMinorHeadName = null;
    @JsonProperty("coa.subHead")
    private String coaSubHead = null;
    @JsonProperty("coa.subHeadName")
    private String coaSubHeadName = null;
    @JsonProperty("coa.groupHead")
    private String coaGroupHead = null;
    @JsonProperty("coa.groupHeadName")
    private String coaGroupHeadName = null;
    @JsonProperty("coa.objectHead")
    private String coaObjectHead = null;
    @JsonProperty("coa.objectHeadName")
    private String coaObjectHeadName = null;
    @JsonProperty("fromBillingPeriod")
    private Long fromBillingPeriod = null;
    @JsonProperty("toBillingPeriod")
    private Long toBillingPeriod = null;

    /**
     * Type of the EAT
     */
    public enum EatTypeEnum {
        SCHEME("Scheme"),

        NON_SCHEME("Non-Scheme");

        private String value;

        EatTypeEnum(String value) {
            this.value = value;
        }

        @JsonCreator
        public static EatTypeEnum fromValue(String text) {
            for (EatTypeEnum b : EatTypeEnum.values()) {
                if (String.valueOf(b.value).equals(text)) {
                    return b;
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


}

