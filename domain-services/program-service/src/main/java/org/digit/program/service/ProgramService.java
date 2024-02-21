package org.digit.program.service;


import lombok.extern.slf4j.Slf4j;
import org.digit.program.configuration.ProgramConfiguration;
import org.digit.program.kafka.ProgramProducer;
import org.digit.program.models.program.Program;
import org.digit.program.models.program.ProgramRequest;
import org.digit.program.models.program.ProgramSearchRequest;
import org.digit.program.models.program.ProgramSearchResponse;
import org.digit.program.repository.ProgramRepository;
import org.digit.program.utils.DispatcherUtil;
import org.digit.program.utils.ErrorHandler;
import org.digit.program.validator.CommonValidator;
import org.digit.program.validator.ProgramValidator;
import org.egov.tracer.model.CustomException;
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
    private final ProgramProducer producer;
    private final ProgramConfiguration configs;
    private final ErrorHandler errorHandler;

    public ProgramService(ProgramRepository programRepository, EnrichmentService enrichmentService,
                          DispatcherUtil dispatcherUtil, ProgramValidator programValidator,
                          CommonValidator commonValidator, ProgramProducer producer, ProgramConfiguration configs, ErrorHandler errorHandler) {
        this.programRepository = programRepository;
        this.enrichmentService = enrichmentService;
        this.dispatcherUtil = dispatcherUtil;
        this.programValidator = programValidator;
        this.commonValidator = commonValidator;
        this.producer = producer;
        this.configs = configs;
        this.errorHandler = errorHandler;
    }

    public ProgramRequest pushToKafka(ProgramRequest programRequest, String action, String messageType) {
        log.info("push Program to Kafka");
        commonValidator.validateRequest(programRequest.getHeader(), action, messageType);
        producer.push(configs.getProgramTopic(), programRequest);
        return programRequest;
    }

    public void createProgram(ProgramRequest programRequest) {
        log.info("create Program");
        try {
            programValidator.validateProgram(programRequest.getProgram(), true, false);
            enrichmentService.enrichProgramForCreate(programRequest.getHeader(), programRequest.getProgram());
            programRepository.saveProgram(programRequest.getProgram());
            dispatcherUtil.dispatchProgram(programRequest);
        } catch (CustomException exception) {
            errorHandler.handleProgramError(programRequest, exception);
        }
    }

    public void updateProgram(ProgramRequest programRequest) {
        log.info("update Program");
        try {
            programValidator.validateProgram(programRequest.getProgram(), false, false);
            enrichmentService.enrichProgramForUpdateOrOnProgram(programRequest.getProgram(),
                    programRequest.getHeader().getSenderId());
            programRepository.updateProgram(programRequest.getProgram(), false);
            dispatcherUtil.dispatchProgram(programRequest);
        } catch (CustomException exception) {
            errorHandler.handleProgramError(programRequest, exception);
        }
    }

    public ProgramSearchResponse searchProgram(ProgramSearchRequest programSearchRequest, String action, String messageType) {
        log.info("search Program");
        List<Program> programs;
        commonValidator.validateRequest(programSearchRequest.getHeader(), action, messageType);
        programs = programRepository.searchProgram(programSearchRequest.getProgramSearch());
        log.info("Found {} programs", programs.size());
        return ProgramSearchResponse.builder().programs(programs).header(programSearchRequest.getHeader()).build();
    }

    public void onProgramCreate(ProgramRequest programRequest) {
        log.info("on Program Create");
        try {
            programValidator.validateProgram(programRequest.getProgram(), false, true);
            commonValidator.validateReplyForProgramCreate(programRequest.getHeader(), programRequest.getProgram().getId(),
                    programRequest.getProgram().getLocationCode());
            enrichmentService.enrichProgramForUpdateOrOnProgram(programRequest.getProgram(),
                    programRequest.getHeader().getSenderId());
            programRepository.updateProgram(programRequest.getProgram(), true);
            dispatcherUtil.dispatchOnProgram(programRequest);
        } catch (CustomException exception) {
            errorHandler.handleProgramReplyError(programRequest, exception);
        }

    }

    public void onProgramUpdate(ProgramRequest programRequest) {
        log.info("on Program Update");
        try {
            programValidator.validateProgram(programRequest.getProgram(), false, false);
            commonValidator.validateReply(programRequest.getHeader(), programRequest.getProgram().getProgramCode(),
                    programRequest.getProgram().getLocationCode());
            enrichmentService.enrichProgramForUpdateOrOnProgram(programRequest.getProgram(),
                    programRequest.getHeader().getSenderId());
            programRepository.updateProgram(programRequest.getProgram(), false);
            dispatcherUtil.dispatchOnProgram(programRequest);
        } catch (CustomException exception) {
            errorHandler.handleProgramReplyError(programRequest, exception);
        }

    }
}
