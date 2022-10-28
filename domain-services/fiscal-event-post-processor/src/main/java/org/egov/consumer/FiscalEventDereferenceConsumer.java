package org.egov.consumer;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.egov.config.FiscalEventPostProcessorConfig;
import org.egov.models.FiscalEventDeReferenced;
import org.egov.models.FiscalEventRequest;
import org.egov.producer.Producer;
import org.egov.service.FiscalEventDereferenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Slf4j
@Component
public class FiscalEventDereferenceConsumer {

    @Autowired
    private FiscalEventDereferenceService dereferenceService;

    @Autowired
    private FiscalEventPostProcessorConfig processorConfig;

    @Autowired
    private Producer producer;

    @Autowired
    private ObjectMapper mapper;

    @KafkaListener(topics = {"${fiscal.event.kafka.push.topic}"})
    public void listen(final HashMap<String, Object> record, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
        try {
            FiscalEventRequest fiscalEventRequest = mapper.convertValue(record, FiscalEventRequest.class);
            FiscalEventDeReferenced fiscalEventDeReferenced = dereferenceService.dereference(fiscalEventRequest);
            producer.push(processorConfig.getFiscalEventDereferenceTopic(), fiscalEventDeReferenced);
        } catch (Exception e) {
            log.error("Error occurred while processing the record from topic : " + topic, e);
            throw new RuntimeException(e);
        }
    }
}
