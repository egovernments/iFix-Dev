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
public class ChallanRequestDTO {

    @JsonProperty("citizen")
    private Object citizen;
    @JsonProperty("auditDetails")
    private Object auditDetails;
    @JsonProperty("id")
    private Object id;
    @JsonProperty("tenantId")
    private String tenantId;
    @JsonProperty("businessService")
    private String businessService;
    @JsonProperty("consumerType")
    private String consumerType;
    @JsonProperty("typeOfExpense")
    private String typeOfExpense;
    @JsonProperty("vendor")
    private String vendor;
    @JsonProperty("vendorName")
    private String vendorName;
    @JsonProperty("amount")
    private List<AmountDTO> amount = null;
    @JsonProperty("billDate")
    private Long billDate;
    @JsonProperty("paidDate")
    private Long paidDate;
    @JsonProperty("billIssuedDate")
    private Long billIssuedDate;
    @JsonProperty("challanNo")
    private String challanNo;
    @JsonProperty("accountId")
    private String accountId;
    @JsonProperty("applicationStatus")
    private String applicationStatus;
    @JsonProperty("totalAmount")
    private String totalAmount;
    @JsonProperty("isBillPaid")
    private Boolean isBillPaid;
    @JsonProperty("filestoreid")
    private String fileStoreId;
    @JsonProperty("taxPeriodFrom")
    private Long taxPeriodFrom;
    @JsonProperty("taxPeriodTo")
    private Long taxPeriodTo;
    @JsonProperty("referenceId")
    private String referenceId;
}
