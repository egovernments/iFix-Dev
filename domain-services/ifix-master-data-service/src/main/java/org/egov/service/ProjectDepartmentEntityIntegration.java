package org.egov.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.request.RequestHeader;
import org.egov.config.MasterDataServiceConfiguration;
import org.egov.repository.ServiceRequestRepository;
import org.egov.tracer.model.CustomException;
import org.egov.util.MasterDataConstants;
import org.egov.web.models.DepartmentEntity;
import org.egov.web.models.DepartmentEntityAttributes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class ProjectDepartmentEntityIntegration {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MasterDataServiceConfiguration mdsConfiguration;
    @Autowired
    private ServiceRequestRepository serviceRequestRepository;

    public DepartmentEntity getDepartmentEntityForId(RequestHeader requestHeader, String tenantId,
                                                     String departmentEntityId) {
        JsonNode searchRequest = createDepartmentEntitySearchRequest(requestHeader, tenantId, departmentEntityId);
        Object response = serviceRequestRepository.fetchResult(createDepartmentEntitySearchUrl(), searchRequest);
        JsonNode responseJson = objectMapper.convertValue(response, JsonNode.class);

        JsonNode departmentEntityDetails = responseJson.get("departmentEntity").get(0);

        DepartmentEntity departmentEntity = getCurrentDepartmentEntity(departmentEntityDetails);
        departmentEntity.setAncestry(createAncestryArrayFor(departmentEntityDetails));

        return departmentEntity;
    }

    private JsonNode createDepartmentEntitySearchRequest(RequestHeader requestHeader, String tenantId,
                                                         String departmentEntityId) {
        ArrayNode departmentEntityIds = objectMapper.createArrayNode();
        departmentEntityIds.add(departmentEntityId);

        ObjectNode searchCriteria = objectMapper.createObjectNode();
        searchCriteria.set("Ids", departmentEntityIds);
        searchCriteria.put("tenantId", tenantId);
        searchCriteria.put("getAncestry", true);

        ObjectNode searchRequest = objectMapper.createObjectNode();
        JsonNode requestHeaderJson = objectMapper.convertValue(requestHeader, JsonNode.class);
        searchRequest.set("requestHeader", requestHeaderJson);
        searchRequest.set("criteria", searchCriteria);

        return searchRequest;
    }

    private DepartmentEntity getCurrentDepartmentEntity(JsonNode departmentEntityDetails) {
        if (departmentEntityDetails != null) {
            while (departmentEntityDetails.get("children").size() != 0) {
                departmentEntityDetails = departmentEntityDetails.get("children").get(0);
            }
            DepartmentEntity departmentEntity = DepartmentEntity.builder()
                    .id(departmentEntityDetails.get("id") != null ? departmentEntityDetails.get("id").asText() : null)
                    .code(departmentEntityDetails.get("code") != null
                            ? departmentEntityDetails.get("code").asText() : null)
                    .name(departmentEntityDetails.get("name") != null
                            ? departmentEntityDetails.get("name").asText() : null)
                    .hierarchyLevel(departmentEntityDetails.get("hierarchyLevel") != null
                            ? departmentEntityDetails.get("hierarchyLevel").asInt() : null)
                    .departmentId(departmentEntityDetails.get("departmentId") != null
                            ? departmentEntityDetails.get("departmentId").asText() : null)
                    .build();
            return departmentEntity;
        } else {
            throw new CustomException(MasterDataConstants.DEPARTMENT_ENTITY_ID, "Department entity details are missing");
        }
    }

    private List<DepartmentEntityAttributes> createAncestryArrayFor(JsonNode departmentEntityDetails) {
        List<DepartmentEntityAttributes> ancestry = new ArrayList<>();
        while (departmentEntityDetails != null) {
            DepartmentEntityAttributes departmentEntityAttributes = DepartmentEntityAttributes.builder()
                    .id(departmentEntityDetails.get("id").asText())
                    .code(departmentEntityDetails.get("code").asText())
                    .name(departmentEntityDetails.get("name").asText())
                    .hierarchyLevel(departmentEntityDetails.get("hierarchyLevel").asInt())
                    .build();
            ancestry.add(departmentEntityAttributes);
            if (departmentEntityDetails.get("children").size() != 0)
                departmentEntityDetails = departmentEntityDetails.get("children").get(0);
            else
                departmentEntityDetails = null;
        }
        return ancestry;
    }

    private String createDepartmentEntitySearchUrl() {
        StringBuilder uriBuilder = new StringBuilder();
        uriBuilder.append(mdsConfiguration.getDepartmentEntityHost())
                .append(mdsConfiguration.getDepartmentEntityContextPath())
                .append(mdsConfiguration.getDepartmentEntitySearchPath());
        return uriBuilder.toString();
    }

}
