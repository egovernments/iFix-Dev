package org.digit.program.controller;

import org.digit.program.configuration.ProgramConfiguration;
import org.digit.program.models.sanction.SanctionRequest;
import org.digit.program.models.sanction.SanctionSearchRequest;
import org.digit.program.models.sanction.SanctionSearchResponse;
import org.digit.program.service.SanctionService;
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
public class SanctionController {
    private final SanctionService sanctionService;
    private final CommonValidator commonValidator;
    private final ProgramConfiguration configs;

    public SanctionController(SanctionService sanctionService, CommonValidator commonValidator, ProgramConfiguration configs) {
        this.sanctionService = sanctionService;
        this.commonValidator = commonValidator;
        this.configs = configs;
    }

    @PostMapping(value = "/on-sanction/_create")
    public ResponseEntity<SanctionRequest> onSanctionCreate(@RequestBody @Valid SanctionRequest sanctionRequest) {
        commonValidator.validateRequest(sanctionRequest.getHeader(), CREATE, ON_SANCTION);
        if (Boolean.TRUE.equals(configs.getIsSanctionAsync())) {
            return ResponseEntity.ok(sanctionService.pushToKafka(sanctionRequest));
        }
        return ResponseEntity.ok(sanctionService.createSanction(sanctionRequest));
    }

    @PostMapping(value = "/on-sanction/_update")
    public ResponseEntity<SanctionRequest> onSanctionUpdate(@RequestBody @Valid SanctionRequest sanctionRequest) {
        commonValidator.validateRequest(sanctionRequest.getHeader(), UPDATE, ON_SANCTION);
        if (Boolean.TRUE.equals(configs.getIsSanctionAsync())) {
            return ResponseEntity.ok(sanctionService.pushToKafka(sanctionRequest));
        }
        return ResponseEntity.ok(sanctionService.updateSanction(sanctionRequest));
    }

    @PostMapping(value = "/sanction/_search")
    public ResponseEntity<SanctionSearchResponse> sanctionSearch(@RequestBody @Valid SanctionSearchRequest sanctionSearchRequest) {
        return ResponseEntity.ok(sanctionService.searchSanction(sanctionSearchRequest, SEARCH, SANCTION));
    }

}
