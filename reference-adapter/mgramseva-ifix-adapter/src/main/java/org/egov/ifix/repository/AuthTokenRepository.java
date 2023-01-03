package org.egov.ifix.repository;

import lombok.extern.slf4j.Slf4j;
import org.egov.ifix.exception.HttpCustomException;
import org.egov.ifix.models.KeyCloackData;
import org.egov.ifix.models.mgramseva.OauthSuccessResponseDTO;
import org.egov.ifix.utils.ApplicationConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Repository;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

import static org.egov.ifix.utils.EventConstants.*;

@Repository
@Slf4j
public class AuthTokenRepository {

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    ApplicationConfiguration applicationConfiguration;


    /**
     * @return
     */
    public KeyCloackData getKeyCloakAuthToken() {
        String url = applicationConfiguration.getKeyCloakHost()
                + applicationConfiguration.getKeyCloakAuthApi();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add(CLIENT_ID, applicationConfiguration.getClientId());
        map.add(CLIENT_SECRET, applicationConfiguration.getClientSecret());
        map.add(GRANT_TYPE, applicationConfiguration.getGrantType());

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(map, headers);

        ResponseEntity<KeyCloackData> response = null;
        try {
            response = restTemplate.exchange(url, HttpMethod.POST, entity, KeyCloackData.class);
        } catch (RestClientException e) {
            log.error(e.getMessage(), e);
            throw new HttpCustomException(KEYCLOAK_AUTH_TOKEN, "Unable to get auth-token from keycloak: ",
                    e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return response.getBody();
    }


    /**
     * @return
     */
    public OauthSuccessResponseDTO getMgramsevaOauthAccessToken() {
        String url = applicationConfiguration.getMgramsevaHost()
                + applicationConfiguration.getMgramsevaOauthAccessTokenEndpoint();
        ;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setAccept(Collections.singletonList(MediaType.ALL));
        headers.setAccessControlAllowOrigin("*");
        headers.setOrigin(applicationConfiguration.getMgramsevaHost());
        headers.setBasicAuth(applicationConfiguration.getMgramsevaBasicAuthorizationBase64Value());

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add(USERNAME, applicationConfiguration.getMgramsevaOauthTokenUsername());
        map.add(PASSWORD, applicationConfiguration.getMgramsevaOauthTokenPassword());
        map.add(SCOPE, applicationConfiguration.getMgramsevaOauthTokenScope());
        map.add(GRANT_TYPE, applicationConfiguration.getMgramsevaOauthTokenGrantType());
        map.add(TENANT_ID, applicationConfiguration.getMgramsevaOauthTokenTenantId());
        map.add(USER_TYPE, applicationConfiguration.getMgramsevaOauthTokenUserType());

        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(map, headers);

        ResponseEntity<OauthSuccessResponseDTO> response = null;
        try {

            response = restTemplate.exchange(url, HttpMethod.POST, httpEntity, OauthSuccessResponseDTO.class);
        } catch (RestClientException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException("Unable to get access token form mgramseva oauth");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException("Unable to get access token form mgramseva oauth");
        }
        return response.getBody();
    }
}


