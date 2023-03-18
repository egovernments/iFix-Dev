package org.egov.enc;

import org.egov.xtra.enc.SymmetricEncryptionService;
import org.egov.xtra.key.EgovKeyGenerator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SymmetricEncryptionServiceTest {

    private String baseURL;
    private String requestFilePath = "request.json";

    @BeforeAll
    public void init() {
        ClassLoader classLoader = this.getClass().getClassLoader();
        baseURL = classLoader.getResource("").getFile();
    }

    @Test
    public void testDecrypt() throws Exception {
        String secretKey = "e1ro8WIs7BsGYwHtBbTWAWrmR/yzJSCoksOaMmOO0SI=";
//        secretKey = "2LIJU1wc0zxuTcoSDlV0WvJJWG08cRhQT6sCwoNyPJI=";
        String ciphertext = "NKxAhfPi3ZM9yXhckVlgQgifrTLH3NNqkBptDcC53MYRh9nf41IeJ+3NK/2RzjFy";
        byte[] secret = Base64.getDecoder().decode(secretKey);
        String plaintext = SymmetricEncryptionService.decrypt(ciphertext, secret);
        plaintext = new String(Base64.getDecoder().decode(plaintext));
        System.out.println(plaintext);
    }

    @Test
    public void testEncrypt() throws Exception {
        String secretKey = "cHoShoa21tYpn4i9nt+oq2ERYROQNcqiTzel4AANo/M=";
        byte[] sekBytes = Base64.getDecoder().decode(secretKey);
        final byte[] fileBytes = Files.readAllBytes(Paths.get(baseURL + requestFilePath));
        String requestBody = new String(fileBytes);
        System.out.println(requestBody);
        byte[] plainBytes = requestBody.getBytes();
        String ciphertext = SymmetricEncryptionService.encrypt(plainBytes, sekBytes);
        System.out.println(ciphertext);
//        String rek = "e1ro8WIs7BsGYwHtBbTWAWrmR/yzJSCoksOaMmOO0SI=";
//        byte[] rekBytes = Base64.getDecoder().decode(rek);
//        String cipherKey = SymmetricEncryptionService.encrypt(rekBytes, sekBytes);
//        System.out.println(cipherKey);
    }

    @Test
    public void testAESKeyGen() throws NoSuchAlgorithmException {
        String rek = EgovKeyGenerator.genAES256Key();
        System.out.println(rek);
    }

}