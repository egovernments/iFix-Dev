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
public class BillDTO {
    @JsonProperty("id")
    private String id;
    @JsonProperty("mobileNumber")
    private String mobileNumber;
    @JsonProperty("payerName")
    private String payerName;
    @JsonProperty("payerAddress")
    private String payerAddress;
    @JsonProperty("payerEmail")
    private String payerEmail;
    @JsonProperty("status")
    private String status;
    @JsonProperty("totalAmount")
    private Float totalAmount;
    @JsonProperty("businessService")
    private String businessService;
    @JsonProperty("billNumber")
    private String billNumber;
    @JsonProperty("billDate")
    private Long billDate;
    @JsonProperty("consumerCode")
    private String consumerCode;
    @JsonProperty("additionalDetails")
    private Object additionalDetails;
    @JsonProperty("billDetails")
    private List<BillDetailDTO> billDetails = null;
    @JsonProperty("tenantId")
    private String tenantId;
    @JsonProperty("fileStoreId")
    private Object fileStoreId;
    @JsonProperty("auditDetails")
    private AuditDetails auditDetails;
}
