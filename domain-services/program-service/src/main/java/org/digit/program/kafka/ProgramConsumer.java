package org.digit.program.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ProgramConsumer {

    private final ObjectMapper objectMapper;

    public ProgramConsumer(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = {"${program.topic}"})
    public void listenProgram(final String receivedObject,  @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
        log.info("Consumed message from topic: {}", topic);
        log.info("Received message: {}", receivedObject);

    }

    @KafkaListener(topics = {"${sanction.topic}"})
    public void listenSanction(final String receivedObject,  @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
        log.info("Consumed message from topic: {}", topic);
        log.info("Received message: {}", receivedObject);



    }
    @KafkaListener(topics = {"${allocation.topic}"})
    public void listenAllocation(final String receivedObject,  @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
        log.info("Consumed message from topic: {}", topic);
        log.info("Received message: {}", receivedObject);

    }

    @KafkaListener(topics = {"${disburse.topic}"})
    public void listenDisburse(final String receivedObject,  @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
        log.info("Consumed message from topic: {}", topic);
        log.info("Received message: {}", receivedObject);
    }
}
