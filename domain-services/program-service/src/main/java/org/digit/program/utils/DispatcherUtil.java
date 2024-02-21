package org.digit.program.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.digit.program.configuration.ProgramConfiguration;
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
    private final CommonUtil commonUtil;

    public DispatcherUtil(ServiceRequestRepository restRepo, ProgramConfiguration configs, ObjectMapper mapper, CommonUtil commonUtil) {
        this.restRepo = restRepo;
        this.configs = configs;
        this.mapper = mapper;
        this.commonUtil = commonUtil;
    }

    public void dispatchProgram (ProgramRequest programRequest) {
        String message;
        try {
            message = mapper.writeValueAsString(programRequest.getProgram());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        if (commonUtil.isSameDomain(programRequest.getHeader().getReceiverId(), configs.getDomain()))
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
        if (!commonUtil.isSameDomain(programRequest.getHeader().getReceiverId(), configs.getDomain()))
            forwardMessage(programRequest.getId(), programRequest.getSignature(),
                    programRequest.getHeader(), message);
    }

    public void sendOnProgram(ProgramRequest programRequest, String message){
        RequestMessage requestMessage = RequestMessage.builder().id(programRequest.getId())
                .header(programRequest.getHeader()).signature(programRequest.getSignature())
                .message(message).build();
        commonUtil.updateUri(requestMessage.getHeader());
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
        if (!commonUtil.isSameDomain(sanctionRequest.getHeader().getReceiverId(), configs.getDomain()))
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
        if (!commonUtil.isSameDomain(allocationRequest.getHeader().getReceiverId(), configs.getDomain()))
            forwardMessage(allocationRequest.getId(), allocationRequest.getSignature(),
                    allocationRequest.getHeader(), message);
    }

    public Object dispatchDisburse (DisbursementRequest disbursementRequest) {
        Object response = null;
        if (commonUtil.isSameDomain(disbursementRequest.getHeader().getReceiverId(), configs.getDomain())) {
            response = forwardToAdapter(disbursementRequest);
        } else {
            String message;
            try {
                message = mapper.writeValueAsString(disbursementRequest.getDisbursement());
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
             forwardMessage(disbursementRequest.getId(), disbursementRequest.getSignature(),
                    disbursementRequest.getHeader(), message);
            
        }
        return response;
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
