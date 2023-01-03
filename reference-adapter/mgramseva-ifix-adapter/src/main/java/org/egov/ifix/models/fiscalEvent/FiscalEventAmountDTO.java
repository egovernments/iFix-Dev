package org.egov.ifix.models.fiscalEvent;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FiscalEventAmountDTO {
    @JsonProperty("id")
    private String id = null;

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

