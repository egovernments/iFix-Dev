package org.egov.util;

import com.jayway.jsonpath.JsonPath;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.egov.common.contract.request.RequestHeader;
import org.egov.config.FiscalEventConfiguration;
import org.egov.repository.ServiceRequestRepository;
import org.egov.tracer.model.CustomException;
import org.egov.web.models.FiscalEventRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@Slf4j
public class TenantUtil {

    @Autowired
    FiscalEventConfiguration fiscalEventConfiguration;

    @Autowired
    ServiceRequestRepository serviceRequestRepository;

    /**
     *
     * @param tenantId
     * @param requestHeader
     * @return
     */
    public boolean validateTenant(String tenantId, RequestHeader requestHeader) {
        if (StringUtils.isNotBlank(tenantId) && requestHeader != null) {

            Map<String, Object> tenantValueMap = new HashMap<>();
            tenantValueMap.put(MasterDataConstants.IDS,
                    Collections.singletonList(tenantId));

            Map<String, Object> tenantMap = new HashMap<>();
            tenantMap.put(MasterDataConstants.REQUEST_HEADER, requestHeader);
            tenantMap.put(MasterDataConstants.CRITERIA, tenantValueMap);

            Object response = serviceRequestRepository.fetchResult(createSearchTenantUrl(), tenantMap);

            try{
                List list = JsonPath.read(response, MasterDataConstants.TENANT_LIST);

                return list != null && !list.isEmpty();
            }catch (Exception e){
                throw new CustomException(MasterDataConstants.JSONPATH_ERROR,"Failed to parse government response for tenantId");
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
