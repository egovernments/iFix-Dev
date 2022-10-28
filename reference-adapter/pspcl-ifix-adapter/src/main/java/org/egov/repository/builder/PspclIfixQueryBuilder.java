package org.egov.repository.builder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.egov.entity.EventPostingDetail;
import org.egov.entity.PspclBillDetail;
import org.egov.entity.PspclPaymentDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Component;

import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
@Slf4j
public class PspclIfixQueryBuilder {

    @Autowired
    private ObjectMapper objectMapper;

    //common prepared statement param name
    public static final String ID = "id";
    public static final String CREATEDBY = "createdby";
    public static final String CREATEDTIME = "createdtime";
    public static final String LASTMODIFIEDBY = "lastmodifiedby";
    public static final String LASTMODIFIEDTIME = "lastmodifiedtime";

    //pspcl bill prepared statement param name
    public static final String ACCOUNT_NO = "account_no";
    public static final String BILL_NO = "bill_no";
    public static final String BILL_ISSUE_DATE = "bill_issue_date";
    public static final String DATE_READING_CURR = "date_reading_curr";
    public static final String DATE_READING_PREV = "date_reading_prev";
    public static final String DUE_DATE_CASH_ONLINE = "due_date_cash_online";
    public static final String PAYABLE_AMOUNT_BY_DUE_DATE = "payable_amount_by_due_date";
    public static final String DUE_DATE_CHEQUE_DD = "due_date_cheque_dd";
    public static final String ORDERBYCOLUMN = "orderbycolumn";
    public static final String PAYABLE_AMOUNT_UPTO_15_DAYS = "payable_amount_upto_15_days";
    public static final String TARIFF_TYPE = "tariff_type";
    public static final String JSON_DATA = "json_data";

    //pspcl payment prepared statement param name
    public static final String TXNID = "txnid";
    public static final String TXNDATE = "txndate";
    public static final String ACNO = "acno";
    public static final String AMT = "amt";
    public static final String BILCYC = "bilcyc";
    public static final String BILGRP = "bilgrp";
    public static final String BILNO = "bilno";
    public static final String BILISSDT = "bilissdt";
    public static final String DUEDTCASH = "duedtcash";
    public static final String DUEDTCHQ = "duedtchq";
    public static final String STATUS_P = "status_p";

    //event detail posting prepared statement param name
    public static final String TENANTID = "tenantId";
    public static final String IFIXEVENTID = "ifixEventId";
    public static final String REFERENCEID = "referenceId";
    public static final String EVENTTYPE = "eventType";
    public static final String STATUS = "status";
    public static final String ERROR = "error";
    public static final String RECORD = "record";
    public static final String LASTMODIFIEDDATE = "lastModifiedDate";
    public static final String CREATEDDATE = "createdDate";

    //Query clause
    public static final String WHERE_CLAUSE = " WHERE ";
    public static final String ORDER_BY_CLAUSE = " ORDER BY ";
    public static final String EQUAL_TO = " = ";
    public static final String IN_CLAUSE = " IN ";
    public static final String GREATER_THAN_AND_EQUAL_TO = " >= ";
    public static final String GREATER_THAN = " > ";
    public static final String LESS_THAN_AND_EQUAL_TO = " <= ";
    public static final String LESS_THAN = " < ";
    public static final String AND = " AND ";
    public static final String DESC = " DESC ";
    public static final String SINGLE_QUOTE = "'";


    //Query

    public static final String INSERT_QUERY_FOR_PSPCL_BILL_DETAIL = "INSERT INTO pspcl_bill_detail" +
            "(ORDERBYCOLUMN,TARIFF_TYPE,BILL_ISSUE_DATE,DATE_READING_CURR,DATE_READING_PREV,DUE_DATE_CASH_ONLINE,DUE_DATE_CHEQUE_DD," +
            "ACCOUNT_NO,PAYABLE_AMOUNT_BY_DUE_DATE,PAYABLE_AMOUNT_UPTO_15_DAYS,BILL_NO,json_data,createdby,createdtime,lastmodifiedby,lastmodifiedtime) " +
            "VALUES" +
            "(:orderbycolumn,:tariff_type,:bill_issue_date,:date_reading_curr,:date_reading_prev,:due_date_cash_online,:due_date_cheque_dd," +
            " :account_no,:payable_amount_by_due_date,:payable_amount_upto_15_days,:bill_no,:json_data,:createdby,:createdtime,:lastmodifiedby,:lastmodifiedtime);";

    public static final String INSERT_QUERY_FOR_PSPCL_PAYMENT_DETAIL = "INSERT INTO pspcl_payment_detail" +
            "(TXNID,TXNDATE,ACNO,AMT,BILCYC,BILGRP,BILNO,BILISSDT,DUEDTCASH,DUEDTCHQ,STATUS_P,json_data,createdby,createdtime,lastmodifiedby,lastmodifiedtime) " +
            "VALUES" +
            "(:txnid,:txndate,:acno,:amt,:bilcyc,:bilgrp,:bilno,:bilissdt,:duedtcash,:duedtchq,:status_p,:json_data,:createdby,:createdtime,:lastmodifiedby,:lastmodifiedtime);";

    public static final String INSERT_QUERY_FOR_EVENT_POSTING_DETAIL = "INSERT INTO pspcl_event_posting_detail" +
            "(TENANT_ID,IFIX_EVENT_ID,REFERENCE_ID,EVENT_TYPE,STATUS,ERROR,CREATED_DATE,LAST_MODIFIED_DATE,RECORD) " +
            "VALUES" +
            "(:tenantId,:ifixEventId,:referenceId,:eventType,:status,:error,:createdDate,:lastModifiedDate,:record);";


    public static final String SELECT_QUERY_FROM_PSPCL_BILL_DETAIL = "SELECT * FROM pspcl_bill_detail";
    public static final String SELECT_QUERY_FROM_PSPCL_PAYMENT_DETAIL = "SELECT * FROM pspcl_payment_detail";


    public String getPspclBillQueryForIssueDateAndAccountNumber(Date date_reading_prev, String accountNumber) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append(SELECT_QUERY_FROM_PSPCL_BILL_DETAIL)
                .append(WHERE_CLAUSE)
                .append(BILL_ISSUE_DATE).append(EQUAL_TO)
                .append("date(")
                .append(SINGLE_QUOTE).append(date_reading_prev).append(SINGLE_QUOTE).append(")")
                .append(AND)
                .append(ACCOUNT_NO).append(EQUAL_TO)
                .append(SINGLE_QUOTE).append(accountNumber).append(SINGLE_QUOTE);
        return (queryBuilder.toString());
    }


    public String getPspclBillQueryForOrderByColumnAndAccountNumber(String orderByColumn, String accountNumber) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append(SELECT_QUERY_FROM_PSPCL_BILL_DETAIL)
                .append(WHERE_CLAUSE).append(ORDERBYCOLUMN).append(EQUAL_TO)
                .append(SINGLE_QUOTE).append(orderByColumn).append(SINGLE_QUOTE)
                .append(AND)
                .append(ACCOUNT_NO).append(EQUAL_TO)
                .append(SINGLE_QUOTE).append(accountNumber).append(SINGLE_QUOTE);
        return (queryBuilder.toString());
    }

    public String getPspclPaymentQueryForTxnIdAndAccountNumber(String txnId, String accountNumber) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append(SELECT_QUERY_FROM_PSPCL_PAYMENT_DETAIL)
                .append(WHERE_CLAUSE).append(TXNID).append(EQUAL_TO)
                .append(SINGLE_QUOTE).append(txnId).append(SINGLE_QUOTE)
                .append(AND)
                .append(ACNO).append(EQUAL_TO)
                .append(SINGLE_QUOTE).append(accountNumber).append(SINGLE_QUOTE);
        return (queryBuilder.toString());
    }

    public String getPspclPaymentQueryForBetweenAndOrderByTxnDateAndAccountNumber(Date fromDate, Date toDate, String accountNumber) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append(SELECT_QUERY_FROM_PSPCL_PAYMENT_DETAIL)
                .append(WHERE_CLAUSE).append(TXNDATE).append(GREATER_THAN_AND_EQUAL_TO)
                .append("date(")
                .append(SINGLE_QUOTE).append(fromDate).append(SINGLE_QUOTE).append(")")
                .append(AND).append(TXNDATE).append(LESS_THAN)
                .append("date(")
                .append(SINGLE_QUOTE).append(toDate).append(SINGLE_QUOTE).append(")")
                .append(AND)
                .append(ACNO).append(EQUAL_TO)
                .append(SINGLE_QUOTE).append(accountNumber).append(SINGLE_QUOTE)
                .append(ORDER_BY_CLAUSE).append(TXNDATE)
                .append(DESC);
        return (queryBuilder.toString());
    }


    public List<SqlParameterSource> getPspclBillSqlParameterSources(List<PspclBillDetail> pspclBillDetails) {
        List<SqlParameterSource> sqlParameterSources = new ArrayList<>();
        if (pspclBillDetails != null && !pspclBillDetails.isEmpty()) {
            for (PspclBillDetail pspclBillDetail : pspclBillDetails) {
                MapSqlParameterSource param = new MapSqlParameterSource();
                param.addValue(PspclIfixQueryBuilder.ACCOUNT_NO, pspclBillDetail.getACCOUNT_NO());
                param.addValue(PspclIfixQueryBuilder.BILL_NO, pspclBillDetail.getBILL_NO());
                param.addValue(PspclIfixQueryBuilder.BILL_ISSUE_DATE, pspclBillDetail.getBILL_ISSUE_DATE());
                param.addValue(PspclIfixQueryBuilder.DATE_READING_CURR, pspclBillDetail.getDATE_READING_CURR());
                param.addValue(PspclIfixQueryBuilder.DATE_READING_PREV, pspclBillDetail.getDATE_READING_PREV());
                param.addValue(PspclIfixQueryBuilder.DUE_DATE_CASH_ONLINE, pspclBillDetail.getDUE_DATE_CASH_ONLINE());
                param.addValue(PspclIfixQueryBuilder.PAYABLE_AMOUNT_BY_DUE_DATE, pspclBillDetail.getPAYABLE_AMOUNT_BY_DUE_DATE());
                param.addValue(PspclIfixQueryBuilder.DUE_DATE_CHEQUE_DD, pspclBillDetail.getDUE_DATE_CHEQUE_DD());
                param.addValue(PspclIfixQueryBuilder.ORDERBYCOLUMN, pspclBillDetail.getORDERBYCOLUMN());
                param.addValue(PspclIfixQueryBuilder.PAYABLE_AMOUNT_UPTO_15_DAYS, pspclBillDetail.getPAYABLE_AMOUNT_UPTO_15_DAYS());
                param.addValue(PspclIfixQueryBuilder.TARIFF_TYPE, pspclBillDetail.getTARIFF_TYPE());

                param.addValue(PspclIfixQueryBuilder.CREATEDBY, pspclBillDetail.getAuditDetails().getCreatedBy());
                param.addValue(PspclIfixQueryBuilder.CREATEDTIME, pspclBillDetail.getAuditDetails().getCreatedTime());
                param.addValue(PspclIfixQueryBuilder.LASTMODIFIEDBY, pspclBillDetail.getAuditDetails().getLastModifiedBy());
                param.addValue(PspclIfixQueryBuilder.LASTMODIFIEDTIME, pspclBillDetail.getAuditDetails().getLastModifiedTime());

                String jsonData = "";
                try {
                    jsonData = objectMapper.writeValueAsString(pspclBillDetail.getBillJsonData());
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
                param.addValue(PspclIfixQueryBuilder.JSON_DATA, jsonData, Types.OTHER);

                sqlParameterSources.add(param);
            }
        }

        return sqlParameterSources;
    }

    public List<SqlParameterSource> getPspclPaymentSqlParameterSources(List<PspclPaymentDetail> pspclPaymentDetails) {
        List<SqlParameterSource> sqlParameterSources = new ArrayList<>();
        if (pspclPaymentDetails != null && !pspclPaymentDetails.isEmpty()) {
            for (PspclPaymentDetail pspclPaymentDetail : pspclPaymentDetails) {
                MapSqlParameterSource param = new MapSqlParameterSource();
                param.addValue(PspclIfixQueryBuilder.ACNO, pspclPaymentDetail.getACNO());
                param.addValue(PspclIfixQueryBuilder.AMT, pspclPaymentDetail.getAMT());
                param.addValue(PspclIfixQueryBuilder.TXNDATE, pspclPaymentDetail.getTXNDATE());
                param.addValue(PspclIfixQueryBuilder.TXNID, pspclPaymentDetail.getTXNID());
                param.addValue(PspclIfixQueryBuilder.BILCYC, pspclPaymentDetail.getBILCYC());
                param.addValue(PspclIfixQueryBuilder.BILGRP, pspclPaymentDetail.getBILGRP());
                param.addValue(PspclIfixQueryBuilder.BILNO, pspclPaymentDetail.getBILNO());
                param.addValue(PspclIfixQueryBuilder.BILISSDT, pspclPaymentDetail.getBILISSDT());
                param.addValue(PspclIfixQueryBuilder.DUEDTCASH, pspclPaymentDetail.getDUEDTCASH());
                param.addValue(PspclIfixQueryBuilder.DUEDTCHQ, pspclPaymentDetail.getDUEDTCHQ());
                param.addValue(PspclIfixQueryBuilder.STATUS_P, pspclPaymentDetail.getSTATUS_P());

                param.addValue(PspclIfixQueryBuilder.CREATEDBY, pspclPaymentDetail.getAuditDetails().getCreatedBy());
                param.addValue(PspclIfixQueryBuilder.CREATEDTIME, pspclPaymentDetail.getAuditDetails().getCreatedTime());
                param.addValue(PspclIfixQueryBuilder.LASTMODIFIEDBY, pspclPaymentDetail.getAuditDetails().getLastModifiedBy());
                param.addValue(PspclIfixQueryBuilder.LASTMODIFIEDTIME, pspclPaymentDetail.getAuditDetails().getLastModifiedTime());

                String jsonData = "";
                try {
                    jsonData = objectMapper.writeValueAsString(pspclPaymentDetail.getPaymentJsonData());
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
                param.addValue(PspclIfixQueryBuilder.JSON_DATA, jsonData, Types.OTHER);

                sqlParameterSources.add(param);
            }
        }

        return sqlParameterSources;
    }

    public List<SqlParameterSource> getEventPostingSqlParameterSources(List<EventPostingDetail> eventPostingDetailList) {
        List<SqlParameterSource> sqlParameterSources = new ArrayList<>();
        if (eventPostingDetailList != null && !eventPostingDetailList.isEmpty()) {
            for (EventPostingDetail eventPostingDetail : eventPostingDetailList) {
                MapSqlParameterSource param = new MapSqlParameterSource();
                param.addValue(PspclIfixQueryBuilder.TENANTID, eventPostingDetail.getTenantId());
                param.addValue(PspclIfixQueryBuilder.IFIXEVENTID, eventPostingDetail.getIfixEventId());
                param.addValue(PspclIfixQueryBuilder.REFERENCEID, eventPostingDetail.getReferenceId());
                param.addValue(PspclIfixQueryBuilder.EVENTTYPE, eventPostingDetail.getEventType());
                param.addValue(PspclIfixQueryBuilder.STATUS, eventPostingDetail.getStatus());
                param.addValue(PspclIfixQueryBuilder.ERROR, eventPostingDetail.getError());
                param.addValue(PspclIfixQueryBuilder.LASTMODIFIEDDATE, eventPostingDetail.getLastModifiedDate());
                param.addValue(PspclIfixQueryBuilder.CREATEDDATE, eventPostingDetail.getCreatedDate());

                param.addValue(PspclIfixQueryBuilder.RECORD, eventPostingDetail.getRecord(), Types.OTHER);
                sqlParameterSources.add(param);
            }
        }

        return sqlParameterSources;
    }
}
