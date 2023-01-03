package org.egov.repository;

import org.egov.config.PspclIfixAdapterConfiguration;
import org.egov.model.KeyCloackData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.MalformedURLException;

import static org.egov.repository.AuthTokenRepository.*;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpMethod.POST;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AuthTokenRepositoryTest {

    @Spy
    private RestTemplate restTemplate;

    @Mock
    private PspclIfixAdapterConfiguration adapterConfiguration;

    private AuthTokenRepository authTokenRepository;

    @BeforeEach
    private void init() throws IOException {
        MockitoAnnotations.openMocks(this);
        authTokenRepository = new AuthTokenRepository(restTemplate, adapterConfiguration);
    }

    @Test
    void getAuthTokenWithNullUriAndReq() {
        assertThrows(IllegalArgumentException.class, () -> authTokenRepository.getAuthToken());
    }

    @Test
    void getAuthTokenWithUriAndReq() {
        doReturn("localhost").when(adapterConfiguration).getKeyCloakHost();
        doReturn("/auth/token").when(adapterConfiguration).getKeyCloakAuthApi();
        doReturn(CLIENT_ID).when(adapterConfiguration).getClientId();
        doReturn(CLIENT_SECRETE).when(adapterConfiguration).getClientSecret();
        doReturn(GRANT_TYPE).when(adapterConfiguration).getGrantType();

        doThrow(new RestClientException("rest client exception")).when(restTemplate)
                .exchange(any(), any(), any(), (Class<KeyCloackData>) any());
        assertThrows(RuntimeException.class, () -> authTokenRepository.getAuthToken());
    }

    @Test
    void getAuthToken() throws MalformedURLException {
        doReturn("http://localhost").when(adapterConfiguration).getKeyCloakHost();
        doReturn("/auth/token").when(adapterConfiguration).getKeyCloakAuthApi();
        doReturn(CLIENT_ID).when(adapterConfiguration).getClientId();
        doReturn(CLIENT_SECRETE).when(adapterConfiguration).getClientSecret();
        doReturn(GRANT_TYPE).when(adapterConfiguration).getGrantType();

        ResponseEntity<KeyCloackData> response = new ResponseEntity<>(HttpStatus.OK);

        HttpEntity httpEntity = mock(HttpEntity.class);

        String uri = "http://localhost/auth/token";
        HttpMethod httpMethod = POST;

        doReturn(response).when(restTemplate)
                .exchange(uri, httpMethod, httpEntity, KeyCloackData.class);

        assertThrows(RuntimeException.class, () -> authTokenRepository.getAuthToken());
    }
}