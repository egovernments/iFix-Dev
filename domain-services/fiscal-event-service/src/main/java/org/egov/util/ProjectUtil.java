package org.egov.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import org.apache.commons.lang3.StringUtils;
import org.egov.config.FiscalEventConfiguration;
import org.egov.repository.ServiceRequestRepository;
import org.egov.tracer.model.CustomException;
import org.egov.web.models.FiscalEventRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class ProjectUtil {
    @Autowired
    FiscalEventConfiguration fiscalEventConfiguration;

    @Autowired
    ServiceRequestRepository serviceRequestRepository;

    @Autowired
    ObjectMapper objectMapper;

    /**
     * @param fiscalEventRequest
     * @return
     */
    public Optional<JsonNode> validateProjectId(FiscalEventRequest fiscalEventRequest) {
        if (fiscalEventRequest != null && fiscalEventRequest.getRequestHeader() != null
                && fiscalEventRequest.getFiscalEvent() != null
                && !StringUtils.isEmpty(fiscalEventRequest.getFiscalEvent().getProjectId())) {

            Map<String, Object> projectValueMap = new HashMap<>();
            projectValueMap.put(MasterDataConstants.IDS,
                    Collections.singletonList(fiscalEventRequest.getFiscalEvent().getProjectId()));
            projectValueMap.put(MasterDataConstants.CRITERIA_TENANT_ID,
                    fiscalEventRequest.getFiscalEvent().getTenantId());

            Map<String, Object> ProjectMap = new HashMap<>();
            ProjectMap.put(MasterDataConstants.REQUEST_HEADER, fiscalEventRequest.getRequestHeader());
            ProjectMap.put(MasterDataConstants.CRITERIA, projectValueMap);

            Object response = serviceRequestRepository.fetchResult(createSearchProjectUrl(), ProjectMap);
            JsonNode jsonNode = objectMapper.convertValue(response, JsonNode.class);
            return Optional.ofNullable(jsonNode);
        }
        return Optional.empty();
    }

    private String createSearchProjectUrl() {
        StringBuilder uriBuilder = new StringBuilder();
        uriBuilder.append(fiscalEventConfiguration.getIfixMasterProjectHost())
                .append(fiscalEventConfiguration.getIfixMasterProjectContextPath())
                .append(fiscalEventConfiguration.getIfixMasterProjectSearchPath());
        return uriBuilder.toString();
    }
}
