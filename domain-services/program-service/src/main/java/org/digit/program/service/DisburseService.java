package org.digit.program.service;

import lombok.extern.slf4j.Slf4j;
import org.digit.program.constants.Status;
import org.digit.program.models.disburse.DisburseSearchRequest;
import org.digit.program.models.disburse.DisburseSearchResponse;
import org.digit.program.models.disburse.Disbursement;
import org.digit.program.models.disburse.DisbursementRequest;
import org.digit.program.models.sanction.Sanction;
import org.digit.program.repository.DisburseRepository;
import org.digit.program.utils.CalculationUtil;
import org.digit.program.utils.DispatcherUtil;
import org.digit.program.validator.CommonValidator;
import org.digit.program.validator.DisbursementValidator;
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

    public DisburseService(DispatcherUtil dispatcherUtil, EnrichmentService enrichmentService, CalculationUtil calculationUtil, DisburseRepository disburseRepository, DisbursementValidator disbursementValidator, CommonValidator commonValidator) {
        this.dispatcherUtil = dispatcherUtil;
        this.enrichmentService = enrichmentService;
        this.calculationUtil = calculationUtil;
        this.disburseRepository = disburseRepository;
        this.disbursementValidator = disbursementValidator;
        this.commonValidator = commonValidator;
    }

    public DisbursementRequest createDisburse(DisbursementRequest disbursementRequest, String action) {
        log.info("Create Disburse");
        commonValidator.validateRequest(disbursementRequest.getHeader(), action);
        disbursementValidator.validateDisbursement(disbursementRequest.getDisbursement(), true);
        enrichmentService.enrichDisburseCreate(disbursementRequest.getDisbursement(),
                disbursementRequest.getHeader().getSenderId());
        Sanction sanction = calculationUtil.calculateAndReturnSanctionForDisburse(disbursementRequest.getDisbursement(),
                disbursementRequest.getHeader().getSenderId());
        disburseRepository.createDisburseAndSanction(disbursementRequest.getDisbursement(), sanction);
        dispatcherUtil.dispatchDisburse(disbursementRequest);
        return disbursementRequest;
    }

    public DisbursementRequest updateDisburse(DisbursementRequest disbursementRequest, String action) {
        log.info("Update Disburse");
        commonValidator.validateRequest(disbursementRequest.getHeader(), action);
        disbursementValidator.validateDisbursement(disbursementRequest.getDisbursement(), false);
        enrichmentService.enrichDisburseUpdate(disbursementRequest.getDisbursement(),
                disbursementRequest.getHeader().getSenderId());
        disburseRepository.updateDisburse(disbursementRequest.getDisbursement(), false);
        dispatcherUtil.dispatchDisburse(disbursementRequest);
        return disbursementRequest;
    }

    public DisburseSearchResponse searchDisburse(DisburseSearchRequest disburseSearchRequest, String action) {
        log.info("Search Disburse");
        commonValidator.validateRequest(disburseSearchRequest.getHeader(), action);
        List<Disbursement> disbursements = disburseRepository.searchDisbursements(disburseSearchRequest.getDisburseSearch());
        return DisburseSearchResponse.builder().header(disburseSearchRequest.getHeader())
                .disbursements(disbursements).build();
    }

    public DisbursementRequest onDisburseCreate(DisbursementRequest disbursementRequest, String action) {
        log.info("On Disburse");
        commonValidator.validateRequest(disbursementRequest.getHeader(), action);
        disbursementValidator.validateDisbursement(disbursementRequest.getDisbursement(), true);
        commonValidator.validateReply(disbursementRequest.getHeader(), disbursementRequest.getDisbursement().getProgramCode(),
                disbursementRequest.getDisbursement().getLocationCode());
        if (disbursementRequest.getDisbursement().getStatus().getStatusCode().equals(Status.FAILED)) {
            Sanction sanction = calculationUtil.calculateAndReturnSanctionForOnDisburseFailure(disbursementRequest
                    .getDisbursement(), disbursementRequest.getHeader().getSenderId());
            disburseRepository.updateDisburseAndSanction(disbursementRequest.getDisbursement(), sanction);
        } else {
            disburseRepository.updateDisburse(disbursementRequest.getDisbursement(), true);
        }
        dispatcherUtil.dispatchDisburse(disbursementRequest);
        return disbursementRequest;
    }

    public DisbursementRequest onDisburseUpdate(DisbursementRequest disbursementRequest, String action) {
        log.info("Update on Disburse");
        commonValidator.validateRequest(disbursementRequest.getHeader(), action);
        disbursementValidator.validateDisbursement(disbursementRequest.getDisbursement(), false);
        commonValidator.validateReply(disbursementRequest.getHeader(), disbursementRequest.getDisbursement().getProgramCode(),
                disbursementRequest.getDisbursement().getLocationCode());
        enrichmentService.enrichDisburseUpdate(disbursementRequest.getDisbursement(),
                disbursementRequest.getHeader().getSenderId());
        disburseRepository.updateDisburse(disbursementRequest.getDisbursement(), false);
        dispatcherUtil.dispatchDisburse(disbursementRequest);
        return disbursementRequest;
    }
}
