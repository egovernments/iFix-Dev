package org.egov.ifix.service;

import org.egov.ifix.models.fiscalEvent.FiscalEvent;

public interface CollectionService {
    void makePspclPaymentByCollectionService(String eventType, FiscalEvent fiscalEvent, String mgramsevaTenantId,
                                             String billConsumerCode);
}
