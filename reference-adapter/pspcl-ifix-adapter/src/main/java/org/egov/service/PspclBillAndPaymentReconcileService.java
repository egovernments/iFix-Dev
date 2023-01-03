package org.egov.service;

import client.stub.GetBillResult;
import client.stub.GetPaymentResult;
import org.egov.model.ReconcileVO;

import java.util.List;

public interface PspclBillAndPaymentReconcileService {

    ReconcileVO reconcile(List<GetBillResult> pspclBillResults, List<GetPaymentResult> pspclPaymentResults);

    void publishFiscalEvent(List<ReconcileVO> reconcileVOS);
}
