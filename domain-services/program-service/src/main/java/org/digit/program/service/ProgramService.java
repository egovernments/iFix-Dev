package org.digit.program.service;


import lombok.extern.slf4j.Slf4j;
import org.digit.program.constants.SortOrder;
import org.digit.program.models.*;
import org.digit.program.repository.ProgramRepository;
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

    public ProgramService(ProgramRepository programRepository, EnrichmentService enrichmentService, DispatcherUtil dispatcherUtil) {
        this.programRepository = programRepository;
        this.enrichmentService = enrichmentService;
        this.dispatcherUtil = dispatcherUtil;
    }

    public RequestJsonMessage createProgram(RequestJsonMessage requestJsonMessage) {
        log.info("createProgram");
        Program program = new Program(requestJsonMessage.getMessage());
        enrichmentService.enrichProgramForCreate(program);
        programRepository.save(program);
        dispatcherUtil.sendOnProgram(requestJsonMessage, program);
        return requestJsonMessage;

    }

    public RequestJsonMessage updateProgram(RequestJsonMessage requestJsonMessage) {
        log.info("updateProgram");
        Program program = new Program(requestJsonMessage.getMessage());
        enrichmentService.enrichProgramForUpdate(program);
        programRepository.save(program);
        dispatcherUtil.sendOnProgram(requestJsonMessage, program);
        return requestJsonMessage;
    }

    public List<ExchangeCode> searchProgram(ProgramSearch programSearch) {
        log.info("searchProgram");
        List<ExchangeCode> programs;
        Sort.Direction direction = Sort.Direction.DESC;
        if (programSearch.getPagination().getSortOrder() != null &&
                programSearch.getPagination().getSortOrder().equals(SortOrder.ASC)) {
            direction = Sort.Direction.ASC;
        }
        Sort sort = null;
        if (programSearch.getPagination() != null && programSearch.getPagination().getSortBy() != null)
            sort = Sort.by(new Sort.Order(direction, programSearch.getPagination().getSortBy()));
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
