package org.egov.service;


import lombok.extern.slf4j.Slf4j;
import org.egov.config.FiscalEventPostProcessorConfig;
import org.egov.producer.Producer;
import org.egov.web.models.FiscalEventDeReferenced;
import org.egov.web.models.FiscalEventRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class FiscalEventDereferenceService {

    @Autowired
    private Producer producer;

    @Autowired
    private FiscalEventPostProcessorConfig processorConfig;

    @Autowired
    private FiscalEventDereferenceEnrichmentService enricher;

    public void dereference(FiscalEventRequest fiscalEventRequest) {
        FiscalEventDeReferenced fiscalEventDeReferenced =  new FiscalEventDeReferenced();
        dereferenceCoaId(fiscalEventRequest,fiscalEventDeReferenced);
        enricher.enrich(fiscalEventDeReferenced,fiscalEventDeReferenced);
        producer.push(processorConfig.getEventProcessorMongoDB(),fiscalEventDeReferenced);
    }

    private void dereferenceCoaId(FiscalEventRequest fiscalEventRequest, FiscalEventDeReferenced fiscalEventDeReferenced) {
    }
}
