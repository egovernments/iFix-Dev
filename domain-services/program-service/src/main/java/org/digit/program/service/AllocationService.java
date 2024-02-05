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

    public RequestJsonMessage createAllocation(RequestJsonMessage requestJsonMessage) {
        log.info("Create Allocation");
        commonValidator.validateRequest(requestJsonMessage);
        List<JsonNode> messages = new ArrayList<>();
        Allocation allocation;
        for (int i = 0; i < requestJsonMessage.getMessage().size(); i++) {
            allocation = enrichmentService.enrichAllocationCreate(new Allocation(requestJsonMessage.getMessage().get(i)),
                    requestJsonMessage.getHeader().getReceiverId());
            allocationValidator.validateAllocation(allocation, true);
            Sanction sanction = calculationUtil.calculateSanctionAmount(allocation.getSanctionId(), allocation.getAmount(),
                    allocation.getType().equals(AllocationType.ALLOCATION) ? true : false);
            sanctionRepository.updateSanctionOnAllocation(sanction);
            allocationRepository.saveAllocation(allocation);
            messages.add(allocation.toJsonNode(allocation));
        }
        requestJsonMessage.setMessage(messages);
        dispatcherUtil.forwardMessage(requestJsonMessage);
        return requestJsonMessage;
    }

    public RequestJsonMessage updateAllocation (RequestJsonMessage requestJsonMessage) {
        log.info("Update Allocation");
        commonValidator.validateRequest(requestJsonMessage);
        List<JsonNode> messages = new ArrayList<>();
        Allocation allocation;
        for (int i = 0; i < requestJsonMessage.getMessage().size(); i++) {
            allocation = new Allocation(requestJsonMessage.getMessage().get(i));
            allocationValidator.validateAllocation(allocation, false);
            allocationRepository.updateAllocation(allocation);
            messages.add(allocation.toJsonNode(allocation));
        }
        requestJsonMessage.setMessage(messages);
        dispatcherUtil.forwardMessage(requestJsonMessage);
        return requestJsonMessage;
    }

    public AllocationResponse searchAllocation (AllocationSearchRequest allocationSearchRequest) {
        log.info("Search Allocation");
        List<Allocation> allocations = allocationRepository.searchAllocation(allocationSearchRequest.getAllocationSearch());
        AllocationResponse allocationResponse = AllocationResponse.builder().header(allocationSearchRequest.getHeader())
                .allocations(allocations).build();
        return allocationResponse;
    }

}
