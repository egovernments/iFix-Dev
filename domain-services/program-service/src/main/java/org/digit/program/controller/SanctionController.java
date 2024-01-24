package org.digit.program.controller;

import org.digit.program.models.RequestJsonMessage;
import org.digit.program.service.SanctionService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/v1")
public class SanctionController {
    private final SanctionService sanctionService;

    public SanctionController(SanctionService sanctionService) {
        this.sanctionService = sanctionService;
    }

    @PostMapping(value = "/on-sanction")
    public ResponseEntity<RequestJsonMessage> onSanction(RequestJsonMessage requestJsonMessage) {
        return ResponseEntity.ok(sanctionService.createSanction(requestJsonMessage));
    }


}
