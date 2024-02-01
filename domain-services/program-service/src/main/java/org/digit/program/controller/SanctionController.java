package org.digit.program.controller;

import org.digit.program.models.*;
import org.digit.program.service.SanctionService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/v1")
public class SanctionController {
    private final SanctionService sanctionService;

    public SanctionController(SanctionService sanctionService) {
        this.sanctionService = sanctionService;
    }

    @PostMapping(value = "/on-sanction/_create")
    public ResponseEntity<RequestJsonMessage> onSanctionCreate(@RequestBody RequestJsonMessage requestJsonMessage) {
        return ResponseEntity.ok(sanctionService.createSanction(requestJsonMessage));
    }

    @PostMapping(value = "/on-sanction/_update")
    public ResponseEntity<RequestJsonMessage> onSanctionUpdate(@RequestBody RequestJsonMessage requestJsonMessage) {
        return ResponseEntity.ok(sanctionService.updateSanction(requestJsonMessage));
    }

    @PostMapping(value = "/sanction/_search")
    public ResponseEntity<SanctionSearchResponse> sanctionSearch(@RequestBody SanctionSearchRequest sanctionSearchRequest) {
        return ResponseEntity.ok(sanctionService.searchSanction(sanctionSearchRequest));
    }

}
