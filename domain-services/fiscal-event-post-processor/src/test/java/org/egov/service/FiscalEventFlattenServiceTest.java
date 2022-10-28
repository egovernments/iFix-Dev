package org.egov.service;

import org.egov.common.contract.AuditDetails;
import org.egov.models.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
class FiscalEventFlattenServiceTest {

    @InjectMocks
    private FiscalEventFlattenService fiscalEventFlattenService;

    @Test
    void testGetFlattenDataWithEmptyUnbundledFiscalEvent() {
        assertTrue(this.fiscalEventFlattenService.getFlattenData(new ArrayList<FiscalEventLineItemUnbundled>()).isEmpty());
    }

    @Test
    void testGetFlattenDataWithDefaultUnbundledFiscalEvent() {
        ArrayList<FiscalEventLineItemUnbundled> fiscalEventLineItemUnbundledList = new ArrayList<FiscalEventLineItemUnbundled>();
        fiscalEventLineItemUnbundledList.add(new FiscalEventLineItemUnbundled());
        List<String> actualFlattenData = this.fiscalEventFlattenService.getFlattenData(fiscalEventLineItemUnbundledList);
        assertEquals(1, actualFlattenData.size());
        assertEquals("{}", actualFlattenData.get(0));
    }

    @Test
    void testGetFlattenDataWithNullUnbundledFiscalEvent() {
        ArrayList<FiscalEventLineItemUnbundled> fiscalEventLineItemUnbundledList = new ArrayList<FiscalEventLineItemUnbundled>();
        fiscalEventLineItemUnbundledList.add(null);
        List<String> actualFlattenData = this.fiscalEventFlattenService.getFlattenData(fiscalEventLineItemUnbundledList);
        assertEquals(1, actualFlattenData.size());
        assertEquals("null", actualFlattenData.get(0));
    }

    @Test
    void testGetFlattenData() {
        ArrayList<FiscalEventLineItemUnbundled> fiscalEventLineItemUnbundledList = new ArrayList<FiscalEventLineItemUnbundled>();
        Government government = new Government("42", "Name");

        BigDecimal amount = BigDecimal.valueOf(1L);
        ChartOfAccount coa = new ChartOfAccount("42", "Coa Code", "Major Head", "Major Head Name", "Major Head Type",
                "Sub Major Head", "Sub Major Head Name", "Minor Head", "Minor Head Name", "Sub Head", "Sub Head Name",
                "Group Head", "Group Head Name", "Object Head", "Object Head Name");

        fiscalEventLineItemUnbundledList
                .add(new FiscalEventLineItemUnbundled("2.0.0", "42", "42", "42", government,
                        "Event Type", 1L, 1L, "42", "42", "42", amount, coa, 1L, 1L, new AuditDetails(), new Object()));
        List<String> actualFlattenData = this.fiscalEventFlattenService.getFlattenData(fiscalEventLineItemUnbundledList);
        assertEquals(1, actualFlattenData.size());
        assertEquals(
                "{\"version\":\"2.0.0\",\"id\":\"42\",\"eventId\":\"42\",\"tenantId\":\"42\",\"government" +
                        ".id\":\"42\",\"government.name\":"
                        + "\"Name\",\"eventType\":\"Event Type\",\"ingestionTime"
                        + "\":1,\"eventTime\":1,\"referenceId\":\"42\",\"linkedEventId\":\"42\"," +
                        "\"linkedReferenceId\":\"42\",\"amount\":1,\"coa.id"
                        + "\":\"42\",\"coa.coaCode\":\"Coa Code\",\"coa.majorHead\":\"Major Head\",\"coa.majorHeadName\":\"Major Head"
                        + " Name\",\"coa.majorHeadType\":\"Major Head Type\",\"coa.subMajorHead\":\"Sub Major Head\",\"coa.subMajorHeadName\":\"Sub"
                        + " Major Head Name\",\"coa.minorHead\":\"Minor Head\",\"coa.minorHeadName\":\"Minor Head Name\",\"coa.subHead\":\"Sub"
                        + " Head\",\"coa.subHeadName\":\"Sub Head Name\",\"coa.groupHead\":\"Group Head\",\"coa.groupHeadName\":\"Group Head"
                        + " Name\",\"coa.objectHead\":\"Object Head\",\"coa.objectHeadName\":\"Object Head Name\",\"fromBillingPeriod\":1,"
                        + "\"toBillingPeriod\":1,\"auditDetails\":{},\"attributes\":{}}",
                actualFlattenData.get(0));
    }
}

