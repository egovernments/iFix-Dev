package org.digit.program.service;


import lombok.extern.slf4j.Slf4j;
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
    private final ProgramValidator programValidator;
    private final CommonValidator commonValidator;

    public ProgramService(ProgramRepository programRepository, EnrichmentService enrichmentService, DispatcherUtil dispatcherUtil, ProgramValidator programValidator, CommonValidator commonValidator) {
        this.programRepository = programRepository;
        this.enrichmentService = enrichmentService;
        this.dispatcherUtil = dispatcherUtil;
        this.programValidator = programValidator;
        this.commonValidator = commonValidator;
    }

    public ProgramRequest createProgram(ProgramRequest programRequest) {
        log.info("create Program");
        commonValidator.validateRequest(programRequest.getHeader());
        programValidator.validateProgram(programRequest.getProgram(), true, false);
        enrichmentService.enrichProgramForCreate(programRequest.getHeader(), programRequest.getProgram());
        programRepository.saveProgram(programRequest.getProgram());
        dispatcherUtil.dispatchProgram(programRequest);
        return programRequest;

    }

    public ProgramRequest updateProgram(ProgramRequest programRequest) {
        log.info("update Program");
        commonValidator.validateRequest(programRequest.getHeader());
        programValidator.validateProgram(programRequest.getProgram(), false, false);
        enrichmentService.enrichProgramForUpdateOrOnProgram(programRequest.getProgram(),
                programRequest.getHeader().getSenderId());
        programRepository.updateProgram(programRequest.getProgram(), false);
        dispatcherUtil.dispatchProgram(programRequest);
        return programRequest;
    }

    public ProgramSearchResponse searchProgram(ProgramSearchRequest programSearchRequest) {
        log.info("search Program");
        List<Program> programs;
        programs = programRepository.searchProgram(programSearchRequest.getProgramSearch());
        log.info("Found {} programs", programs.size());
        return ProgramSearchResponse.builder().programs(programs).header(programSearchRequest.getHeader()).build();
    }

    public ProgramRequest onProgramCreate(ProgramRequest programRequest) {
        log.info("on Program Create");
        commonValidator.validateRequest(programRequest.getHeader());
        programValidator.validateProgram(programRequest.getProgram(), false, true);
        enrichmentService.enrichProgramForUpdateOrOnProgram(programRequest.getProgram(),
                programRequest.getHeader().getSenderId());
        programRepository.updateProgram(programRequest.getProgram(), true);
        dispatcherUtil.dispatchOnProgram(programRequest);
        return programRequest;
    }

    public ProgramRequest onProgramUpdate(ProgramRequest programRequest) {
        log.info("on Program Update");
        commonValidator.validateRequest(programRequest.getHeader());
        programValidator.validateProgram(programRequest.getProgram(), false, false);
        enrichmentService.enrichProgramForUpdateOrOnProgram(programRequest.getProgram(),
                programRequest.getHeader().getSenderId());
        programRepository.updateProgram(programRequest.getProgram(), false);
        dispatcherUtil.dispatchOnProgram(programRequest);
        return programRequest;
    }
}
