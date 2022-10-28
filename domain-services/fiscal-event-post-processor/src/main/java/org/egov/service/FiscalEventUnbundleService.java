package org.egov.service;


import lombok.extern.slf4j.Slf4j;
import org.egov.models.AmountDetailsDeReferenced;
import org.egov.models.FiscalEventDeReferenced;
import org.egov.models.FiscalEventLineItemUnbundled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class FiscalEventUnbundleService {

    /**
     * Unbundled the dereference fiscal event
     *
     * @param fiscalEventDeReferenced
     * @return
     */
    public List<FiscalEventLineItemUnbundled> unbundle(FiscalEventDeReferenced fiscalEventDeReferenced) {
        List<FiscalEventLineItemUnbundled> fiscalEventLineItemUnbundledList = new ArrayList<>();
        List<AmountDetailsDeReferenced> amountDetails = fiscalEventDeReferenced.getAmountDetails();
        if (amountDetails != null && !amountDetails.isEmpty()) {
            for (AmountDetailsDeReferenced amountDetailsDeReferenced : amountDetails) {
                fiscalEventLineItemUnbundledList.add(getUnbundleFiscalEventFromDereferenceEvent(
                        amountDetailsDeReferenced, fiscalEventDeReferenced));
            }
        }

        return fiscalEventLineItemUnbundledList;
    }

    /**
     * @param amountDetailsDeReferenced
     * @param fiscalEventDeReferenced
     * @return
     */
    private FiscalEventLineItemUnbundled getUnbundleFiscalEventFromDereferenceEvent(AmountDetailsDeReferenced amountDetailsDeReferenced,
                                                                                    FiscalEventDeReferenced fiscalEventDeReferenced) {
        FiscalEventLineItemUnbundled fiscalEventLineItemUnbundled = new FiscalEventLineItemUnbundled();

        fiscalEventLineItemUnbundled.setId(amountDetailsDeReferenced.getId());
        fiscalEventLineItemUnbundled.setEventId(fiscalEventDeReferenced.getId());

        fiscalEventLineItemUnbundled.setAmount(amountDetailsDeReferenced.getAmount());
        fiscalEventLineItemUnbundled.setCoa(amountDetailsDeReferenced.getCoa());
        fiscalEventLineItemUnbundled.setFromBillingPeriod(amountDetailsDeReferenced.getFromBillingPeriod());
        fiscalEventLineItemUnbundled.setToBillingPeriod(amountDetailsDeReferenced.getToBillingPeriod());

        fiscalEventLineItemUnbundled.setIngestionTime(fiscalEventDeReferenced.getIngestionTime());
        fiscalEventLineItemUnbundled.setEventTime(fiscalEventDeReferenced.getEventTime());
        fiscalEventLineItemUnbundled.setEventType(fiscalEventDeReferenced.getEventType());
        fiscalEventLineItemUnbundled.setGovernment(fiscalEventDeReferenced.getGovernment());
        fiscalEventLineItemUnbundled.setLinkedEventId(fiscalEventDeReferenced.getLinkedEventId());
        fiscalEventLineItemUnbundled.setLinkedReferenceId(fiscalEventDeReferenced.getLinkedReferenceId());
        fiscalEventLineItemUnbundled.setReferenceId(fiscalEventDeReferenced.getReferenceId());
        fiscalEventLineItemUnbundled.setTenantId(fiscalEventDeReferenced.getTenantId());
        fiscalEventLineItemUnbundled.setVersion(fiscalEventDeReferenced.getVersion());

        fiscalEventLineItemUnbundled.setAuditDetails(fiscalEventDeReferenced.getAuditDetails());
        fiscalEventLineItemUnbundled.setAttributes(fiscalEventDeReferenced.getAttributes());

        return fiscalEventLineItemUnbundled;
    }
}
