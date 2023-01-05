package org.egov.service;


import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.egov.common.contract.AuditDetails;
import org.egov.common.contract.request.RequestHeader;
import org.egov.tracer.model.CustomException;
import org.egov.util.CoaUtil;
import org.egov.util.FiscalEventUtil;
import org.egov.web.models.Amount;
import org.egov.web.models.FiscalEvent;
import org.egov.web.models.FiscalEventRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Service
@Slf4j
public class FiscalEventEnrichmentService {

    @Autowired
    private FiscalEventUtil fiscalEventUtil;
    
    @Autowired
	private CoaUtil coaUtil;


    public void enrichFiscalEventPushPost(FiscalEventRequest fiscalEventRequest) {
        RequestHeader requestHeader = fiscalEventRequest.getRequestHeader();
        List<FiscalEvent> fiscalEvents = fiscalEventRequest.getFiscalEvent();
        Set<String> coaCodes = new HashSet<>();
        if (fiscalEvents != null && !fiscalEvents.isEmpty()) {
            Long ingestionTime = System.currentTimeMillis();
            for (FiscalEvent fiscalEvent : fiscalEvents) {
                //set the id
                fiscalEvent.setId(UUID.randomUUID().toString());

                for (Amount amount : fiscalEvent.getAmountDetails()) {
                  //  Amount newAmount = new Amount();
                   // BeanUtils.copyProperties(amount, newAmount);
                    //set the amount id
                	amount.setId(UUID.randomUUID().toString());
                	coaCodes.add(amount.getCoaCode());
                    AuditDetails auditDetails = null;
                    if (fiscalEvent.getAuditDetails() == null) {
                        auditDetails = fiscalEventUtil.enrichAuditDetails(requestHeader.getUserInfo().getUuid(), amount.getAuditDetails(), true);
                    } else {
                        auditDetails = fiscalEventUtil.enrichAuditDetails(requestHeader.getUserInfo().getUuid(), amount.getAuditDetails(), false);
                    }
                    amount.setAuditDetails(auditDetails);
                }

                AuditDetails auditDetails = null;
                if (fiscalEvent.getAuditDetails() == null) {
                    auditDetails = fiscalEventUtil.enrichAuditDetails(requestHeader.getUserInfo().getUuid(), fiscalEvent.getAuditDetails(), true);
                } else {
                    auditDetails = fiscalEventUtil.enrichAuditDetails(requestHeader.getUserInfo().getUuid(), fiscalEvent.getAuditDetails(), false);
                }

                //set the audit details
                fiscalEvent.setAuditDetails(auditDetails);
                fiscalEvent.setIngestionTime(ingestionTime);
            }
        }
        
        validateAndEnrichCoa(fiscalEventRequest, coaCodes, fiscalEventRequest.getFiscalEvent().get(0).getTenantId());
        
    }
    /**
	 * @param fiscalEventRequest
	 * @param errorMap
	 */
	public void validateAndEnrichCoa(FiscalEventRequest fiscalEventRequest, Set<String> coaCodes,  String tenantId) {
		Map<String, String> errorMap = new HashMap<>();
		List<String> errorCoaCodes = new ArrayList<>();
		JsonNode jsonNode = coaUtil.fetchCoaDetailsByCoaCodes(fiscalEventRequest.getRequestHeader(), coaCodes, tenantId);

		for (String coaCode : coaCodes) {
			boolean isPresent = false;

			if (jsonNode != null) {
				for (JsonNode chartOfAccountJN : jsonNode) {
					String coaCd = chartOfAccountJN.get("coaCode").asText();
					if (StringUtils.isNotBlank(coaCd) && coaCode.equals(coaCd.trim())) {
						isPresent = true;
						break;
					}
				}
			}

			if (!isPresent) {
				errorCoaCodes.add(coaCode);
			}
		}
		if (!errorCoaCodes.isEmpty()) {
			errorMap.put("COA_CODE_INVALID", "This chart of account Code : " + errorCoaCodes + " is invalid "
					+ "(or) Combination of tenant id with this chart of account code doesn't exist");
			throw new CustomException(errorMap);
		}

		fiscalEventUtil.enrichCoaDetails(fiscalEventRequest, jsonNode);
	}
}
