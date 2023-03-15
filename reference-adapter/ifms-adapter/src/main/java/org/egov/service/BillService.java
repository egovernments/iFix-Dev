package org.egov.service;

import org.egov.models.Bill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BillService {

    @Autowired
    private PaymentStatusService paymentStatusService; //Just for mocking purpose; to be removed later

    public void processBill(Bill bill) {
        // TODO:    1. Transform bill into IFMSPaymentInstructionDTO
        //          2. Call IFMS

        //Just for mocking
        paymentStatusService.producePaymentStatusFromBill(bill);
    }

}
