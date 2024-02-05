package org.digit.program.validator;

import lombok.extern.slf4j.Slf4j;
import org.digit.program.constants.AllocationType;
import org.digit.program.models.*;
import org.digit.program.repository.AllocationRepository;
import org.digit.program.repository.ProgramRepository;
import org.digit.program.repository.SanctionRepository;
import org.egov.tracer.model.CustomException;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

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

    public void validateAllocation(Allocation allocation, boolean isCreate) {

        List<Program> programs = programRepository.searchProgram(ProgramSearch.builder().programCode(allocation.getProgramCode()).build());
        if (programs.size() == 0)
            throw new CustomException("PROGRAM_NOT_FOUND", "Program not found for ProgramCode: " + allocation.getProgramCode());

        List<Sanction> sanctions = sanctionRepository.searchSanction(SanctionSearch.builder().ids(Collections.singletonList(allocation.getSanctionId())).build());
        if (sanctions.size() == 0)
            throw new CustomException("SANCTION_NOT_FOUND", "Sanction not found for id: " + allocation.getSanctionId());

        if (allocation.getType().equals(AllocationType.ALLOCATION) && allocation.getAmount() > (sanctions.get(0).getSanctionedAmount()) - sanctions.get(0).getAllocatedAmount())
            throw new CustomException("SANCTIONED_AMOUNT_ERROR", "Sanctioned amount should be greater than allocated amount");

        if (allocation.getType().equals(AllocationType.DEDUCTION) && allocation.getAmount() > sanctions.get(0).getAvailableAmount())
            throw new CustomException("AVAILABLE_AMOUNT_ERROR", "Available amount should be greater than deduction amount");

        if (!isCreate) {
            List<Allocation> allocations = allocationRepository.searchAllocation(AllocationSearch.builder().ids(Collections.singletonList(allocation.getId())).build());
            if (allocations.size() == 0)
                throw new CustomException("ALLOCATION_NOT_FOUND", "Allocation not found for id: " + allocation.getId());
        }

    }



}
