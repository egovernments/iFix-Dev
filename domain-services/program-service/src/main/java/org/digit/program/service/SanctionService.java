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

    public SanctionService(SanctionRepository sanctionRepository, EnrichmentService enrichmentService,
                           SanctionValidator sanctionValidator, DispatcherUtil dispatcherUtil, CommonValidator commonValidator,
                           ProgramProducer producer, ProgramConfiguration configs, ErrorHandler errorHandler) {
        this.sanctionRepository = sanctionRepository;
        this.enrichmentService = enrichmentService;
        this.sanctionValidator = sanctionValidator;
        this.dispatcherUtil = dispatcherUtil;
        this.commonValidator = commonValidator;
        this.producer = producer;
        this.configs = configs;
        this.errorHandler = errorHandler;
    }

    /**
     * Pushes to kafka topic
     * @param sanctionRequest
     * @return
     */
    public SanctionRequest pushToKafka(SanctionRequest sanctionRequest) {
        log.info("pushToKafka");
        producer.push(configs.getSanctionTopic(), sanctionRequest);
        return sanctionRequest;
    }

    /**
     * Validates request, enriches, persists and dispatches on-sanction create request
     * @param sanctionRequest
     */
    public SanctionRequest createSanction(SanctionRequest sanctionRequest) {
        log.info("Creating Sanction");
        try {
            sanctionValidator.validateSanction(sanctionRequest.getSanction().getChildren(), true);
            // Validates if receiver id is same as configured in mdms
            commonValidator.validateReply(sanctionRequest.getHeader(), sanctionRequest.getSanction().getChildren().get(0).getLocationCode());
            enrichmentService.enrichSanctionCreate(sanctionRequest.getSanction(), sanctionRequest.getHeader());
            sanctionRepository.saveSanction(sanctionRequest.getSanction().getChildren());
            // Forwards on sanction message to exchange service if sender and current domains are same
            dispatcherUtil.dispatchOnSanction(sanctionRequest);
            log.info("Successfully Created Sanction");
        } catch (CustomException exception) {
            log.error("Error while creating sanction", exception);
            errorHandler.handleSanctionError(sanctionRequest, exception);
        }
        return sanctionRequest;
    }

    /**
     * Validates request, enriches, persists and dispatches on-sanction update request
     * @param sanctionRequest
     */
    public SanctionRequest updateSanction(SanctionRequest sanctionRequest) {
        log.info("updateSanction");
        try {
            sanctionValidator.validateSanction(sanctionRequest.getSanction().getChildren(), false);
            commonValidator.validateReply(sanctionRequest.getHeader(), sanctionRequest.getSanction().getChildren().get(0).getLocationCode());
            enrichmentService.enrichSanctionUpdate(sanctionRequest.getSanction().getChildren(), sanctionRequest.getHeader().getSenderId());
            sanctionRepository.updateSanction(sanctionRequest.getSanction().getChildren());
            dispatcherUtil.dispatchOnSanction(sanctionRequest);
            log.info("Successfully Updated Sanction");
        } catch (CustomException exception) {
            log.error("Error while updating sanction", exception);
            errorHandler.handleSanctionError(sanctionRequest, exception);
        }
        return sanctionRequest;
    }

    /**
     * Validates url messageType and action with header and searches for sanctions
     * @param sanctionSearchRequest
     * @param action
     * @param messageType
     * @return
     */
    public SanctionSearchResponse searchSanction(SanctionSearchRequest sanctionSearchRequest, String action, String messageType) {
        log.info("searchSanction");
        List<Sanction> sanctions;
        commonValidator.validateRequest(sanctionSearchRequest.getHeader(), action, messageType);
        sanctions = sanctionRepository.searchSanction(sanctionSearchRequest.getSanctionSearch(), true);
        log.info("Found {} sanctions", sanctions.size());
        return SanctionSearchResponse.builder().header(sanctionSearchRequest.getHeader()).sanctions(sanctions).build();
    }
}
