package org.digit.program.validator;

import lombok.extern.slf4j.Slf4j;
import org.digit.program.constants.Action;
import org.digit.program.models.RequestHeader;
import org.digit.program.models.program.Program;
import org.digit.program.models.program.ProgramSearch;
import org.digit.program.repository.ProgramRepository;
import org.egov.tracer.model.CustomException;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
@Slf4j
public class CommonValidator {

    private final ProgramRepository programRepository;

    public CommonValidator(ProgramRepository programRepository) {
        this.programRepository = programRepository;
    }

    public void validateRequest(RequestHeader requestHeader, String action) {
        if (!action.equalsIgnoreCase(Action.SEARCH.toString()) && requestHeader.getReceiverId()
                .equals(requestHeader.getSenderId()))
            throw new CustomException("RECEIVER_ID_SENDER_ID_ERROR", "ReceiverId should not be same as SenderId");
        if (!requestHeader.getReceiverId().contains("@"))
            throw new CustomException("RECEIVER_ID_ERROR", "ReceiverId is wrong, it should have @");
        if (!requestHeader.getSenderId().contains("@"))
            throw new CustomException("SENDER_ID_ERROR", "SenderId is wrong, it should have @");
        if (!action.equalsIgnoreCase(requestHeader.getAction().toString())) {
            throw new CustomException("ACTION_ERROR", "Action in request header should be same as url");
        }
    }

    public void validateReply(RequestHeader requestHeader, String programCode, String locationCode) {
        List<Program> programs = programRepository.searchProgram(ProgramSearch.builder().programCode(programCode)
                .locationCode(locationCode).build());
        if (!requestHeader.getReceiverId().split("@")[1].equalsIgnoreCase(programs.get(0).getClientHostUrl()))
            throw new CustomException("RECEIVER_ID_CLIENT_HOST_URL_ERROR", "ReceiverId should be same as program client host url");
    }

    public void validateReplyForProgramCreate(RequestHeader requestHeader, String id, String locationCode) {
        List<Program> programs = programRepository.searchProgram(ProgramSearch.builder().ids(Collections.singletonList(id))
                .locationCode(locationCode).build());
        if (!requestHeader.getReceiverId().split("@")[1].equalsIgnoreCase(programs.get(0).getClientHostUrl()))
            throw new CustomException("RECEIVER_ID_ERROR", "ReceiverId should be same as program client host url");
    }
}
