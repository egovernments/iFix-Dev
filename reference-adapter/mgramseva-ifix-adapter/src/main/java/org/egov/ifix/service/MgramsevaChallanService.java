package org.egov.ifix.service;

import org.egov.ifix.models.fiscalEvent.FiscalEvent;

import javax.validation.constraints.NotNull;

public interface MgramsevaChallanService {
    void createChallan(String eventType, FiscalEvent fiscalEvent, String mgramsevaTenantId,
                       @NotNull String pspclAccountNumber);
}
