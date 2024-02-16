package org.digit.program.controller;

import org.digit.program.models.sanction.SanctionRequest;
import org.digit.program.models.sanction.SanctionSearchRequest;
import org.digit.program.models.sanction.SanctionSearchResponse;
import org.digit.program.service.SanctionService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequestMapping("/v1")
public class SanctionController {
    private final SanctionService sanctionService;

    public SanctionController(SanctionService sanctionService) {
        this.sanctionService = sanctionService;
    }

    @PostMapping(value = "/on-sanction/_create")
    public ResponseEntity<SanctionRequest> onSanctionCreate(@RequestBody @Valid SanctionRequest sanctionRequest) {
        return ResponseEntity.ok(sanctionService.createSanction(sanctionRequest, "create"));
    }

    @PostMapping(value = "/on-sanction/_update")
    public ResponseEntity<SanctionRequest> onSanctionUpdate(@RequestBody @Valid SanctionRequest sanctionRequest) {
        return ResponseEntity.ok(sanctionService.updateSanction(sanctionRequest, "update"));
    }

    @PostMapping(value = "/sanction/_search")
    public ResponseEntity<SanctionSearchResponse> sanctionSearch(@RequestBody @Valid SanctionSearchRequest sanctionSearchRequest) {
        return ResponseEntity.ok(sanctionService.searchSanction(sanctionSearchRequest, "search"));
    }

}
