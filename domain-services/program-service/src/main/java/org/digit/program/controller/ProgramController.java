package org.digit.program.controller;


import lombok.extern.slf4j.Slf4j;
import org.digit.program.configuration.ProgramConfiguration;
import org.digit.program.models.*;
import org.digit.program.service.ProgramService;
import org.digit.program.utils.DispatcherUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("v1")
@Slf4j
public class ProgramController {

    private final ProgramService programService;

    public ProgramController(ProgramService programService) {
        this.programService = programService;
    }

    @PostMapping(value = "/program/_create")
    public ResponseEntity<RequestJsonMessage> createProgram(@RequestBody RequestJsonMessage requestJsonMessage) {
        try {
            return ResponseEntity.ok(programService.createProgram(requestJsonMessage));
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping(value = "/program/_search")
    public ResponseEntity<ProgramSearchResponse> searchProgram(@RequestBody ProgramSearchRequest programSearchRequest) {
        try {
            return ResponseEntity.ok(programService.searchProgram(programSearchRequest));
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping(value = "/program/_update")
    public ResponseEntity<RequestJsonMessage> updateProgram(@RequestBody RequestJsonMessage requestJsonMessage) {
        try {
            return ResponseEntity.ok(programService.updateProgram(requestJsonMessage));
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping(value ={ "/on-program/_create", "/on-program/_update"})
    public ResponseEntity<RequestJsonMessage> onProgram(@RequestBody RequestJsonMessage requestJsonMessage) {
        try {
            return ResponseEntity.ok(programService.onProgram(requestJsonMessage));
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

}
