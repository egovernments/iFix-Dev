package org.digit.program.validator;

import lombok.extern.slf4j.Slf4j;
import org.digit.program.models.*;
import org.digit.program.repository.ProgramRepository;
import org.digit.program.repository.SanctionRepository;
import org.egov.tracer.model.CustomException;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.*;

@Component
@Slf4j
public class SanctionValidator {

    private final SanctionRepository sanctionRepository;
    private final ProgramRepository programRepository;

    public SanctionValidator(SanctionRepository sanctionRepository, ProgramRepository programRepository) {
        this.sanctionRepository = sanctionRepository;
        this.programRepository = programRepository;
    }

    public void validateSanction(Sanction sanction, Boolean isCreate) {
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<Sanction>> violations = validator.validate(sanction);
        Map<String, String> errorMap = new HashMap<>();
        if (!violations.isEmpty()) {
            // Handle validation errors
            for (ConstraintViolation<Sanction> violation : violations) {
                log.error("Validation error: " + violation.getPropertyPath() + " " + violation.getMessage());
                errorMap.put(violation.getMessageTemplate(), violation.getPropertyPath() + " " + violation.getMessage());
            }
            throw new CustomException(errorMap);
        }
        log.info("validating Sanction");
        List<Program> programs = programRepository.searchProgram(ProgramSearch.builder()
                .programCode(sanction.getProgramCode()).build());
        if (CollectionUtils.isEmpty(programs)) {
            throw new IllegalArgumentException("No Programs exists for program code: " + sanction.getProgramCode());
        }

        if (!isCreate) {
            List<Sanction> sanctionFromSearch = sanctionRepository.searchSanction(SanctionSearch.builder()
                    .ids(Collections.singletonList(sanction.getId())).build());
            if (CollectionUtils.isEmpty(sanctionFromSearch)) {
                throw new IllegalArgumentException("No sanction found for id: " + sanction.getId());
            }
        }
    }
}
