package org.digit.program.validator;

import lombok.extern.slf4j.Slf4j;
import org.digit.program.constants.Status;
import org.digit.program.models.program.Program;
import org.digit.program.models.program.ProgramSearch;
import org.digit.program.repository.ProgramRepository;
import org.egov.tracer.model.CustomException;
import org.springframework.stereotype.Component;

import java.util.*;

import static org.digit.program.constants.Error.*;

@Component
@Slf4j
public class ProgramValidator {
    private final ProgramRepository programRepository;

    public ProgramValidator(ProgramRepository programRepository) {
        this.programRepository = programRepository;
    }

    /**
     * Validates common fields for program create and update
     * @param program
     * @param isCreate
     * @param isOnProgramCreate
     */
    public void validateProgram(Program program, Boolean isCreate, Boolean isOnProgramCreate) {
        log.info("Validating program");
        if (program.getStartDate() == 0)
            throw new CustomException(START_DATE_ERROR, START_DATE_ERROR_MSG);

        if (program.getEndDate() != 0 && program.getStartDate() > program.getEndDate())
            throw new CustomException(DATES_ERROR, DATES_ERROR_MSG);

        if (program.getParentId() != null) {
            List<Program> programs = programRepository.searchProgram(ProgramSearch.builder()
                    .ids(Collections.singletonList(program.getParentId())).build());
            if (programs.isEmpty())
                throw new CustomException(PROGRAM_PARENT_ID_NOT_FOUND, PROGRAM_PARENT_ID_NOT_FOUND_MSG + program.getParentId());
        }

        if (Boolean.TRUE.equals(isCreate)) {
            validateForCreate(program);
        } else {
            validateForUpdate(program, isOnProgramCreate);
        }
        log.info("Validated program for {}", program.getName());
    }

    /**
     * Validates fields required for update and create reply
     * @param program
     * @param isOnProgramCreate
     */
    public void validateForUpdate(Program program, Boolean isOnProgramCreate) {
        if (program.getId() == null || program.getId().isEmpty())
            throw new CustomException(PROGRAM_ID_ERROR, PROGRAM_ID_ERROR_MSG);

        List<Program> programs = programRepository.searchProgram(ProgramSearch.builder()
                .ids(Collections.singletonList(program.getId())).build());
        if (programs.isEmpty()) {
            throw new CustomException(PROGRAM_NOT_FOUND, PROGRAM_NOT_FOUND_MSG + program.getId());
        }
        if (Boolean.FALSE.equals(isOnProgramCreate) && (program.getProgramCode() == null || program.getProgramCode().isEmpty())) {
            throw new CustomException(PROGRAM_CODE_ERROR, PROGRAM_CODE_ERROR_MSG);
        }
        if (Boolean.TRUE.equals(isOnProgramCreate) && (program.getProgramCode() == null || program.getProgramCode()
                .isEmpty()) && program.getStatus().getStatusCode().equals(Status.APPROVED)) {
            throw new CustomException(PROGRAM_CODE_ERROR, PROGRAM_CODE_ERROR_MSG);
        }
    }

    /**
     * Validates if program is already present for id.
     * @param program
     */
    public void validateForCreate(Program program) {
        if (program.getId() != null && !program.getId().isEmpty()) {
            List<Program> programs = programRepository.searchProgram(ProgramSearch.builder()
                    .ids(Collections.singletonList(program.getId())).build());
            if (!programs.isEmpty())
                throw new CustomException(PROGRAM_FOUND_FOR_CREATE, PROGRAM_FOUND_FOR_CREATE_MSG + program.getId());
        }
    }
}
