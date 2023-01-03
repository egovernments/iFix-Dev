package org.egov.ifix.service;

import lombok.extern.slf4j.Slf4j;
import org.egov.ifix.cache.AdapterCache;
import org.egov.ifix.exception.GenericCustomException;
import org.egov.ifix.models.KeyCloackData;
import org.egov.ifix.models.mgramseva.OauthSuccessResponseDTO;
import org.egov.ifix.repository.AuthTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.concurrent.TimeUnit;

import static org.egov.ifix.utils.EventConstants.*;

/**
 * @author mani
 * This will provide authtoken. First time it will ask repository for authtoken .
 * Once token received from repository it will cache it . Cache expiry is set to 5 minutes before the
 * token expiry.
 */
@Service
@Slf4j
public class AuthTokenService {

    @Autowired
    private AuthTokenRepository authTokenRepository;
    @Autowired
    private AdapterCache<KeyCloackData> adapterCache;
    @Autowired
    private AdapterCache<OauthSuccessResponseDTO> oauthSuccessResponseDTOAdapterCache;

    /**
     * @return authtoken
     * First search in cache . if found return else ask repository to provide .
     * Cache in redis with expiry time .
     */

    public String getKeyCloakAuthToken() {

        KeyCloackData keyCloakAuthTokenData = adapterCache.getValue(CACHED_KEYCLOAK_AUTH_TOKEN);
        if (keyCloakAuthTokenData == null) {
            keyCloakAuthTokenData = authTokenRepository.getKeyCloakAuthToken();

            if (keyCloakAuthTokenData == null || StringUtils.isEmpty(keyCloakAuthTokenData.getAccess_token())) {
                log.error("Unable to get auth token from keycloak end point");
                throw new GenericCustomException(ACCESS_TOKEN, "Unable to get auth token from keycloak end point");

            } else if (keyCloakAuthTokenData.getExpires_in() <= 0) {
                log.error("Invalid expiry time in auth token from keycloak");
                throw new GenericCustomException(ACCESS_TOKEN, "Invalid expiry time in auth token from keycloak");

            } else {
                adapterCache.putValueWithExpireTime(CACHED_KEYCLOAK_AUTH_TOKEN, keyCloakAuthTokenData,
                        keyCloakAuthTokenData.getExpires_in(), TimeUnit.MILLISECONDS);
            }
        } else {
            log.info("got token from Cache");
        }
        return keyCloakAuthTokenData.getAccess_token();
    }

    /**
     * @return
     */
    public String getMgramsevaAccessToken() {
        OauthSuccessResponseDTO oauthAccessTokenData = oauthSuccessResponseDTOAdapterCache
                .getValue(CACHED_MGRAMSEVA_ACCESS_TOKEN);

        if (oauthAccessTokenData == null) {
            oauthAccessTokenData = authTokenRepository.getMgramsevaOauthAccessToken();

            if (oauthAccessTokenData == null || StringUtils.isEmpty(oauthAccessTokenData.getAccessToken())) {
                log.error("Unable to get access token from mgramseva oauth end point");
                throw new GenericCustomException(ACCESS_TOKEN, "Unable to get access token from mgramseva oauth");
            } else if (oauthAccessTokenData.getExpiresIn() <= 0) {
                log.error("Invalid expiry time in access token of mgramseva oauth");
                throw new GenericCustomException(ACCESS_TOKEN, "Invalid expiry time in access token of mgramseva oauth");
            } else {
                oauthSuccessResponseDTOAdapterCache.putValueWithExpireTime(CACHED_MGRAMSEVA_ACCESS_TOKEN,
                        oauthAccessTokenData, oauthAccessTokenData.getExpiresIn(), TimeUnit.SECONDS);
            }
        } else {
            log.info("Received access token from redis cache server: ");
        }

        return oauthAccessTokenData.getAccessToken();
    }

}
