package org.egov.ifix.service;

import org.egov.ifix.models.FiscalEventRequest;
import org.egov.ifix.models.FiscalEventResponse;
import org.egov.ifix.models.KeyCloackData;
import org.egov.ifix.utils.ApplicationConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class PostEvent {

	@Autowired
	RestTemplate restTemplate;
	
	@Autowired
	AuthTokenService authTokenService;

	@Autowired
	ApplicationConfiguration applicationConfiguration;

	 
	 

	public ResponseEntity<FiscalEventResponse> post(FiscalEventRequest event) {

		log.info("Posting fiscal event  to IFix ");

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setBearerAuth(authTokenService.getAuthToken());
		ResponseEntity<FiscalEventResponse> response =null; 
		
		HttpEntity<FiscalEventRequest> request = new HttpEntity<>(event, headers);
		
		String url = applicationConfiguration.getIfixHost() + applicationConfiguration.getIfixEventApi();
		response = restTemplate.postForEntity(url, request, FiscalEventResponse.class);
	
		log.info("Posting to IFix status" +response.getStatusCode());

		return response;
	}

	 
	

}
