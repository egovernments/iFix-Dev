package org.egov.xtra.key;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.Security;
import java.util.Base64;

public class EgovKeyGenerator {

    SecureRandom secureRandom;

    public EgovKeyGenerator() {
        Security.addProvider(new BouncyCastleProvider());
        secureRandom = new SecureRandom();
    }

    //Generate random bytes with use of SecureRandom
    //Being used to generate Initial Vector and Symmetric Key
    private byte[] getRandomBytes(int size) {
        byte[] randomBytes = new byte[size];
        secureRandom.nextBytes(randomBytes);
        return randomBytes;
    }

    public static String genAES256Key() throws NoSuchAlgorithmException {
        final KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(256);
        // Generate Key
        final SecretKey key		= keyGenerator.generateKey();
        final String	keyText	= Base64.getEncoder().encodeToString(key.getEncoded());
        return keyText;
    }

}
