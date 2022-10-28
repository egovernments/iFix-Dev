package org.egov.repository;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.egov.tracer.model.ServiceCallException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
@Slf4j
@AllArgsConstructor
@NoArgsConstructor
public class ServiceRequestRepository {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RestTemplate restTemplate;

    /**
     * @param uri
     * @param request
     * @return
     */
    public Object fetchResult(String uri, Object request) {
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        Object response = null;
        try {
            response = restTemplate.postForObject(uri, request, Map.class);
        } catch (HttpClientErrorException e) {
            log.error("External Service threw an Exception: ", e);
            throw new ServiceCallException(e.getResponseBodyAsString());
        } catch (Exception e) {
            log.error("Exception while fetching from searcher: ", e);
        }

        return response;
    }
}
