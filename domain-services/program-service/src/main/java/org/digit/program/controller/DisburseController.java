package org.digit.program.controller;

import org.digit.program.models.RequestJsonMessage;
import org.digit.program.service.DisburseService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/v1")
public class DisburseController {

    private DisburseService disburseService;

    public DisburseController(DisburseService disburseService) {
        this.disburseService = disburseService;
    }

    @PostMapping(value = "/disburse/_create")
    public ResponseEntity<RequestJsonMessage> createDisburse(@RequestBody RequestJsonMessage requestJsonMessage) {
        return ResponseEntity.ok(disburseService.createDisburse(requestJsonMessage));
    }

}
