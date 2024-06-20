package org.egov.ifms;

import lombok.extern.slf4j.Slf4j;
import org.egov.config.IfmsAdapterConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
public class IfmsAuthenticator {

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private IfmsAdapterConfig ifmsAdapterConfig;

    @Value("${}")
    private String jitAuthenticateUrl;

    public void login() {

    }

}
