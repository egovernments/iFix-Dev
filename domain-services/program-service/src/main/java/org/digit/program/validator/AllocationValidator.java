package org.digit.program.validator;

import lombok.extern.slf4j.Slf4j;
import org.digit.program.constants.AllocationType;
import org.digit.program.models.allocation.Allocation;
import org.digit.program.models.allocation.AllocationSearch;
import org.digit.program.models.program.Program;
import org.digit.program.models.program.ProgramSearch;
import org.digit.program.models.sanction.Sanction;
import org.digit.program.models.sanction.SanctionSearch;
import org.digit.program.repository.AllocationRepository;
import org.digit.program.repository.ProgramRepository;
import org.digit.program.repository.SanctionRepository;
import org.egov.tracer.model.CustomException;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

import static org.digit.program.constants.Error.*;

@Component
@Slf4j
public class AllocationValidator {

    private final ProgramRepository programRepository;
    private final SanctionRepository sanctionRepository;
    private final AllocationRepository allocationRepository;

    public AllocationValidator(ProgramRepository programRepository, SanctionRepository sanctionRepository, AllocationRepository allocationRepository) {
        this.programRepository = programRepository;
        this.sanctionRepository = sanctionRepository;
        this.allocationRepository = allocationRepository;
    }

    /**
     * Validates location and program codes and if sanction is present for given sanction id
     * @param allocations
     * @param isCreate
     */
    public void validateAllocation(List<Allocation> allocations, Boolean isCreate) {
        log.info("Validating allocations");
        validateProgramAndLocationCodes(allocations);
        List<Sanction> sanctionsFromSearch = validateSanction(allocations);
        if (Boolean.TRUE.equals(isCreate)) {
            validateForCreate(allocations, sanctionsFromSearch);
        } else {
            validateForUpdate(allocations);
        }
        log.info("Validated allocations");
    }

    /**
     * Validates program and location codes for all allocations
     * @param allocations
     */
    public void validateProgramAndLocationCodes(List<Allocation> allocations) {
        Set<String> programCodes = new HashSet<>();
        Set<String> locationCodes = new HashSet<>();
        for (Allocation allocation : allocations) {
            programCodes.add(allocation.getProgramCode());
            locationCodes.add(allocation.getLocationCode());
        }
        if (programCodes.size() > 1) {
            throw new CustomException(PROGRAM_CODE_ERROR_ALLOCATION, PROGRAM_CODE_ERROR_ALLOCATION_MSG);
        }
        if (locationCodes.size() > 1) {
            throw new CustomException(LOCATION_CODE_ERROR, LOCATION_CODE_ERROR_MSG);
        }

        List<Program> programs = programRepository.searchProgram(ProgramSearch.builder()
                .programCode(allocations.get(0).getProgramCode())
                .locationCode(allocations.get(0).getLocationCode()).build());
        if (programs.isEmpty()) {
            throw new CustomException(NO_PROGRAMS_FOUND, NO_PROGRAMS_FOUND_MSG + allocations.get(0).getProgramCode());
        }
    }

    public List<Sanction> validateSanction(List<Allocation> allocations) {
        Set<String> sanctionIds = allocations.stream().map(Allocation::getSanctionId).collect(Collectors.toSet());
        List<Sanction> sanctionFromSearch = sanctionRepository.searchSanction(SanctionSearch.builder()
                .ids(new ArrayList<>(sanctionIds)).programCode(allocations.get(0).getProgramCode()).build(), false);
        if (sanctionFromSearch.size() != sanctionIds.size()) {
            sanctionIds.removeAll(sanctionFromSearch.stream().map(Sanction::getId).collect(Collectors.toSet()));
            throw new CustomException(SANCTIONS_NOT_FOUND, SANCTIONS_NOT_FOUND_MSG + sanctionIds);
        }
        if (!sanctionFromSearch.get(0).getProgramCode().equalsIgnoreCase(allocations.get(0).getProgramCode())) {
            throw new CustomException(PROGRAM_CODE_ERROR_SANCTION, PROGRAM_CODE_ERROR_SANCTION_MSG);
        }
        return sanctionFromSearch;
    }

    /**
     * Validates if allocated amount exceeds sanctioned amount or deduction amount is less than available amount
     * @param sanctionsFromSearch
     * @param allocations
     */
    public void validateAmountWithSanctionedAmount(List<Sanction> sanctionsFromSearch, List<Allocation> allocations) {
        Map<String, Double> sanctionIdAllocatedAmountMap = new HashMap<>();
        for (Sanction sanction : sanctionsFromSearch) {
            sanctionIdAllocatedAmountMap.put(sanction.getId(), 0.0);
        }

        for (Allocation allocation : allocations) {
            if (allocation.getAllocationType().equals(AllocationType.ALLOCATION))
                sanctionIdAllocatedAmountMap.put(allocation.getSanctionId(), sanctionIdAllocatedAmountMap.get(allocation.getSanctionId()) + allocation.getGrossAmount());
            else
                sanctionIdAllocatedAmountMap.put(allocation.getSanctionId(), sanctionIdAllocatedAmountMap.get(allocation.getSanctionId()) - allocation.getGrossAmount());
        }
        for (Sanction sanction : sanctionsFromSearch) {
            if (sanctionIdAllocatedAmountMap.get(sanction.getId()) > 0 && sanctionIdAllocatedAmountMap.get(sanction.getId()) > (sanction.getGrossAmount() - sanction.getAllocatedAmount()))
                throw new CustomException(SANCTIONED_AMOUNT_ERROR_ALLOCATION, SANCTIONED_AMOUNT_ERROR_ALLOCATION_MSG);
            if (sanctionIdAllocatedAmountMap.get(sanction.getId()) < 0 && Math.abs(sanctionIdAllocatedAmountMap.get(sanction.getId())) > sanction.getAvailableAmount())
                throw new CustomException(AVAILABLE_AMOUNT_ERROR_DEDUCTION, AVAILABLE_AMOUNT_ERROR_DEDUCTION_MSG);
        }
    }

    /**
     * Validates allocation id exists
     * @param allocations
     */
    public void validateForUpdate(List<Allocation> allocations) {
        Set<String> allocationIds = new HashSet<>();
        for (Allocation allocation : allocations) {
            if (allocation.getId() == null || allocation.getId().isEmpty()) {
                throw new CustomException(INVALID_ALLOCATION_ID, INVALID_ALLOCATION_ID_MSG);
            } else {
                allocationIds.add(allocation.getId());
            }
        }
        List<Allocation> allocationsFromSearch = allocationRepository.searchAllocation(AllocationSearch.builder()
                .ids(new ArrayList<>(allocationIds)).build());
        if (allocationsFromSearch.size() != allocationIds.size()) {
            allocationIds.removeAll(allocationsFromSearch.stream().map(Allocation::getId).collect(Collectors.toSet()));
            throw new CustomException(ALLOCATIONS_NOT_FOUND, ALLOCATIONS_NOT_FOUND_MSG + allocationIds);
        }
    }

    /**
     * Validates if allocation id already exists
     * @param allocations
     * @param sanctionsFromSearch
     */
    public void validateForCreate(List<Allocation> allocations, List<Sanction> sanctionsFromSearch) {
        Set<String> idsFromRequest = allocations.stream().filter(allocation -> allocation.getId() != null &&
                !allocation.getId().isEmpty()).map(Allocation::getId).collect(Collectors.toSet());
        if (!idsFromRequest.isEmpty()) {
            List<Allocation> allocationsFromSearch = allocationRepository
                    .searchAllocation(AllocationSearch.builder().ids(new ArrayList<>(idsFromRequest)).build());
            if (!allocationsFromSearch.isEmpty()) {
                List<String> ids = allocationsFromSearch.stream().map(Allocation::getId).collect(Collectors.toList());
                throw new CustomException(DUPLICATE_ALLOCATION_ID, DUPLICATE_ALLOCATION_ID_MSG + ids);
            }
        }

        validateAmountWithSanctionedAmount(sanctionsFromSearch, allocations);
    }

}
