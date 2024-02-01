package org.digit.program.utils;

import org.digit.program.configuration.ProgramConfiguration;
import org.digit.program.models.RequestJsonMessage;
import org.digit.program.models.RequestMessage;
import org.digit.program.repository.ServiceRequestRepository;
import org.springframework.stereotype.Component;

@Component
public class DispatcherUtil {

    private final ServiceRequestRepository restRepo;
    private final ProgramConfiguration configs;

    public DispatcherUtil(ServiceRequestRepository restRepo, ProgramConfiguration configs) {
        this.restRepo = restRepo;
        this.configs = configs;
    }

    public void sendOnProgram(RequestJsonMessage requestJsonMessage){
        RequestMessage requestMessage = RequestMessage.builder().id(requestJsonMessage.getId())
                .header(requestJsonMessage.getHeader()).signature(requestJsonMessage.getSignature())
                .message(requestJsonMessage.getMessage().toString()).build();
        updateUri(requestMessage);
        StringBuilder url = new StringBuilder(configs.getExchangeHost()).append(configs.getExchangePath())
                .append("on-").append(requestJsonMessage.getHeader().getMessageType());
        restRepo.fetchResult(url, requestMessage);
    }

    private void updateUri(RequestMessage requestMessage){
        String senderId = requestMessage.getHeader().getSenderId();
        requestMessage.getHeader().setSenderId(requestMessage.getHeader().getReceiverId());
        requestMessage.getHeader().setReceiverId(senderId);
    }

    public RequestJsonMessage forwardMessage(RequestJsonMessage requestJsonMessage){
        RequestMessage requestMessage = RequestMessage.builder().id(requestJsonMessage.getId())
                .header(requestJsonMessage.getHeader()).signature(requestJsonMessage.getSignature())
                .message(requestJsonMessage.getMessage().toString()).build();
        StringBuilder url = new StringBuilder(configs.getExchangeHost()).append(configs.getExchangePath())
                .append(requestMessage.getHeader().getMessageType().toString());
        restRepo.fetchResult(url, requestMessage);
        return requestJsonMessage;
    }

}
