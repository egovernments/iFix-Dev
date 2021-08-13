package org.egov.service;


import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.AuditDetails;
import org.egov.common.contract.request.RequestHeader;
import org.egov.util.FiscalEventPostProcessorUtil;
import org.egov.web.models.FiscalEvent;
import org.egov.web.models.FiscalEventDeReferenced;
import org.egov.web.models.FiscalEventRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class FiscalEventDereferenceEnrichmentService {

    @Autowired
    private FiscalEventPostProcessorUtil processorUtil;

    public void enrich(FiscalEventRequest fiscalEventRequest, FiscalEventDeReferenced fiscalEventDeReferenced) {
        RequestHeader requestHeader = fiscalEventRequest.getRequestHeader();
        FiscalEvent fiscalEvent = fiscalEventRequest.getFiscalEvent();

        fiscalEventDeReferenced.setEventTime(fiscalEvent.getEventTime());
        fiscalEventDeReferenced.setEventType(fiscalEvent.getEventType().name());
        fiscalEventDeReferenced.setParentReferenceId(fiscalEvent.getParentReferenceId());
        fiscalEventDeReferenced.setParentEventId(fiscalEvent.getParentEventId());
        fiscalEventDeReferenced.setReferenceId(fiscalEvent.getReferenceId());
        fiscalEventDeReferenced.setAttributes(fiscalEvent.getAttributes());
        fiscalEventDeReferenced.setVersion("1.0.0");

        AuditDetails auditDetails = null;
        if (fiscalEventDeReferenced.getAuditDetails() == null) {
            auditDetails = processorUtil.enrichAuditDetails(requestHeader.getUserInfo().getUuid(), fiscalEvent.getAuditDetails(), true);
        } else {
            auditDetails = processorUtil.enrichAuditDetails(requestHeader.getUserInfo().getUuid(), fiscalEvent.getAuditDetails(), false);
        }
        fiscalEventDeReferenced.setAuditDetails(auditDetails);
        fiscalEventDeReferenced.setId(UUID.randomUUID().toString());
    }
}
