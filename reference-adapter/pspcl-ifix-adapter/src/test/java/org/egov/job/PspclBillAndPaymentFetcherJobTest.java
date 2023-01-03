package org.egov.job;

import client.stub.GetBillResult;
import client.stub.GetPaymentResult;
import org.egov.model.AccountNumberGpMappingVO;
import org.egov.repository.PspclBillDetailRepository;
import org.egov.repository.PspclPaymentDetailRepository;
import org.egov.service.PspclBillAndPaymentReconcileService;
import org.egov.util.MDMSClient;
import org.egov.util.PspclUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PspclBillAndPaymentFetcherJobTest {

    private PspclBillAndPaymentFetcherJob fetcherJob;


    @Mock
    private PspclUtil pspclUtil;

    @Mock
    private PspclBillAndPaymentReconcileService pspclBillAndPaymentReconcileService;

    @Mock
    private MDMSClient mdmsClient;

    @Mock
    private PspclBillDetailRepository billDetailRepository;

    @Mock
    private PspclPaymentDetailRepository paymentDetailRepository;

    @BeforeEach
    private void init() throws IOException {
        MockitoAnnotations.openMocks(this);
        fetcherJob = new PspclBillAndPaymentFetcherJob(pspclUtil, pspclBillAndPaymentReconcileService,
                mdmsClient, billDetailRepository, paymentDetailRepository);
    }

    @Test
    void executeInternal() {

        List<AccountNumberGpMappingVO> acnGpMappingVOs = new ArrayList<>();
        acnGpMappingVOs.add(new AccountNumberGpMappingVO());
        List<GetBillResult> pspclBillResults = new ArrayList<>();
        List<GetPaymentResult> pspclPaymentResults = new ArrayList<>();

        doReturn(pspclBillResults).when(pspclUtil).getBillsFromPspcl(any());
        doReturn(pspclPaymentResults).when(pspclUtil).getPaymentsFromPspcl(any());

        //ReconcileVO reconcileVO = pspclBillAndPaymentReconcileService.reconcile(pspclBillResults, pspclPaymentResults);
        //fetcherJob.executeInternal(new JobExecutionContextImpl());
    }
}