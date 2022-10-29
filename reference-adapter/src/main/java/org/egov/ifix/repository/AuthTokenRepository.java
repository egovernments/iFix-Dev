package org.egov.ifix.repository;

import org.egov.ifix.models.KeyCloackData;
import org.egov.ifix.utils.ApplicationConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class AuthTokenRepository {
	
	@Autowired
	RestTemplate restTemplate;
	
	@Autowired
	ApplicationConfiguration applicationConfiguration;
	

	public KeyCloackData getAuthToken()
	{
		
		log.debug("getting token from ifix .........");
		
		String url = applicationConfiguration.getKeyCloakHost()
				+ applicationConfiguration.getKeyCloakAuthApi();

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

		MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
		map.add("client_id", applicationConfiguration.getClientId());
		map.add("client_secret", applicationConfiguration.getClientSecret());
		map.add("grant_type", applicationConfiguration.getGrantType());

		HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(map, headers);

		ResponseEntity<KeyCloackData> response = null;
		try {

			response = restTemplate.exchange(url, HttpMethod.POST, entity, KeyCloackData.class);
		} catch (RestClientException e) {
			log.error(e.getMessage(),e);
			throw new RuntimeException("Unable to get authtoken from keycloak");
		}
		log.info("authtoken" +response.getBody().getAccess_token());
		return response.getBody();
			 
		}
		
	}


