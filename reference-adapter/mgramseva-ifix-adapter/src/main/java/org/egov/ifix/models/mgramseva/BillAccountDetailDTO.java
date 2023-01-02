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
public class BillAccountDetailDTO {
    @JsonProperty("id")
    private String id;
    @JsonProperty("tenantId")
    private String tenantId;
    @JsonProperty("billDetailId")
    private String billDetailId;
    @JsonProperty("demandDetailId")
    private String demandDetailId;
    @JsonProperty("order")
    private Integer order;
    @JsonProperty("amount")
    private Float amount;
    @JsonProperty("adjustedAmount")
    private Integer adjustedAmount;
    @JsonProperty("taxHeadCode")
    private String taxHeadCode;
    @JsonProperty("additionalDetails")
    private Object additionalDetails;
    @JsonProperty("auditDetails")
    private Object auditDetails;
}
