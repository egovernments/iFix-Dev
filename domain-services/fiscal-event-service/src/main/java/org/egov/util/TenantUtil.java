package org.egov.util;

import com.jayway.jsonpath.JsonPath;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.egov.common.contract.request.RequestHeader;
import org.egov.config.FiscalEventConfiguration;
import org.egov.repository.ServiceRequestRepository;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class TenantUtil {

    @Autowired
    FiscalEventConfiguration fiscalEventConfiguration;

    @Autowired
    ServiceRequestRepository serviceRequestRepository;

    /**
     * @param tenantIds
     * @param requestHeader
     * @return
     */
    public boolean validateTenant(List<String> tenantIds, RequestHeader requestHeader) {
        if (tenantIds!= null && !tenantIds.isEmpty() && requestHeader != null) {

            Map<String, Object> tenantValueMap = new HashMap<>();
            tenantValueMap.put(MasterDataConstants.IDS,tenantIds);

            Map<String, Object> tenantMap = new HashMap<>();
            tenantMap.put(MasterDataConstants.REQUEST_HEADER, requestHeader);
            tenantMap.put(MasterDataConstants.CRITERIA, tenantValueMap);

            Object response = serviceRequestRepository.fetchResult(createSearchTenantUrl(), tenantMap);

            try {
                List list = JsonPath.read(response, MasterDataConstants.TENANT_LIST);

                return (list != null && !list.isEmpty() && (list.size() == tenantIds.size()));
            } catch (Exception e) {
                throw new CustomException(MasterDataConstants.JSONPATH_ERROR, "Failed to parse government response for tenantId");
            }
        }
        return false;
    }


    /**
     * @return
     */
    private String createSearchTenantUrl() {
        StringBuilder uriBuilder = new StringBuilder();
        uriBuilder.append(fiscalEventConfiguration.getIfixMasterGovernmentHost())
                .append(fiscalEventConfiguration.getIfixMasterGovernmentContextPath())
                .append(fiscalEventConfiguration.getIfixMasterGovernmentSearchPath());
        return uriBuilder.toString();
    }

}
