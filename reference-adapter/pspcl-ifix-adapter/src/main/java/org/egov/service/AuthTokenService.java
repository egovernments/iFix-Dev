package org.egov.service;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.egov.model.KeyCloackData;
import org.egov.repository.AuthTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Date;


@Service
@Slf4j
@AllArgsConstructor
@NoArgsConstructor
public class AuthTokenService {

    @Autowired
    private AuthTokenRepository authTokenRepository;

    private KeyCloackData data;

    @PostConstruct
    protected void getAuthTokenAtStart() {
        data = authTokenRepository.getAuthToken();
        log.info("keycloak data loaded!!");
    }

    public String getAuthToken() {
        if (data != null && data.getExpires_in() > 0) {
            long now = new Date().getTime();
            if (now <= (data.getExpires_in())) {
                //log.info("keycloak detail : {} , {}", data.getAccess_token(), data.getToken_type());
                return data.getAccess_token();
            }
        }
        data = authTokenRepository.getAuthToken();
        return data.getAccess_token();
    }
}
