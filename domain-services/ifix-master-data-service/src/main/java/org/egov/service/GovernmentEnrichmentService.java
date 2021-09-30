package org.egov.service;

import org.egov.common.contract.AuditDetails;
import org.egov.common.contract.request.RequestHeader;
import org.egov.util.MasterDataServiceUtil;
import org.egov.web.models.Government;
import org.egov.web.models.GovernmentRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GovernmentEnrichmentService {

    @Autowired
    MasterDataServiceUtil enrichAuditDetails;

    public void enrichGovernmentData(GovernmentRequest governmentRequest) {
        Government government = governmentRequest.getGovernment();
        RequestHeader requestHeader = governmentRequest.getRequestHeader();

        AuditDetails auditDetails = null;

        if (government.getAuditDetails() == null) {
            auditDetails = enrichAuditDetails.enrichAuditDetails(requestHeader.getUserInfo().getUuid(), government.getAuditDetails(), true);
        } else {
            auditDetails = enrichAuditDetails.enrichAuditDetails(requestHeader.getUserInfo().getUuid(), government.getAuditDetails(), false);
        }

        government.setAuditDetails(auditDetails);
    }
}
