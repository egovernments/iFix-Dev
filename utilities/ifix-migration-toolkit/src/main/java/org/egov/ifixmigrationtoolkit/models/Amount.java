package org.egov.ifixmigrationtoolkit.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.math.BigDecimal;

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

