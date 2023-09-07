package org.egov.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.egov.config.TestDataFormatter;
import org.egov.tracer.model.ServiceCallException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
class ServiceRequestRepositoryTest {
    /*@Autowired
    private TestDataFormatter testDataFormatter;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private ServiceRequestRepository serviceRequestRepository;

    @Test
    void testFetchResult() throws IOException {
        Object responseObject = new Object();
        when(objectMapper.configure((SerializationFeature) any(), anyBoolean())).thenReturn(new ObjectMapper());

        doReturn(responseObject).when(restTemplate).postForObject("URI", "Request", Map.class);

        Object actualResponseObject = serviceRequestRepository.fetchResult("URI", "Request");

        assertSame(responseObject, actualResponseObject);
    }

    @Test
    void testFetchResultHttpClientErrorException() throws IOException {
        when(objectMapper.configure((SerializationFeature) any(), anyBoolean())).thenReturn(new ObjectMapper());

        doThrow(new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR))
                .when(restTemplate).postForObject("URI", null, Map.class);

        assertThrows(ServiceCallException.class, () -> serviceRequestRepository.fetchResult("URI", null));
    }*/


}

