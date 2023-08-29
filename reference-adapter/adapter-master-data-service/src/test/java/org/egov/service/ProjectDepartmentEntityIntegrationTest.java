package org.egov.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.egov.config.MasterDataServiceConfiguration;
import org.egov.config.TestDataFormatter;
import org.egov.repository.ServiceRequestRepository;
import org.egov.web.models.ProjectRequest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
//@SpringBootTest()
class ProjectDepartmentEntityIntegrationTest {

   /* @Autowired
    private TestDataFormatter testDataFormatter;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private MasterDataServiceConfiguration mdsConfiguration;

    @Mock
    private ServiceRequestRepository serviceRequestRepository;

    @InjectMocks
    private ProjectDepartmentEntityIntegration projectDepartmentEntityIntegration;

    private ProjectRequest projectCreateRequest;
    private String departmentEntitySearchResponse;

    private String departmentEntitySearchUrl;
    private ObjectNode objectNode;

    private JsonNode jsonNode;
    private ArrayNode arrayNode;

    @BeforeAll
    public void init() throws IOException {
        projectCreateRequest = testDataFormatter.getProjectCreateRequestData();
        departmentEntitySearchResponse = testDataFormatter.getDepartmentEntitySearchResponseDataAsString();

        doReturn(new String()).when(mdsConfiguration).getDepartmentEntityHost();
        doReturn(new String()).when(mdsConfiguration).getDepartmentEntityContextPath();
        doReturn(new String()).when(mdsConfiguration).getDepartmentEntitySearchPath();

        objectNode = new ObjectMapper().createObjectNode();
        arrayNode = new ObjectMapper().createArrayNode();
        jsonNode = new ObjectMapper().readTree(departmentEntitySearchResponse);
    }

    @Test
    void getDepartmentEntityForId() {
        assertNotNull(projectCreateRequest.getProjectDTO().getDepartmentEntityIds());

        when(objectMapper.convertValue((Object) any(), (Class<Object>) any()))
                .thenReturn(jsonNode);

        doReturn(arrayNode).when(objectMapper).createArrayNode();
        doReturn(objectNode).when(objectMapper).createObjectNode();

        doReturn(new Object()).when(serviceRequestRepository).fetchResult((String) any(), (JsonNode) any());

        projectDepartmentEntityIntegration
                .getDepartmentEntityForIds(projectCreateRequest.getRequestHeader(),
                        projectCreateRequest.getProjectDTO().getTenantId(),
                        projectCreateRequest.getProjectDTO().getDepartmentEntityIds());
    }*/
}