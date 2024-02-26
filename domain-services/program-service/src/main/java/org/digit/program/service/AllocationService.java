package org.digit.program.service;

import lombok.extern.slf4j.Slf4j;
import org.digit.program.configuration.ProgramConfiguration;
import org.digit.program.kafka.ProgramProducer;
import org.digit.program.models.allocation.Allocation;
import org.digit.program.models.allocation.AllocationRequest;
import org.digit.program.models.allocation.AllocationResponse;
import org.digit.program.models.allocation.AllocationSearchRequest;
import org.digit.program.models.sanction.Sanction;
import org.digit.program.repository.AllocationRepository;
import org.digit.program.utils.CalculationUtil;
import org.digit.program.utils.DispatcherUtil;
import org.digit.program.utils.ErrorHandler;
import org.digit.program.validator.AllocationValidator;
import org.digit.program.validator.CommonValidator;
import org.egov.tracer.model.CustomException;
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
    private final ProgramProducer producer;
    private final ProgramConfiguration configs;
    private final ErrorHandler errorHandler;

    public AllocationService(AllocationRepository allocationRepository, EnrichmentService enrichmentService,
                             DispatcherUtil dispatcherUtil, CalculationUtil calculationUtil,
                             CommonValidator commonValidator, AllocationValidator allocationValidator,
                             ProgramProducer producer, ProgramConfiguration configs, ErrorHandler errorHandler) {
        this.allocationRepository = allocationRepository;
        this.enrichmentService = enrichmentService;
        this.dispatcherUtil = dispatcherUtil;
        this.calculationUtil = calculationUtil;
        this.commonValidator = commonValidator;
        this.allocationValidator = allocationValidator;
        this.producer = producer;
        this.configs = configs;
        this.errorHandler = errorHandler;
    }

    /**
     * Validates url messageType and action with header and push to kafka topic
     * @param allocationRequest
     * @param action
     * @param messageType
     * @return
     */
    public AllocationRequest pushToKafka(AllocationRequest allocationRequest, String action, String messageType) {
        log.info("pushToKafka");
        commonValidator.validateRequest(allocationRequest.getHeader(), action, messageType);
        producer.push(configs.getAllocationTopic(), allocationRequest);
        return allocationRequest;
    }

    /**
     * Validates, enriches, persists and dispatches on-allocation create request
     * @param allocationRequest
     */
    public void createAllocation(AllocationRequest allocationRequest) {
        log.info("Create Allocation");
        try {
            allocationValidator.validateAllocation(allocationRequest.getAllocations(), true);
            commonValidator.validateReply(allocationRequest.getHeader(), allocationRequest.getAllocations().get(0).getProgramCode(),
                    allocationRequest.getAllocations().get(0).getLocationCode());
            enrichmentService.enrichAllocationCreate(allocationRequest.getAllocations(), allocationRequest.getHeader().getSenderId());
            List<Sanction> sanctions = calculationUtil.calculateAndReturnSanction(allocationRequest.getAllocations());
            allocationRepository.saveAllocationsAndSanctions(allocationRequest.getAllocations(), sanctions);
            dispatcherUtil.dispatchOnAllocation(allocationRequest);
        } catch (CustomException exception) {
            errorHandler.handleAllocationError(allocationRequest, exception);
        }

    }

    /**
     * Validates, enriches persists and dispatches on-allocation update request
     * @param allocationRequest
     */
    public void updateAllocation (AllocationRequest allocationRequest) {
        log.info("Update Allocation");
        try {
            allocationValidator.validateAllocation(allocationRequest.getAllocations(), false);
            commonValidator.validateReply(allocationRequest.getHeader(), allocationRequest.getAllocations().get(0).getProgramCode(),
                    allocationRequest.getAllocations().get(0).getLocationCode());
            enrichmentService.enrichAllocationUpdate(allocationRequest.getAllocations(), allocationRequest.getHeader().getSenderId());
            allocationRepository.updateAllocation(allocationRequest.getAllocations());
            dispatcherUtil.dispatchOnAllocation(allocationRequest);
        } catch (CustomException exception) {
            errorHandler.handleAllocationError(allocationRequest, exception);
        }

    }

    /**
     * Validates url with header and searches for allocation
     * @param allocationSearchRequest
     * @param action
     * @param messageType
     * @return
     */
    public AllocationResponse searchAllocation (AllocationSearchRequest allocationSearchRequest, String action, String messageType) {
        log.info("Search Allocation");
        commonValidator.validateRequest(allocationSearchRequest.getHeader(), action, messageType);
        List<Allocation> allocations = allocationRepository.searchAllocation(allocationSearchRequest.getAllocationSearch());
        return AllocationResponse.builder().header(allocationSearchRequest.getHeader())
                .allocations(allocations).build();
    }

}
