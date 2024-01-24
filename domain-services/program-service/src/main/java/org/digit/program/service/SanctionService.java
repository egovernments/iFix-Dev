package org.digit.program.service;

import lombok.extern.slf4j.Slf4j;
import org.digit.program.models.ExchangeCode;
import org.digit.program.models.RequestJsonMessage;
import org.digit.program.models.Sanction;
import org.digit.program.models.SanctionSearch;
import org.digit.program.repository.SanctionRepository;
import org.digit.program.utils.CommonUtil;
import org.digit.program.validator.SanctionValidator;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class SanctionService {

    private final SanctionRepository sanctionRepository;
    private final EnrichmentService enrichmentService;
    private final CommonUtil commonUtil;
    private final SanctionValidator sanctionValidator;

    public SanctionService(SanctionRepository sanctionRepository, EnrichmentService enrichmentService, CommonUtil commonUtil, SanctionValidator sanctionValidator) {
        this.sanctionRepository = sanctionRepository;
        this.enrichmentService = enrichmentService;
        this.commonUtil = commonUtil;
        this.sanctionValidator = sanctionValidator;
    }

    public RequestJsonMessage createSanction(RequestJsonMessage requestJsonMessage) {
        log.info("createSanction");
        Sanction sanction = enrichmentService.enrichSanctionCreate(new Sanction(requestJsonMessage.getMessage()));

        sanctionRepository.save(sanction);
        return requestJsonMessage;
    }

    public RequestJsonMessage updateSanction(RequestJsonMessage requestJsonMessage) {
        log.info("updateSanction");
        Sanction sanction = new Sanction(requestJsonMessage.getMessage());
        sanctionValidator.validateUpdateSanction(sanction);
        sanctionRepository.save(sanction);
        return requestJsonMessage;
    }

    public List<ExchangeCode> searchSanction(SanctionSearch sanctionSearch) {
        log.info("searchSanction");
        List<ExchangeCode> sanctions;
        Sort sort = commonUtil.getPagination(sanctionSearch.getPagination());
        sanctions = sanctionRepository.findByCriteria(sanctionSearch.getIds(), sanctionSearch.getProgramCode(),
                sanctionSearch.getLocationCode(), sort);
        return sanctions;
    }

}
