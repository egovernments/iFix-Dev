package org.egov.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.egov.config.FiscalEventPostProcessorConfig;
import org.egov.resposioty.ServiceRequestRepository;
import org.egov.tracer.model.CustomException;
import org.egov.web.models.FiscalEventRequest;
import org.egov.web.models.Government;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class GovernmentUtil {
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private FiscalEventPostProcessorConfig configuration;

    @Autowired
    private ServiceRequestRepository serviceRequestRepository;

    /**
     * @param fiscalEventRequest
     * @return
     */
    public List<Government> getGovernmentFromGovernmentService(FiscalEventRequest fiscalEventRequest) {
        if (fiscalEventRequest != null && fiscalEventRequest.getRequestHeader() != null
                && fiscalEventRequest.getFiscalEvent() != null
                && StringUtils.isNotBlank(fiscalEventRequest.getFiscalEvent().getTenantId())) {

            Map<String, Object> tenantValueMap = new HashMap<>();
            tenantValueMap.put(MasterDataConstants.IDS,
                    Collections.singletonList(fiscalEventRequest.getFiscalEvent().getTenantId()));

            Map<String, Object> tenantMap = new HashMap<>();
            tenantMap.put(MasterDataConstants.REQUEST_HEADER, fiscalEventRequest.getRequestHeader());
            tenantMap.put(MasterDataConstants.CRITERIA, tenantValueMap);

            Object response = serviceRequestRepository.fetchResult(createSearchTenantUrl(), tenantMap);
            List<Government> governments = null;
            try {
                governments = JsonPath.read(response, MasterDataConstants.TENANT_LIST);
            } catch (Exception e) {
                throw new CustomException(MasterDataConstants.JSONPATH_ERROR, "Failed to parse government response for tenantId");
            }

            if (governments != null && !governments.isEmpty()) {
                return objectMapper.convertValue(governments, new TypeReference<List<Government>>() {
                });
            }
        }
        return Collections.emptyList();
    }


    /**
     * @return
     */
    private String createSearchTenantUrl() {
        StringBuilder uriBuilder = new StringBuilder();
        uriBuilder.append(configuration.getIfixMasterGovernmentHost())
                .append(configuration.getIfixMasterGovernmentContextPath())
                .append(configuration.getIfixMasterGovernmentSearchPath());
        return uriBuilder.toString();
    }

}
