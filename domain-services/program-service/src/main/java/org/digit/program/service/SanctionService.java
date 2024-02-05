package org.digit.program.service;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.digit.program.models.*;
import org.digit.program.repository.SanctionRepository;
import org.digit.program.utils.DispatcherUtil;
import org.digit.program.validator.CommonValidator;
import org.digit.program.validator.SanctionValidator;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class SanctionService {

    private final SanctionRepository sanctionRepository;
    private final EnrichmentService enrichmentService;
    private final SanctionValidator sanctionValidator;
    private final DispatcherUtil dispatcherUtil;
    private final CommonValidator commonValidator;

    public SanctionService(SanctionRepository sanctionRepository, EnrichmentService enrichmentService, SanctionValidator sanctionValidator, DispatcherUtil dispatcherUtil, CommonValidator commonValidator) {
        this.sanctionRepository = sanctionRepository;
        this.enrichmentService = enrichmentService;
        this.sanctionValidator = sanctionValidator;
        this.dispatcherUtil = dispatcherUtil;
        this.commonValidator = commonValidator;
    }

    public RequestJsonMessage createSanction(RequestJsonMessage requestJsonMessage) {
        log.info("createSanction");
        commonValidator.validateRequest(requestJsonMessage);
        List<JsonNode> messages = new ArrayList<>();
        Sanction sanction;
        for (int i = 0; i < requestJsonMessage.getMessage().size(); i++) {
            sanction = (new Sanction(requestJsonMessage.getMessage().get(i)));
            sanctionValidator.validateSanction(sanction, true);
            enrichmentService.enrichSanctionCreate(sanction, requestJsonMessage.getHeader().getReceiverId());
            sanctionRepository.saveSanction(sanction);
            messages.add(sanction.toJsonNode(sanction));
        }
        requestJsonMessage.setMessage(messages);
        dispatcherUtil.forwardMessage(requestJsonMessage);
        return requestJsonMessage;
    }

    public RequestJsonMessage updateSanction(RequestJsonMessage requestJsonMessage) {
        log.info("updateSanction");
        commonValidator.validateRequest(requestJsonMessage);
        List<JsonNode> messages = new ArrayList<>();
        Sanction sanction;
        for (int i = 0; i < requestJsonMessage.getMessage().size(); i++) {
            sanction = new Sanction(requestJsonMessage.getMessage().get(i));
            sanctionValidator.validateSanction(sanction, false);
            enrichmentService.enrichSanctionUpdate(sanction);
            sanctionRepository.updateSanction(sanction);
            messages.add(sanction.toJsonNode(sanction));
        }
        requestJsonMessage.setMessage(messages);
        dispatcherUtil.forwardMessage(requestJsonMessage);
        return requestJsonMessage;
    }

    public SanctionSearchResponse searchSanction(SanctionSearchRequest sanctionSearchRequest) {
        log.info("searchSanction");
        List<Sanction> sanctions;
        sanctions = sanctionRepository.searchSanction(sanctionSearchRequest.getSanctionSearch());
        return SanctionSearchResponse.builder().header(sanctionSearchRequest.getHeader()).sanctions(sanctions).build();
    }

}
