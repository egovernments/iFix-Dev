package org.digit.program.kafka;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class ProgramProducer {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public ProgramProducer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void push(String topic, Object value) {
        kafkaTemplate.send(topic, value);
    }
}
