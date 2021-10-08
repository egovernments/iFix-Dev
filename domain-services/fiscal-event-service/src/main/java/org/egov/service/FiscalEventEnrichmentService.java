package org.egov.service;


import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.AuditDetails;
import org.egov.common.contract.request.RequestHeader;
import org.egov.util.FiscalEventUtil;
import org.egov.web.models.Amount;
import org.egov.web.models.FiscalEvent;
import org.egov.web.models.FiscalEventRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class FiscalEventEnrichmentService {

    @Autowired
    private FiscalEventUtil fiscalEventUtil;


    public void enrichFiscalEventPushPost(FiscalEventRequest fiscalEventRequest) {
        RequestHeader requestHeader = fiscalEventRequest.getRequestHeader();
        FiscalEvent fiscalEvent = fiscalEventRequest.getFiscalEvent();
        //set the id
        if (fiscalEvent != null) {
            fiscalEvent.setId(UUID.randomUUID().toString());

            List<Amount> amounts = new ArrayList<>();
            for (Amount amount : fiscalEvent.getAmountDetails()) {
                Amount newAmount = new Amount();
                BeanUtils.copyProperties(amount, newAmount);
                //set the amount id
                newAmount.setId(UUID.randomUUID().toString());
                amounts.add(newAmount);
            }
            fiscalEvent.setAmountDetails(amounts);

            AuditDetails auditDetails = null;
            if (fiscalEvent.getAuditDetails() == null) {
                auditDetails = fiscalEventUtil.enrichAuditDetails(requestHeader.getUserInfo().getUuid(), fiscalEvent.getAuditDetails(), true);
            } else {
                auditDetails = fiscalEventUtil.enrichAuditDetails(requestHeader.getUserInfo().getUuid(), fiscalEvent.getAuditDetails(), false);
            }

            //set the audit details
            fiscalEvent.setAuditDetails(auditDetails);
            fiscalEvent.setIngestionTime(System.currentTimeMillis());
        }
    }
}
