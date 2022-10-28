package org.egov.ifix.service;

import org.egov.ifix.models.mgramseva.ChallanResponseDTO;

import java.util.Optional;

public interface PspclEventService {
    void processPspclEventForMgramseva(String eventType);

}
