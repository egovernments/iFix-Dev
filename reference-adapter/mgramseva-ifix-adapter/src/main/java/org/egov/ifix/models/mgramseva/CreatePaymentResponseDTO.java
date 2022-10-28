package org.egov.ifix.models.mgramseva;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreatePaymentResponseDTO {
    @JsonProperty("ResponseInfo")
    public Object responseInfo;
    @JsonProperty("Payments")
    public List<PaymentDTO> payments = null;
}
