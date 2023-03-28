package org.egov.ifms;

import lombok.extern.slf4j.Slf4j;
import org.egov.config.IfmsAdapterConfig;
import org.egov.key.KeyGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.security.NoSuchAlgorithmException;

@Slf4j
@Component
public class IfmsAuthenticator {

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private IfmsAdapterConfig ifmsAdapterConfig;

    @Value("${ifms.jit.hostname}")
    private String ifmsJitHostname;
    @Value("${ifms.jit.authenticate.endpoint}")
    private String authenticateEndpoint;
    @Value("${ifms.jit.client.id}")
    private String clientId;
    @Value("${ifms.jit.client.secret}")
    private String clientSecret;

    public void refreshTokens() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("clientId", clientId);
        httpHeaders.add("clientSecret", clientSecret);


    }

    private String getEncryptedAppKey() throws NoSuchAlgorithmException {

        return null;
    }

}
