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

    /**
     * Pushes to kafka topic
     * @param programRequest
     * @return
     */
    public ProgramRequest pushToKafka(ProgramRequest programRequest) {
        log.info("Pushing Program to Kafka");
        producer.push(configs.getProgramTopic(), programRequest);
        log.info("Pushed Program to Kafka");
        return programRequest;
    }

    /**
     * Validates program request, enriches the required fields, persists to db and dispatches the program.
     * @param programRequest
     */
    public ProgramRequest createProgram(ProgramRequest programRequest) {
        log.info("Creating Program");
        try {
            programValidator.validateProgram(programRequest.getProgram(), true, false);
            enrichmentService.enrichProgramForCreate(programRequest.getHeader(), programRequest.getProgram());
            programRepository.saveProgram(programRequest.getProgram());
            dispatcherUtil.dispatchProgram(programRequest);
            log.info("Created Program");
        } catch (CustomException exception) {
            log.error("Error while creating program", exception);
            errorHandler.handleProgramError(programRequest, exception);
        }
        return programRequest;
    }

    /**
     * Validates program request, enriches the required fields, persists to db and dispatches the program.
     * @param programRequest
     */
    public ProgramRequest updateProgram(ProgramRequest programRequest) {
        log.info("Updating Program");
        try {
            programValidator.validateProgram(programRequest.getProgram(), false, false);
            enrichmentService.enrichProgramForUpdateOrOnProgram(programRequest.getProgram(), programRequest.getHeader());
            programRepository.updateProgram(programRequest.getProgram(), false);
            dispatcherUtil.dispatchProgram(programRequest);
            log.info("Updated Program");
        } catch (CustomException exception) {
            log.error("Error while updating program", exception);
            errorHandler.handleProgramError(programRequest, exception);
        }
        return programRequest;
    }

    /**
     * Validates url with request header and search for programs in db.
     * @param programSearchRequest
     * @param action
     * @param messageType
     * @return
     */
    public ProgramSearchResponse searchProgram(ProgramSearchRequest programSearchRequest, String action, String messageType) {
        log.info("Searching Program");
        List<Program> programs;
        commonValidator.validateRequest(programSearchRequest.getHeader(), action, messageType);
        programs = programRepository.searchProgram(programSearchRequest.getProgramSearch());
        log.info("Found {} programs", programs.size());
        return ProgramSearchResponse.builder().programs(programs).header(programSearchRequest.getHeader()).build();
    }

    /**
     * Validates program reply, enriches the audit details, persists to db and dispatches the program.
     * @param programRequest
     */
    public ProgramRequest onProgramCreate(ProgramRequest programRequest) {
        log.info("On-Program Create");
        try {
            programValidator.validateProgram(programRequest.getProgram(), false, true);
            commonValidator.validateReply(programRequest.getHeader(), programRequest.getProgram().getLocationCode());
            enrichmentService.enrichProgramForUpdateOrOnProgram(programRequest.getProgram(), programRequest.getHeader());
            programRepository.updateProgram(programRequest.getProgram(), true);
            dispatcherUtil.dispatchOnProgram(programRequest);
            log.info("On-Program Created");
        } catch (CustomException exception) {
            log.error("Error while creating on program", exception);
            errorHandler.handleProgramReplyError(programRequest, exception);
        }
        return programRequest;
    }

    /**
     * Validates program reply, enriches the audit details, persists to db and dispatches the program.
     * @param programRequest
     */
    public ProgramRequest onProgramUpdate(ProgramRequest programRequest) {
        log.info("On-Program Update");
        try {
            programValidator.validateProgram(programRequest.getProgram(), false, false);
            commonValidator.validateReply(programRequest.getHeader(), programRequest.getProgram().getLocationCode());
            enrichmentService.enrichProgramForUpdateOrOnProgram(programRequest.getProgram(), programRequest.getHeader());
            programRepository.updateProgram(programRequest.getProgram(), false);
            dispatcherUtil.dispatchOnProgram(programRequest);
            log.info("On-Program Updated");
        } catch (CustomException exception) {
            log.error("Error while updating on program", exception);
            errorHandler.handleProgramReplyError(programRequest, exception);
        }
        return programRequest;
    }
}
