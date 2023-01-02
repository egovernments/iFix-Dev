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
public class GetBillResult {

    @JsonProperty("ORDERBYCOLUMN")
    private String ORDERBYCOLUMN;

    @JsonProperty("TARIFF_TYPE")
    private String TARIFF_TYPE;

    @JsonProperty("BILL_ISSUE_DATE")
    private String BILL_ISSUE_DATE;

    @JsonProperty("DATE_READING_CURR")
    private String DATE_READING_CURR;

    @JsonProperty("DATE_READING_PREV")
    private String DATE_READING_PREV;

    @JsonProperty("DUE_DATE_CASH_ONLINE")
    private String DUE_DATE_CASH_ONLINE;

    @JsonProperty("DUE_DATE_CHEQUE_DD")
    private String DUE_DATE_CHEQUE_DD;

    @JsonProperty("ACCOUNT_NO")
    private String ACCOUNT_NO;

    @JsonProperty("OLD_ACCOUNT_NO")
    private String OLD_ACCOUNT_NO;

    @JsonProperty("MRU")
    private String MRU;

    @JsonProperty("SUB_DIVISION_CODE")
    private String SUB_DIVISION_CODE;

    @JsonProperty("EC_000_100_KWH_AMOUNT")
    private String EC_000_100_KWH_AMOUNT;

    @JsonProperty("EC_101_300_KWH_AMOUNT")
    private String EC_101_300_KWH_AMOUNT;

    @JsonProperty("EC_301_500_KWH_AMOUNT")
    private String EC_301_500_KWH_AMOUNT;

    @JsonProperty("EC_501_ABOVE_KWH_AMOUNT")
    private String EC_501_ABOVE_KWH_AMOUNT;

    @JsonProperty("MOBILE_NO")
    private String MOBILE_NO;

    @JsonProperty("EMAIL_ID")
    private String EMAIL_ID;

    @JsonProperty("METER_SECURITY_AMOUNT")
    private String METER_SECURITY_AMOUNT;

    @JsonProperty("TOTAL_CONSUMPTION")
    private String TOTAL_CONSUMPTION;

    @JsonProperty("TOTAL_CONSUMPTION_NEW")
    private String TOTAL_CONSUMPTION_NEW;

    @JsonProperty("TOTAL_CONSUMPTION_OLD")
    private String TOTAL_CONSUMPTION_OLD;

    @JsonProperty("PRE_ARR_SURCHARGES")
    private String PRE_ARR_SURCHARGES;

    @JsonProperty("PRE_ARR_TOTAL")
    private String PRE_ARR_TOTAL;

    @JsonProperty("PRE_ADJ_ENERGY_CHARGES")
    private String PRE_ADJ_ENERGY_CHARGES;

    @JsonProperty("PRE_ADJ_FCA_PLUS_RENTALS")
    private String PRE_ADJ_FCA_PLUS_RENTALS;

    @JsonProperty("PRE_ADJ_FIXED_CHARGES")
    private String PRE_ADJ_FIXED_CHARGES;

    @JsonProperty("PRE_ADJ_TAXES")
    private String PRE_ADJ_TAXES;

    @JsonProperty("PRE_ADJ_TOTAL")
    private String PRE_ADJ_TOTAL;


    @JsonProperty("SECURITY_CONS_AMOUNT")
    private String SECURITY_CONS_AMOUNT;

    @JsonProperty("SUN_ALLW_EC")
    private String SUN_ALLW_EC;

    @JsonProperty("SUN_ALLW_FC")
    private String SUN_ALLW_FC;

    @JsonProperty("SUN_ALLW_FCA_PLUS_RENTALS")
    private String SUN_ALLW_FCA_PLUS_RENTALS;

    @JsonProperty("SUN_ALLW_TAXES")
    private String SUN_ALLW_TAXES;

    @JsonProperty("SUN_ALLW_TOTAL")
    private String SUN_ALLW_TOTAL;

    @JsonProperty("SUN_CHRG_EC")
    private String SUN_CHRG_EC;

    @JsonProperty("SUN_CHRG_FC")
    private String SUN_CHRG_FC;

    @JsonProperty("SUN_CHRG_FCA_PLUS_RENTALS")
    private String SUN_CHRG_FCA_PLUS_RENTALS;

    @JsonProperty("SUN_CHRG_TAXES")
    private String SUN_CHRG_TAXES;

    @JsonProperty("SUN_CHRG_TOTAL")
    private String SUN_CHRG_TOTAL;

    @JsonProperty("CONNECTED_LOAD")
    private String CONNECTED_LOAD;

    @JsonProperty("EC_KWH_TOTAL_AMOUNT")
    private String EC_KWH_TOTAL_AMOUNT;

    @JsonProperty("FUEL_COST_ADJ_AMOUNT")
    private String FUEL_COST_ADJ_AMOUNT;

    @JsonProperty("LPSC_2_PERCENT")
    private String LPSC_2_PERCENT;

    @JsonProperty("PAYABLE_AMOUNT_BY_DUE_DATE")
    private String PAYABLE_AMOUNT_BY_DUE_DATE;


    @JsonProperty("PAYABLE_AMOUNT_UPTO_15_DAYS")
    private String PAYABLE_AMOUNT_UPTO_15_DAYS;

    @JsonProperty("RENT_MCB")
    private String RENT_MCB;

    @JsonProperty("RENT_METER")
    private String RENT_METER;

    @JsonProperty("TAX_NET_ED_WITH_SIGN")
    private String TAX_NET_ED_WITH_SIGN;

    @JsonProperty("TAX_NET_IDF_WITH_SIGN")
    private String TAX_NET_IDF_WITH_SIGN;

    @JsonProperty("CONSUMPTION_1")
    private String CONSUMPTION_1;

    @JsonProperty("CONSUMPTION_2")
    private String CONSUMPTION_2;

    @JsonProperty("CONSUMPTION_3")
    private String CONSUMPTION_3;

    @JsonProperty("CONSUMPTION_4")
    private String CONSUMPTION_4;

    @JsonProperty("CONSUMPTION_5")
    private String CONSUMPTION_5;

    @JsonProperty("CONSUMPTION_6")
    private String CONSUMPTION_6;

    @JsonProperty("MTR_READING_CURR")
    private String MTR_READING_CURR;

    @JsonProperty("MTR_READING_PREV")
    private String MTR_READING_PREV;

    @JsonProperty("CGST")
    private String CGST;

    @JsonProperty("DATE_OF_CONNECTION")
    private String DATE_OF_CONNECTION;

    @JsonProperty("PRE_ARR_PENDING_AMOUNT")
    private String PRE_ARR_PENDING_AMOUNT;

    @JsonProperty("RENT_TOTAL")
    private String RENT_TOTAL;

    @JsonProperty("SGST")
    private String SGST;

    @JsonProperty("SUN_ALLW_NOTICE_DATE")
    private String SUN_ALLW_NOTICE_DATE;

    @JsonProperty("SUN_CHRG_NOTICE_DATE")
    private String SUN_CHRG_NOTICE_DATE;

    @JsonProperty("TAX_TOTAL")
    private String TAX_TOTAL;

    @JsonProperty("MTR_DIGIT")
    private String MTR_DIGIT;

    @JsonProperty("FC_RATE_KW_MON")
    private String FC_RATE_KW_MON;

    @JsonProperty("SUBSIDY_UNITS")
    private String SUBSIDY_UNITS;

    @JsonProperty("BILL_CYCLE")
    private String BILL_CYCLE;

    @JsonProperty("EC_000_100_KWH_RATE")
    private String EC_000_100_KWH_RATE;

    @JsonProperty("EC_101_300_KWH_RATE")
    private String EC_101_300_KWH_RATE;

    @JsonProperty("EC_301_500_KWH_RATE")
    private String EC_301_500_KWH_RATE;

    @JsonProperty("EC_501_ABOVE_KWH_RATE")
    private String EC_501_ABOVE_KWH_RATE;

    @JsonProperty("FC_BILL_PERIOD")
    private String FC_BILL_PERIOD;

    @JsonProperty("FC_LOAD")
    private String FC_LOAD;

    @JsonProperty("FUEL_COST_ADJ_RATE")
    private String FUEL_COST_ADJ_RATE;

    @JsonProperty("FUEL_COST_ADJ_UNITS")
    private String FUEL_COST_ADJ_UNITS;

    @JsonProperty("FC_TOTAL")
    private String FC_TOTAL;

    @JsonProperty("INTEREST_OF_SECURITY")
    private String INTEREST_OF_SECURITY;

    @JsonProperty("UNITS_CONCESSION")
    private String UNITS_CONCESSION;

    @JsonProperty("PRE_ARR_INTEREST")
    private String PRE_ARR_INTEREST;

    @JsonProperty("TAX_NET_COWCESS_WITH_SIGN")
    private String TAX_NET_COWCESS_WITH_SIGN;

    @JsonProperty("SUBSIDY_GOP_AMOUNT")
    private String SUBSIDY_GOP_AMOUNT;

    @JsonProperty("TAX_NET_MC_WITH_SIGN")
    private String TAX_NET_MC_WITH_SIGN;

    @JsonProperty("BILL_STATUS")
    private String BILL_STATUS;

    @JsonProperty("EC_000_100_KWH_UNITS")
    private String EC_000_100_KWH_UNITS;

    @JsonProperty("EC_101_300_KWH_UNITS")
    private String EC_101_300_KWH_UNITS;

    @JsonProperty("EC_301_500_KWH_UNITS")
    private String EC_301_500_KWH_UNITS;

    @JsonProperty("EC_501_ABOVE_KWH_UNITS")
    private String EC_501_ABOVE_KWH_UNITS;

    @JsonProperty("METER_MULTIPLIER")
    private String METER_MULTIPLIER;

    @JsonProperty("BILL_GROUP")
    private String BILL_GROUP;

    @JsonProperty("CONSUMER_ADDRESS")
    private String CONSUMER_ADDRESS;

    @JsonProperty("EXTRA1")
    private String EXTRA1;

    @JsonProperty("EXTRA10")
    private String EXTRA10;


    @JsonProperty("EXTRA2")
    private String EXTRA2;

    @JsonProperty("EXTRA3")
    private String EXTRA3;

    @JsonProperty("EXTRA4")
    private String EXTRA4;

    @JsonProperty("EXTRA5")
    private String EXTRA5;

    @JsonProperty("EXTRA6")
    private String EXTRA6;

    @JsonProperty("EXTRA7")
    private String EXTRA7;

    @JsonProperty("EXTRA8")
    private String EXTRA8;

    @JsonProperty("EXTRA9")
    private String EXTRA9;

    @JsonProperty("SUN_ALLW_NOTICE_NO")
    private String SUN_ALLW_NOTICE_NO;

    @JsonProperty("FEEDER_CODE")
    private String FEEDER_CODE;

    @JsonProperty("MTR_CAPACITY")
    private String MTR_CAPACITY;

    @JsonProperty("MTR_NO")
    private String MTR_NO;

    @JsonProperty("SUN_CHRG_NOTICE_NO")
    private String SUN_CHRG_NOTICE_NO;

    @JsonProperty("BILL_NO")
    private String BILL_NO;

    @JsonProperty("CIRCLE")
    private String CIRCLE;

    @JsonProperty("DIVISION")
    private String DIVISION;

    @JsonProperty("MESSAGE")
    private String MESSAGE;

    @JsonProperty("INV_REMARKS")
    private String INV_REMARKS;

    @JsonProperty("PAYMENT_HISTORY_1")
    private String PAYMENT_HISTORY_1;

    @JsonProperty("SUB_DIV_NAME")
    private String SUB_DIV_NAME;

    @JsonProperty("EMPID")
    private String EMPID;

    @JsonProperty("USERID")
    private String USERID;

    @JsonProperty("CONSUMER_NAME")
    private String CONSUMER_NAME;

    @JsonProperty("CONSUMER_GSTIN")
    private String CONSUMER_GSTIN;

    @JsonProperty("MTR_MAKE")
    private String MTR_MAKE;

    @JsonProperty("CURRENT_METER_CODE")
    private String CURRENT_METER_CODE;

    @JsonProperty("RENT_OTHER")
    private String RENT_OTHER;

    @JsonProperty("SUN_ALLW_CUR_PRE_ROUNDING")
    private String SUN_ALLW_CUR_PRE_ROUNDING;

    @JsonProperty("PAYMENT_HISTORY_2")
    private String PAYMENT_HISTORY_2;

    @JsonProperty("PAYMENT_HISTORY_3")
    private String PAYMENT_HISTORY_3;

    @JsonProperty("PAYMENT_HISTORY_4")
    private String PAYMENT_HISTORY_4;

    @JsonProperty("PAYMENT_HISTORY_5")
    private String PAYMENT_HISTORY_5;

    @JsonProperty("PAYMENT_HISTORY_6")
    private String PAYMENT_HISTORY_6;

    @JsonProperty("ADD_SUR_UNITS")
    private String ADD_SUR_UNITS;

    @JsonProperty("ADD_SUR_RATE")
    private String ADD_SUR_RATE;

    @JsonProperty("ADD_SUR_AMT")
    private String ADD_SUR_AMT;

    @JsonProperty("MTR_RENT_TAX_AMT")
    private String MTR_RENT_TAX_AMT;

    @JsonProperty("MTR_RENT_CGST_AMT")
    private String MTR_RENT_CGST_AMT;

    @JsonProperty("MTR_RENT_SGST_AMT")
    private String MTR_RENT_SGST_AMT;

    @JsonProperty("MTR_RENT_TTL_AMT")
    private String MTR_RENT_TTL_AMT;

    @JsonProperty("MCB_RENT_TAX_AMT")
    private String MCB_RENT_TAX_AMT;

    @JsonProperty("MCB_RENT_CGST_AMT")
    private String MCB_RENT_CGST_AMT;

    @JsonProperty("MCB_RENT_SGST_AMT")
    private String MCB_RENT_SGST_AMT;

    @JsonProperty("MCB_RENT_TTL_AMT")
    private String MCB_RENT_TTL_AMT;


    @JsonProperty("EE_QTY")
    private String EE_QTY;

    @JsonProperty("EE_NON_TAX_AMT")
    private String EE_NON_TAX_AMT;

    @JsonProperty("EE_RENT_TAX_AMT")
    private String EE_RENT_TAX_AMT;

    @JsonProperty("EE_RENT_CGST_AMT")
    private String EE_RENT_CGST_AMT;

    @JsonProperty("EE_RENT_SGST_AMT")
    private String EE_RENT_SGST_AMT;

    @JsonProperty("EE_RENT_TTL_AMT")
    private String EE_RENT_TTL_AMT;

    @JsonProperty("SC_WSD_AMT_WITHHELD")
    private String SC_WSD_AMT_WITHHELD;

    @JsonProperty("TNAME")
    private String TNAME;

    @JsonProperty("SYNCDT")
    private String SYNCDT;


    @Override
    public String toString() {
        return "GetBillResult{" +
                "ORDERBYCOLUMN='" + ORDERBYCOLUMN + '\'' +
                ", TARIFF_TYPE='" + TARIFF_TYPE + '\'' +
                ", BILL_ISSUE_DATE='" + BILL_ISSUE_DATE + '\'' +
                ", DATE_READING_CURR='" + DATE_READING_CURR + '\'' +
                ", DATE_READING_PREV='" + DATE_READING_PREV + '\'' +
                ", DUE_DATE_CASH_ONLINE='" + DUE_DATE_CASH_ONLINE + '\'' +
                ", DUE_DATE_CHEQUE_DD='" + DUE_DATE_CHEQUE_DD + '\'' +
                ", ACCOUNT_NO='" + ACCOUNT_NO + '\'' +
                ", OLD_ACCOUNT_NO='" + OLD_ACCOUNT_NO + '\'' +
                ", MRU='" + MRU + '\'' +
                ", SUB_DIVISION_CODE='" + SUB_DIVISION_CODE + '\'' +
                ", EC_000_100_KWH_AMOUNT='" + EC_000_100_KWH_AMOUNT + '\'' +
                ", EC_101_300_KWH_AMOUNT='" + EC_101_300_KWH_AMOUNT + '\'' +
                ", EC_301_500_KWH_AMOUNT='" + EC_301_500_KWH_AMOUNT + '\'' +
                ", EC_501_ABOVE_KWH_AMOUNT='" + EC_501_ABOVE_KWH_AMOUNT + '\'' +
                ", MOBILE_NO='" + MOBILE_NO + '\'' +
                ", EMAIL_ID='" + EMAIL_ID + '\'' +
                ", METER_SECURITY_AMOUNT='" + METER_SECURITY_AMOUNT + '\'' +
                ", TOTAL_CONSUMPTION='" + TOTAL_CONSUMPTION + '\'' +
                ", TOTAL_CONSUMPTION_NEW='" + TOTAL_CONSUMPTION_NEW + '\'' +
                ", TOTAL_CONSUMPTION_OLD='" + TOTAL_CONSUMPTION_OLD + '\'' +
                ", PRE_ARR_SURCHARGES='" + PRE_ARR_SURCHARGES + '\'' +
                ", PRE_ARR_TOTAL='" + PRE_ARR_TOTAL + '\'' +
                ", PRE_ADJ_ENERGY_CHARGES='" + PRE_ADJ_ENERGY_CHARGES + '\'' +
                ", PRE_ADJ_FCA_PLUS_RENTALS='" + PRE_ADJ_FCA_PLUS_RENTALS + '\'' +
                ", PRE_ADJ_FIXED_CHARGES='" + PRE_ADJ_FIXED_CHARGES + '\'' +
                ", PRE_ADJ_TAXES='" + PRE_ADJ_TAXES + '\'' +
                ", PRE_ADJ_TOTAL='" + PRE_ADJ_TOTAL + '\'' +
                ", SECURITY_CONS_AMOUNT='" + SECURITY_CONS_AMOUNT + '\'' +
                ", SUN_ALLW_EC='" + SUN_ALLW_EC + '\'' +
                ", SUN_ALLW_FC='" + SUN_ALLW_FC + '\'' +
                ", SUN_ALLW_FCA_PLUS_RENTALS='" + SUN_ALLW_FCA_PLUS_RENTALS + '\'' +
                ", SUN_ALLW_TAXES='" + SUN_ALLW_TAXES + '\'' +
                ", SUN_ALLW_TOTAL='" + SUN_ALLW_TOTAL + '\'' +
                ", SUN_CHRG_EC='" + SUN_CHRG_EC + '\'' +
                ", SUN_CHRG_FC='" + SUN_CHRG_FC + '\'' +
                ", SUN_CHRG_FCA_PLUS_RENTALS='" + SUN_CHRG_FCA_PLUS_RENTALS + '\'' +
                ", SUN_CHRG_TAXES='" + SUN_CHRG_TAXES + '\'' +
                ", SUN_CHRG_TOTAL='" + SUN_CHRG_TOTAL + '\'' +
                ", CONNECTED_LOAD='" + CONNECTED_LOAD + '\'' +
                ", EC_KWH_TOTAL_AMOUNT='" + EC_KWH_TOTAL_AMOUNT + '\'' +
                ", FUEL_COST_ADJ_AMOUNT='" + FUEL_COST_ADJ_AMOUNT + '\'' +
                ", LPSC_2_PERCENT='" + LPSC_2_PERCENT + '\'' +
                ", PAYABLE_AMOUNT_BY_DUE_DATE='" + PAYABLE_AMOUNT_BY_DUE_DATE + '\'' +
                ", PAYABLE_AMOUNT_UPTO_15_DAYS='" + PAYABLE_AMOUNT_UPTO_15_DAYS + '\'' +
                ", RENT_MCB='" + RENT_MCB + '\'' +
                ", RENT_METER='" + RENT_METER + '\'' +
                ", TAX_NET_ED_WITH_SIGN='" + TAX_NET_ED_WITH_SIGN + '\'' +
                ", TAX_NET_IDF_WITH_SIGN='" + TAX_NET_IDF_WITH_SIGN + '\'' +
                ", CONSUMPTION_1='" + CONSUMPTION_1 + '\'' +
                ", CONSUMPTION_2='" + CONSUMPTION_2 + '\'' +
                ", CONSUMPTION_3='" + CONSUMPTION_3 + '\'' +
                ", CONSUMPTION_4='" + CONSUMPTION_4 + '\'' +
                ", CONSUMPTION_5='" + CONSUMPTION_5 + '\'' +
                ", CONSUMPTION_6='" + CONSUMPTION_6 + '\'' +
                ", MTR_READING_CURR='" + MTR_READING_CURR + '\'' +
                ", MTR_READING_PREV='" + MTR_READING_PREV + '\'' +
                ", CGST='" + CGST + '\'' +
                ", DATE_OF_CONNECTION='" + DATE_OF_CONNECTION + '\'' +
                ", PRE_ARR_PENDING_AMOUNT='" + PRE_ARR_PENDING_AMOUNT + '\'' +
                ", RENT_TOTAL='" + RENT_TOTAL + '\'' +
                ", SGST='" + SGST + '\'' +
                ", SUN_ALLW_NOTICE_DATE='" + SUN_ALLW_NOTICE_DATE + '\'' +
                ", SUN_CHRG_NOTICE_DATE='" + SUN_CHRG_NOTICE_DATE + '\'' +
                ", TAX_TOTAL='" + TAX_TOTAL + '\'' +
                ", MTR_DIGIT='" + MTR_DIGIT + '\'' +
                ", FC_RATE_KW_MON='" + FC_RATE_KW_MON + '\'' +
                ", SUBSIDY_UNITS='" + SUBSIDY_UNITS + '\'' +
                ", BILL_CYCLE='" + BILL_CYCLE + '\'' +
                ", EC_000_100_KWH_RATE='" + EC_000_100_KWH_RATE + '\'' +
                ", EC_101_300_KWH_RATE='" + EC_101_300_KWH_RATE + '\'' +
                ", EC_301_500_KWH_RATE='" + EC_301_500_KWH_RATE + '\'' +
                ", EC_501_ABOVE_KWH_RATE='" + EC_501_ABOVE_KWH_RATE + '\'' +
                ", FC_BILL_PERIOD='" + FC_BILL_PERIOD + '\'' +
                ", FC_LOAD='" + FC_LOAD + '\'' +
                ", FUEL_COST_ADJ_RATE='" + FUEL_COST_ADJ_RATE + '\'' +
                ", FUEL_COST_ADJ_UNITS='" + FUEL_COST_ADJ_UNITS + '\'' +
                ", FC_TOTAL='" + FC_TOTAL + '\'' +
                ", INTEREST_OF_SECURITY='" + INTEREST_OF_SECURITY + '\'' +
                ", UNITS_CONCESSION='" + UNITS_CONCESSION + '\'' +
                ", PRE_ARR_INTEREST='" + PRE_ARR_INTEREST + '\'' +
                ", TAX_NET_COWCESS_WITH_SIGN='" + TAX_NET_COWCESS_WITH_SIGN + '\'' +
                ", SUBSIDY_GOP_AMOUNT='" + SUBSIDY_GOP_AMOUNT + '\'' +
                ", TAX_NET_MC_WITH_SIGN='" + TAX_NET_MC_WITH_SIGN + '\'' +
                ", BILL_STATUS='" + BILL_STATUS + '\'' +
                ", EC_000_100_KWH_UNITS='" + EC_000_100_KWH_UNITS + '\'' +
                ", EC_101_300_KWH_UNITS='" + EC_101_300_KWH_UNITS + '\'' +
                ", EC_301_500_KWH_UNITS='" + EC_301_500_KWH_UNITS + '\'' +
                ", EC_501_ABOVE_KWH_UNITS='" + EC_501_ABOVE_KWH_UNITS + '\'' +
                ", METER_MULTIPLIER='" + METER_MULTIPLIER + '\'' +
                ", BILL_GROUP='" + BILL_GROUP + '\'' +
                ", CONSUMER_ADDRESS='" + CONSUMER_ADDRESS + '\'' +
                ", EXTRA1='" + EXTRA1 + '\'' +
                ", EXTRA10='" + EXTRA10 + '\'' +
                ", EXTRA2='" + EXTRA2 + '\'' +
                ", EXTRA3='" + EXTRA3 + '\'' +
                ", EXTRA4='" + EXTRA4 + '\'' +
                ", EXTRA5='" + EXTRA5 + '\'' +
                ", EXTRA6='" + EXTRA6 + '\'' +
                ", EXTRA7='" + EXTRA7 + '\'' +
                ", EXTRA8='" + EXTRA8 + '\'' +
                ", EXTRA9='" + EXTRA9 + '\'' +
                ", SUN_ALLW_NOTICE_NO='" + SUN_ALLW_NOTICE_NO + '\'' +
                ", FEEDER_CODE='" + FEEDER_CODE + '\'' +
                ", MTR_CAPACITY='" + MTR_CAPACITY + '\'' +
                ", MTR_NO='" + MTR_NO + '\'' +
                ", SUN_CHRG_NOTICE_NO='" + SUN_CHRG_NOTICE_NO + '\'' +
                ", BILL_NO='" + BILL_NO + '\'' +
                ", CIRCLE='" + CIRCLE + '\'' +
                ", DIVISION='" + DIVISION + '\'' +
                ", MESSAGE='" + MESSAGE + '\'' +
                ", INV_REMARKS='" + INV_REMARKS + '\'' +
                ", PAYMENT_HISTORY_1='" + PAYMENT_HISTORY_1 + '\'' +
                ", SUB_DIV_NAME='" + SUB_DIV_NAME + '\'' +
                ", EMPID='" + EMPID + '\'' +
                ", USERID='" + USERID + '\'' +
                ", CONSUMER_NAME='" + CONSUMER_NAME + '\'' +
                ", CONSUMER_GSTIN='" + CONSUMER_GSTIN + '\'' +
                ", MTR_MAKE='" + MTR_MAKE + '\'' +
                ", CURRENT_METER_CODE='" + CURRENT_METER_CODE + '\'' +
                ", RENT_OTHER='" + RENT_OTHER + '\'' +
                ", SUN_ALLW_CUR_PRE_ROUNDING='" + SUN_ALLW_CUR_PRE_ROUNDING + '\'' +
                ", PAYMENT_HISTORY_2='" + PAYMENT_HISTORY_2 + '\'' +
                ", PAYMENT_HISTORY_3='" + PAYMENT_HISTORY_3 + '\'' +
                ", PAYMENT_HISTORY_4='" + PAYMENT_HISTORY_4 + '\'' +
                ", PAYMENT_HISTORY_5='" + PAYMENT_HISTORY_5 + '\'' +
                ", PAYMENT_HISTORY_6='" + PAYMENT_HISTORY_6 + '\'' +
                ", ADD_SUR_UNITS='" + ADD_SUR_UNITS + '\'' +
                ", ADD_SUR_RATE='" + ADD_SUR_RATE + '\'' +
                ", ADD_SUR_AMT='" + ADD_SUR_AMT + '\'' +
                ", MTR_RENT_TAX_AMT='" + MTR_RENT_TAX_AMT + '\'' +
                ", MTR_RENT_CGST_AMT='" + MTR_RENT_CGST_AMT + '\'' +
                ", MTR_RENT_SGST_AMT='" + MTR_RENT_SGST_AMT + '\'' +
                ", MTR_RENT_TTL_AMT='" + MTR_RENT_TTL_AMT + '\'' +
                ", MCB_RENT_TAX_AMT='" + MCB_RENT_TAX_AMT + '\'' +
                ", MCB_RENT_CGST_AMT='" + MCB_RENT_CGST_AMT + '\'' +
                ", MCB_RENT_SGST_AMT='" + MCB_RENT_SGST_AMT + '\'' +
                ", MCB_RENT_TTL_AMT='" + MCB_RENT_TTL_AMT + '\'' +
                ", EE_QTY='" + EE_QTY + '\'' +
                ", EE_NON_TAX_AMT='" + EE_NON_TAX_AMT + '\'' +
                ", EE_RENT_TAX_AMT='" + EE_RENT_TAX_AMT + '\'' +
                ", EE_RENT_CGST_AMT='" + EE_RENT_CGST_AMT + '\'' +
                ", EE_RENT_SGST_AMT='" + EE_RENT_SGST_AMT + '\'' +
                ", EE_RENT_TTL_AMT='" + EE_RENT_TTL_AMT + '\'' +
                ", SC_WSD_AMT_WITHHELD='" + SC_WSD_AMT_WITHHELD + '\'' +
                ", TNAME='" + TNAME + '\'' +
                ", SYNCDT='" + SYNCDT + '\'' +
                '}';
    }
}
