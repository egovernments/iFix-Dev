package org.egov.util;

import com.jayway.jsonpath.JsonPath;
import org.apache.commons.lang3.StringUtils;
import org.egov.config.FiscalEventConfiguration;
import org.egov.repository.ServiceRequestRepository;
import org.egov.tracer.model.CustomException;
import org.egov.web.models.FiscalEventRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ProjectUtil {
    @Autowired
    FiscalEventConfiguration fiscalEventConfiguration;

    @Autowired
    ServiceRequestRepository serviceRequestRepository;

    /**
     * @param fiscalEventRequest
     * @return
     */
    public boolean validateProjectId(FiscalEventRequest fiscalEventRequest) {
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

            try{
                List list = JsonPath.read(response, MasterDataConstants.$_PROJECT_LIST);

                return list != null && !list.isEmpty();
            }catch (Exception e){
                throw new CustomException(MasterDataConstants.JSONPATH_ERROR,"Failed to parse project response for projectId");
            }
        }
        return false;
    }

    private String createSearchProjectUrl() {
        StringBuilder uriBuilder = new StringBuilder();
        uriBuilder.append(fiscalEventConfiguration.getIfixMasterProjectHost())
                .append(fiscalEventConfiguration.getIfixMasterProjectContextPath())
                .append(fiscalEventConfiguration.getIfixMasterProjectSearchPath());
        return uriBuilder.toString();
    }
}
