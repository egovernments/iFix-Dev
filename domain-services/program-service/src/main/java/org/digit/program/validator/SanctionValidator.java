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

import static org.digit.program.constants.Error.*;

@Component
@Slf4j
public class SanctionValidator {

    private final SanctionRepository sanctionRepository;
    private final ProgramRepository programRepository;

    public SanctionValidator(SanctionRepository sanctionRepository, ProgramRepository programRepository) {
        this.sanctionRepository = sanctionRepository;
        this.programRepository = programRepository;
    }

    /**
     * Validates sanction request
     * @param sanctions
     * @param isCreate
     */
    public void validateSanction(List<Sanction> sanctions, Boolean isCreate) {
        log.info("validating Sanction");
        validateProgramAndLocationCodes(sanctions);

        if (Boolean.TRUE.equals(isCreate)) {
            validateForCreate(sanctions);
        } else {
            validateForUpdate(sanctions);
        }
        log.info("validated Sanction");
    }

    /**
     * Validates program and locatin codes is same for all sanctions and if program is present for given program code
     * @param sanctions
     */
    public void validateProgramAndLocationCodes(List<Sanction> sanctions) {
        Set<String> programCodes = new HashSet<>();
        Set<String> locationCodes = new HashSet<>();
        for (Sanction sanction : sanctions) {
            programCodes.add(sanction.getProgramCode());
            locationCodes.add(sanction.getLocationCode());
        }
        if (programCodes.size() > 1) {
            throw new CustomException(SAME_PROGRAM_CODE, SAME_PROGRAM_CODE_MSG);
        }
        if (locationCodes.size() > 1) {
            throw new CustomException(SAME_LOCATION_CODE, SAME_LOCATION_CODE_MSG);
        }
        List<Program> programs = programRepository.searchProgram(ProgramSearch.builder()
                .programCode(sanctions.get(0).getProgramCode()).locationCode(sanctions.get(0).getLocationCode()).build());
        if (CollectionUtils.isEmpty(programs)) {
            throw new CustomException(NO_PROGRAMS_FOUND , NO_PROGRAMS_FOUND_MSG + sanctions.get(0).getProgramCode());
        }
    }
    /**
     * Validates if id is already present in db for given sanctions for create
     * @param sanctions
     */
    public void validateForCreate(List<Sanction> sanctions) {
        Set<String> idsFromRequest = sanctions.stream().filter(sanction -> sanction.getId() != null &&
                !sanction.getId().isEmpty()).map(Sanction::getId).collect(Collectors.toSet());
        if (!idsFromRequest.isEmpty()) {
            List<Sanction> existingSanctions = sanctionRepository.searchSanction(SanctionSearch.builder()
                    .ids(new ArrayList<>(idsFromRequest)).build(), false);
            if (!existingSanctions.isEmpty()) {
                List<String> ids = existingSanctions.stream().map(Sanction::getId).collect(Collectors.toList());
                throw new CustomException(DUPLICATE_SANCTION_ID, DUPLICATE_SANCTION_ID_MSG + ids);
            }
        }
    }

    /**
     * Validates if ids are present in db for given sanctions for update
     * @param sanctions
     */
    public void validateForUpdate(List<Sanction> sanctions) {
        Set<String> sanctionIds = new HashSet<>();
        for (Sanction sanction : sanctions) {
            if (sanction.getId() == null || sanction.getId().isEmpty()) {
                throw new CustomException(SANCTION_ID_NULL, SANCTION_ID_NULL_MSG);
            } else {
                sanctionIds.add(sanction.getId());
            }
        }
        List<Sanction> sanctionFromSearch = sanctionRepository.searchSanction(SanctionSearch.builder()
                .ids(new ArrayList<>(sanctionIds)).build(), false);
        if (sanctionFromSearch.size() != sanctionIds.size()) {
            sanctionIds.removeAll(sanctionFromSearch.stream().map(Sanction::getId).collect(Collectors.toSet()));
            throw new CustomException(NO_SANCTIONS_FOUND, NO_SANCTIONS_FOUND_MSG + sanctionIds);
        }
    }
}
