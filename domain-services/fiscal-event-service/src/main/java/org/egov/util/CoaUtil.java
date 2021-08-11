package org.egov.util;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.request.RequestHeader;
import org.egov.config.FiscalEventConfiguration;
import org.egov.repository.ServiceRequestRepository;
import org.egov.tracer.model.CustomException;
import org.egov.web.models.Amount;
import org.egov.web.models.FiscalEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class CoaUtil {

    public static final String COA_IDS_JSON_PATH = "$.chartOfAccounts.*.id";

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private FiscalEventConfiguration configuration;

    @Autowired
    private ServiceRequestRepository serviceRequestRepository;

    /**
     * Get the COA Details from master data service
     * @param requestHeader
     * @param fiscalEvent
     * @return
     */
    public List<String> getCOAIdsFromCOAService(RequestHeader requestHeader, FiscalEvent fiscalEvent) {
        String url = createCoaSearchUrl();
        Map<String, Object> coaSearchRequest = createSearchCoaRequest(requestHeader, fiscalEvent);

        Object response = serviceRequestRepository.fetchResult(url,coaSearchRequest);
        List<String> responseCoaIds = null;
        try{
            responseCoaIds = JsonPath.read(response, COA_IDS_JSON_PATH);
        }catch (Exception e){
            throw new CustomException("JSONPATH_ERROR","Failed to parse coa response for coaIds");
        }
        return responseCoaIds;
    }

    private Map<String, Object> createSearchCoaRequest(RequestHeader requestHeader, FiscalEvent fiscalEvent) {
        List<String> coaIds = new ArrayList<>();
        for (Amount amount : fiscalEvent.getAmountDetails()) {
            coaIds.add(amount.getCoaId());
        }
        Map<String, Object> coaSearchRequest = new HashMap<>();
        Map<String, Object> criteria = new HashMap<>();
        criteria.put("Ids", coaIds);
        criteria.put("tenantId", fiscalEvent.getTenantId());

        coaSearchRequest.put("requestHeader", requestHeader);
        coaSearchRequest.put("criteria", criteria);
        return coaSearchRequest;
    }

    private String createCoaSearchUrl() {
        StringBuilder uriBuilder =  new StringBuilder(configuration.getIfixMasterHost())
                .append(configuration.getIfixMasterContextPath()).append(configuration.getCoaSearchPath());
        return uriBuilder.toString();
    }
}
