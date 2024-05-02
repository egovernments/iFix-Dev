package org.digit.program.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.digit.program.configuration.ProgramConfiguration;
import org.digit.program.constants.MessageType;
import org.digit.program.models.allocation.AllocationRequest;
import org.digit.program.models.disburse.DisbursementRequest;
import org.digit.program.models.program.ProgramRequest;
import org.digit.program.models.RequestHeader;
import org.digit.program.models.RequestMessage;
import org.digit.program.models.sanction.SanctionRequest;
import org.digit.program.repository.ServiceRequestRepository;
import org.egov.tracer.model.CustomException;
import org.springframework.stereotype.Component;

@Component
@Slf4j
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

    /**
     * Dispatches program to respective service
     * @param programRequest
     */
    public void dispatchProgram (ProgramRequest programRequest) {
        log.info("Dispatching Program");
        String message;
        try {
            message = mapper.writeValueAsString(programRequest.getProgram());
        } catch (JsonProcessingException e) {
            throw new CustomException("PARSING_ERROR", "Error while parsing " + e.getMessage());
        }
        if (commonUtil.isSameDomain(programRequest.getHeader().getReceiverId(), configs.getDomain()))
            sendOnProgram(programRequest, message);
        else
            forwardMessage(programRequest.getSignature(),
                    programRequest.getHeader(), message);
        log.info("Program Successfully Dispatched");
    }

    /**
     * Dispatches reply to exchange service is domain is same
     * @param programRequest
     */
    public void dispatchOnProgram (ProgramRequest programRequest) {
        log.info("Dispatching On Program");
        String message;
        try {
            message = mapper.writeValueAsString(programRequest.getProgram());
        } catch (JsonProcessingException e) {
            log.error("Error while parsing " + e.getMessage());
            throw new CustomException("PARSING_ERROR", "Error while parsing " + e.getMessage());
        }
        if (!commonUtil.isSameDomain(programRequest.getHeader().getReceiverId(), configs.getDomain()))
            forwardMessage(programRequest.getSignature(), programRequest.getHeader(), message);
        log.info("On Program Successfully Dispatched");
    }

    /**
     * Sends reply to exchange service when program is created
     * @param programRequest
     * @param message
     */
    public void sendOnProgram(ProgramRequest programRequest, String message){
        log.info("Sending Program reply");
        RequestMessage requestMessage = RequestMessage.builder()
                .header(programRequest.getHeader()).signature(programRequest.getSignature())
                .message(message).build();
        commonUtil.updateUri(requestMessage.getHeader());
        StringBuilder url = new StringBuilder(configs.getExchangeHost()).append(configs.getExchangePath())
                .append(programRequest.getHeader().getMessageType());
        restRepo.fetchResult(url, requestMessage);
    }

    /**
     * Forwards on sanction message to exchange service
     * @param sanctionRequest
     */
    public void dispatchOnSanction (SanctionRequest sanctionRequest) {
        log.info("Dispatching On Sanction");
        String message;
        try {
            message = mapper.writeValueAsString(sanctionRequest.getSanction());
        } catch (JsonProcessingException e) {
            throw new CustomException("PARSING_ERROR", "Error while parsing " + e.getMessage());
        }
        if (!commonUtil.isSameDomain(sanctionRequest.getHeader().getReceiverId(), configs.getDomain()))
            forwardMessage(sanctionRequest.getSignature(), sanctionRequest.getHeader(), message);
        log.info("On Sanction Successfully Dispatched");
    }

    /**
     * Forwards on allocation message to exchange service
     * @param allocationRequest
     */
    public void dispatchOnAllocation (AllocationRequest allocationRequest) {
        log.info("Dispatching On Allocation");
        String message;
        try {
            message = mapper.writeValueAsString(allocationRequest.getAllocation());
        } catch (JsonProcessingException e) {
            throw new CustomException("PARSING_ERROR", "Error while parsing " + e.getMessage());
        }
        if (!commonUtil.isSameDomain(allocationRequest.getHeader().getReceiverId(), configs.getDomain()))
            forwardMessage(allocationRequest.getSignature(),
                    allocationRequest.getHeader(), message);
        log.info("On Allocation Successfully Dispatched");
    }

    /**
     * Dispatches disbursement to adapter if receiver id is same as domain else forward to exchange service
     * @param disbursementRequest
     * @return
     */
    public DisbursementRequest dispatchDisburse (DisbursementRequest disbursementRequest) {
        log.info("Dispatching Disbursement");
        Object response = null;
        if (commonUtil.isSameDomain(disbursementRequest.getHeader().getReceiverId(), configs.getDomain())) {
            log.info("Dispatching Disbursement to Adapter");
            response = forwardToAdapter(disbursementRequest);
            if (disbursementRequest.getHeader().getMessageType().equals(MessageType.DISBURSE)) {
                DisbursementRequest disbursementReply = mapper.convertValue(response, DisbursementRequest.class);
                log.info("Disbursement Successfully Dispatched");
                return disbursementReply;
            }
        } else {
            log.info("Forwarding Disbursement to Exchange");
            String message;
            try {
                message = mapper.writeValueAsString(disbursementRequest.getDisbursement());
            } catch (JsonProcessingException e) {
                throw new CustomException("PARSING_ERROR", "Error while parsing " + e.getMessage());
            }
             forwardMessage(disbursementRequest.getSignature(),
                    disbursementRequest.getHeader(), message);
        }
        log.info("Disbursement Successfully Dispatched");
        return null;
    }

    /**
     * Builds request message and forwards to exchange service
     * @param signature
     * @param requestHeader
     * @param message
     * @return
     */
    public Object forwardMessage(String signature, RequestHeader requestHeader, String message){
        log.info("Forwarding message");
        RequestMessage requestMessage = RequestMessage.builder()
                .header(requestHeader).signature(signature)
                .message(message).build();
        StringBuilder url = new StringBuilder(configs.getExchangeHost()).append(configs.getExchangePath())
                .append(requestMessage.getHeader().getMessageType().toString());
        return restRepo.fetchResult(url, requestMessage);
    }

    /**
     * Forwards message to adapter in case receiver id and domain are same
     * @param disbursementRequest
     * @return
     */
    public Object forwardToAdapter(DisbursementRequest disbursementRequest){
        StringBuilder url = new StringBuilder(configs.getAdapterHost()).append(configs.getAdapterPath())
                .append(disbursementRequest.getHeader().getMessageType().toString()).append("/_")
                .append(disbursementRequest.getHeader().getAction().toString());
        return restRepo.fetchResult(url, disbursementRequest);
    }
}
