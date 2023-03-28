package org.egov.key;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.security.NoSuchAlgorithmException;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class KeyGeneratorTest {

    @Test
    public void testGenSek() throws NoSuchAlgorithmException {
        String key = KeyGenerator.genAES256Key();
        System.out.println(key);
    }

}