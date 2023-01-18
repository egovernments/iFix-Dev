package org.egov.util;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.AuditDetails;
import org.egov.web.models.Amount;
import org.egov.web.models.FiscalEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

@Component
@Slf4j
public class FiscalEventMapperUtil {

    public static final String AUDIT_DETAILS = "auditDetails";
    public static final String AMOUNT_DETAILS = "amountDetails";

    @Autowired
    private ObjectMapper objectMapper;

    private FiscalEvent getFiscalEvent(JsonNode node) {
        FiscalEvent fiscalEvent = FiscalEvent.builder()
                .id(node.get("id").asText())
                .tenantId(node.get("tenantId") != null ? node.get("tenantId").asText() : null)
                .eventType(node.get("eventType") != null ? FiscalEvent.EventTypeEnum.valueOf(node.get("eventType").asText()) : null)
                .eventTime(node.get("eventTime") != null ? node.get("eventTime").asLong() : null)
                .referenceId(node.get("referenceId") != null ? node.get("referenceId").asText() : null)
                .linkedReferenceId(node.get("linkedReferenceId") != null ? node.get("linkedReferenceId").asText() : null)
                .linkedEventId(node.get("linkedEventId") != null ? node.get("linkedEventId").asText() : null)
                .ingestionTime(node.get("ingestionTime") != null ? node.get("ingestionTime").asLong() : null)
                .receivers(node.get("receivers") != null && node.get("receivers").isArray() ? objectMapper.convertValue(node.get("receivers"), new TypeReference<List<String>>() {
                }) : null)
                .attributes(node.get("attributes"))
                .build();

        //audit details
        if (node.get(AUDIT_DETAILS) != null) {
            AuditDetails auditDetails = AuditDetails.builder()
                    .createdBy(node.get(AUDIT_DETAILS).get("createdBy") != null ? node.get(AUDIT_DETAILS).get("createdBy").asText() : null)
                    .lastModifiedBy(node.get(AUDIT_DETAILS).get("lastModifiedBy") != null ? node.get(AUDIT_DETAILS).get("lastModifiedBy").asText() : null)
                    .createdTime(node.get(AUDIT_DETAILS).get("createdTime") != null ? node.get(AUDIT_DETAILS).get("createdTime").asLong() : null)
                    .lastModifiedTime(node.get(AUDIT_DETAILS).get("lastModifiedTime") != null ? node.get(AUDIT_DETAILS).get("lastModifiedTime").asLong() : null)
                    .build();
            fiscalEvent.setAuditDetails(auditDetails);
        }
        //Amount Details
        if (node.get(AMOUNT_DETAILS) != null && !node.get(AMOUNT_DETAILS).isEmpty()) {
            List<Amount> amountDetails = new ArrayList<>();
            Iterator<JsonNode> amtIterator = node.get(AMOUNT_DETAILS).iterator();
            while (amtIterator.hasNext()) {
                JsonNode amountNode = amtIterator.next();
                Amount amount = Amount.builder()
                        .id(amountNode.get("id") != null ? amountNode.get("id").asText() : null)
                        .amount(amountNode.get("amount") != null ? amountNode.get("amount").decimalValue() : null)
                        .coaId(amountNode.get("coa") != null && amountNode.get("coa").get("id") != null ? amountNode.get("coa").get("id").asText() : null)
                        .fromBillingPeriod(amountNode.get("fromBillingPeriod") != null ? amountNode.get("fromBillingPeriod").asLong() : null)
                        .toBillingPeriod(amountNode.get("toBillingPeriod") != null ? amountNode.get("toBillingPeriod").asLong() : null)
                        .build();

                amountDetails.add(amount);
            }
            fiscalEvent.setAmountDetails(amountDetails);
        }
        return fiscalEvent;
    }
}
