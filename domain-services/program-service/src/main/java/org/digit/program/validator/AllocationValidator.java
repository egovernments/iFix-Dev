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

        validateProgramAndLocationCodes(allocations);

        List<Sanction> sanctionsFromSearch = validateSanction(allocations);

        if (Boolean.TRUE.equals(isCreate)) {
            validateForCreate(allocations, sanctionsFromSearch);
        } else {
            validateForUpdate(allocations);
        }
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
            throw new CustomException("PROGRAM_CODE_ERROR", "Program code should be same for all allocations");
        }
        if (locationCodes.size() > 1) {
            throw new CustomException("LOCATION_CODE_ERROR", "Location code should be same for all allocations");
        }

        List<Program> programs = programRepository.searchProgram(ProgramSearch.builder()
                .programCode(allocations.get(0).getProgramCode())
                .locationCode(allocations.get(0).getLocationCode()).build());
        if (programs.isEmpty()) {
            throw new CustomException("NO_PROGRAMS_FOUND", "No program found for code: " + allocations.get(0).getProgramCode());
        }
    }

    public List<Sanction> validateSanction(List<Allocation> allocations) {
        Set<String> sanctionIds = allocations.stream().map(Allocation::getSanctionId).collect(Collectors.toSet());
        List<Sanction> sanctionFromSearch = sanctionRepository.searchSanction(SanctionSearch.builder()
                .ids(new ArrayList<>(sanctionIds)).programCode(allocations.get(0).getProgramCode()).build());
        if (sanctionFromSearch.size() != sanctionIds.size()) {
            sanctionIds.removeAll(sanctionFromSearch.stream().map(Sanction::getId).collect(Collectors.toSet()));
            throw new CustomException("SANCTIONS_NOT_FOUND", "No sanction found for id(s): " + sanctionIds);
        }
        if (!sanctionFromSearch.get(0).getProgramCode().equalsIgnoreCase(allocations.get(0).getProgramCode())) {
            throw new CustomException("PROGRAM_CODE_ERROR", "Program code should be same as sanction program code");
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
            if (allocation.getType().equals(AllocationType.ALLOCATION))
                sanctionIdAllocatedAmountMap.put(allocation.getSanctionId(), sanctionIdAllocatedAmountMap.get(allocation.getSanctionId()) + allocation.getGrossAmount());
            else
                sanctionIdAllocatedAmountMap.put(allocation.getSanctionId(), sanctionIdAllocatedAmountMap.get(allocation.getSanctionId()) - allocation.getGrossAmount());
        }
        for (Sanction sanction : sanctionsFromSearch) {
            if (sanctionIdAllocatedAmountMap.get(sanction.getId()) > 0 && sanctionIdAllocatedAmountMap.get(sanction.getId()) > (sanction.getGrossAmount() - sanction.getAllocatedAmount()))
                throw new CustomException("SANCTIONED_AMOUNT_ERROR", "Sanctioned amount should be greater than allocated amount");
            if (sanctionIdAllocatedAmountMap.get(sanction.getId()) < 0 && Math.abs(sanctionIdAllocatedAmountMap.get(sanction.getId())) > sanction.getAvailableAmount())
                throw new CustomException("AVAILABLE_AMOUNT_ERROR", "Available amount should be greater than deduction amount");
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
                throw new CustomException("INVALID_ALLOCATION_ID", "Allocation id cannot be empty");
            } else {
                allocationIds.add(allocation.getId());
            }
        }
        List<Allocation> allocationsFromSearch = allocationRepository.searchAllocation(AllocationSearch.builder()
                .ids(new ArrayList<>(allocationIds)).build());
        if (allocationsFromSearch.size() != allocationIds.size()) {
            allocationIds.removeAll(allocationsFromSearch.stream().map(Allocation::getId).collect(Collectors.toSet()));
            throw new CustomException("ALLOCATIONS_NOT_FOUND", "No allocation found for id(s): " + allocationIds);
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
                throw new CustomException("DUPLICATE_ALLOCATION_ID", "Duplicate allocation id(s): " + ids);
            }
        }

        validateAmountWithSanctionedAmount(sanctionsFromSearch, allocations);
    }

}
