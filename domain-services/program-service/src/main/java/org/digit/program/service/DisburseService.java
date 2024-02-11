package org.digit.program.service;

import lombok.extern.slf4j.Slf4j;
import org.digit.program.constants.Status;
import org.digit.program.models.disburse.DisburseSearchRequest;
import org.digit.program.models.disburse.DisburseSearchResponse;
import org.digit.program.models.disburse.Disbursement;
import org.digit.program.models.disburse.DisbursementRequest;
import org.digit.program.models.sanction.Sanction;
import org.digit.program.repository.DisburseRepository;
import org.digit.program.repository.SanctionRepository;
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

    public DisburseService(DispatcherUtil dispatcherUtil, EnrichmentService enrichmentService, CalculationUtil calculationUtil, SanctionRepository sanctionRepository, DisburseRepository disburseRepository) {
        this.dispatcherUtil = dispatcherUtil;
        this.enrichmentService = enrichmentService;
        this.calculationUtil = calculationUtil;
        this.disburseRepository = disburseRepository;
    }

    public DisbursementRequest createDisburse(DisbursementRequest disbursementRequest) {
        log.info("Create Disburse");
        enrichmentService.enrichDisburseCreate(disbursementRequest.getDisbursement(),
                disbursementRequest.getHeader().getReceiverId());
        Sanction sanction = calculationUtil.calculateAndReturnSanctionForDisburse(disbursementRequest.getDisbursement().getSanctionId(),
                disbursementRequest.getDisbursement().getNetAmount(), false);
        if (sanction != null)
            disburseRepository.createDisburseAndSanction(disbursementRequest.getDisbursement(), sanction);
        else
            disburseRepository.saveDisburse(disbursementRequest.getDisbursement(), null, true);
        dispatcherUtil.dispatchDisburse(disbursementRequest);
        return disbursementRequest;
    }

    public DisbursementRequest updateDisburse(DisbursementRequest disbursementRequest) {
        log.info("Update Disburse");
        enrichmentService.enrichDisburseUpdate(disbursementRequest.getDisbursement());
        disburseRepository.updateDisburse(disbursementRequest.getDisbursement());
        dispatcherUtil.dispatchDisburse(disbursementRequest);
        return disbursementRequest;
    }

    public DisburseSearchResponse searchDisburse(DisburseSearchRequest disburseSearchRequest) {
        log.info("Search Disburse");
        List<Disbursement> disbursements = disburseRepository.searchDisbursements(disburseSearchRequest.getDisburseSearch());
        return DisburseSearchResponse.builder().header(disburseSearchRequest.getHeader())
                .disbursements(disbursements).build();

    }

    public DisbursementRequest onDisburse (DisbursementRequest disbursementRequest) {
        log.info("On Disburse");
        enrichmentService.enrichDisburseUpdate(disbursementRequest.getDisbursement());
        if (disbursementRequest.getDisbursement().getStatus().equals(Status.FAILED)) {
            Sanction sanction = calculationUtil.calculateAndReturnSanctionForDisburse(disbursementRequest.getDisbursement().getSanctionId(),
                    disbursementRequest.getDisbursement().getNetAmount(), true);
            disburseRepository.updateDisburseAndSanction(disbursementRequest.getDisbursement(), sanction);
        } else {
            disburseRepository.updateDisburse(disbursementRequest.getDisbursement());
        }
        dispatcherUtil.dispatchDisburse(disbursementRequest);
        return disbursementRequest;
    }

}
