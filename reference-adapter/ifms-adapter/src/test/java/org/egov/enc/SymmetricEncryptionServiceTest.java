package org.egov.enc;

import org.egov.xtra.enc.SymmetricEncryptionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.Base64;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SymmetricEncryptionServiceTest {

    @Test
    public void testDecrypt() throws Exception {
        String secretKey = "5fgkBG5QWhIPNDNJ3HmM+MnxNxx3t/7BlyWG+w3LHH8=";
        String ciphertext = "W2ttlXinnWGByecBfjwGJjVN7PPGZOa085Q+Tq1wAzg5gPbPrOyiOD0bRwzHHX+7";
        byte[] secret = Base64.getDecoder().decode(secretKey);
        String plaintext = SymmetricEncryptionService.decrypt(ciphertext, secret);
        System.out.println(plaintext);
    }

}