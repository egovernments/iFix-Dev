package org.digit.program.service;

import lombok.extern.slf4j.Slf4j;
import org.digit.program.models.*;
import org.digit.program.repository.SanctionRepository;
import org.digit.program.utils.DispatcherUtil;
import org.digit.program.validator.SanctionValidator;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class SanctionService {

    private final SanctionRepository sanctionRepository;
    private final EnrichmentService enrichmentService;
    private final SanctionValidator sanctionValidator;
    private final DispatcherUtil dispatcherUtil;

    public SanctionService(SanctionRepository sanctionRepository, EnrichmentService enrichmentService, SanctionValidator sanctionValidator, DispatcherUtil dispatcherUtil) {
        this.sanctionRepository = sanctionRepository;
        this.enrichmentService = enrichmentService;
        this.sanctionValidator = sanctionValidator;
        this.dispatcherUtil = dispatcherUtil;
    }

    public RequestJsonMessage createSanction(RequestJsonMessage requestJsonMessage) {
        log.info("createSanction");
        Sanction sanction = enrichmentService.enrichSanctionCreate(new Sanction(requestJsonMessage.getMessage()), requestJsonMessage);
        sanctionRepository.saveSanction(sanction);
        requestJsonMessage.setMessage(sanction.toJsonNode(sanction));
        dispatcherUtil.forwardMessage(requestJsonMessage);
        return requestJsonMessage;
    }

    public RequestJsonMessage updateSanction(RequestJsonMessage requestJsonMessage) {
        log.info("updateSanction");
        Sanction sanction = new Sanction(requestJsonMessage.getMessage());
        sanctionValidator.validateUpdateSanction(sanction);
        sanctionRepository.updateSanction(sanction);
        requestJsonMessage.setMessage(sanction.toJsonNode(sanction));
        dispatcherUtil.forwardMessage(requestJsonMessage);
        return requestJsonMessage;
    }

    public SanctionSearchResponse searchSanction(SanctionSearchRequest sanctionSearchRequest) {
        log.info("searchSanction");
        List<Sanction> sanctions;
        sanctionSearchRequest.getSanctionSearch().setPagination(enrichmentService.enrichSearch(sanctionSearchRequest.getSanctionSearch().getPagination()));
        sanctions = sanctionRepository.searchSanction(sanctionSearchRequest.getSanctionSearch());
        return SanctionSearchResponse.builder().header(sanctionSearchRequest.getHeader()).sanctions(sanctions).build();
    }

}
