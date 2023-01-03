package org.egov.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.egov.common.contract.request.RequestHeader;
import org.egov.config.PspclIfixAdapterConfiguration;
import org.egov.entity.*;
import org.egov.model.FiscalEvent;
import org.egov.model.FiscalEventRequest;
import org.egov.model.FiscalEventResponse;
import org.egov.model.ReconcileVO;
import org.egov.repository.EventPostingDetailRepository;
import org.egov.service.AuthTokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.stubbing.OngoingStubbing;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class FiscalEventUtilTest {

    @Mock
    private AuthTokenService authTokenService;

    @Mock
    private EventPostingDetailRepository eventPostingDetailRepository;

    private FiscalEventUtil fiscalEventUtil;

    @Spy
    private ObjectMapper objectMapper;

    @Mock
    private PspclIfixAdapterConfiguration pspclIfixAdapterConfiguration;

    @Mock
    private PspclIfixAdapterUtil pspclIfixAdapterUtil;

    @Spy
    private RestTemplate restTemplate;

    PspclBillDetail pspclBillDetail;
    PspclPaymentDetail pspclPaymentDetail;

    @BeforeEach
    private void init() throws IOException {
        MockitoAnnotations.openMocks(this);
        fiscalEventUtil = new FiscalEventUtil(pspclIfixAdapterConfiguration, pspclIfixAdapterUtil, authTokenService,
                restTemplate, objectMapper, eventPostingDetailRepository);

        BillJsonData billJsonData = new BillJsonData();
        billJsonData.setADD_SUR_AMT("ADD SUR AMT");
        billJsonData.setADD_SUR_RATE("ADD SUR RATE");
        billJsonData.setADD_SUR_UNITS("ADD SUR UNITS");
        billJsonData.setBILL_CYCLE("BILL CYCLE");
        billJsonData.setBILL_GROUP("BILL GROUP");
        billJsonData.setBILL_STATUS("BILL STATUS");
        billJsonData.setCGST("CGST");
        billJsonData.setCIRCLE("CIRCLE");
        billJsonData.setCONNECTED_LOAD("CONNECTED LOAD");
        billJsonData.setCONSUMER_ADDRESS("CONSUMER ADDRESS");
        billJsonData.setCONSUMER_GSTIN("CONSUMER GSTIN");
        billJsonData.setCONSUMER_NAME("CONSUMER NAME");
        billJsonData.setCONSUMPTION_1("CONSUMPTION 1");
        billJsonData.setCONSUMPTION_2("CONSUMPTION 2");
        billJsonData.setCONSUMPTION_3("CONSUMPTION 3");
        billJsonData.setCONSUMPTION_4("CONSUMPTION 4");
        billJsonData.setCONSUMPTION_5("CONSUMPTION 5");
        billJsonData.setCONSUMPTION_6("CONSUMPTION 6");
        billJsonData.setCURRENT_METER_CODE("CURRENT METER CODE");
        billJsonData.setDATE_OF_CONNECTION("DATE OF CONNECTION");
        billJsonData.setDIVISION("DIVISION");
        billJsonData.setEC_000_100_KWH_AMOUNT("10");
        billJsonData.setEC_000_100_KWH_RATE("EC 000 100 KWH RATE");
        billJsonData.setEC_000_100_KWH_UNITS("EC 000 100 KWH UNITS");
        billJsonData.setEC_101_300_KWH_AMOUNT("10");
        billJsonData.setEC_101_300_KWH_RATE("EC 101 300 KWH RATE");
        billJsonData.setEC_101_300_KWH_UNITS("EC 101 300 KWH UNITS");
        billJsonData.setEC_301_500_KWH_AMOUNT("10");
        billJsonData.setEC_301_500_KWH_RATE("EC 301 500 KWH RATE");
        billJsonData.setEC_301_500_KWH_UNITS("EC 301 500 KWH UNITS");
        billJsonData.setEC_501_ABOVE_KWH_AMOUNT("10");
        billJsonData.setEC_501_ABOVE_KWH_RATE("EC 501 ABOVE KWH RATE");
        billJsonData.setEC_501_ABOVE_KWH_UNITS("EC 501 ABOVE KWH UNITS");
        billJsonData.setEC_KWH_TOTAL_AMOUNT("10");
        billJsonData.setEE_NON_TAX_AMT("EE NON TAX AMT");
        billJsonData.setEE_QTY("EE QTY");
        billJsonData.setEE_RENT_CGST_AMT("EE RENT CGST AMT");
        billJsonData.setEE_RENT_SGST_AMT("EE RENT SGST AMT");
        billJsonData.setEE_RENT_TAX_AMT("EE RENT TAX AMT");
        billJsonData.setEE_RENT_TTL_AMT("EE RENT TTL AMT");
        billJsonData.setEMAIL_ID("EMAIL ID");
        billJsonData.setEMPID("EMPID");
        billJsonData.setEXTRA1("EXTRA1");
        billJsonData.setEXTRA10("EXTRA10");
        billJsonData.setEXTRA2("EXTRA2");
        billJsonData.setEXTRA3("EXTRA3");
        billJsonData.setEXTRA4("EXTRA4");
        billJsonData.setEXTRA5("EXTRA5");
        billJsonData.setEXTRA6("EXTRA6");
        billJsonData.setEXTRA7("EXTRA7");
        billJsonData.setEXTRA8("EXTRA8");
        billJsonData.setEXTRA9("EXTRA9");
        billJsonData.setFC_BILL_PERIOD("FC BILL PERIOD");
        billJsonData.setFC_LOAD("FC LOAD");
        billJsonData.setFC_RATE_KW_MON("FC RATE KW MON");
        billJsonData.setFC_TOTAL("FC TOTAL");
        billJsonData.setFEEDER_CODE("FEEDER CODE");
        billJsonData.setFUEL_COST_ADJ_AMOUNT("10");
        billJsonData.setFUEL_COST_ADJ_RATE("FUEL COST ADJ RATE");
        billJsonData.setFUEL_COST_ADJ_UNITS("FUEL COST ADJ UNITS");
        billJsonData.setINTEREST_OF_SECURITY("INTEREST OF SECURITY");
        billJsonData.setINV_REMARKS("INV REMARKS");
        billJsonData.setLPSC_2_PERCENT("LPSC 2 PERCENT");
        billJsonData.setMCB_RENT_CGST_AMT("MCB RENT CGST AMT");
        billJsonData.setMCB_RENT_SGST_AMT("MCB RENT SGST AMT");
        billJsonData.setMCB_RENT_TAX_AMT("MCB RENT TAX AMT");
        billJsonData.setMCB_RENT_TTL_AMT("MCB RENT TTL AMT");
        billJsonData.setMESSAGE("MESSAGE");
        billJsonData.setMETER_MULTIPLIER("METER MULTIPLIER");
        billJsonData.setMETER_SECURITY_AMOUNT("10");
        billJsonData.setMOBILE_NO("MOBILE NO");
        billJsonData.setMRU("MRU");
        billJsonData.setMTR_CAPACITY("MTR CAPACITY");
        billJsonData.setMTR_DIGIT("MTR DIGIT");
        billJsonData.setMTR_MAKE("MTR MAKE");
        billJsonData.setMTR_NO("MTR NO");
        billJsonData.setMTR_READING_CURR("MTR READING CURR");
        billJsonData.setMTR_READING_PREV("MTR READING PREV");
        billJsonData.setMTR_RENT_CGST_AMT("MTR RENT CGST AMT");
        billJsonData.setMTR_RENT_SGST_AMT("MTR RENT SGST AMT");
        billJsonData.setMTR_RENT_TAX_AMT("MTR RENT TAX AMT");
        billJsonData.setMTR_RENT_TTL_AMT("MTR RENT TTL AMT");
        billJsonData.setOLD_ACCOUNT_NO("3");
        billJsonData.setPAYMENT_HISTORY_1("PAYMENT HISTORY 1");
        billJsonData.setPAYMENT_HISTORY_2("PAYMENT HISTORY 2");
        billJsonData.setPAYMENT_HISTORY_3("PAYMENT HISTORY 3");
        billJsonData.setPAYMENT_HISTORY_4("PAYMENT HISTORY 4");
        billJsonData.setPAYMENT_HISTORY_5("PAYMENT HISTORY 5");
        billJsonData.setPAYMENT_HISTORY_6("PAYMENT HISTORY 6");
        billJsonData.setPRE_ADJ_ENERGY_CHARGES("PRE ADJ ENERGY CHARGES");
        billJsonData.setPRE_ADJ_FCA_PLUS_RENTALS("PRE ADJ FCA PLUS RENTALS");
        billJsonData.setPRE_ADJ_FIXED_CHARGES("PRE ADJ FIXED CHARGES");
        billJsonData.setPRE_ADJ_TAXES("PRE ADJ TAXES");
        billJsonData.setPRE_ADJ_TOTAL("PRE ADJ TOTAL");
        billJsonData.setPRE_ARR_INTEREST("PRE ARR INTEREST");
        billJsonData.setPRE_ARR_PENDING_AMOUNT("10");
        billJsonData.setPRE_ARR_SURCHARGES("PRE ARR SURCHARGES");
        billJsonData.setPRE_ARR_TOTAL("PRE ARR TOTAL");
        billJsonData.setRENT_MCB("RENT MCB");
        billJsonData.setRENT_METER("RENT METER");
        billJsonData.setRENT_OTHER("RENT OTHER");
        billJsonData.setRENT_TOTAL("RENT TOTAL");
        billJsonData.setSC_WSD_AMT_WITHHELD("SC WSD AMT WITHHELD");
        billJsonData.setSECURITY_CONS_AMOUNT("10");
        billJsonData.setSGST("SGST");
        billJsonData.setSUBSIDY_GOP_AMOUNT("10");
        billJsonData.setSUBSIDY_UNITS("SUBSIDY UNITS");
        billJsonData.setSUB_DIVISION_CODE("SUB DIVISION CODE");
        billJsonData.setSUB_DIV_NAME("SUB DIV NAME");
        billJsonData.setSUN_ALLW_CUR_PRE_ROUNDING("SUN ALLW CUR PRE ROUNDING");
        billJsonData.setSUN_ALLW_EC("SUN ALLW EC");
        billJsonData.setSUN_ALLW_FC("SUN ALLW FC");
        billJsonData.setSUN_ALLW_FCA_PLUS_RENTALS("SUN ALLW FCA PLUS RENTALS");
        billJsonData.setSUN_ALLW_NOTICE_DATE("SUN ALLW NOTICE DATE");
        billJsonData.setSUN_ALLW_NOTICE_NO("SUN ALLW NOTICE NO");
        billJsonData.setSUN_ALLW_TAXES("SUN ALLW TAXES");
        billJsonData.setSUN_ALLW_TOTAL("SUN ALLW TOTAL");
        billJsonData.setSUN_CHRG_EC("SUN CHRG EC");
        billJsonData.setSUN_CHRG_FC("SUN CHRG FC");
        billJsonData.setSUN_CHRG_FCA_PLUS_RENTALS("SUN CHRG FCA PLUS RENTALS");
        billJsonData.setSUN_CHRG_NOTICE_DATE("SUN CHRG NOTICE DATE");
        billJsonData.setSUN_CHRG_NOTICE_NO("SUN CHRG NOTICE NO");
        billJsonData.setSUN_CHRG_TAXES("SUN CHRG TAXES");
        billJsonData.setSUN_CHRG_TOTAL("SUN CHRG TOTAL");
        billJsonData.setSYNCDT("SYNCDT");
        billJsonData.setTAX_NET_COWCESS_WITH_SIGN("TAX NET COWCESS WITH SIGN");
        billJsonData.setTAX_NET_ED_WITH_SIGN("TAX NET ED WITH SIGN");
        billJsonData.setTAX_NET_IDF_WITH_SIGN("TAX NET IDF WITH SIGN");
        billJsonData.setTAX_NET_MC_WITH_SIGN("TAX NET MC WITH SIGN");
        billJsonData.setTAX_TOTAL("TAX TOTAL");
        billJsonData.setTNAME("TNAME");
        billJsonData.setTOTAL_CONSUMPTION("TOTAL CONSUMPTION");
        billJsonData.setTOTAL_CONSUMPTION_NEW("TOTAL CONSUMPTION NEW");
        billJsonData.setTOTAL_CONSUMPTION_OLD("TOTAL CONSUMPTION OLD");
        billJsonData.setUNITS_CONCESSION("UNITS CONCESSION");
        billJsonData.setUSERID("USERID");

        pspclBillDetail = new PspclBillDetail();
        pspclBillDetail.setACCOUNT_NO("3");
        LocalDateTime atStartOfDayResult = LocalDate.of(1970, 1, 1).atStartOfDay();
        pspclBillDetail.setBILL_ISSUE_DATE(Date.from(atStartOfDayResult.atZone(ZoneId.of("UTC")).toInstant()));
        pspclBillDetail.setBILL_NO("BILL NO");
        pspclBillDetail.setBillJsonData(billJsonData);
        LocalDateTime atStartOfDayResult1 = LocalDate.of(1970, 1, 1).atStartOfDay();
        pspclBillDetail.setDATE_READING_CURR(Date.from(atStartOfDayResult1.atZone(ZoneId.of("UTC")).toInstant()));
        LocalDateTime atStartOfDayResult2 = LocalDate.of(1970, 1, 1).atStartOfDay();
        pspclBillDetail.setDATE_READING_PREV(Date.from(atStartOfDayResult2.atZone(ZoneId.of("UTC")).toInstant()));
        pspclBillDetail.setDUE_DATE_CASH_ONLINE("DUE DATE CASH ONLINE");
        pspclBillDetail.setDUE_DATE_CHEQUE_DD("DUE DATE CHEQUE DD");
        pspclBillDetail.setId(123L);
        pspclBillDetail.setORDERBYCOLUMN("ORDERBYCOLUMN");
        pspclBillDetail.setPAYABLE_AMOUNT_BY_DUE_DATE("10");
        pspclBillDetail.setPAYABLE_AMOUNT_UPTO_15_DAYS("10");
        pspclBillDetail.setTARIFF_TYPE("TARIFF TYPE");

        PaymentJsonData paymentJsonData = new PaymentJsonData();
        paymentJsonData.setAGENCY_CODE("AGENCY CODE");
        paymentJsonData.setAMT_AFTER_DUEDATE("AMT AFTER DUEDATE");
        paymentJsonData.setBILYR("BILYR");
        paymentJsonData.setBRANCH("BRANCH");
        paymentJsonData.setCASHDESK("CASHDESK");
        paymentJsonData.setCATEGORY("CATEGORY");
        paymentJsonData.setCHEQUE_DATE("CHEQUE DATE");
        paymentJsonData.setCHEQUE_NO("CHEQUE NO");
        paymentJsonData.setCIRNAME("CIRNAME");
        paymentJsonData.setCNAME("CNAME");
        paymentJsonData.setCODE_SDIV("CODE SDIV");
        paymentJsonData.setCONSUMER_ID("CONSUMER ID");
        paymentJsonData.setDIVNAME("DIVNAME");
        paymentJsonData.setED("ED");
        paymentJsonData.setELID("ELID");
        paymentJsonData.setEMAIL("EMAIL");
        paymentJsonData.setIF_SAP("IF SAP");
        paymentJsonData.setMBNO("MBNO");
        paymentJsonData.setOCTRAI("OCTRAI");
        paymentJsonData.setPAYMENT_MODE("PAYMENT MODE");
        paymentJsonData.setPG_ERR_CODE("PG ERR CODE");
        paymentJsonData.setPG_ERR_MSG("PG ERR MSG");
        paymentJsonData.setPG_REF_ID("PG REF ID");
        paymentJsonData.setPG_STATUS("PG STATUS");
        paymentJsonData.setPU_WEBHOOK_ID("PU WEBHOOK ID");
        paymentJsonData.setRECEIPTDT("RECEIPTDT");
        paymentJsonData.setRECEIPTNO("RECEIPTNO");
        paymentJsonData.setRECON_ID("RECON ID");
        paymentJsonData.setSEND_MAIL_MSG("SEND MAIL MSG");
        paymentJsonData.setSEND_MOB_SMS("SEND MOB SMS");
        paymentJsonData.setSOP("SOP");
        paymentJsonData.setSUBDIVNAME("SUBDIVNAME");
        paymentJsonData.setSURCHARGE("SURCHARGE");
        paymentJsonData.setSYNCDT("SYNCDT");
        paymentJsonData.setSYNCED("SYNCED");
        paymentJsonData.setSYNCMSG("SYNCMSG");
        paymentJsonData.setTBL_NAME("TBL NAME");
        paymentJsonData.setTOT_AMT("TOT AMT");
        paymentJsonData.setVID("VID");
        paymentJsonData.setWATER_SUPPLY("WATER SUPPLY");

        pspclPaymentDetail = new PspclPaymentDetail();
        pspclPaymentDetail.setACNO("ACNO");
        pspclPaymentDetail.setAMT("AMT");
        pspclPaymentDetail.setBILCYC("BILCYC");
        pspclPaymentDetail.setBILGRP("BILGRP");
        LocalDateTime atStartOfDayResult3 = LocalDate.of(1970, 1, 1).atStartOfDay();
        pspclPaymentDetail.setBILISSDT(Date.from(atStartOfDayResult3.atZone(ZoneId.of("UTC")).toInstant()));
        pspclPaymentDetail.setBILNO("BILNO");
        LocalDateTime atStartOfDayResult4 = LocalDate.of(1970, 1, 1).atStartOfDay();
        pspclPaymentDetail.setDUEDTCASH(Date.from(atStartOfDayResult4.atZone(ZoneId.of("UTC")).toInstant()));
        LocalDateTime atStartOfDayResult5 = LocalDate.of(1970, 1, 1).atStartOfDay();
        pspclPaymentDetail.setDUEDTCHQ(Date.from(atStartOfDayResult5.atZone(ZoneId.of("UTC")).toInstant()));
        pspclPaymentDetail.setId(123L);
        pspclPaymentDetail.setPaymentJsonData(paymentJsonData);
        pspclPaymentDetail.setSTATUS_P("STATUS P");
        LocalDateTime atStartOfDayResult6 = LocalDate.of(1970, 1, 1).atStartOfDay();
        pspclPaymentDetail.setTXNDATE(Date.from(atStartOfDayResult6.atZone(ZoneId.of("UTC")).toInstant()));
        pspclPaymentDetail.setTXNID("TXNID");
    }

    @Test
    void testGetReceiptFiscalEvent() {
        doReturn("8247-12-373-56-78-90").when(pspclIfixAdapterConfiguration).getReceiptCoaCode();

        ReconcileVO reconcileVO = new ReconcileVO();
        reconcileVO.setCurrentCalculatedBillAmt(BigDecimal.valueOf(1L));
        reconcileVO.setCurrentPspclBillDetail(pspclBillDetail);
        reconcileVO.setCurrentPspclPaymentDetail(pspclPaymentDetail);
        reconcileVO.setDepartmentEntityCode("Department Entity Code");
        reconcileVO.setDepartmentEntityName("Department Entity Name");
        reconcileVO.setStatus(true);
        assertThrows(Exception.class,()->this.fiscalEventUtil.getReceiptFiscalEvent(reconcileVO));
    }

    @Test
    void testGetReceiptFiscalEventWithNullReconcileVO() {
        ReconcileVO reconcileVO = null;
        assertNull(this.fiscalEventUtil.getReceiptFiscalEvent(reconcileVO));
    }

    @Test
    void testGetDemandFiscalEvent() {
        doReturn("8247-12-373-56-78-90").when(pspclIfixAdapterConfiguration).getDemandCoaCode();

        ReconcileVO reconcileVO = new ReconcileVO();
        reconcileVO.setCurrentCalculatedBillAmt(BigDecimal.valueOf(1L));
        reconcileVO.setCurrentPspclBillDetail(pspclBillDetail);
        reconcileVO.setCurrentPspclPaymentDetail(pspclPaymentDetail);
        reconcileVO.setDepartmentEntityCode("Department Entity Code");
        reconcileVO.setDepartmentEntityName("Department Entity Name");
        reconcileVO.setStatus(true);
        assertThrows(Exception.class,()->fiscalEventUtil.getDemandFiscalEvent(reconcileVO));
    }

    @Test
    void testPublishFiscalEventWithDefaultFiscalEventReq() {
        when(this.authTokenService.getAuthToken()).thenReturn("ABC123");
        this.fiscalEventUtil.publishFiscalEvent(new FiscalEventRequest());
        verify(this.authTokenService).getAuthToken();
    }

    @Test
    void testPublishFiscalEventWithNullReq() {
        when(this.authTokenService.getAuthToken()).thenReturn("ABC123");
        this.fiscalEventUtil.publishFiscalEvent(null);
        verify(this.authTokenService).getAuthToken();
    }

    @Test
    void testPublishFiscalEventWithEmptyFiscalEvents() {
        when(this.authTokenService.getAuthToken()).thenReturn("ABC123");
        RequestHeader requestHeader = new RequestHeader();
        this.fiscalEventUtil.publishFiscalEvent(new FiscalEventRequest(requestHeader, new ArrayList<>()));
        verify(this.authTokenService).getAuthToken();
    }

    @Test
    void testPublishFiscalEventWithAuthTokenException() {
        when(this.authTokenService.getAuthToken())
                .thenThrow(new RestClientException("Posting fiscal eventRequest to iFix... "));
        assertThrows(RestClientException.class, () -> this.fiscalEventUtil.publishFiscalEvent(new FiscalEventRequest()));
        verify(this.authTokenService).getAuthToken();
    }

    @Test
    void testPublishFiscalEventWithFiscalEvents() throws RestClientException {
        when(this.pspclIfixAdapterConfiguration.getIfixEventUrl()).thenReturn("https://example.org/example");
        when(this.pspclIfixAdapterConfiguration.getIfixHost()).thenReturn("localhost");

        ResponseEntity<FiscalEventResponse> responseEntity =new ResponseEntity<>(HttpStatus.OK);
        doReturn(responseEntity).when(restTemplate).postForEntity((String) any(), (Object) any(), (Class<Object>) any(), (Object[]) any());


        when(this.eventPostingDetailRepository.save((List<EventPostingDetail>) any())).thenReturn(new int[1]);
        when(this.authTokenService.getAuthToken()).thenReturn("ABC123");

        ArrayList<FiscalEvent> fiscalEventList = new ArrayList<>();
        fiscalEventList.add(new FiscalEvent());
        this.fiscalEventUtil.publishFiscalEvent(new FiscalEventRequest(new RequestHeader(), fiscalEventList));
        verify(this.restTemplate).postForEntity((String) any(), (Object) any(), (Class<Object>) any(), (Object[]) any());
        verify(this.pspclIfixAdapterConfiguration).getIfixEventUrl();
        verify(this.pspclIfixAdapterConfiguration).getIfixHost();
        verify(this.eventPostingDetailRepository).save((List<EventPostingDetail>) any());
        verify(this.authTokenService).getAuthToken();
    }

    @Test
    void testPublishFiscalEventWithExceptionAtSavingEvents() throws RestClientException {
//        when(this.restTemplate.postForEntity((String) any(), (Object) any(), (Class<Object>) any(), (Object[]) any()))
//                .thenReturn(new ResponseEntity<>(HttpStatus.CONTINUE));

        ResponseEntity<FiscalEventResponse> responseEntity =new ResponseEntity<>(HttpStatus.OK);
        doReturn(responseEntity).when(restTemplate).postForEntity((String) any(), (Object) any(), (Class<Object>) any(), (Object[]) any());

        when(this.pspclIfixAdapterConfiguration.getIfixEventUrl()).thenReturn("https://example.org/example");
        when(this.pspclIfixAdapterConfiguration.getIfixHost()).thenReturn("localhost");
        when(this.eventPostingDetailRepository.save((List<EventPostingDetail>) any()))
                .thenThrow(new RestClientException("Posting fiscal eventRequest to iFix... "));
        when(this.authTokenService.getAuthToken()).thenReturn("ABC123");

        ArrayList<FiscalEvent> fiscalEventList = new ArrayList<>();
        fiscalEventList.add(new FiscalEvent());
        assertThrows(RestClientException.class,
                () -> this.fiscalEventUtil.publishFiscalEvent(new FiscalEventRequest(new RequestHeader(), fiscalEventList)));
        verify(this.restTemplate).postForEntity((String) any(), (Object) any(), (Class<Object>) any(), (Object[]) any());
        verify(this.pspclIfixAdapterConfiguration).getIfixEventUrl();
        verify(this.pspclIfixAdapterConfiguration).getIfixHost();
        verify(this.eventPostingDetailRepository).save((List<EventPostingDetail>) any());
        verify(this.authTokenService).getAuthToken();
    }

    @Test
    void testPublishFiscalEventWithSuccessfullySavedEvents() throws JsonProcessingException, RestClientException {
//        when(this.restTemplate.postForEntity((String) any(), (Object) any(), (Class<Object>) any(), (Object[]) any()))
//                .thenReturn(null);

        //ResponseEntity<FiscalEventResponse> responseEntity =new ResponseEntity<>(HttpStatus.OK);
        doReturn(null).when(restTemplate).postForEntity((String) any(), (Object) any(), (Class<Object>) any(), (Object[]) any());

        when(this.pspclIfixAdapterConfiguration.getIfixEventUrl()).thenReturn("https://example.org/example");
        when(this.pspclIfixAdapterConfiguration.getIfixHost()).thenReturn("localhost");
        when(this.objectMapper.writeValueAsString((Object) any())).thenReturn("42");
        when(this.eventPostingDetailRepository.save((List<EventPostingDetail>) any())).thenReturn(new int[1]);
        when(this.authTokenService.getAuthToken()).thenReturn("ABC123");

        ArrayList<FiscalEvent> fiscalEventList = new ArrayList<>();
        fiscalEventList.add(new FiscalEvent());
        this.fiscalEventUtil.publishFiscalEvent(new FiscalEventRequest(new RequestHeader(), fiscalEventList));
        verify(this.restTemplate).postForEntity((String) any(), (Object) any(), (Class<Object>) any(), (Object[]) any());
        verify(this.pspclIfixAdapterConfiguration).getIfixEventUrl();
        verify(this.pspclIfixAdapterConfiguration).getIfixHost();
        verify(this.eventPostingDetailRepository).save((List<EventPostingDetail>) any());
        verify(this.authTokenService).getAuthToken();
    }

    @Test
    void testGetIfixPublishUrl() {
        when(this.pspclIfixAdapterConfiguration.getIfixEventUrl()).thenReturn("https://example.org/example");
        when(this.pspclIfixAdapterConfiguration.getIfixHost()).thenReturn("localhost");
        assertEquals("localhosthttps://example.org/example", this.fiscalEventUtil.getIfixPublishUrl());
        verify(this.pspclIfixAdapterConfiguration).getIfixEventUrl();
        verify(this.pspclIfixAdapterConfiguration).getIfixHost();
    }
}

