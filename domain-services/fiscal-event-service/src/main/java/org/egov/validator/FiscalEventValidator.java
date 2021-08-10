package org.egov.validator;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.egov.common.contract.request.RequestHeader;
import org.egov.tracer.model.CustomException;
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
        validateReqHeader(fiscalEventRequest.getRequestHeader());
        validateFiscalEventExceptAmountDetails(fiscalEventRequest.getFiscalEvent());
        validateFiscalEventAmountDetails(fiscalEventRequest.getFiscalEvent());
    }

    /**
     * Validate the request header
     * @param requestHeader
     */
    private void validateReqHeader(RequestHeader requestHeader) {
        if(requestHeader == null)
            throw new CustomException("REQUEST_HEADER","Request header is missing");
        if(requestHeader.getUserInfo() == null)
            throw new CustomException("USER_INFO","User info is missing in request header");
        if(StringUtils.isBlank(requestHeader.getUserInfo().getUuid()))
            throw new CustomException("USER_INFO","User info is missing in request header");
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
