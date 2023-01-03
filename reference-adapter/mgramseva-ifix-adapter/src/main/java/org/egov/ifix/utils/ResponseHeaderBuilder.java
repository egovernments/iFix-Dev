package org.egov.ifix.utils;

import org.egov.common.contract.request.RequestHeader;
import org.egov.common.contract.response.ResponseHeader;
import org.springframework.stereotype.Component;

@Component
public class ResponseHeaderBuilder {
	
	public ResponseHeader getResponseHeader(RequestHeader requestHeader) {
		return ResponseHeader.builder().correlationId(requestHeader.getCorrelationId()).
		msgId(requestHeader.getMsgId()).build();
		
	}

}
