package org.digit.exchange.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.digit.exchange.config.AppConfig;
import org.digit.exchange.model.RequestMessageWrapper;
import org.digit.exchange.service.ExchangeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ExchangeConsumer {

    private ExchangeService exchangeService;
    private ObjectMapper objectMapper;

    public ExchangeConsumer(ExchangeService exchangeService, ObjectMapper objectMapper) {
        this.exchangeService = exchangeService;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = {"${app.exchange.topic.name}"})
    public void listen(final String receivedObject,  @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
        log.info("Consumed message from topic: {}", topic);
        log.info("Received message: {}", receivedObject);
        RequestMessageWrapper requestMessageWrapper = null;
        try {
            requestMessageWrapper = objectMapper.readValue(receivedObject, RequestMessageWrapper.class);
        } catch (JsonProcessingException e) {
            log.error("Exception while converting message to RequestMessageWrapper", e);
            throw new RuntimeException(e);
        }
        log.info("Successfully converted message to RequestMessageWrapper");
        exchangeService.send(requestMessageWrapper);
    }

}
