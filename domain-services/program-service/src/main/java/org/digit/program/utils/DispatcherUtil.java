package org.digit.program.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.digit.program.configuration.ProgramConfiguration;
import org.digit.program.constants.MessageType;
import org.digit.program.models.allocation.AllocationRequest;
import org.digit.program.models.disburse.DisbursementRequest;
import org.digit.program.models.program.ProgramRequest;
import org.digit.program.models.RequestHeader;
import org.digit.program.models.RequestMessage;
import org.digit.program.models.sanction.SanctionRequest;
import org.digit.program.repository.ServiceRequestRepository;
import org.springframework.stereotype.Component;

@Component
public class DispatcherUtil {

    private final ServiceRequestRepository restRepo;
    private final ProgramConfiguration configs;
    private final ObjectMapper mapper;

    public DispatcherUtil(ServiceRequestRepository restRepo, ProgramConfiguration configs, ObjectMapper mapper) {
        this.restRepo = restRepo;
        this.configs = configs;
        this.mapper = mapper;
    }

    public void dispatchProgram (ProgramRequest programRequest) {
        String message;
        try {
            message = mapper.writeValueAsString(programRequest.getProgram());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        if (programRequest.getHeader().getReceiverId().split("@")[1].equalsIgnoreCase(configs.getDomain()))
            sendOnProgram(programRequest, message);
        else
            forwardMessage(programRequest.getId(), programRequest.getSignature(),
                    programRequest.getHeader(), message);
    }

    public void dispatchOnProgram (ProgramRequest programRequest) {
        String message;
        try {
            message = mapper.writeValueAsString(programRequest.getProgram());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        if (!programRequest.getHeader().getReceiverId().split("@")[1].equalsIgnoreCase(configs.getDomain()))
            forwardMessage(programRequest.getId(), programRequest.getSignature(),
                    programRequest.getHeader(), message);
    }

    public void sendOnProgram(ProgramRequest programRequest, String message){
        RequestMessage requestMessage = RequestMessage.builder().id(programRequest.getId())
                .header(programRequest.getHeader()).signature(programRequest.getSignature())
                .message(message).build();
        updateUri(requestMessage);
        StringBuilder url = new StringBuilder(configs.getExchangeHost()).append(configs.getExchangePath())
                .append(programRequest.getHeader().getMessageType());
        restRepo.fetchResult(url, requestMessage);
    }

    public void dispatchOnSanction (SanctionRequest sanctionRequest) {
        String message;
        try {
            message = mapper.writeValueAsString(sanctionRequest.getSanctions());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        if (!sanctionRequest.getHeader().getReceiverId().split("@")[1].equalsIgnoreCase(configs.getDomain()))
            forwardMessage(sanctionRequest.getId(), sanctionRequest.getSignature(),
                    sanctionRequest.getHeader(), message);
    }

    public void dispatchOnAllocation (AllocationRequest allocationRequest) {
        String message;
        try {
            message = mapper.writeValueAsString(allocationRequest.getAllocations());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        if (!allocationRequest.getHeader().getReceiverId().split("@")[1].equalsIgnoreCase(configs.getDomain()))
            forwardMessage(allocationRequest.getId(), allocationRequest.getSignature(),
                    allocationRequest.getHeader(), message);
    }

    public void dispatchDisburse (DisbursementRequest disbursementRequest) {
        if (!disbursementRequest.getHeader().getReceiverId().split("@")[1].equalsIgnoreCase(configs.getDomain())) {
            String message;
            try {
                message = mapper.writeValueAsString(disbursementRequest.getDisbursement());
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            forwardMessage(disbursementRequest.getId(), disbursementRequest.getSignature(),
                    disbursementRequest.getHeader(), message);
        } else {
            forwardToAdapter(disbursementRequest);
        }
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

    public Object forwardToAdapter(DisbursementRequest disbursementRequest){
        StringBuilder url = new StringBuilder(configs.getAdapterHost()).append(configs.getAdapterPath())
                .append(disbursementRequest.getHeader().getMessageType().toString()).append("/_")
                .append(disbursementRequest.getHeader().getAction().toString());
        Object response = restRepo.fetchResult(url, disbursementRequest);
        return response;
    }

}
