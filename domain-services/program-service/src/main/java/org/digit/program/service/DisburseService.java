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

    public DisburseService(DispatcherUtil dispatcherUtil, EnrichmentService enrichmentService, CalculationUtil calculationUtil, DisburseRepository disburseRepository, DisbursementValidator disbursementValidator, CommonValidator commonValidator, ProgramProducer producer, ProgramConfiguration configs, ErrorHandler errorHandler, CommonUtil commonUtil) {
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
            enrichmentService.enrichDisburseCreate(disbursementRequest.getDisbursement(),
                    disbursementRequest.getHeader().getSenderId());
            Sanction sanction = calculationUtil.calculateAndReturnSanctionForDisburse(disbursementRequest.getDisbursement(),
                    disbursementRequest.getHeader().getSenderId());
            disburseRepository.createDisburseAndSanction(disbursementRequest.getDisbursement(), sanction);
            DisbursementRequest disbursementRequestFromAdapter = dispatcherUtil.dispatchDisburse(disbursementRequest);
            if (disbursementRequestFromAdapter != null) {
                commonUtil.updateUri(disbursementRequestFromAdapter.getHeader());
                onDisburseCreate(disbursementRequestFromAdapter);
            }
        } catch (CustomException exception) {
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
            enrichmentService.enrichDisburseUpdate(disbursementRequest.getDisbursement(),
                    disbursementRequest.getHeader().getSenderId(), false);
            disburseRepository.updateDisburse(disbursementRequest.getDisbursement(), false);
            DisbursementRequest disbursementRequestFromAdapter = dispatcherUtil.dispatchDisburse(disbursementRequest);
            if (disbursementRequestFromAdapter != null) {
                commonUtil.updateUri(disbursementRequestFromAdapter.getHeader());
                onDisburseUpdate(disbursementRequestFromAdapter);
            }
        } catch (CustomException exception) {
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
            commonValidator.validateReply(disbursementRequest.getHeader(), disbursement.getLocationCode());
            enrichmentService.enrichDisburseUpdate(disbursement, disbursementRequest.getHeader().getSenderId(), true);
            Sanction sanction = calculationUtil.calculateAndReturnSanctionForOnDisburse(disbursement,
                    disbursementRequest.getHeader().getSenderId());
            disburseRepository.updateDisburseAndSanction(disbursement, sanction);
            dispatcherUtil.dispatchDisburse(disbursementRequest);
        } catch (CustomException exception) {
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
            enrichmentService.enrichDisburseUpdate(disbursementRequest.getDisbursement(), disbursementRequest.getHeader().getSenderId(), true);
            disburseRepository.updateDisburse(disbursementRequest.getDisbursement(), false);
            dispatcherUtil.dispatchDisburse(disbursementRequest);
        } catch (CustomException exception) {
            errorHandler.handleDisburseReplyError(disbursementRequest, exception);
        }
        return disbursementRequest;
    }
}
