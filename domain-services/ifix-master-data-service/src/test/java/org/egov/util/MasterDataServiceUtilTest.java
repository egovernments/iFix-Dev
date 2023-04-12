package org.egov.util;

import org.egov.MasterApplicationMain;
import org.egov.common.contract.AuditDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(classes = MasterApplicationMain.class)
class MasterDataServiceUtilTest {


    @InjectMocks
    private MasterDataServiceUtil masterDataServiceUtil;

    private String userId;
    private AuditDetails auditDetails;

    @BeforeEach
    public void init() throws IOException {
        userId = "MASTER00-DATA-SERV-ICE0-UTIL0TESTS10";
        Long time = System.currentTimeMillis();
        auditDetails = new AuditDetails(userId, userId, time, time);
    }

    @Test
    void testEnrichAuditDetails() {
        AuditDetails actualEnrichAuditDetailsResult = masterDataServiceUtil.enrichAuditDetails(userId,
                new AuditDetails(), true);

        assertEquals(userId, actualEnrichAuditDetailsResult.getCreatedBy());
        assertEquals(userId, actualEnrichAuditDetailsResult.getLastModifiedBy());
    }

    @Test
    void testEnrichAuditDetailsWithAuditDetailsData() {
        AuditDetails actualEnrichAuditDetailsResult = masterDataServiceUtil
                .enrichAuditDetails(auditDetails.getCreatedBy(), auditDetails, false);

        assertEquals(auditDetails.getCreatedBy(), actualEnrichAuditDetailsResult.getCreatedBy());
        assertEquals(auditDetails.getLastModifiedBy(), actualEnrichAuditDetailsResult.getLastModifiedBy());
    }


}

