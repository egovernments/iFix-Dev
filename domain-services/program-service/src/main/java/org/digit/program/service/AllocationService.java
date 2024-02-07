package org.digit.program.service;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.digit.program.constants.AllocationType;
import org.digit.program.models.*;
import org.digit.program.repository.AllocationRepository;
import org.digit.program.repository.SanctionRepository;
import org.digit.program.utils.CalculationUtil;
import org.digit.program.utils.DispatcherUtil;
import org.digit.program.validator.AllocationValidator;
import org.digit.program.validator.CommonValidator;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class AllocationService {

    private final AllocationRepository allocationRepository;
    private final EnrichmentService enrichmentService;
    private final DispatcherUtil dispatcherUtil;
    private final CalculationUtil calculationUtil;
    private final SanctionRepository sanctionRepository;
    private final CommonValidator commonValidator;
    private final AllocationValidator allocationValidator;

    public AllocationService(AllocationRepository allocationRepository, EnrichmentService enrichmentService, DispatcherUtil dispatcherUtil, CalculationUtil calculationUtil, SanctionRepository sanctionRepository, CommonValidator commonValidator, AllocationValidator allocationValidator) {
        this.allocationRepository = allocationRepository;
        this.enrichmentService = enrichmentService;
        this.dispatcherUtil = dispatcherUtil;
        this.calculationUtil = calculationUtil;
        this.sanctionRepository = sanctionRepository;
        this.commonValidator = commonValidator;
        this.allocationValidator = allocationValidator;
    }

    public AllocationRequest createAllocation(AllocationRequest allocationRequest) {
        log.info("Create Allocation");
        commonValidator.validateRequest(allocationRequest.getHeader());
        Allocation allocation;
        for (int i = 0; i < allocationRequest.getAllocations().size(); i++) {
            allocation = allocationRequest.getAllocations().get(i);
            enrichmentService.enrichAllocationCreate(allocation,
                    allocationRequest.getHeader().getReceiverId());
            allocationValidator.validateAllocation(allocation, true);
            Sanction sanction = calculationUtil.calculateSanctionAmount(allocation.getSanctionId(), allocation.getAmount(),
                    allocation.getType().equals(AllocationType.ALLOCATION) ? true : false);
            sanctionRepository.updateSanctionOnAllocation(sanction);
            allocationRepository.saveAllocation(allocation);
        }
        dispatcherUtil.forwardMessage(allocationRequest.getId(), allocationRequest.getSignature(), allocationRequest.getHeader(), allocationRequest.getAllocations().toString());
        return allocationRequest;
    }

    public AllocationRequest updateAllocation (AllocationRequest allocationRequest) {
        log.info("Update Allocation");
        commonValidator.validateRequest(allocationRequest.getHeader());
        Allocation allocation;
        for (int i = 0; i < allocationRequest.getAllocations().size(); i++) {
            allocation = allocationRequest.getAllocations().get(i);
            allocationValidator.validateAllocation(allocation, false);
            allocationRepository.updateAllocation(allocation);
        }
        dispatcherUtil.forwardMessage(allocationRequest.getId(), allocationRequest.getSignature(),
                allocationRequest.getHeader(), allocationRequest.getAllocations().toString());
        return allocationRequest;
    }

    public AllocationResponse searchAllocation (AllocationSearchRequest allocationSearchRequest) {
        log.info("Search Allocation");
        List<Allocation> allocations = allocationRepository.searchAllocation(allocationSearchRequest.getAllocationSearch());
        return AllocationResponse.builder().header(allocationSearchRequest.getHeader())
                .allocations(allocations).build();
    }

}
