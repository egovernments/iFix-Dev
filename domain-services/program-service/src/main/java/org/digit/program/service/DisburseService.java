package org.digit.program.service;

import lombok.extern.slf4j.Slf4j;
import org.digit.program.models.*;
import org.digit.program.repository.DisburseRepository;
import org.digit.program.repository.SanctionRepository;
import org.digit.program.utils.CalculationUtil;
import org.digit.program.utils.DispatcherUtil;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@Slf4j
public class DisburseService {

    private final DispatcherUtil dispatcherUtil;
    private final EnrichmentService enrichmentService;
    private final CalculationUtil calculationUtil;
    private final SanctionRepository sanctionRepository;
    private final DisburseRepository disburseRepository;

    public DisburseService(DispatcherUtil dispatcherUtil, EnrichmentService enrichmentService, CalculationUtil calculationUtil, SanctionRepository sanctionRepository, DisburseRepository disburseRepository) {
        this.dispatcherUtil = dispatcherUtil;
        this.enrichmentService = enrichmentService;
        this.calculationUtil = calculationUtil;
        this.sanctionRepository = sanctionRepository;
        this.disburseRepository = disburseRepository;
    }

    public DisbursementRequest createDisburse(DisbursementRequest disbursementRequest) {
        log.info("Create Disburse");
        enrichmentService.enrichDisburseCreate(disbursementRequest.getDisbursement(), disbursementRequest.getHeader().getReceiverId());
        Sanction sanction = calculationUtil.calculateSanctionAmount(disbursementRequest.getDisbursement().getSanctionId(), disbursementRequest.getDisbursement().getNetAmount(), false);
        sanctionRepository.updateSanction(Collections.singletonList(sanction));
        disburseRepository.saveDisburse(disbursementRequest.getDisbursement(), null);
        dispatcherUtil.forwardMessage(disbursementRequest.getId(), disbursementRequest.getSignature(),
                disbursementRequest.getHeader(), disbursementRequest.getDisbursement().toString());
        return disbursementRequest;
    }

    public DisburseSearchResponse searchDisburse(DisburseSearchRequest disburseSearchRequest) {
        log.info("Search Disburse");
        List<Disbursement> disbursements = disburseRepository.searchDisbursements(disburseSearchRequest.getDisburseSearch());
        return DisburseSearchResponse.builder().header(disburseSearchRequest.getHeader())
                .disbursements(disbursements).build();

    }

}
