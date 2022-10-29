package org.egov.ifix.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.egov.ifix.models.Event;
import org.egov.ifix.models.EventRequest;
import org.egov.ifix.models.EventResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public  class MasterDataController {
	
/*	@PostMapping("/events/v1/_push")
	public ResponseEntity<?>createPeject( @Valid @RequestBody ProjectRequest projectRequest) {
		log.debug("request received");
		
		
		return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
	}*/

}
