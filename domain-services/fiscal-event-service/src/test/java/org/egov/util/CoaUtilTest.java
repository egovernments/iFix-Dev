package org.egov.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.egov.config.FiscalEventConfiguration;
import org.egov.config.TestDataFormatter;
import org.egov.repository.ServiceRequestRepository;
import org.egov.tracer.model.CustomException;
import org.egov.web.models.FiscalEventRequest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
@Slf4j
class CoaUtilTest {

    @Autowired
    private TestDataFormatter testDataFormatter;

    @Mock
    private FiscalEventConfiguration fiscalEventConfiguration;
    @Mock
    private ServiceRequestRepository serviceRequestRepository;
    @Spy
    private ObjectMapper objectMapper;

    @InjectMocks
    private CoaUtil coaUtil;

    private FiscalEventRequest fiscalEventRequest;
    private JsonNode validCOAResponse;

    @BeforeAll
    public void init() throws IOException {
        fiscalEventRequest = testDataFormatter.getFiscalEventPushRequestData();
        validCOAResponse = testDataFormatter.getCOASearchResponse();
    }

    @Test
    public void testGetCOAIdsFromCOAServiceWithInvalidResponse() {
        doReturn(objectMapper.createObjectNode()).when(serviceRequestRepository).fetchResult(any(), any());
        assertThrows(CustomException.class,
                () -> coaUtil.getCOAIdsFromCOAService(fiscalEventRequest.getRequestHeader(),
                        fiscalEventRequest.getFiscalEvent()));
    }

    @Test
    public void testGetCOAIdsFromCOAServiceWithValidResponse() {
        Map<String, Object> map = objectMapper.convertValue(validCOAResponse, new TypeReference<Map<String, Object>>() {});
        doReturn(map).when(serviceRequestRepository).fetchResult(any(), any());
        List<String> validCOAIds = coaUtil.getCOAIdsFromCOAService(fiscalEventRequest.getRequestHeader(),
                fiscalEventRequest.getFiscalEvent());
        assertTrue(validCOAIds.size() > 0);
    }

}
