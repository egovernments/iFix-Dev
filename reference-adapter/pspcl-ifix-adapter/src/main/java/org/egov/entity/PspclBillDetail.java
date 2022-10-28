package org.egov.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.egov.common.contract.AuditDetails;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PspclBillDetail {
    private Long id;

    private String ORDERBYCOLUMN;
    private String TARIFF_TYPE;
    private Date BILL_ISSUE_DATE;
    private Date DATE_READING_CURR;
    private Date DATE_READING_PREV;
    private String DUE_DATE_CASH_ONLINE;
    private String DUE_DATE_CHEQUE_DD;
    private String ACCOUNT_NO;
    private String PAYABLE_AMOUNT_BY_DUE_DATE;//actual bill amount
    private String PAYABLE_AMOUNT_UPTO_15_DAYS;
    private String BILL_NO;

    private BillJsonData billJsonData;

    private AuditDetails auditDetails;

}
