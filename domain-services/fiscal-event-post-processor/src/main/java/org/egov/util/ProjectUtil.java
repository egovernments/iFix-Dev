package org.egov.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.egov.config.FiscalEventPostProcessorConfig;
import org.egov.resposioty.ServiceRequestRepository;
import org.egov.tracer.model.CustomException;
import org.egov.web.models.DepartmentEntity;
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
     * @param idMap
     * @return
     */
    public JsonNode getProjectReference(FiscalEventRequest fiscalEventRequest) {
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
            return jsonNode;
        }
        return objectMapper.createObjectNode();
    }

    private String createSearchProjectUrl() {
        StringBuilder uriBuilder = new StringBuilder();
        uriBuilder.append(processorConfig.getIfixMasterProjectHost())
                .append(processorConfig.getIfixMasterProjectContextPath())
                .append(processorConfig.getIfixMasterProjectSearchPath());
        return uriBuilder.toString();
    }

    /**
     *
     * @param departmentEntityNode
     * @return
     */
    public DepartmentEntity getDepartmentEntityFromProject(JsonNode departmentEntityNode) {
        DepartmentEntity departmentEntity = null;
        try {
            departmentEntity = objectMapper.treeToValue(departmentEntityNode, DepartmentEntity.class);
        } catch (JsonProcessingException e) {
            throw new CustomException(MasterDataConstants.JSONPATH_ERROR, "Failed to parse project Node for department entity");
        }
        return departmentEntity;
    }
}

