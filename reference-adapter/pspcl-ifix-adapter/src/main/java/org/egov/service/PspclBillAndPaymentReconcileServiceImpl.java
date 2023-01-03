package org.egov.service;

import client.stub.GetBillResult;
import client.stub.GetPaymentResult;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.egov.common.contract.request.RequestHeader;
import org.egov.entity.PspclBillDetail;
import org.egov.entity.PspclPaymentDetail;
import org.egov.mapper.PspclDataEntityMapper;
import org.egov.model.FiscalEvent;
import org.egov.model.FiscalEventRequest;
import org.egov.model.ReconcileVO;
import org.egov.repository.PspclBillDetailRepository;
import org.egov.repository.PspclPaymentDetailRepository;
import org.egov.util.FiscalEventUtil;
import org.egov.util.PspclIfixAdapterUtil;
import org.egov.util.RequestHeaderUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.egov.util.PspclIfixAdapterConstant.BILL_ISSUE_DATE_FORMAT;
import static org.egov.util.PspclIfixAdapterConstant.TXN_DATE_FORMAT;

@Service
@Slf4j
@AllArgsConstructor
@NoArgsConstructor
public class PspclBillAndPaymentReconcileServiceImpl implements PspclBillAndPaymentReconcileService {

    @Autowired
    private PspclIfixAdapterUtil pspclIfixAdapterUtil;

    @Autowired
    private PspclDataEntityMapper pspclDataEntityMapper;

    @Autowired
    private PspclBillDetailRepository billDetailRepository;

    @Autowired
    private PspclPaymentDetailRepository paymentDetailRepository;

    @Autowired
    private FiscalEventUtil fiscalEventUtil;

    @Autowired
    private RequestHeaderUtil requestHeaderUtil;

    @Autowired
    private PspclBillReconcileService billReconcileService;

    @Autowired
    private PspclPaymentReconcileService paymentReconcileService;


    @Override
    public ReconcileVO reconcile(List<GetBillResult> pspclBillResults, List<GetPaymentResult> pspclPaymentResults) {
        log.info("Started - reconcile pspcl bills and payments...");
        ReconcileVO reconcileVO = new ReconcileVO();
        PspclBillDetail lastBillDetail = null;
        PspclPaymentDetail lastPaymentDetail = null;
        BigDecimal lastBillAmt = null;
        BigDecimal lastPaymentAmt = null;
        BigDecimal currentBillAmt = null;
        BigDecimal currentMonthBillAmt = null;

        //1. sort bills result based on 'BILL_ISSUE_DATE' and payments result based on 'TXNDATE'
        sortBillResultsWRTLatestBillIssueDate(pspclBillResults);
        sortPaymentResultsWRTTXNDate(pspclPaymentResults);

        billReconcileService.reconcileBill(pspclBillResults, reconcileVO);
        paymentReconcileService.reconcilePayment(pspclPaymentResults, reconcileVO);

        if (reconcileVO.getCurrentPspclBillDetail() != null && !reconcileVO.isBillReconcile()) {
            PspclPaymentDetail currentMonthPaymentResult = null;
            PspclBillDetail currentPspclBillDetail = reconcileVO.getCurrentPspclBillDetail();
            if (reconcileVO.getCurrentPspclPaymentDetail() != null && !reconcileVO.isPaymentReconcile()) {
                currentMonthPaymentResult = reconcileVO.getCurrentPspclPaymentDetail();
            }
            //2. Fetch the last month bill & Payment
            //a. Get the Last_Bill based on 'DATE_READING_PREV'
            //b. Get the Last_Payment based on range of 'DATE_READING_PREV' to current time.
            Date lastBillDate = currentPspclBillDetail != null ? currentPspclBillDetail.getDATE_READING_PREV() : null;
            String accountNumber = currentPspclBillDetail != null ? currentPspclBillDetail.getACCOUNT_NO() : null;
            if (currentPspclBillDetail != null && StringUtils.isNotBlank(currentPspclBillDetail.getPAYABLE_AMOUNT_BY_DUE_DATE())) {

                currentBillAmt = new BigDecimal(currentPspclBillDetail.getPAYABLE_AMOUNT_BY_DUE_DATE());
                if (lastBillDate != null) {
                    lastBillDetail = getLastBillDetail(lastBillDate, accountNumber);
                    lastPaymentDetail = getLastPaymentDetails(lastBillDate, accountNumber);
                    if (lastBillDetail != null) {
                        lastBillAmt = new BigDecimal(lastBillDetail.getPAYABLE_AMOUNT_BY_DUE_DATE());
                    } else {
                        lastBillAmt = new BigDecimal("0");
                    }
                    if (lastPaymentDetail != null) {
                        lastPaymentAmt = new BigDecimal(lastPaymentDetail.getAMT());
                    } else {
                        lastPaymentAmt = new BigDecimal("0");
                    }
                }

                //3. calculate the current bill and current payment
                currentMonthBillAmt = (currentBillAmt.subtract(lastBillAmt.subtract(lastPaymentAmt)));

                reconcileVO.setCurrentCalculatedBillAmt(currentMonthBillAmt);

                return reconcileVO;
            }

        }
        return reconcileVO;
    }

    @Override
    public void publishFiscalEvent(List<ReconcileVO> reconcileVOS) {
        log.info("Preparing for fiscal event publish to iFix...");
        int chunkSize = 5;
        int chunkPointer = 0;
        if (reconcileVOS != null && !reconcileVOS.isEmpty()) {
            int totalPublishEventSize = reconcileVOS.size();
            int noOfChunk = (totalPublishEventSize / chunkSize);
            int leftChunkData = (totalPublishEventSize % chunkSize);
            //process - noOfChunk
            for (int chunkIndex = 0; chunkIndex < noOfChunk; chunkIndex++) {
                int dataIndex = 0;
                chunkPointer = createFiscalEventAndPublish(reconcileVOS, chunkSize, chunkPointer);
            }
            //process - leftChunkData
            if (leftChunkData != 0) {
                chunkPointer = createFiscalEventAndPublish(reconcileVOS, leftChunkData, chunkPointer);
            }

        }


    }

    private int createFiscalEventAndPublish(List<ReconcileVO> reconcileVOS, int chunkSize, int chunkPointer) {
        int dataIndex;
        List<FiscalEvent> fiscalEvents = new ArrayList<>();

        //create the fiscal event
        for (dataIndex = chunkPointer; dataIndex < (chunkPointer + chunkSize); dataIndex++) {
            ReconcileVO billAndPaymentEventDetail = reconcileVOS.get(dataIndex);
            FiscalEvent billFiscalEvent = fiscalEventUtil.getDemandFiscalEvent(billAndPaymentEventDetail);
            FiscalEvent paymentFiscalEvent = fiscalEventUtil.getReceiptFiscalEvent(billAndPaymentEventDetail);

            if (billFiscalEvent != null) {
                fiscalEvents.add(billFiscalEvent);
            }
            if (paymentFiscalEvent != null) {
                fiscalEvents.add(paymentFiscalEvent);
            }
        }

        //publish the fiscal event
        FiscalEventRequest fiscalEventRequest = new FiscalEventRequest();
        RequestHeader requestHeader = requestHeaderUtil.getRequestHeader();
        fiscalEventRequest.setFiscalEvent(fiscalEvents);
        fiscalEventRequest.setRequestHeader(requestHeader);

        fiscalEventUtil.publishFiscalEvent(fiscalEventRequest);

        chunkPointer = dataIndex;
        return chunkPointer;
    }

    private PspclPaymentDetail getLastPaymentDetails(Date lastBillDate, String accountNumber) {
        Date lastBillDateFormatted = pspclIfixAdapterUtil.format(BILL_ISSUE_DATE_FORMAT, lastBillDate);
        Date fromDate = pspclIfixAdapterUtil.format(TXN_DATE_FORMAT, lastBillDateFormatted);
        Date toDate = new Date();
        List<PspclPaymentDetail> dbPspclPaymentDetails = paymentDetailRepository.findByTXNDATEBetweenOrderByTXNDATEDescAndAccountNumber(fromDate, toDate, accountNumber);
        return (dbPspclPaymentDetails != null && !dbPspclPaymentDetails.isEmpty() ? dbPspclPaymentDetails.get(0) : null);
    }

    private PspclBillDetail getLastBillDetail(Date lastBillDate, String accountNumber) {
        Optional<PspclBillDetail> lastMonthBillOptional = billDetailRepository
                .findByBillIssueDateAndAccountNumber(lastBillDate, accountNumber);
        if (lastMonthBillOptional.isPresent())
            return lastMonthBillOptional.get();
        return null;
    }

    private void sortPaymentResultsWRTTXNDate(List<GetPaymentResult> pspclPaymentResults) {
        pspclPaymentResults.sort((paymentResult1, paymentResult2) -> {

            if (StringUtils.isNotBlank(paymentResult1.getTXNDATE())
                    && StringUtils.isNotBlank(paymentResult2.getTXNDATE())) {

                Date txnDate1 = pspclIfixAdapterUtil.format(TXN_DATE_FORMAT, paymentResult1.getTXNDATE());
                Date txnDate2 = pspclIfixAdapterUtil.format(TXN_DATE_FORMAT, paymentResult2.getTXNDATE());

                return (txnDate2.compareTo(txnDate1));
            }
            return 0;
        });
    }

    private void sortBillResultsWRTLatestBillIssueDate(List<GetBillResult> pspclBillResults) {
        pspclBillResults.sort((billResult1, billResult2) -> {

            if (StringUtils.isNotBlank(billResult1.getBILL_ISSUE_DATE())
                    && StringUtils.isNotBlank(billResult2.getBILL_ISSUE_DATE())) {

                Date billIssueDate1 = pspclIfixAdapterUtil.format(BILL_ISSUE_DATE_FORMAT, billResult1.getBILL_ISSUE_DATE());
                Date billIssueDate2 = pspclIfixAdapterUtil.format(BILL_ISSUE_DATE_FORMAT, billResult2.getBILL_ISSUE_DATE());

                return (billIssueDate2.compareTo(billIssueDate1));
            }
            return 0;
        });
    }
}
