package org.egov.ifix.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.egov.ifix.persistance.PspclEventDetail;
import org.egov.ifix.persistance.PspclEventDetailRepository;
import org.egov.ifix.service.PspclEventPersistenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

import static org.egov.ifix.utils.EventConstants.NA;

@Slf4j
@Service
public class PspclEventPersistenceServiceImpl implements PspclEventPersistenceService {

    @Autowired
    private PspclEventDetailRepository pspclEventDetailRepository;

    @Override
    public PspclEventDetail saveFailedPspclEventDetail(String mgramsevaTenantId, String eventType,
                                                       String data, String error) {
        if (NA.equalsIgnoreCase(data)) {
            data = null;
        }
        return savePspclEventDetail(false, mgramsevaTenantId, eventType, NA, data, error, null, null);
    }

    /**
     * @param mgramsevaTenantId
     * @param eventType
     * @return
     */
    @Override
    public PspclEventDetail saveSuccessPspclEventDetail(String mgramsevaTenantId, String eventType, String eventId,
                                                        String accountNo, Double amount) {
        return savePspclEventDetail(true, mgramsevaTenantId, eventType, eventId, null, null,
                accountNo, amount);
    }

    /**
     * @param isSuccess
     * @param mgramsevaTenantId
     * @param eventType
     * @param data
     * @param error
     * @return
     */
    private PspclEventDetail savePspclEventDetail(boolean isSuccess, String mgramsevaTenantId, String eventType,
                                                  String eventId, String data, String error, String accountNo, Double amount) {
        PspclEventDetail pspclEventDetail = new PspclEventDetail();
        pspclEventDetail.setEventType(eventType);
        pspclEventDetail.setTenantId(mgramsevaTenantId);
        pspclEventDetail.setCreatedDate(new Date());
        pspclEventDetail.setSuccess(isSuccess);
        pspclEventDetail.setEventId(eventId);


        if (!isSuccess) {
            pspclEventDetail.setError(error);
            pspclEventDetail.setData(data);
        } else {
            pspclEventDetail.setAccountNo(accountNo);
            pspclEventDetail.setAmount(amount);
        }

        return pspclEventDetailRepository.save(pspclEventDetail);
    }
}
