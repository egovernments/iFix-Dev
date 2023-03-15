package org.egov.service;

import org.apache.commons.lang3.RandomStringUtils;
import org.egov.config.IfmsAdapterConfig;
import org.egov.kafka.Producer;
import org.egov.models.Beneficiary;
import org.egov.models.BeneficiaryTransferStatus;
import org.egov.models.Bill;
import org.egov.models.PaymentStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PaymentStatusService {

    @Autowired
    private Producer producer;
    @Autowired
    private IfmsAdapterConfig ifmsAdapterConfig;

    // Only for the purpose of mocking
    public void producePaymentStatusFromBill(Bill bill) {
        List<BeneficiaryTransferStatus> beneficiaryTransferStatusList = new ArrayList<>();
        for(Beneficiary beneficiary : bill.getBeneficiaries()) {
            beneficiaryTransferStatusList.add(
                    BeneficiaryTransferStatus.builder()
                            .accountNumber(beneficiary.getAccountNumber())
                            .ifscCode(beneficiary.getIfscCode())
                            .sequenceDate(bill.getBillDate())
                            .rbiSequenceNumber(RandomStringUtils.random(10, false, true))
                            .endToEndId(RandomStringUtils.random(10, false, true))
                            .status("SUCCESS")
                            .build());
        }

        PaymentStatus paymentStatus = PaymentStatus.builder()
                .billNumber(bill.getBillNumber())
                .billDate(bill.getBillDate())
                .voucherNumber(RandomStringUtils.random(10, false, true))
                .voucherDate(bill.getBillDate())
                .beneficiaryTransferStatuses(beneficiaryTransferStatusList)
                .build();

        producer.push(ifmsAdapterConfig.getPaymentStatusTopic(), paymentStatus);
    }

    public void updatePaymentStatus(Object paymentDetailsDTO) {

    }

}
