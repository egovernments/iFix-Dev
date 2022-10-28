package org.egov.ifix.service;

import org.egov.ifix.persistance.PspclEventDetail;

public interface PspclEventPersistenceService {
    PspclEventDetail saveFailedPspclEventDetail(String mgramsevaTenantId, String eventType,
                                                String data, String error);

    PspclEventDetail saveSuccessPspclEventDetail(String mgramsevaTenantId, String eventType, String eventId,
                                                 String accountNo, Double amount);
}
