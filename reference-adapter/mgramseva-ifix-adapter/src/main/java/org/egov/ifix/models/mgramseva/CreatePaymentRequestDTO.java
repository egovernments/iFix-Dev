package org.egov.ifix.models.mgramseva;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreatePaymentRequestDTO {
    @JsonProperty("RequestInfo")
    private RequestInfoDTO requestInfo;
    @JsonProperty("Payment")
    private PaymentDTO payment;
}
