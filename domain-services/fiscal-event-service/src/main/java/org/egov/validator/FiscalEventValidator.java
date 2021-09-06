package org.egov.validator;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;
import org.egov.common.contract.request.RequestHeader;
import org.egov.repository.FiscalEventRepository;
import org.egov.tracer.model.CustomException;
import org.egov.util.CoaUtil;
import org.egov.util.MasterDataConstants;
import org.egov.util.ProjectUtil;
import org.egov.util.TenantUtil;
import org.egov.web.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@Slf4j
public class FiscalEventValidator {


    @Autowired
    private CoaUtil coaUtil;

    @Autowired
    TenantUtil tenantUtil;

    @Autowired
    ProjectUtil projectUtil;

    @Autowired
    private FiscalEventRepository eventRepository;

    /**
     * Validate the fiscal Event request
     *
     * @param fiscalEventRequest
     */
    public void validateFiscalEventPushPost(FiscalEventRequest fiscalEventRequest) {
        if (fiscalEventRequest != null && fiscalEventRequest.getRequestHeader() != null
                && fiscalEventRequest.getFiscalEvent() != null) {
            Map<String, String> errorMap = new HashMap<>();

            if (StringUtils.isBlank(fiscalEventRequest.getFiscalEvent().getReferenceId())) {
                errorMap.put(MasterDataConstants.REFERENCE_ID, "Reference id is missing");
            }

            if (fiscalEventRequest.getFiscalEvent().getEventType() == null
                    || !EnumUtils.isValidEnum(FiscalEvent.EventTypeEnum.class, fiscalEventRequest.getFiscalEvent()
                    .getEventType().name())) {

                errorMap.put(MasterDataConstants.EVENT_TYPE, "Fical event type is missing");
            }

            validateReqHeader(fiscalEventRequest.getRequestHeader());
            validateTenantId(fiscalEventRequest, errorMap);
            validateProjectId(fiscalEventRequest, errorMap);
            validateFiscalEventAmountDetails(fiscalEventRequest, errorMap);
            validateParentEventId(fiscalEventRequest, errorMap);

            if (!errorMap.isEmpty()) {
                throw new CustomException(errorMap);
            }

        } else {
            throw new CustomException(MasterDataConstants.FISCAL_EVENT, "Fiscal event request data is not valid");
        }
    }

    private void validateParentEventId(FiscalEventRequest fiscalEventRequest, Map<String, String> errorMap) {
        if (StringUtils.isNotBlank(fiscalEventRequest.getFiscalEvent().getParentEventId())
                && StringUtils.isNotBlank(fiscalEventRequest.getFiscalEvent().getTenantId())) {
            Criteria criteria = new Criteria();

            criteria.setTenantId(fiscalEventRequest.getFiscalEvent().getTenantId());
            criteria.setIds(Collections.singletonList(fiscalEventRequest.getFiscalEvent().getParentEventId()));

            List<Object> fiscalEventList = eventRepository.searchFiscalEvent(criteria);
            if (fiscalEventList == null || fiscalEventList.isEmpty())
                errorMap.put(MasterDataConstants.PARENT_EVENT_ID, "Parent event id doesn't exist in the system.");

        }
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
     * Validate the fiscal event request - amount details(line items) attributes
     *
     * @param fiscalEventRequest
     * @param errorMap
     */
    private void validateFiscalEventAmountDetails(FiscalEventRequest fiscalEventRequest, Map<String, String> errorMap) {
        RequestHeader requestHeader = fiscalEventRequest.getRequestHeader();
        List<Amount> amountDetails = fiscalEventRequest.getFiscalEvent().getAmountDetails();

        if (amountDetails == null || amountDetails.isEmpty())
            errorMap.put("AMOUNT_DETAILS", "Amount details are missing");

        List<String> coaIds = new ArrayList<>();
        int amountDetailSize = amountDetails.size();
        int index = 1;
        for (Amount amount : amountDetails) {
            if (amount.getAmount() == null /*|| amount.getAmount().compareTo(BigDecimal.ZERO)==0*/)
                errorMap.put("AMOUNT-" + index++, "Amount is missing for coaId : " + amount.getCoaId());
            if (StringUtils.isNotBlank(amount.getCoaId()))
                coaIds.add(amount.getCoaId());
        }

        List<String> responseCoaIds = coaUtil.getCOAIdsFromCOAService(requestHeader, fiscalEventRequest.getFiscalEvent());

        List<String> errorCoaIds = new ArrayList<>();
        for (String coaId : coaIds) {
            if (!responseCoaIds.contains(coaId))
                errorCoaIds.add(coaId);
        }

        if (!errorCoaIds.isEmpty()) {
            errorMap.put("COA_ID_INVALID", "This chart of account id : " + errorCoaIds.toString() + " is invalid " +
                    "(or) Combination of tenant id with this chart of account id doesn't exist");
        }
    }

    /**
     * @param fiscalEventRequest
     */
    public void validateTenantId(FiscalEventRequest fiscalEventRequest, Map<String, String> errorMap) {
        if (StringUtils.isBlank(fiscalEventRequest.getFiscalEvent().getTenantId())) {
            errorMap.put(MasterDataConstants.TENANT_ID, "Tenant id is missing in request");
        }
        boolean isValidTenant = false;
        if (fiscalEventRequest.getFiscalEvent() != null && StringUtils.isNotBlank(fiscalEventRequest.getFiscalEvent().getTenantId())) {
            isValidTenant = tenantUtil.validateTenant(fiscalEventRequest.getFiscalEvent().getTenantId(), fiscalEventRequest.getRequestHeader());
        }
        if (!isValidTenant) {
            errorMap.put(MasterDataConstants.TENANT_ID, "Tenant id doesn't exist in the system");
        }
    }

    /**
     * @param fiscalEventRequest
     */
    public void validateProjectId(FiscalEventRequest fiscalEventRequest, Map<String, String> errorMap) {
        boolean isValidProject = projectUtil.validateProjectId(fiscalEventRequest);

        if (!isValidProject) {
            errorMap.put(MasterDataConstants.PROJECT_ID, "Project id doesn't exist in the system");
        }
    }

    public void validateFiscalEventSearchPost(FiscalEventGetRequest fiscalEventGetRequest) {
        RequestHeader requestHeader = fiscalEventGetRequest.getRequestHeader();
        Criteria criteria = fiscalEventGetRequest.getCriteria();

        validateReqHeader(requestHeader);
        if (criteria == null)
            throw new CustomException(MasterDataConstants.SEARCH_CRITERIA, "Search criteria is missing.");

        Map<String, String> errorMap = new HashMap<>();

        //validation : tenant id
        if (StringUtils.isBlank(criteria.getTenantId()))
            errorMap.put(MasterDataConstants.TENANT_ID, "Tenant id is missing in request");
        boolean isValidTenant = false;
        if (StringUtils.isNotBlank(criteria.getTenantId())) {
            isValidTenant = tenantUtil.validateTenant(criteria.getTenantId(), requestHeader);
        }
        if (!isValidTenant)
            errorMap.put(MasterDataConstants.TENANT_ID, "Tenant id doesn't exist in the system");

        //validation : event type
        if (StringUtils.isBlank(criteria.getEventType()))
            errorMap.put(MasterDataConstants.EVENT_TYPE, "Event type is missing in request");

        //validation :  fromEventTime and toEventTime
        if (criteria.getFromEventTime() != null) {
            if (criteria.getToEventTime() == null) {
                errorMap.put(MasterDataConstants.TO_EVENT_TIME, "To(End) event time is missing for given From(start) event time in request.");
            }
        }
        if (criteria.getToEventTime() != null) {
            if (criteria.getFromEventTime() == null) {
                errorMap.put(MasterDataConstants.FROM_EVENT_TIME, "From(start) event time is missing for given To(End) event time in request.");
            }
        }
        if (!errorMap.isEmpty()) {
            throw new CustomException(errorMap);
        }
    }
}
