package org.digit.program.service;


import lombok.extern.slf4j.Slf4j;
import org.digit.program.configuration.ProgramConfiguration;
import org.digit.program.models.*;
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

    public RequestJsonMessage createProgram(RequestJsonMessage requestJsonMessage) {
        log.info("create Program");
        commonValidator.validateRequest(requestJsonMessage);
        Program program = new Program(requestJsonMessage.getMessage());
        programValidator.validateProgram(program, true);
        enrichmentService.enrichProgramForCreate(requestJsonMessage.getHeader(), program);
        programRepository.saveProgram(program);
        requestJsonMessage.setMessage(program.toJsonNode(program));
        if (requestJsonMessage.getHeader().getReceiverId().split("@")[1].equalsIgnoreCase(configs.getDomain()))
            dispatcherUtil.sendOnProgram(requestJsonMessage);
        else
            dispatcherUtil.forwardMessage(requestJsonMessage);
        return requestJsonMessage;

    }

    public RequestJsonMessage updateProgram(RequestJsonMessage requestJsonMessage) {
        log.info("update Program");
        commonValidator.validateRequest(requestJsonMessage);
        Program program = new Program(requestJsonMessage.getMessage());
        enrichmentService.enrichProgramForUpdate(program);
        programValidator.validateProgram(program, false);
        programRepository.updateProgram(program);
        requestJsonMessage.setMessage(program.toJsonNode(program));
        if (requestJsonMessage.getHeader().getReceiverId().split("@")[1].equalsIgnoreCase(configs.getDomain()))
            dispatcherUtil.sendOnProgram(requestJsonMessage);
        else
            dispatcherUtil.forwardMessage(requestJsonMessage);
        return requestJsonMessage;
    }

    public ProgramSearchResponse searchProgram(ProgramSearchRequest programSearchRequest) {
        log.info("search Program");
        List<Program> programs = null;
        programs = programRepository.searchProgram(programSearchRequest.getProgramSearch());
        log.info("Found {} programs", programs.size());
        return ProgramSearchResponse.builder().programs(programs).header(programSearchRequest.getHeader()).build();
    }

    public RequestJsonMessage onProgram(RequestJsonMessage requestJsonMessage) {
        log.info("on Program");
        commonValidator.validateRequest(requestJsonMessage);
        Program program = new Program(requestJsonMessage.getMessage());
        programValidator.validateProgram(program, false);
        enrichmentService.enrichProgramForUpdate(program);
        programRepository.updateProgram(program);
        if (!requestJsonMessage.getHeader().getReceiverId().split("@")[1].equalsIgnoreCase(configs.getDomain()))
            dispatcherUtil.forwardMessage(requestJsonMessage);
        return requestJsonMessage;
    }
}
