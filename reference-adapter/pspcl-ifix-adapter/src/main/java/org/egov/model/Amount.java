package org.egov.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;

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

    @JsonProperty("amount")
    private BigDecimal amount = null;

    @JsonProperty("coaCode")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String coaCode = null;

    @JsonProperty("coaId")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String coaId = null;

    @JsonProperty("fromBillingPeriod")
    private Long fromBillingPeriod = null;

    @JsonProperty("toBillingPeriod")
    private Long toBillingPeriod = null;

}
