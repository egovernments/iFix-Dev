package org.digit.exchange.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;
import org.digit.exchange.config.AppConfig;
import org.digit.exchange.constants.ExchangeType;
import org.digit.exchange.kafka.ExchangeProducer;
import org.digit.exchange.model.JsonMessage;
import org.digit.exchange.model.RequestMessage;
import org.digit.exchange.model.RequestMessageWrapper;
import org.digit.exchange.repository.ServiceRequestRepository;
import org.egov.tracer.model.CustomException;
import org.springframework.stereotype.Service;

import javax.servlet.ServletContext;

@Service
@Slf4j
public class ExchangeService {
    private final ExchangeProducer producer;
    private final AppConfig config;
    private final ObjectMapper mapper;
    private final ServiceRequestRepository restRepo;
    private final SecurityService securityService;
    private final ServletContext servletContext;


    public ExchangeService(ExchangeProducer producer, AppConfig config, ObjectMapper mapper, ServiceRequestRepository restRepo, SecurityService securityService,  ServletContext servletContext) {
        this.producer = producer;
        this.config = config;
        this.mapper = mapper;
        this.restRepo = restRepo;
        this.securityService = securityService;
        this.servletContext = servletContext;
    }

    public RequestMessage processMessage(ExchangeType type, RequestMessage messageRequest) {
        RequestMessageWrapper requestMessageWrapper = new RequestMessageWrapper(type, messageRequest);
        producer.push( config.getExchangeTopic(), requestMessageWrapper);
        log.info("Pushed message to kafka topic");
        // If log events is true then process and push to the kafka
        if (config.isEnabledEventLogs()) {
            log.info("Emitting events to topic");
            emitEventsToTopic(requestMessageWrapper);
        }
        return messageRequest;
    }

    public void send(RequestMessageWrapper requestMessageWrapper) {
        RequestMessage requestMessage = requestMessageWrapper.getRequestMessage();
        Boolean isReply = requestMessageWrapper.getType().toString().contains("on-") ;
        String url = getReceiverEndPoint(requestMessage, isReply);
        if(url==null)
            return;
        log.info("Receiver url mapped: {}", url);
        if(securityService.isSameDomain(requestMessage.getHeader().getReceiverId(), config.getDomain())){
            log.info("Converting message to Json Node");
            JsonNode jsonNode;
            try {
                jsonNode = mapper.readTree(requestMessage.getMessage());
            } catch (JsonProcessingException e) {
                throw new CustomException("JSON_PROCESSING_EXCEPTION", e.getMessage());
            }

            log.info("Converted message to Json Node");
            JsonMessage jsonMessage = JsonMessage.builder().signature(requestMessage.getSignature())
                    .header(requestMessage.getHeader()).jsonNode(jsonNode).build();
            try {
                restRepo.fetchResult(url, jsonMessage);
                log.info("Posted request to : {}", url);
            } catch (CustomException exception) {
                log.error("Exception while calling the API : {}", exception.getMessage());
                handleErrorForSameDomain(requestMessageWrapper, isReply, exception);
            } catch (Exception exception) {
                log.error("Exception while calling the API : {}", exception.getMessage());
                handleErrorForSameDomain(requestMessageWrapper, isReply, new CustomException("EXTERANL_SERVICE_ERROR",
                        exception.getMessage()));
            }
        } else {
            // If sending the request from this server to another server then sign the message
            if (securityService.isSameDomain(requestMessage.getHeader().getSenderId(), config.getDomain())) {
                requestMessage = securityService.signRequestMessage(requestMessage);
            }
            try {
                restRepo.fetchResult(url, requestMessage);
                log.info("Posted request to : {}", url);
            } catch (CustomException exception) {
                log.error("Exception while calling the API : {}", exception.getMessage());
                handleErrorForDifferentDomain(requestMessageWrapper, isReply, exception);
            } catch (Exception exception) {
                log.error("Exception while calling the API : {}", exception.getMessage());
                handleErrorForDifferentDomain(requestMessageWrapper, isReply, new CustomException("EXTERANL_SERVICE_ERROR", exception.getMessage()));
            }
        }

    }

    public void emitEventsToTopic(RequestMessageWrapper requestMessageWrapper) {
        try {
            log.info("Converting message to Json Node for emit events");
            JsonNode jsonNode;
            RequestMessage requestMessage = requestMessageWrapper.getRequestMessage();
            jsonNode = mapper.readTree(requestMessageWrapper.getRequestMessage().getMessage());
            log.info("Converted message to Json Node");
            JsonMessage jsonMessage = JsonMessage.builder().signature(requestMessage.getSignature())
                    .header(requestMessage.getHeader()).jsonNode(jsonNode).build();
            producer.push( config.getExchangeEventLogTopic(), jsonMessage);
            log.info("Pushed message to kafka topic : " + config.getExchangeEventLogTopic());
        }catch (Exception e) {
            log.error("Exception while emitting events: {}", e.getMessage());
        }
    }

    private void handleErrorForSameDomain(RequestMessageWrapper requestMessageWrapper, Boolean isReply, CustomException exception) {
        if (Boolean.TRUE.equals(isReply)) {
            log.info("Pushing error to error topic for same domain");
            producer.push(config.getErrorTopic(), requestMessageWrapper);
        } else {
            log.info("Swapping receiver and sender domain");
            updateHeaderDomains(requestMessageWrapper);
            String newUrl = getReceiverEndPoint(requestMessageWrapper.getRequestMessage(), true);
            convertMessageAndSetStatus(requestMessageWrapper.getRequestMessage(), exception);
            try {
                restRepo.fetchResult(newUrl, requestMessageWrapper.getRequestMessage());
            } catch (Exception ex) {
                log.info("Pushing error to error topic for same domain");
                producer.push(config.getErrorTopic(), requestMessageWrapper);
            }

        }
    }

    private void handleErrorForDifferentDomain(RequestMessageWrapper requestMessageWrapper, Boolean isReply, CustomException exception) {
        if (Boolean.TRUE.equals(isReply)) {
            producer.push(config.getErrorTopic(), requestMessageWrapper);
        } else {
            log.info("Swapping receiver and sender domain");
            updateHeaderDomains(requestMessageWrapper);
            String newUrl = getReceiverEndPoint(requestMessageWrapper.getRequestMessage(), true);
            convertMessageAndSetStatus(requestMessageWrapper.getRequestMessage(), exception);
            log.info("Converting message to Json Node");
            JsonNode newNode;
            try {
                newNode = mapper.readTree(requestMessageWrapper.getRequestMessage().getMessage());
            } catch (JsonProcessingException ex) {
                throw new RuntimeException(ex);
            }

            JsonMessage newJsonMessage = JsonMessage.builder()
                    .signature(requestMessageWrapper.getRequestMessage().getSignature())
                    .header(requestMessageWrapper.getRequestMessage().getHeader())
                    .jsonNode(newNode).build();
            try {
                restRepo.fetchResult(newUrl, newJsonMessage);
            } catch (Exception ex) {
                log.info("Pushing error to error topic for different domain");
                producer.push(config.getErrorTopic(), requestMessageWrapper);
            }
        }
    }

    void updateHeaderDomains(RequestMessageWrapper requestMessageWrapper) {
        String receiverDomain = requestMessageWrapper.getRequestMessage().getHeader().getSenderId();
        String senderDomain = requestMessageWrapper.getRequestMessage().getHeader().getReceiverId();
        requestMessageWrapper.getRequestMessage().getHeader().setReceiverId(receiverDomain);
        requestMessageWrapper.getRequestMessage().getHeader().setSenderId(senderDomain);
        requestMessageWrapper.getRequestMessage().getHeader().setMessageType(ExchangeType.fromValue("on-" + requestMessageWrapper.getRequestMessage().getHeader().getMessageType().toString()));
        requestMessageWrapper.setType(ExchangeType.fromValue("on-" + requestMessageWrapper.getType().toString()));
    }

    String getReceiverEndPoint(RequestMessage message, Boolean isReply){
        if(securityService.isSameDomain(message.getHeader().getReceiverId(), config.getDomain())){
            // Get the receiver Service name and get the host from config
            String receiverService = message.getHeader().getReceiverId().contains("@") ? message.getHeader().getReceiverId().split("@")[0] : "program";
            if (!config.getReceiverEndpoints().containsKey(receiverService))
                return null;
            String baseUrl = config.getReceiverEndpoints().get(receiverService);
            //Retrieve url from config
            return baseUrl + "/" + message.getHeader().getMessageType().toString() + "/_" + message.getHeader().getAction();
        } else {
            String receiverDomain = securityService.extractHostUrlFromURL(message.getHeader().getReceiverId());
            String contextPath = servletContext.getContextPath();
            return receiverDomain  +"/v1/exchange/" + message.getHeader().getMessageType().toString();
        }
    }

    void convertMessageAndSetStatus(RequestMessage requestMessage, CustomException exception) {
        ObjectNode objectNode;
        try {
            objectNode = mapper.readValue(requestMessage.getMessage(), ObjectNode.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        setErrorStatuses(objectNode, exception);

        try {
            requestMessage.setMessage(mapper.writeValueAsString(objectNode));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    void setErrorStatuses(ObjectNode objectNode, CustomException exception) {

        if (objectNode.has("status"))
            objectNode.remove("status");
        ObjectNode statusNode = mapper.createObjectNode();
        statusNode.put("status_code", "ERROR");
        statusNode.put("status_message", exception.getCode() + " " + exception.getMessage());
        objectNode.set("status", statusNode);
        if (objectNode.has("children") && objectNode.get("children").isArray()) {
            for (JsonNode child : objectNode.get("children")) {
                setErrorStatuses((ObjectNode) child, exception);
            }
        }
    }
}
