package org.egov.service;

import org.egov.model.KeyCloackData;
import org.egov.repository.AuthTokenRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.doReturn;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AuthTokenServiceTest {

    @Mock
    private AuthTokenRepository authTokenRepository;

    private AuthTokenService authTokenService;

    private KeyCloackData keyCloackData;

    @BeforeEach
    private void init() throws IOException {
        MockitoAnnotations.openMocks(this);
        authTokenService = new AuthTokenService(authTokenRepository,keyCloackData);

        keyCloackData = new KeyCloackData();
        keyCloackData.setAccess_token("tyeu3irowed csd mcksldf;ew;kf;ke;fwf ewv kekwlllllllllllllllllllvdcmsfnlmd");
        keyCloackData.setClient_id("client_id");
        keyCloackData.setClient_secret("client_secret");

    }

    @Test
    void getAuthToken() {
        doReturn(keyCloackData).when(authTokenRepository).getAuthToken();
        assertNotNull(authTokenService.getAuthToken());
    }
}