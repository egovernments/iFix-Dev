package org.egov.util;

import org.egov.common.contract.request.RequestHeader;
import org.egov.common.contract.response.ResponseHeader;
import org.springframework.stereotype.Component;


@Component
public class ResponseHeaderCreator {

    public ResponseHeader createResponseInfoFromRequestInfo(final RequestHeader requestInfo, final Boolean success) {

        final String correlationId = requestInfo != null ? requestInfo.getCorrelationId() : "";
        final String ver = requestInfo != null ? requestInfo.getVersion() : "";
        Long ts = null;
        if (requestInfo != null)
            ts = requestInfo.getTs();
        final String sign = requestInfo.getSignature()!= null ? requestInfo.getSignature() : "" ;
        final String msgId = requestInfo != null ? requestInfo.getMsgId() : "";
        final String responseStatus = success ? "successful" : "failed";

        return ResponseHeader.builder().version(ver).ts(ts).msgId(msgId).correlationId(correlationId).signature(sign)
                .status(responseStatus).build();
    }
}
