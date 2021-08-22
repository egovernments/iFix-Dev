package org.egov.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.JsonNode;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.security.KeyFactory;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;

@Configuration
public class KeycloakJwtVerifierConfig {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Value("${keycloak.host}")
    private String keycloakHost;
    @Value("${keycloak.context-path}")
    private String keycloakContextPath;
    @Value("${keycloak.realm}")
    private String keycloakRealmName;
    @Autowired
    private RestTemplate restTemplate;
    private JWTVerifier jwtVerifier;

    @Bean
    public JWTVerifier jwtVerifier() {
        String url = keycloakHost + keycloakContextPath + "/realms/" + keycloakRealmName;
        JsonNode realmDetails = restTemplate.getForObject(url, JsonNode.class);
        String publicKey = realmDetails.get("public_key").asText();
        JWTVerifier jwtVerifier = null;
        try {
            byte[] encoded = Base64.decodeBase64(publicKey);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            RSAPublicKey pubKey = (RSAPublicKey) kf.generatePublic(new X509EncodedKeySpec(encoded));

            Algorithm algorithm = Algorithm.RSA256(pubKey, null);
            jwtVerifier = JWT.require(algorithm).build();
        } catch (Exception e) {
            logger.error("Failed to create JWT Verifier");
        }
        return jwtVerifier;
    }

}
