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
        String ciphertext = "Y6XemYcCMUmsx9GI0OCFyizhAuFmSOkbM64R1F+ZyAxsOpqKf5HoxG9Lg+pd8HF98nvBX0nNd5MrJl/uXY5sf+mCd0ZJQyb171FTWW1IBBxJ6YAYcFRpFwtyUVgrdBJdu46WY/9+PljKuhsKS8QhjsIqHJsfcf83VBfFC2kNC0dDG0SUhkHdUMKe2sZoDtp/FoeYFVtzPLnjjnANTRYOKCXJz3Z+dZxYU318q2dHY0devzTRoe48MUm7unaGCBwJW/nIdAZmG4KwbhuvgihEyzmPwflwmiC6k3PmDdKfte7CDOgMctcmcQmw8+Swq2dT/uuFKOV4VN+hNSrpXVUIX63Y54UxV3DIRFbJTbrapbGikQhI/VicllYpoSn7Fbp11wjkqlPGoix8jCGKmLPz4Visj2HSrGjhI/TWMEyoFOwWjFY/jqVwUj0gIr9HK+KR1zJjHAr8y9PQvrSGBibEU2IibapLbuvWH/ghfw/UPwGRcPRDO8EZkQaxACFtCeG+Oi7DzRF7OcDzGbYwD5v+yyK2Vtrn+ez+9EjHoxRjbt3Qs/bn7ERBWF7eyKQh11lHKL1xMf8P9hYEvnqT61OE5lP8JmZtugZvCsr+QYiAwZ+AuW0nXvqol8J/29aTplWJiXQxrSVnNlruHghtr1eyK5Ah9s2+TYBSc/cFzrHHS3k=";
        byte[] secret = Base64.getDecoder().decode(secretKey);
        String plaintext = SymmetricEncryptionService.decrypt(ciphertext, secret);
        plaintext = new String(Base64.getDecoder().decode(plaintext));
        System.out.println(plaintext);
    }

    @Test
    public void testEncrypt() throws Exception {
        String secretKey = "6WeGtuYkrZslUFzKo8g8JUkMEivFZPtcVUVoppqw3xc=";
        byte[] sekBytes = Base64.getDecoder().decode(secretKey);
        final byte[] fileBytes = Files.readAllBytes(Paths.get(baseURL + requestFilePath));
        String requestBody = new String(fileBytes);
        System.out.println(requestBody);
        byte[] plainBytes = requestBody.getBytes();
        String ciphertext = SymmetricEncryptionService.encrypt(plainBytes, sekBytes);
        System.out.println(ciphertext);
        String rek = "e1ro8WIs7BsGYwHtBbTWAWrmR/yzJSCoksOaMmOO0SI=";
        byte[] rekBytes = Base64.getDecoder().decode(rek);
        String cipherKey = SymmetricEncryptionService.encrypt(rekBytes, sekBytes);
        System.out.println(cipherKey);
    }

    @Test
    public void testAESKeyGen() throws NoSuchAlgorithmException {
        String rek = EgovKeyGenerator.genAES256Key();
        System.out.println(rek);
    }

}