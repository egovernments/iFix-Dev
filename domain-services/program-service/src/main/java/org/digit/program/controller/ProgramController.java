package org.digit.program.controller;

import org.digit.program.models.program.ProgramRequest;
import org.digit.program.models.program.ProgramSearchRequest;
import org.digit.program.models.program.ProgramSearchResponse;
import org.digit.program.service.ProgramService;
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

    public ProgramController(ProgramService programService) {
        this.programService = programService;
    }

    @PostMapping(value = "/program/_create")
    public ResponseEntity<ProgramRequest> createProgram(@RequestBody @Valid ProgramRequest programRequest) {
        return ResponseEntity.ok(programService.pushToKafka(programRequest, "create", "program"));
    }

    @PostMapping(value = "/program/_update")
    public ResponseEntity<ProgramRequest> updateProgram(@RequestBody @Valid ProgramRequest programRequest) {
        return ResponseEntity.ok(programService.pushToKafka(programRequest, "update", "program"));
    }

    @PostMapping(value = "/program/_search")
    public ResponseEntity<ProgramSearchResponse> searchProgram(@RequestBody @Valid ProgramSearchRequest programSearchRequest) {
        return ResponseEntity.ok(programService.searchProgram(programSearchRequest, "search", "program"));
    }

    @PostMapping(value = "/on-program/_create")
    public ResponseEntity<ProgramRequest> onProgramCreate(@RequestBody @Valid ProgramRequest programRequest) {
        return ResponseEntity.ok(programService.pushToKafka(programRequest, "create", "on-program"));
    }

    @PostMapping(value = "/on-program/_update")
    public ResponseEntity<ProgramRequest> onProgramUpdate(@RequestBody @Valid ProgramRequest programRequest) {
            return ResponseEntity.ok(programService.pushToKafka(programRequest, "update", "on-program"));
    }
}
