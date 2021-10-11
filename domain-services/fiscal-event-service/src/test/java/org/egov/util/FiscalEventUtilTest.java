package org.egov.util;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.egov.common.contract.AuditDetails;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
class FiscalEventUtilTest {

    @InjectMocks
    private FiscalEventUtil fiscalEventUtil;

    @Test
    void testEnrichAuditDetailsWhenCreating() {
        AuditDetails actualEnrichAuditDetailsResult = this.fiscalEventUtil.enrichAuditDetails("By", new AuditDetails(),
                true);
        assertEquals("By", actualEnrichAuditDetailsResult.getCreatedBy());
        assertEquals("By", actualEnrichAuditDetailsResult.getLastModifiedBy());
    }

    @Test
    void testEnrichAuditDetailsWhenUpdating() {
        AuditDetails actualEnrichAuditDetailsResult = this.fiscalEventUtil.enrichAuditDetails("By", new AuditDetails(),
                false);
        assertEquals("By", actualEnrichAuditDetailsResult.getLastModifiedBy());
        assertNotNull(actualEnrichAuditDetailsResult.getLastModifiedBy());
    }

    @Test
    void testEnrichAuditDetailsWithAllNonNulls() {
        AuditDetails auditDetails = mock(AuditDetails.class);
        when(auditDetails.getCreatedTime()).thenReturn(1L);
        when(auditDetails.getCreatedBy()).thenReturn("Creator");
        AuditDetails actualEnrichAuditDetailsResult = this.fiscalEventUtil.enrichAuditDetails("Modifier", auditDetails, false);
        assertEquals("Creator", actualEnrichAuditDetailsResult.getCreatedBy());
        assertEquals("Modifier", actualEnrichAuditDetailsResult.getLastModifiedBy());
        assertEquals(1L, actualEnrichAuditDetailsResult.getCreatedTime().longValue());
        verify(auditDetails).getCreatedBy();
        verify(auditDetails).getCreatedTime();
    }
}

