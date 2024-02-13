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

    public void validateAllocation(List<Allocation> allocations, boolean isCreate) {

        List<Program> programs = programRepository.searchProgram(ProgramSearch.builder().programCode(allocations.get(0).getProgramCode()).build());
        if (programs.isEmpty())
            throw new CustomException("PROGRAM_NOT_FOUND", "Program not found for ProgramCode: " + allocations.get(0).getProgramCode());

        Set<String> sanctionIds = allocations.stream().map(Allocation::getSanctionId).collect(Collectors.toSet());
        List<Sanction> sanctionFromSearch = sanctionRepository.searchSanction(SanctionSearch.builder()
                .ids(new ArrayList<>(sanctionIds)).build());
        if (sanctionFromSearch.size() != sanctionIds.size()) {
            sanctionIds.removeAll(sanctionFromSearch.stream().map(Sanction::getId).collect(Collectors.toSet()));
            throw new IllegalArgumentException("No sanction found for id(s): " + sanctionIds);
        }
        Map<String, Double> sanctionIdAllocatedAmountMap = new HashMap<>();
        for (Sanction sanction : sanctionFromSearch) {
            sanctionIdAllocatedAmountMap.put(sanction.getId(), 0.0);
        }

        for (Allocation allocation : allocations) {
            if (allocation.getType().equals(AllocationType.ALLOCATION))
                sanctionIdAllocatedAmountMap.put(allocation.getSanctionId(), sanctionIdAllocatedAmountMap.get(allocation.getSanctionId()) + allocation.getAmount());
            else
                sanctionIdAllocatedAmountMap.put(allocation.getSanctionId(), sanctionIdAllocatedAmountMap.get(allocation.getSanctionId()) - allocation.getAmount());
        }
        for (Sanction sanction : sanctionFromSearch) {
            if (sanctionIdAllocatedAmountMap.get(sanction.getId()) > 0 && sanctionIdAllocatedAmountMap.get(sanction.getId()) > (sanction.getSanctionedAmount() - sanction.getAllocatedAmount()))
                throw new CustomException("SANCTIONED_AMOUNT_ERROR", "Sanctioned amount should be greater than allocated amount");
            if (sanctionIdAllocatedAmountMap.get(sanction.getId()) < 0 && Math.abs(sanctionIdAllocatedAmountMap.get(sanction.getId())) > sanction.getAvailableAmount())
                throw new CustomException("AVAILABLE_AMOUNT_ERROR", "Available amount should be greater than deduction amount");
        }

        if (!isCreate) {
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
                throw new IllegalArgumentException("No allocation found for id(s): " + allocationIds);
            }
        }
    }



}
