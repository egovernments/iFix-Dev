package org.egov.job;

import client.stub.GetBillResult;
import client.stub.GetPaymentResult;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.egov.entity.PspclBillDetail;
import org.egov.entity.PspclPaymentDetail;
import org.egov.model.AccountNumberGpMappingVO;
import org.egov.model.ReconcileVO;
import org.egov.repository.PspclBillDetailRepository;
import org.egov.repository.PspclPaymentDetailRepository;
import org.egov.service.PspclBillAndPaymentReconcileService;
import org.egov.tracer.model.CustomException;
import org.egov.util.MDMSClient;
import org.egov.util.PspclUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
@AllArgsConstructor
@NoArgsConstructor
public class PspclBillAndPaymentFetcherJob implements ApplicationRunner {

    @Autowired
    private PspclUtil pspclUtil;

    @Autowired
    private PspclBillAndPaymentReconcileService pspclBillAndPaymentReconcileService;

    @Autowired
    private MDMSClient mdmsClient;

    @Autowired
    private PspclBillDetailRepository billDetailRepository;

    @Autowired
    private PspclPaymentDetailRepository paymentDetailRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("Started PspclBillAndPaymentFetcherJob ....");
        List<ReconcileVO> reconcileVOS = new ArrayList<>();

        try {
            List<AccountNumberGpMappingVO> acnGpMappingVOs = mdmsClient.getAcnGpMappingVOs();
            if (!acnGpMappingVOs.isEmpty()) {
                for (AccountNumberGpMappingVO acnGpMappingVO : acnGpMappingVOs) {
                    //get the bills And payments from PSPCL system
                    log.info("Get pspcl details for account number : {}", acnGpMappingVO.getAccountNumber());
                    List<GetBillResult> pspclBillResults = pspclUtil.getBillsFromPspcl(acnGpMappingVO.getAccountNumber());
                    List<GetPaymentResult> pspclPaymentResults = pspclUtil.getPaymentsFromPspcl(acnGpMappingVO.getAccountNumber());

                    ReconcileVO reconcileVO = pspclBillAndPaymentReconcileService.reconcile(pspclBillResults, pspclPaymentResults);

                    reconcileVO.setDepartmentEntityCode(acnGpMappingVO.getDepartmentEntityCode());
                    reconcileVO.setDepartmentEntityName(acnGpMappingVO.getDepartmentEntityName());

                    reconcileVOS.add(reconcileVO);

                }

            }

            //save to DB
            savePspclDetails(reconcileVOS);

            //publish the events
            pspclBillAndPaymentReconcileService.publishFiscalEvent(reconcileVOS);

        } catch (Exception e) {
            log.error("Exception occurred while running PspclBillAndPaymentFetcherJob", e);
            throw new CustomException("JOB_FAILURE", "Exception occurred while running PspclBillAndPaymentFetcherJob");
        }
        log.info("Completed PspclBillAndPaymentFetcherJob ....");
    }

    private void savePspclDetails(List<ReconcileVO> reconcileVOS) {
        List<PspclBillDetail> pspclBillDetails = new ArrayList<>();
        List<PspclPaymentDetail> pspclPaymentDetails = new ArrayList<>();
        for (ReconcileVO reconcileVO : reconcileVOS) {
            if (reconcileVO.getCurrentPspclBillDetail() != null) {
                pspclBillDetails.add(reconcileVO.getCurrentPspclBillDetail());
            }
            if (reconcileVO.getCurrentPspclPaymentDetail() != null) {
                pspclPaymentDetails.add(reconcileVO.getCurrentPspclPaymentDetail());
            }
        }
        if (!pspclBillDetails.isEmpty()) {
            billDetailRepository.save(pspclBillDetails);
        }
        if (!pspclPaymentDetails.isEmpty()) {
            paymentDetailRepository.save(pspclPaymentDetails);
        }
    }
}
