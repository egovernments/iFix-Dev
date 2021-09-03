package org.egov.service;


import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.request.RequestHeader;
import org.egov.util.MasterDataConstants;
import org.egov.web.models.FiscalEvent;
import org.egov.web.models.FiscalEventDeReferenced;
import org.egov.web.models.FiscalEventRequest;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class FiscalEventDereferenceEnrichmentService {


    public void enrich(FiscalEventRequest fiscalEventRequest, FiscalEventDeReferenced fiscalEventDeReferenced) {
        RequestHeader requestHeader = fiscalEventRequest.getRequestHeader();
        FiscalEvent fiscalEvent = fiscalEventRequest.getFiscalEvent();

        fiscalEventDeReferenced.setIngestionTime(fiscalEvent.getIngestionTime());
        fiscalEventDeReferenced.setEventTime(fiscalEvent.getEventTime());
        fiscalEventDeReferenced.setEventType(fiscalEvent.getEventType().name());
        fiscalEventDeReferenced.setParentReferenceId(fiscalEvent.getParentReferenceId());
        fiscalEventDeReferenced.setParentEventId(fiscalEvent.getParentEventId());
        fiscalEventDeReferenced.setReferenceId(fiscalEvent.getReferenceId());
        fiscalEventDeReferenced.setAttributes(fiscalEvent.getAttributes());
        fiscalEventDeReferenced.setVersion(MasterDataConstants.FISCAL_EVENT_VERSION);

        fiscalEventDeReferenced.setAuditDetails(fiscalEvent.getAuditDetails());
        fiscalEventDeReferenced.setId(fiscalEvent.getId());
    }
}
