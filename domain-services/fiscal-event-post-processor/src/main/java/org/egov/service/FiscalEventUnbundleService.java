package org.egov.service;


import lombok.extern.slf4j.Slf4j;
import org.egov.web.models.AmountDetailsDeReferenced;
import org.egov.web.models.FiscalEventDeReferenced;
import org.egov.web.models.FiscalEventLineItemUnbundled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class FiscalEventUnbundleService {

    /**
     * Unbundled the dereference fiscal event
     * @param fiscalEventDeReferenced
     * @return
     */
    public List<FiscalEventLineItemUnbundled> unbundle(FiscalEventDeReferenced fiscalEventDeReferenced) {
        List<FiscalEventLineItemUnbundled> fiscalEventLineItemUnbundledList =  new ArrayList<>();
        List<AmountDetailsDeReferenced> amountDetails = fiscalEventDeReferenced.getAmountDetails();
        if(amountDetails != null && !amountDetails.isEmpty()){
            for(AmountDetailsDeReferenced amountDetailsDeReferenced : amountDetails){
                FiscalEventLineItemUnbundled fiscalEventLineItemUnbundled = getUnbundleFiscalEventFromDereferenceEvent(amountDetailsDeReferenced,fiscalEventDeReferenced);
                fiscalEventLineItemUnbundledList.add(fiscalEventLineItemUnbundled);
            }
        }

        return fiscalEventLineItemUnbundledList;
    }

    /**
     *
     * @param amountDetailsDeReferenced
     * @param fiscalEventDeReferenced
     * @return
     */
    private FiscalEventLineItemUnbundled getUnbundleFiscalEventFromDereferenceEvent(AmountDetailsDeReferenced amountDetailsDeReferenced,
                                                                                    FiscalEventDeReferenced fiscalEventDeReferenced) {
        FiscalEventLineItemUnbundled fiscalEventLineItemUnbundled =  new FiscalEventLineItemUnbundled();

        fiscalEventLineItemUnbundled.setId(amountDetailsDeReferenced.getId());
        fiscalEventLineItemUnbundled.setEventId(fiscalEventDeReferenced.getId());

        fiscalEventLineItemUnbundled.setAmount(amountDetailsDeReferenced.getAmount());
        fiscalEventLineItemUnbundled.setCoa(amountDetailsDeReferenced.getCoa());
        fiscalEventLineItemUnbundled.setFromBillingPeriod(amountDetailsDeReferenced.getFromBillingPeriod());
        fiscalEventLineItemUnbundled.setToBillingPeriod(amountDetailsDeReferenced.getToBillingPeriod());

        fiscalEventLineItemUnbundled.setDepartment(fiscalEventDeReferenced.getDepartment());
        fiscalEventLineItemUnbundled.setEventTime(fiscalEventDeReferenced.getEventTime());
        fiscalEventLineItemUnbundled.setEventType(fiscalEventDeReferenced.getEventType());
        fiscalEventLineItemUnbundled.setExpenditure(fiscalEventDeReferenced.getExpenditure());
        fiscalEventLineItemUnbundled.setGovernment(fiscalEventDeReferenced.getGovernment());
        fiscalEventLineItemUnbundled.setParentEventId(fiscalEventDeReferenced.getParentEventId());
        fiscalEventLineItemUnbundled.setParentReferenceId(fiscalEventDeReferenced.getParentReferenceId());
        fiscalEventLineItemUnbundled.setProject(fiscalEventDeReferenced.getProject());
        fiscalEventLineItemUnbundled.setReferenceId(fiscalEventDeReferenced.getReferenceId());
        fiscalEventLineItemUnbundled.setTenantId(fiscalEventDeReferenced.getTenantId());
        fiscalEventLineItemUnbundled.setVersion(fiscalEventDeReferenced.getVersion());
        return fiscalEventLineItemUnbundled;
    }
}
