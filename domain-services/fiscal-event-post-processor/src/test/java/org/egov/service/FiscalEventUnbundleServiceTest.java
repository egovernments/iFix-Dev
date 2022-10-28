package org.egov.service;

import org.egov.config.TestDataFormatter;
import org.egov.models.FiscalEventDeReferenced;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
class FiscalEventUnbundleServiceTest {


    @InjectMocks
    private FiscalEventUnbundleService fiscalEventUnbundleService;

    @Autowired
    private TestDataFormatter testDataFormatter;

    private FiscalEventDeReferenced fiscalEventDeReferenced;

    @BeforeAll
    void init() throws IOException {
        fiscalEventDeReferenced = testDataFormatter.getFiscalEventDereferencedData();
    }

    @Test
    void testUnbundleWithEmptyAmountDetailsInFiscalEventDereference() {
        assertTrue(this.fiscalEventUnbundleService.unbundle(new FiscalEventDeReferenced()).isEmpty());
    }

    @Test
    void testUnbundleWithValidFiscalEventDereferencedData() {
        assertEquals(1, this.fiscalEventUnbundleService.unbundle(fiscalEventDeReferenced).size());
    }
}

