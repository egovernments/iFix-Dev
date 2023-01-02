package org.egov.service;


import lombok.extern.slf4j.Slf4j;
import org.egov.models.FiscalEvent;
import org.egov.models.FiscalEventDeReferenced;
import org.egov.models.FiscalEventRequest;
import org.egov.util.MasterDataConstants;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class FiscalEventDereferenceEnrichmentService {


    public void enrich(FiscalEventRequest fiscalEventRequest, FiscalEventDeReferenced fiscalEventDeReferenced) {
        FiscalEvent fiscalEvent = fiscalEventRequest.getFiscalEvent();

        fiscalEventDeReferenced.setIngestionTime(fiscalEvent.getIngestionTime());
        fiscalEventDeReferenced.setEventTime(fiscalEvent.getEventTime());
        fiscalEventDeReferenced.setEventType(fiscalEvent.getEventType().name());
        fiscalEventDeReferenced.setLinkedReferenceId(fiscalEvent.getLinkedReferenceId());
        fiscalEventDeReferenced.setLinkedEventId(fiscalEvent.getLinkedEventId());
        fiscalEventDeReferenced.setReferenceId(fiscalEvent.getReferenceId());
        fiscalEventDeReferenced.setAttributes(fiscalEvent.getAttributes());
        fiscalEventDeReferenced.setVersion(fiscalEvent.getVersion());

        fiscalEventDeReferenced.setAuditDetails(fiscalEvent.getAuditDetails());
        fiscalEventDeReferenced.setId(fiscalEvent.getId());
    }
}
