package org.egov.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentJsonData {

    @JsonProperty("BILYR")
    private String BILYR;

    @JsonProperty("CATEGORY")
    private String CATEGORY;

    @JsonProperty("VID")
    private String VID;
    
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
}
