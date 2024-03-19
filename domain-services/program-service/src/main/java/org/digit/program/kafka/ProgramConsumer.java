package org.digit.program.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.digit.program.constants.MessageType;
import org.digit.program.models.allocation.AllocationRequest;
import org.digit.program.models.disburse.DisbursementRequest;
import org.digit.program.models.program.ProgramRequest;
import org.digit.program.models.sanction.SanctionRequest;
import org.digit.program.service.AllocationService;
import org.digit.program.service.DisburseService;
import org.digit.program.service.ProgramService;
import org.digit.program.service.SanctionService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ProgramConsumer {

    private final ObjectMapper mapper;
    private final ProgramProducer producer;
    private final ProgramService programService;
    private final SanctionService sanctionService;
    private final AllocationService allocationService;
    private final DisburseService disburseService;
    @Value("${error.kafka.topic}")
    private String errorTopic;

    public ProgramConsumer(ObjectMapper mapper,  ProgramProducer producer, ProgramService programService,
                           SanctionService sanctionService, AllocationService allocationService,
                           DisburseService disburseService) {
        this.mapper = mapper;
        this.producer = producer;
        this.programService = programService;
        this.sanctionService = sanctionService;
        this.allocationService = allocationService;
        this.disburseService = disburseService;
    }

    @KafkaListener(topics = {"${program.kafka.topic}"})
    public void listenProgram(final String receivedObject, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
        log.info("Consumed message from topic: {}", topic);
        log.info("Received message: {}", receivedObject);
        try {
            ProgramRequest programRequest = mapper.readValue(receivedObject, ProgramRequest.class);
            if (programRequest.getHeader().getMessageType().equals(MessageType.PROGRAM)) {
                switch (programRequest.getHeader().getAction()) {
                    case CREATE:
                        programService.createProgram(programRequest);
                        break;
                    case UPDATE:
                        programService.updateProgram(programRequest);
                        break;
                    default:
                        producer.push(errorTopic, receivedObject);
                }
            } else if (programRequest.getHeader().getMessageType().equals(MessageType.ON_PROGRAM)) {
                switch (programRequest.getHeader().getAction()) {
                    case CREATE:
                        programService.onProgramCreate(programRequest);
                        break;
                    case UPDATE:
                        programService.onProgramUpdate(programRequest);
                        break;
                    default:
                        producer.push(errorTopic, receivedObject);
                        break;
                }
            }
        } catch (Exception e) {
            producer.push(errorTopic, receivedObject);
            log.error("Error: ", e);
        }
    }

    @KafkaListener(topics = {"${sanction.kafka.topic}"})
    public void listenSanction(final String receivedObject,  @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
        log.info("Consumed message from topic: {}", topic);
        log.info("Received message: {}", receivedObject);
        try {
            SanctionRequest sanctionRequest = mapper.readValue(receivedObject, SanctionRequest.class);
            switch (sanctionRequest.getHeader().getAction()) {
                case CREATE:
                    sanctionService.createSanction(sanctionRequest);
                    break;
                case UPDATE:
                    sanctionService.updateSanction(sanctionRequest);
                    break;
                default:
                    producer.push(errorTopic, receivedObject);
                    break;
            }
        } catch ( Exception e) {
            producer.push(errorTopic, receivedObject);
            log.error("Error: ", e);
        }


    }
    @KafkaListener(topics = {"${allocation.kafka.topic}"})
    public void listenAllocation(final String receivedObject,  @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
        log.info("Consumed message from topic: {}", topic);
        log.info("Received message: {}", receivedObject);
        try {
            AllocationRequest allocationRequest = mapper.readValue(receivedObject, AllocationRequest.class);
            switch (allocationRequest.getHeader().getAction()) {
                case CREATE:
                    allocationService.createAllocation(allocationRequest);
                    break;
                case UPDATE:
                    allocationService.updateAllocation(allocationRequest);
                    break;
                default:
                    producer.push(errorTopic, receivedObject);
                    break;
            }
        } catch (Exception e) {
            producer.push(errorTopic, receivedObject);
            log.error("Error: ", e);
        }
    }

    @KafkaListener(topics = {"${disburse.kafka.topic}"})
    public void listenDisburse(final String receivedObject,  @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
        log.info("Consumed message from topic: {}", topic);
        log.info("Received message: {}", receivedObject);
        try {
            DisbursementRequest disbursementRequest = mapper.readValue(receivedObject, DisbursementRequest.class);
            if(disbursementRequest.getHeader().getMessageType().equals(MessageType.DISBURSE)) {
                switch (disbursementRequest.getHeader().getAction()) {
                    case CREATE:
                        disburseService.createDisburse(disbursementRequest);
                        break;
                    case UPDATE:
                        disburseService.updateDisburse(disbursementRequest);
                        break;
                    default:
                        producer.push(errorTopic, receivedObject);
                        break;
                }
            } else if (disbursementRequest.getHeader().getMessageType().equals(MessageType.ON_DISBURSE)) {
                switch (disbursementRequest.getHeader().getAction()) {
                    case CREATE:
                        disburseService.onDisburseCreate(disbursementRequest);
                        break;
                    case UPDATE:
                        disburseService.onDisburseUpdate(disbursementRequest);
                        break;
                    default:
                        producer.push(errorTopic, receivedObject);
                        break;
                }
            }
        } catch (Exception e) {
            producer.push(errorTopic, receivedObject);
            log.error("Error: ", e);
        }
    }

    @KafkaListener(topics = {"${error.kafka.topic}"})
    public void listenError(final String receivedObject,  @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
        log.info("Consumed message from topic: {}", topic);
        log.info("Received message: {}", receivedObject);
    }
}
