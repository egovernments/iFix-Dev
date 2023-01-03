package org.egov.service;

import org.egov.config.TestDataFormatter;
import org.egov.models.FiscalEventDeReferenced;
import org.egov.models.FiscalEventRequest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
class FiscalEventDereferenceEnrichmentServiceTest {

    @InjectMocks
    private FiscalEventDereferenceEnrichmentService fiscalEventDereferenceEnrichmentService;

    @Autowired
    private TestDataFormatter testDataFormatter;

    private FiscalEventRequest fiscalEventRequest;

    @BeforeAll
    void init() throws IOException {
        fiscalEventRequest = testDataFormatter.getFiscalEventValidatedData();
    }

    @Test
    void testEnrich() {
        FiscalEventDeReferenced fiscalEventDeReferenced = new FiscalEventDeReferenced();
        this.fiscalEventDereferenceEnrichmentService.enrich(fiscalEventRequest, fiscalEventDeReferenced);
        assertNotNull(fiscalEventDeReferenced.getReferenceId());
        assertNull(fiscalEventDeReferenced.getLinkedReferenceId());
        assertNull(fiscalEventDeReferenced.getLinkedEventId());
        assertNotNull(fiscalEventDeReferenced.getIngestionTime());
        assertNotNull(fiscalEventDeReferenced.getId());
        assertNotNull(fiscalEventDeReferenced.getEventTime());
        assertNotNull(fiscalEventDeReferenced.getAuditDetails());
    }
}

