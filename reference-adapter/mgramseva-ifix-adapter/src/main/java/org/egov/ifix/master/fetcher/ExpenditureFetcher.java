package org.egov.ifix.master.fetcher;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.egov.ifix.utils.ApplicationConfiguration;
import org.egov.ifix.utils.ServiceRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ExpenditureFetcher {

    @Autowired
    ApplicationConfiguration applicationConfiguration;
    @Autowired
    ServiceRequestRepository serviceRequestRepository;
    @Autowired
    ObjectMapper objectMapper;

    public ObjectNode getExpenditureDetails(String expenditureUuid) {
        String tenantId = applicationConfiguration.getTenantId();
        JsonNode searchRequest = createExpenditureSearchRequest(tenantId, expenditureUuid);

        Object response = serviceRequestRepository.fetchResult(createSearchDepartmentUrl(), searchRequest);
        JsonNode responseJson = objectMapper.convertValue(response, JsonNode.class);
        JsonNode expenditureDetails = responseJson.get("expenditure").get(0);

        ObjectNode expenditure = objectMapper.createObjectNode();
        expenditure.put("id", expenditureDetails.get("id").asText());
        expenditure.put("code", expenditureDetails.get("code").asText());
        expenditure.put("name", expenditureDetails.get("name").asText());
        expenditure.put("type", expenditureDetails.get("type").asText());

        return expenditure;
    }

    private JsonNode createExpenditureSearchRequest(String tenantId, String expenditureUuid) {
        ArrayNode expenditureIds = objectMapper.createArrayNode();
        expenditureIds.add(expenditureUuid);

        ObjectNode searchCriteria = objectMapper.createObjectNode();
        searchCriteria.set("Ids", expenditureIds);
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
                .append(applicationConfiguration.getAdapterMasterExpenditureSearchPath());
        return uriBuilder.toString();
    }

}
