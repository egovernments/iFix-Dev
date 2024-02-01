package org.digit.program.service;

import lombok.extern.slf4j.Slf4j;
import org.digit.program.models.*;
import org.digit.program.repository.AllocationRepository;
import org.digit.program.repository.SanctionRepository;
import org.digit.program.utils.CalculationUtil;
import org.digit.program.utils.DispatcherUtil;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class AllocationService {

    private AllocationRepository allocationRepository;
    private EnrichmentService enrichmentService;
    private DispatcherUtil dispatcherUtil;
    private CalculationUtil calculationUtil;
    private SanctionRepository sanctionRepository;

    public AllocationService(AllocationRepository allocationRepository, EnrichmentService enrichmentService, DispatcherUtil dispatcherUtil, CalculationUtil calculationUtil, SanctionRepository sanctionRepository) {
        this.allocationRepository = allocationRepository;
        this.enrichmentService = enrichmentService;
        this.dispatcherUtil = dispatcherUtil;
        this.calculationUtil = calculationUtil;
        this.sanctionRepository = sanctionRepository;
    }

    public RequestJsonMessage createAllocation(RequestJsonMessage requestJsonMessage) {
        log.info("Create Allocation");
        Allocation allocation = enrichmentService.enrichAllocationCreate(new Allocation(requestJsonMessage.getMessage()), requestJsonMessage);
        Sanction sanction = calculationUtil.calculateSanctionAmount(allocation);
        sanctionRepository.updateSanctionOnAllocation(sanction);
        allocationRepository.saveAllocation(allocation);
        requestJsonMessage.setMessage(allocation.toJsonNode(allocation));
        dispatcherUtil.forwardMessage(requestJsonMessage);
        return requestJsonMessage;
    }

    public RequestJsonMessage updateAllocation (RequestJsonMessage requestJsonMessage) {
        log.info("Update Allocation");
        Allocation allocation = new Allocation(requestJsonMessage.getMessage());
//        allocationValidator.validateUpdateAllocation(allocation);
        allocationRepository.updateAllocation(allocation);
        requestJsonMessage.setMessage(allocation.toJsonNode(allocation));
        dispatcherUtil.forwardMessage(requestJsonMessage);
        return requestJsonMessage;
    }

    public AllocationResponse searchAllocation (AllocationSearchRequest allocationSearchRequest) {
        log.info("Search Allocation");
        allocationSearchRequest.getAllocationSearch().setPagination(enrichmentService.enrichSearch(allocationSearchRequest
                .getAllocationSearch().getPagination()));
        List<Allocation> allocations = allocationRepository.searchAllocation(allocationSearchRequest.getAllocationSearch());
        AllocationResponse allocationResponse = AllocationResponse.builder().header(allocationSearchRequest.getHeader())
                .allocations(allocations).build();
        return allocationResponse;
    }

}
