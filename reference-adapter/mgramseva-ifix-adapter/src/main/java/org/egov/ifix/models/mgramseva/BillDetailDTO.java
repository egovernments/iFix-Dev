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
public class BillDetailDTO {
    @JsonProperty("id")
    private String id;
    @JsonProperty("tenantId")
    private String tenantId;
    @JsonProperty("demandId")
    private String demandId;
    @JsonProperty("billId")
    private String billId;
    @JsonProperty("expiryDate")
    private Long expiryDate;
    @JsonProperty("amount")
    private Float amount;
    @JsonProperty("amountPaid")
    private Object amountPaid;
    @JsonProperty("fromPeriod")
    private Long fromPeriod;
    @JsonProperty("toPeriod")
    private Long toPeriod;
    @JsonProperty("additionalDetails")
    private Object additionalDetails;
    @JsonProperty("billAccountDetails")
    private List<BillAccountDetailDTO> billAccountDetails = null;
}
