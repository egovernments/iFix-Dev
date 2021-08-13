package org.egov.consumer;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.egov.service.FiscalEventDereferenceService;
import org.egov.web.models.FiscalEventRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Slf4j
@Component
public class FiscalEventPostConsumer {

    @Autowired
    private FiscalEventDereferenceService deferenceService;

    @Autowired
    private ObjectMapper mapper;
    /**/
    @KafkaListener(topics = {"${fiscal.event.kafka.push.topic}"})
    public void listen(final HashMap<String, Object> record, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {

        try {
            log.info("Received fiscal event post request : " + record);
            FiscalEventRequest fiscalEventRequest = mapper.convertValue(record, FiscalEventRequest.class);;
            deferenceService.dereference(fiscalEventRequest);
        }
        catch (Exception e){
            log.error("Error occured while processing the record from topic : " /*+ topic*/, e);
        }

    }
}
