package org.egov.validator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;
import org.egov.common.contract.request.RequestHeader;
import org.egov.config.FiscalEventConfiguration;
import org.egov.repository.FiscalEventRepository;
import org.egov.tracer.model.CustomException;
import org.egov.util.FiscalEventMapperUtil;
import org.egov.util.FiscalEventUtil;
import org.egov.util.MasterDataConstants;
import org.egov.util.TenantUtil;
import org.egov.web.models.Amount;
import org.egov.web.models.Criteria;
import org.egov.web.models.FiscalEvent;
import org.egov.web.models.FiscalEventGetRequest;
import org.egov.web.models.FiscalEventRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class FiscalEventValidator {

    @Autowired
    TenantUtil tenantUtil;

    @Autowired
    private FiscalEventRepository eventRepository;

    @Autowired
    private FiscalEventConfiguration fiscalEventConfiguration;

    @Autowired
    private FiscalEventMapperUtil mapperUtil;

    @Autowired
    private FiscalEventUtil fiscalEventUtil;

    /**
     * Validate the fiscal Event request
     *
     * @param fiscalEventRequest
     */
    public void validateFiscalEventPushPost(FiscalEventRequest fiscalEventRequest) {
        if (fiscalEventRequest != null && fiscalEventRequest.getRequestHeader() != null
                && fiscalEventRequest.getFiscalEvent() != null && !fiscalEventRequest.getFiscalEvent().isEmpty()) {
            Map<String, String> errorMap = new HashMap<>();

            validateBatchSize(fiscalEventRequest);

            Set<String> parentEventIds = new HashSet<>();
            String tenantId = fiscalEventRequest.getFiscalEvent().get(0).getTenantId();

            //Set<String> coaCodes = new HashSet<>();
            for (FiscalEvent fiscalEvent : fiscalEventRequest.getFiscalEvent()) {
                boolean isMissing = false;
                validateFiscalEventRequiredFields(fiscalEvent, errorMap, isMissing);
                validateFiscalEventAmountDetails(fiscalEvent, errorMap);
                if (isMissing) {
                    break;
                }

                if (StringUtils.isNotBlank(fiscalEvent.getLinkedEventId()))
                    parentEventIds.add(fiscalEvent.getLinkedEventId());
            }

            // validateTenantId(fiscalEventRequest, errorMap);

            validateParentEventId(parentEventIds, tenantId, errorMap);

            if (!errorMap.isEmpty()) {
                throw new CustomException(errorMap);
            }

        } else {
            throw new CustomException(MasterDataConstants.FISCAL_EVENT, "Fiscal event request data is not valid");
        }
    }

    private void validateFiscalEventRequiredFields(FiscalEvent fiscalEvent, Map<String, String> errorMap,
                                                   boolean isMissing) {
        if (StringUtils.isBlank(fiscalEvent.getReferenceId())) {
            errorMap.put(MasterDataConstants.REFERENCE_ID, "Reference id is missing for event type  : "
                    + (fiscalEvent.getEventType() != null ? fiscalEvent.getEventType().name() : null));
            isMissing = true;
        }
        if (fiscalEvent.getEventType() == null
                || !EnumUtils.isValidEnum(FiscalEvent.EventTypeEnum.class, fiscalEvent.getEventType().name())) {

            errorMap.put(MasterDataConstants.EVENT_TYPE, "Fiscal event type is missing");
            isMissing = true;
        }
        // TODO: ADD tenant id not null check

    }

    private void validateBatchSize(FiscalEventRequest fiscalEventRequest) {
        if (StringUtils.isNotBlank(fiscalEventConfiguration.getFiscalEventPushReqMaxSize())) {
            int size = Integer.parseInt(fiscalEventConfiguration.getFiscalEventPushReqMaxSize());
            if (fiscalEventRequest.getFiscalEvent().size() > size) {
                throw new CustomException("PUSH_DATA_SIZE", "Fiscal event push data size should be at most : " + size);
            }
        }
    }

    private void validateFiscalEventAmountDetails(FiscalEvent fiscalEvent, Map<String, String> errorMap) {
        List<Amount> amountDetails = fiscalEvent.getAmountDetails();
        if (amountDetails == null || amountDetails.isEmpty()) {
            errorMap.put("AMOUNT_DETAILS", "Amount details are missing in the request");
            return;
        }

        // Set<String> coaCodes = new HashSet<>();
        for (Amount amount : amountDetails) {
            if (amount.getAmount() == null /* || amount.getAmount().compareTo(BigDecimal.ZERO)==0 */)
                errorMap.put("AMOUNT", "Amount is missing in request for coaCode : " + amount.getCoaCode());
            if (StringUtils.isBlank(amount.getCoaCode())) {
                errorMap.put("COA_CODE", "COA Code is missing in the request data");
                throw new CustomException(errorMap);
            }
        }

    }

    private void validateParentEventId(Set<String> parentEventIds, String tenantId, Map<String, String> errorMap) {

        if (!parentEventIds.isEmpty() && StringUtils.isNotBlank(tenantId)) {
            Criteria criteria = new Criteria();
            List<String> ids = new ArrayList<>(parentEventIds);
            criteria.setTenantId(tenantId);
            criteria.setIds(ids);

            List<FiscalEvent> fiscalEventList = eventRepository.searchFiscalEvent(criteria);
            if (fiscalEventList == null || fiscalEventList.isEmpty()) {
                errorMap.put(MasterDataConstants.PARENT_EVENT_ID,
                        "Linked event id(s) : " + parentEventIds + " doesn't exist in the system.");
            } else if (!fiscalEventList.isEmpty() && (fiscalEventList.size() != parentEventIds.size())) {
                /*
                 * List<FiscalEvent> dbFiscalEvents = mapperUtil
                 * .mapDereferencedFiscalEventToFiscalEvent(fiscalEventList);
                 */
                Set<String> missingParentEventIds = new HashSet<>();
                for (String parentEventId : parentEventIds) {
                    boolean isExist = false;
                    for (FiscalEvent fiscalEvent : fiscalEventList) {
                        if (fiscalEvent != null && StringUtils.isNotBlank(fiscalEvent.getLinkedEventId())
                                && fiscalEvent.getLinkedEventId().equals(parentEventId)) {
                            isExist = true;
                            break;
                        }
                    }
                    if (!isExist) {
                        missingParentEventIds.add(parentEventId);
                    }
                }
                if (!missingParentEventIds.isEmpty()) {
                    errorMap.put(MasterDataConstants.PARENT_EVENT_ID,
                            "Linked event id(s) : " + missingParentEventIds + " doesn't exist in the system.");
                }
            }
        }

    }

    /**
     * @param fiscalEventRequest
     * @param errorMap
     */
    public void validateTenantId(FiscalEventRequest fiscalEventRequest, Map<String, String> errorMap) {
        List<FiscalEvent> fiscalEvents = fiscalEventRequest.getFiscalEvent();
        Set<String> tenantIds = new HashSet<>();
        for (FiscalEvent fiscalEvent : fiscalEvents) {
            if (StringUtils.isBlank(fiscalEvent.getTenantId())) {
                errorMap.put(MasterDataConstants.TENANT_ID, "Tenant id is missing in request");
                return;
            }
            tenantIds.add(fiscalEvent.getTenantId());
        }

        if (tenantIds.size() > 1) {
            errorMap.put(MasterDataConstants.TENANT_ID, "Tenant id should be same in the request");
            return;
        }
        boolean isValidTenant = false;
        if (!tenantIds.isEmpty()) {
            isValidTenant = tenantUtil.validateTenant(new ArrayList<>(tenantIds),
                    fiscalEventRequest.getRequestHeader());
        }
        if (!isValidTenant) {
            errorMap.put(MasterDataConstants.TENANT_ID, "Tenant id doesn't exist in the system");
        }
    }

    public void validateFiscalEventSearchPost(FiscalEventGetRequest fiscalEventGetRequest) {
        RequestHeader requestHeader = fiscalEventGetRequest.getRequestHeader();
        Criteria criteria = fiscalEventGetRequest.getCriteria();

        // Ghanshyam validateReqHeader(requestHeader);
        if (criteria == null)
            throw new CustomException(MasterDataConstants.SEARCH_CRITERIA, "Search criteria is missing.");

        Map<String, String> errorMap = new HashMap<>();

        // validation : tenant id
        if (StringUtils.isBlank(criteria.getTenantId()))
            errorMap.put(MasterDataConstants.TENANT_ID, "Tenant id is missing in request");

        // validation : event type
        /*
         * if (StringUtils.isBlank(criteria.getEventType()))
         * errorMap.put(MasterDataConstants.EVENT_TYPE,
         * "Event type is missing in request");
         */
        // validation : fromEventTime and toEventTime
        if (criteria.getFromEventTime() != null && criteria.getToEventTime() == null) {
            errorMap.put(MasterDataConstants.TO_EVENT_TIME,
                    "To(End) event time is missing for given From(start) event time in request.");
        }
        if (criteria.getToEventTime() != null && criteria.getFromEventTime() == null) {
            errorMap.put(MasterDataConstants.FROM_EVENT_TIME,
                    "From(start) event time is missing for given To(End) event time in request.");
        }
        if (!errorMap.isEmpty()) {
            throw new CustomException(errorMap);
        }
    }
}
