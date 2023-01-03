package org.egov.model;

import lombok.Getter;
import lombok.Setter;
import org.egov.entity.PspclBillDetail;
import org.egov.entity.PspclPaymentDetail;

import java.math.BigDecimal;

@Getter
@Setter
public class ReconcileVO {
    private PspclBillDetail currentPspclBillDetail;
    private PspclPaymentDetail currentPspclPaymentDetail;
    
    private BigDecimal currentCalculatedBillAmt;

    private String departmentEntityName;
    private String departmentEntityCode;

    private boolean status;

    private boolean billReconcile;
    private boolean paymentReconcile;
}
