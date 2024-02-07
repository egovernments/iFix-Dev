package org.digit.program.service;

import lombok.extern.slf4j.Slf4j;
import org.digit.program.models.sanction.Sanction;
import org.digit.program.models.sanction.SanctionRequest;
import org.digit.program.models.sanction.SanctionSearchRequest;
import org.digit.program.models.sanction.SanctionSearchResponse;
import org.digit.program.repository.SanctionRepository;
import org.digit.program.utils.DispatcherUtil;
import org.digit.program.validator.CommonValidator;
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
    private final CommonValidator commonValidator;

    public SanctionService(SanctionRepository sanctionRepository, EnrichmentService enrichmentService, SanctionValidator sanctionValidator, DispatcherUtil dispatcherUtil, CommonValidator commonValidator) {
        this.sanctionRepository = sanctionRepository;
        this.enrichmentService = enrichmentService;
        this.sanctionValidator = sanctionValidator;
        this.dispatcherUtil = dispatcherUtil;
        this.commonValidator = commonValidator;
    }

    public SanctionRequest createSanction(SanctionRequest sanctionRequest) {
        log.info("createSanction");
        commonValidator.validateRequest(sanctionRequest.getHeader());
        sanctionValidator.validateSanction(sanctionRequest.getSanctions(), true);
        enrichmentService.enrichSanctionCreate(sanctionRequest.getSanctions(), sanctionRequest.getHeader().getReceiverId());
        sanctionRepository.saveSanction(sanctionRequest.getSanctions());
        dispatcherUtil.forwardMessage(sanctionRequest.getId(), sanctionRequest.getSignature(),
                sanctionRequest.getHeader(), sanctionRequest.getSanctions().toString());
        return sanctionRequest;
    }

    public SanctionRequest updateSanction(SanctionRequest sanctionRequest) {
        log.info("updateSanction");
        commonValidator.validateRequest(sanctionRequest.getHeader());
        sanctionValidator.validateSanction(sanctionRequest.getSanctions(), false);
        enrichmentService.enrichSanctionUpdate(sanctionRequest.getSanctions());
        sanctionRepository.updateSanction(sanctionRequest.getSanctions());
        dispatcherUtil.forwardMessage(sanctionRequest.getId(), sanctionRequest.getSignature(),
                sanctionRequest.getHeader(), sanctionRequest.getSanctions().toString());
        return sanctionRequest;
    }

    public SanctionSearchResponse searchSanction(SanctionSearchRequest sanctionSearchRequest) {
        log.info("searchSanction");
        List<Sanction> sanctions;
        sanctions = sanctionRepository.searchSanction(sanctionSearchRequest.getSanctionSearch());
        return SanctionSearchResponse.builder().header(sanctionSearchRequest.getHeader()).sanctions(sanctions).build();
    }

}
