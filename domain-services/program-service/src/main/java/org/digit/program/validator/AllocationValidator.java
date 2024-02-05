package org.digit.program.validator;

import lombok.extern.slf4j.Slf4j;
import org.digit.program.repository.ProgramRepository;
import org.digit.program.repository.SanctionRepository;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AllocationValidator {

    private ProgramRepository programRepository;
    private SanctionRepository sanctionRepository;

    public AllocationValidator(ProgramRepository programRepository, SanctionRepository sanctionRepository) {
        this.programRepository = programRepository;
        this.sanctionRepository = sanctionRepository;
    }


}
