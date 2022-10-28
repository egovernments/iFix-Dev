package org.egov.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.egov.config.PspclIfixAdapterConfiguration;
import org.egov.entity.EventPostingDetail;
import org.egov.entity.PspclBillDetail;
import org.egov.entity.PspclPaymentDetail;
import org.egov.model.*;
import org.egov.repository.EventPostingDetailRepository;
import org.egov.service.AuthTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static org.egov.util.PspclIfixAdapterConstant.*;

@Component
@Slf4j
@AllArgsConstructor
@NoArgsConstructor
public class FiscalEventUtil {

    @Autowired
    private PspclIfixAdapterConfiguration adapterConfiguration;

    @Autowired
    private PspclIfixAdapterUtil ifixAdapterUtil;

    @Autowired
    private AuthTokenService authTokenService;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private EventPostingDetailRepository eventPostingDetailRepo;


    /**
     * Create a fiscal event of event type - Receipt
     *
     * @param billAndPaymentEventDetail
     * @return
     */
    public FiscalEvent getReceiptFiscalEvent(ReconcileVO billAndPaymentEventDetail) {
        FiscalEvent paymentFiscalEvent = null;
        List<Amount> amounts = new ArrayList<>();

        if (billAndPaymentEventDetail != null && billAndPaymentEventDetail.getCurrentPspclPaymentDetail() != null) {
            PspclPaymentDetail pspclPaymentDetail = billAndPaymentEventDetail.getCurrentPspclPaymentDetail();

            //mandatory condition check
            if (StringUtils.isBlank(adapterConfiguration.getReceiptCoaCode())
                    && StringUtils.isBlank(adapterConfiguration.getTenantId())
                    && StringUtils.isBlank(pspclPaymentDetail.getTXNID())
                    && StringUtils.isBlank(pspclPaymentDetail.getAMT())) {
                return paymentFiscalEvent;
            }
            paymentFiscalEvent = new FiscalEvent();

            Amount paymentAmount = new Amount();
            if (StringUtils.isNotBlank(pspclPaymentDetail.getAMT())) {
                paymentAmount.setAmount(new BigDecimal(pspclPaymentDetail.getAMT()));
            }
//            paymentAmount.setFromBillingPeriod();//missing
//            paymentAmount.setToBillingPeriod();//missing
            paymentAmount.setCoaCode(adapterConfiguration.getReceiptCoaCode());
            amounts.add(paymentAmount);

            if (pspclPaymentDetail.getTXNDATE() != null) {
                paymentFiscalEvent.setEventTime(ifixAdapterUtil.format(TXN_DATE_FORMAT, pspclPaymentDetail.getTXNDATE()).getTime());
            }
            paymentFiscalEvent.setEventType(EVENT_TYPE_RECEIPT);
            paymentFiscalEvent.setReferenceId(pspclPaymentDetail.getTXNID());
            paymentFiscalEvent.setTenantId(adapterConfiguration.getTenantId());

            List<String> receivers = new ArrayList<>();
            receivers.add(adapterConfiguration.getFiscalEventReceiver());
            paymentFiscalEvent.setReceivers(receivers);

            ObjectNode additionalAttributes = objectMapper.createObjectNode();
            ObjectNode departmentEntity = objectMapper.createObjectNode();
            departmentEntity.put(FE_ADDITIONAL_ATTRIBUTE_DEPARTMENT_ENTITY_CODE, billAndPaymentEventDetail.getDepartmentEntityCode());
            departmentEntity.put(FE_ADDITIONAL_ATTRIBUTE_DEPARTMENT_ENTITY_NAME, billAndPaymentEventDetail.getDepartmentEntityName());

            additionalAttributes.put(FE_ADDITIONAL_ATTRIBUTE_ACCOUNT_NUMBER, pspclPaymentDetail.getACNO());
            additionalAttributes.set(FE_ADDITIONAL_ATTRIBUTE_DEPARTMENT_ENTITY, departmentEntity);

            paymentFiscalEvent.setAttributes(additionalAttributes);
            paymentFiscalEvent.setAmountDetails(amounts);

        }
        return paymentFiscalEvent;
    }

    /**
     * Create a fiscal event of event type - Demand
     *
     * @param billAndPaymentEventDetail
     * @return
     */
    public FiscalEvent getDemandFiscalEvent(ReconcileVO billAndPaymentEventDetail) {
        FiscalEvent billFiscalEvent = null;
        List<Amount> amounts = new ArrayList<>();
        if (billAndPaymentEventDetail != null && billAndPaymentEventDetail.getCurrentPspclBillDetail() != null
                && billAndPaymentEventDetail.getCurrentCalculatedBillAmt() != null
                && billAndPaymentEventDetail.getCurrentCalculatedBillAmt().compareTo(BigDecimal.ZERO) != 0) {

            PspclBillDetail pspclBillDetail = billAndPaymentEventDetail.getCurrentPspclBillDetail();
            //mandatory condition check
            if (StringUtils.isBlank(adapterConfiguration.getDemandCoaCode())
                    && StringUtils.isBlank(adapterConfiguration.getTenantId())
                    && StringUtils.isBlank(pspclBillDetail.getBILL_NO())) {
                return billFiscalEvent;
            }
            billFiscalEvent = new FiscalEvent();
            Amount billAmount = new Amount();
            billAmount.setAmount(billAndPaymentEventDetail.getCurrentCalculatedBillAmt());
            if (pspclBillDetail.getDATE_READING_PREV() != null) {
                billAmount.setFromBillingPeriod(ifixAdapterUtil.format(BILL_ISSUE_DATE_FORMAT, pspclBillDetail.getDATE_READING_PREV()).getTime());
            }
            if (pspclBillDetail.getDATE_READING_CURR() != null) {
                billAmount.setToBillingPeriod(ifixAdapterUtil.format(BILL_ISSUE_DATE_FORMAT, pspclBillDetail.getDATE_READING_CURR()).getTime());
            }
            billAmount.setCoaCode(adapterConfiguration.getDemandCoaCode());
            amounts.add(billAmount);

            billFiscalEvent.setEventTime(ifixAdapterUtil.format(BILL_ISSUE_DATE_FORMAT, pspclBillDetail.getBILL_ISSUE_DATE()).getTime());
            billFiscalEvent.setEventType(EVENT_TYPE_DEMAND);
            billFiscalEvent.setReferenceId(pspclBillDetail.getBILL_NO());
            billFiscalEvent.setTenantId(adapterConfiguration.getTenantId());

            List<String> receivers = new ArrayList<>();
            receivers.add(adapterConfiguration.getFiscalEventReceiver());
            billFiscalEvent.setReceivers(receivers);

            ObjectNode additionalAttributes = objectMapper.createObjectNode();
            ObjectNode departmentEntity = objectMapper.createObjectNode();
            departmentEntity.put(FE_ADDITIONAL_ATTRIBUTE_DEPARTMENT_ENTITY_CODE, billAndPaymentEventDetail.getDepartmentEntityCode());
            departmentEntity.put(FE_ADDITIONAL_ATTRIBUTE_DEPARTMENT_ENTITY_NAME, billAndPaymentEventDetail.getDepartmentEntityName());

            additionalAttributes.put(FE_ADDITIONAL_ATTRIBUTE_ACCOUNT_NUMBER, pspclBillDetail.getACCOUNT_NO());
            additionalAttributes.set(FE_ADDITIONAL_ATTRIBUTE_DEPARTMENT_ENTITY, departmentEntity);

            billFiscalEvent.setAttributes(additionalAttributes);
            billFiscalEvent.setAmountDetails(amounts);

        }
        return billFiscalEvent;
    }

    /**
     * publish fiscal event to iFix
     *
     * @param eventRequest
     */
    public void publishFiscalEvent(FiscalEventRequest eventRequest) {
        log.info("Posting fiscal eventRequest to iFix... ");
        List<EventPostingDetail> eventPostingDetailList = new ArrayList<>();

        //header
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(authTokenService.getAuthToken());
        //request
        HttpEntity<FiscalEventRequest> request = new HttpEntity<>(eventRequest, headers);

        if (eventRequest != null && eventRequest.getFiscalEvent() != null && !eventRequest.getFiscalEvent().isEmpty()) {
            try {
                ResponseEntity<FiscalEventResponse> responseEntity = restTemplate.postForEntity(getIfixPublishUrl(), request, FiscalEventResponse.class);

                eventPostingDetailList = wrapEventResponse(responseEntity, eventRequest);
                log.info("Posting to iFix - status : " + responseEntity.getStatusCode());
            } catch (RestClientException e) {
                log.error(LOG_ERROR_PREFIX + RECOVERABLE_ERROR + e.getMessage(), e);

                eventPostingDetailList = composeEventPostingDetail(eventRequest.getFiscalEvent(), HttpStatus.BAD_REQUEST,
                        e.getMessage());
            } catch (Exception e) {
                log.error(LOG_ERROR_PREFIX + NON_RECOVERABLE_ERROR + e.getMessage(), e);

                eventPostingDetailList = composeEventPostingDetail(eventRequest.getFiscalEvent(), HttpStatus.INTERNAL_SERVER_ERROR,
                        e.getMessage());
            }

            eventPostingDetailRepo.save(eventPostingDetailList);

        } else {
            log.error(LOG_ERROR_PREFIX + NON_RECOVERABLE_ERROR + "Invalid parameter to push event");
        }
    }

    /**
     * @return - return iFix event publish url
     */
    public String getIfixPublishUrl() {
        return (adapterConfiguration.getIfixHost() + adapterConfiguration.getIfixEventUrl());
    }


    /**
     * @param responseEntity
     * @param eventRequest
     * @return
     */
    private List<EventPostingDetail> wrapEventResponse(ResponseEntity<FiscalEventResponse> responseEntity,
                                                       FiscalEventRequest eventRequest) {
        if (responseEntity != null && responseEntity.getStatusCode() != null
                && eventRequest != null && eventRequest.getFiscalEvent() != null && !eventRequest.getFiscalEvent().isEmpty()) {

            List<EventPostingDetail> eventPostingDetailList = new ArrayList<>();

            if (HttpStatus.Series.SERVER_ERROR.equals(responseEntity.getStatusCode().series())
                    || HttpStatus.Series.CLIENT_ERROR.equals(responseEntity.getStatusCode().series())) {

                eventPostingDetailList = composeEventPostingDetail(eventRequest.getFiscalEvent(),
                        responseEntity.getStatusCode(), NA);

                log.error(LOG_ERROR_PREFIX + RECOVERABLE_ERROR +
                        "4 or 5 Series exception while sending request to Fiscal Event Service");

            } else if (HttpStatus.Series.SUCCESSFUL.equals(responseEntity.getStatusCode().series())) {

                List<FiscalEvent> responseFiscalEvents = responseEntity.getBody().getFiscalEvent();

                if (responseFiscalEvents != null && !responseFiscalEvents.isEmpty()) {
                    eventPostingDetailList = composeEventPostingDetail(responseFiscalEvents,
                            responseEntity.getStatusCode(), NA);

                    log.info(LOG_INFO_PREFIX + "Fiscal Event published successfully");
                } else {

                    eventPostingDetailList = composeEventPostingDetail(eventRequest.getFiscalEvent(),
                            responseEntity.getStatusCode(), EMPTY_FISCAL_EVENT);

                    log.error(LOG_ERROR_PREFIX + RECOVERABLE_ERROR + " Unable to receive Fiscal Event list in response");
                }
            }

            return eventPostingDetailList;
        }

        return Collections.emptyList();
    }

    /**
     * @param fiscalEventList
     * @param httpStatus
     * @param error
     * @return
     */
    private List<EventPostingDetail> composeEventPostingDetail(List<FiscalEvent> fiscalEventList, final HttpStatus httpStatus,
                                                               final String error) {
        if (fiscalEventList != null && httpStatus != null) {
            return fiscalEventList.stream()
                    .map(fiscalEvent -> {
                        EventPostingDetail eventPostingDetail = new EventPostingDetail();
                        eventPostingDetail.setTenantId(fiscalEvent.getTenantId());
                        eventPostingDetail.setReferenceId(fiscalEvent.getReferenceId());
                        eventPostingDetail.setEventType(fiscalEvent.getEventType());
                        eventPostingDetail.setCreatedDate(new Date());
                        eventPostingDetail.setLastModifiedDate(new Date());
                        eventPostingDetail.setError(error);
                        eventPostingDetail.setStatus(String.valueOf(httpStatus.value()));

                        try {
                            String eventRecord = objectMapper.writeValueAsString(fiscalEvent);
                            eventPostingDetail.setRecord(eventRecord);
                        } catch (JsonProcessingException e) {
                            log.error("Exception occurred while converting fiscal event into json string ", e);
                        }

                        if (HttpStatus.Series.SUCCESSFUL.equals(httpStatus.series())) {
                            eventPostingDetail.setIfixEventId(fiscalEvent.getId());
                        }

                        return eventPostingDetail;
                    }).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }
}
