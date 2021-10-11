package org.egov.util;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.egov.config.FiscalEventConfiguration;
import org.egov.config.TestDataFormatter;
import org.egov.repository.ServiceRequestRepository;
import org.egov.web.models.FiscalEventRequest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
@Slf4j
class ProjectUtilTest {

    @Autowired
    private TestDataFormatter testDataFormatter;

    @Mock
    private FiscalEventConfiguration fiscalEventConfiguration;
    @Mock
    private ServiceRequestRepository serviceRequestRepository;
    @Spy
    private ObjectMapper objectMapper;

    @InjectMocks
    private ProjectUtil projectUtil;

    private FiscalEventRequest fiscalEventRequest;
    private JsonNode projectSearchResponse;

    @BeforeAll
    public void init() throws IOException {
        fiscalEventRequest = testDataFormatter.getFiscalEventPushRequestData();
        projectSearchResponse = testDataFormatter.getProjectSearchResponse();
    }

    @Test
    void testValidateProjectIdWithEmptyFiscalEvent() {
        assertFalse(projectUtil.validateProjectId(new FiscalEventRequest()).isPresent());
    }

    @Test
    void testValidateProjectIdWithValidProjectId() {
        Map<String, Object> map = objectMapper.convertValue(projectSearchResponse, new TypeReference<Map<String, Object>>() {});
        doReturn(map).when(serviceRequestRepository).fetchResult(any(), any());
        Optional<JsonNode> response = projectUtil.validateProjectId(fiscalEventRequest);
        assertTrue(response.isPresent());
    }

}
