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
        String appKey = "O1OmNTLviO7uJWmMZId5xANb82VUJyz0v/NenSTFMQY=";
        String ciphertext = "jKN01/HzjzOYWQ2x2tgLX9bG9VwnL6gD5ihNdnGNWw6cd0/tW0ga/WDVpsT0odam";
        byte[] secret = Base64.getDecoder().decode(appKey);
        String sek = SymmetricEncryptionService.decrypt(ciphertext, secret);
        System.out.println(sek);
    }

    @Test
    public void testDecryptResponse() throws Exception {
        String secretKey = "e1ro8WIs7BsGYwHtBbTWAWrmR/yzJSCoksOaMmOO0SI=";
        String ciphertext = "D3JF6tvSW7vA91ysLUMO2RgwkcuLRklo/46NUoOWvxWhl4Z3eWfy3CRMyK5nn9Hqd3//w+OCKvFN92O9rkMB0VftuAALS131CYdcMabgI/nzk4YhGpmzAHryuRUBJeOboov/m+j/PCPtzyRqsDXum/ZFufsdg8lTt5pOjEvGQgIlf2/qqU5AN8/AmxZPm9FRmxWiKFPdbiHVmEock3VDkwfJ8BTUdofIppMlM7fUbWn1PHltvS8sA2fa5UnVkDgGMCQralurBr4VurUB9VUHwrOgmW1QaB3AiD6JpMeSpbu2zpJbaJnlnGy6VarnQCOJmRFYpj9vk0wgVKJyKhCSsg==";
        byte[] secret = Base64.getDecoder().decode(secretKey);
        String plaintext = SymmetricEncryptionService.decrypt(ciphertext, secret);
        plaintext = new String(Base64.getDecoder().decode(plaintext));
        System.out.println(plaintext);
    }

    @Test
    public void testEncrypt() throws Exception {
        String secretKey = "rL0B3Mg2Mvhd81sc35c2u2mapQz/A0VfrWoAR6IUzHY=";
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