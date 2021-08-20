package org.egov.filters.pre;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.JsonNode;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.StringUtils;
import org.egov.Utils.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.RestTemplate;

import java.security.KeyFactory;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;

import static org.egov.constants.RequestContextConstants.*;

public class JwtAuthenticationFilter extends ZuulFilter {

    private static final String INVALID_JWT = "Invalid JWT";
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private JWTVerifier jwtVerifier;

    public JwtAuthenticationFilter(JWTVerifier jwtVerifier) {
        this.jwtVerifier = jwtVerifier;
    }

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 3;
    }

    @Override
    public boolean shouldFilter() {
        return RequestContext.getCurrentContext().getBoolean(AUTH_BOOLEAN_FLAG_NAME);
    }

    @Override
    public Object run() throws ZuulException {
        RequestContext context = RequestContext.getCurrentContext();
        String authToken = (String) context.get(AUTH_TOKEN_KEY);
        DecodedJWT decodedJWT = null;

        try {
            decodedJWT = jwtVerifier.verify(authToken);
        } catch (Exception ex) {
            logger.error(INVALID_JWT, ex);
            ExceptionUtils.raiseCustomException(HttpStatus.UNAUTHORIZED, "Verifying JWT failed");
        }

        String payload = StringUtils.newStringUtf8(Base64.decodeBase64(decodedJWT.getPayload()));
        context.set(JWT_PAYLOAD_KEY, payload);
        return null;
    }

}
