package org.digit.exchange.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.digit.exchange.config.AppConfig;
import org.digit.exchange.constants.ExchangeType;
import org.digit.exchange.kafka.ExchangeProducer;
import org.digit.exchange.model.JsonMessage;
import org.digit.exchange.model.RequestMessage;
import org.digit.exchange.model.RequestMessageWrapper;
import org.digit.exchange.repository.ServiceRequestRepository;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ExchangeService {
    private final ExchangeProducer producer;
    private final AppConfig config;
    private final ObjectMapper mapper;
    private final ServiceRequestRepository restRepo;

    public ExchangeService(ExchangeProducer producer, AppConfig config, ObjectMapper mapper, ServiceRequestRepository restRepo) {
        this.producer = producer;
        this.config = config;
        this.mapper = mapper;
        this.restRepo = restRepo;
    }

    public RequestMessage processMessage(ExchangeType type, RequestMessage messageRequest) {
        RequestMessageWrapper requestMessageWrapper = new RequestMessageWrapper(type, messageRequest);
        producer.push( config.getExchangeTopic(), requestMessageWrapper);
        log.info("Pushed message to kafka topic");
        return messageRequest;
    }

    public void send(RequestMessageWrapper requestMessageWrapper) {
        RequestMessage requestMessage = requestMessageWrapper.getRequestMessage();
        Boolean isReply = requestMessageWrapper.getType().toString().contains("on-") ;
        String url = getReceiverEndPoint(requestMessage, isReply);
        if(url==null)
            return;
        log.info("Receiver url mapped: {}", url);
        String receiverDomain = requestMessage.getHeader().getReceiverId().contains("@") ? requestMessage.getHeader().getReceiverId().split("@")[1] : requestMessage.getHeader().getReceiverId();
        if( receiverDomain.equalsIgnoreCase(config.getDomain())){
            log.info("Converting message to Json Node");
            JsonNode node;
            try {
                node = mapper.readTree(requestMessage.getMessage());
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            log.info("Converted message to Json Node");
            JsonMessage jsonMessage = JsonMessage.builder().signature(requestMessage.getSignature()).header(requestMessage.getHeader()).jsonNode(node).build();
            try {
                restRepo.fetchResult(url, jsonMessage);
                log.info("Posted request to : {}", url);
            } catch (Exception e) {
                log.error("Exception while calling the API : {}", e.getMessage());
                handleErrorForSameDomain(requestMessageWrapper, isReply);
            }
        } else {
            try {
                restRepo.fetchResult(url, requestMessage);
                log.info("Posted request to : {}", url);
            } catch (Exception e) {
                log.error("Exception while calling the API : {}", e.getMessage());
                handleErrorForDifferentDomain(requestMessageWrapper, isReply);
            }
        }

    }

    private void handleErrorForSameDomain(RequestMessageWrapper requestMessageWrapper, Boolean isReply) {
        if (Boolean.TRUE.equals(isReply)) {
            log.info("Pushing error to error topic for same domain");
            producer.push(config.getErrorTopic(), requestMessageWrapper);
        } else {
            log.info("Swapping receiver and sender domain");
            String receiverDomain = requestMessageWrapper.getRequestMessage().getHeader().getSenderId();
            String senderDomain = requestMessageWrapper.getRequestMessage().getHeader().getReceiverId();
            requestMessageWrapper.getRequestMessage().getHeader().setReceiverId(receiverDomain);
            requestMessageWrapper.getRequestMessage().getHeader().setSenderId(senderDomain);
            requestMessageWrapper.setType(ExchangeType.fromValue("on-" + requestMessageWrapper.getType().toString()));
            String newUrl = getReceiverEndPoint(requestMessageWrapper.getRequestMessage(), true);
            try {
                restRepo.fetchResult(newUrl, requestMessageWrapper.getRequestMessage());
            } catch (Exception ex) {
                log.info("Pushing error to error topic for same domain");
                producer.push(config.getErrorTopic(), requestMessageWrapper);
            }

        }
    }

    private void handleErrorForDifferentDomain(RequestMessageWrapper requestMessageWrapper, Boolean isReply) {
        if (Boolean.TRUE.equals(isReply)) {
            producer.push(config.getErrorTopic(), requestMessageWrapper);
        } else {
            log.info("Swapping receiver and sender domain");
            String receiverDomain = requestMessageWrapper.getRequestMessage().getHeader().getSenderId();
            String senderDomain = requestMessageWrapper.getRequestMessage().getHeader().getReceiverId();
            requestMessageWrapper.getRequestMessage().getHeader().setReceiverId(receiverDomain);
            requestMessageWrapper.getRequestMessage().getHeader().setSenderId(senderDomain);
            requestMessageWrapper.setType(ExchangeType.fromValue("on-" + requestMessageWrapper.getType().toString()));
            String newUrl = getReceiverEndPoint(requestMessageWrapper.getRequestMessage(), true);
            JsonNode newNode;
            log.info("Converting message to Json Node");
            try {
                newNode = mapper.readTree(requestMessageWrapper.getRequestMessage().getMessage());
            } catch (JsonProcessingException ex) {
                throw new RuntimeException(ex);
            }
            JsonMessage newJsonMessage = JsonMessage.builder().signature(requestMessageWrapper.getRequestMessage().getSignature()).header(requestMessageWrapper.getRequestMessage().getHeader()).jsonNode(newNode).build();
            try {
                restRepo.fetchResult(newUrl, newJsonMessage);
            } catch (Exception ex) {
                log.info("Pushing error to error topic for different domain");
                producer.push(config.getErrorTopic(), requestMessageWrapper);
            }
        }
    }


    String getReceiverEndPoint(RequestMessage message, Boolean isReply){
        String receiverDomain = message.getHeader().getReceiverId().contains("@") ? message.getHeader().getReceiverId().split("@")[1] : message.getHeader().getReceiverId();
        if( receiverDomain.equalsIgnoreCase(config.getDomain())){
            // Get the reciver Service name and get the host from config
            String receiverService = message.getHeader().getReceiverId().contains("@") ? message.getHeader().getReceiverId().split("@")[0] : "program";
            if (!config.getReceiverEndpoints().containsKey(receiverService))
                return null;
            String baseUrl = config.getReceiverEndpoints().get(receiverService);
            //Retrieve url from config
            return baseUrl + "/" + message.getHeader().getMessageType().toString() + "/_" + message.getHeader().getAction();
        } else {
            return receiverDomain + "/digit-exchange/v1/" + message.getHeader().getMessageType().toString();
        }
    }
}
