package org.egov.service;

import java.util.List;

import org.egov.producer.Producer;
import org.egov.validator.GovernmentValidator;
import org.egov.web.models.Government;
import org.egov.web.models.GovernmentRequest;
import org.egov.web.models.GovernmentSearchRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GovernmentService {

    @Autowired
    GovernmentValidator governmentValidator;

    //@Autowired
    //GovernmentRepository governmentRepository;

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

       //.save(governmentRequest.getGovernment());

        return governmentRequest;
    }

    /**
     * @param governmentSearchRequest
     * @return
     */
    public List<Government> searchAllGovernmentByIdList(GovernmentSearchRequest governmentSearchRequest) {
        governmentValidator.validateGovernmentSearchRequestData(governmentSearchRequest);

       // return governmentRepository.findAllByIdList(governmentSearchRequest.getCriteria().getIds());
        return null;
    }
}
