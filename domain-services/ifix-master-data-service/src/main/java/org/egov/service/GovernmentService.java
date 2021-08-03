package org.egov.service;

import org.egov.repository.GovernmentRepository;
import org.egov.validator.GovernmentValidator;
import org.egov.web.models.GovernmentRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GovernmentService {

    @Autowired
    GovernmentValidator governmentValidator;

    @Autowired
    GovernmentRepository governmentRepository;

    @Autowired
    GovernmentEnrichmentService governmentEnrichmentService;

    public GovernmentRequest addGovernment(GovernmentRequest governmentRequest) {
        governmentValidator.validateGovernmentRequestData(governmentRequest);
        governmentEnrichmentService.enrichGovernmentData(governmentRequest);

        governmentRepository.save(governmentRequest);

        return governmentRequest;
    }
}
