package org.egov.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import lombok.*;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;

/**
 * Capture the transaction amount and chart of account corresponding to the transaction amount
 */
@ApiModel(description = "Capture the transaction amount and chart of account corresponding to the transaction amount")
@Validated
@javax.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2021-08-09T17:56:59.067+05:30")

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Amount {
    @JsonProperty("id")
    private String id = null;

    @JsonProperty("amount")
    private BigDecimal amount = null;

    @JsonProperty("coaId")
    private String coaId = null;

    @JsonProperty("fromBillingPeriod")
    private Long fromBillingPeriod = null;

    @JsonProperty("toBillingPeriod")
    private Long toBillingPeriod = null;


}

