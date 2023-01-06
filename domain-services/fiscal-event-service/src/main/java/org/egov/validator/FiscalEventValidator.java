package org.egov.validator;


import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;
import org.egov.common.contract.request.RequestHeader;
import org.egov.config.FiscalEventConfiguration;
import org.egov.repository.FiscalEventRepository;
import org.egov.tracer.model.CustomException;
import org.egov.util.*;
import org.egov.web.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.*;

@Component
@Slf4j
public class FiscalEventValidator {

    @Autowired
    private CoaUtil coaUtil;

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

            //TODO - remove, if not needed
            if (StringUtils.isNotBlank(fiscalEventConfiguration.getFiscalEventPushReqMaxSize())) {
                int size = Integer.parseInt(fiscalEventConfiguration.getFiscalEventPushReqMaxSize());
                if (fiscalEventRequest.getFiscalEvent().size() > size) {
                    throw new CustomException("PUSH_DATA_SIZE", "Fiscal event push data size should be at most : " + size);
                }
            }

            for (FiscalEvent fiscalEvent : fiscalEventRequest.getFiscalEvent()) {
                boolean isMissing = false;
                if (StringUtils.isBlank(fiscalEvent.getReferenceId())) {
                    errorMap.put(MasterDataConstants.REFERENCE_ID, "Reference id is missing for event type  : " + (fiscalEvent.getEventType() != null ? fiscalEvent.getEventType().name() : null));
                    isMissing = true;
                }
                if (fiscalEvent.getEventType() == null
                        || !EnumUtils.isValidEnum(FiscalEvent.EventTypeEnum.class, fiscalEvent.getEventType().name())) {

                    errorMap.put(MasterDataConstants.EVENT_TYPE, "Fiscal event type is missing");
                    isMissing = true;
                }
                if (isMissing) {
                    break;
                }
            }
            validateReqHeader(fiscalEventRequest.getRequestHeader());
            validateTenantId(fiscalEventRequest, errorMap);
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
        if (fiscalEventRequest.getFiscalEvent() != null && !fiscalEventRequest.getFiscalEvent().isEmpty()
                && StringUtils.isNotBlank(fiscalEventRequest.getFiscalEvent().get(0).getTenantId())) {

            List<FiscalEvent> fiscalEvents = fiscalEventRequest.getFiscalEvent();
            Set<String> parentEventIds = new HashSet<>();
            String tenantId = fiscalEventRequest.getFiscalEvent().get(0).getTenantId();
            for (FiscalEvent fiscalEvent : fiscalEvents) {
                if (StringUtils.isNotBlank(fiscalEvent.getLinkedEventId())) {
                    parentEventIds.add(fiscalEvent.getLinkedEventId());
                }
            }
            if (!parentEventIds.isEmpty() && StringUtils.isNotBlank(tenantId)) {
                Criteria criteria = new Criteria();
                List<String> ids = new ArrayList<>(parentEventIds);
                criteria.setTenantId(tenantId);
                criteria.setIds(ids);

                List<Object> fiscalEventList = eventRepository.searchFiscalEvent(criteria);
                if (fiscalEventList == null || fiscalEventList.isEmpty()) {
                    errorMap.put(MasterDataConstants.PARENT_EVENT_ID, "Linked event id(s) : " + parentEventIds + " doesn't exist in the system.");
                } else if (!fiscalEventList.isEmpty() && (fiscalEventList.size() != parentEventIds.size())) {
                    List<FiscalEvent> dbFiscalEvents = mapperUtil.mapDereferencedFiscalEventToFiscalEvent(fiscalEventList);
                    Set<String> missingParentEventIds = new HashSet<>();
                    for (String parentEventId : parentEventIds) {
                        boolean isExist = false;
                        for (FiscalEvent fiscalEvent : dbFiscalEvents) {
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
                        errorMap.put(MasterDataConstants.PARENT_EVENT_ID, "Linked event id(s) : " + missingParentEventIds + " doesn't exist in the system.");
                    }
                }
            }
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
        if (fiscalEventRequest.getFiscalEvent() != null && !fiscalEventRequest.getFiscalEvent().isEmpty()
                && StringUtils.isNotBlank(fiscalEventRequest.getFiscalEvent().get(0).getTenantId())) {

            String tenantId = fiscalEventRequest.getFiscalEvent().get(0).getTenantId();
            RequestHeader requestHeader = fiscalEventRequest.getRequestHeader();

            List<Amount> allAmountDetails = new ArrayList<>();
            for (FiscalEvent fiscalEvent : fiscalEventRequest.getFiscalEvent()) {
                List<Amount> amountDetails = fiscalEvent.getAmountDetails();
                if (amountDetails == null || amountDetails.isEmpty()) {
                    errorMap.put("AMOUNT_DETAILS", "Amount details are missing in the request");
                    return;
                }
                allAmountDetails.addAll(amountDetails);
            }

            Set<String> coaCodes = new HashSet<>();
            for (Amount amount : allAmountDetails) {
                if (amount.getAmount() == null /*|| amount.getAmount().compareTo(BigDecimal.ZERO)==0*/)
                    errorMap.put("AMOUNT", "Amount is missing in request for coaCode : " + amount.getCoaCode());
                if (StringUtils.isNotBlank(amount.getCoaCode()))
                    coaCodes.add(amount.getCoaCode());
                else {
                    errorMap.put("COA_CODE", "COA Code is missing in the request data");
                    throw new CustomException(errorMap);
                }
            }

            JsonNode jsonNode = coaUtil.fetchCoaDetailsByCoaCodes(requestHeader, coaCodes, tenantId);

            List<String> errorCoaCodes = new ArrayList<>();
            for (String coaCode : coaCodes) {
                boolean isPresent = false;

                if (jsonNode != null) {
                    for (JsonNode chartOfAccountJN : jsonNode) {
                        String coaCd = chartOfAccountJN.get("coaCode").asText();
                        if (StringUtils.isNotBlank(coaCd) && coaCode.equals(coaCd.trim())) {
                            isPresent = true;
                            break;
                        }
                    }
                }

                if (!isPresent) {
                    errorCoaCodes.add(coaCode);
                }
            }

            if (!errorCoaCodes.isEmpty()) {
                errorMap.put("COA_CODE_INVALID", "This chart of account Code : " + errorCoaCodes + " is invalid " +
                        "(or) Combination of tenant id with this chart of account code doesn't exist");
                return;
            }

            fiscalEventUtil.enrichCoaDetails(fiscalEventRequest, jsonNode);
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
            isValidTenant = tenantUtil.validateTenant(new ArrayList<>(tenantIds), fiscalEventRequest.getRequestHeader());
        }
        if (!isValidTenant) {
            errorMap.put(MasterDataConstants.TENANT_ID, "Tenant id doesn't exist in the system");
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
            List<String> tenantIds = new ArrayList<>();
            tenantIds.add(criteria.getTenantId());
            isValidTenant = tenantUtil.validateTenant(tenantIds, requestHeader);
        }
        if (!isValidTenant)
            errorMap.put(MasterDataConstants.TENANT_ID, "Tenant id doesn't exist in the system");

        //validation : event type
        if (StringUtils.isBlank(criteria.getEventType()))
            errorMap.put(MasterDataConstants.EVENT_TYPE, "Event type is missing in request");

        //validation :  fromEventTime and toEventTime
        if (criteria.getFromEventTime() != null && criteria.getToEventTime() == null) {
            errorMap.put(MasterDataConstants.TO_EVENT_TIME, "To(End) event time is missing for given From(start) event time in request.");
        }
        if (criteria.getToEventTime() != null && criteria.getFromEventTime() == null) {
            errorMap.put(MasterDataConstants.FROM_EVENT_TIME, "From(start) event time is missing for given To(End) event time in request.");
        }
        if (!errorMap.isEmpty()) {
            throw new CustomException(errorMap);
        }
    }

    public void validateFiscalEventPlainSearch(FiscalEventPlainSearchRequest fiscalEventGetRequest) {
        RequestHeader requestHeader = fiscalEventGetRequest.getRequestHeader();
        PlainsearchCriteria criteria = fiscalEventGetRequest.getCriteria();

        validateReqHeader(requestHeader);

        if (criteria == null)
            throw new CustomException(MasterDataConstants.SEARCH_CRITERIA, "Search criteria is missing.");

        Map<String, String> errorMap = new HashMap<>();

        //validation : tenant id
        if (StringUtils.isBlank(criteria.getTenantId()))
            errorMap.put(MasterDataConstants.TENANT_ID, "Tenant id is missing in request");
        boolean isValidTenant = false;
        if (StringUtils.isNotBlank(criteria.getTenantId())) {
            List<String> tenantIds = new ArrayList<>();
            tenantIds.add(criteria.getTenantId());
            isValidTenant = tenantUtil.validateTenant(tenantIds, requestHeader);
        }
        if (!isValidTenant)
            errorMap.put(MasterDataConstants.TENANT_ID, "Tenant id doesn't exist in the system");


        if (ObjectUtils.isEmpty(criteria.getOffset()) || ObjectUtils.isEmpty(criteria.getLimit())) {
            errorMap.put("IFIX_PLAINSEARCH_ERR", "Feeding offset and limit are mandatory in plainsearch request.");
        }
        if (!errorMap.isEmpty()) {
            throw new CustomException(errorMap);
        }


    }
}
