package org.egov.ifixespipeline.consumers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.request.RequestHeader;
import org.egov.ifixespipeline.models.FiscalEvent;
import org.egov.ifixespipeline.models.FiscalEventRequest;
import org.egov.ifixespipeline.models.ProducerPOJO;
import org.egov.ifixespipeline.producer.Producer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
@Slf4j
public class MigrationRecordsListener {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private Producer producer;

    @Value("${fiscal.event.migration.origin.push.topic}")
    private String fiscalEventsIndexTopic;

    @KafkaListener(topics = { "${fiscal.events.migration.topic}"})
    public void listen(HashMap<String, Object> record, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
        try {
            ProducerPOJO incomingData = objectMapper.convertValue(record, ProducerPOJO.class);
            for(FiscalEvent fiscalEvent : incomingData.getRecords()){
                producer.push(fiscalEventsIndexTopic, FiscalEventRequest.builder().requestHeader(new RequestHeader()).fiscalEvent(fiscalEvent).build());
            }

        }catch(Exception e) {
            log.error("Exception while reading from the queue: ", e);
        }
    }

}
