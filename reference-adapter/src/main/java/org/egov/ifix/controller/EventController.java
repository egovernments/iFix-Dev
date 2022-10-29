package org.egov.ifix.controller;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.egov.common.contract.response.ResponseHeader;
import org.egov.ifix.models.Event;
import org.egov.ifix.models.EventRequest;
import org.egov.ifix.models.EventResponse;
import org.egov.ifix.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class EventController {
	
	@Autowired
	private EventService eventService;
	
	@PostMapping("/events/v1/_push")
	public ResponseEntity<?>pushEvents( @Valid @RequestBody EventRequest eventRequest) {
		log.debug("request received");
		eventService.pushEvent(eventRequest);
		EventResponse response=new EventResponse();
		List<Event> events=new ArrayList<>();
		events.add(eventRequest.getEvent());
		response.setEvent(events); 
		response.setResponseInfo(getResponseInfo(eventRequest));
		
		
		return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
	}

	private ResponseHeader getResponseInfo(EventRequest eventRequest) {
		ResponseHeader responseInfo=new ResponseHeader();
		responseInfo.setCorrelationId(eventRequest.getRequestHeader().getCorrelationId());
		responseInfo.setTs(Instant.now().toEpochMilli());
		responseInfo.setVersion(eventRequest.getRequestHeader().getVersion());
		responseInfo.setStatus(HttpStatus.ACCEPTED.toString());
		responseInfo.setMsgId(eventRequest.getRequestHeader().getMsgId());
		return responseInfo;
	}
	
}
