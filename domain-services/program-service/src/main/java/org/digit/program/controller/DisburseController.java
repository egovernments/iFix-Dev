package org.digit.program.controller;

import org.digit.program.configuration.ProgramConfiguration;
import org.digit.program.models.disburse.DisburseSearchRequest;
import org.digit.program.models.disburse.DisburseSearchResponse;
import org.digit.program.models.disburse.DisbursementRequest;
import org.digit.program.service.DisburseService;
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
public class DisburseController {

    private final DisburseService disburseService;
    private final CommonValidator commonValidator;
    private final ProgramConfiguration configs;

    public DisburseController(DisburseService disburseService, CommonValidator commonValidator, ProgramConfiguration configs) {
        this.disburseService = disburseService;
        this.commonValidator = commonValidator;
        this.configs = configs;
    }

    @PostMapping(value = "/disburse/_create")
    public ResponseEntity<DisbursementRequest> createDisburse(@RequestBody @Valid DisbursementRequest disbursementRequest) {
        commonValidator.validateRequest(disbursementRequest.getHeader(), CREATE, DISBURSE);
        if (Boolean.TRUE.equals(configs.getIsDisburseAsync())) {
            return ResponseEntity.ok(disburseService.pushToKafka(disbursementRequest));
        }
        return ResponseEntity.ok(disburseService.createDisburse(disbursementRequest));
    }

    @PostMapping(value = "/disburse/_update")
    public ResponseEntity<DisbursementRequest> updateDisburse(@RequestBody @Valid DisbursementRequest disbursementRequest) {
        commonValidator.validateRequest(disbursementRequest.getHeader(), UPDATE, DISBURSE);
        if (Boolean.TRUE.equals(configs.getIsDisburseAsync())) {
            return ResponseEntity.ok(disburseService.pushToKafka(disbursementRequest));
        }
        return ResponseEntity.ok(disburseService.updateDisburse(disbursementRequest));
    }

    @PostMapping(value = "/disburse/_search")
    public ResponseEntity<DisburseSearchResponse> searchDisburse(@RequestBody @Valid DisburseSearchRequest disburseSearchRequest) {
        return ResponseEntity.ok(disburseService.searchDisburse(disburseSearchRequest, SEARCH, DISBURSE));

    }

    @PostMapping(value = "/on-disburse/_create")
    public ResponseEntity<DisbursementRequest> onDisburseCreate(@RequestBody @Valid DisbursementRequest disbursementRequest) {
        commonValidator.validateRequest(disbursementRequest.getHeader(), CREATE, ON_DISBURSE);
        if (Boolean.TRUE.equals(configs.getIsDisburseAsync())) {
            return ResponseEntity.ok(disburseService.pushToKafka(disbursementRequest));
        }
        return ResponseEntity.ok(disburseService.onDisburseCreate(disbursementRequest));
    }

    @PostMapping(value = "/on-disburse/_update")
    public ResponseEntity<DisbursementRequest> onDisburseUpdate(@RequestBody @Valid DisbursementRequest disbursementRequest) {
        commonValidator.validateRequest(disbursementRequest.getHeader(), UPDATE, ON_DISBURSE);
        if (Boolean.TRUE.equals(configs.getIsDisburseAsync())) {
            return ResponseEntity.ok(disburseService.pushToKafka(disbursementRequest));
        }
        return ResponseEntity.ok(disburseService.onDisburseUpdate(disbursementRequest));
    }


}
