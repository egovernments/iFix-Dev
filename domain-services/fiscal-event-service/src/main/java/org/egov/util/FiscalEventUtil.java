package org.egov.util;


import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.egov.common.contract.AuditDetails;
import org.egov.web.models.Amount;
import org.egov.web.models.FiscalEvent;
import org.egov.web.models.FiscalEventRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class FiscalEventUtil {

    /**
     * Method to return auditDetails of fiscal event request
     *
     * @param by
     * @param isCreate
     * @return AuditDetails
     */
    public AuditDetails enrichAuditDetails(String by, AuditDetails auditDetails, Boolean isCreate) {
        Long time = System.currentTimeMillis();
        if (isCreate)
            return AuditDetails.builder().createdBy(by).lastModifiedBy(by).createdTime(time).lastModifiedTime(time).build();
        else
            return AuditDetails.builder().createdBy(auditDetails.getCreatedBy()).lastModifiedBy(by)
                    .createdTime(auditDetails.getCreatedTime()).lastModifiedTime(time).build();
    }

    /**
     * @param fiscalEventRequest
     * @param chartOfAccountsJN
     */
    public void enrichCoaDetails(FiscalEventRequest fiscalEventRequest, JsonNode chartOfAccountsJN) {
        List<FiscalEvent> fiscalEvents = new ArrayList<>();

        for (FiscalEvent fiscalEvent : fiscalEventRequest.getFiscalEvent()) {
            List<Amount> amountDetails = fiscalEvent.getAmountDetails();

            FiscalEvent newFiscalEvent = new FiscalEvent();
            BeanUtils.copyProperties(fiscalEvent, newFiscalEvent);
            List<Amount> amounts = new ArrayList<>();

            for (Amount amount : amountDetails) {
                String reqCoaCode = amount.getCoaCode();
                Amount newAmount = new Amount();
                BeanUtils.copyProperties(amount, newAmount);

                for (JsonNode chartOfAccountJN : chartOfAccountsJN) {
                    String coaCode = chartOfAccountJN.get("coaCode").asText();
                    String coaId = chartOfAccountJN.get("id").asText();
                    if (StringUtils.isNotBlank(reqCoaCode) && StringUtils.isNotBlank(coaCode) && coaCode.equals(reqCoaCode)) {
                        newAmount.setCoaId(coaId);
                        break;
                    }
                }

                amounts.add(newAmount);
            }

            newFiscalEvent.setAmountDetails(amounts);
            fiscalEvents.add(newFiscalEvent);
        }
        fiscalEventRequest.setFiscalEvent(fiscalEvents);
    }
}
