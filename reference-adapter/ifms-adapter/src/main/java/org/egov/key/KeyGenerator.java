package org.egov.key;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.Security;
import java.util.Base64;

public class KeyGenerator {

    SecureRandom secureRandom;

    public KeyGenerator() {
        Security.addProvider(new BouncyCastleProvider());
        secureRandom = new SecureRandom();
    }

    public static String genAES256Key() throws NoSuchAlgorithmException {
        final javax.crypto.KeyGenerator keyGenerator = javax.crypto.KeyGenerator.getInstance("AES");
        keyGenerator.init(256);
        final SecretKey secretKey = keyGenerator.generateKey();
        final String key = Base64.getEncoder().encodeToString(secretKey.getEncoded());
        return key;
    }

}
