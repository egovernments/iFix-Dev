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
    public void testDecryptSek() throws Exception {
        String appKey = "ysftrEbv577GMbhflKmstlwExjTHp7jJAf2p5AOSRzA=";
        String ciphertext = "+FXzHTLAw3/MeeAUtcfRPaLwTJa81DMBzFoVFwH86Si+pYVJh7ZIZy7wmXdPkYTy";
        byte[] secret = Base64.getDecoder().decode(appKey);
        String sek = SymmetricEncryptionService.decrypt(ciphertext, secret);
        System.out.println(sek);
    }

    @Test
    public void testDecryptResponse() throws Exception {
        String rek = "e1ro8WIs7BsGYwHtBbTWAWrmR/yzJSCoksOaMmOO0SI=";
        String ciphertext = "DIJJS8vuKbC15JdvMv7awk2gSDS84f1vin8DTSWUcSxFTHGvvTuGY9BXPwsaKMlf/fjagr6S8JtYwACjIYtFpWlzJZNHJlEYlPIuXagjnUWIPO2rRcNcaaN5go/R6U+TofkKB2XfKh9KPxvTQ1K6xEKMvnZxpOOdDA3bN3LTiOllNwBJ2llqGxI+bOgAdsYUDfNMfSkPXF2kI+1Ck9w4wcwXJNwJgPoXh6D+xT3SCgd5lFd4SxwHPu1kKR7qm179HS8v1utOH0whnpgtv5jpH7e0Wr+8CzTnFDMJQ7loAMJvwau3AdxwpDes0T1R0RHNumhQ/gIixyl54CHhsZMFPgbcjNnS9SMEI6qadq/+wnYTFWtxrgtsqkmRQFC42itr+LoF2tCxzSo2nKAIZeSsLLdomK+setAcIWcg/U0CPcqFwwCgLaoe2PS4HbjJFMyGzBj9J7gX7ED2I8yRQIqDohW6cla5zhdsEOzOc+Ft7nNqy/cKtt7cwch5ufUMy39iSWI82xxryNGBaPEOrfzz2/4QMS0ltiIHV6XchPGd5cxfUA1jvlaDIBEkHWcySkCFWoS/5a+Pfa7+AkXqjcE4FJE+ArCVsNXX7VF+cXaifPQ=";
        byte[] secret = Base64.getDecoder().decode(rek);
        String plaintext = SymmetricEncryptionService.decrypt(ciphertext, secret);
        plaintext = new String(Base64.getDecoder().decode(plaintext));
        System.out.println(plaintext);
    }

    @Test
    public void testEncrypt() throws Exception {
        String secretKey = "yzHVLkkBehjItj4NKl5k9vn8Ab8w5M3LWh5UN9vuo7U=";
        byte[] sekBytes = Base64.getDecoder().decode(secretKey);
        final byte[] fileBytes = Files.readAllBytes(Paths.get(baseURL + "request.json"));
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