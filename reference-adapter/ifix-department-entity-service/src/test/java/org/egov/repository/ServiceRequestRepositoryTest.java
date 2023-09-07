package org.egov.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.egov.tracer.model.ServiceCallException;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Disabled("TODO: Need to work on it")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
class ServiceRequestRepositoryTest {

    @InjectMocks
    private ServiceRequestRepository serviceRequestRepository;

    @Spy
    private ObjectMapper mapper;

    @Mock
    private RestTemplate restTemplate;

    @Test
    void testFetchResultWithRestTemplateReturn() throws RestClientException {
        when(restTemplate.postForObject((String) any(), (Object) any(), (Class<Object>) any(), (Object[]) any()))
                .thenReturn("Post For Object");
        assertEquals("Post For Object",
                (serviceRequestRepository).fetchResult("Uri", "Request"));
        verify(restTemplate).postForObject((String) any(), (Object) any(), (Class<Object>) any(), (Object[]) any());
    }

    @Test
    void testFetchResultWithHttpClientError() throws RestClientException {
        when(restTemplate.postForObject((String) any(), (Object) any(), (Class<Object>) any(), (Object[]) any()))
                .thenThrow(new HttpClientErrorException(HttpStatus.CONTINUE));
        assertThrows(ServiceCallException.class,
                () -> (serviceRequestRepository).fetchResult("Uri", "Request"));
        verify(restTemplate).postForObject((String) any(), (Object) any(), (Class<Object>) any(), (Object[]) any());
    }

    @Test
    void testFetchResultWithServiceCallException() throws RestClientException {
        when(restTemplate.postForObject((String) any(), (Object) any(), (Class<Object>) any(), (Object[]) any()))
                .thenThrow(new ServiceCallException("An error occurred"));
        assertNull(serviceRequestRepository.fetchResult("Uri", "Request"));
        verify(restTemplate).postForObject((String) any(), (Object) any(), (Class<Object>) any(), (Object[]) any());
    }

    @Test
    void testFetchResult() throws RestClientException {
        ObjectMapper objectMapper = mock(ObjectMapper.class);
        when(objectMapper.configure((com.fasterxml.jackson.databind.SerializationFeature) any(), anyBoolean()))
                .thenReturn(new ObjectMapper());
        when(restTemplate.postForObject((String) any(), (Object) any(), (Class<Object>) any(), (Object[]) any()))
                .thenReturn("Post For Object");
        assertEquals("Post For Object",
                (serviceRequestRepository).fetchResult("Uri", "Request"));
        verify(mapper).configure((com.fasterxml.jackson.databind.SerializationFeature) any(), anyBoolean());
        verify(restTemplate).postForObject((String) any(), (Object) any(), (Class<Object>) any(), (Object[]) any());
    }
}

