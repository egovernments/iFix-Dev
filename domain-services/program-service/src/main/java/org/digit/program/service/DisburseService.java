package org.digit.program.service;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.digit.program.models.Disbursement;
import org.digit.program.models.RequestJsonMessage;
import org.digit.program.models.Sanction;
import org.digit.program.repository.SanctionRepository;
import org.digit.program.utils.CalculationUtil;
import org.digit.program.utils.DispatcherUtil;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
        List<JsonNode> messages = new ArrayList<>();
        Disbursement disbursement;
        for (int i = 0; i < requestJsonMessage.getMessage().size(); i++) {
            disbursement = enrichmentService.enrichDisburseCreate(new Disbursement(requestJsonMessage.getMessage().get(i)), requestJsonMessage.getHeader().getReceiverId());
            Sanction sanction = calculationUtil.calculateSanctionAmount(disbursement.getSanctionId(), disbursement.getNetAmount(), false);
            sanctionRepository.updateSanction(sanction);
            messages.add(disbursement.toJsonNode(disbursement));
        }
        requestJsonMessage.setMessage(messages);
        dispatcherUtil.forwardMessage(requestJsonMessage);
        return requestJsonMessage;
    }

}
