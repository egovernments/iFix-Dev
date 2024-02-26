package org.digit.program.validator;

import lombok.extern.slf4j.Slf4j;
import org.digit.program.constants.Action;
import org.digit.program.models.RequestHeader;
import org.digit.program.models.program.Program;
import org.digit.program.models.program.ProgramSearch;
import org.digit.program.repository.ProgramRepository;
import org.digit.program.utils.CommonUtil;
import org.egov.tracer.model.CustomException;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
@Slf4j
public class CommonValidator {

    private final ProgramRepository programRepository;
    private final CommonUtil commonUtil;

    public CommonValidator(ProgramRepository programRepository, CommonUtil commonUtil) {
        this.programRepository = programRepository;
        this.commonUtil = commonUtil;
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
     * @param programCode
     * @param locationCode
     */
    public void validateReply(RequestHeader requestHeader, String programCode, String locationCode) {
        List<Program> programs = programRepository.searchProgram(ProgramSearch.builder().programCode(programCode)
                .locationCode(locationCode).build());
        if (!commonUtil.isSameDomain(requestHeader.getReceiverId(), programs.get(0).getClientHostUrl()))
            throw new CustomException("RECEIVER_ID_CLIENT_HOST_URL_ERROR", "ReceiverId should be same as program client host url");
    }

    /**
     * Validates if reply is for same domain present in program
     * @param requestHeader
     * @param id
     * @param locationCode
     */
    public void validateReplyForProgramCreate(RequestHeader requestHeader, String id, String locationCode) {
        log.info("Validating Reply");
        List<Program> programs = programRepository.searchProgram(ProgramSearch.builder().ids(Collections.singletonList(id))
                .locationCode(locationCode).build());
        if (!commonUtil.isSameDomain(requestHeader.getReceiverId(), programs.get(0).getClientHostUrl()))
            throw new CustomException("RECEIVER_ID_CLIENT_HOST_URL_ERROR", "ReceiverId should not be same as program client host url");
    }
}
