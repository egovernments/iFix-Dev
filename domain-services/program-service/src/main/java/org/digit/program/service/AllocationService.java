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
     * Pushes to kafka topic
     * @param allocationRequest
     * @return
     */
    public AllocationRequest pushToKafka(AllocationRequest allocationRequest) {
        log.info("pushToKafka");
        producer.push(configs.getAllocationTopic(), allocationRequest);
        return allocationRequest;
    }

    /**
     * Validates, enriches, persists and dispatches on-allocation create request
     * @param allocationRequest
     */
    public AllocationRequest createAllocation(AllocationRequest allocationRequest) {
        log.info("Create Allocation");
        try {
            allocationValidator.validateAllocation(allocationRequest.getAllocation().getChildren(), true);
            // Validates if receiver id is same as configured in mdms
            commonValidator.validateReply(allocationRequest.getHeader(), allocationRequest.getAllocation().getChildren().get(0).getLocationCode());
            enrichmentService.enrichAllocationCreate(allocationRequest.getAllocation(), allocationRequest.getHeader());
            // Calculates the allocated and available amount for each sanction in the list of allocation and returns the sanctions
            List<Sanction> sanctions = calculationUtil.calculateAndReturnSanctionForAllocation(allocationRequest.getAllocation().getChildren());
            // Saves allocations and sanctions in transactional manner
            allocationRepository.saveAllocationsAndSanctions(allocationRequest.getAllocation().getChildren(), sanctions);
            // Forwards on allocation message to exchange service if sender and current domains are same
            dispatcherUtil.dispatchOnAllocation(allocationRequest);
            log.info("Allocation created successfully");
        } catch (CustomException exception) {
            log.error("Error while creating allocation", exception);
            errorHandler.handleAllocationError(allocationRequest, exception);
        }
        return allocationRequest;
    }

    /**
     * Validates, enriches persists and dispatches on-allocation update request
     * @param allocationRequest
     */
    public AllocationRequest updateAllocation (AllocationRequest allocationRequest) {
        log.info("Update Allocation");
        try {
            allocationValidator.validateAllocation(allocationRequest.getAllocation().getChildren(), false);
            commonValidator.validateReply(allocationRequest.getHeader(), allocationRequest.getAllocation().getChildren().get(0).getLocationCode());
            enrichmentService.enrichAllocationUpdate(allocationRequest.getAllocation().getChildren(), allocationRequest.getHeader().getSenderId());
            allocationRepository.updateAllocation(allocationRequest.getAllocation().getChildren());
            dispatcherUtil.dispatchOnAllocation(allocationRequest);
            log.info("Allocation updated successfully");
        } catch (CustomException exception) {
            log.error("Error while updating allocation", exception);
            errorHandler.handleAllocationError(allocationRequest, exception);
        }
        return allocationRequest;
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
        log.info("Found {} allocations", allocations.size());
        return AllocationResponse.builder().header(allocationSearchRequest.getHeader())
                .allocations(allocations).build();
    }

}
