package org.egov.util;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.egov.common.contract.request.RequestHeader;
import org.egov.config.FiscalEventPostProcessorConfig;
import org.egov.resposioty.ServiceRequestRepository;
import org.egov.tracer.model.CustomException;
import org.egov.web.models.Amount;
import org.egov.web.models.ChartOfAccount;
import org.egov.web.models.FiscalEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@Slf4j
public class CoaUtil {
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private FiscalEventPostProcessorConfig configuration;

    @Autowired
    private ServiceRequestRepository serviceRequestRepository;

    /**
     * Get the COA Details from master data service
     *
     * @param requestHeader
     * @param fiscalEvent
     * @return
     */
    public List<ChartOfAccount> getCOAIdsFromCOAService(RequestHeader requestHeader, FiscalEvent fiscalEvent) {
        String url = createCoaSearchUrl();
        Map<String, Object> coaSearchRequest = createSearchCoaRequest(requestHeader, fiscalEvent);

        Object response = serviceRequestRepository.fetchResult(url, coaSearchRequest);
        List<ChartOfAccount> chartOfAccounts = null;
        try {
            chartOfAccounts = JsonPath.read(response, MasterDataConstants.COA_JSON_PATH);
            if (chartOfAccounts != null) {
                return objectMapper.convertValue(chartOfAccounts, new TypeReference<List<ChartOfAccount>>() {
                });
            }
        } catch (Exception e) {
            throw new CustomException(MasterDataConstants.JSONPATH_ERROR, "Failed to parse coa response for coaIds");
        }
        return Collections.emptyList();
    }

    private Map<String, Object> createSearchCoaRequest(RequestHeader requestHeader, FiscalEvent fiscalEvent) {
        if (fiscalEvent != null && StringUtils.isNotBlank(fiscalEvent.getTenantId())) {

            List<String> coaIds = new ArrayList<>();
            if (fiscalEvent.getAmountDetails() != null
                    && !fiscalEvent.getAmountDetails().isEmpty()) {
                for (Amount amount : fiscalEvent.getAmountDetails()) {
                    if (StringUtils.isNotBlank(amount.getCoaId())) {
                        coaIds.add(amount.getCoaId());
                    }
                }
            }

            Map<String, Object> coaSearchRequest = new HashMap<>();
            Map<String, Object> criteria = new HashMap<>();
            criteria.put(MasterDataConstants.IDS, coaIds);
            criteria.put(MasterDataConstants.CRITERIA_TENANT_ID, fiscalEvent.getTenantId());

            coaSearchRequest.put(MasterDataConstants.REQUEST_HEADER, requestHeader);
            coaSearchRequest.put(MasterDataConstants.CRITERIA, criteria);
            return coaSearchRequest;
        }

        return Collections.emptyMap();
    }

    private String createCoaSearchUrl() {
        StringBuilder uriBuilder = new StringBuilder(configuration.getIfixMasterCoaHost())
                .append(configuration.getIfixMasterCoaContextPath()).append(configuration.getIfixMasterCoaSearchPath());
        return uriBuilder.toString();
    }

}
