package org.egov.service;


import lombok.extern.slf4j.Slf4j;
import org.egov.config.FiscalEventConfiguration;
import org.egov.producer.Producer;
import org.egov.validator.FiscalEventValidator;
import org.egov.web.models.FiscalEventRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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


    /**
     * Validate , enrich and push the fiscal Event request to topic
     * @param fiscalEventRequest
     * @return
     */
    public FiscalEventRequest fiscalEventsV1PushPost(FiscalEventRequest fiscalEventRequest) {
        validator.validateFiscalEventPushPost(fiscalEventRequest);
        enricher.enrichFiscalEventPushPost(fiscalEventRequest);
        producer.push(eventConfiguration.getFiscalPushRequest(),fiscalEventRequest.getFiscalEvent());
        return fiscalEventRequest;
    }
}
