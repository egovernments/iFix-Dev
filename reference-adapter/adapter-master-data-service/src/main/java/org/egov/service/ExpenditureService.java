package org.egov.service;

import org.egov.repository.ExpenditureRepository;
import org.egov.validator.ExpenditureValidator;
import org.egov.web.models.Expenditure;
import org.egov.web.models.ExpenditureRequest;
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

    @Autowired
    private ExpenditureEnrichmentService enricher;

    /**
     * @param expenditureSearchRequest
     * @return
     */
    public List<Expenditure> findAllByCriteria(ExpenditureSearchRequest expenditureSearchRequest) {
        expenditureValidator.validateExpenditureSearchRequest(expenditureSearchRequest);
        return expenditureRepository.findAllByCriteria(expenditureSearchRequest.getCriteria());
    }

    public ExpenditureRequest createV1Expenditure(ExpenditureRequest expenditureRequest) {
        expenditureValidator.validateExpenditureCreateRequest(expenditureRequest);
        enricher.enrichCreateExpenditure(expenditureRequest);
        expenditureRepository.save(expenditureRequest.getExpenditure());
        return expenditureRequest;
    }
}
