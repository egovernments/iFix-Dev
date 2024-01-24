package org.digit.program.controller;


import org.digit.program.configuration.ProgramConfiguration;
import org.digit.program.models.ExchangeCode;
import org.digit.program.models.ProgramSearch;
import org.digit.program.models.RequestJsonMessage;
import org.digit.program.service.ProgramService;
import org.digit.program.utils.DispatcherUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/v1")
public class ProgramController {

    private final ProgramService programService;
    private final ProgramConfiguration configs;
    private final DispatcherUtil dispatcherUtil;

    public ProgramController(ProgramService programService, ProgramConfiguration configs, DispatcherUtil dispatcherUtil) {
        this.programService = programService;
        this.configs = configs;
        this.dispatcherUtil = dispatcherUtil;
    }

    @PostMapping(value = "/program/_create")
    public ResponseEntity<RequestJsonMessage> createProgram(@RequestBody RequestJsonMessage requestJsonMessage) {
        if (requestJsonMessage.getHeader().getReceiverId().equalsIgnoreCase(configs.getDomain())) {
            try {
                return ResponseEntity.ok(programService.createProgram(requestJsonMessage));
            } catch (Exception e) {
                return ResponseEntity.badRequest().build();
            }
        } else {
            return ResponseEntity.ok(dispatcherUtil.forwardMessage(requestJsonMessage, true));
        }
    }

    @PostMapping(value = "/program/_search")
    public ResponseEntity<List<ExchangeCode>> searchProgram(@RequestBody ProgramSearch programSearch) {
        try {
            return ResponseEntity.ok(programService.searchProgram(programSearch));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping(value = "/program/_update")
    public ResponseEntity<RequestJsonMessage> updateProgram(@RequestBody RequestJsonMessage requestJsonMessage) {
        if (requestJsonMessage.getHeader().getReceiverId().equalsIgnoreCase(configs.getDomain())) {
            try {
                return ResponseEntity.ok(programService.updateProgram(requestJsonMessage));
            } catch (Exception e) {
                return ResponseEntity.badRequest().build();
            }
        } else {
            return ResponseEntity.ok(dispatcherUtil.forwardMessage(requestJsonMessage, false));
        }
    }

    @PostMapping(value ={ "/on-program/_create", "/on-program/_update"})
    public ResponseEntity<RequestJsonMessage> onProgram(@RequestBody RequestJsonMessage requestJsonMessage) {
        try {
            return ResponseEntity.ok(programService.onProgram(requestJsonMessage));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

}
