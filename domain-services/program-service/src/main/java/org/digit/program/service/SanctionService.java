package org.digit.program.service;

import lombok.extern.slf4j.Slf4j;
import org.digit.program.configuration.ProgramConfiguration;
import org.digit.program.kafka.ProgramProducer;
import org.digit.program.models.sanction.Sanction;
import org.digit.program.models.sanction.SanctionRequest;
import org.digit.program.models.sanction.SanctionSearchRequest;
import org.digit.program.models.sanction.SanctionSearchResponse;
import org.digit.program.repository.SanctionRepository;
import org.digit.program.utils.DispatcherUtil;
import org.digit.program.utils.ErrorHandler;
import org.digit.program.validator.CommonValidator;
import org.digit.program.validator.SanctionValidator;
import org.egov.tracer.model.CustomException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class SanctionService {

    private final SanctionRepository sanctionRepository;
    private final EnrichmentService enrichmentService;
    private final SanctionValidator sanctionValidator;
    private final DispatcherUtil dispatcherUtil;
    private final CommonValidator commonValidator;
    private final ProgramProducer producer;
    private final ProgramConfiguration configs;
    private final ErrorHandler errorHandler;

    public SanctionService(SanctionRepository sanctionRepository, EnrichmentService enrichmentService, SanctionValidator sanctionValidator, DispatcherUtil dispatcherUtil, CommonValidator commonValidator, ProgramProducer producer, ProgramConfiguration configs, ErrorHandler errorHandler) {
        this.sanctionRepository = sanctionRepository;
        this.enrichmentService = enrichmentService;
        this.sanctionValidator = sanctionValidator;
        this.dispatcherUtil = dispatcherUtil;
        this.commonValidator = commonValidator;
        this.producer = producer;
        this.configs = configs;
        this.errorHandler = errorHandler;
    }

    public SanctionRequest pushToKafka(SanctionRequest sanctionRequest, String action, String messageType) {
        log.info("pushToKafka");
        commonValidator.validateRequest(sanctionRequest.getHeader(), action, messageType);
        producer.push(configs.getSanctionTopic(), sanctionRequest);
        return sanctionRequest;
    }

    public void createSanction(SanctionRequest sanctionRequest) {
        log.info("createSanction");
        try {
            sanctionValidator.validateSanction(sanctionRequest.getSanctions(), true);
            commonValidator.validateReply(sanctionRequest.getHeader(), sanctionRequest.getSanctions().get(0)
                            .getProgramCode(),
                    sanctionRequest.getSanctions().get(0).getLocationCode());
            enrichmentService.enrichSanctionCreate(sanctionRequest.getSanctions(),
                    sanctionRequest.getHeader());
            sanctionRepository.saveSanction(sanctionRequest.getSanctions());
            dispatcherUtil.dispatchOnSanction(sanctionRequest);
        } catch (CustomException exception) {
            errorHandler.handleSanctionError(sanctionRequest, exception);
        }
    }

    public void updateSanction(SanctionRequest sanctionRequest) {
        log.info("updateSanction");
        try {
            sanctionValidator.validateSanction(sanctionRequest.getSanctions(), false);
            commonValidator.validateReply(sanctionRequest.getHeader(), sanctionRequest.getSanctions().get(0).getProgramCode(),
                    sanctionRequest.getSanctions().get(0).getLocationCode());
            enrichmentService.enrichSanctionUpdate(sanctionRequest.getSanctions(), sanctionRequest.getHeader().getSenderId());
            sanctionRepository.updateSanction(sanctionRequest.getSanctions());
            dispatcherUtil.dispatchOnSanction(sanctionRequest);
        } catch (CustomException exception) {
            errorHandler.handleSanctionError(sanctionRequest, exception);
        }
    }

    public SanctionSearchResponse searchSanction(SanctionSearchRequest sanctionSearchRequest, String action, String messageType) {
        log.info("searchSanction");
        List<Sanction> sanctions;
        commonValidator.validateRequest(sanctionSearchRequest.getHeader(), action, messageType);
        sanctions = sanctionRepository.searchSanction(sanctionSearchRequest.getSanctionSearch());
        return SanctionSearchResponse.builder().header(sanctionSearchRequest.getHeader()).sanctions(sanctions).build();
    }

}
