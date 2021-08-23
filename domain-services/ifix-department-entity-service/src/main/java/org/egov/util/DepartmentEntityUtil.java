package org.egov.util;

import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.AuditDetails;
import org.springframework.stereotype.Component;


@Component
@Slf4j
public class DepartmentEntityUtil {


    /**
     * Method to return auditDetails of fiscal event request
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
