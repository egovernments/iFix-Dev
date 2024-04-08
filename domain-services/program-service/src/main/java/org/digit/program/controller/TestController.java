package org.digit.program.controller;

import org.digit.program.models.disburse.Disbursement;
import org.digit.program.service.EncryptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequestMapping("/v1")
public class TestController {
    private final EncryptionService encryptionService;
    @Autowired
    public TestController(EncryptionService encryptionService) {
        this.encryptionService = encryptionService;
    }

    @PostMapping(value = "/disburse/encrypt")
    public ResponseEntity<Disbursement> encryptDisbursement(@RequestBody @Valid Disbursement disbursement) {
        return ResponseEntity.ok(encryptionService.getEncryptedDisbursement(disbursement));
    }
}
