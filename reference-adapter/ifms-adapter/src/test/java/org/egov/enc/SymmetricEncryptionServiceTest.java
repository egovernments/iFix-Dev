package org.egov.enc;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.file.Files;
import java.nio.file.Paths;
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
    public void testDecryptSek() throws Exception {
        String appKey = "nXIDx/4elkUv44ffXYCYm46B2IKfn+eh/wfAcuMS+m4=";
        String ciphertext = "Vq0sDLSlhrARucnWYmjvgAInWIhS/8tLX9ksJUQVUanEirZMxRf4kOBN9xCXoFua";
        SecretKey secretKey = new SecretKeySpec(Base64.getDecoder().decode(appKey), "AES");
        byte[] sek = SymmetricEncryptionService.decrypt(ciphertext, secretKey);
        String sekString = Base64.getEncoder().encodeToString(sek);
        System.out.println(sekString);
    }

    @Test
    public void testDecryptResponse() throws Exception {
        String rek = "e1ro8WIs7BsGYwHtBbTWAWrmR/yzJSCoksOaMmOO0SI=";
        String ciphertext = "0snte1FcOAtC0FmdQwnI2SzhAuFmSOkbM64R1F+ZyAy3BCLqg1szxUnpioa1qgAgnsMJsi/fGTPmLWtdXe2pKwgzbrmCfQvMZ9uhsq642LYVdxPW21D70didroc6v4GHkCwXggBII7vrFRS2ewxt0QH8Wlrk1HG+yrPldhqxop2caI5KUKKcUcREy3i6301r6H+CxRfq0WuZyXJre5ATdGcMmXv9yj2ELOX+v/+mjtoPtZJ3fCu5KeIw8GMLasm9";
        byte[] secret = Base64.getDecoder().decode(rek);
        SecretKey secretKey = new SecretKeySpec(secret, "AES");
        byte[] plainBytes = SymmetricEncryptionService.decrypt(ciphertext, secretKey);
        String plaintext = new String(plainBytes);
        System.out.println(plaintext);
    }

    @Test
    public void testEncrypt() throws Exception {
        String sekString = "h7b890EUMIrgv37UU24GQLpLr7emgpjQ2Qx1+3VJPoI=";
        byte[] sekBytes = Base64.getDecoder().decode(sekString);
        SecretKey secretKey = new SecretKeySpec(sekBytes, "AES");
        final byte[] fileBytes = Files.readAllBytes(Paths.get(baseURL + "request.json"));
        String requestBody = new String(fileBytes);
        System.out.println(requestBody);
        byte[] plainBytes = requestBody.getBytes();
        String ciphertext = SymmetricEncryptionService.encrypt(plainBytes, secretKey);
        System.out.println(ciphertext);
        String rek = "e1ro8WIs7BsGYwHtBbTWAWrmR/yzJSCoksOaMmOO0SI=";
        byte[] rekBytes = Base64.getDecoder().decode(rek);
        String cipherKey = SymmetricEncryptionService.encrypt(rekBytes, secretKey);
        System.out.println(cipherKey);
    }

}