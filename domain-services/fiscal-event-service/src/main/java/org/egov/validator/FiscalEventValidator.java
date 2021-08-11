package org.egov.validator;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.egov.common.contract.request.RequestHeader;
import org.egov.tracer.model.CustomException;
import org.egov.util.CoaUtil;
import org.egov.web.models.Amount;
import org.egov.web.models.FiscalEvent;
import org.egov.web.models.FiscalEventRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class FiscalEventValidator {
    
    
    @Autowired
    private CoaUtil coaUtil;

    /**
     * Validate the fiscal Event request
     *
     * @param fiscalEventRequest
     */
    public void validateFiscalEventPushPost(FiscalEventRequest fiscalEventRequest) {
        validateReqHeader(fiscalEventRequest.getRequestHeader());
        validateFiscalEventExceptAmountDetails(fiscalEventRequest.getFiscalEvent());
        validateFiscalEventAmountDetails(fiscalEventRequest);
    }

    /**
     * Validate the request header
     *
     * @param requestHeader
     */
    private void validateReqHeader(RequestHeader requestHeader) {
        if (requestHeader == null)
            throw new CustomException("REQUEST_HEADER", "Request header is missing");
        if (requestHeader.getUserInfo() == null)
            throw new CustomException("USER_INFO", "User info is missing in request header");
        if (StringUtils.isBlank(requestHeader.getUserInfo().getUuid()))
            throw new CustomException("USER_INFO", "User info is missing in request header");
    }

    /**
     * Validate the fiscal event request except amount details(line items)
     *
     * @param fiscalEventRequest
     */
    private void validateFiscalEventExceptAmountDetails(FiscalEvent fiscalEvent) {
    }


    /**
     * Validate the fiscal event request - amount details(line items) attributes
     *
     * @param fiscalEventRequest
     */
    private void validateFiscalEventAmountDetails(FiscalEventRequest fiscalEventRequest) {
        RequestHeader requestHeader = fiscalEventRequest.getRequestHeader();
        List<Amount> amountDetails = fiscalEventRequest.getFiscalEvent().getAmountDetails();

        if (amountDetails == null || amountDetails.isEmpty())
            throw new CustomException("AMOUNT_DETAILS", "Amount details are missing");

        List<String> coaIds = new ArrayList<>();
        int amountDetailSize = amountDetails.size();
        for (Amount amount : amountDetails) {
            if (amount.getAmount() == null /*|| amount.getAmount().compareTo(BigDecimal.ZERO)==0*/)
                throw new CustomException("AMOUNT", "Amount is missing for coaId : " + amount.getCoaId());
            if (StringUtils.isNotBlank(amount.getCoaId()))
                coaIds.add(amount.getCoaId());
        }

        if (coaIds.size() != amountDetailSize)
            throw new CustomException("COA_ID", "Chart of account id(s) are missing in one of the amount details");

        List<String> responseCoaIds = coaUtil.getCOAIdsFromCOAService(requestHeader, fiscalEventRequest.getFiscalEvent());

        for (String coaId : coaIds) {
            if (!responseCoaIds.contains(coaId))
                throw new CustomException("COA_ID_INVALID", "This chart of account id : " + coaId + " is invalid.");
        }
    }
}
