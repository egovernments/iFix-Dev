package org.egov.web.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.annotations.ApiModel;
import lombok.*;
import org.egov.common.contract.AuditDetails;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * This object captures the information for fund and sub-fund
 */
@ApiModel(description = "This object captures the information for fund and sub-fund")
@Validated
@javax.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2021-08-02T16:24:12.742+05:30")

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Fund {
    @JsonProperty("id")
    private String id = null;

    @JsonProperty("tenantId")
    private String tenantId = null;

    @JsonProperty("code")
    private String code = null;

    @JsonProperty("name")
    private String name = null;
    @JsonProperty("type")
    private TypeEnum type = null;
    @JsonProperty("subFund")
    @Valid
    private List<SubFund> subFund = new ArrayList<>();
    @JsonProperty("auditDetails")
    private AuditDetails auditDetails = null;

    public Fund addSubFundItem(SubFund subFundItem) {
        this.subFund.add(subFundItem);
        return this;
    }


    /**
     * type of fund
     */
    public enum TypeEnum {
        CSS("CSS"),

        FC("FC"),

        WB("WB");

        private String value;

        TypeEnum(String value) {
            this.value = value;
        }

        @JsonCreator
        public static TypeEnum fromValue(String text) {
            for (TypeEnum b : TypeEnum.values()) {
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

