package org.egov.key;

import org.egov.xtra.key.EgovKeyGenerator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.security.NoSuchAlgorithmException;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class EgovKeyGeneratorTest {

    EgovKeyGenerator egovKeyGenerator;

    @BeforeAll
    public void init() {
        egovKeyGenerator = new EgovKeyGenerator();
    }

    @Test
    public void testGenSek() throws NoSuchAlgorithmException {
        String key = EgovKeyGenerator.genSek();
        System.out.println(key);
    }

}