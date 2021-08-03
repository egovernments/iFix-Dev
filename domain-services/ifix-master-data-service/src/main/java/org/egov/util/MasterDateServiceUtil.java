package org.egov.util;


import org.egov.common.contract.AuditDetails;
import org.egov.web.models.ChartOfAccount;
import org.springframework.stereotype.Component;

@Component
public class MasterDateServiceUtil {

    /**
     * Method to return auditDetails for create/update flows for COA
     *
     * @param by
     * @param isCreate
     * @return AuditDetails
     */
    public AuditDetails enrichAuditDetails(String by, AuditDetails auditDetails, Boolean isCreate) {
        Long time = System.currentTimeMillis();
        if(isCreate)
            return AuditDetails.builder().createdBy(by).lastModifiedBy(by).createdTime(time).lastModifiedTime(time).build();
        else
            return AuditDetails.builder().createdBy(auditDetails.getCreatedBy()).lastModifiedBy(by)
                    .createdTime(auditDetails.getCreatedTime()).lastModifiedTime(time).build();
    }

}
