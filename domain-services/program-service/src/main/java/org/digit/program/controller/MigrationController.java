package org.digit.program.controller;

import org.digit.program.service.MigrationService;
import org.egov.common.contract.request.RequestInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/migration/v1")
public class MigrationController {
    private final MigrationService migrationService;

    @Autowired
    public MigrationController(MigrationService migrationService) {
        this.migrationService = migrationService;
    }

    @RequestMapping(value = "/program/_create", method = RequestMethod.POST)
    public void createProgram() {
        migrationService.createProgram();
    }

    @RequestMapping(value = "/sanction-allocation/_create", method = RequestMethod.POST)
    public void createSanctionAllocation(@Valid @RequestBody RequestInfo requestInfo) {
        migrationService.createSanctionAllocation(requestInfo);
    }
}
