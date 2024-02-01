package org.digit.program.controller;

import org.digit.program.models.AllocationResponse;
import org.digit.program.models.AllocationSearchRequest;
import org.digit.program.models.RequestJsonMessage;
import org.digit.program.service.AllocationService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/v1")
public class AllocationController {

    private AllocationService allocationService;


    public AllocationController(AllocationService allocationService) {
        this.allocationService = allocationService;
    }

    @PostMapping(value = "/on-allocation/_create")
    public ResponseEntity<RequestJsonMessage> onAllocationCreate(@RequestBody RequestJsonMessage requestJsonMessage) {
        return ResponseEntity.ok(allocationService.createAllocation(requestJsonMessage));
    }

    @PostMapping(value = "/on-allocation/_update")
    public ResponseEntity<RequestJsonMessage> onAllocationUpdate(@RequestBody RequestJsonMessage requestJsonMessage) {
        return ResponseEntity.ok(allocationService.updateAllocation(requestJsonMessage));
    }

    public ResponseEntity<AllocationResponse> searchAllocation(AllocationSearchRequest allocationSearchRequest) {
        return ResponseEntity.ok(allocationService.searchAllocation(allocationSearchRequest));

    }
}
