package org.egov.xtra.enc;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class SymmetricEncryptionService {

    public static String decrypt(final String cypherText, final byte[] secret)
            throws Exception {
        final SecretKeySpec secretKey = new SecretKeySpec(secret, "AES");
        final Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        return Base64.getEncoder().encodeToString(cipher.doFinal(Base64.getDecoder().decode(cypherText)));
    }

}
