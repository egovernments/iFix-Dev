package org.egov.service;


import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.egov.common.contract.request.RequestHeader;
import org.egov.config.FiscalEventConfiguration;
import org.egov.producer.Producer;
import org.egov.repository.FiscalEventRepository;
import org.egov.util.FiscalEventMapperUtil;
import org.egov.validator.FiscalEventValidator;
import org.egov.web.models.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.egov.util.MasterDataConstants.FISCAL_EVENT_VERSION;

@Service
@Slf4j
public class FiscalEventService {

    @Autowired
    private FiscalEventValidator validator;

    @Autowired
    private FiscalEventEnrichmentService enricher;

    @Autowired
    private Producer producer;

    @Autowired
    private FiscalEventConfiguration eventConfiguration;

    @Autowired
    private FiscalEventRepository eventRepository;

    @Autowired
    private FiscalEventMapperUtil mapperUtil;


    /**
     * Validate , enrich and push the fiscal Event request to topic
     *
     * @param fiscalEventRequest
     * @return
     */
    public FiscalEventRequest fiscalEventsV1PushPost(FiscalEventRequest fiscalEventRequest) {
        validator.validateFiscalEventPushPost(fiscalEventRequest);
        enricher.enrichFiscalEventPushPost(fiscalEventRequest);

        FiscalEventRequest fiscalEventRequestCopy = new FiscalEventRequest();
        BeanUtils.copyProperties(fiscalEventRequest, fiscalEventRequestCopy);
        //nullify the coaCode before pushing to topics
        nullifyCoaCodeAndAddVersion(fiscalEventRequest);

        if (fiscalEventRequest.getFiscalEvent() != null && !fiscalEventRequest.getFiscalEvent().isEmpty()) {
            RequestHeader requestHeader = fiscalEventRequest.getRequestHeader();
            for (FiscalEvent fiscalEvent : fiscalEventRequest.getFiscalEvent()) {
                ObjectNode fiscalEventRequestNode = JsonNodeFactory.instance.objectNode();
                fiscalEventRequestNode.putPOJO("requestHeader", requestHeader);
                fiscalEventRequestNode.putPOJO("fiscalEvent", fiscalEvent);

                //push with request header details
                producer.push(eventConfiguration.getFiscalPushRequest(), fiscalEventRequestNode);
                //push without request header details
                producer.push(eventConfiguration.getFiscalEventPushToMongoSink(), fiscalEvent);
            }
        }
        //nullify coaId before sending the response
        nullifyCoaIdCodeAndAddVersion(fiscalEventRequestCopy);
        return fiscalEventRequestCopy;
    }

    private void nullifyCoaIdCodeAndAddVersion(FiscalEventRequest fiscalEventRequest) {
        List<FiscalEvent> fiscalEvents = new ArrayList<>();

        if (fiscalEventRequest.getFiscalEvent() != null && !fiscalEventRequest.getFiscalEvent().isEmpty()) {
            for (FiscalEvent fiscalEvent : fiscalEventRequest.getFiscalEvent()) {
                FiscalEvent fiscalEventCopy = new FiscalEvent();
                BeanUtils.copyProperties(fiscalEvent, fiscalEventCopy);

                if (fiscalEvent.getAmountDetails() != null && !fiscalEvent.getAmountDetails().isEmpty()) {
                    List<Amount> amounts = new ArrayList<>();
                    for (Amount amount : fiscalEvent.getAmountDetails()) {
                        Amount amountCopy = new Amount();
                        BeanUtils.copyProperties(amount, amountCopy);
                        amountCopy.setCoaId(null);
                        amounts.add(amountCopy);
                    }
                    fiscalEventCopy.setVersion(FISCAL_EVENT_VERSION);
                    fiscalEventCopy.setAmountDetails(amounts);
                }
                fiscalEvents.add(fiscalEventCopy);
            }
            fiscalEventRequest.setFiscalEvent(fiscalEvents);
        }
    }

    private void nullifyCoaCodeAndAddVersion(FiscalEventRequest fiscalEventRequest) {
        List<FiscalEvent> fiscalEvents = new ArrayList<>();

        if (fiscalEventRequest.getFiscalEvent() != null && !fiscalEventRequest.getFiscalEvent().isEmpty()) {
            for (FiscalEvent fiscalEvent : fiscalEventRequest.getFiscalEvent()) {
                FiscalEvent fiscalEventCopy = new FiscalEvent();
                BeanUtils.copyProperties(fiscalEvent, fiscalEventCopy);

                if (fiscalEvent.getAmountDetails() != null && !fiscalEvent.getAmountDetails().isEmpty()) {
                    List<Amount> amounts = new ArrayList<>();
                    for (Amount amount : fiscalEvent.getAmountDetails()) {
                        Amount amountCopy = new Amount();
                        BeanUtils.copyProperties(amount, amountCopy);
                        amountCopy.setCoaCode(null);
                        amounts.add(amountCopy);
                    }
                    fiscalEventCopy.setVersion(FISCAL_EVENT_VERSION);
                    fiscalEventCopy.setAmountDetails(amounts);
                }
                fiscalEvents.add(fiscalEventCopy);
            }
            fiscalEventRequest.setFiscalEvent(fiscalEvents);
        }
    }

    /**
     * Validate the request, search based on the criteria
     *
     * @param fiscalEventGetRequest
     * @return
     */
    public List<FiscalEvent> fiscalEventsV1SearchPost(FiscalEventGetRequest fiscalEventGetRequest) {
        validator.validateFiscalEventSearchPost(fiscalEventGetRequest);
        Criteria searchCriteria = fiscalEventGetRequest.getCriteria();

        if ((searchCriteria.getIds() == null || searchCriteria.getIds().isEmpty()) && StringUtils.isBlank(searchCriteria.getEventType())
                && StringUtils.isBlank(searchCriteria.getTenantId()) && searchCriteria.getFromEventTime() == null
                && searchCriteria.getToEventTime() == null && (searchCriteria.getReferenceId() == null || searchCriteria.getReferenceId().isEmpty()) && searchCriteria.getFromIngestionTime() == null
                && searchCriteria.getToIngestionTime() == null && StringUtils.isBlank(searchCriteria.getReceiver())) {
            return Collections.emptyList();
        }

        List<Object> dereferencedFiscalEvents = eventRepository.searchFiscalEvent(searchCriteria);

        if (dereferencedFiscalEvents == null || dereferencedFiscalEvents.isEmpty())
            return Collections.emptyList();

        List<FiscalEvent> fiscalEvents = mapperUtil.mapDereferencedFiscalEventToFiscalEvent(dereferencedFiscalEvents);

        return fiscalEvents;
    }

    public List<FiscalEvent> fiscalEventsV1PlainSearchPost(FiscalEventPlainSearchRequest fiscalEventGetRequest) {
        //validator.validateFiscalEventPlainSearch(fiscalEventGetRequest);

        PlainsearchCriteria searchCriteria = fiscalEventGetRequest.getCriteria();

        List<Object> dereferencedFiscalEvents = eventRepository.plainSearchFiscalEvent(searchCriteria);

        if (dereferencedFiscalEvents == null || dereferencedFiscalEvents.isEmpty())
            return Collections.emptyList();

        List<FiscalEvent> fiscalEvents = mapperUtil.mapDereferencedFiscalEventToFiscalEvent(dereferencedFiscalEvents);

        return fiscalEvents;
    }

    public Long getFiscalEventsCount(FiscalEventPlainSearchRequest body) {
        return eventRepository.getFiscalEventsCount(body.getCriteria());
    }
}
