package org.egov.ifms;

import lombok.Getter;
import org.egov.xtra.key.PublicKeyLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import javax.crypto.spec.SecretKeySpec;
import java.security.PublicKey;

@Component
@Getter
public class IfmsCredentialAndKeyManager {

    @Value("${}")
    private String clientId;
    @Value("${}")
    private String clientSecret;

    private PublicKey publicKey;
    private String authToken;
    private SecretKeySpec secretKeySpec;

    @PostConstruct
    public void init() throws Exception {

    }

    public void initPublicKey() throws Exception {
        ClassLoader classLoader = this.getClass().getClassLoader();
        String basePath = classLoader.getResource("").getFile();
        publicKey = PublicKeyLoader.getPublicKeyFromByteFile(basePath + "publicKey");
    }

}
