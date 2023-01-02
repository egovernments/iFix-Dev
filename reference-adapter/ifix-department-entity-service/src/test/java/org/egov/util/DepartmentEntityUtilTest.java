package org.egov.util;

import org.egov.common.contract.AuditDetails;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

@Disabled("TODO: Need to work on it")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
class DepartmentEntityUtilTest {

    @InjectMocks
    private DepartmentEntityUtil departmentEntityUtil;

    @Test
    void testEnrichAuditDetails() {
        AuditDetails actualEnrichAuditDetailsResult = this.departmentEntityUtil.enrichAuditDetails("By", new AuditDetails(),
                true);
        assertEquals("By", actualEnrichAuditDetailsResult.getCreatedBy());
        assertEquals("By", actualEnrichAuditDetailsResult.getLastModifiedBy());
    }

    @Test
    void testEnrichAuditDetailsWithDefaultAuditDetails() {
        AuditDetails actualEnrichAuditDetailsResult = this.departmentEntityUtil.enrichAuditDetails("By", new AuditDetails(),
                false);
        assertNull(actualEnrichAuditDetailsResult.getCreatedBy());
        assertEquals("By", actualEnrichAuditDetailsResult.getLastModifiedBy());
        assertNull(actualEnrichAuditDetailsResult.getCreatedTime());
    }

    @Test
    void testEnrichAuditDetailsWithAuditDetails() {
        AuditDetails auditDetails = mock(AuditDetails.class);
        when(auditDetails.getCreatedTime()).thenReturn(1L);
        when(auditDetails.getCreatedBy()).thenReturn("Jan 1, 2020 8:00am GMT+0100");
        AuditDetails actualEnrichAuditDetailsResult = this.departmentEntityUtil.enrichAuditDetails("By", auditDetails,
                false);
        assertEquals("Jan 1, 2020 8:00am GMT+0100", actualEnrichAuditDetailsResult.getCreatedBy());
        assertEquals("By", actualEnrichAuditDetailsResult.getLastModifiedBy());
        assertEquals(1L, actualEnrichAuditDetailsResult.getCreatedTime().longValue());
        verify(auditDetails).getCreatedBy();
        verify(auditDetails).getCreatedTime();
    }
}

