package org.egov.ifix.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.egov.ifix.persistance.EventPostingDetail;
import org.egov.ifix.persistance.EventPostingDetailRepository;
import org.egov.ifix.service.EventPostingDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Slf4j
@Service
public class EventPostingDetailServiceImpl implements EventPostingDetailService {

    @Autowired
    private EventPostingDetailRepository eventPostingDetailRepository;


    /**
     * @param sourceSystem
     * @param eventId
     * @param mgramsevaTenantId
     * @param eventType
     * @return
     */
    @Override
    public EventPostingDetail saveSuccessEventPostingDetail(String sourceSystem, String eventId, String mgramsevaTenantId,
                                                            String eventType) {
        return composeEventPostingDetail(true, sourceSystem, eventId, mgramsevaTenantId, eventType,
                null, null);
    }


    /**
     * @param sourceSystem
     * @param eventId
     * @param mgramsevaTenantId
     * @param eventType
     * @param data
     * @param error
     * @return
     */
    @Override
    public EventPostingDetail saveFailedEventPostingDetail(String sourceSystem, String eventId, String mgramsevaTenantId,
                                                           String eventType, String data, String error) {
        return composeEventPostingDetail(false, sourceSystem, eventId, mgramsevaTenantId, eventType, data, error);
    }


    /**
     * @param isSuccess
     * @param sourceSystem
     * @param eventId
     * @param mgramsevaTenantId
     * @param eventType
     * @param data
     * @param error
     * @return
     */
    private EventPostingDetail composeEventPostingDetail(boolean isSuccess, String sourceSystem, String eventId, String mgramsevaTenantId,
                                                         String eventType, String data, String error) {
        EventPostingDetail eventPostingDetail = new EventPostingDetail();
        eventPostingDetail.setEventId(eventId);
        eventPostingDetail.setTenantId(mgramsevaTenantId);
        eventPostingDetail.setEventType(eventType);
        eventPostingDetail.setCreatedDate(new Date());
        eventPostingDetail.setLastModifiedDate(new Date());

        if (!isSuccess) {
            eventPostingDetail.setError(error);
            eventPostingDetail.setRecord(data);
            eventPostingDetail.setStatus("Fail");
        } else {
            eventPostingDetail.setStatus("Success");
        }

        return eventPostingDetailRepository.save(eventPostingDetail);
    }
}
