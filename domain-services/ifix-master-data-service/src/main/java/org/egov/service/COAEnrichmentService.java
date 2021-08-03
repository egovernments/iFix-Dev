package org.egov.service;


import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.AuditDetails;
import org.egov.common.contract.request.RequestHeader;
import org.egov.util.MasterDataServiceUtil;
import org.egov.web.models.COARequest;
import org.egov.web.models.ChartOfAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class COAEnrichmentService {

    @Autowired
    private MasterDataServiceUtil mdsUtil;

    /**
     * Enrich the COA create request with COA code, id and audit details.
     *
     * @param coaRequest
     */
    public void enrichCreatePost(COARequest coaRequest) {
        ChartOfAccount chartOfAccount = coaRequest.getChartOfAccount();
        RequestHeader requestHeader = coaRequest.getRequestHeader();

        createCoaCode(chartOfAccount);
        AuditDetails auditDetails = null;
        /**
         * check COA code is available in the Master system , if yes update the audit details and insert as a new record
         *  else insert as fresh record
         */

        if(chartOfAccount.getAuditDetails() == null){
            auditDetails = mdsUtil.enrichAuditDetails(requestHeader.getUserInfo() != null ? requestHeader.getUserInfo().getUuid():"", chartOfAccount.getAuditDetails(), true);
        }else{
            auditDetails = mdsUtil.enrichAuditDetails(requestHeader.getUserInfo() != null ? requestHeader.getUserInfo().getUuid():"", chartOfAccount.getAuditDetails(), false);
        }

        chartOfAccount.setAuditDetails(auditDetails);
        chartOfAccount.setId(UUID.randomUUID().toString());

    }

    /**
     * Create a COA code based on
     * majorHead
     * - subMajorHead
     * - minorHead
     * - subHead
     * - groupHead
     * - objectHead
     *
     * @param chartOfAccount
     */
    protected void createCoaCode(ChartOfAccount chartOfAccount) {
        StringBuilder coaCode = new StringBuilder();
        coaCode.append(chartOfAccount.getMinorHead()).append("-")
                .append(chartOfAccount.getSubMajorHead()).append("-")
                .append(chartOfAccount.getMinorHead()).append("-")
                .append(chartOfAccount.getSubHead()).append("-")
                .append(chartOfAccount.getGroupHead()).append("-")
                .append(chartOfAccount.getObjectHead()).append("-");

        chartOfAccount.setCoaCode(coaCode.toString());
    }
}
