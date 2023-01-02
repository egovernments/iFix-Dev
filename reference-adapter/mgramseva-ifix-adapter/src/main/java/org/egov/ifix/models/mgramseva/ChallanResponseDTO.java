package org.egov.ifix.models.mgramseva;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.egov.common.contract.AuditDetails;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChallanResponseDTO {
    @JsonProperty("citizen")
    private CitizenResponseDTO citizen;
    @JsonProperty("id")
    private String id;
    @JsonProperty("tenantId")
    private String tenantId;
    @JsonProperty("businessService")
    private String businessService;
    @JsonProperty("challanNo")
    private String challanNo;
    @JsonProperty("referenceId")
    private String referenceId;
    @JsonProperty("description")
    private String description;
    @JsonProperty("accountId")
    private String accountId;
    @JsonProperty("additionalDetail")
    private String additionalDetail;
    @JsonProperty("source")
    private String source;
    @JsonProperty("taxPeriodFrom")
    private Long taxPeriodFrom;
    @JsonProperty("taxPeriodTo")
    private Long taxPeriodTo;
    @JsonProperty("calculation")
    private CalculationResponseDTO calculation;
    @JsonProperty("amount")
    private List<AmountDTO> amount = null;
    @JsonProperty("address")
    private AddressDTO address;
    @JsonProperty("filestoreid")
    private String filestoreid;
    @JsonProperty("auditDetails")
    private AuditDetails auditDetails;
    @JsonProperty("applicationStatus")
    private String applicationStatus;
    @JsonProperty("vendor")
    private String vendor;
    @JsonProperty("typeOfExpense")
    private String typeOfExpense;
    @JsonProperty("billDate")
    private Long billDate;
    @JsonProperty("billIssuedDate")
    private Long billIssuedDate;
    @JsonProperty("paidDate")
    private Long paidDate;
    @JsonProperty("isBillPaid")
    private Boolean isBillPaid;
    @JsonProperty("vendorName")
    private String vendorName;
    @JsonProperty("totalAmount")
    private String totalAmount;
}
