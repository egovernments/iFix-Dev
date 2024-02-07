package org.digit.program.controller;

import org.digit.program.models.allocation.AllocationRequest;
import org.digit.program.models.allocation.AllocationResponse;
import org.digit.program.models.allocation.AllocationSearchRequest;
import org.digit.program.service.AllocationService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/v1")
public class AllocationController {

    private final AllocationService allocationService;


    public AllocationController(AllocationService allocationService) {
        this.allocationService = allocationService;
    }

    @PostMapping(value = "/on-allocation/_create")
    public ResponseEntity<AllocationRequest> onAllocationCreate(@RequestBody AllocationRequest allocationRequest) {
        return ResponseEntity.ok(allocationService.createAllocation(allocationRequest));
    }

    @PostMapping(value = "/on-allocation/_update")
    public ResponseEntity<AllocationRequest> onAllocationUpdate(@RequestBody AllocationRequest allocationRequest) {
        return ResponseEntity.ok(allocationService.updateAllocation(allocationRequest));
    }

    @PostMapping(value = "/allocation/_search")
    public ResponseEntity<AllocationResponse> searchAllocation(@RequestBody AllocationSearchRequest allocationSearchRequest) {
        return ResponseEntity.ok(allocationService.searchAllocation(allocationSearchRequest));

    }
}
