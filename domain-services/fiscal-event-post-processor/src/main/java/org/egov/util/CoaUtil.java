package org.egov.util;


import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonArray;
import com.jayway.jsonpath.JsonPath;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONArray;
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

import java.io.IOException;
import java.util.*;

@Component
@Slf4j
public class CoaUtil {

    public static final String COA_JSON_PATH = "$.chartOfAccounts.*";

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
            chartOfAccounts = JsonPath.read(response, COA_JSON_PATH);
        } catch (Exception e) {
            throw new CustomException("JSONPATH_ERROR", "Failed to parse coa response for coaIds");
        }
        if (chartOfAccounts != null) {
            return objectMapper.convertValue(chartOfAccounts, new TypeReference<List<ChartOfAccount>>() {
            });
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
            criteria.put("Ids", coaIds);
            criteria.put("tenantId", fiscalEvent.getTenantId());

            coaSearchRequest.put("requestHeader", requestHeader);
            coaSearchRequest.put("criteria", criteria);
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
