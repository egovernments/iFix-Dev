package org.digit.program.service;

import lombok.extern.slf4j.Slf4j;
import org.digit.program.configuration.ProgramConfiguration;
import org.digit.program.kafka.ProgramProducer;
import org.digit.program.models.disburse.DisburseSearchRequest;
import org.digit.program.models.disburse.DisburseSearchResponse;
import org.digit.program.models.disburse.Disbursement;
import org.digit.program.models.disburse.DisbursementRequest;
import org.digit.program.models.sanction.Sanction;
import org.digit.program.repository.DisburseRepository;
import org.digit.program.utils.CalculationUtil;
import org.digit.program.utils.CommonUtil;
import org.digit.program.utils.DispatcherUtil;
import org.digit.program.utils.ErrorHandler;
import org.digit.program.validator.CommonValidator;
import org.digit.program.validator.DisbursementValidator;
import org.egov.tracer.model.CustomException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class DisburseService {

    private final DispatcherUtil dispatcherUtil;
    private final EnrichmentService enrichmentService;
    private final CalculationUtil calculationUtil;
    private final DisburseRepository disburseRepository;
    private final DisbursementValidator disbursementValidator;
    private final CommonValidator commonValidator;
    private final ProgramProducer producer;
    private final ProgramConfiguration configs;
    private final ErrorHandler errorHandler;
    private final CommonUtil commonUtil;
    private final EncryptionService encryptionService;

    public DisburseService(DispatcherUtil dispatcherUtil, EnrichmentService enrichmentService, CalculationUtil calculationUtil, DisburseRepository disburseRepository, DisbursementValidator disbursementValidator, CommonValidator commonValidator, ProgramProducer producer, ProgramConfiguration configs, ErrorHandler errorHandler, CommonUtil commonUtil, EncryptionService encryptionService) {
        this.dispatcherUtil = dispatcherUtil;
        this.enrichmentService = enrichmentService;
        this.calculationUtil = calculationUtil;
        this.disburseRepository = disburseRepository;
        this.disbursementValidator = disbursementValidator;
        this.commonValidator = commonValidator;
        this.producer = producer;
        this.configs = configs;
        this.errorHandler = errorHandler;
        this.commonUtil = commonUtil;
        this.encryptionService = encryptionService;
    }

    /**
     * Pushes to kafka topic
     * @param disbursementRequest
     * @return
     */
    public DisbursementRequest pushToKafka(DisbursementRequest disbursementRequest) {
        log.info("pushToKafka");
        producer.push(configs.getDisburseTopic(), disbursementRequest);
        return disbursementRequest;
    }

    /**
     * Validates, enriches and calculates sanction available amount and persist disbursement and sanction to repository also
     * forwards the request
     * @param disbursementRequest
     */
    public DisbursementRequest createDisburse(DisbursementRequest disbursementRequest) {
        log.info("Create Disburse");
        try {
            disbursementValidator.validateDisbursement(disbursementRequest.getDisbursement(), true, false);
            // Enriches id, audit details and message type
            enrichmentService.enrichDisburseCreate(disbursementRequest.getDisbursement(), disbursementRequest.getHeader());
            // Finds sanction with available amount if not already present and calculates new available amount
            Sanction sanction = calculationUtil.calculateAndReturnSanctionForDisburse(disbursementRequest.getDisbursement(),
                    disbursementRequest.getHeader().getSenderId());
            // Encrypts PII data
            Disbursement disbursement = encryptionService.getEncryptedDisbursement(disbursementRequest.getDisbursement());
            // Persists disbursement and sanction in one transaction
            disburseRepository.saveDisburseAndSanction(disbursement, sanction);
            // Dispatches disbursement to appropriate service
            DisbursementRequest disbursementRequestFromAdapter = null;
            try {
                disbursementRequestFromAdapter = dispatcherUtil.dispatchDisburse(disbursementRequest);
            } catch (CustomException e) {
                errorHandler.handleDisburseWhenIfmsAdapterThrowsError(disbursementRequest, e);
                onDisburseCreate(disbursementRequest);
            }

            if (disbursementRequestFromAdapter != null) {
                commonUtil.updateUri(disbursementRequestFromAdapter.getHeader());
                // Calls onDisburseCreate method which replies to the appropriate service
                onDisburseCreate(disbursementRequestFromAdapter);
            }
            log.info("Disburse created successfully");
        } catch (CustomException exception) {
            log.error("Error while creating disburse", exception);
            errorHandler.handleDisburseError(disbursementRequest, exception);
        }
        return disbursementRequest;
    }

    /**
     * Validates, enriches, updates and forwards disbursement.
     * @param disbursementRequest
     */
    public DisbursementRequest updateDisburse(DisbursementRequest disbursementRequest) {
        log.info("Update Disburse");
        try {
            disbursementValidator.validateDisbursement(disbursementRequest.getDisbursement(), false, false);
            enrichmentService.enrichDisburseUpdate(disbursementRequest.getDisbursement(), disbursementRequest.getHeader(), false);
            disburseRepository.updateDisburse(disbursementRequest.getDisbursement(), false, true);
            DisbursementRequest disbursementRequestFromAdapter = dispatcherUtil.dispatchDisburse(disbursementRequest);
            if (disbursementRequestFromAdapter != null) {
                commonUtil.updateUri(disbursementRequestFromAdapter.getHeader());
                // Calls onDisburseUpdate method which replies to the appropriate service
                onDisburseUpdate(disbursementRequestFromAdapter);
            }
            log.info("Disburse updated successfully");
        } catch (CustomException exception) {
            log.error("Error while updating disburse", exception);
            errorHandler.handleDisburseError(disbursementRequest, exception);
        }
        return disbursementRequest;
    }

    /**
     * Validates header and searches for disbursement
     * @param disburseSearchRequest
     * @param action
     * @param messageType
     * @return
     */
    public DisburseSearchResponse searchDisburse(DisburseSearchRequest disburseSearchRequest, String action, String messageType) {
        log.info("Search Disburse");
        commonValidator.validateRequest(disburseSearchRequest.getHeader(), action, messageType);
        List<Disbursement> disbursements = disburseRepository.searchDisbursements(disburseSearchRequest.getDisburseSearch());
        log.info("Found {} disbursements", disbursements.size());
        encryptionService.getDecryptedDisbursement(disbursements);
        return DisburseSearchResponse.builder().header(disburseSearchRequest.getHeader())
                .disbursements(disbursements).build();
    }

    /**
     * Validates, enriches, calculates sanction available amount in case of failure and persist disbursement and sanction to repository
     * Forwards disbursement
     * @param disbursementRequest
     */
    public DisbursementRequest onDisburseCreate(DisbursementRequest disbursementRequest) {
        log.info("On Disburse Create");
        try {
            Disbursement disbursement = disbursementRequest.getDisbursement();
            disbursementValidator.validateDisbursement(disbursement, false, true);
            // Validates if receiver id is same as configured in mdms
            commonValidator.validateReply(disbursementRequest.getHeader(), disbursement.getLocationCode());
            enrichmentService.enrichDisburseUpdate(disbursement, disbursementRequest.getHeader(), true);
            // If status is failed or error then increases the sanction amount and returns the sanction for update
            Sanction sanction = calculationUtil.calculateAndReturnSanctionForOnDisburse(disbursement,
                    disbursementRequest.getHeader().getSenderId());
            disburseRepository.updateDisburseAndSanction(disbursement, sanction, true);
            dispatcherUtil.dispatchDisburse(disbursementRequest);
            log.info("On Disburse created successfully");
        } catch (CustomException exception) {
            log.error("Error while on-disburse create", exception);
            errorHandler.handleDisburseReplyError(disbursementRequest, exception);
        }
        return disbursementRequest;
    }

    /**
     * Validates enriches, and persists disburse update
     * @param disbursementRequest
     */
    public DisbursementRequest onDisburseUpdate(DisbursementRequest disbursementRequest) {
        log.info("On Disburse Update");
        try {
            disbursementValidator.validateDisbursement(disbursementRequest.getDisbursement(), false, false);
            commonValidator.validateReply(disbursementRequest.getHeader(), disbursementRequest.getDisbursement().getLocationCode());
            enrichmentService.enrichDisburseUpdate(disbursementRequest.getDisbursement(), disbursementRequest.getHeader(), true);
            // If status is failed or error then increases the sanction amount and returns the sanction for update
            Sanction sanction = calculationUtil.calculateAndReturnSanctionForOnDisburse(disbursementRequest.getDisbursement(),
                    disbursementRequest.getHeader().getSenderId());
            disburseRepository.updateDisburseAndSanction(disbursementRequest.getDisbursement(), sanction, false);
            dispatcherUtil.dispatchDisburse(disbursementRequest);
            log.info("On Disburse updated successfully");
        } catch (CustomException exception) {
            log.error("Error while on-disburse update");
            errorHandler.handleDisburseReplyError(disbursementRequest, exception);
        }
        return disbursementRequest;
    }
}
