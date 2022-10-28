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
public class PspclPaymentDetail {

    private Long id;

    private String TXNID;
    private Date TXNDATE;
    private String ACNO;
    private String AMT;//payment
    private String BILCYC;
    private String BILGRP;
    private String BILNO;
    private Date BILISSDT;
    private Date DUEDTCASH;
    private Date DUEDTCHQ;
    private String STATUS_P;

    private PaymentJsonData paymentJsonData;

    private AuditDetails auditDetails;
}
