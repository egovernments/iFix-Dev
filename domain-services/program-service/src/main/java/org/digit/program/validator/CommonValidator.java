package org.digit.program.validator;

import lombok.extern.slf4j.Slf4j;
import org.digit.program.models.RequestHeader;
import org.egov.tracer.model.CustomException;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CommonValidator {

    public void validateRequest(RequestHeader requestHeader) {
        if (requestHeader.getSenderId() == null || requestHeader.getSenderId().isEmpty())
            throw new CustomException("SENDER_ID_ERROR", "SenderId should not be empty");
        if (requestHeader.getReceiverId() == null || requestHeader.getReceiverId().isEmpty())
            throw new CustomException("RECEIVER_ID_ERROR", "ReceiverId should not be empty");
        if (requestHeader.getSenderUri() == null || requestHeader.getSenderUri().isEmpty())
            throw new CustomException("SENDER_URI_ERROR", "SenderUri should not be empty");
        if (requestHeader.getReceiverId().equals(requestHeader.getSenderId()))
            throw new CustomException("RECEIVER_ID_ERROR", "ReceiverId should not be same as SenderId");
    }

}
