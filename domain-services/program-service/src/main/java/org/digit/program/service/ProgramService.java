package org.digit.program.service;


import lombok.extern.slf4j.Slf4j;
import org.digit.program.models.*;
import org.digit.program.repository.ProgramRepository;
import org.digit.program.utils.CommonUtil;
import org.digit.program.utils.DispatcherUtil;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ProgramService {
    private final ProgramRepository programRepository;
    private final EnrichmentService enrichmentService;
    private final DispatcherUtil dispatcherUtil;
    private final CommonUtil commonUtil;

    public ProgramService(ProgramRepository programRepository, EnrichmentService enrichmentService, DispatcherUtil dispatcherUtil, CommonUtil commonUtil) {
        this.programRepository = programRepository;
        this.enrichmentService = enrichmentService;
        this.dispatcherUtil = dispatcherUtil;
        this.commonUtil = commonUtil;
    }

    public RequestJsonMessage createProgram(RequestJsonMessage requestJsonMessage) {
        log.info("createProgram");
        Program program = new Program(requestJsonMessage.getMessage());
        enrichmentService.enrichProgramForCreate(program);
        programRepository.save(program);
        dispatcherUtil.sendOnProgram(requestJsonMessage, program);
        requestJsonMessage.setMessage(program.toJsonNode(program));
        return requestJsonMessage;

    }

    public RequestJsonMessage updateProgram(RequestJsonMessage requestJsonMessage) {
        log.info("updateProgram");
        Program program = new Program(requestJsonMessage.getMessage());
        enrichmentService.enrichProgramForUpdate(program);
        programRepository.save(program);
        dispatcherUtil.sendOnProgram(requestJsonMessage, program);
        requestJsonMessage.setMessage(program.toJsonNode(program));
        return requestJsonMessage;
    }

    public List<ExchangeCode> searchProgram(ProgramSearch programSearch) {
        log.info("searchProgram");
        List<ExchangeCode> programs;
        Sort sort = commonUtil.getPagination(programSearch.getPagination());
        programs = programRepository.findByCriteria(programSearch.getIds(), programSearch.getName(), programSearch.getParentId(),
                programSearch.getProgramCode(), programSearch.getLocationCode(), sort);
        return programs;
    }

    public RequestJsonMessage onProgram(RequestJsonMessage requestJsonMessage) {
        log.info("onProgram");
        Program program = new Program(requestJsonMessage.getMessage());
        programRepository.save(program);
        return requestJsonMessage;
    }
}
