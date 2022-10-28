package org.egov.mapper;

import client.stub.GetBillResult;
import client.stub.GetPaymentResult;
import com.google.gson.Gson;
import org.egov.entity.PspclBillDetail;
import org.egov.entity.PspclPaymentDetail;
import org.egov.util.PspclIfixAdapterUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;

import static org.junit.Assert.assertNotNull;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PspclDataEntityMapperTest {

    @Mock
    private PspclIfixAdapterUtil pspclIfixAdapterUtil;

    private PspclDataEntityMapper pspclDataEntityMapper;

    String billReqData = "{\n" +
            "    \"ORDERBYCOLUMN\": \"20211231\",\n" +
            "    \"TARIFF_TYPE\": \"SAP-SBM-SP SMALL POWER FOR DPC-0\",\n" +
            "    \"BILL_ISSUE_DATE\": \"31-DEC-2021\",\n" +
            "    \"DATE_READING_CURR\": \"31-DEC-2021\",\n" +
            "    \"DATE_READING_PREV\": \"09-Dec-2021\",\n" +
            "    \"DUE_DATE_CASH_ONLINE\": \"17-Jan-2022\",\n" +
            "    \"DUE_DATE_CHEQUE_DD\": \"17-Jan-2022\",\n" +
            "    \"ACCOUNT_NO\": \"3005754414\",\n" +
            "    \"OLD_ACCOUNT_NO\": \"0\",\n" +
            "    \"MRU\": \"\",\n" +
            "    \"SUB_DIVISION_CODE\": \"2422\",\n" +
            "    \"EC_000_100_KWH_AMOUNT\": \"0\",\n" +
            "    \"EC_101_300_KWH_AMOUNT\": \"0\",\n" +
            "    \"EC_301_500_KWH_AMOUNT\": \"0\",\n" +
            "    \"EC_501_ABOVE_KWH_AMOUNT\": \"0\",\n" +
            "    \"MOBILE_NO\": \"\",\n" +
            "    \"EMAIL_ID\": \"\",\n" +
            "    \"METER_SECURITY_AMOUNT\": \"1880\",\n" +
            "    \"TOTAL_CONSUMPTION\": \"5552\",\n" +
            "    \"TOTAL_CONSUMPTION_NEW\": \"5552\",\n" +
            "    \"TOTAL_CONSUMPTION_OLD\": \"0\",\n" +
            "    \"PRE_ARR_SURCHARGES\": \"0\",\n" +
            "    \"PRE_ARR_TOTAL\": \"-12672\",\n" +
            "    \"PRE_ADJ_ENERGY_CHARGES\": \"0\",\n" +
            "    \"PRE_ADJ_FCA_PLUS_RENTALS\": \"0\",\n" +
            "    \"PRE_ADJ_FIXED_CHARGES\": \"0\",\n" +
            "    \"PRE_ADJ_TAXES\": \"0\",\n" +
            "    \"PRE_ADJ_TOTAL\": \"0\",\n" +
            "    \"SECURITY_CONS_AMOUNT\": \"6000\",\n" +
            "    \"SUN_ALLW_EC\": \"0\",\n" +
            "    \"SUN_ALLW_FC\": \"0\",\n" +
            "    \"SUN_ALLW_FCA_PLUS_RENTALS\": \"0\",\n" +
            "    \"SUN_ALLW_TAXES\": \"0\",\n" +
            "    \"SUN_ALLW_TOTAL\": \"0\",\n" +
            "    \"SUN_CHRG_EC\": \"0\",\n" +
            "    \"SUN_CHRG_FC\": \"0\",\n" +
            "    \"SUN_CHRG_FCA_PLUS_RENTALS\": \"0\",\n" +
            "    \"SUN_CHRG_TAXES\": \"0\",\n" +
            "    \"SUN_CHRG_TOTAL\": \"0\",\n" +
            "    \"CONNECTED_LOAD\": \"12\",\n" +
            "    \"EC_KWH_TOTAL_AMOUNT\": \"29852\",\n" +
            "    \"FUEL_COST_ADJ_AMOUNT\": \"0\",\n" +
            "    \"LPSC_2_PERCENT\": \"363\",\n" +
            "    \"PAYABLE_AMOUNT_BY_DUE_DATE\": \"23670\",\n" +
            "    \"PAYABLE_AMOUNT_UPTO_15_DAYS\": \"24033\",\n" +
            "    \"RENT_MCB\": \"4\",\n" +
            "    \"RENT_METER\": \"18\",\n" +
            "    \"TAX_NET_ED_WITH_SIGN\": \"4161\",\n" +
            "    \"TAX_NET_IDF_WITH_SIGN\": \"1387\",\n" +
            "    \"CONSUMPTION_1\": \"2457\",\n" +
            "    \"CONSUMPTION_2\": \"0\",\n" +
            "    \"CONSUMPTION_3\": \"3736\",\n" +
            "    \"CONSUMPTION_4\": \"1212\",\n" +
            "    \"CONSUMPTION_5\": \"1222\",\n" +
            "    \"CONSUMPTION_6\": \"1026\",\n" +
            "    \"MTR_READING_CURR\": \"12194.49\",\n" +
            "    \"MTR_READING_PREV\": \"6642\",\n" +
            "    \"CGST\": \"1.98\",\n" +
            "    \"DATE_OF_CONNECTION\": \"20201027\",\n" +
            "    \"PRE_ARR_PENDING_AMOUNT\": \"-12672\",\n" +
            "    \"RENT_TOTAL\": \"25.96\",\n" +
            "    \"SGST\": \"1.98\",\n" +
            "    \"SUN_ALLW_NOTICE_DATE\": \"\",\n" +
            "    \"SUN_CHRG_NOTICE_DATE\": \"\",\n" +
            "    \"TAX_TOTAL\": \"5548\",\n" +
            "    \"MTR_DIGIT\": \"6\",\n" +
            "    \"FC_RATE_KW_MON\": \"80\",\n" +
            "    \"SUBSIDY_UNITS\": \"0\",\n" +
            "    \"BILL_CYCLE\": \"10\",\n" +
            "    \"EC_000_100_KWH_RATE\": \"0\",\n" +
            "    \"EC_101_300_KWH_RATE\": \"0\",\n" +
            "    \"EC_301_500_KWH_RATE\": \"0\",\n" +
            "    \"EC_501_ABOVE_KWH_RATE\": \"0\",\n" +
            "    \"FC_BILL_PERIOD\": \"22\",\n" +
            "    \"FC_LOAD\": \"12\",\n" +
            "    \"FUEL_COST_ADJ_RATE\": \"0\",\n" +
            "    \"FUEL_COST_ADJ_UNITS\": \"0\",\n" +
            "    \"FC_TOTAL\": \"694\",\n" +
            "    \"INTEREST_OF_SECURITY\": \"0\",\n" +
            "    \"UNITS_CONCESSION\": \"0\",\n" +
            "    \"PRE_ARR_INTEREST\": \"0\",\n" +
            "    \"TAX_NET_COWCESS_WITH_SIGN\": \"0\",\n" +
            "    \"SUBSIDY_GOP_AMOUNT\": \"2806\",\n" +
            "    \"TAX_NET_MC_WITH_SIGN\": \"0\",\n" +
            "    \"BILL_STATUS\": \"OK\",\n" +
            "    \"EC_000_100_KWH_UNITS\": \"0\",\n" +
            "    \"EC_101_300_KWH_UNITS\": \"0\",\n" +
            "    \"EC_301_500_KWH_UNITS\": \"0\",\n" +
            "    \"EC_501_ABOVE_KWH_UNITS\": \"0\",\n" +
            "    \"METER_MULTIPLIER\": \"0\",\n" +
            "    \"BILL_GROUP\": \"0\",\n" +
            "    \"CONSUMER_ADDRESS\": \"0-#P.NO 11 ABADI CHANDER NAGAR LUDHIANA-141008-INDIA\",\n" +
            "    \"EXTRA1\": \"5552\",\n" +
            "    \"EXTRA10\": \"0\",\n" +
            "    \"EXTRA2\": \"0\",\n" +
            "    \"EXTRA3\": \"36338.96\",\n" +
            "    \"EXTRA4\": \"0\",\n" +
            "    \"EXTRA5\": \"0\",\n" +
            "    \"EXTRA6\": \"0\",\n" +
            "    \"EXTRA7\": \"0\",\n" +
            "    \"EXTRA8\": \"0\",\n" +
            "    \"EXTRA9\": \"0\",\n" +
            "    \"SUN_ALLW_NOTICE_NO\": \"\",\n" +
            "    \"FEEDER_CODE\": \"\",\n" +
            "    \"MTR_CAPACITY\": \"()\",\n" +
            "    \"MTR_NO\": \"10027774-0\",\n" +
            "    \"SUN_CHRG_NOTICE_NO\": \"\",\n" +
            "    \"BILL_NO\": \"52206363939\",\n" +
            "    \"CIRCLE\": \"WEST CITY LUDHIANA\",\n" +
            "    \"DIVISION\": \"CITY WEST SPECIAL DI\",\n" +
            "    \"MESSAGE\": \"0|||\",\n" +
            "    \"INV_REMARKS\": \"INVOICE CUM BILL OF SUPPLY\",\n" +
            "    \"PAYMENT_HISTORY_1\": \"12640.00/-   Dated : 20211228\",\n" +
            "    \"SUB_DIV_NAME\": \"SUB DIVISION-(T)/UNIT-1\",\n" +
            "    \"EMPID\": \"\",\n" +
            "    \"USERID\": \"\",\n" +
            "    \"CONSUMER_NAME\": \"MR.  MOHD.  SULTAN\",\n" +
            "    \"CONSUMER_GSTIN\": \"\",\n" +
            "    \"MTR_MAKE\": \"HPL ELECTRIC & POWER\",\n" +
            "    \"CURRENT_METER_CODE\": \"O\",\n" +
            "    \"RENT_OTHER\": \"\",\n" +
            "    \"SUN_ALLW_CUR_PRE_ROUNDING\": \"2.02/1.02\",\n" +
            "    \"PAYMENT_HISTORY_2\": \"\",\n" +
            "    \"PAYMENT_HISTORY_3\": \"\",\n" +
            "    \"PAYMENT_HISTORY_4\": \"\",\n" +
            "    \"PAYMENT_HISTORY_5\": \"\",\n" +
            "    \"PAYMENT_HISTORY_6\": \"\",\n" +
            "    \"ADD_SUR_UNITS\": \"5552\",\n" +
            "    \"ADD_SUR_RATE\": \".3\",\n" +
            "    \"ADD_SUR_AMT\": \"0\",\n" +
            "    \"MTR_RENT_TAX_AMT\": \"18\",\n" +
            "    \"MTR_RENT_CGST_AMT\": \"3.24\",\n" +
            "    \"MTR_RENT_SGST_AMT\": \"3.24\",\n" +
            "    \"MTR_RENT_TTL_AMT\": \"24.48\",\n" +
            "    \"MCB_RENT_TAX_AMT\": \"4\",\n" +
            "    \"MCB_RENT_CGST_AMT\": \".72\",\n" +
            "    \"MCB_RENT_SGST_AMT\": \".72\",\n" +
            "    \"MCB_RENT_TTL_AMT\": \"5.44\",\n" +
            "    \"EE_QTY\": \"5552\",\n" +
            "    \"EE_NON_TAX_AMT\": \"30546\",\n" +
            "    \"EE_RENT_TAX_AMT\": \"0\",\n" +
            "    \"EE_RENT_CGST_AMT\": \"0\",\n" +
            "    \"EE_RENT_SGST_AMT\": \"0\",\n" +
            "    \"EE_RENT_TTL_AMT\": \"30546\",\n" +
            "    \"SC_WSD_AMT_WITHHELD\": \"0\",\n" +
            "    \"TNAME\": \"NF_SAP_SBM_GSC\",\n" +
            "    \"SYNCDT\": \"20220108123303\"\n" +
            "  }";
    String paymentReqData = "{\n" +
            "    \"TXNID\": \"BLS2101145390195\",\n" +
            "    \"TXNDATE\": \"1/14/2021 7:46:01 PM\",\n" +
            "    \"ACNO\": \"3005754414\",\n" +
            "    \"AMT\": \"15670\",\n" +
            "    \"BILCYC\": \"10\",\n" +
            "    \"BILGRP\": \"null\",\n" +
            "    \"BILNO\": \"50014046776\",\n" +
            "    \"BILISSDT\": \"1/8/2021 12:00:00 AM\",\n" +
            "    \"DUEDTCASH\": \"1/18/2021 12:00:00 AM\",\n" +
            "    \"DUEDTCHQ\": \"1/18/2021 12:00:00 AM\",\n" +
            "    \"BILYR\": \"2020\",\n" +
            "    \"CATEGORY\": \"SP SMALL POWER FOR DPC\",\n" +
            "    \"VID\": \"12\",\n" +
            "    \"STATUS_P\": \"SUCCESS\",\n" +
            "    \"MBNO\": \"\",\n" +
            "    \"EMAIL\": \"\",\n" +
            "    \"PG_ERR_MSG\": \"\",\n" +
            "    \"PG_ERR_CODE\": \"\",\n" +
            "    \"CNAME\": \"MR.  MOHD.  SULTAN\",\n" +
            "    \"SUBDIVNAME\": \"null\",\n" +
            "    \"DIVNAME\": \"CITY WEST SPECIAL DI\",\n" +
            "    \"CIRNAME\": \"WEST CITY LUDHIANA\",\n" +
            "    \"RECEIPTNO\": \"153897470\",\n" +
            "    \"AGENCY_CODE\": \"BBPS00002\",\n" +
            "    \"PAYMENT_MODE\": \"CS\",\n" +
            "    \"BRANCH\": \"BLS\",\n" +
            "    \"CASHDESK\": \"01\",\n" +
            "    \"CHEQUE_DATE\": \"\",\n" +
            "    \"CHEQUE_NO\": \"\",\n" +
            "    \"RECEIPTDT\": \"1/14/2021 7:45:37 PM\",\n" +
            "    \"PG_STATUS\": \"\",\n" +
            "    \"PG_REF_ID\": \"\",\n" +
            "    \"RECON_ID\": \"34774\",\n" +
            "    \"SYNCED\": \"S\",\n" +
            "    \"SYNCMSG\": \"~\",\n" +
            "    \"SYNCDT\": \"1/14/2021 7:46:02 PM\",\n" +
            "    \"ED\": \"2605\",\n" +
            "    \"OCTRAI\": \"0\",\n" +
            "    \"IF_SAP\": \"Y\",\n" +
            "    \"SOP\": \"13026\",\n" +
            "    \"WATER_SUPPLY\": \"\",\n" +
            "    \"CODE_SDIV\": \"2422\",\n" +
            "    \"TBL_NAME\": \"SAP_NONSBM\",\n" +
            "    \"SURCHARGE\": \"261\",\n" +
            "    \"TOT_AMT\": \"15670\",\n" +
            "    \"AMT_AFTER_DUEDATE\": \"15931\",\n" +
            "    \"PU_WEBHOOK_ID\": \"\",\n" +
            "    \"ELID\": \"\",\n" +
            "    \"CONSUMER_ID\": \"\",\n" +
            "    \"SEND_MAIL_MSG\": \"-1\",\n" +
            "    \"SEND_MOB_SMS\": \"-1\"\n" +
            "  }";

    private GetBillResult getBillResult;
    private GetPaymentResult getPaymentResult;

    @BeforeEach
    private void init() throws IOException {
        MockitoAnnotations.openMocks(this);
        pspclDataEntityMapper = new PspclDataEntityMapper(pspclIfixAdapterUtil);

        Gson gson = new Gson();
        getBillResult = gson.fromJson(billReqData, GetBillResult.class);
        getPaymentResult = gson.fromJson(paymentReqData, GetPaymentResult.class);
    }

    @Test
    void mapPspclBillToEntity() {
        PspclBillDetail pspclBillDetail = pspclDataEntityMapper.mapPspclBillToEntity(getBillResult);

        assertNotNull(pspclBillDetail);
        assertNotNull(pspclBillDetail.getPAYABLE_AMOUNT_BY_DUE_DATE());
    }

    @Test
    void mapPspclPaymentToEntity() {
        PspclPaymentDetail pspclPaymentDetail = pspclDataEntityMapper.mapPspclPaymentToEntity(getPaymentResult);
        assertNotNull(pspclPaymentDetail);
        assertNotNull(pspclPaymentDetail.getAMT());
    }
}