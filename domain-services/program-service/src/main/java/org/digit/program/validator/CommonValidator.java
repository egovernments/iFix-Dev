package org.digit.program.validator;

import lombok.extern.slf4j.Slf4j;
import org.digit.program.models.RequestHeader;
import org.egov.tracer.model.CustomException;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CommonValidator {

    public void validateRequest(RequestHeader requestHeader) {
        if (requestHeader.getReceiverId().equals(requestHeader.getSenderId()))
            throw new CustomException("RECEIVER_ID_ERROR", "ReceiverId should not be same as SenderId");
        if (!requestHeader.getReceiverId().contains("@"))
            throw new CustomException("RECEIVER_ID_ERROR", "ReceiverId is wrong, it should have @");
        if (!requestHeader.getSenderId().contains("@"))
            throw new CustomException("SENDER_ID_ERROR", "SenderId is wrong, it should have @");
    }

}
