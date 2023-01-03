package org.egov.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.egov.tracer.model.ServiceCallException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Map;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ServiceRequestRepositoryTest {

    @Spy
    private ObjectMapper objectMapper;

    @Spy
    private RestTemplate restTemplate;

    private ServiceRequestRepository serviceRequestRepository;


    @BeforeEach
    private void init() throws IOException {
        MockitoAnnotations.openMocks(this);
        serviceRequestRepository = new ServiceRequestRepository(objectMapper, restTemplate);
    }

    @Test
    void fetchResultWithDefaultRequest() {
        Object request = new Object();
        String uri = "http://localhost:8080/abcd";
        assertNull(serviceRequestRepository.fetchResult(uri, request));
    }

    @Test
    void fetchResultWithClientError() {
        Object request = new Object();
        String uri = "http://localhost:8080/abcd";
        doThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST)).when(restTemplate).postForObject(uri, request, Map.class);
        assertThrows(ServiceCallException.class, () -> serviceRequestRepository.fetchResult(uri, request));
    }

    @Test
    void fetchResultWithSuccessResponse() {
        Object request = new Object();
        String uri = "http://localhost:8080/abcd";
        doReturn(new Object()).when(restTemplate).postForObject(uri, request, Map.class);
        assertNotNull(serviceRequestRepository.fetchResult(uri, request));
    }
}