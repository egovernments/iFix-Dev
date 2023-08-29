package org.egov.service;

import org.egov.config.MasterDataServiceConfiguration;
import org.egov.repository.ExpenditureRepository;
import org.egov.util.DtoWrapper;
import org.egov.util.KafkaProducer;
import org.egov.validator.ExpenditureValidator;
import org.egov.web.models.ExpenditureDTO;
import org.egov.web.models.ExpenditureRequest;
import org.egov.web.models.ExpenditureSearchRequest;
import org.egov.web.models.persist.Expenditure;
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

    @Autowired
    private KafkaProducer kafkaProducer;

    @Autowired
    private MasterDataServiceConfiguration masterDataServiceConfiguration;

    @Autowired
    private DtoWrapper dtoWrapper;

    /**
     * @param expenditureSearchRequest
     * @return
     */
    public List<ExpenditureDTO> findAllByCriteria(ExpenditureSearchRequest expenditureSearchRequest) {
        expenditureValidator.validateExpenditureSearchRequest(expenditureSearchRequest);
        List<Expenditure> expenditureList = expenditureRepository.findAllByCriteria(expenditureSearchRequest.getCriteria());

        return dtoWrapper.wrapExpenditureDTOByEntity(expenditureList);
    }

    public ExpenditureRequest createV1Expenditure(ExpenditureRequest expenditureRequest) {
        expenditureValidator.validateExpenditureCreateRequest(expenditureRequest);
        enricher.enrichCreateExpenditure(expenditureRequest);

        kafkaProducer.push(masterDataServiceConfiguration.getPersisterKafkaExpenditureCreateTopic(), expenditureRequest);

        return expenditureRequest;
    }
}
