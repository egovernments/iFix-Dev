package org.digit.program.validator;

import lombok.extern.slf4j.Slf4j;
import org.digit.program.models.ExchangeCode;
import org.digit.program.models.Sanction;
import org.digit.program.models.SanctionSearch;
import org.digit.program.repository.SanctionRepository;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

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
//        List<Sanction> sanctionFromSearch = sanctionRepository.searchSanction(SanctionSearch.builder()
//                .ids(Collections.singletonList(sanction.getId())).build());
//        if (CollectionUtils.isEmpty(sanctionFromSearch)) {
//            throw new IllegalArgumentException("No sanction found for id: " + sanction.getId());
//        }
    }
}
