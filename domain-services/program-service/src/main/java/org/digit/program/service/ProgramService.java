package org.digit.program.service;


import lombok.extern.slf4j.Slf4j;
import org.digit.program.configuration.ProgramConfiguration;
import org.digit.program.models.program.Program;
import org.digit.program.models.program.ProgramRequest;
import org.digit.program.models.program.ProgramSearchRequest;
import org.digit.program.models.program.ProgramSearchResponse;
import org.digit.program.repository.ProgramRepository;
import org.digit.program.utils.DispatcherUtil;
import org.digit.program.validator.CommonValidator;
import org.digit.program.validator.ProgramValidator;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ProgramService {
    private final ProgramRepository programRepository;
    private final EnrichmentService enrichmentService;
    private final DispatcherUtil dispatcherUtil;
    private final ProgramConfiguration configs;
    private final ProgramValidator programValidator;
    private final CommonValidator commonValidator;

    public ProgramService(ProgramRepository programRepository, EnrichmentService enrichmentService, DispatcherUtil dispatcherUtil, ProgramConfiguration configs, ProgramValidator programValidator, CommonValidator commonValidator) {
        this.programRepository = programRepository;
        this.enrichmentService = enrichmentService;
        this.dispatcherUtil = dispatcherUtil;
        this.configs = configs;
        this.programValidator = programValidator;
        this.commonValidator = commonValidator;
    }

    public ProgramRequest createProgram(ProgramRequest programRequest) {
        log.info("create Program");
        commonValidator.validateRequest(programRequest.getHeader());
        programValidator.validateProgram(programRequest.getProgram(), true);
        enrichmentService.enrichProgramForCreate(programRequest.getHeader(), programRequest.getProgram());
        programRepository.saveProgram(programRequest.getProgram());
        if (programRequest.getHeader().getReceiverId().split("@")[1].equalsIgnoreCase(configs.getDomain()))
            dispatcherUtil.sendOnProgram(programRequest);
        else
            dispatcherUtil.forwardMessage(programRequest.getId(), programRequest.getSignature(),
                    programRequest.getHeader(), programRequest.getProgram().toString());
        return programRequest;

    }

    public ProgramRequest updateProgram(ProgramRequest programRequest) {
        log.info("update Program");
        commonValidator.validateRequest(programRequest.getHeader());
        enrichmentService.enrichProgramForUpdate(programRequest.getProgram());
        programValidator.validateProgram(programRequest.getProgram(), false);
        programRepository.updateProgram(programRequest.getProgram());
        if (programRequest.getHeader().getReceiverId().split("@")[1].equalsIgnoreCase(configs.getDomain()))
            dispatcherUtil.sendOnProgram(programRequest);
        else
            dispatcherUtil.forwardMessage(programRequest.getId(), programRequest.getSignature(),
                    programRequest.getHeader(), programRequest.getProgram().toString());
        return programRequest;
    }

    public ProgramSearchResponse searchProgram(ProgramSearchRequest programSearchRequest) {
        log.info("search Program");
        List<Program> programs = null;
        programs = programRepository.searchProgram(programSearchRequest.getProgramSearch());
        log.info("Found {} programs", programs.size());
        return ProgramSearchResponse.builder().programs(programs).header(programSearchRequest.getHeader()).build();
    }

    public ProgramRequest onProgram(ProgramRequest programRequest) {
        log.info("on Program");
        commonValidator.validateRequest(programRequest.getHeader());
        programValidator.validateProgram(programRequest.getProgram(), false);
        enrichmentService.enrichProgramForOnProgram(programRequest.getProgram());
        programRepository.updateProgram(programRequest.getProgram());
        if (!programRequest.getHeader().getReceiverId().split("@")[1].equalsIgnoreCase(configs.getDomain()))
            dispatcherUtil.forwardMessage(programRequest.getId(), programRequest.getSignature(),
                    programRequest.getHeader(), programRequest.getProgram().toString());
        return programRequest;
    }
}
