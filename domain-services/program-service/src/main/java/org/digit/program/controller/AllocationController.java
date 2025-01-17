package org.digit.program.controller;

import org.digit.program.configuration.ProgramConfiguration;
import org.digit.program.models.allocation.AllocationRequest;
import org.digit.program.models.allocation.AllocationResponse;
import org.digit.program.models.allocation.AllocationSearchRequest;
import org.digit.program.service.AllocationService;
import org.digit.program.validator.CommonValidator;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.validation.Valid;

import static org.digit.program.constants.ProgramConstants.*;

@Controller
@RequestMapping("/v1")
public class AllocationController {

    private final AllocationService allocationService;
    private final CommonValidator commonValidator;
    private final ProgramConfiguration configs;

    public AllocationController(AllocationService allocationService, CommonValidator commonValidator, ProgramConfiguration configs) {
        this.allocationService = allocationService;
        this.commonValidator = commonValidator;
        this.configs = configs;
    }

    @PostMapping(value = "/on-allocation/_create")
    public ResponseEntity<AllocationRequest> onAllocationCreate(@RequestBody @Valid AllocationRequest allocationRequest) {
        commonValidator.validateRequest(allocationRequest.getHeader(), CREATE, ON_ALLOCATION);
        if (Boolean.TRUE.equals(configs.getIsAllocationAsync())) {
            return ResponseEntity.ok(allocationService.pushToKafka(allocationRequest));
        }
        return ResponseEntity.ok(allocationService.createAllocation(allocationRequest));
    }

    @PostMapping(value = "/on-allocation/_update")
    public ResponseEntity<AllocationRequest> onAllocationUpdate(@RequestBody @Valid AllocationRequest allocationRequest) {
        commonValidator.validateRequest(allocationRequest.getHeader(), UPDATE, ON_ALLOCATION);
        if (Boolean.TRUE.equals(configs.getIsAllocationAsync())) {
            return ResponseEntity.ok(allocationService.pushToKafka(allocationRequest));
        }
        return ResponseEntity.ok(allocationService.updateAllocation(allocationRequest));
    }

    @PostMapping(value = "/allocation/_search")
    public ResponseEntity<AllocationResponse> searchAllocation(@RequestBody @Valid AllocationSearchRequest allocationSearchRequest) {
        return ResponseEntity.ok(allocationService.searchAllocation(allocationSearchRequest, SEARCH, ALLOCATION));

    }
}
