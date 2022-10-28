package org.egov.ifix.models;

import java.math.BigDecimal;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Capture the transaction amount and chart of account corresponding to the transaction amount
 */
@Validated
@javax.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2021-08-09T17:28:42.515+05:30")

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class Amount {
    @JsonProperty("id")
    private String id = null;

    @JsonProperty("amount")
    private BigDecimal amount = null;

    @JsonProperty("coaCode")
    private String coaCode = null;

    @JsonProperty("fromBillingPeriod")
    private Long fromBillingPeriod = null;

    @JsonProperty("toBillingPeriod")
    private Long toBillingPeriod = null;


}
