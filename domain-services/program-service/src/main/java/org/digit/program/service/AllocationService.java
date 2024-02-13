package org.digit.program.service;

import lombok.extern.slf4j.Slf4j;
import org.digit.program.models.allocation.Allocation;
import org.digit.program.models.allocation.AllocationRequest;
import org.digit.program.models.allocation.AllocationResponse;
import org.digit.program.models.allocation.AllocationSearchRequest;
import org.digit.program.models.sanction.Sanction;
import org.digit.program.repository.AllocationRepository;
import org.digit.program.utils.CalculationUtil;
import org.digit.program.utils.DispatcherUtil;
import org.digit.program.validator.AllocationValidator;
import org.digit.program.validator.CommonValidator;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class AllocationService {

    private final AllocationRepository allocationRepository;
    private final EnrichmentService enrichmentService;
    private final DispatcherUtil dispatcherUtil;
    private final CalculationUtil calculationUtil;
    private final CommonValidator commonValidator;
    private final AllocationValidator allocationValidator;

    public AllocationService(AllocationRepository allocationRepository, EnrichmentService enrichmentService, DispatcherUtil dispatcherUtil, CalculationUtil calculationUtil,  CommonValidator commonValidator, AllocationValidator allocationValidator) {
        this.allocationRepository = allocationRepository;
        this.enrichmentService = enrichmentService;
        this.dispatcherUtil = dispatcherUtil;
        this.calculationUtil = calculationUtil;
        this.commonValidator = commonValidator;
        this.allocationValidator = allocationValidator;
    }

    public AllocationRequest createAllocation(AllocationRequest allocationRequest) {
        log.info("Create Allocation");
        commonValidator.validateRequest(allocationRequest.getHeader());
        allocationValidator.validateAllocation(allocationRequest.getAllocations(), true);
        enrichmentService.enrichAllocationCreate(allocationRequest.getAllocations(), allocationRequest.getHeader().getSenderId());
        List<Sanction> sanctions = calculationUtil.calculateAndReturnSanction(allocationRequest.getAllocations());
        allocationRepository.saveAllocationsAndSanctions(allocationRequest.getAllocations(), sanctions);
        dispatcherUtil.dispatchOnAllocation(allocationRequest);
        return allocationRequest;
    }

    public AllocationRequest updateAllocation (AllocationRequest allocationRequest) {
        log.info("Update Allocation");
        commonValidator.validateRequest(allocationRequest.getHeader());
        allocationValidator.validateAllocation(allocationRequest.getAllocations(), false);
        enrichmentService.enrichAllocationUpdate(allocationRequest.getAllocations(), allocationRequest.getHeader().getSenderId());
        allocationRepository.updateAllocation(allocationRequest.getAllocations());
        dispatcherUtil.dispatchOnAllocation(allocationRequest);
        return allocationRequest;
    }

    public AllocationResponse searchAllocation (AllocationSearchRequest allocationSearchRequest) {
        log.info("Search Allocation");
        List<Allocation> allocations = allocationRepository.searchAllocation(allocationSearchRequest.getAllocationSearch());
        return AllocationResponse.builder().header(allocationSearchRequest.getHeader())
                .allocations(allocations).build();
    }

}
