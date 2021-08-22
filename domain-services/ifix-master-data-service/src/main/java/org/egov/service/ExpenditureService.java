package org.egov.service;

import org.egov.repository.ExpenditureRepository;
import org.egov.validator.ExpenditureValidator;
import org.egov.web.models.Expenditure;
import org.egov.web.models.ExpenditureSearchRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExpenditureService {

    @Autowired
    ExpenditureRepository expenditureRepository;

    @Autowired
    ExpenditureValidator expenditureValidator;

    /**
     * @param expenditureSearchRequest
     * @return
     */
    public List<Expenditure> findAllByCriteria(ExpenditureSearchRequest expenditureSearchRequest) {
        expenditureValidator.validateExpenditureSearchRequest(expenditureSearchRequest);
        return expenditureRepository.findAllByCriteria(expenditureSearchRequest.getCriteria());
    }

}
