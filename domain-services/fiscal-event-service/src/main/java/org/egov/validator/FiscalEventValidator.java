package org.egov.validator;


import lombok.extern.slf4j.Slf4j;
import org.egov.web.models.FiscalEvent;
import org.egov.web.models.FiscalEventRequest;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class FiscalEventValidator {

    /**
     * Validate the fiscal Event request
     * @param fiscalEventRequest
     */
    public void validateFiscalEventPushPost(FiscalEventRequest fiscalEventRequest) {
        validateReqHeader(fiscalEventRequest);
        validateFiscalEventExceptAmountDetails(fiscalEventRequest.getFiscalEvent());
        validateFiscalEventAmountDetails(fiscalEventRequest.getFiscalEvent());
    }

    /**
     * Validate the request header
     * @param fiscalEventRequest
     */
    private void validateReqHeader(FiscalEventRequest fiscalEventRequest) {
    }

    /**
     * Validate the fiscal event request except amount details(line items)
     * @param fiscalEventRequest
     */
    private void validateFiscalEventExceptAmountDetails(FiscalEvent fiscalEvent) {
    }


    /**
     * Validate the fiscal event request - amount details(line items) attributes
     * @param fiscalEventRequest
     */
    private void validateFiscalEventAmountDetails(FiscalEvent fiscalEvent) {
    }

}
