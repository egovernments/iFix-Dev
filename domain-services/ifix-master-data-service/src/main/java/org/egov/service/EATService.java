package org.egov.service;

import org.egov.repository.EATRepository;
import org.egov.validator.EATValidator;
import org.egov.web.models.EAT;
import org.egov.web.models.EatSearchRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EATService {

    @Autowired
    EATRepository eatRepository;

    @Autowired
    EATValidator eatValidator;

    /**
     * @param eatSearchRequest
     * @return
     */
    public List<EAT> findAllByCriteria(EatSearchRequest eatSearchRequest) {
        eatValidator.validateEatSearchRequest(eatSearchRequest);
        return eatRepository.findAllByCriteria(eatSearchRequest.getCriteria());
    }

}
