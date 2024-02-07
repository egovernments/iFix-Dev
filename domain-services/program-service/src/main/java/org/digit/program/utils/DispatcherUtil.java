package org.digit.program.utils;

import org.digit.program.configuration.ProgramConfiguration;
import org.digit.program.constants.MessageType;
import org.digit.program.models.ProgramRequest;
import org.digit.program.models.RequestHeader;
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

    public void sendOnProgram(ProgramRequest programRequest){
        RequestMessage requestMessage = RequestMessage.builder().id(programRequest.getId())
                .header(programRequest.getHeader()).signature(programRequest.getSignature())
                .message(programRequest.getProgram().toString()).build();
        updateUri(requestMessage);
        StringBuilder url = new StringBuilder(configs.getExchangeHost()).append(configs.getExchangePath())
                .append(programRequest.getHeader().getMessageType());
        restRepo.fetchResult(url, requestMessage);
    }

    private void updateUri(RequestMessage requestMessage){
        String senderId = requestMessage.getHeader().getSenderId();
        requestMessage.getHeader().setSenderId(requestMessage.getHeader().getReceiverId());
        requestMessage.getHeader().setReceiverId(senderId);
        requestMessage.getHeader().setMessageType(MessageType.fromValue("on-" + requestMessage.getHeader().getMessageType().toString()));
    }

    public Object forwardMessage(String id, String signature, RequestHeader requestHeader, String message){
        RequestMessage requestMessage = RequestMessage.builder().id(id)
                .header(requestHeader).signature(signature)
                .message(message).build();
        StringBuilder url = new StringBuilder(configs.getExchangeHost()).append(configs.getExchangePath())
                .append(requestMessage.getHeader().getMessageType().toString());
        Object response = restRepo.fetchResult(url, requestMessage);
        return response;
    }

}
