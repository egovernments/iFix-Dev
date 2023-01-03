package org.egov.repository;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.egov.config.PspclIfixAdapterConfiguration;
import org.egov.model.KeyCloackData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Repository;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Repository
@Slf4j
@NoArgsConstructor
@AllArgsConstructor
public class AuthTokenRepository {

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    PspclIfixAdapterConfiguration adapterConfiguration;

    public static final String CLIENT_ID = "client_id";
    public static final String CLIENT_SECRETE = "client_secret";
    public static final String GRANT_TYPE = "grant_type";


    public KeyCloackData getAuthToken() {

        log.debug("getting token from keycloak .........");

        String url = adapterConfiguration.getKeyCloakHost() + adapterConfiguration.getKeyCloakAuthApi();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add(CLIENT_ID, adapterConfiguration.getClientId());
        map.add(CLIENT_SECRETE, adapterConfiguration.getClientSecret());
        map.add(GRANT_TYPE, adapterConfiguration.getGrantType());

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(map, headers);

        ResponseEntity<KeyCloackData> response = null;
        try {

            response = restTemplate.exchange(url, HttpMethod.POST, entity, KeyCloackData.class);
        } catch (RestClientException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException("Unable to get authtoken from keycloak");
        }
        log.debug("authtoken : " + (response != null && response.getBody() != null ? response.getBody().getAccess_token() : null));
        return response.getBody();

    }

}


