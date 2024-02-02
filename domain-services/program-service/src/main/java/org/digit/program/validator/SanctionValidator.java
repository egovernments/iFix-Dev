package org.digit.program.validator;

import lombok.extern.slf4j.Slf4j;
import org.digit.program.constants.SortOrder;
import org.digit.program.models.*;
import org.digit.program.repository.ProgramRepository;
import org.digit.program.repository.SanctionRepository;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;

@Component
@Slf4j
public class SanctionValidator {

    private final SanctionRepository sanctionRepository;
    private final ProgramRepository programRepository;

    public SanctionValidator(SanctionRepository sanctionRepository, ProgramRepository programRepository) {
        this.sanctionRepository = sanctionRepository;
        this.programRepository = programRepository;
    }

    public void validateCreateSanction(Sanction sanction) {
        log.info("validating Sanction");
        List<Program> programs = programRepository.searchProgram(ProgramSearch.builder()
                .programCode(sanction.getProgramCode()).pagination(Pagination.builder().limit(1).offset(0)
                        .sortOrder(SortOrder.DESC).sortBy("last_modified_time").build()).build());
        if (!CollectionUtils.isEmpty(programs)) {
            throw new IllegalArgumentException("No Programs exists for program code: " + sanction.getProgramCode());
        }
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
