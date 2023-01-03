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

    public List<DepartmentEntity> getDepartmentEntityForIds(RequestHeader requestHeader, String tenantId,
                                                      List<String> departmentEntityIds) {
        JsonNode searchRequest = createDepartmentEntitySearchRequest(requestHeader, tenantId, departmentEntityIds);
        Object response = serviceRequestRepository.fetchResult(createDepartmentEntitySearchUrl(), searchRequest);
        JsonNode responseJson = objectMapper.convertValue(response, JsonNode.class);
        ArrayNode departmentEntityDetails = (ArrayNode) responseJson.get("departmentEntity");

        List<DepartmentEntity> departmentEntities = new ArrayList<>();
        for(JsonNode departmentEntityDetail : departmentEntityDetails) {
            DepartmentEntity departmentEntity = getCurrentDepartmentEntity(departmentEntityDetail);
            departmentEntity.setAncestry(createAncestryArrayFor(departmentEntityDetail));
            departmentEntities.add(departmentEntity);
        }

        return departmentEntities;
    }

    private JsonNode createDepartmentEntitySearchRequest(RequestHeader requestHeader, String tenantId,
                                                         List<String> departmentEntityIds) {
        ArrayNode departmentEntityIdArrayNode = objectMapper.createArrayNode();
        departmentEntityIds.forEach(departmentEntityId -> departmentEntityIdArrayNode.add(departmentEntityId));

        ObjectNode searchCriteria = objectMapper.createObjectNode();
        searchCriteria.set("Ids", departmentEntityIdArrayNode);
        searchCriteria.put("tenantId", tenantId);
        searchCriteria.put("getAncestry", true);

        ObjectNode searchRequest = objectMapper.createObjectNode();
        JsonNode requestHeaderJson = objectMapper.convertValue(requestHeader, JsonNode.class);
        searchRequest.set("requestHeader", requestHeaderJson);
        searchRequest.set("criteria", searchCriteria);

        return searchRequest;
    }

    private DepartmentEntity getCurrentDepartmentEntity(JsonNode departmentEntityDetail) {
        if (departmentEntityDetail != null) {
            while (departmentEntityDetail.get("children").size() != 0) {
                departmentEntityDetail = departmentEntityDetail.get("children").get(0);
            }
            DepartmentEntity departmentEntity = DepartmentEntity.builder()
                    .id(departmentEntityDetail.get("id") != null ? departmentEntityDetail.get("id").asText() : null)
                    .code(departmentEntityDetail.get("code") != null
                            ? departmentEntityDetail.get("code").asText() : null)
                    .name(departmentEntityDetail.get("name") != null
                            ? departmentEntityDetail.get("name").asText() : null)
                    .hierarchyLevel(departmentEntityDetail.get("hierarchyLevel") != null
                            ? departmentEntityDetail.get("hierarchyLevel").asInt() : null)
                    .departmentId(departmentEntityDetail.get("departmentId") != null
                            ? departmentEntityDetail.get("departmentId").asText() : null)
                    .build();
            return departmentEntity;
        } else {
            throw new CustomException(MasterDataConstants.DEPARTMENT_ENTITY_ID, "Department entity details are missing");
        }
    }

    private List<DepartmentEntityAttributes> createAncestryArrayFor(JsonNode departmentEntityDetail) {
        List<DepartmentEntityAttributes> ancestry = new ArrayList<>();
        while (departmentEntityDetail != null) {
            DepartmentEntityAttributes departmentEntityAttributes = DepartmentEntityAttributes.builder()
                    .id(departmentEntityDetail.get("id").asText())
                    .code(departmentEntityDetail.get("code").asText())
                    .name(departmentEntityDetail.get("name").asText())
                    .hierarchyLevel(departmentEntityDetail.get("hierarchyLevel").asInt())
                    .build();
            ancestry.add(departmentEntityAttributes);
            if (departmentEntityDetail.get("children").size() != 0)
                departmentEntityDetail = departmentEntityDetail.get("children").get(0);
            else
                departmentEntityDetail = null;
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
