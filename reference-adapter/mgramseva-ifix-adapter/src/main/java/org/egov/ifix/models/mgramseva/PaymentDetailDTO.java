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
public class PaymentDetailDTO {
    @JsonProperty("id")
    private String id;
    @JsonProperty("tenantId")
    private String tenantId;
    @JsonProperty("totalDue")
    private Integer totalDue;
    @JsonProperty("totalAmountPaid")
    private Double totalAmountPaid;
    @JsonProperty("manualReceiptNumber")
    private String manualReceiptNumber;
    @JsonProperty("receiptNumber")
    private String receiptNumber;
    @JsonProperty("receiptType")
    private String receiptType;
    @JsonProperty("receiptDate")
    private Long receiptDate;
    @JsonProperty("businessService")
    private String businessService;
    @JsonProperty("billId")
    private String billId;
    @JsonProperty("bill")
    private BillDTO  bill;
    @JsonProperty("additionalDetails")
    private Integer additionalDetails;
}
