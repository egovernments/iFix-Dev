package org.egov.service;

import client.stub.GetPaymentResult;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.egov.entity.PspclPaymentDetail;
import org.egov.mapper.PspclDataEntityMapper;
import org.egov.model.ReconcileVO;
import org.egov.repository.PspclPaymentDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@AllArgsConstructor
@NoArgsConstructor
public class PspclPaymentReconcileService {

    @Autowired
    private PspclDataEntityMapper pspclDataEntityMapper;

    @Autowired
    private PspclPaymentDetailRepository paymentDetailRepository;


    public void reconcilePayment(List<GetPaymentResult> pspclPaymentResults, ReconcileVO reconcileVO) {
        if (pspclPaymentResults != null && !pspclPaymentResults.isEmpty()) {
            GetPaymentResult currentMonthPaymentResult = pspclPaymentResults.get(0);

            Optional<PspclPaymentDetail> optionalPspclPaymentDetail = paymentDetailRepository
                    .findByTXNIDAndAccountNumber(currentMonthPaymentResult.getTXNID(), currentMonthPaymentResult.getACNO());
            if (optionalPspclPaymentDetail.isPresent()) {
                reconcileVO.setPaymentReconcile(true);
            } else {
                PspclPaymentDetail currentPspclPaymentDetail = pspclDataEntityMapper.mapPspclPaymentToEntity(currentMonthPaymentResult);
                reconcileVO.setCurrentPspclPaymentDetail(currentPspclPaymentDetail);
            }
        }

    }
}
