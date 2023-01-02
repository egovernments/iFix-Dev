package org.egov.service;

import client.stub.GetBillResult;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.egov.entity.PspclBillDetail;
import org.egov.mapper.PspclDataEntityMapper;
import org.egov.model.ReconcileVO;
import org.egov.repository.PspclBillDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
@NoArgsConstructor
public class PspclBillReconcileService {

    @Autowired
    private PspclDataEntityMapper pspclDataEntityMapper;

    @Autowired
    private PspclBillDetailRepository billDetailRepository;


    public void reconcileBill(List<GetBillResult> pspclBillResults, ReconcileVO reconcileVO) {
        if (pspclBillResults != null && !pspclBillResults.isEmpty()) {
            GetBillResult currentMonthBillResult = pspclBillResults.get(0);

            List<PspclBillDetail> pspclBillDetails = billDetailRepository
                    .findByORDERBYCOLUMNAndAccountNumber(currentMonthBillResult.getORDERBYCOLUMN(), currentMonthBillResult.getACCOUNT_NO());
            if (pspclBillDetails != null && !pspclBillDetails.isEmpty()) {
                reconcileVO.setBillReconcile(true);
            } else {
                PspclBillDetail currentPspclBillDetail = pspclDataEntityMapper.mapPspclBillToEntity(currentMonthBillResult);
                reconcileVO.setCurrentPspclBillDetail(currentPspclBillDetail);
            }
        }
    }
}
