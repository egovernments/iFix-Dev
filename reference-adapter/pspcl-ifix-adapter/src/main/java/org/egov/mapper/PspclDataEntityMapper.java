package org.egov.mapper;

import client.stub.GetBillResult;
import client.stub.GetPaymentResult;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.AuditDetails;
import org.egov.entity.BillJsonData;
import org.egov.entity.PaymentJsonData;
import org.egov.entity.PspclBillDetail;
import org.egov.entity.PspclPaymentDetail;
import org.egov.util.PspclIfixAdapterUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

import static org.egov.util.PspclIfixAdapterConstant.BILL_ISSUE_DATE_FORMAT;
import static org.egov.util.PspclIfixAdapterConstant.TXN_DATE_FORMAT;

@Component
@Slf4j
@AllArgsConstructor
@NoArgsConstructor
public class PspclDataEntityMapper {

    @Autowired
    private PspclIfixAdapterUtil pspclIfixAdapterUtil;


    public PspclBillDetail mapPspclBillToEntity(GetBillResult currentMonthBill) {
        PspclBillDetail pspclBillDetail = new PspclBillDetail();
        BillJsonData billJsonData = new BillJsonData();

        BeanUtils.copyProperties(currentMonthBill, billJsonData);

        pspclBillDetail.setBillJsonData(billJsonData);

        pspclBillDetail.setBILL_NO(currentMonthBill.getBILL_NO());

        pspclBillDetail.setBILL_ISSUE_DATE(pspclIfixAdapterUtil.format(BILL_ISSUE_DATE_FORMAT, currentMonthBill.getBILL_ISSUE_DATE()));
        pspclBillDetail.setACCOUNT_NO(currentMonthBill.getACCOUNT_NO());

        pspclBillDetail.setDATE_READING_CURR(pspclIfixAdapterUtil.format(BILL_ISSUE_DATE_FORMAT, currentMonthBill.getDATE_READING_CURR()));

        pspclBillDetail.setDATE_READING_PREV(pspclIfixAdapterUtil.format(BILL_ISSUE_DATE_FORMAT, currentMonthBill.getDATE_READING_PREV()));

        pspclBillDetail.setDUE_DATE_CASH_ONLINE(currentMonthBill.getDUE_DATE_CASH_ONLINE());
        pspclBillDetail.setDUE_DATE_CHEQUE_DD(currentMonthBill.getDUE_DATE_CHEQUE_DD());
        pspclBillDetail.setTARIFF_TYPE(currentMonthBill.getTARIFF_TYPE());
        pspclBillDetail.setPAYABLE_AMOUNT_UPTO_15_DAYS(currentMonthBill.getPAYABLE_AMOUNT_UPTO_15_DAYS());
        pspclBillDetail.setPAYABLE_AMOUNT_BY_DUE_DATE(currentMonthBill.getPAYABLE_AMOUNT_BY_DUE_DATE());
        pspclBillDetail.setORDERBYCOLUMN(currentMonthBill.getORDERBYCOLUMN());

        AuditDetails auditDetails = new AuditDetails();
        auditDetails.setCreatedBy("pspcl-ifix-adapter");
        auditDetails.setLastModifiedBy("pspcl-ifix-adapter");
        auditDetails.setCreatedTime(new Date().getTime());
        auditDetails.setLastModifiedTime(new Date().getTime());

        pspclBillDetail.setAuditDetails(auditDetails);

        return pspclBillDetail;

    }

    public PspclPaymentDetail mapPspclPaymentToEntity(GetPaymentResult currentMonthPayment) {
        PspclPaymentDetail pspclPaymentDetail = new PspclPaymentDetail();
        PaymentJsonData paymentJsonData = new PaymentJsonData();

        BeanUtils.copyProperties(currentMonthPayment, paymentJsonData);

        pspclPaymentDetail.setPaymentJsonData(paymentJsonData);

        pspclPaymentDetail.setACNO(currentMonthPayment.getACNO());
        pspclPaymentDetail.setAMT(currentMonthPayment.getAMT());
        pspclPaymentDetail.setBILCYC(currentMonthPayment.getBILCYC());
        pspclPaymentDetail.setBILGRP(currentMonthPayment.getBILGRP());
        pspclPaymentDetail.setBILISSDT(pspclIfixAdapterUtil.format(TXN_DATE_FORMAT, currentMonthPayment.getBILISSDT()));
        pspclPaymentDetail.setTXNID(currentMonthPayment.getTXNID());
        pspclPaymentDetail.setTXNDATE(pspclIfixAdapterUtil.format(TXN_DATE_FORMAT, currentMonthPayment.getTXNDATE()));
        pspclPaymentDetail.setSTATUS_P(currentMonthPayment.getSTATUS_P());
        pspclPaymentDetail.setDUEDTCHQ(pspclIfixAdapterUtil.format(TXN_DATE_FORMAT, currentMonthPayment.getDUEDTCHQ()));
        pspclPaymentDetail.setDUEDTCASH(pspclIfixAdapterUtil.format(TXN_DATE_FORMAT, currentMonthPayment.getDUEDTCASH()));
        pspclPaymentDetail.setBILNO(currentMonthPayment.getBILNO());

        AuditDetails auditDetails = new AuditDetails();
        auditDetails.setCreatedBy("pspcl-ifix-adapter");
        auditDetails.setLastModifiedBy("pspcl-ifix-adapter");
        auditDetails.setCreatedTime(new Date().getTime());
        auditDetails.setLastModifiedTime(new Date().getTime());

        pspclPaymentDetail.setAuditDetails(auditDetails);

        return pspclPaymentDetail;
    }
}
