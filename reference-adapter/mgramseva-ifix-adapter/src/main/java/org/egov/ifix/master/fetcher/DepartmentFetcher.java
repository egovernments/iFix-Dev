package org.egov.ifix.master.fetcher;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;
import org.egov.ifix.utils.ApplicationConfiguration;
import org.egov.ifix.utils.ServiceRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class DepartmentFetcher {

    @Autowired
    ApplicationConfiguration applicationConfiguration;
    @Autowired
    ServiceRequestRepository serviceRequestRepository;
    @Autowired
    ObjectMapper objectMapper;

    public ObjectNode getDepartmentDetails(String departmentUuid) {
        String tenantId = applicationConfiguration.getTenantId();
        JsonNode searchRequest = createDepartmentSearchRequest(tenantId, departmentUuid);

        Object response = serviceRequestRepository.fetchResult(createSearchDepartmentUrl(), searchRequest);
        JsonNode responseJson = objectMapper.convertValue(response, JsonNode.class);
        JsonNode departmentDetails = responseJson.get("department").get(0);

        ObjectNode department = objectMapper.createObjectNode();
        department.put("id", departmentDetails.get("id").asText());
        department.put("code", departmentDetails.get("code").asText());
        department.put("name", departmentDetails.get("name").asText());

        return department;
    }

    private JsonNode createDepartmentSearchRequest(String tenantId, String departmentUuid) {
        ArrayNode departmentIds = objectMapper.createArrayNode();
        departmentIds.add(departmentUuid);

        ObjectNode searchCriteria = objectMapper.createObjectNode();
        searchCriteria.set("Ids", departmentIds);
        searchCriteria.put("tenantId", tenantId);

        ObjectNode searchRequest = objectMapper.createObjectNode();
        searchRequest.set("requestHeader", objectMapper.createObjectNode());
        searchRequest.set("criteria", searchCriteria);

        return searchRequest;
    }

    private String createSearchDepartmentUrl() {
        StringBuilder uriBuilder = new StringBuilder();
        uriBuilder.append(applicationConfiguration.getAdapterMasterDataHost())
                .append(applicationConfiguration.getAdapterMasterDataContextPath())
                .append(applicationConfiguration.getAdapterMasterDepartmentSearchPath());
        return uriBuilder.toString();
    }

}
