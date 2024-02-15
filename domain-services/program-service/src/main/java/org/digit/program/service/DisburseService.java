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
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class DisburseService {

    private final DispatcherUtil dispatcherUtil;
    private final EnrichmentService enrichmentService;
    private final CalculationUtil calculationUtil;
    private final DisburseRepository disburseRepository;

    public DisburseService(DispatcherUtil dispatcherUtil, EnrichmentService enrichmentService, CalculationUtil calculationUtil, DisburseRepository disburseRepository) {
        this.dispatcherUtil = dispatcherUtil;
        this.enrichmentService = enrichmentService;
        this.calculationUtil = calculationUtil;
        this.disburseRepository = disburseRepository;
    }

    public DisbursementRequest createDisburse(DisbursementRequest disbursementRequest) {
        log.info("Create Disburse");
        enrichmentService.enrichDisburseCreate(disbursementRequest.getDisbursement(),
                disbursementRequest.getHeader().getSenderId());
        Sanction sanction = calculationUtil.calculateAndReturnSanctionForDisburse(disbursementRequest.getDisbursement(),
                disbursementRequest.getHeader().getSenderId());
        disburseRepository.createDisburseAndSanction(disbursementRequest.getDisbursement(), sanction);
        dispatcherUtil.dispatchDisburse(disbursementRequest);
        return disbursementRequest;
    }

    public DisbursementRequest updateDisburse(DisbursementRequest disbursementRequest) {
        log.info("Update Disburse");
        enrichmentService.enrichDisburseUpdate(disbursementRequest.getDisbursement(),
                disbursementRequest.getHeader().getSenderId());
        disburseRepository.updateDisburse(disbursementRequest.getDisbursement(), false);
        dispatcherUtil.dispatchDisburse(disbursementRequest);
        return disbursementRequest;
    }

    public DisburseSearchResponse searchDisburse(DisburseSearchRequest disburseSearchRequest) {
        log.info("Search Disburse");
        List<Disbursement> disbursements = disburseRepository.searchDisbursements(disburseSearchRequest.getDisburseSearch());
        return DisburseSearchResponse.builder().header(disburseSearchRequest.getHeader())
                .disbursements(disbursements).build();
    }

    public DisbursementRequest onDisburseCreate(DisbursementRequest disbursementRequest) {
        log.info("On Disburse");
        enrichmentService.enrichDisburseUpdate(disbursementRequest.getDisbursement(),
                disbursementRequest.getHeader().getSenderId());
        if (disbursementRequest.getDisbursement().getStatus().getStatusCode().equals(Status.FAILED)) {
            Sanction sanction = calculationUtil.calculateAndReturnSanctionForOnDisburseFailure(disbursementRequest
                    .getDisbursement());
            disburseRepository.updateDisburseAndSanction(disbursementRequest.getDisbursement(), sanction);
        } else {
            disburseRepository.updateDisburse(disbursementRequest.getDisbursement(), true);
        }
        dispatcherUtil.dispatchDisburse(disbursementRequest);
        return disbursementRequest;
    }

}
