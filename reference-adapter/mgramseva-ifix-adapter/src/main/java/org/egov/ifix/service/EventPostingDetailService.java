package org.egov.ifix.service;

import org.egov.ifix.persistance.EventPostingDetail;

public interface EventPostingDetailService {
    EventPostingDetail saveSuccessEventPostingDetail(String sourceSystem, String eventId, String mgramsevaTenantId,
                                                     String eventType);

    EventPostingDetail saveFailedEventPostingDetail(String sourceSystem, String eventId, String mgramsevaTenantId,
                                                    String eventType, String data, String error);
}
