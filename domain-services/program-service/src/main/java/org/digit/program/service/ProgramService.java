package org.digit.program.service;


import lombok.extern.slf4j.Slf4j;
import org.digit.program.configuration.ProgramConfiguration;
import org.digit.program.models.*;
import org.digit.program.repository.ProgramRepository;
import org.digit.program.utils.DispatcherUtil;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ProgramService {
    private final ProgramRepository programRepository;
    private final EnrichmentService enrichmentService;
    private final DispatcherUtil dispatcherUtil;
    private final ProgramConfiguration configs;

    public ProgramService(ProgramRepository programRepository, EnrichmentService enrichmentService, DispatcherUtil dispatcherUtil, ProgramConfiguration configs) {
        this.programRepository = programRepository;
        this.enrichmentService = enrichmentService;
        this.dispatcherUtil = dispatcherUtil;
        this.configs = configs;
    }

    public RequestJsonMessage createProgram(RequestJsonMessage requestJsonMessage) {
        log.info("createProgram");
        Program program = new Program(requestJsonMessage.getMessage());
        enrichmentService.enrichProgramForCreate(requestJsonMessage.getHeader(), program);
        programRepository.saveProgram(program);
        requestJsonMessage.setMessage(program.toJsonNode(program));
        if (requestJsonMessage.getHeader().getReceiverId().split("@")[1].equalsIgnoreCase(configs.getDomain()))
            dispatcherUtil.sendOnProgram(requestJsonMessage);
        else
            dispatcherUtil.forwardMessage(requestJsonMessage, true);
        return requestJsonMessage;

    }

    public RequestJsonMessage updateProgram(RequestJsonMessage requestJsonMessage) {
        log.info("updateProgram");
        Program program = new Program(requestJsonMessage.getMessage());
        enrichmentService.enrichProgramForUpdate(program);
        programRepository.updateProgram(program);
        requestJsonMessage.setMessage(program.toJsonNode(program));
        if (requestJsonMessage.getHeader().getReceiverId().split("@")[1].equalsIgnoreCase(configs.getDomain()))
            dispatcherUtil.sendOnProgram(requestJsonMessage);
        else
            dispatcherUtil.forwardMessage(requestJsonMessage, false);
        return requestJsonMessage;
    }

    public ProgramSearchResponse searchProgram(ProgramSearchRequest programSearchRequest) {
        log.info("searchProgram");
        List<Program> programs = null;
        enrichmentService.enrichProgramForSearch(programSearchRequest.getProgramSearch());
        programs = programRepository.searchProgram(programSearchRequest.getProgramSearch());
        return ProgramSearchResponse.builder().programs(programs).header(programSearchRequest.getHeader()).build();
    }

    public RequestJsonMessage onProgram(RequestJsonMessage requestJsonMessage) {
        log.info("onProgram");
        Program program = new Program(requestJsonMessage.getMessage());
        enrichmentService.enrichProgramForUpdate(program);
        programRepository.updateProgram(program);
        return requestJsonMessage;
    }
}
