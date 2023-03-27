package org.egov.enc;

import org.egov.xtra.enc.AsymmetricEncryptionService;
import org.egov.xtra.key.EgovKeyGenerator;
import org.egov.xtra.key.PublicKeyLoader;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.util.Base64;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AsymmetricEncryptionServiceTest {

    PublicKey publicKey;
    String baseURL;
    EgovKeyGenerator egovKeyGenerator;

    @BeforeAll
    public void init() throws Exception {
        ClassLoader classLoader = this.getClass().getClassLoader();
        baseURL = classLoader.getResource("").getFile();
        publicKey = PublicKeyLoader.getPublicKeyFromByteFile(baseURL + "publicKey");
        egovKeyGenerator = new EgovKeyGenerator();
    }

    @Test
    public void genAppKey() throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException,
            BadPaddingException, InvalidKeyException {
        String appKey = EgovKeyGenerator.genAES256Key();
        System.out.println(appKey);
        String plaintext = appKey;
        byte[] plainBytes = Base64.getDecoder().decode(plaintext);
        String ciphertext = AsymmetricEncryptionService.encrypt(plainBytes, publicKey);
        System.out.println(ciphertext);
    }


}