package org.egov.util;


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

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * @param dereferencedFiscalEvents
     * @return
     */
    public List<FiscalEvent> mapDereferencedFiscalEventToFiscalEvent(List<Object> dereferencedFiscalEvents) {
        if (dereferencedFiscalEvents == null || dereferencedFiscalEvents.isEmpty())
            Collections.emptyList();
        JsonNode dereferencedFiscalEventNode = objectMapper.convertValue(dereferencedFiscalEvents, JsonNode.class);
        Iterator<JsonNode> nodeIterator = dereferencedFiscalEventNode.iterator();
        List<FiscalEvent> fiscalEvents = new ArrayList<>();
        while (nodeIterator.hasNext()) {
            JsonNode node = nodeIterator.next();
            fiscalEvents.add(getFiscalEvent(node));
        }
        return fiscalEvents;
    }

    private FiscalEvent getFiscalEvent(JsonNode node) {
        FiscalEvent fiscalEvent = FiscalEvent.builder()
                .id(node.get("id").asText())
                .tenantId(node.get("tenantId") != null ? node.get("tenantId").asText() : null)
                .eventType(node.get("eventType") != null ? FiscalEvent.EventTypeEnum.valueOf(node.get("eventType").asText()) : null)
                .eventTime(node.get("eventTime") != null ? node.get("eventTime").asLong() : null)
                .referenceId(node.get("referenceId") != null ? node.get("referenceId").asText() : null)
                .parentReferenceId(node.get("parentReferenceId") != null ? node.get("parentReferenceId").asText() : null)
                .parentEventId(node.get("parentEventId") != null ? node.get("parentEventId").asText() : null)
                .ingestionTime(node.get("ingestionTime") != null ? node.get("ingestionTime").asLong() : null)
                .attributes(node.get("attributes"))
                .build();

        //audit details
        if (node.get("auditDetails") != null) {
            AuditDetails auditDetails = AuditDetails.builder()
                    .createdBy(node.get("auditDetails").get("createdBy") != null ? node.get("auditDetails").get("createdBy").asText() : null)
                    .lastModifiedBy(node.get("auditDetails").get("lastModifiedBy") != null ? node.get("auditDetails").get("lastModifiedBy").asText() : null)
                    .createdTime(node.get("auditDetails").get("createdTime") != null ? node.get("auditDetails").get("createdTime").asLong() : null)
                    .lastModifiedTime(node.get("auditDetails").get("lastModifiedTime") != null ? node.get("auditDetails").get("lastModifiedTime").asLong() : null)
                    .build();
            fiscalEvent.setAuditDetails(auditDetails);
        }
        //Amount Details
        if (node.get("amountDetails") != null && !node.get("amountDetails").isEmpty()) {
            List<Amount> amountDetails = new ArrayList<>();
            Iterator<JsonNode> amtIterator = node.get("amountDetails").iterator();
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
        //project
        if (node.get("project") != null && node.get("project").get("id") != null) {
            fiscalEvent.setProjectId(node.get("project").get("id").asText());
        }
        return fiscalEvent;
    }
}
