package org.egov.validator;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.egov.common.contract.request.RequestHeader;
import org.egov.tracer.model.CustomException;
import org.egov.util.MasterDataConstants;
import org.egov.util.ProjectUtil;
import org.egov.util.TenantUtil;
import org.egov.web.models.FiscalEvent;
import org.egov.web.models.FiscalEventRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class FiscalEventValidator {

    @Autowired
    TenantUtil tenantUtil;

    @Autowired
    ProjectUtil projectUtil;

    /**
     * Validate the fiscal Event request
     * @param fiscalEventRequest
     */
    public void validateFiscalEventPushPost(FiscalEventRequest fiscalEventRequest) {
        validateTenantId(fiscalEventRequest);
        validateProjectId(fiscalEventRequest);
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

    /**
     * @param fiscalEventRequest
     */
    public void validateTenantId(FiscalEventRequest fiscalEventRequest) {
        boolean isValidTenant = tenantUtil.validateTenant(fiscalEventRequest);

        if (!isValidTenant) {

            throw new CustomException(MasterDataConstants.TENANT_ID, "Tenant id doesn't exist in the system");
        }
    }

    /**
     * @param fiscalEventRequest
     */
    public void validateProjectId(FiscalEventRequest fiscalEventRequest) {
        boolean isValidProject = projectUtil.validateProjectId(fiscalEventRequest);

        if (!isValidProject) {
            throw new CustomException(MasterDataConstants.PROJECT_ID, "Project id doesn't exist in the system");
        }
    }

}
