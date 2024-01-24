package org.digit.program.validator;

import lombok.extern.slf4j.Slf4j;
import org.digit.program.models.ExchangeCode;
import org.digit.program.models.Sanction;
import org.digit.program.repository.SanctionRepository;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Component
@Slf4j
public class SanctionValidator {

    private final SanctionRepository sanctionRepository;

    public SanctionValidator(SanctionRepository sanctionRepository) {
        this.sanctionRepository = sanctionRepository;
    }

    public void validateUpdateSanction(Sanction sanction) {
        log.info("validating Sanction");
        List<ExchangeCode> sanctionFromSearch = sanctionRepository.findByCriteria(Collections.singletonList(sanction.getId()), sanction.getProgramCode(), sanction.getLocationCode(), null);
        if (CollectionUtils.isEmpty(sanctionFromSearch)) {
            throw new IllegalArgumentException("No sanction found for id: " + sanction.getId());
        }
    }
}
