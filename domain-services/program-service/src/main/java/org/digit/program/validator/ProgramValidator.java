package org.digit.program.validator;

import lombok.extern.slf4j.Slf4j;
import org.digit.program.models.program.Program;
import org.digit.program.models.program.ProgramSearch;
import org.digit.program.repository.ProgramRepository;
import org.egov.tracer.model.CustomException;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@Slf4j
public class ProgramValidator {
    private final ProgramRepository programRepository;

    public ProgramValidator(ProgramRepository programRepository) {
        this.programRepository = programRepository;
    }

    public void validateProgram(Program program, Boolean isCreate) {
        if (program.getStartDate() == 0)
            throw new CustomException("START_DATE_ERROR", "startDate should not be empty");

        if (program.getEndDate() != 0 && program.getStartDate() > program.getEndDate())
            throw new CustomException("DATES_ERROR", "startDate should be less than endDate");

        if (program.getParentId() != null) {
            List<Program> programs = programRepository.searchProgram(ProgramSearch.builder().ids(Collections.singletonList(program.getParentId())).build());
            if (programs.isEmpty())
                throw new CustomException("PROGRAM_PARENT_ID_NOT_FOUND", "Program not found for ParentId: " + program.getParentId());
        }

        if (Boolean.FALSE.equals(isCreate)) {
            List<Program> programs = programRepository.searchProgram(ProgramSearch.builder().ids(Collections.singletonList(program.getId())).build());
            if (programs.isEmpty()) {
                throw new CustomException("PROGRAM_NOT_FOUND", "Program not found for id: " + program.getId());
            }
            if (program.getProgramCode() == null || program.getProgramCode().isEmpty()) {
                throw new CustomException("PROGRAM_CODE_ERROR", "programCode should not be empty");
            }
        }
    }
}
