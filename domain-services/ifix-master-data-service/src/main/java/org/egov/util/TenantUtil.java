package org.egov.util;

import com.jayway.jsonpath.JsonPath;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.egov.common.contract.request.RequestHeader;
import org.egov.config.MasterDataServiceConfiguration;
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
    private MasterDataServiceConfiguration configuration;

    @Autowired
    private ServiceRequestRepository searchRequestRepository;

    /**
     * @param tenantId
     * @param requestHeader
     * @return
     */
    public boolean validateTenant(String tenantId, RequestHeader requestHeader) {
        if (StringUtils.isNotBlank(tenantId)) {
            Map<String, Object> tenantValueMap = new HashMap<>();
            tenantValueMap.put(MasterDataConstants.IDS, Collections.singletonList(tenantId.trim()));

            Map<String, Object> tenantMap = new HashMap<>();
            tenantMap.put(MasterDataConstants.REQUEST_HEADER, requestHeader);
            tenantMap.put(MasterDataConstants.CRITERIA, tenantValueMap);

            Object response = searchRequestRepository.fetchResult(createSearchGovernmentUrl(), tenantMap);
            try {
                List list = JsonPath.read(response, MasterDataConstants.TENANT_LIST);

                return list != null && !list.isEmpty();
            } catch (Exception e) {
                throw new CustomException(MasterDataConstants.JSONPATH_ERROR, "Failed to parse government " +
                        "response for tenantId");
            }
        }

        return false;
    }

    /**
     * @return
     */
    private String createSearchGovernmentUrl() {
        StringBuilder uriBuilder = new StringBuilder();
        uriBuilder.append(configuration.getIfixMasterGovernmentHost())
                .append(configuration.getIfixMasterGovernmentContextPath())
                .append(configuration.getIfixMasterGovernmentSearchPath());
        return uriBuilder.toString();
    }

}
