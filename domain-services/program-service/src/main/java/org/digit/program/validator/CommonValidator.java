package org.digit.program.validator;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.digit.program.constants.Action;
import org.digit.program.models.RequestHeader;
import org.digit.program.repository.ProgramRepository;
import org.digit.program.utils.CommonUtil;
import org.digit.program.utils.MdmsUtil;
import org.egov.mdms.model.MdmsResponse;
import org.egov.tracer.model.CustomException;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CommonValidator {
    private final CommonUtil commonUtil;
    private final MdmsUtil mdmsUtil;
    private final ObjectMapper mapper;

    public CommonValidator(CommonUtil commonUtil, MdmsUtil mdmsUtil, ObjectMapper mapper) {
        this.commonUtil = commonUtil;
        this.mdmsUtil = mdmsUtil;
        this.mapper = mapper;
    }

    /**
     * Validates request header
     * @param requestHeader
     * @param action
     * @param messageType
     */
    public void validateRequest(RequestHeader requestHeader, String action, String messageType) {
        log.info("Validating request header");
        if (!action.equalsIgnoreCase(Action.SEARCH.toString()) && requestHeader.getReceiverId()
                .equals(requestHeader.getSenderId()))
            throw new CustomException("RECEIVER_ID_SENDER_ID_ERROR", "ReceiverId should not be same as SenderId");
        if (!requestHeader.getReceiverId().contains("@"))
            throw new CustomException("RECEIVER_ID_ERROR", "ReceiverId is wrong, it should have @");
        if (!requestHeader.getSenderId().contains("@"))
            throw new CustomException("SENDER_ID_ERROR", "SenderId is wrong, it should have @");
        if (!action.equalsIgnoreCase(requestHeader.getAction().toString()))
            throw new CustomException("ACTION_ERROR", "Action in request header should be same as url");
        if (!messageType.equalsIgnoreCase(requestHeader.getMessageType().toString()))
            throw new CustomException("MESSAGE_TYPE_ERROR", "MessageType in request header should be same as url");
        log.debug("Validated request header for {} {}", messageType, action);
    }

    /**
     * Validates if reply is for same domain present in program
     * @param requestHeader
     * @param locationCode
     */
    public void validateReply(RequestHeader requestHeader, String locationCode) {
        MdmsResponse mdmsResponse = mdmsUtil.callMdms(locationCode);
        JsonNode node = mapper.convertValue(mdmsResponse.getMdmsRes().get("exchange").get("ExchangeServers").get(0), JsonNode.class);
        if (!commonUtil.isSameDomain(node.get("hostUrl").asText(), requestHeader.getReceiverId())) {
            throw new CustomException("RECEIVER_ID_CLIENT_HOST_URL_ERROR", "ReceiverId should be same as client host url");
        }
    }
}
