package org.egov.service;


import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.AuditDetails;
import org.egov.common.contract.request.RequestHeader;
import org.egov.util.MasterDataServiceUtil;
import org.egov.web.models.ExpenditureDTO;
import org.egov.web.models.ExpenditureRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class ExpenditureEnrichmentService {

    @Autowired
    private MasterDataServiceUtil mdsUtil;

    public void enrichCreateExpenditure(ExpenditureRequest expenditureRequest) {
        ExpenditureDTO expenditureDTO = expenditureRequest.getExpenditureDTO();
        RequestHeader requestHeader = expenditureRequest.getRequestHeader();

        AuditDetails auditDetails = null;
        if (expenditureDTO.getAuditDetails() == null) {
            auditDetails = mdsUtil.enrichAuditDetails(requestHeader.getUserInfo().getUuid(), expenditureDTO.getAuditDetails(), true);
        } else {
            auditDetails = mdsUtil.enrichAuditDetails(requestHeader.getUserInfo().getUuid(), expenditureDTO.getAuditDetails(), false);
        }

        expenditureDTO.setAuditDetails(auditDetails);
        expenditureDTO.setId(UUID.randomUUID().toString());
    }
}
