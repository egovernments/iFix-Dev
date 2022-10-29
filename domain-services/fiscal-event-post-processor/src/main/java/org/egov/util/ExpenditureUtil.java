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
import org.egov.web.models.Expenditure;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class ExpenditureUtil {
    @Autowired
    FiscalEventPostProcessorConfig processorConfig;

    @Autowired
    ServiceRequestRepository serviceRequestRepository;

    @Autowired
    ObjectMapper objectMapper;

    /**
     * @param tenantId
     * @param expenditureId
     * @param requestHeader
     * @return
     */
    public List<Expenditure> getExpenditureReference(String tenantId, String expenditureId, RequestHeader requestHeader) {
        if (!StringUtils.isEmpty(tenantId) && !StringUtils.isEmpty(expenditureId)) {
            Map<String, Object> expenditureValueMap = new HashMap<>();
            expenditureValueMap.put(MasterDataConstants.IDS, Collections.singletonList(expenditureId));
            expenditureValueMap.put(MasterDataConstants.CRITERIA_TENANT_ID, tenantId);

            Map<String, Object> expenditureMap = new HashMap<>();
            expenditureMap.put(MasterDataConstants.REQUEST_HEADER, requestHeader);
            expenditureMap.put(MasterDataConstants.CRITERIA, expenditureValueMap);

            Object response = serviceRequestRepository.fetchResult(createSearchExpenditureUrl(), expenditureMap);

            try {
                List<Expenditure> expenditureList = JsonPath.read(response, MasterDataConstants.EXPENDITURE_JSON_PATH);

                return objectMapper.convertValue(expenditureList, new TypeReference<List<Expenditure>>() {
                });
            } catch (Exception e) {
                throw new CustomException(MasterDataConstants.JSONPATH_ERROR, "Failed to parse project response for projectId");
            }
        }
        return Collections.emptyList();
    }

    private String createSearchExpenditureUrl() {
        StringBuilder uriBuilder = new StringBuilder();
        uriBuilder.append(processorConfig.getIfixMasterExpenditureHost())
                .append(processorConfig.getIfixMasterExpenditureContextPath())
                .append(processorConfig.getIfixMasterExpenditureSearchPath());
        return uriBuilder.toString();
    }
}
