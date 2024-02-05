package org.digit.program.utils;

import com.fasterxml.jackson.databind.JsonNode;
import org.digit.program.configuration.ProgramConfiguration;
import org.digit.program.constants.MessageType;
import org.digit.program.models.RequestJsonMessage;
import org.digit.program.models.RequestMessage;
import org.digit.program.repository.ServiceRequestRepository;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DispatcherUtil {

    private final ServiceRequestRepository restRepo;
    private final ProgramConfiguration configs;

    public DispatcherUtil(ServiceRequestRepository restRepo, ProgramConfiguration configs) {
        this.restRepo = restRepo;
        this.configs = configs;
    }

    public void sendOnProgram(RequestJsonMessage requestJsonMessage){
        List<String> messages = new ArrayList<>();
        for (JsonNode message : requestJsonMessage.getMessage()) {
            messages.add(message.toString());
        }
        RequestMessage requestMessage = RequestMessage.builder().id(requestJsonMessage.getId())
                .header(requestJsonMessage.getHeader()).signature(requestJsonMessage.getSignature())
                .message(messages).build();
        updateUri(requestMessage);
        StringBuilder url = new StringBuilder(configs.getExchangeHost()).append(configs.getExchangePath())
                .append(requestJsonMessage.getHeader().getMessageType());
        restRepo.fetchResult(url, requestMessage);
    }

    private void updateUri(RequestMessage requestMessage){
        String senderId = requestMessage.getHeader().getSenderId();
        requestMessage.getHeader().setSenderId(requestMessage.getHeader().getReceiverId());
        requestMessage.getHeader().setReceiverId(senderId);
        requestMessage.getHeader().setMessageType(MessageType.fromValue("on-" + requestMessage.getHeader().getMessageType().toString()));
    }

    public RequestJsonMessage forwardMessage(RequestJsonMessage requestJsonMessage){
        List<String> messages = new ArrayList<>();
        for (JsonNode message : requestJsonMessage.getMessage()) {
            messages.add(message.toString());
        }
        RequestMessage requestMessage = RequestMessage.builder().id(requestJsonMessage.getId())
                .header(requestJsonMessage.getHeader()).signature(requestJsonMessage.getSignature())
                .message(messages).build();
        StringBuilder url = new StringBuilder(configs.getExchangeHost()).append(configs.getExchangePath())
                .append(requestMessage.getHeader().getMessageType().toString());
        restRepo.fetchResult(url, requestMessage);
        return requestJsonMessage;
    }

}
