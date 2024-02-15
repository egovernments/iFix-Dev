package org.digit.program.validator;

import lombok.extern.slf4j.Slf4j;
import org.digit.program.models.program.Program;
import org.digit.program.models.program.ProgramSearch;
import org.digit.program.models.sanction.Sanction;
import org.digit.program.models.sanction.SanctionSearch;
import org.digit.program.repository.ProgramRepository;
import org.digit.program.repository.SanctionRepository;
import org.egov.tracer.model.CustomException;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
public class SanctionValidator {

    private final SanctionRepository sanctionRepository;
    private final ProgramRepository programRepository;

    public SanctionValidator(SanctionRepository sanctionRepository, ProgramRepository programRepository) {
        this.sanctionRepository = sanctionRepository;
        this.programRepository = programRepository;
    }

    public void validateSanction(List<Sanction> sanctions, Boolean isCreate) {
        log.info("validating Sanction");
        validateProgramAndLocationCodes(sanctions);

        if (Boolean.FALSE.equals(isCreate)) {
            validateForUpdate(sanctions);
        }
    }

    public void validateProgramAndLocationCodes(List<Sanction> sanctions) {
        Set<String> programCodes = new HashSet<>();
        Set<String> locationCodes = new HashSet<>();
        for (Sanction sanction : sanctions) {
            programCodes.add(sanction.getProgramCode());
            locationCodes.add(sanction.getLocationCode());
        }
        if (programCodes.size() > 1) {
            throw new CustomException("SAME_PROGRAM_CODE", "Program code should be same for all Sanction");
        }
        if (locationCodes.size() > 1) {
            throw new CustomException("SAME_LOCATION_CODE", "Location code should be same for all Sanction");
        }
        List<Program> programs = programRepository.searchProgram(ProgramSearch.builder()
                .programCode(sanctions.get(0).getProgramCode()).locationCode(sanctions.get(0).getLocationCode()).build());
        if (CollectionUtils.isEmpty(programs)) {
            throw new CustomException("NO_PROGRAMS_FOUND" , "No active programs exists for program code: " + sanctions.get(0).getProgramCode());
        }
    }

    public void validateForUpdate(List<Sanction> sanctions) {
        Set<String> sanctionIds = new HashSet<>();
        for (Sanction sanction : sanctions) {
            if (sanction.getId() == null || sanction.getId().isEmpty()) {
                throw new CustomException("SANCTION_ID_NULL", "Sanction id cannot be null or empty");
            } else {
                sanctionIds.add(sanction.getId());
            }
        }
        List<Sanction> sanctionFromSearch = sanctionRepository.searchSanction(SanctionSearch.builder()
                .ids(new ArrayList<>(sanctionIds)).build());
        if (sanctionFromSearch.size() != sanctionIds.size()) {
            sanctionIds.removeAll(sanctionFromSearch.stream().map(Sanction::getId).collect(Collectors.toSet()));
            throw new CustomException("NO_SANCTIONS_FOUND", "No sanction found for id(s): " + sanctionIds);
        }
    }
}
