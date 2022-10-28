package client.stub;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class GetPaymentResult {

    @JsonProperty("TXNID")
    private String TXNID;

    @JsonProperty("TXNDATE")
    private String TXNDATE;

    @JsonProperty("ACNO")
    private String ACNO;

    @JsonProperty("AMT")
    private String AMT;

    @JsonProperty("BILCYC")
    private String BILCYC;

    @JsonProperty("BILGRP")
    private String BILGRP;

    @JsonProperty("BILNO")
    private String BILNO;

    @JsonProperty("BILISSDT")
    private String BILISSDT;

    @JsonProperty("DUEDTCASH")
    private String DUEDTCASH;

    @JsonProperty("DUEDTCHQ")
    private String DUEDTCHQ;

    @JsonProperty("BILYR")
    private String BILYR;

    @JsonProperty("CATEGORY")
    private String CATEGORY;

    @JsonProperty("VID")
    private String VID;

    @JsonProperty("STATUS_P")
    private String STATUS_P;

    @JsonProperty("MBNO")
    private String MBNO;

    @JsonProperty("EMAIL")
    private String EMAIL;

    @JsonProperty("PG_ERR_MSG")
    private String PG_ERR_MSG;

    @JsonProperty("PG_ERR_CODE")
    private String PG_ERR_CODE;

    @JsonProperty("CNAME")
    private String CNAME;

    @JsonProperty("SUBDIVNAME")
    private String SUBDIVNAME;

    @JsonProperty("DIVNAME")
    private String DIVNAME;

    @JsonProperty("CIRNAME")
    private String CIRNAME;

    @JsonProperty("RECEIPTNO")
    private String RECEIPTNO;

    @JsonProperty("AGENCY_CODE")
    private String AGENCY_CODE;

    @JsonProperty("PAYMENT_MODE")
    private String PAYMENT_MODE;

    @JsonProperty("BRANCH")
    private String BRANCH;

    @JsonProperty("CASHDESK")
    private String CASHDESK;

    @JsonProperty("CHEQUE_DATE")
    private String CHEQUE_DATE;

    @JsonProperty("CHEQUE_NO")
    private String CHEQUE_NO;

    @JsonProperty("RECEIPTDT")
    private String RECEIPTDT;

    @JsonProperty("PG_STATUS")
    private String PG_STATUS;

    @JsonProperty("PG_REF_ID")
    private String PG_REF_ID;

    @JsonProperty("RECON_ID")
    private String RECON_ID;

    @JsonProperty("SYNCED")
    private String SYNCED;

    @JsonProperty("SYNCMSG")
    private String SYNCMSG;

    @JsonProperty("SYNCDT")
    private String SYNCDT;

    @JsonProperty("ED")
    private String ED;

    @JsonProperty("OCTRAI")
    private String OCTRAI;

    @JsonProperty("IF_SAP")
    private String IF_SAP;

    @JsonProperty("SOP")
    private String SOP;

    @JsonProperty("WATER_SUPPLY")
    private String WATER_SUPPLY;

    @JsonProperty("CODE_SDIV")
    private String CODE_SDIV;

    @JsonProperty("TBL_NAME")
    private String TBL_NAME;

    @JsonProperty("SURCHARGE")
    private String SURCHARGE;

    @JsonProperty("TOT_AMT")
    private String TOT_AMT;

    @JsonProperty("AMT_AFTER_DUEDATE")
    private String AMT_AFTER_DUEDATE;

    @JsonProperty("PU_WEBHOOK_ID")
    private String PU_WEBHOOK_ID;

    @JsonProperty("ELID")
    private String ELID;

    @JsonProperty("CONSUMER_ID")
    private String CONSUMER_ID;

    @JsonProperty("SEND_MAIL_MSG")
    private String SEND_MAIL_MSG;

    @JsonProperty("SEND_MOB_SMS")
    private String SEND_MOB_SMS;

    @Override
    public String toString() {
        return "GetPaymentResult{" +
                "TXNID='" + TXNID + '\'' +
                ", TXNDATE='" + TXNDATE + '\'' +
                ", ACNO='" + ACNO + '\'' +
                ", AMT='" + AMT + '\'' +
                ", BILCYC='" + BILCYC + '\'' +
                ", BILGRP='" + BILGRP + '\'' +
                ", BILNO='" + BILNO + '\'' +
                ", BILISSDT='" + BILISSDT + '\'' +
                ", DUEDTCASH='" + DUEDTCASH + '\'' +
                ", DUEDTCHQ='" + DUEDTCHQ + '\'' +
                ", BILYR='" + BILYR + '\'' +
                ", CATEGORY='" + CATEGORY + '\'' +
                ", VID='" + VID + '\'' +
                ", STATUS_P='" + STATUS_P + '\'' +
                ", MBNO='" + MBNO + '\'' +
                ", EMAIL='" + EMAIL + '\'' +
                ", PG_ERR_MSG='" + PG_ERR_MSG + '\'' +
                ", PG_ERR_CODE='" + PG_ERR_CODE + '\'' +
                ", CNAME='" + CNAME + '\'' +
                ", SUBDIVNAME='" + SUBDIVNAME + '\'' +
                ", DIVNAME='" + DIVNAME + '\'' +
                ", CIRNAME='" + CIRNAME + '\'' +
                ", RECEIPTNO='" + RECEIPTNO + '\'' +
                ", AGENCY_CODE='" + AGENCY_CODE + '\'' +
                ", PAYMENT_MODE='" + PAYMENT_MODE + '\'' +
                ", BRANCH='" + BRANCH + '\'' +
                ", CASHDESK='" + CASHDESK + '\'' +
                ", CHEQUE_DATE='" + CHEQUE_DATE + '\'' +
                ", CHEQUE_NO='" + CHEQUE_NO + '\'' +
                ", RECEIPTDT='" + RECEIPTDT + '\'' +
                ", PG_STATUS='" + PG_STATUS + '\'' +
                ", PG_REF_ID='" + PG_REF_ID + '\'' +
                ", RECON_ID='" + RECON_ID + '\'' +
                ", SYNCED='" + SYNCED + '\'' +
                ", SYNCMSG='" + SYNCMSG + '\'' +
                ", SYNCDT='" + SYNCDT + '\'' +
                ", ED='" + ED + '\'' +
                ", OCTRAI='" + OCTRAI + '\'' +
                ", IF_SAP='" + IF_SAP + '\'' +
                ", SOP='" + SOP + '\'' +
                ", WATER_SUPPLY='" + WATER_SUPPLY + '\'' +
                ", CODE_SDIV='" + CODE_SDIV + '\'' +
                ", TBL_NAME='" + TBL_NAME + '\'' +
                ", SURCHARGE='" + SURCHARGE + '\'' +
                ", TOT_AMT='" + TOT_AMT + '\'' +
                ", AMT_AFTER_DUEDATE='" + AMT_AFTER_DUEDATE + '\'' +
                ", PU_WEBHOOK_ID='" + PU_WEBHOOK_ID + '\'' +
                ", ELID='" + ELID + '\'' +
                ", CONSUMER_ID='" + CONSUMER_ID + '\'' +
                ", SEND_MAIL_MSG='" + SEND_MAIL_MSG + '\'' +
                ", SEND_MOB_SMS='" + SEND_MOB_SMS + '\'' +
                '}';
    }
}
