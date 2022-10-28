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
public class PaymentDTO {
    @JsonProperty("tenantId")
    private String tenantId;
    @JsonProperty("id")
    private String id;
    @JsonProperty("totalDue")
    private Integer totalDue;
    @JsonProperty("totalAmountPaid")
    private Double totalAmountPaid;
    @JsonProperty("transactionNumber")
    private String transactionNumber;
    @JsonProperty("transactionDate")
    private Long transactionDate;
    @JsonProperty("paymentMode")
    private String paymentMode;
    @JsonProperty("instrumentDate")
    private Long instrumentDate;
    @JsonProperty("instrumentNumber")
    private String instrumentNumber;
    @JsonProperty("instrumentStatus")
    private String instrumentStatus;
    @JsonProperty("ifscCode")
    private String ifscCode;
    @JsonProperty("paymentDetails")
    private List<PaymentDetailDTO> paymentDetails = null;
    @JsonProperty("paidBy")
    private String paidBy;
    @JsonProperty("mobileNumber")
    private String mobileNumber;
    @JsonProperty("payerName")
    private String payerName;
    @JsonProperty("payerAddress")
    private String payerAddress;
    @JsonProperty("payerEmail")
    private String payerEmail;
    @JsonProperty("payerId")
    private String payerId;
    @JsonProperty("paymentStatus")
    private String paymentStatus;
}
