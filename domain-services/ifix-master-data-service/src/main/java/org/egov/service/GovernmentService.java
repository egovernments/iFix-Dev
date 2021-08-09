package org.egov.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.extern.slf4j.Slf4j;
import org.egov.producer.Producer;
import org.egov.repository.GovernmentRepository;
import org.egov.tracer.model.ServiceCallException;
import org.egov.validator.GovernmentValidator;
import org.egov.web.models.Government;
import org.egov.web.models.GovernmentRequest;
import org.egov.web.models.GovernmentSearchRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class GovernmentService {

    @Autowired
    GovernmentValidator governmentValidator;

    @Autowired
    GovernmentRepository governmentRepository;

    @Autowired
    GovernmentEnrichmentService governmentEnrichmentService;

    @Autowired
    private Producer producer;

    /**
     * @param governmentRequest
     * @return
     */
    public GovernmentRequest addGovernment(GovernmentRequest governmentRequest) {
        governmentValidator.validateGovernmentRequestData(governmentRequest);
        governmentEnrichmentService.enrichGovernmentData(governmentRequest);

        governmentRepository.save(governmentRequest.getGovernment());

        return governmentRequest;
    }

    /**
     * @param governmentSearchRequest
     * @return
     */
    public List<Government> searchAllGovernmentByIdList(GovernmentSearchRequest governmentSearchRequest) {
        governmentValidator.validateGovernmentSearchRequestData(governmentSearchRequest);

        return governmentRepository.findAllByIdList(governmentSearchRequest.getCriteria().getIds());
    }

    @Component
    @Slf4j
    public static class ServiceRequestRepository {

        private ObjectMapper mapper;

        private RestTemplate restTemplate;


        @Autowired
        public ServiceRequestRepository(ObjectMapper mapper, RestTemplate restTemplate) {
            this.mapper = mapper;
            this.restTemplate = restTemplate;
        }


        public Object fetchResult(StringBuilder uri, Object request) {
            mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
            Object response = null;
            try {
                response = restTemplate.postForObject(uri.toString(), request, Map.class);
            }catch(HttpClientErrorException e) {
                log.error("External Service threw an Exception: ",e);
                throw new ServiceCallException(e.getResponseBodyAsString());
            }catch(Exception e) {
                log.error("Exception while fetching from searcher: ",e);
            }

            return response;
        }
    }
}
