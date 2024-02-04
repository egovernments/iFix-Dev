package org.digit.program.service;

import lombok.extern.slf4j.Slf4j;
import org.digit.program.models.Disbursement;
import org.digit.program.models.RequestJsonMessage;
import org.digit.program.models.Sanction;
import org.digit.program.repository.SanctionRepository;
import org.digit.program.utils.CalculationUtil;
import org.digit.program.utils.DispatcherUtil;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class DisburseService {

    private DispatcherUtil dispatcherUtil;
    private EnrichmentService enrichmentService;
    private CalculationUtil calculationUtil;
    private SanctionRepository sanctionRepository;

    public DisburseService(DispatcherUtil dispatcherUtil, EnrichmentService enrichmentService, CalculationUtil calculationUtil, SanctionRepository sanctionRepository) {
        this.dispatcherUtil = dispatcherUtil;
        this.enrichmentService = enrichmentService;
        this.calculationUtil = calculationUtil;
        this.sanctionRepository = sanctionRepository;
    }

    public RequestJsonMessage createDisburse(RequestJsonMessage requestJsonMessage) {
        log.info("Create Disburse");
        Disbursement disbursement = enrichmentService.enrichDisburseCreate(new Disbursement(requestJsonMessage.getMessage()), requestJsonMessage.getHeader().getReceiverId());
        Sanction sanction = calculationUtil.calculateSanctionAmount(disbursement.getSanctionId(), disbursement.getNetAmount(), false);
        sanctionRepository.updateSanction(sanction);

        requestJsonMessage.setMessage(disbursement.toJsonNode(disbursement));
        dispatcherUtil.forwardMessage(requestJsonMessage);
        return requestJsonMessage;
    }

}
