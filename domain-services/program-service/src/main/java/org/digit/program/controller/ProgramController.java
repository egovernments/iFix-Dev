package org.digit.program.controller;

import org.digit.program.configuration.ProgramConfiguration;
import org.digit.program.models.program.ProgramRequest;
import org.digit.program.models.program.ProgramSearchRequest;
import org.digit.program.models.program.ProgramSearchResponse;
import org.digit.program.service.ProgramService;
import org.digit.program.validator.CommonValidator;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequestMapping("/v1")
public class ProgramController {

    private final ProgramService programService;
    private final CommonValidator commonValidator;
    private final ProgramConfiguration configs;

    public ProgramController(ProgramService programService, CommonValidator commonValidator, ProgramConfiguration configs) {
        this.programService = programService;
        this.commonValidator = commonValidator;
        this.configs = configs;
    }

    @PostMapping(value = "/program/_create")
    public ResponseEntity<ProgramRequest> createProgram(@RequestBody @Valid ProgramRequest programRequest) {
        commonValidator.validateRequest(programRequest.getHeader(), "create", "program");
        if (Boolean.TRUE.equals(configs.getIsProgramAsync())) {
            return ResponseEntity.ok(programService.pushToKafka(programRequest));
        }
        return ResponseEntity.ok(programService.createProgram(programRequest));
    }

    @PostMapping(value = "/program/_update")
    public ResponseEntity<ProgramRequest> updateProgram(@RequestBody @Valid ProgramRequest programRequest) {
        commonValidator.validateRequest(programRequest.getHeader(), "update", "program");
        if (Boolean.TRUE.equals(configs.getIsProgramAsync())) {
            return ResponseEntity.ok(programService.pushToKafka(programRequest));
        }
        return ResponseEntity.ok(programService.updateProgram(programRequest));
    }

    @PostMapping(value = "/program/_search")
    public ResponseEntity<ProgramSearchResponse> searchProgram(@RequestBody @Valid ProgramSearchRequest programSearchRequest) {
        return ResponseEntity.ok(programService.searchProgram(programSearchRequest, "search", "program"));
    }

    @PostMapping(value = "/on-program/_create")
    public ResponseEntity<ProgramRequest> onProgramCreate(@RequestBody @Valid ProgramRequest programRequest) {
        commonValidator.validateRequest(programRequest.getHeader(), "create", "on-program");
        if (Boolean.TRUE.equals(configs.getIsProgramAsync())) {
            return ResponseEntity.ok(programService.pushToKafka(programRequest));
        }
        return ResponseEntity.ok(programService.onProgramCreate(programRequest));
    }

    @PostMapping(value = "/on-program/_update")
    public ResponseEntity<ProgramRequest> onProgramUpdate(@RequestBody @Valid ProgramRequest programRequest) {
        commonValidator.validateRequest(programRequest.getHeader(), "update", "on-program");
        if (Boolean.TRUE.equals(configs.getIsProgramAsync())) {
            return ResponseEntity.ok(programService.pushToKafka(programRequest));
        }
        return ResponseEntity.ok(programService.onProgramUpdate(programRequest));
    }
}
