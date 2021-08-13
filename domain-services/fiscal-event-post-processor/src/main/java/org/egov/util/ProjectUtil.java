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
import org.egov.web.models.Project;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class ProjectUtil {
    @Autowired
    FiscalEventPostProcessorConfig processorConfig;

    @Autowired
    ServiceRequestRepository serviceRequestRepository;

    @Autowired
    ObjectMapper objectMapper;

    /**
     * @param fiscalEventRequest
     * @return
     */
    public List<Project> getProjectReference(FiscalEventRequest fiscalEventRequest) {
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
                List<Project>  projectList = JsonPath.read(response, MasterDataConstants.PROJECT_LIST);

                return objectMapper.convertValue(projectList, new TypeReference<List<Project>>() {});
            }catch (Exception e){
                throw new CustomException(MasterDataConstants.JSONPATH_ERROR,"Failed to parse project response for projectId");
            }
        }
        return Collections.emptyList();
    }

    private String createSearchProjectUrl() {
        StringBuilder uriBuilder = new StringBuilder();
        uriBuilder.append(processorConfig.getIfixMasterProjectHost())
                .append(processorConfig.getIfixMasterProjectContextPath())
                .append(processorConfig.getIfixMasterProjectSearchPath());
        return uriBuilder.toString();
    }
}
