package org.egov.ifix.master.fetcher;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.egov.ifix.exception.HttpCustomException;
import org.egov.ifix.utils.ApplicationConfiguration;
import org.egov.ifix.utils.ServiceRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import static org.egov.ifix.utils.EventConstants.DEPARTMENT_ENTITY_CODE;

@Component
public class DepartmentEntityFetcher {

    @Autowired
    ApplicationConfiguration applicationConfiguration;
    @Autowired
    ServiceRequestRepository serviceRequestRepository;
    @Autowired
    ObjectMapper objectMapper;

    public ObjectNode getDepartmentEntityDetailsFromCode(String code) {
        String tenantId = applicationConfiguration.getTenantId();
        JsonNode searchRequest = createDepartmentEntitySearchRequest(tenantId, code);
        Object response = serviceRequestRepository.fetchResult(createDepartmentEntitySearchUrl(), searchRequest);
        JsonNode responseJson = objectMapper.convertValue(response, JsonNode.class);

        JsonNode departmentEntityNode = responseJson.get("departmentEntity");

        if (departmentEntityNode == null || departmentEntityNode.size() == 0) {
            throw new HttpCustomException(DEPARTMENT_ENTITY_CODE, "Unable to find department entity by code: " + code,
                    HttpStatus.BAD_REQUEST);
        }else if (departmentEntityNode != null && departmentEntityNode.size() > 1) {
            throw new HttpCustomException(DEPARTMENT_ENTITY_CODE, "Multiple department entity found",
                    HttpStatus.BAD_REQUEST);
        }

        JsonNode departmentEntityDetails = departmentEntityNode.get(0);

        ObjectNode departmentEntity = getCurrentDepartmentEntity(departmentEntityDetails);
        departmentEntity.set("ancestry", createAncestryArrayFor(departmentEntityDetails));

        return departmentEntity;
    }

    private JsonNode createDepartmentEntitySearchRequest(String tenantId,
                                                         String departmentEntityCode) {
        ObjectNode searchCriteria = objectMapper.createObjectNode();
        searchCriteria.put("code", departmentEntityCode);
        searchCriteria.put("tenantId", tenantId);
        searchCriteria.put("getAncestry", true);

        ObjectNode searchRequest = objectMapper.createObjectNode();
        searchRequest.set("requestHeader", objectMapper.createObjectNode());
        searchRequest.set("criteria", searchCriteria);

        return searchRequest;
    }

    private ObjectNode getCurrentDepartmentEntity(JsonNode departmentEntityDetails) {
        if (departmentEntityDetails != null) {
            while (departmentEntityDetails.get("children").size() != 0) {
                departmentEntityDetails = departmentEntityDetails.get("children").get(0);
            }

            ObjectNode departmentEntity = objectMapper.createObjectNode();
            departmentEntity.put("id", departmentEntityDetails.get("id").asText());
            departmentEntity.put("code", departmentEntityDetails.get("code").asText());
            departmentEntity.put("name", departmentEntityDetails.get("name").asText());
            departmentEntity.put("hierarchyLevel", departmentEntityDetails.get("hierarchyLevel").asInt());
            departmentEntity.put("departmentId", departmentEntityDetails.get("departmentId").asText());

            return departmentEntity;
        }
        return null;
    }

    private ArrayNode createAncestryArrayFor(JsonNode departmentEntityDetails) {
        ArrayNode ancestry = objectMapper.createArrayNode();
        while (departmentEntityDetails != null) {
            ObjectNode departmentEntityAttributes = objectMapper.createObjectNode();
            departmentEntityAttributes.put("id", departmentEntityDetails.get("id").asText());
            departmentEntityAttributes.put("code", departmentEntityDetails.get("code").asText());
            departmentEntityAttributes.put("name", departmentEntityDetails.get("name").asText());
            departmentEntityAttributes.put("hierarchyLevel", departmentEntityDetails.get("hierarchyLevel").asInt());
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
        uriBuilder.append(applicationConfiguration.getDepartmentEntityHost())
                .append(applicationConfiguration.getDepartmentEntityContextPath())
                .append(applicationConfiguration.getDepartmentEntitySearchPath());
        return uriBuilder.toString();
    }


}
